package com.sven.rainbowbeachlib

import android.annotation.SuppressLint
import android.app.Activity
import android.app.Application
import android.content.Context
import android.os.Bundle
import com.sven.rainbowbeachlib.service.LocalFileServer
import com.sven.rainbowbeachlib.tools.RbbLogUtils

/**
 * @Author:         xwp
 * @CreateDate:     2023/6/24
 * @Version:        1.0
 */

@SuppressLint("StaticFieldLeak")
object RainbowBeach {

    const val ACTIVITY_FILTER_NAME_PRE = "com.sven.rainbowbeachlib"

    var currentNeedCheckActivity: Activity? = null
    var topActivity: Activity? = null
    private val mLocalFileServer by lazy {
        LocalFileServer()
    }


    fun start(context: Context) {
        val applicationContext: Context = context.applicationContext
        if (applicationContext is Application) {
            registerActivityListener(applicationContext)
        }
        mLocalFileServer.start(context)
    }


    private fun registerActivityListener(application: Application) {
        application.registerActivityLifecycleCallbacks(object :
            Application.ActivityLifecycleCallbacks {
            override fun onActivityCreated(activity: Activity, savedInstanceState: Bundle?) {

            }

            override fun onActivityStarted(activity: Activity) {
            }

            override fun onActivityResumed(activity: Activity) {
                topActivity = activity
                if (!activity::class.java.name.contains(ACTIVITY_FILTER_NAME_PRE)) {
                    RbbLogUtils.logInfo("RainbowBeach onActivityResumed activity = $activity")
                    currentNeedCheckActivity = activity
                }
            }

            override fun onActivityPaused(activity: Activity) {
            }

            override fun onActivityStopped(activity: Activity) {
                topActivity = null
                if (!activity::class.java.name.contains(ACTIVITY_FILTER_NAME_PRE)) {
                    RbbLogUtils.logInfo("RainbowBeach onActivityPaused activity = $activity")
                    currentNeedCheckActivity = null
                }
            }

            override fun onActivitySaveInstanceState(activity: Activity, outState: Bundle) {
            }

            override fun onActivityDestroyed(activity: Activity) {
            }
        })
    }
}