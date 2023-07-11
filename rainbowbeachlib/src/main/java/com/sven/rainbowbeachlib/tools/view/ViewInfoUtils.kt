package com.sven.rainbowbeachlib.tools.view

import android.app.Activity
import android.graphics.Rect
import android.view.View
import android.view.ViewGroup
import androidx.core.view.children
import androidx.fragment.app.Fragment
import androidx.fragment.app.FragmentActivity
import com.sven.rainbowbeachlib.RainbowBeach
import com.sven.rainbowbeachlib.bean.ViewInfoBean
import com.sven.rainbowbeachlib.tools.RbbLogUtils

/**
 * @Author:         xwp
 * @CreateDate:     2023/7/7
 * @Version:        1.0
 */
object ViewInfoUtils {

    private var lastTime = 0L

    fun getViewInfo(rawX: Float, rawY: Float): MutableList<ViewInfoBean>? {
        val allViewInfo = getAllViewInfo()
        allViewInfo?.let {
            val iterator = it.iterator()
            while (iterator.hasNext()) {
                val infoBean = iterator.next()
                infoBean.checkStatus = ViewInfoBean.CHECK_STATUS_UNCHECK
                if (!isInRect(rawX, rawY, infoBean.rect)) {
                    iterator.remove()
                }
            }
        }
        return allViewInfo
    }

    fun getAllViewInfo(): MutableList<ViewInfoBean>? {
        val activity = RainbowBeach.topActivity
        activity ?: return null
        RbbLogUtils.logInfo("getViewInfo start >>> ")
        lastTime = System.currentTimeMillis()
        val activityRootView =
            activity.window.decorView.findViewById<View>(android.R.id.content) as ViewGroup

        val viewInfoList = mutableListOf<ViewInfoBean>()

        getViewRectInfo(
            activity,
            mutableListOf(activityRootView),
            viewInfoList,
            activity::class.java.name
        )

        val actFragments = getActFragments(activity)

        getFragmentViewRectInfos(activity, actFragments, viewInfoList)

        RbbLogUtils.logInfo("getViewInfo end >>> time = ${System.currentTimeMillis() - lastTime}")

        RbbLogUtils.logInfo("viewInfoList = $viewInfoList")
        viewInfoList.reverse()
        return viewInfoList
    }


    private fun getFragmentViewRectInfos(
        activity: Activity,
        actFragments: List<Fragment>,
        viewInfoList: MutableList<ViewInfoBean>
    ) {
        actFragments.forEach { fragment ->
            fragment.view?.let { rootView ->
                if (rootView is ViewGroup) {
                    getViewRectInfo(
                        activity,
                        mutableListOf(rootView),
                        viewInfoList,
                        fragment::class.java.name
                    )
                } else {
                    val viewId = rootView.id
                    if (viewId != 0) {
                        try {
                            val viewStrId = activity.resources.getResourceEntryName(viewId)
                            val rect = Rect()
                            rootView.getGlobalVisibleRect(rect)
                            val viewInfoBean =
                                ViewInfoBean(
                                    viewStrId,
                                    rect,
                                    view = rootView,
                                    attachPageName = fragment::class.java.name
                                )
                            viewInfoList.add(viewInfoBean)
                        } catch (e: Exception) {
                            e.printStackTrace()
                        }
                    }
                }
            }
        }
    }


    private fun getViewRectInfo(
        activity: Activity,
        viewGroupList: MutableList<ViewGroup>,
        viewInfoList: MutableList<ViewInfoBean>,
        attachName: String
    ) {
        val nextViewGroups = mutableListOf<ViewGroup>()
        if (viewGroupList.size <= 0) {
            return
        }
        viewGroupList.forEach { viewGroup ->
            viewGroup.children.forEach {
                val viewId = it.id
                if (viewId != 0) {
                    try {
                        val viewStrId = activity.resources.getResourceEntryName(viewId)
                        val rect = Rect()
                        it.getGlobalVisibleRect(rect)
                        val viewInfoBean =
                            ViewInfoBean(viewStrId, rect, attachName, view = it)
                        viewInfoList.add(viewInfoBean)
                    } catch (e: Exception) {
                        e.printStackTrace()
                    }
                }
                if (it is ViewGroup) {
                    nextViewGroups.add(it)
                }
            }
        }
        getViewRectInfo(activity, nextViewGroups, viewInfoList, attachName)
    }

    private fun getActFragments(activity: Activity): List<Fragment> {
        val fragments = mutableListOf<Fragment>()
        if (activity is FragmentActivity) {
            val actFragments = activity.supportFragmentManager.fragments
            for (i in actFragments.indices) {
                val fragment = actFragments[i]
                if (fragment != null && fragment.isAdded && fragment.isVisible) {
                    fragments.add(fragment)
                    getFragFragments(fragment, fragments)
                }
            }
        }
        return fragments
    }


    private fun getFragFragments(fragment: Fragment, fragments: MutableList<Fragment>) {
        val tempFragments = fragment.childFragmentManager.fragments
        for (i in tempFragments.indices) {
            val tempFragment = tempFragments[i]
            if (tempFragment != null && tempFragment.isAdded && tempFragment.isVisible) {
                fragments.add(tempFragment)
            }
        }
    }

    private fun isInRect(x: Float, y: Float, r: Rect): Boolean {
        if (x > r.left && x < r.right && y > r.top && y < r.bottom) {
            return true;
        }
        return false
    }
}