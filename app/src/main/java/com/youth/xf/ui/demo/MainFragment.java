package com.youth.xf.ui.demo;

import android.os.Bundle;
import android.support.design.widget.FloatingActionButton;
import android.support.v4.app.Fragment;
import android.support.v4.app.FragmentManager;
import android.support.v4.app.FragmentPagerAdapter;
import android.support.v4.view.ViewPager;
import android.view.View;

import com.youth.xf.R;

import com.youth.xf.base.AFengFragment;
import com.youth.xf.ui.demo.book.BookFragment;
import com.youth.xf.ui.demo.home.OneFragment;
import com.youth.xf.ui.demo.movie.MovieFragment;

import java.util.ArrayList;
import java.util.List;

import butterknife.BindView;

/**
 * Created by Administrator on 2017/6/13.
 */

public class MainFragment extends AFengFragment implements View.OnClickListener, ViewPager.OnPageChangeListener {


    @BindView(R.id.vp_content)
    ViewPager mViewPager;

    @BindView(R.id.main_fab)
    FloatingActionButton fab_btn;

    public static MainFragment newInstance() {
        MainFragment fragment = new MainFragment();
        return fragment;
    }

    /**
     * 初始化布局,返回layout
     */
    @Override
    protected int getLayoutId() {
        return R.layout.fragment_main;
    }

    /**
     * 初始化View控件
     *
     * @param savedInstanceState
     */
    @Override
    protected void initView(Bundle savedInstanceState) {


    }

    /**
     * 给View控件添加事件监听器
     */
    @Override
    protected void setListener() {

    }

    /**
     * 处理业务逻辑，状态恢复等操作
     *
     * @param savedInstanceState
     */
    @Override
    protected void processLogic(Bundle savedInstanceState) {
        initContent();
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

    @Override
    public void onPageScrolled(int i, float v, int i1) {

    }

    @Override
    public void onPageSelected(int i) {


    }

    @Override
    public void onPageScrollStateChanged(int i) {

    }


    /**
     * Called when a view has been clicked.
     *
     * @param v The view that was clicked.
     */
    @Override
    public void onClick(View v) {


    }



    /**
     * 初始化内容
     */
    private void initContent() {
        ArrayList<Fragment> mFragmentList = new ArrayList<>();

        mFragmentList.add(new OneFragment());
        mFragmentList.add(new BookFragment());
        mFragmentList.add(new MovieFragment());

        MyFragmentPagerAdapter adapter = new MyFragmentPagerAdapter(getChildFragmentManager(), mFragmentList);
        mViewPager.setAdapter(adapter);
        mViewPager.setOffscreenPageLimit(2);
        mViewPager.addOnPageChangeListener(this);

        mViewPager.setCurrentItem(0);
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
