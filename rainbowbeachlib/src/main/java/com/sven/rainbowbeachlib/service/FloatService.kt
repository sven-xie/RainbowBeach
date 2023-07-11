package com.sven.rainbowbeachlib.service

import android.app.Service
import android.content.Context
import android.content.Intent
import android.os.IBinder
import android.view.LayoutInflater
import android.view.View
import com.lzf.easyfloat.EasyFloat
import com.lzf.easyfloat.enums.ShowPattern
import com.lzf.easyfloat.enums.SidePattern
import com.sven.rainbowbeachlib.R
import com.sven.rainbowbeachlib.tools.DisplayUtil
import com.sven.rainbowbeachlib.view.AdbOperationActivity
import com.sven.rainbowbeachlib.view.CheckViewInfoActivity
import com.sven.rainbowbeachlib.view.SpManagerActivity

/**
 * @Author:         xwp
 * @CreateDate:     2023/6/24
 * @Version:        1.0
 */
class FloatService : Service() {

    private lateinit var context: Context;
    private var easyFloat: EasyFloat.Builder? = null


    companion object {
        const val TAG = "FloatService"
        const val KEY_INTENT_FLOAT_STATUS = "KEY_INTENT_FLOAT_STATUS"
        const val KEY_INTENT_FLOAT_SHOW = 0
        const val KEY_INTENT_FLOAT_HIDE = 1

        fun showFloatView(context: Context) {
            val intent = Intent(context, FloatService::class.java)
            intent.putExtra(KEY_INTENT_FLOAT_STATUS, KEY_INTENT_FLOAT_SHOW)
            context.startService(intent)
        }

        fun hideFloatView(context: Context) {
            val intent = Intent(context, FloatService::class.java)
            intent.putExtra(KEY_INTENT_FLOAT_STATUS, KEY_INTENT_FLOAT_HIDE)
            context.startService(intent)
        }
    }

    override fun onBind(intent: Intent?): IBinder? {
        return null
    }

    override fun onCreate() {
        super.onCreate()
        context = this
        showFloatView()
    }

    override fun onDestroy() {
        super.onDestroy()
        EasyFloat.dismiss(TAG)
    }

    override fun onStartCommand(intent: Intent?, flags: Int, startId: Int): Int {
        val status = intent?.getIntExtra(KEY_INTENT_FLOAT_STATUS, KEY_INTENT_FLOAT_SHOW)
            ?: KEY_INTENT_FLOAT_SHOW
        if (status == KEY_INTENT_FLOAT_SHOW) {
            EasyFloat.show(TAG)
        } else {
            EasyFloat.hide(TAG)
        }
        return super.onStartCommand(intent, flags, startId)
    }

    private fun showFloatView() {
        val mFloatView =
            LayoutInflater.from(context).inflate(R.layout.float_view_layout, null)
        easyFloat = EasyFloat.with(context) // 设置浮窗xml布局文件/自定义View，并可设置详细信息
            .setLayout(mFloatView)
            .setShowPattern(ShowPattern.ALL_TIME)
            .setMatchParent(false, false) // 设置吸附方式，共15种模式，详情参考SidePattern
            .setSidePattern(SidePattern.DEFAULT) // 设置浮窗的标签，用于区分多个浮窗
            .setGravity(
                0,
                DisplayUtil.getScreenWidth(context) - DisplayUtil.dip2px(context, 90F),
                100
            )
            .setDragEnable(true)
            .setTag(TAG)

        easyFloat?.show()

        mFloatView.findViewById<View>(R.id.btn_query_view_id).setOnClickListener {
            val intent = Intent(context, CheckViewInfoActivity::class.java)
            intent.addFlags(Intent.FLAG_ACTIVITY_NEW_TASK)
            startActivity(intent)
        }

        mFloatView.findViewById<View>(R.id.btn_sp_manager).setOnClickListener {
            val intent = Intent(context, SpManagerActivity::class.java)
            intent.addFlags(Intent.FLAG_ACTIVITY_NEW_TASK)
            startActivity(intent)
        }

        mFloatView.findViewById<View>(R.id.btn_adb).setOnClickListener {
            val intent = Intent(context, AdbOperationActivity::class.java)
            intent.addFlags(Intent.FLAG_ACTIVITY_NEW_TASK)
            startActivity(intent)
        }

        mFloatView.findViewById<View>(R.id.btn_exit).setOnClickListener {
//            EasyFloat.dismiss()
            stopSelf()
        }
    }
}