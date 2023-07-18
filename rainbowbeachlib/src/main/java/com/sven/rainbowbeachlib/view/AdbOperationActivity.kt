package com.sven.rainbowbeachlib.view

import android.content.Context
import android.os.Bundle
import android.view.View
import android.widget.EditText
import androidx.fragment.app.FragmentActivity
import com.sven.rainbowbeachlib.R
import com.sven.rainbowbeachlib.service.FloatService

/**
 * @Author:         xwp
 * @CreateDate:     2023/6/24
 * @Version:        1.0
 */
class AdbOperationActivity : FragmentActivity() {

    private lateinit var mContext: Context
    private lateinit var mEditAdb: EditText

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        mContext = this
        setContentView(R.layout.activity_adb_operation)

        findViewById<View>(R.id.btn_back).setOnClickListener {
            finish()
        }

        mEditAdb = findViewById(R.id.edit_adb)

        findViewById<View>(R.id.btn_run_adb).setOnClickListener {
            mEditAdb.text.toString()
            FloatService.runAdbCommand(mContext, mEditAdb.text.toString())
        }

        findViewById<View>(R.id.btn_clear).setOnClickListener {
            mEditAdb.setText("")
        }

        FloatService.hideFloatView(mContext)
    }


    override fun onDestroy() {
        super.onDestroy()
        FloatService.showFloatView(mContext)
    }
}