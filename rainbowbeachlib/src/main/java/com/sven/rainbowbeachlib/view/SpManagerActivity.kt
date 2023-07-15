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
import com.sven.rainbowbeachlib.tools.Constants.SP_STORE_PATH
import com.sven.rainbowbeachlib.tools.CopyPasteUtil
import com.sven.rainbowbeachlib.tools.RbbUtils
import java.io.File

/**
 * @Author:         xwp
 * @CreateDate:     2023/6/24
 * @Version:        1.0
 */
class SpManagerActivity : FragmentActivity() {

    private lateinit var mContext: Context

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        mContext = this
        setContentView(R.layout.activity_sp_manager)

        findViewById<View>(R.id.btn_back).setOnClickListener {
            finish()
        }

        findViewById<View>(R.id.btn_copy_sp_file).setOnClickListener {
            val copyStorePath = externalCacheDir?.absolutePath + SP_STORE_PATH;
            val file = File(copyStorePath)
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
            val success = CopyPasteUtil.copyFolder(
                mContext.applicationInfo.dataDir + "/shared_prefs",
                copyStorePath
            )
            if (success) {
                RbbUtils.showToast(mContext, "拷贝成功")
            } else {
                RbbUtils.showToast(mContext, "拷贝失败")
            }
        }

        findViewById<View>(R.id.btn_restore_sp_file).setOnClickListener {
            val copyStorePath = externalCacheDir?.absolutePath + SP_STORE_PATH;
            val file = File(copyStorePath)
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
            val success = CopyPasteUtil.copyFolder(
                copyStorePath,
                mContext.applicationInfo.dataDir + "/shared_prefs"
            )

            if (success) {
                RbbUtils.showToast(mContext, "还原成功")
            } else {
                RbbUtils.showToast(mContext, "还原失败")
            }
        }

//        checkSdPermission()

        EasyFloat.hide(FloatService.FLOAT_TAG)
    }


    override fun onDestroy() {
        super.onDestroy()
        EasyFloat.show(FloatService.FLOAT_TAG)
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