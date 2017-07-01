package com.afeng.xf.ui.demo.home;

import android.os.Bundle;

import com.afeng.xf.R;
import com.afeng.xf.base.BaseActivity;
import com.afeng.xf.utils.AFengUtils.StatusBarUtil;

/**
 * Created by Administrator on 2017/6/26.
 */

public class AboutMeActivity extends BaseActivity {


    @Override
    protected int getLayoutId() {
        return R.layout.activity_aboutme;
    }

    @Override
    protected void initView(Bundle savedInstanceState) {
        StatusBarUtil.hideSystemUI(this);

    }

    @Override
    protected void setListener() {

    }

    @Override
    protected void processLogic(Bundle savedInstanceState) {

    }
}
