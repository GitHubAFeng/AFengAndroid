package com.youth.xf.ui.demo.test;

import android.annotation.SuppressLint;
import android.os.Bundle;
import android.os.StrictMode;
import android.widget.ImageView;
import android.widget.TextView;

import com.youth.xf.R;
import com.youth.xf.ui.demo.AFengFragment;
import com.youth.xframe.widget.XToast;

import cn.bingoogolapple.bgabanner.BGABanner;

/**
 * 作者： AFeng
 * 时间：2017/3/2
 */


@SuppressLint("ValidFragment")
public class SimpleCardFragment extends AFengFragment {
    private static final String TYPE = "mType";
    private String mType = "Android";
    private String mTitle;
    private BGABanner mBGABanner;



    public static SimpleCardFragment getInstance(String title) {
        SimpleCardFragment sf = new SimpleCardFragment();
        sf.mTitle = title;
        return sf;
    }

    @Override
    protected void onVisible() {

    }

    @Override
    protected void onInvisible() {

    }

    @Override
    public int getLayoutId() {
        return R.layout.afeng_simple_one;
    }

    @Override
    public void initData(Bundle savedInstanceState) {
        if (getArguments() != null) {
            mType = getArguments().getString(TYPE);
        }
    }

    @Override
    public void initView() {
        TextView card_title_tv = getViewById(R.id.card_title_tv);
        card_title_tv.setText(mTitle);
        initBanner();


    }


    private void initBanner(){
        mBGABanner=getViewById(R.id.banner_main);

        mBGABanner.setData(R.drawable.test_baner01,R.drawable.test_baner02,R.drawable.test_banner03);

        mBGABanner.setDelegate(new BGABanner.Delegate<ImageView, String>() {
            @Override
            public void onBannerItemClick(BGABanner banner, ImageView itemView, String model, int position) {
                XToast.info("点击了" + position);
            }
        });


    }








}