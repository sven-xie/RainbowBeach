package com.sven.rainbowbeach

import android.content.SharedPreferences
import android.os.Bundle
import androidx.appcompat.app.AppCompatActivity
import com.sven.rainbowbeach.view.TestFragment


class CheckViewInfoDemoActivity : AppCompatActivity() {
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_check_view_info_demo)

        val testFragment1 = TestFragment()
        val testFragment2 = TestFragment()
        val beginTransaction = supportFragmentManager.beginTransaction()
        beginTransaction.add(R.id.fl_test_fragment_1, testFragment1)
        beginTransaction.add(R.id.fl_test_fragment_2, testFragment2)
        beginTransaction.commitAllowingStateLoss()

        val sp: SharedPreferences = getSharedPreferences("test_sp", MODE_PRIVATE)
        val editor: SharedPreferences.Editor = sp.edit()
        editor.putString("data", "我是Rocky111")
        editor.commit()
    }

    override fun onResume() {
        super.onResume()

    }


}