package com.afeng.xf.ui.contribute;

import android.os.Bundle;

import com.afeng.xf.R;
import com.afeng.xf.base.BaseFragment;

/**
 * Created by Administrator on 2017/7/13.
 */

public class FuLiHeadContributeFragment extends BaseFragment {




    private static FuLiHeadContributeFragment instance;

    public static FuLiHeadContributeFragment getInstance() {
        if (instance == null) {
            synchronized (FuLiHeadContributeFragment.class) {
                if (instance == null) {
                    instance = new FuLiHeadContributeFragment();
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
