package com.sven.rainbowbeachlib.tools

import android.content.Context
import android.widget.Toast

/**
 * @Author:         xwp
 * @CreateDate:     2023/7/10
 * @Version:        1.0
 */
object RbbUtils {

    fun showToast(context: Context, message: String) {
        Toast.makeText(context, message, Toast.LENGTH_SHORT).show()
    }
}