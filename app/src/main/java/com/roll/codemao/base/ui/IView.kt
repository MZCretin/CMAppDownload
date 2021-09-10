package com.roll.codemao.base.ui

import com.trello.rxlifecycle2.components.support.RxAppCompatActivity

/**
 * author： deemons
 * date:    2018/4/25
 * desc:
 */
interface IView : BaseView {

    fun getContext(): RxAppCompatActivity

    fun showToast(msg: String?)
}
