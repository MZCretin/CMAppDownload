package com.roll.codemao.ui

import android.os.Bundle
import android.view.View
import com.roll.codemao.R
import com.roll.codemao.base.ui.BaseActivity
import com.roll.codemao.databinding.ActivityTestMvpFragmentBinding
import com.roll.codemao.fragment.InfoMvpFragment

class TestMvpFragmentActivity : BaseActivity<ActivityTestMvpFragmentBinding>() {

    override fun initData() {
        supportFragmentManager.beginTransaction()
                .add(R.id.fragment_container,
                        InfoMvpFragment.newInstance("嘻嘻")).commit()
    }

    override fun initView(view: View?, savedInstanceState: Bundle?) {
        showContentView()
    }

    override fun useCommonHeader(): Boolean {
        return true
    }

    override val contentViewId = R.layout.activity_test_mvp_fragment

    override fun refreshPageData() {

    }
}