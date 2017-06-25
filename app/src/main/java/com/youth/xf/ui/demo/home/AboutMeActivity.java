package com.youth.xf.ui.demo.home;

import android.os.Bundle;

import com.youth.xf.R;
import com.youth.xf.base.BaseActivity;
import com.youth.xf.utils.AFengUtils.StatusBarUtil;

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
