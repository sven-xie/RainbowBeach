package com.sven.rainbowbeach

import android.content.Context
import android.content.Intent
import android.os.Build
import android.os.Bundle
import android.provider.Settings
import android.view.View
import androidx.appcompat.app.AppCompatActivity
import com.sven.rainbowbeach.view.TestFragment
import com.sven.rainbowbeachlib.view.StartActivity
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

        if (checkOverlayPermission(this)) {
            btn_open_float_permission.visibility = View.GONE
            startActivity(Intent(this@MainActivity, StartActivity::class.java))
        } else {
            btn_open_float_permission.visibility = View.VISIBLE
            btn_open_float_permission.setOnClickListener {
                startActivity(Intent(this@MainActivity, StartActivity::class.java))
            }
        }
    }

    override fun onResume() {
        super.onResume()
        if (checkOverlayPermission(this)) {
            btn_open_float_permission.visibility = View.GONE
        } else {
            btn_open_float_permission.visibility = View.VISIBLE
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