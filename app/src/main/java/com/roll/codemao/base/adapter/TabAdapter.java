package com.roll.codemao.base.adapter;

import androidx.fragment.app.Fragment;
import androidx.fragment.app.FragmentManager;
import androidx.fragment.app.FragmentStatePagerAdapter;


import java.util.List;

/**
 * Created by cretin on 2018/3/17.
 */
public class TabAdapter extends FragmentStatePagerAdapter {
    private List<Fragment> fragments;
    private List<String> tabTitle;

    public TabAdapter(FragmentManager fm, List<Fragment> fragments) {
        super(fm);
        this.fragments = fragments;
    }

    public void setTabTitle(List<String> tabTitle) {
        this.tabTitle = tabTitle;
    }

    @Override
    public Fragment getItem(int position) {
        return fragments.get(position);
    }

    @Override
    public int getCount() {
        return fragments.size();
    }

    //设置tablayout标题
    @Override
    public CharSequence getPageTitle(int position) {
        return tabTitle.get(position);
    }
}
