package com.afeng.xf.ui.contribute;

import android.graphics.Color;
import android.os.Bundle;
import android.support.v4.app.Fragment;
import android.support.v4.app.FragmentManager;
import android.support.v4.app.FragmentPagerAdapter;
import android.support.v4.view.ViewPager;
import android.support.v7.app.ActionBar;
import android.support.v7.widget.Toolbar;

import com.afeng.xf.R;
import com.afeng.xf.base.BaseActivity;
import com.flyco.tablayout.SlidingTabLayout;
import com.flyco.tablayout.listener.OnTabSelectListener;


import butterknife.BindView;

/**
 * Created by Administrator on 2017/7/13.
 */

public class ContributeActivity extends BaseActivity {

    @BindView(R.id.main_contribute_viewPager)
    ViewPager mMainViewPager;

    @BindView(R.id.main_contribute_tab)
    SlidingTabLayout mMainTabLayout;

    @BindView(R.id.main_contribute_toolbar)
    Toolbar toolbar;


    //tab的标题
    public String[] mTitles = {
            "首页轮播",
            "首页下拉",
            "福利头部",
            "福利下拉"
    };

    // 与标题对应的Fragment
    public Fragment[] mFragments = {

            HomeBannerContributeFragment.getInstance(),
            HomeRvItemContributeFragment.getInstance(),
            FuLiHeadContributeFragment.getInstance(),
            FuLiContributeFragment.getInstance()
    };


    @Override
    protected int getLayoutId() {
        return R.layout.activity_contribute;
    }

    @Override
    protected void initView(Bundle savedInstanceState) {
        initToolBar();
        initViewPager();

    }

    @Override
    protected void setListener() {

    }

    @Override
    protected void processLogic(Bundle savedInstanceState) {

    }


    private void initToolBar() {
        //自定义标题栏
        setSupportActionBar(toolbar);
        ActionBar actionBar = getSupportActionBar();
        if (actionBar != null) {
            //去除默认Title显示
            actionBar.setDisplayShowTitleEnabled(false);
        }

        // 设置标题栏
//        toolbar.setTitleTextColor(Color.WHITE);
//        toolbar.setTitle("内容管理");

        toolbar.setOnMenuItemClickListener(menuItem -> {
            switch (menuItem.getItemId()) {

            }
            return true;
        });

        // 设置了回退按钮，及点击事件的效果
        getSupportActionBar().setDisplayHomeAsUpEnabled(true);
        toolbar.setNavigationOnClickListener(v -> finish());

    }


    private void initViewPager() {


        mMainViewPager.setAdapter(new MainViewPagerAdapter(getSupportFragmentManager(), mFragments, mTitles));
        mMainTabLayout.setViewPager(mMainViewPager);
        mMainTabLayout.setOnTabSelectListener(new OnTabSelectListener() {

            @Override
            public void onTabSelect(int position) {


            }

            @Override
            public void onTabReselect(int position) {

            }
        });

        mMainViewPager.addOnPageChangeListener(new ViewPager.OnPageChangeListener() {
            @Override
            public void onPageScrolled(int position, float positionOffset, int positionOffsetPixels) {

            }

            @Override
            public void onPageSelected(int position) {
                mMainTabLayout.setCurrentTab(position);
            }

            @Override
            public void onPageScrollStateChanged(int state) {

            }
        });

        mMainViewPager.setCurrentItem(0);
    }


    class MainViewPagerAdapter extends FragmentPagerAdapter {

        private Fragment[] mAdapterFragments;
        private String[] mAdapterTitles;

        public MainViewPagerAdapter(FragmentManager fm, Fragment[] fragments, String[] titles) {
            super(fm);
            this.mAdapterFragments = fragments;
            this.mAdapterTitles = titles;
        }

        @Override
        public Fragment getItem(int i) {
            return mAdapterFragments[i];
        }

        @Override
        public int getCount() {
            return mAdapterFragments.length;
        }

        @Override
        public CharSequence getPageTitle(int position) {
            return mAdapterTitles[position];
        }
    }


}
