package com.sven.rainbowbeach.service

import android.app.Service
import android.content.Intent
import android.os.IBinder
import android.view.LayoutInflater
import com.lzf.easyfloat.EasyFloat
import com.lzf.easyfloat.enums.ShowPattern
import com.lzf.easyfloat.enums.SidePattern
import com.sven.rainbowbeach.R

/**
 * @Author:         xwp
 * @CreateDate:     2023/6/24
 * @Version:        1.0
 */
class FloatService : Service() {

    override fun onBind(intent: Intent?): IBinder? {
        return null
    }

    override fun onCreate() {
        super.onCreate()

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

    }
}