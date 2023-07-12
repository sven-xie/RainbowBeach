package com.sven.rainbowbeachlib.view

import android.content.Context
import android.os.Bundle
import android.view.View
import android.view.ViewGroup
import android.view.Window
import android.view.WindowManager
import android.widget.TextView
import androidx.core.view.children
import androidx.fragment.app.FragmentActivity
import androidx.recyclerview.widget.LinearLayoutManager
import androidx.recyclerview.widget.RecyclerView
import com.lzf.easyfloat.EasyFloat
import com.sven.rainbowbeachlib.R
import com.sven.rainbowbeachlib.bean.ViewDetailInfoBean
import com.sven.rainbowbeachlib.bean.ViewInfoBean
import com.sven.rainbowbeachlib.service.FloatService
import com.sven.rainbowbeachlib.tools.view.ViewInfoUtils
import com.sven.rainbowbeachlib.view.adapter.ViewDetailInfoAdapter
import com.sven.rainbowbeachlib.view.adapter.ViewListInfoAdapter
import com.sven.rainbowbeachlib.widgets.ViewInfoRectView

/**
 * @Author:         xwp
 * @CreateDate:     2023/6/24
 * @Version:        1.0
 */
class CheckViewInfoActivity : FragmentActivity() {

    private lateinit var mTvTitle: TextView
    private lateinit var mViewInfoRectView: ViewInfoRectView
    private lateinit var mContext: Context
    private lateinit var mRvViewInfo: RecyclerView
    private val mAdapter: ViewListInfoAdapter by lazy { ViewListInfoAdapter(mutableListOf()) }
    private val mDetailAdapter: ViewDetailInfoAdapter by lazy { ViewDetailInfoAdapter(mutableListOf()) }
    private var isDetailShowing = false

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
            if (isDetailShowing) {
                showViewListInfo()
                return@setOnClickListener
            }
            finish()
        }

        mRvViewInfo = findViewById(R.id.rv_view_info)

        mRvViewInfo.setOnClickListener {

        }

        mTvTitle = findViewById(R.id.tv_title)

        mRvViewInfo = findViewById(R.id.rv_view_info)
        mRvViewInfo.visibility = View.GONE
        mRvViewInfo.adapter = mAdapter
        mRvViewInfo.layoutManager = LinearLayoutManager(mContext, RecyclerView.VERTICAL, false)

        mAdapter.mItemClickListener = object : ViewListInfoAdapter.OnViewInfoItemClickListener {
            override fun onItemClick(viewInfoBean: ViewInfoBean) {
                mViewInfoRectView.invalidate()
            }

            override fun onItemDetailClick(viewInfoBean: ViewInfoBean) {
                seeViewDetails(viewInfoBean)
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
            mTvTitle.text = "View列表"
        } else {
            mTvTitle.text = "点击View查看信息"
        }
    }


    private fun showViewListInfo() {
        mTvTitle.text = "View列表"
        isDetailShowing = false
        mRvViewInfo.visibility = View.VISIBLE
        mRvViewInfo.adapter = mAdapter
        mRvViewInfo.layoutManager = LinearLayoutManager(mContext, RecyclerView.VERTICAL, false)
    }


    private fun seeViewDetails(viewInfoBean: ViewInfoBean) {
        val view = viewInfoBean.view
        view ?: return
        val viewParent = view.parent
        var childViewInfoStr = "Null"
        if (view is ViewGroup && view.childCount > 0) {
            childViewInfoStr = ""
            view.children.forEach {
                childViewInfoStr += resources.getResourceEntryName(it.id) + ";"
            }
        }
        val viewDetailInfoBeanList = mutableListOf<ViewDetailInfoBean>()
        viewDetailInfoBeanList.add(ViewDetailInfoBean("View Id Name", viewInfoBean.id))
        viewDetailInfoBeanList.add(ViewDetailInfoBean("View Id(Hex)", view.id.toString()))
        viewDetailInfoBeanList.add(ViewDetailInfoBean("Class Type", view.toString()))
        viewDetailInfoBeanList.add(ViewDetailInfoBean("宽度", view.width.toString()))
        viewDetailInfoBeanList.add(ViewDetailInfoBean("高度", view.height.toString()))

        viewDetailInfoBeanList.add(ViewDetailInfoBean("父控件", viewParent.toString()))
        viewDetailInfoBeanList.add(ViewDetailInfoBean("子控件", childViewInfoStr))
        viewDetailInfoBeanList.add(
            ViewDetailInfoBean(
                "相对父控件rect",
                "[${view.left}, ${view.top}, ${view.right}, ${view.bottom}]"
            )
        )
        viewDetailInfoBeanList.add(
            ViewDetailInfoBean("相对屏幕rect", viewInfoBean.rect.toString())
        )
        viewDetailInfoBeanList.add(
            ViewDetailInfoBean(
                "isClickable",
                view.isClickable.toString()
            )
        )
        viewDetailInfoBeanList.add(
            ViewDetailInfoBean(
                "isEnabled",
                view.isEnabled.toString()
            )
        )
        viewDetailInfoBeanList.add(
            ViewDetailInfoBean(
                "visibility",
                view.visibility.toString()
            )
        )
        viewDetailInfoBeanList.add(
            ViewDetailInfoBean(
                "alpha",
                view.alpha.toString()
            )
        )
        viewDetailInfoBeanList.add(
            ViewDetailInfoBean(
                "isSelected",
                view.isSelected.toString()
            )
        )

        mTvTitle.text = "View详细消息"
        mRvViewInfo.visibility = View.VISIBLE
        mRvViewInfo.adapter = mDetailAdapter
        mDetailAdapter.updateData(viewDetailInfoBeanList)
        mRvViewInfo.layoutManager = LinearLayoutManager(mContext, RecyclerView.VERTICAL, false)

        isDetailShowing = true
    }
}