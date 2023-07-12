package com.sven.rainbowbeachlib

import android.annotation.SuppressLint
import android.app.Activity
import android.app.Application
import android.content.Intent
import android.os.Bundle
import com.sven.rainbowbeachlib.tools.RbbLogUtils
import com.sven.rainbowbeachlib.view.StartActivity

/**
 * @Author:         xwp
 * @CreateDate:     2023/6/24
 * @Version:        1.0
 */

@SuppressLint("StaticFieldLeak")
object RainbowBeach {
    var topActivity: Activity? = null

    fun start(activity: Activity) {
        registerActivityListener(activity.application)
        activity.startActivity(Intent(activity, StartActivity::class.java))
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
                if (!activity::class.java.name.contains("com.sven.rainbowbeachlib")) {
                    RbbLogUtils.logInfo("RainbowBeach onActivityResumed activity = $activity")
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
            }
        })
    }
}