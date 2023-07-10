package com.sven.rainbowbeachlib.widgets

import android.content.Context
import android.graphics.Canvas
import android.graphics.Color
import android.graphics.Paint
import android.util.AttributeSet
import android.view.View
import com.sven.rainbowbeachlib.bean.ViewInfoBean

/**
 * @Author:         xwp
 * @CreateDate:     2023/6/24
 * @Version:    c   1.0
 */
class ViewInfoRectView @JvmOverloads constructor(
    context: Context,
    attrs: AttributeSet? = null,
    defStyle: Int = -1
) :
    View(context, attrs, defStyle) {
    private var mRectPaint: Paint = Paint()
    private var rectList: MutableList<ViewInfoBean>? = null
    private var statusBarHeight = 0
    private var defStrokeWidth = dip2px(1F).toFloat()
    private var checkedStrokeWidth = dip2px(3F).toFloat()
    private val mColorList by lazy {
        arrayListOf(
            Color.BLACK,
            Color.BLUE,
            Color.CYAN,
            Color.DKGRAY,
            Color.GRAY,
            Color.GREEN,
            Color.MAGENTA,
            Color.RED,
        )
    }

    init {
        mRectPaint.color = Color.GREEN
        mRectPaint.style = Paint.Style.STROKE

        statusBarHeight = getStatusBarHeight()
    }

    fun setViewRectList(rectList: MutableList<ViewInfoBean>?) {
        this.rectList = rectList
        invalidate()
    }

    override fun onDraw(canvas: Canvas?) {
        super.onDraw(canvas)
        canvas ?: return
        rectList?.forEach {
            if (it.checkStatus == ViewInfoBean.CHECK_STATUS_CHECKED) {
                mRectPaint.color = Color.YELLOW
                mRectPaint.strokeWidth = checkedStrokeWidth
                canvas.drawRect(
                    it.rect.left.toFloat(),
                    (it.rect.top - statusBarHeight).toFloat(),
                    it.rect.right.toFloat(),
                    (it.rect.bottom - statusBarHeight).toFloat(),
                    mRectPaint
                )
            } else if (it.checkStatus == ViewInfoBean.CHECK_STATUS_UNCHECK) {
                mRectPaint.color = Color.RED
                mRectPaint.strokeWidth = defStrokeWidth
                canvas.drawRect(
                    it.rect.left.toFloat(),
                    (it.rect.top - statusBarHeight).toFloat(),
                    it.rect.right.toFloat(),
                    (it.rect.bottom - statusBarHeight).toFloat(),
                    mRectPaint
                )
            } else {
                mRectPaint.color = Color.GREEN
                mRectPaint.strokeWidth = defStrokeWidth
                canvas.drawRect(
                    it.rect.left.toFloat(),
                    (it.rect.top - statusBarHeight).toFloat(),
                    it.rect.right.toFloat(),
                    (it.rect.bottom - statusBarHeight).toFloat(),
                    mRectPaint
                )
            }
        }
    }


    private fun dip2px(dipValue: Float): Int {
        val scale = resources.displayMetrics.density
        return (dipValue * scale + 0.5f).toInt()
    }

    //获取状态栏的高度
    private fun getStatusBarHeight(): Int {
        val resourceId: Int =
            resources.getIdentifier("status_bar_height", "dimen", "android")
        return if (resourceId > 0) {
            resources.getDimensionPixelSize(resourceId)
        } else 0
    }
}