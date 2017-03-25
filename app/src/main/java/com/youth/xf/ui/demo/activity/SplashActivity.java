package com.youth.xf.ui.demo.activity;

import android.content.Intent;
import android.os.Bundle;

import com.youth.xf.base.AFengActivity;
import com.youth.xf.R;
import com.youth.xf.ui.demo.MainActivity;

import cn.bingoogolapple.bgabanner.BGABanner;

/**
 * Created by AFeng on 2017/3/8.
 */

public class SplashActivity extends AFengActivity {

    private BGABanner mBackgroundBanner;
    private BGABanner mForegroundBanner;

    @Override
    public int getLayoutId() {
        return R.layout.splash_main;
    }

    @Override
    protected void initView(Bundle savedInstanceState) {
        mBackgroundBanner = getViewById(R.id.banner_guide_background);
        mForegroundBanner = getViewById(R.id.banner_guide_foreground);
    }

    @Override
    protected void setListener() {
        //这个事件必须在数据源绑定前设置，否则会导致按钮不能正确显示
        /**
         * 设置进入按钮和跳过按钮控件资源 id 及其点击事件
         * 如果进入按钮和跳过按钮有一个不存在的话就传 0
         * 在 BGABanner 里已经帮开发者处理了防止重复点击事件
         * 在 BGABanner 里已经帮开发者处理了「跳过按钮」和「进入按钮」的显示与隐藏
         */
        mForegroundBanner.setEnterSkipViewIdAndDelegate(R.id.btn_guide_enter, R.id.tv_guide_skip, () -> {
            startActivity(new Intent(SplashActivity.this, MainActivity.class));
            finish();
        });
    }



    private void initBanner() {
        // 设置数据源
        mBackgroundBanner.setData(R.drawable.uoko_guide_background_1, R.drawable.uoko_guide_background_2, R.drawable.uoko_guide_background_3);
        mForegroundBanner.setData(R.drawable.uoko_guide_foreground_1, R.drawable.uoko_guide_foreground_2, R.drawable.uoko_guide_foreground_3);
    }


    @Override
    protected void processLogic(Bundle savedInstanceState) {
        initBanner();

    }


    @Override
    protected void onResume() {
        super.onResume();

        // 如果开发者的引导页主题是透明的，需要在界面可见时给背景 Banner 设置一个白色背景，避免滑动过程中两个 Banner 都设置透明度后能看到 Launcher
        mBackgroundBanner.setBackgroundResource(android.R.color.white);
    }


}
