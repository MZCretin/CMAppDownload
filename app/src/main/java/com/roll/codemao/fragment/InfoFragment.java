package com.roll.codemao.fragment;

import android.os.Bundle;
import androidx.fragment.app.Fragment;
import android.view.View;

import com.roll.codemao.R;
import com.roll.codemao.base.ui.RootFragment;
import com.roll.codemao.databinding.FragmentInfoBinding;

/**
 * A simple {@link Fragment} subclass.
 */
public class InfoFragment extends RootFragment<FragmentInfoBinding> {
    public static final String TAG = "InfoFragment";

    public static InfoFragment newInstance(String title, boolean showTitle) {
        Bundle args = new Bundle();
        args.putString("title", title);
        args.putBoolean("showTitle", showTitle);
        InfoFragment fragment = new InfoFragment();
        fragment.setArguments(args);
        return fragment;
    }

    public static InfoFragment newInstance(String title) {
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
}
