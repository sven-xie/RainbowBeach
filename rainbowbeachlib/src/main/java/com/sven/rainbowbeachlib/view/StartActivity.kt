package com.sven.rainbowbeachlib.view

import android.content.Context
import android.content.Intent
import android.os.Build
import android.os.Bundle
import android.provider.Settings
import androidx.fragment.app.FragmentActivity
import com.hjq.permissions.OnPermissionCallback
import com.hjq.permissions.Permission
import com.hjq.permissions.XXPermissions
import com.sven.rainbowbeachlib.service.FloatService
import com.sven.rainbowbeachlib.tools.RbbUtils

/**
 * @Author:         xwp
 * @CreateDate:     2023/7/11
 * @Version:        1.0
 */
class StartActivity : FragmentActivity() {

    private lateinit var mContext: Context
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        mContext = this

        if (checkOverlayPermission(mContext)) {
            startService(Intent(mContext, FloatService::class.java))
        } else {
            XXPermissions.with(this) //申请悬浮窗权限
                .permission(Permission.SYSTEM_ALERT_WINDOW)
                .request(object : OnPermissionCallback {
                    override fun onGranted(permissions: List<String?>?, all: Boolean) {
                        if (!all) {
                            RbbUtils.showToast(mContext, "请务必开启【浮悬窗】权限")
                        } else {
                            startService(Intent(mContext, FloatService::class.java))
                        }
                        finish()
                    }

                    override fun onDenied(permissions: List<String?>?, never: Boolean) {
                        RbbUtils.showToast(mContext, "请务必开启【浮悬窗】权限")
                        finish()
                    }
                })
        }
    }


    private fun checkOverlayPermission(context: Context?): Boolean {
        try {
            if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.M) {
                return Settings.canDrawOverlays(context)
            }
        } catch (e: Throwable) {
            e.printStackTrace()
        }
        return true
    }
}