package com.roll.codemao.base.ui

import com.roll.codemao.app.data.api.service.ApiService
import com.roll.codemao.base.ui.helper.HttpHelper

/**
 * desc:
 */
open class RootPresenter<V : IView> : IPresenter {
    var httpHelper: HttpHelper? = null
    var apiService: ApiService? = null

    override fun onDestroy() {
        mView = null
    }

    override fun start() {

    }

    override fun bind(httpHelper: HttpHelper?, apiService: ApiService?) {
        this.httpHelper = httpHelper
        this.apiService = apiService
    }

    override fun onResume() {

    }

    var mView: V? = null

    @Suppress("UNCHECKED_CAST")
    override fun attachView(view: IView) {
        mView = view as V
    }

}