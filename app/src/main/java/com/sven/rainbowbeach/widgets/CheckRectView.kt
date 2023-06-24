package com.sven.rainbowbeach.widgets

import android.content.Context
import android.graphics.Canvas
import android.graphics.Paint
import android.graphics.Rect
import android.util.AttributeSet
import android.view.View
import androidx.core.content.ContextCompat
import com.sven.rainbowbeach.R
import com.sven.rainbowbeach.util.DisplayUtil
import kotlin.random.Random

/**
 * @Author:         xwp
 * @CreateDate:     2023/6/24
 * @Version:        1.0
 */
class CheckRectView(context: Context, attrs: AttributeSet? = null, defStyle: Int = -1) :
    View(context, attrs, defStyle) {

    private var mRectPaint: Paint = Paint()
    private var rectList: MutableList<Rect>? = null
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
        mRectPaint.strokeWidth = DisplayUtil.dip2px(1F).toFloat()
    }

    fun setViewRectList(rectList: MutableList<Rect>) {
        this.rectList = rectList
        invalidate()
    }

    override fun onDraw(canvas: Canvas?) {
        super.onDraw(canvas)
        canvas ?: return
        rectList?.forEach {
            mRectPaint.color = mColorList[Random.nextInt(mColorList.size - 1)]
            canvas.drawRect(it, mRectPaint)
        }
    }
}