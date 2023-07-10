package com.sven.rainbowbeach.service

import android.app.Service
import android.content.Context
import android.content.Intent
import android.os.IBinder
import android.view.LayoutInflater
import android.view.View
import com.lzf.easyfloat.EasyFloat
import com.lzf.easyfloat.enums.ShowPattern
import com.lzf.easyfloat.enums.SidePattern
import com.sven.rainbowbeach.R
import com.sven.rainbowbeach.view.CheckViewActivity

/**
 * @Author:         xwp
 * @CreateDate:     2023/6/24
 * @Version:        1.0
 */
class FloatService : Service() {

    private lateinit var context: Context;

    override fun onBind(intent: Intent?): IBinder? {
        return null
    }

    override fun onCreate() {
        super.onCreate()
        context = this
        showFloatView()
    }

    private fun showFloatView() {
        val mFloatView =
            LayoutInflater.from(this@FloatService).inflate(R.layout.float_view_layout, null)
        EasyFloat.with(this@FloatService) // 设置浮窗xml布局文件/自定义View，并可设置详细信息
            .setLayout(mFloatView)
            .setShowPattern(ShowPattern.ALL_TIME)
            .setMatchParent(false, false) // 设置吸附方式，共15种模式，详情参考SidePattern
            .setSidePattern(SidePattern.DEFAULT) // 设置浮窗的标签，用于区分多个浮窗
            .setGravity(
                0,
                100,
                100
            )
            .setDragEnable(true)
            .show()

        mFloatView.findViewById<View>(R.id.btn_query_view_id).setOnClickListener {
            val intent = Intent(context, CheckViewActivity::class.java)
            intent.addFlags(Intent.FLAG_ACTIVITY_NEW_TASK)
            startActivity(intent)
        }
    }
}