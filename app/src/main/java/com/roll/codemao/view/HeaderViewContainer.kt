package com.roll.codemao.view

import android.app.Activity
import android.content.Context
import androidx.annotation.StringRes
import androidx.constraintlayout.widget.ConstraintLayout
import android.util.AttributeSet
import android.view.View
import android.widget.ImageView
import android.widget.LinearLayout
import android.widget.TextView
import com.roll.codemao.R

class HeaderViewContainer constructor(context: Context, attributes: AttributeSet? = null, defStyleAttr: Int = 0) : LinearLayout(context, attributes, defStyleAttr) {

    private var titleView: TextView? = null
    private var ivBack: ImageView? = null
    private var headerContainer: ConstraintLayout? = null
    private var ivRight: ImageView? = null
    private var tvRight: TextView? = null
    private var lineDivider: View? = null
    private var llFragmentContainer:LinearLayout? = null

    init {
        View.inflate(context, R.layout.layout_header_view_container, this)
        findView()
    }

    private fun findView() {
        llFragmentContainer = findViewById(R.id.ll_fragment_container)
        titleView = findViewById(R.id.tv_title_info)
        ivBack = findViewById(R.id.iv_back)
        headerContainer = findViewById(R.id.header_container)
        ivRight = findViewById(R.id.iv_right)
        tvRight = findViewById(R.id.tv_right)
        lineDivider = findViewById(R.id.line_divider)

        ivBack?.setOnClickListener {
            if (context is Activity) {
                (context as Activity).finish()
            }
        }
    }

    fun setHeaderPaddingTop(statusBarHeight: Int) {
        headerContainer?.setPadding(0, statusBarHeight, 0, 0)
    }

    internal fun setContentView(rootView: View) {
        llFragmentContainer?.addView(rootView, LayoutParams(LayoutParams.MATCH_PARENT, LayoutParams.MATCH_PARENT))
    }

    /**
     * 设置标题
     */
    fun setPageTitle(title: String) {
        titleView?.text = title
    }

    /**
     * 设置标题
     */
    fun setPageTitle(@StringRes titleRes: Int) {
        titleView?.setText(titleRes)
    }

    /**
     * 绑定头部的点击事件
     */
    fun bindClickListener(listener: (id: Int, view: View) -> Unit) {
        ivBack?.setOnClickListener {
            if (context is Activity) {
                (context as Activity).finish()
            }
            listener(R.id.iv_back, it)
        }

        ivRight?.setOnClickListener {
            listener(R.id.iv_right, it)
        }

        tvRight?.setOnClickListener {
            listener(R.id.tv_right, it)
        }
    }

    fun getTitleView(): TextView? {
        return titleView
    }

    fun getRightTextView(): TextView? {
        return tvRight
    }

    fun getRightImageView(): ImageView? {
        return ivRight
    }

    fun getBackBtn(): ImageView? {
        return ivBack
    }
}