package com.roll.codemao.ui

import android.os.Bundle
import android.util.Log
import android.view.View
import com.roll.codemao.R
import com.roll.codemao.base.ui.BaseActivity
import com.roll.codemao.base.ui.expand.kotlin.*
import com.roll.codemao.base.ui.helper.*
import com.roll.codemao.databinding.ActivityTestBinding
import com.google.gson.Gson
import com.roll.www.zhulishitidian.utils.expand.setOnClick

class TestActivity : BaseActivity<ActivityTestBinding>() {

    override val contentViewId: Int = R.layout.activity_test

    override fun initData() {
        showContentView()

        mBinding().tvTestMethod.setOnClick {
            httpHelper?.doNetWork(apiService!!.testData2(), {
                showToast(Gson().toJson(it))
            })
        }

        mBinding().tvTestRxjava.setOnClick {
            apiService?.testData()
                    ?.io_()
                    ?._io()
                    ?.flatMap {
                        apiService?.testData1()
                    }
                    ?.io_main()
                    ?.bindLoadingDialog(this)
                    ?.handlerResult(this, {
                        Log.e("","")
                    })
        }

        mBinding().image1.loadLocalImage(R.mipmap.image_test)

        mBinding().image.createConfig()
                .bindCornerType(10f,ImageEngineCallback.CornerType.BOTTOM_LEFT)
                .loadNetImage("https://timgsa.baidu.com/timg?image&quality=80&size=b9999_10000&sec=1605171101674&di=774671a6011c8a1c45649f2926d9498d&imgtype=0&src=http%3A%2F%2Fa2.att.hudong.com%2F86%2F10%2F01300000184180121920108394217.jpg")

        headerViewHelper?.setPageTitle("你好呀大爷")
    }

    override fun useCommonHeader(): Boolean {
        return true
    }

    override fun refreshPageData() {
        httpHelper?.doNetWork(apiService!!.testData2(), {
            Log.e("","")
        })
    }

    override fun initView(view: View?, savedInstanceState: Bundle?) {




    }
}