package com.sven.rainbowbeach.widgets

import android.content.Context
import android.graphics.Canvas
import android.graphics.Paint
import android.util.AttributeSet
import android.view.View
import androidx.core.content.ContextCompat
import com.sven.rainbowbeach.R
import com.sven.rainbowbeachlib.bean.ViewInfoBean
import kotlin.random.Random

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
    private val mColorList by lazy {
        arrayListOf(
            ContextCompat.getColor(context, R.color.yellow),
            ContextCompat.getColor(context, R.color.black),
            ContextCompat.getColor(context, R.color.blue),
            ContextCompat.getColor(context, R.color.Green),
            ContextCompat.getColor(context, R.color.Grey)
        )
    }

    init {
        mRectPaint.color = ContextCompat.getColor(context, R.color.yellow)
        mRectPaint.style = Paint.Style.STROKE
        mRectPaint.strokeWidth = dip2px(1F).toFloat()
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
            mRectPaint.color = mColorList[Random.nextInt(mColorList.size - 1)]
            canvas.drawRect(
                it.rect.left.toFloat(),
                (it.rect.top - statusBarHeight).toFloat(),
                it.rect.right.toFloat(),
                (it.rect.bottom - statusBarHeight).toFloat(),
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
}