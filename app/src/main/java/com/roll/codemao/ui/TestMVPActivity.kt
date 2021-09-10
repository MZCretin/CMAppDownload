package com.roll.codemao.ui

import android.os.Bundle
import android.view.View
import com.roll.codemao.R
import com.roll.codemao.base.ui.BaseMVPActivity
import com.roll.codemao.databinding.ActivityTestMVPBinding

class TestMVPActivity : BaseMVPActivity<ActivityTestMVPBinding, TestMVPPresenter>(), TestMVPPresenter.View {

    override fun generatePresenter(): TestMVPPresenter? {
        return TestMVPPresenter()
    }

    override fun initData() {
        showContentView()
    }

    override fun initView(view: View?, savedInstanceState: Bundle?) {
        mBinding().tvTest.setOnClickListener {
            mPresenter?.getData()
        }
    }

    override val contentViewId: Int = R.layout.activity_test_m_v_p

    override fun refreshPageData() {

    }

    override fun showData(toJson: String) {
        showToast(toJson)
    }
}