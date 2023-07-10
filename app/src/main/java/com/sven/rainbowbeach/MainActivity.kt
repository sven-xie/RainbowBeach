package com.sven.rainbowbeach

import android.content.Context
import android.content.Intent
import android.os.Build
import android.os.Bundle
import android.provider.Settings
import android.view.View
import androidx.appcompat.app.AppCompatActivity
import com.hjq.permissions.OnPermissionCallback
import com.hjq.permissions.Permission
import com.hjq.permissions.XXPermissions
import com.sven.rainbowbeachlib.service.FloatService
import com.sven.rainbowbeach.util.LogUtils
import com.sven.rainbowbeach.view.TestFragment
import kotlinx.android.synthetic.main.activity_main.*


class MainActivity : AppCompatActivity() {
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_main)

        val testFragment = TestFragment()
        val beginTransaction = supportFragmentManager.beginTransaction()
        beginTransaction.add(R.id.fl_test_fragment, testFragment)
//        beginTransaction.hide(testFragment)
        beginTransaction.commitAllowingStateLoss()

    }

    override fun onResume() {
        super.onResume()

        if (checkOverlayPermission(this)) {
            btn_open_float_permission.visibility = View.GONE
            startService(Intent(this@MainActivity, FloatService::class.java))
        } else {
            btn_open_float_permission.visibility = View.VISIBLE
            btn_open_float_permission.setOnClickListener {
                XXPermissions.with(this) //申请悬浮窗权限
                    .permission(Permission.SYSTEM_ALERT_WINDOW)
                    .request(object : OnPermissionCallback {
                        override fun onGranted(permissions: List<String?>?, all: Boolean) {
                            if (!all) {
                                LogUtils.i("请务必开启【浮悬窗】权限")
                            } else {
                                startService(Intent(this@MainActivity, FloatService::class.java))
                            }
                        }

                        override fun onDenied(permissions: List<String?>?, never: Boolean) {
                            LogUtils.i("请务必开启【浮悬窗】权限")
                        }
                    })
            }
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