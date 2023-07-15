package com.sven.rainbowbeachlib.tools

import android.annotation.SuppressLint
import android.content.Context
import android.net.wifi.WifiInfo
import android.net.wifi.WifiManager
import android.util.Log
import android.widget.Toast
import java.net.Inet6Address
import java.net.InetAddress
import java.net.NetworkInterface
import java.util.*

/**
 * @Author:         xwp
 * @CreateDate:     2023/7/10
 * @Version:        1.0
 */
object RbbUtils {

    fun showToast(context: Context, message: String) {
        Toast.makeText(context, message, Toast.LENGTH_SHORT).show()
    }


    fun logInfo(message: String) {
        Log.i("RainbowBeach", message)
    }

}