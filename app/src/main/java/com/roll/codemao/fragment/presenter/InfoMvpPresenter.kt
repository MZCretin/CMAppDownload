package com.roll.codemao.fragment.presenter

import com.roll.codemao.base.ui.IView
import com.roll.codemao.base.ui.RootPresenter


class InfoMvpPresenter : RootPresenter<InfoMvpPresenter.View>() {

    interface View : IView {

    }

    fun getData(){
        httpHelper?.doNetWork(apiService?.testData(),success = {
            mView?.showToast(it.data.toString())
        })
    }
}