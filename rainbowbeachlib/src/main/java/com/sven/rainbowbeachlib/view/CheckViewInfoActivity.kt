package com.sven.rainbowbeachlib.view

import android.content.Context
import android.os.Bundle
import android.view.View
import android.view.Window
import android.view.WindowManager
import androidx.fragment.app.FragmentActivity
import androidx.recyclerview.widget.LinearLayoutManager
import androidx.recyclerview.widget.RecyclerView
import com.lzf.easyfloat.EasyFloat
import com.sven.rainbowbeachlib.R
import com.sven.rainbowbeachlib.bean.ViewInfoBean
import com.sven.rainbowbeachlib.service.FloatService
import com.sven.rainbowbeachlib.tools.view.ViewInfoUtils
import com.sven.rainbowbeachlib.view.adapter.ViewInfoAdapter
import com.sven.rainbowbeachlib.widgets.ViewInfoRectView

/**
 * @Author:         xwp
 * @CreateDate:     2023/6/24
 * @Version:        1.0
 */
class CheckViewInfoActivity : FragmentActivity() {

    private lateinit var mViewInfoRectView: ViewInfoRectView
    private lateinit var mContext: Context
    private lateinit var mRvViewInfo: RecyclerView
    private val mAdapter: ViewInfoAdapter by lazy { ViewInfoAdapter(mutableListOf<ViewInfoBean>()) }

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        mContext = this
        this.requestWindowFeature(Window.FEATURE_NO_TITLE);
        this.window.setFlags(
            WindowManager.LayoutParams.FLAG_FULLSCREEN,
            WindowManager.LayoutParams.FLAG_FULLSCREEN
        );
        setContentView(R.layout.activity_check_view)

        findViewById<View>(R.id.fl_container).setOnTouchListener { v, event ->
            if (mRvViewInfo.visibility == View.VISIBLE) {
                mRvViewInfo.visibility = View.GONE
                return@setOnTouchListener false
            }
            val rawX = event.rawX
            val rawY = event.rawY
            getViewInfo(rawX, rawY)
            return@setOnTouchListener false
        }

        findViewById<View>(R.id.iv_close)?.setOnClickListener {
            finish()
        }

        mRvViewInfo = findViewById(R.id.rv_view_info)

        mRvViewInfo.setOnClickListener {

        }

        mRvViewInfo = findViewById(R.id.rv_view_info)
        mRvViewInfo.visibility = View.GONE
        mRvViewInfo.adapter = mAdapter
        mRvViewInfo.layoutManager = LinearLayoutManager(mContext, RecyclerView.VERTICAL, false)

        mAdapter.mItemClickListener = object : ViewInfoAdapter.OnViewInfoItemClickListener {
            override fun onItemClick(viewInfoBean: ViewInfoBean) {
                mViewInfoRectView.invalidate()
            }

            override fun onItemDetailClick(viewInfoBean: ViewInfoBean) {

            }
        }

        mViewInfoRectView = findViewById(R.id.check_rect_view)

        getAllViewInfo()

        EasyFloat.hide(FloatService.TAG)
    }


    override fun onDestroy() {
        super.onDestroy()
        EasyFloat.show(FloatService.TAG)
    }

    private fun getAllViewInfo() {
        val viewInfos = ViewInfoUtils.getAllViewInfo()
        mViewInfoRectView.setViewRectList(viewInfos)
        if (viewInfos != null && viewInfos.isNotEmpty()) {
            mAdapter.updateData(viewInfos)
        }
    }

    private fun getViewInfo(rawX: Float, rawY: Float) {
        val viewInfos = ViewInfoUtils.getViewInfo(rawX, rawY)
        mViewInfoRectView.setViewRectList(viewInfos)
        if (viewInfos != null && viewInfos.isNotEmpty()) {
            mRvViewInfo.visibility = View.VISIBLE
            mAdapter.updateData(viewInfos)
        }
    }
}