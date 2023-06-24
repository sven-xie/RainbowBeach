package com.sven.rainbowbeach.conn

import com.king.asocket.ASocket
import com.king.asocket.ISocket
import com.king.asocket.tcp.TCPServer
import com.sven.rainbowbeach.util.LogUtils

/**
 * @Author:         xwp
 * @CreateDate:     2023/6/24
 * @Version:        1.0
 */
class RainbowBeach {

    private var aSocket: ASocket? = null

    companion object {
        const val PORT = 7009
    }

    fun start() {
        val client = TCPServer(PORT)
        aSocket = ASocket(client)
        aSocket?.let {
            it.setOnSocketStateListener(object : ISocket.OnSocketStateListener {
                override fun onStarted() {
                    LogUtils.i("RainbowBeach 连接已开启")
                }

                override fun onClosed() {
                    LogUtils.i("RainbowBeach 连接已关闭")
                }

                override fun onException(e: Exception) {
                    LogUtils.i("RainbowBeach 连接异常： $e")
                }

            })
            it.setOnMessageReceivedListener { data ->
                LogUtils.i("RainbowBeach 接收：${String(data)}")
            }
            it.start()
        }
    }

    fun stop() {
        aSocket?.closeAndQuit()
    }

    fun sendCommand(command: ByteArray) {
        aSocket?.write(command)
    }
}