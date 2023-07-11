package com.sven.rainbowbeachlib.view

import android.content.Context
import android.os.Bundle
import android.view.View
import androidx.fragment.app.FragmentActivity
import com.hjq.permissions.OnPermissionCallback
import com.hjq.permissions.Permission
import com.hjq.permissions.XXPermissions
import com.lzf.easyfloat.EasyFloat
import com.sven.rainbowbeachlib.R
import com.sven.rainbowbeachlib.service.FloatService
import com.sven.rainbowbeachlib.tools.CopyPasteUtil
import com.sven.rainbowbeachlib.tools.RbbUtils
import java.io.File

/**
 * @Author:         xwp
 * @CreateDate:     2023/6/24
 * @Version:        1.0
 */
class SpManagerActivity : FragmentActivity() {

    companion object {
        const val SP_STORE_PATH = "/sdcard/Download/rainbowBeach/spFiles"
    }

    private lateinit var mContext: Context

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        mContext = this
        setContentView(R.layout.activity_sp_manager)

        findViewById<View>(R.id.btn_back).setOnClickListener {
            finish()
        }

        findViewById<View>(R.id.btn_copy_sp_file).setOnClickListener {
            val file = File(SP_STORE_PATH)
            if (file.exists()) {
                if (file.isDirectory) {
                    file.listFiles()?.forEach {
                        it.delete()
                    }
                } else {
                    file.delete()
                }
            } else {
                file.mkdirs()
            }
            CopyPasteUtil.copyFolder(
                mContext.applicationInfo.dataDir + "/shared_prefs",
                SP_STORE_PATH
            )
        }

        findViewById<View>(R.id.btn_restore_sp_file).setOnClickListener {
            val file = File(SP_STORE_PATH)
            if (!file.exists() || file.listFiles() == null || file.listFiles().isEmpty()) {
                RbbUtils.showToast(mContext, "SP文件未找到");
                return@setOnClickListener
            }
            val targetFile = File(mContext.applicationInfo.dataDir + "/shared_prefs")
            if (targetFile.exists()) {
                if (targetFile.isDirectory) {
                    targetFile.listFiles()?.forEach {
                        it.delete()
                    }
                } else {
                    targetFile.delete()
                }
            } else {
                targetFile.mkdirs()
            }
            CopyPasteUtil.copyFolder(
                SP_STORE_PATH,
                mContext.applicationInfo.dataDir + "/shared_prefs"
            )
        }

        checkSdPermission()

        EasyFloat.hide(FloatService.TAG)
    }


    override fun onDestroy() {
        super.onDestroy()
        EasyFloat.show(FloatService.TAG)
    }

    private fun checkSdPermission() {
        XXPermissions.with(this) //申请悬浮窗权限
            .permission(Permission.READ_EXTERNAL_STORAGE)
            .permission(Permission.WRITE_EXTERNAL_STORAGE)
            .request(object : OnPermissionCallback {
                override fun onGranted(permissions: List<String?>?, all: Boolean) {
                }

                override fun onDenied(permissions: List<String?>?, never: Boolean) {
                    RbbUtils.showToast(mContext, "请允许存储卡读写权限")
                }
            })
    }
}