package com.roll.codemao.base.ui

import androidx.databinding.ViewDataBinding
import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import com.roll.codemao.app.data.api.HttpManager
import com.roll.codemao.app.data.api.service.ApiService
import com.roll.codemao.base.ui.helper.HttpHelper

abstract class BaseFragment<RB : ViewDataBinding?> : RootFragment<RB>() {
    private var isViewInitiated = false
    private var isDataInitiated = false

    @JvmField
    var httpHelper: HttpHelper? = null

    @JvmField
    var apiService: ApiService? = null

    override fun onCreateView(inflater: LayoutInflater, container: ViewGroup?, savedInstanceState: Bundle?): View? {
        val rootView = super.onCreateView(inflater, container, savedInstanceState)
        initHttp()
        beforeInitData()
        initData()
        afterInitData()
        if (useLazyFragment())
            isViewInitiated = true
        return rootView
    }

    open fun beforeInitData(){

    }

    open fun afterInitData(){

    }

    override fun setUserVisibleHint(isVisibleToUser: Boolean) {
        super.setUserVisibleHint(isVisibleToUser)
        //如果用户需要懒加载数据才去判断
        if (useLazyFragment() && isVisibleToUser && isViewInitiated && !isDataInitiated) {
            fetchData()
            isDataInitiated = true
        }
    }

    /**
     * 如果你希望页面进来才加载数据的话
     */
    open fun fetchData() {

    }

    override fun onResume() {
        super.onResume()
        initHttp()
    }

    private fun initHttp() {
        obtainHttp()
        obtainApiService()
    }

    private fun obtainHttp(): HttpHelper {
        if (httpHelper == null) {
            if (mActivity is RootActivity<*>)
                httpHelper = HttpHelper(mActivity as RootActivity<*>)
            else {
                throw RuntimeException("宿主请使用RootActivity")
            }
        }
        return httpHelper!!
    }

    private fun obtainApiService(): ApiService? {
        if (apiService == null) {
            apiService = HttpManager.getInstance().create(ApiService::class.java)
        }
        return apiService
    }

    protected abstract fun initData()

    open fun useLazyFragment(): Boolean {
        return false
    }
}