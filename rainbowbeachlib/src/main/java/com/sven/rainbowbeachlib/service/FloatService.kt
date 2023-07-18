package com.sven.rainbowbeachlib.service

import android.app.Service
import android.content.Context
import android.content.Intent
import android.graphics.PixelFormat
import android.os.Build
import android.os.IBinder
import android.view.Gravity
import android.view.LayoutInflater
import android.view.View
import android.view.WindowManager
import android.widget.FrameLayout
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
    private val mAdbHelper by lazy {
        AdbHelper()
    }
    private var mCurrentFloatView: View? = null
    private lateinit var floatView: View
    private lateinit var hideFloatView: View


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
        mAdbHelper.stop()
    }

    override fun onStartCommand(intent: Intent?, flags: Int, startId: Int): Int {
        val status = intent?.getIntExtra(KEY_INTENT_FLOAT_STATUS, KEY_INTENT_FLOAT_SHOW)
            ?: KEY_INTENT_FLOAT_SHOW
        if (status == KEY_INTENT_FLOAT_SHOW) {
            showFloatView(floatView, DisplayUtil.dip2px(mContext, 60F))
        } else if (status == KEY_INTENT_FLOAT_HIDE) {
            showFloatView(hideFloatView, DisplayUtil.dip2px(mContext, 20F))
        }
        mAdbHelper.start(mContext)

        intent?.getStringExtra(KEY_INTENT_ADB_COMMAND_STR)?.let {
            mAdbHelper.addCommand(it)
        }
        return super.onStartCommand(intent, flags, startId)
    }

    private fun showFloatView() {
        floatView =
            LayoutInflater.from(mContext).inflate(R.layout.float_view_layout, null)
        showFloatView(floatView, DisplayUtil.dip2px(mContext, 60F))

        hideFloatView =
            LayoutInflater.from(mContext).inflate(R.layout.float_hide_view_layout, null)

        hideFloatView.setOnClickListener {
            showFloatView(floatView, DisplayUtil.dip2px(mContext, 60F))
        }


        floatView.findViewById<View>(R.id.btn_query_view_id).setOnClickListener {
            val intent = Intent(mContext, CheckViewInfoActivity::class.java)
            intent.addFlags(Intent.FLAG_ACTIVITY_NEW_TASK)
            startActivity(intent)
        }

        floatView.findViewById<View>(R.id.btn_sp_manager).setOnClickListener {
            val intent = Intent(mContext, SpManagerActivity::class.java)
            intent.addFlags(Intent.FLAG_ACTIVITY_NEW_TASK)
            startActivity(intent)
        }

        floatView.findViewById<View>(R.id.btn_screen_shot).setOnClickListener {
            if (!mAdbHelper.isConnected()) {
                RbbUtils.showToast(mContext, "adb未连接")
                return@setOnClickListener
            }
            val path = externalCacheDir?.absolutePath + Constants.SCREENSHOT_PATH
            FileUtils.createFolder(path)
            mAdbHelper.addCommand("/system/bin/screencap -p " + path + File.separator + System.currentTimeMillis() + ".png")
        }

        floatView.findViewById<View>(R.id.btn_screen_record).setOnClickListener {
            if (!mAdbHelper.isConnected()) {
                RbbUtils.showToast(mContext, "adb未连接")
                return@setOnClickListener
            }
            val path = externalCacheDir?.absolutePath + Constants.SCREEN_RECORD_PATH
            FileUtils.createFolder(path)
            mAdbHelper.addCommand("screenrecord " + path + File.separator + System.currentTimeMillis() + ".mp4")
        }

        floatView.findViewById<View>(R.id.btn_adb).setOnClickListener {
            val intent = Intent(mContext, AdbOperationActivity::class.java)
            intent.addFlags(Intent.FLAG_ACTIVITY_NEW_TASK)
            startActivity(intent)
        }

        floatView.findViewById<View>(R.id.btn_exit).setOnClickListener {
            stopSelf()
            exitProcess(0)
        }

        floatView.findViewById<View>(R.id.btn_hide).setOnClickListener {
            showFloatView(hideFloatView, DisplayUtil.dip2px(mContext, 20F))
        }
    }


    private fun showFloatView(floatView: View, floatViewWidth: Int) {
        val windowManager = getSystemService(WINDOW_SERVICE) as WindowManager
        mCurrentFloatView?.let {
            windowManager.removeView(it)
        }
        val layoutParams = WindowManager.LayoutParams()
        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.O) {
            layoutParams.type = WindowManager.LayoutParams.TYPE_APPLICATION_OVERLAY
        } else {
            layoutParams.type = WindowManager.LayoutParams.TYPE_PHONE
        }
        layoutParams.format = PixelFormat.RGBA_8888
        layoutParams.gravity = Gravity.LEFT or Gravity.TOP
        layoutParams.flags =
            WindowManager.LayoutParams.FLAG_NOT_TOUCH_MODAL or WindowManager.LayoutParams.FLAG_NOT_FOCUSABLE
        layoutParams.width = FrameLayout.LayoutParams.WRAP_CONTENT
        layoutParams.height = FrameLayout.LayoutParams.WRAP_CONTENT
        layoutParams.x = DisplayUtil.getScreenWidth(mContext) - floatViewWidth
        layoutParams.y = 100
        windowManager.addView(floatView, layoutParams)
        mCurrentFloatView = floatView
    }
}