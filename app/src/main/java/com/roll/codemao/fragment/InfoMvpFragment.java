package com.roll.codemao.fragment;

import android.os.Bundle;
import androidx.fragment.app.Fragment;
import android.view.View;

import com.roll.codemao.R;
import com.roll.codemao.base.ui.BaseMVPFragment;
import com.roll.codemao.databinding.FragmentInfoBinding;
import com.roll.codemao.fragment.presenter.InfoMvpPresenter;

import org.jetbrains.annotations.Nullable;

/**
 * A simple {@link Fragment} subclass.
 */
public class InfoMvpFragment extends BaseMVPFragment<FragmentInfoBinding, InfoMvpPresenter> implements InfoMvpPresenter.View {
    public static final String TAG = "InfoFragment";

    public static InfoMvpFragment newInstance(String title, boolean showTitle) {
        Bundle args = new Bundle();
        args.putString("title", title);
        args.putBoolean("showTitle", showTitle);
        InfoMvpFragment fragment = new InfoMvpFragment();
        fragment.setArguments(args);
        return fragment;
    }

    public static InfoMvpFragment newInstance(String title) {
        return newInstance(title, true);
    }

    @Override
    protected void refreshPageData() {

    }

    @Override
    protected int getContentViewId() {
        return R.layout.fragment_info;
    }

    @Override
    protected void initView(View contentView, Bundle savedInstanceState) {
        showContentView();
        if (getArguments() != null) {
            if (!getArguments().getBoolean("showTitle", true)) {
            }
        } else {
        }
    }

    @Nullable
    @Override
    public InfoMvpPresenter generatePresenter() {
        return new InfoMvpPresenter();
    }

    @Override
    protected void initData() {
        getPresenter().getData();
    }

    @Override
    public void lazyInit() {

    }
}
