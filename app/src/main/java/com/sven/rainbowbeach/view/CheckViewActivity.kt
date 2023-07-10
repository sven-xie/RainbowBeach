package com.sven.rainbowbeach.view

import android.annotation.SuppressLint
import android.os.Bundle
import android.view.Window
import android.view.WindowManager
import androidx.fragment.app.FragmentActivity
import com.sven.rainbowbeach.R
import com.sven.rainbowbeachlib.tools.view.ViewInfoUtils
import kotlinx.android.synthetic.main.activity_check_view.*

/**
 * @Author:         xwp
 * @CreateDate:     2023/6/24
 * @Version:        1.0
 */
@SuppressLint("ClickableViewAccessibility")
class CheckViewActivity : FragmentActivity() {


    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        this.requestWindowFeature(Window.FEATURE_NO_TITLE);
        this.window.setFlags(
            WindowManager.LayoutParams.FLAG_FULLSCREEN,
            WindowManager.LayoutParams.FLAG_FULLSCREEN
        );
        setContentView(R.layout.activity_check_view)

        fl_container.setOnTouchListener { v, event ->
            val rawX = event.rawX
            val rawY = event.rawY
            getViewInfo(rawX, rawY)
            return@setOnTouchListener false
        }
    }


    private fun getViewInfo(rawX: Float, rawY: Float) {
        val viewInfos = ViewInfoUtils.getViewInfo(rawX, rawY)
        check_rect_view.setViewRectList(viewInfos)
    }
}