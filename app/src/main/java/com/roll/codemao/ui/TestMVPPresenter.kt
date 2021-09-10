package com.roll.codemao.ui

import com.roll.codemao.base.ui.IView
import com.roll.codemao.base.ui.RootPresenter
import com.google.gson.Gson

class TestMVPPresenter : RootPresenter<TestMVPPresenter.View>() {

    interface View : IView {
        fun showData(toJson: String)
    }

    fun getData() {
        httpHelper?.doNetWork(apiService?.testData2(),{
            mView?.showData(Gson().toJson(it))
        })
    }

}