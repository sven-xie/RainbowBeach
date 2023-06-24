package com.sven.rainbowbeach.util

import android.util.Log
import com.sven.rainbowbeach.BuildConfig

/**
 * @Author:         xwp
 * @CreateDate:     2023/6/24
 * @Version:        1.0
 */
object LogUtils {
    fun i(msg: String) {
        if (BuildConfig.DEBUG) {
            Log.i("RainbowBeach", ">> $msg")
        }
    }
}