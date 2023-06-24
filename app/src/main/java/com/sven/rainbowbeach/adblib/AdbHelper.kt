package com.sven.rainbowbeach.adblib

import android.content.Context
import android.content.res.AssetManager
import android.util.Base64
import com.sven.rainbowbeach.util.LogUtils
import java.io.File
import java.io.IOException
import java.net.Socket
import java.security.NoSuchAlgorithmException
import java.util.concurrent.ArrayBlockingQueue
import java.util.concurrent.atomic.AtomicBoolean


class AdbHelper {
    companion object {
        const val TAG = "AdbHelper"
    }

    private var adbCrypto: AdbCrypto? = null
    private lateinit var mContext: Context
    private var isAdbConnected = AtomicBoolean(false)
    private var adbConnection: AdbConnection? = null
    private val commandBlockQueue by lazy {
        ArrayBlockingQueue<String>(100)
    }

    fun start(context: Context) {
        mContext = context
        setupCrypto(context)
        asyncRefreshAdbConnection()
    }

    fun stop() {
        commandBlockQueue.clear()
        isAdbConnected.set(false)
    }


    fun addCommand(command: String) {
        commandBlockQueue.put(command)
    }

    private fun asyncRefreshAdbConnection() {
        LogUtils.i("asyncRefreshAdbConnection ...")
        object : Thread() {
            override fun run() {
                try {
                    if (adbConnection != null) {
                        adbConnection?.close()
                        adbConnection = null
                    }
                    // 找到UsbDevice对应的UsbInterface
                    val sock = Socket("127.0.0.1", 5555)
                    adbConnection = AdbConnection.create(TcpChannel(sock), adbCrypto)
                    adbConnection?.connect()
                    isAdbConnected.set(true)
                    execCommand()
                } catch (e: Exception) {
                    e.printStackTrace()
                }
            }
        }.start()
    }


    @Throws(NoSuchAlgorithmException::class, IOException::class)
    private fun setupCrypto(context: Context) {
        val base64: AdbBase64 = MyAdbBase64()
        try {
            adbCrypto = AdbCrypto.loadAdbKeyPair(
                base64,
                File(context.filesDir, "private_key"),
                File(context.filesDir, "public_key")
            )
        } catch (e: Exception) {
            e.printStackTrace()
        }

        if (adbCrypto == null) {
            try {
                adbCrypto = AdbCrypto.generateAdbKeyPair(base64)
                adbCrypto?.saveAdbKeyPair(
                    File(context.filesDir, "private_key"),
                    File(context.filesDir, "public_key")
                )
            } catch (e: Exception) {
                LogUtils.i("fail to generate and save key-pair = $e")
            }
        }
    }


    private fun execCommand() {
        while (isAdbConnected.get()) {
            val commandStr = commandBlockQueue.take()
            adbConnection?.let {
                ServiceCommand.excCommand(it, commandStr)
            }
        }
    }


    @Synchronized
    private fun execOsServer() {
        adbConnection ?: return
        val assetManager: AssetManager = mContext.assets
        val inputStream = assetManager.open("mycarserver.jar")
        val buffer = ByteArray(inputStream.available())
        inputStream.read(buffer)
        val fileBase64 = Base64.encode(buffer, 2)
        val excCommand = ServiceCommand.excCommand(adbConnection, fileBase64, 1280L, 4096000L)
        if (excCommand) {
            LogUtils.i("execOsServer success")
        } else {
            LogUtils.i("execOsServer failed")
        }
    }
}
