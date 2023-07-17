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
import com.sven.rainbowbeachlib.adblib.AdbHelper
import com.sven.rainbowbeachlib.tools.Constants
import com.sven.rainbowbeachlib.tools.DisplayUtil
import com.sven.rainbowbeachlib.tools.FileUtils
import com.sven.rainbowbeachlib.tools.RbbUtils
import com.sven.rainbowbeachlib.view.AdbOperationActivity
import com.sven.rainbowbeachlib.view.CheckViewInfoActivity
import com.sven.rainbowbeachlib.view.SpManagerActivity
import java.io.File
import kotlin.system.exitProcess

/**
 * @Author:         xwp
 * @CreateDate:     2023/6/24
 * @Version:        1.0
 */
class FloatService : Service() {

    private lateinit var mContext: Context;
    private var easyFloat: EasyFloat.Builder? = null
    private val mAdbHelper by lazy {
        AdbHelper()
    }


    companion object {
        const val FLOAT_TAG = "FloatService"
        const val FLOAT_HIDE_TAG = "FloatService_hide"
        const val KEY_INTENT_FLOAT_STATUS = "KEY_INTENT_FLOAT_STATUS"
        const val KEY_INTENT_ADB_COMMAND_STR = "KEY_INTENT_ADB_COMMAND_STR"
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

        fun runAdbCommand(context: Context, adbCommandStr: String) {
            val intent = Intent(context, FloatService::class.java)
            intent.putExtra(KEY_INTENT_ADB_COMMAND_STR, adbCommandStr)
            context.startService(intent)
        }
    }

    override fun onBind(intent: Intent?): IBinder? {
        return null
    }

    override fun onCreate() {
        super.onCreate()
        mContext = this
        showFloatView()
    }

    override fun onDestroy() {
        super.onDestroy()
        EasyFloat.dismiss(FLOAT_TAG)
        EasyFloat.dismiss(FLOAT_HIDE_TAG)
        mAdbHelper.stop()
    }

    override fun onStartCommand(intent: Intent?, flags: Int, startId: Int): Int {
        val status = intent?.getIntExtra(KEY_INTENT_FLOAT_STATUS, KEY_INTENT_FLOAT_SHOW)
            ?: KEY_INTENT_FLOAT_SHOW
        if (status == KEY_INTENT_FLOAT_SHOW) {
            EasyFloat.hide(FLOAT_HIDE_TAG)
            EasyFloat.show(FLOAT_TAG)
        } else if (status == KEY_INTENT_FLOAT_HIDE) {
            EasyFloat.hide(FLOAT_TAG)
        }
        mAdbHelper.start(mContext)

        intent?.getStringExtra(KEY_INTENT_ADB_COMMAND_STR)?.let {
            mAdbHelper.addCommand(it)
        }
        return super.onStartCommand(intent, flags, startId)
    }

    private fun showFloatView() {
        val mFloatView =
            LayoutInflater.from(mContext).inflate(R.layout.float_view_layout, null)
        easyFloat = EasyFloat.with(mContext) // 设置浮窗xml布局文件/自定义View，并可设置详细信息
            .setLayout(mFloatView)
            .setShowPattern(ShowPattern.ALL_TIME)
            .setMatchParent(false, false) // 设置吸附方式，共15种模式，详情参考SidePattern
            .setSidePattern(SidePattern.DEFAULT) // 设置浮窗的标签，用于区分多个浮窗
            .setGravity(
                0,
                DisplayUtil.getScreenWidth(mContext) - DisplayUtil.dip2px(mContext, 60F),
                100
            )
            .setDragEnable(false)
            .setTag(FLOAT_TAG)

        val hideRootView =
            LayoutInflater.from(mContext).inflate(R.layout.float_hide_view_layout, null)
        EasyFloat.with(mContext) // 设置浮窗xml布局文件/自定义View，并可设置详细信息
            .setLayout(hideRootView)
            .setShowPattern(ShowPattern.ALL_TIME)
            .setMatchParent(false, false) // 设置吸附方式，共15种模式，详情参考SidePattern
            .setSidePattern(SidePattern.DEFAULT) // 设置浮窗的标签，用于区分多个浮窗
            .setGravity(
                0,
                DisplayUtil.getScreenWidth(mContext) - DisplayUtil.dip2px(mContext, 20F),
                100
            )
            .setDragEnable(false)
            .setTag(FLOAT_HIDE_TAG)
            .show()

        hideRootView.setOnClickListener {
            EasyFloat.show(FLOAT_TAG)
            EasyFloat.hide(FLOAT_HIDE_TAG)
        }

        EasyFloat.hide(FLOAT_HIDE_TAG)

        easyFloat?.show()

        mFloatView.findViewById<View>(R.id.btn_query_view_id).setOnClickListener {
            val intent = Intent(mContext, CheckViewInfoActivity::class.java)
            intent.addFlags(Intent.FLAG_ACTIVITY_NEW_TASK)
            startActivity(intent)
        }

        mFloatView.findViewById<View>(R.id.btn_sp_manager).setOnClickListener {
            val intent = Intent(mContext, SpManagerActivity::class.java)
            intent.addFlags(Intent.FLAG_ACTIVITY_NEW_TASK)
            startActivity(intent)
        }

        mFloatView.findViewById<View>(R.id.btn_screen_shot).setOnClickListener {
            if (!mAdbHelper.isConnected()) {
                RbbUtils.showToast(mContext, "adb未连接")
                return@setOnClickListener
            }
            val path = externalCacheDir?.absolutePath + Constants.SCREENSHOT_PATH
            FileUtils.createFolder(path)
            mAdbHelper.addCommand("/system/bin/screencap -p " + path + File.separator + System.currentTimeMillis() + ".png")
        }

        mFloatView.findViewById<View>(R.id.btn_screen_record).setOnClickListener {
            if (!mAdbHelper.isConnected()) {
                RbbUtils.showToast(mContext, "adb未连接")
                return@setOnClickListener
            }
            val path = externalCacheDir?.absolutePath + Constants.SCREEN_RECORD_PATH
            FileUtils.createFolder(path)
            mAdbHelper.addCommand("screenrecord " + path + File.separator + System.currentTimeMillis() + ".mp4")
        }

        mFloatView.findViewById<View>(R.id.btn_adb).setOnClickListener {
            val intent = Intent(mContext, AdbOperationActivity::class.java)
            intent.addFlags(Intent.FLAG_ACTIVITY_NEW_TASK)
            startActivity(intent)
        }

        mFloatView.findViewById<View>(R.id.btn_exit).setOnClickListener {
            stopSelf()
            exitProcess(0)
        }

        mFloatView.findViewById<View>(R.id.btn_hide).setOnClickListener {
            EasyFloat.hide(FLOAT_TAG)
            EasyFloat.show(FLOAT_HIDE_TAG)
        }
    }
}