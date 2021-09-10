package com.roll.codemao.base.ui

import androidx.databinding.ViewDataBinding
import android.os.Bundle
import com.roll.codemao.app.data.api.HttpManager
import com.roll.codemao.app.data.api.service.ApiService
import com.roll.codemao.base.ui.helper.HttpHelper

/**
 * Created by grubber on 2017/1/6.
 */
abstract class BaseActivity<B : ViewDataBinding?> : RootActivity<B>() {

    @JvmField
    var httpHelper: HttpHelper? = null

    @JvmField
    var apiService: ApiService? = null

    override fun onCreate(savedInstanceState: Bundle?) {
        initHttp()
        onCreateBefore()
        super.onCreate(savedInstanceState)
        onCreateAfter()
        initData()
    }

    private fun obtainApiService(): ApiService? {
        if(apiService == null){
            apiService = HttpManager.getInstance().create(ApiService::class.java)
        }
        return apiService
    }

    open fun onCreateBefore(){

    }

    open fun onCreateAfter(){

    }

    override fun onResume() {
        super.onResume()
        initHttp()
    }

    private fun obtainHttp(): HttpHelper {
        if (httpHelper == null) {
            httpHelper = HttpHelper(this)
        }
        return httpHelper!!
    }

    private fun initHttp() {
        obtainHttp()
        obtainApiService()
    }

    protected abstract fun initData()

}