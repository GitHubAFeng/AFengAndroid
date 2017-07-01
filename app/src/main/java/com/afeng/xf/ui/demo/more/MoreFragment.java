package com.afeng.xf.ui.demo.more;

import android.os.Bundle;

import com.afeng.xf.R;
import com.afeng.xf.base.AFengFragment;

/**
 * Created by Administrator on 2017/5/7.
 */

public class MoreFragment extends AFengFragment {





    private static MoreFragment instance;
    public static MoreFragment getInstance() {
        if (instance == null) {
            synchronized (MoreFragment.class) {
                if (instance == null) {
                    instance = new MoreFragment();
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
        return R.layout.fragment_more;
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
