package com.youth.xf.ui.demo.fragments;

import android.content.Context;
import android.os.Bundle;
import android.support.v4.app.Fragment;
import android.support.v4.view.ViewPager;

import com.flyco.tablayout.SlidingTabLayout;
import com.flyco.tablayout.listener.OnTabSelectListener;
import com.youth.xf.R;
import com.youth.xf.ui.adapter.MyFragmentPagerAdapter;
import com.youth.xf.base.AFengFragment;

import java.util.ArrayList;

/**
 * Created by AFeng on 2017/3/4.
 */

public class OneFragment extends AFengFragment {

    private ArrayList<Fragment> fragmentArrayList = new ArrayList<>();
    private int[] mIconUnselectIds = {
            R.mipmap.tab_home_unselect, R.mipmap.tab_speech_unselect,
            R.mipmap.tab_contact_unselect, R.mipmap.tab_more_unselect};
    private int[] mIconSelectIds = {
            R.mipmap.tab_home_select, R.mipmap.tab_speech_select,
            R.mipmap.tab_contact_select, R.mipmap.tab_more_select};

    private ViewPager mViewPager;
    private SlidingTabLayout mTabLayout;
    private Context mContext;


    @Override
    public int getLayoutId() {
        return R.layout.afeng_one_fra;
    }

    @Override
    protected void initView(Bundle savedInstanceState) {
        mViewPager = getViewById(R.id.one_t1_vp);
        mTabLayout = getViewById(R.id.one_tl_tab);
    }

    @Override
    protected void setListener() {

    }

    @Override
    protected void processLogic(Bundle savedInstanceState) {
        initVP();
    }


    private void initVP() {

        final String[] mTitles = {
                "首页", "消息", "联系人", "更多"
        };

        for (String title : mTitles) {
            fragmentArrayList.add(SimpleFragment.getInstance(title));
        }


        mViewPager.setAdapter(new MyFragmentPagerAdapter(getChildFragmentManager(), fragmentArrayList, mTitles));

        mTabLayout.setViewPager(mViewPager);
        mTabLayout.setOnTabSelectListener(new OnTabSelectListener() {
            @Override
            public void onTabSelect(int position) {
                mViewPager.setCurrentItem(position);
            }

            @Override
            public void onTabReselect(int position) {

            }
        });

        mViewPager.addOnPageChangeListener(new ViewPager.OnPageChangeListener() {
            @Override
            public void onPageScrolled(int position, float positionOffset, int positionOffsetPixels) {

            }

            @Override
            public void onPageSelected(int position) {
                mTabLayout.setCurrentTab(position);
            }

            @Override
            public void onPageScrollStateChanged(int state) {

            }
        });

        mViewPager.setCurrentItem(1);

    }


    @Override
    protected void onVisible() {

    }

    @Override
    protected void onInvisible() {

    }

    @Override
    public void onAttach(Context context) {
        super.onAttach(context);
        this.mContext = context;
    }

}
