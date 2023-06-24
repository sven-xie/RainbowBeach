package com.sven.rainbowbeach.widgets

import android.content.Context
import android.util.AttributeSet
import android.widget.LinearLayout
import androidx.appcompat.widget.AppCompatTextView

/**
 * @Author:         xwp
 * @CreateDate:     2023/1/4
 * @Version:        1.0
 */
class CommonButtonLinearlayout @JvmOverloads constructor(
    context: Context,
    attrs: AttributeSet? = null,
    defStyleAttr: Int = 0
) : LinearLayout(context, attrs, defStyleAttr) {

    override fun setPressed(pressed: Boolean) {
        super.setPressed(pressed)
        alpha = if (pressed) {
            0.6f
        } else {
            1.0f
        }
    }
}