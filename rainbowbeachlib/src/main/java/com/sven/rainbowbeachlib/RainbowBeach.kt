package com.sven.rainbowbeachlib

import android.annotation.SuppressLint
import android.app.Activity
import android.app.Application
import android.os.Bundle
import com.sven.rainbowbeachlib.tools.RbbLogUtils

/**
 * @Author:         xwp
 * @CreateDate:     2023/6/24
 * @Version:        1.0
 */

@SuppressLint("StaticFieldLeak")
object RainbowBeach {
    var topActivity: Activity? = null

    fun start(application: Application) {
        registerActivityListener(application)
    }

    fun stop() {
        topActivity = null
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
                if (!activity::class.java.name.contains("com.sven.rainbowbeachlib")) {
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
                if (!activity::class.java.name.contains("com.sven.rainbowbeachlib")) {
                    topActivity = null
                }
            }
        })
    }
}