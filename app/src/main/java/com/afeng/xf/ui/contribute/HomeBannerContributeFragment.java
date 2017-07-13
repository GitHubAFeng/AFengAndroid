package com.afeng.xf.ui.contribute;

import android.os.Bundle;

import com.afeng.xf.R;
import com.afeng.xf.base.BaseFragment;

/**
 * Created by Administrator on 2017/7/13.
 */

public class HomeBannerContributeFragment extends BaseFragment {

    private static HomeBannerContributeFragment instance;

    public static HomeBannerContributeFragment getInstance() {
        if (instance == null) {
            synchronized (HomeBannerContributeFragment.class) {
                if (instance == null) {
                    instance = new HomeBannerContributeFragment();
                }
            }
        }
        return instance;
    }


    @Override
    protected int getLayoutId() {
        return R.layout.fragment_fuli_contribute;
    }

    @Override
    protected void initView(Bundle savedInstanceState) {

    }

    @Override
    protected void setListener() {

    }

    @Override
    protected void processLogic(Bundle savedInstanceState) {

    }

    @Override
    protected void onLazyLoadOnce() {

    }

    @Override
    protected void onVisibleToUser() {

    }

    @Override
    protected void onInvisibleToUser() {

    }


}
