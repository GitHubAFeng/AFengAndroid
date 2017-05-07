package com.youth.xf.ui.demo.home;

import android.content.Context;
import android.os.Bundle;
import android.support.v4.app.Fragment;
import android.support.v4.app.FragmentManager;
import android.support.v4.app.FragmentPagerAdapter;
import android.support.v4.view.ViewPager;

import com.flyco.tablayout.SlidingTabLayout;
import com.flyco.tablayout.listener.OnTabSelectListener;
import com.youth.xf.R;

import com.youth.xf.base.AFengFragment;
import com.youth.xf.ui.demo.meizi.MeiZiFragment;
import com.youth.xf.ui.demo.more.MoreFragment;
import com.youth.xf.ui.demo.news.NewsFragment;

import java.util.ArrayList;
import java.util.List;

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

    /**
     * 懒加载一次。如果只想在对用户可见时才加载数据，并且只加载一次数据，在子类中重写该方法
     */
    @Override
    protected void onLazyLoadOnce() {

    }

    /**
     * 对用户可见时触发该方法。如果只想在对用户可见时才加载数据，在子类中重写该方法
     */
    @Override
    protected void onVisibleToUser() {

    }

    /**
     * 对用户不可见时触发该方法
     */
    @Override
    protected void onInvisibleToUser() {

    }


    private void initVP() {

        final String[] mTitles = {
                "首页", "头条", "妹纸", "更多"
        };

//        for (String title : mTitles) {
//            fragmentArrayList.add(SimpleFragment.getInstance(title));
//        }

        fragmentArrayList.add(SimpleFragment.getInstance("首页"));
        fragmentArrayList.add(NewsFragment.getInstance());
        fragmentArrayList.add(MeiZiFragment.getInstance());
        fragmentArrayList.add(MoreFragment.getInstance());


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

        mViewPager.setCurrentItem(0);

    }


    @Override
    public void onAttach(Context context) {
        super.onAttach(context);
        this.mContext = context;
    }


    class MyFragmentPagerAdapter extends FragmentPagerAdapter {
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


}
