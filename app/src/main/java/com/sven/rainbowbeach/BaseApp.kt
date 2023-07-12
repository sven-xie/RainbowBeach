package com.sven.rainbowbeach

import android.app.Application
import com.sven.rainbowbeach.util.Utils
import com.sven.rainbowbeachlib.RainbowBeach

/**
 * @Author:         xwp
 * @CreateDate:     2023/6/24
 * @Version:        1.0
 */
class BaseApp : Application() {


    override fun onCreate() {
        super.onCreate()
        Utils.init(this)
    }
}