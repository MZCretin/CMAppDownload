package com.roll.codemao.ui

import android.os.Bundle
import android.view.View
import com.roll.codemao.R
import com.roll.codemao.base.ui.RootActivity
import com.roll.codemao.databinding.ActivityTestExtendRootBinding

class TestExtendRootActivity : RootActivity<ActivityTestExtendRootBinding>() {

    override fun initView(view: View?, savedInstanceState: Bundle?) {

    }

    override val contentViewId: Int = R.layout.activity_test_extend_root

    override fun refreshPageData() {

    }

    override fun useStatusBarInit(): Boolean {
        return super.useStatusBarInit()
    }
}