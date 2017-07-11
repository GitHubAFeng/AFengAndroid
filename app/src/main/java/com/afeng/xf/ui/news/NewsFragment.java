package com.afeng.xf.ui.news;

import android.os.Bundle;

import com.afeng.xf.R;
import com.afeng.xf.base.BaseFragment;

/**
 * Created by Administrator on 2017/5/7.
 */

public class NewsFragment extends BaseFragment {





    private static NewsFragment instance;
    public static NewsFragment getInstance() {
        if (instance == null) {
            synchronized (NewsFragment.class) {
                if (instance == null) {
                    instance = new NewsFragment();
                }
            }
        }
        return instance;
    }

    /**
     * 初始化布局,返回layout
     */
    @Override
    protected int getLayoutId() {
        return R.layout.fragment_news;
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
}
