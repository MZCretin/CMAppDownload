package com.roll.codemao.base.ui.helper

import androidx.annotation.StringRes
import android.widget.ImageView
import android.widget.TextView
import com.roll.codemao.view.HeaderViewContainer

/**
 * 头部View通用处理
 */
class HeaderViewHelper {
    var headerViewContainer: HeaderViewContainer? = null

    internal fun bindHeaderView(headerViewContainer: HeaderViewContainer) {
        this.headerViewContainer = headerViewContainer
    }

    /**
     * 设置标题
     */
    fun setPageTitle(title: String) {
        headerViewContainer?.setPageTitle(title)
    }

    /**
     * 设置标题
     */
    fun setPageTitle(@StringRes titleRes: Int) {
        headerViewContainer?.setPageTitle(titleRes)
    }

    /**
     * 获取标题
     */
    fun getTitleView(): TextView? {
        return headerViewContainer?.getTitleView()
    }

    /**
     * 获取标题栏右边的图片控件
     */
    fun getRightImageView(): ImageView? {
        return headerViewContainer?.getRightImageView()
    }

    /**
     * 获取标题栏右边的文字图片
     */
    fun getRightTextView(): TextView? {
        return headerViewContainer?.getRightTextView()
    }
}