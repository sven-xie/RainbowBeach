package com.sven.rainbowbeachlib.view

import android.content.Context
import android.content.Intent
import android.net.Uri
import android.os.Build
import android.os.Bundle
import android.provider.Settings
import androidx.fragment.app.FragmentActivity
import com.sven.rainbowbeachlib.service.FloatService
import com.sven.rainbowbeachlib.tools.Permissions
import com.sven.rainbowbeachlib.tools.RbbUtils


/**
 * @Author:         xwp
 * @CreateDate:     2023/7/11
 * @Version:        1.0
 */
class StartActivity : FragmentActivity() {

    companion object {
        const val REQUEST_DIALOG_PERMISSION = 1023
    }

    private lateinit var mContext: Context
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        mContext = this
        if (checkOverlayPermission(mContext)) {
            checkSdReadPermission()
        } else {
            jumpFloatSettingPermission()
        }
    }


    override fun onResume() {
        super.onResume()
        if (checkOverlayPermission(mContext)) {
            checkSdReadPermission()
        }
    }

    override fun onActivityResult(requestCode: Int, resultCode: Int, data: Intent?) {
        super.onActivityResult(requestCode, resultCode, data)
        if (requestCode == REQUEST_DIALOG_PERMISSION) {
            if (checkOverlayPermission(mContext)) {
                checkSdReadPermission()
            } else {
                RbbUtils.showToast(mContext, "请务必开启【浮悬窗】权限")
                finish()
            }
        }
    }

    private fun jumpFloatSettingPermission() {
        val sdkInt = Build.VERSION.SDK_INT
        if (sdkInt >= Build.VERSION_CODES.O) { //8.0以上
            val intent = Intent(Settings.ACTION_MANAGE_OVERLAY_PERMISSION)
            startActivityForResult(intent, REQUEST_DIALOG_PERMISSION)
        } else if (sdkInt >= Build.VERSION_CODES.M) { //6.0-8.0
            val intent = Intent(Settings.ACTION_MANAGE_OVERLAY_PERMISSION)
            intent.data = Uri.parse("package:$packageName")
            startActivityForResult(intent, REQUEST_DIALOG_PERMISSION)
        } else { //4.4-6.0以下
            //无需处理了
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

    private fun checkSdReadPermission() {
        Permissions.request(
            this@StartActivity,
            arrayOf(
                "android.permission.READ_EXTERNAL_STORAGE"
            )
        ) { t ->
            if (t == -1) {
                RbbUtils.showToast(mContext, "请务必开启存储读取权限")
                finish()
            } else {
                startService(Intent(mContext, FloatService::class.java))
            }
            finish()
        }
    }
}