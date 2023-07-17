package com.sven.rainbowbeach

import android.content.Context
import android.content.SharedPreferences
import android.os.Build
import android.os.Bundle
import android.provider.Settings
import android.view.View
import androidx.appcompat.app.AppCompatActivity
import com.sven.rainbowbeach.view.TestFragment
import kotlinx.android.synthetic.main.activity_main.*


class MainActivity : AppCompatActivity() {
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_main)

        val testFragment1 = TestFragment()
        val testFragment2 = TestFragment()
        val beginTransaction = supportFragmentManager.beginTransaction()
        beginTransaction.add(R.id.fl_test_fragment_1, testFragment1)
        beginTransaction.add(R.id.fl_test_fragment_2, testFragment2)
//        beginTransaction.hide(testFragment)
        beginTransaction.commitAllowingStateLoss()

        if (checkOverlayPermission(this)) {
            btn_open_float_permission.visibility = View.GONE
        } else {
            btn_open_float_permission.visibility = View.VISIBLE
            btn_open_float_permission.setOnClickListener {
            }
        }

        val sp: SharedPreferences = getSharedPreferences("test_sp", MODE_PRIVATE)
        val editor: SharedPreferences.Editor = sp.edit()
        editor.putString("data", "我是Rocky111")
        editor.commit()
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