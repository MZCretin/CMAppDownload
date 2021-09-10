package com.roll.codemao.base.ui

import androidx.databinding.ViewDataBinding
import android.os.Bundle
import com.trello.rxlifecycle2.components.support.RxAppCompatActivity

abstract class BaseMVPActivity<B : ViewDataBinding?,P : IPresenter> : BaseActivity<B>(),IView{
    internal var mPresenter: P? = null

    override fun onCreate(savedInstanceState: Bundle?) {
        mPresenter = getPresenter()
        mPresenter?.start()
        super.onCreate(savedInstanceState)
        mPresenter?.attachView(this)
        mPresenter?.bind(httpHelper,apiService)
    }

    open fun getPresenter(): P? {
        if (mPresenter == null) {
            mPresenter = generatePresenter()
        }
        return mPresenter
    }

    override fun onDestroy() {
        if (mPresenter != null) {
            mPresenter?.onDestroy()
            mPresenter = null
        }
        super.onDestroy()
    }

    override fun getActivityInstance(): RootActivity<*> {
        return this
    }

    abstract fun generatePresenter(): P?

    override fun getContext(): RxAppCompatActivity = this
}