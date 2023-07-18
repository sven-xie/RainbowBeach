package com.sven.rainbowbeachlib

import android.annotation.SuppressLint
import android.app.Activity
import android.app.Application
import android.content.Context
import android.os.Bundle
import com.sven.rainbowbeachlib.service.LocalFileServer

/**
 * @Author:         xwp
 * @CreateDate:     2023/6/24
 * @Version:        1.0
 */

@SuppressLint("StaticFieldLeak")
object RainbowBeach {

    const val ACTIVITY_FILTER_NAME_PRE = "com.sven.rainbowbeachlib"

    private val actList = mutableListOf<Activity>()

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
                actList.add(0, activity)
            }

            override fun onActivityStarted(activity: Activity) {
            }

            override fun onActivityResumed(activity: Activity) {
            }

            override fun onActivityPaused(activity: Activity) {
            }

            override fun onActivityStopped(activity: Activity) {
            }

            override fun onActivitySaveInstanceState(activity: Activity, outState: Bundle) {
            }

            override fun onActivityDestroyed(activity: Activity) {
                actList.remove(activity)
            }
        })
    }


    fun getCurrentNeedCheckActivity(): Activity? {
        actList.forEach {
            if (!it::class.java.name.contains(ACTIVITY_FILTER_NAME_PRE)) {
                return it;
            }
        }
        return null
    }


    fun getTopActivity(): Activity? {
        if (actList.size > 0) {
            return actList[0]
        }
        return null
    }
}