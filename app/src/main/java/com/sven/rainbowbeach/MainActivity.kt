package com.sven.rainbowbeach

import android.content.Context
import android.content.Intent
import android.content.SharedPreferences
import android.os.Bundle
import androidx.appcompat.app.AppCompatActivity
import com.sven.rainbowbeachlib.service.LocalFileServer
import com.sven.rainbowbeachlib.tools.CommonUtils
import kotlinx.android.synthetic.main.activity_main.*


class MainActivity : AppCompatActivity() {

    private lateinit var mContext: Context

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_main)

        mContext = this

        tv_local_sd_service_address.text =
            "服务已开启：请在网页打开地址：http://${CommonUtils.getHostIP()}:${LocalFileServer.PORT}"

        val sp: SharedPreferences = getSharedPreferences("test_sp", MODE_PRIVATE)
        val editor: SharedPreferences.Editor = sp.edit()
        editor.putString("data", "我是Rocky111")
        editor.commit()

        btn_jump_to_check_view_info.setOnClickListener {
            startActivity(Intent(mContext, CheckViewInfoDemoActivity::class.java))
        }
    }

}