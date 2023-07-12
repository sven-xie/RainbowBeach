package com.sven.rainbowbeachlib.widgets

import android.content.Context
import android.graphics.Canvas
import android.graphics.Color
import android.graphics.Paint
import android.util.AttributeSet
import android.view.View
import com.sven.rainbowbeachlib.bean.IndexTextBean
import com.sven.rainbowbeachlib.bean.ViewInfoBean
import kotlin.math.abs

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
    private var textSize = dip2px(12F).toFloat()
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
        mRectPaint.textSize = textSize
        statusBarHeight = getStatusBarHeight()
    }

    fun setViewRectList(rectList: MutableList<ViewInfoBean>?) {
        this.rectList = rectList
        invalidate()
    }

    override fun onDraw(canvas: Canvas?) {
        super.onDraw(canvas)
        canvas ?: return
        val indexTextList = mutableListOf<IndexTextBean>()
        rectList?.forEachIndexed { index, viewInfoBean ->
            val indexStr = index.toString()
            if (viewInfoBean.checkStatus == ViewInfoBean.CHECK_STATUS_CHECKED) {
                mRectPaint.color = Color.YELLOW
                mRectPaint.strokeWidth = checkedStrokeWidth
                canvas.drawRect(
                    viewInfoBean.rect.left.toFloat(),
                    (viewInfoBean.rect.top - statusBarHeight).toFloat(),
                    viewInfoBean.rect.right.toFloat(),
                    (viewInfoBean.rect.bottom - statusBarHeight).toFloat(),
                    mRectPaint
                )
                filterIndexText(
                    indexStr,
                    viewInfoBean.rect.centerX().toFloat(),
                    viewInfoBean.rect.top - statusBarHeight + textSize / 3,
                    indexTextList
                )
            } else if (viewInfoBean.checkStatus == ViewInfoBean.CHECK_STATUS_UNCHECK) {
                mRectPaint.color = Color.GREEN
                mRectPaint.strokeWidth = defStrokeWidth
                canvas.drawRect(
                    viewInfoBean.rect.left.toFloat(),
                    (viewInfoBean.rect.top - statusBarHeight).toFloat(),
                    viewInfoBean.rect.right.toFloat(),
                    (viewInfoBean.rect.bottom - statusBarHeight).toFloat(),
                    mRectPaint
                )
                filterIndexText(
                    indexStr,
                    viewInfoBean.rect.centerX().toFloat(),
                    viewInfoBean.rect.top - statusBarHeight + textSize / 3,
                    indexTextList
                )
            } else {
                mRectPaint.color = Color.RED
                mRectPaint.strokeWidth = defStrokeWidth
                canvas.drawRect(
                    viewInfoBean.rect.left.toFloat(),
                    (viewInfoBean.rect.top - statusBarHeight).toFloat(),
                    viewInfoBean.rect.right.toFloat(),
                    (viewInfoBean.rect.bottom - statusBarHeight).toFloat(),
                    mRectPaint
                )
            }
        }

        indexTextList.forEach {
            mRectPaint.color = Color.GREEN
            mRectPaint.strokeWidth = 0F
            mRectPaint.textSize = textSize
            val measureText = mRectPaint.measureText(it.index)
            canvas.drawText(
                it.index,
                0,
                it.index.length,
                it.x - measureText / 2,
                it.y,
                mRectPaint
            )
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

    private fun filterIndexText(
        index: String,
        x: Float,
        y: Float,
        indexTextBeanList: MutableList<IndexTextBean>
    ) {
        indexTextBeanList.forEach {
            if (abs(it.x - x) < 10 && abs(it.y - y) < 10) {
                it.index = it.index + "," + index
                return
            }
        }
        indexTextBeanList.add(IndexTextBean(index, x, y))
    }
}