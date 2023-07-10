package com.sven.rainbowbeachlib

import android.annotation.SuppressLint
import android.app.Activity
import android.app.Application
import android.os.Bundle
import com.king.asocket.ASocket
import com.king.asocket.ISocket
import com.king.asocket.tcp.TCPClient
import com.sven.rainbowbeachlib.tools.RbbLogUtils

/**
 * @Author:         xwp
 * @CreateDate:     2023/6/24
 * @Version:        1.0
 */

@SuppressLint("StaticFieldLeak")
object RainbowBeach {
    const val PORT = 7009
    private var aSocket: ASocket? = null
    var topActivity: Activity? = null

    fun start(application: Application) {
        registerActivityListener(application)

        val client = TCPClient("172.0.0.1", PORT)
        aSocket = ASocket(client)
        aSocket?.let {
            it.setOnSocketStateListener(object : ISocket.OnSocketStateListener {
                override fun onStarted() {
                    RbbLogUtils.logInfo("RainbowBeach 连接已开启")
                }

                override fun onClosed() {
                    RbbLogUtils.logInfo("RainbowBeach 连接已关闭")
                }

                override fun onException(e: Exception) {
                    RbbLogUtils.logInfo("RainbowBeach 连接异常： $e")
                }

            })
            it.setOnMessageReceivedListener { data ->
                RbbLogUtils.logInfo("RainbowBeach 接收：${String(data)}")
            }
            it.start()
        }
    }

    fun stop() {
        topActivity = null
        aSocket?.closeAndQuit()
    }

    fun sendCommand(command: ByteArray) {
        aSocket?.write(command)
    }


    private fun registerActivityListener(application: Application) {
        application.registerActivityLifecycleCallbacks(object :
            Application.ActivityLifecycleCallbacks {
            override fun onActivityCreated(activity: Activity, savedInstanceState: Bundle?) {

            }

            override fun onActivityStarted(activity: Activity) {
            }

            override fun onActivityResumed(activity: Activity) {
                RbbLogUtils.logInfo("RainbowBeach onActivityResumed activity = $activity")
                if (activity::class.java.simpleName != "CheckViewActivity") {
                    topActivity = activity
                }
            }

            override fun onActivityPaused(activity: Activity) {

            }

            override fun onActivityStopped(activity: Activity) {

            }

            override fun onActivitySaveInstanceState(activity: Activity, outState: Bundle) {
            }

            override fun onActivityDestroyed(activity: Activity) {
                if (activity::class.java.simpleName != "CheckViewActivity") {
                    topActivity = activity
                }
            }
        })
    }
}