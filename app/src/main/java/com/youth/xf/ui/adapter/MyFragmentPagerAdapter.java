package com.youth.xf.ui.adapter;

import android.support.v4.app.Fragment;
import android.support.v4.app.FragmentManager;
import android.support.v4.app.FragmentPagerAdapter;


import java.util.List;


public class MyFragmentPagerAdapter extends FragmentPagerAdapter {
    private List<?> fragments;
    private String[] titles;

    /**
     * 普通，主页使用
     */
    public MyFragmentPagerAdapter(FragmentManager fm, List<?> mFragment) {
        super(fm);
        this.fragments = mFragment;
    }

    public MyFragmentPagerAdapter(FragmentManager fm, List<?> fragments, String[] titles) {
        super(fm);
        this.fragments = fragments;
        this.titles = titles;
    }

    @Override
    public Fragment getItem(int position) {
        return (Fragment) fragments.get(position);
    }

    @Override
    public int getCount() {
        return fragments.size();
    }

    //此方法用来显示tab上的名字
    @Override
    public CharSequence getPageTitle(int position) {
        if (titles != null) {
            return titles[position];
        } else {
            return "";
        }
    }

    public void addFragmentList(List<?> fragment) {
        this.fragments.clear();
        this.fragments = null;
        this.fragments = fragment;
        notifyDataSetChanged();
    }


}