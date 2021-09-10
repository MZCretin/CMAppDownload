package com.roll.codemao.ui

import android.content.Intent
import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import android.text.TextUtils
import android.view.View
import com.blankj.utilcode.util.ActivityUtils
import com.roll.codemao.R
import com.roll.codemao.app.KV
import com.roll.codemao.app.LocalStorageKeys
import com.roll.codemao.app.data.api.ApiConfig
import com.roll.codemao.app.data.api.model.event.NotifyTokenBind
import com.roll.codemao.base.ui.BaseActivity
import com.roll.codemao.databinding.ActivityBindTokenBinding
import org.greenrobot.eventbus.EventBus

class BindTokenActivity : BaseActivity<ActivityBindTokenBinding>() {
    override fun useCommonHeader(): Boolean {
        return true
    }

    override fun initData() {

    }

    override fun initView(view: View?, savedInstanceState: Bundle?) {
        headerViewHelper?.setPageTitle("绑定API_KEY")
        showContentView()

        var localKey = KV.get<String>(LocalStorageKeys.LOCAL_API_KEY)
        mBinding().edApiKey.setText(localKey)

        mBinding().tvBtn.setOnClickListener {
            val apiKey = mBinding().edApiKey.text.toString().trim()
            if (TextUtils.isEmpty(apiKey)) {
                showToast("APIKEY不能为空")
                return@setOnClickListener
            }
            if (apiKey.length != 32) {
                showToast("APIKEY的长度为32")
                return@setOnClickListener
            }
            KV.put(LocalStorageKeys.LOCAL_API_KEY, apiKey)
            EventBus.getDefault().post(NotifyTokenBind())
            if (!ActivityUtils.isActivityExistsInStack(MainActivity::class.java)) {
                startActivity(Intent(this, MainActivity::class.java))
            }
            finish()
        }
    }

    override val contentViewId = R.layout.activity_bind_token

    override fun refreshPageData() {

    }

}