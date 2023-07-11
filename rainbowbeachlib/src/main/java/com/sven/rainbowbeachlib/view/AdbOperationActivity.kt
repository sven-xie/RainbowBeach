package com.sven.rainbowbeachlib.view

import android.content.Context
import android.os.Bundle
import android.view.View
import androidx.fragment.app.FragmentActivity
import com.lzf.easyfloat.EasyFloat
import com.sven.rainbowbeachlib.R
import com.sven.rainbowbeachlib.adblib.AdbHelper
import com.sven.rainbowbeachlib.service.FloatService
import java.io.File

/**
 * @Author:         xwp
 * @CreateDate:     2023/6/24
 * @Version:        1.0
 */
class AdbOperationActivity : FragmentActivity() {

    companion object {
        const val SP_STORE_PATH = "/sdcard/Download/rainbowBeach/spFiles"
        const val SCREENSHOT_PATH = "/sdcard/Download/rainbowBeach/screenshot"
        const val SCREEN_RECORD_PATH = "/sdcard/Download/rainbowBeach/screenRecord"
    }

    private lateinit var mContext: Context
    private val mAdbHelper by lazy {
        AdbHelper()
    }

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        mContext = this
        setContentView(R.layout.activity_adb_operation)

        findViewById<View>(R.id.btn_back).setOnClickListener {
            finish()
        }

        findViewById<View>(R.id.btn_catch_screen).setOnClickListener {
            createFolder(SCREENSHOT_PATH)
            mAdbHelper.addCommand("/system/bin/screencap -p " + SCREENSHOT_PATH + File.separator + System.currentTimeMillis() + ".png")
        }

        findViewById<View>(R.id.btn_record_screen).setOnClickListener {
            createFolder(SCREEN_RECORD_PATH)
            mAdbHelper.addCommand("screenrecord " + SCREEN_RECORD_PATH + File.separator + System.currentTimeMillis() + ".mp4")
        }

        mAdbHelper.start(mContext)

        EasyFloat.hide(FloatService.TAG)
    }


    override fun onDestroy() {
        super.onDestroy()
        mAdbHelper.stop()
        EasyFloat.show(FloatService.TAG)
    }

    private fun createFolder(path: String) {
        //新建一个File，传入文件夹目录
        val file = File(path)
        //判断文件夹是否存在，如果不存在就创建，否则不创建
        if (!file.exists()) {
            //通过file的mkdirs()方法创建目录中包含却不存在的文件夹
            file.mkdirs()
        }
    }
}