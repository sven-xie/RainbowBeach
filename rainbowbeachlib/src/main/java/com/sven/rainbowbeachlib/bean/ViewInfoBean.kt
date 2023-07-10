package com.sven.rainbowbeachlib.bean

import android.graphics.Rect
import android.view.View

/**
 * @Author:         xwp
 * @CreateDate:     2023/7/7
 * @Version:        1.0
 */
data class ViewInfoBean(
    var id: String,
    var rect: Rect,
    var attachPageName: String,
    var view: View? = null,
    var checkStatus: Int = CHECK_STATUS_NORMAL
) {
    companion object {
        const val CHECK_STATUS_NORMAL = 0
        const val CHECK_STATUS_UNCHECK = 1
        const val CHECK_STATUS_CHECKED = 2
    }
}
