package com.roll.codemao.base.ui

import androidx.databinding.ViewDataBinding
import com.trello.rxlifecycle2.components.support.RxAppCompatActivity

abstract class BaseMVPFragment<B : ViewDataBinding?, P : IPresenter> : LazyFragment<B>(), IView {
    internal var mPresenter: P? = null

    override fun beforeInitData() {
        mPresenter = getPresenter()
        mPresenter?.attachView(this)
        mPresenter?.bind(httpHelper, apiService)
        mPresenter?.start()
    }

    open fun getPresenter(): P? {
        if (mPresenter == null) {
            mPresenter = generatePresenter()
        }
        return mPresenter
    }

    override fun getActivityInstance(): RootActivity<*> {
        return mActivity!! as RootActivity<*>
    }

    abstract fun generatePresenter(): P?

    override fun getContext(): RxAppCompatActivity = mActivity as RxAppCompatActivity
}