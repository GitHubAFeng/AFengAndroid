package com.afeng.xf.ui.demo.activity;

import android.app.Activity;
import android.content.Intent;
import android.os.Bundle;
import android.widget.ImageView;

import com.afeng.xf.utils.AFengUtils.StatusBarUtil;
import com.avos.avoscloud.AVException;
import com.avos.avoscloud.AVObject;
import com.avos.avoscloud.AVQuery;
import com.avos.avoscloud.FindCallback;
import com.bumptech.glide.Glide;
import com.afeng.xf.base.AFengActivity;
import com.afeng.xf.R;
import com.afeng.xf.ui.data.SplashBannerItem;
import com.afeng.xf.ui.demo.MainActivity;

import java.util.ArrayList;
import java.util.List;

import cn.bingoogolapple.bgabanner.BGABanner;

/**
 * Created by AFeng on 2017/3/8.
 */

public class SplashActivity extends AFengActivity {

    private BGABanner mBackgroundBanner;
    private BGABanner mForegroundBanner;

    private List<SplashBannerItem> backDatas = new ArrayList<>();   //背景数据
    private List<SplashBannerItem> foreDatas = new ArrayList<>();   //前景数据

    // 基本为空
    private List<String> backDesc = new ArrayList<>();   //背景描述
    private List<String> foreDesc = new ArrayList<>();   //前景描述

    boolean isShowNet = false;

    Activity mActivity;

    @Override
    public int getLayoutId() {
        return R.layout.splash_main;
    }

    @Override
    protected void initView(Bundle savedInstanceState) {
        mActivity = this;

        Intent intent = getIntent();
        isShowNet = intent.getBooleanExtra("isshownet", false);

        //此页面隐藏状态栏
        StatusBarUtil.hideSystemUI(this);

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


    @Override
    protected void processLogic(Bundle savedInstanceState) {
        initdata();
    }


    @Override
    protected void onResume() {
        super.onResume();

        // 如果开发者的引导页主题是透明的，需要在界面可见时给背景 Banner 设置一个白色背景，避免滑动过程中两个 Banner 都设置透明度后能看到 Launcher
        mBackgroundBanner.setBackgroundResource(android.R.color.white);
    }


    private void initdata() {

        try {
            if (isShowNet) {
                // 网络
                initNetBanner();
                initNetBannerData();

            } else {
                // 本地
                initlocalBanner();
            }
        } catch (Exception e) {
            e.printStackTrace();
            finish();
        }


    }

    private void initNetBanner() {

        // 设置图片加载方法
        mBackgroundBanner.setAdapter(new BGABanner.Adapter<ImageView, SplashBannerItem>() {

            @Override
            public void fillBannerItem(BGABanner bgaBanner, ImageView imageView, SplashBannerItem splashBannerItem, int i) {
                Glide.with(mActivity)
                        .load(splashBannerItem.getImg())
//                        .placeholder(R.drawable.splash_hold)  //临时占位图
                        .error(R.drawable.load_err)  //加载错误时显示图
                        .centerCrop()
                        .dontAnimate()
                        .into(imageView);
            }
        });


        mForegroundBanner.setAdapter(new BGABanner.Adapter<ImageView, SplashBannerItem>() {

            @Override
            public void fillBannerItem(BGABanner bgaBanner, ImageView imageView, SplashBannerItem splashBannerItem, int i) {
                Glide.with(mActivity)
                        .load(splashBannerItem.getImg())
//                        .placeholder(R.drawable.splash_hold)  //临时占位图
                        .error(R.drawable.load_err)  //加载错误时显示图
                        .centerCrop()
                        .dontAnimate()
                        .into(imageView);
            }
        });

    }


    private void initNetBannerData() {


        //        AVObject todoFolder = new AVObject("SplashBannerItem");// 构建对象
//        todoFolder.put("img", "http://oki2v8p4s.bkt.clouddn.com/home_ban_4.png");
//        todoFolder.put("isBack", 0);
//        todoFolder.put("isShow", 1);
//        todoFolder.put("desc", "花儿与少年 第三季 2017-06-25期");
//
//        todoFolder.saveInBackground();// 保存到服务端


        AVQuery<AVObject> avQuery = new AVQuery<>("SplashBannerItem");
        avQuery.whereEqualTo("isShow", 1);  //确认为1时才下载显示
        avQuery.whereEqualTo("isBack", 1);  //确认为背景
        avQuery.limit(3);// 最多返回 3 条结果
        avQuery.orderByAscending("order");
        avQuery.findInBackground(new FindCallback<AVObject>() {
            @Override
            public void done(List<AVObject> list, AVException e) {

                if (list.size() > 0) {
                    for (AVObject avObject : list) {

                        String desc = avObject.getString("desc");
                        String img = avObject.getString("img");

                        SplashBannerItem data = new SplashBannerItem();
                        data.setDesc(desc);
                        data.setImg(img);
                        backDatas.add(data);
                        backDesc.add(desc);
                    }
                } else {

                }

                //绑定轮播图片与提示
                mBackgroundBanner.setData(backDatas, backDesc);
            }
        });


        AVQuery<AVObject> foreQuery = new AVQuery<>("SplashBannerItem");
        foreQuery.whereEqualTo("isShow", 1);  //确认为1时才下载显示
        foreQuery.whereEqualTo("isBack", 0);  //确认为前景
        foreQuery.limit(3);// 最多返回 3 条结果
        foreQuery.orderByAscending("order");
        foreQuery.findInBackground(new FindCallback<AVObject>() {
            @Override
            public void done(List<AVObject> list, AVException e) {

                if (list.size() > 0) {
                    for (AVObject avObject : list) {

                        String desc = avObject.getString("desc");
                        String img = avObject.getString("img");

                        SplashBannerItem data = new SplashBannerItem();
                        data.setDesc(desc);
                        data.setImg(img);
                        foreDatas.add(data);
                        foreDesc.add(desc);
                    }
                } else {

                }

                //绑定轮播图片与提示
                mForegroundBanner.setData(foreDatas, foreDesc);
            }
        });


    }


    private void initlocalBanner() {

        mBackgroundBanner.setData(R.drawable.the_guide_background_2, R.drawable.the_guide_background_1, R.drawable.the_guide_background_3);
        mForegroundBanner.setData(R.drawable.the_guide_foreground_1, R.drawable.the_guide_foreground_2, R.drawable.the_guide_foreground_3);

    }


}
