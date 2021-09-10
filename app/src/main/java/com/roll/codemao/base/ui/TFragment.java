package com.roll.codemao.base.ui;

import android.app.Activity;
import android.os.Bundle;
import androidx.annotation.Nullable;

import com.trello.rxlifecycle2.components.support.RxFragment;

/**
 * @date: on 2019/4/9
 * @author: a112233
 * @email: mxnzp_life@163.com
 * @desc: 添加描述
 */
public abstract class TFragment extends RxFragment {
    protected RootActivity mActivity;

    @Override
    public void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
    }

    @Override
    public void onAttach(Activity activity) {
        super.onAttach(activity);
        this.mActivity = (RootActivity) activity;
    }

    //获取宿主Activity
    protected RootActivity getHoldingActivity() {
        return mActivity;
    }

}