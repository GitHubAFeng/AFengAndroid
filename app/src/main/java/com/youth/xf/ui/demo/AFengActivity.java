package com.youth.xf.ui.demo;

import android.content.Context;
import android.graphics.Bitmap;
import android.os.Bundle;
import android.support.design.widget.NavigationView;
import android.support.v4.app.Fragment;
import android.support.v4.content.ContextCompat;
import android.support.v4.view.GravityCompat;
import android.support.v4.view.ViewPager;
import android.support.v4.widget.DrawerLayout;
import android.support.v7.app.ActionBar;
import android.support.v7.widget.Toolbar;
import android.view.KeyEvent;
import android.view.View;
import android.widget.FrameLayout;
import android.widget.ImageView;
import android.widget.LinearLayout;

import com.lzy.okgo.OkGo;
import com.lzy.okgo.callback.BitmapCallback;
import com.youth.xf.BaseActivity;
import com.youth.xf.R;
import com.youth.xf.ui.adapter.MyFragmentPagerAdapter;

import com.youth.xf.ui.constants.ConstantsImageUrls;
import com.youth.xf.ui.demo.fragments.OneFragment;
import com.youth.xf.ui.demo.test.SimpleCardFragment;
import com.youth.xf.utils.AFengUtils.ImgLoadUtil;
import com.youth.xf.utils.AFengUtils.findview.AnnotateUtils;
import com.youth.xf.utils.AFengUtils.findview.ViewInject;
import com.youth.xframe.utils.log.XLog;
import com.youth.xframe.utils.statusbar.XStatusBar;
import com.youth.xframe.widget.XToast;

import java.util.ArrayList;

import okhttp3.Call;
import okhttp3.Response;


/**
 * 作者： AFeng
 * 时间：2017/2/26
 */

public class AFengActivity extends BaseActivity implements View.OnClickListener, ViewPager.OnPageChangeListener {

    private Toolbar toolbar;
    private FrameLayout TitleMenuFra;
    private DrawerLayout drawerLayout;
    private ImageView mTitleOne, mTitleTwo, mTitleThr, mTitleMenu;
    private NavigationView mNavigationView;
    private ViewPager mViewPager;

    private Context mContext;


    @Override
    public int getLayoutId() {
        return R.layout.afeng_main;
    }

    @Override
    public void initData(Bundle savedInstanceState) {
    }

    @Override
    public void initView() {
        AnnotateUtils.injectViews(this);
        mContext = this;

        initId();
        initBar();
        initNav();
        initListener();
        initContent();
    }


    private void initId() {
        toolbar = getViewById(R.id.toolbar);
        TitleMenuFra = getViewById(R.id.ll_title_menu);
        drawerLayout = getViewById(R.id.drawer_layout);
        mTitleOne = getViewById(R.id.iv_title_one);
        mTitleTwo = getViewById(R.id.iv_title_two);
        mTitleThr = getViewById(R.id.iv_title_thr);
        mTitleMenu = getViewById(R.id.iv_title_menu);
        drawerLayout = getViewById(R.id.drawer_layout);
        mNavigationView = getViewById(R.id.nav_view);
        mViewPager = getViewById(R.id.vp_content);

    }


    private void initNav() {
        //添加头部视图
        mNavigationView.inflateHeaderView(R.layout.afeng_nav_main);
        View view = mNavigationView.getHeaderView(0);

        ImageView mAvatar = (ImageView) view.findViewById(R.id.iv_avatar);
        //使用Glide来加载网络图片有点慢
//        ImgLoadUtil.displayCircle(this.getApplication(), mAvatar, ConstantsImageUrls.AVATAR);

        loadAvatar(mAvatar);  //加载 下载好的图片

        LinearLayout mNavHomepage = (LinearLayout) view.findViewById(R.id.ll_nav_homepage);
        LinearLayout mNavScanDownload = (LinearLayout) view.findViewById(R.id.ll_nav_scan_download);
        LinearLayout mNavDeedback = (LinearLayout) view.findViewById(R.id.ll_nav_deedback);
        LinearLayout mNavAbout = (LinearLayout) view.findViewById(R.id.ll_nav_about);
        LinearLayout mNavExit = (LinearLayout) view.findViewById(R.id.ll_nav_exit);
        mNavHomepage.setOnClickListener(this);
        mNavScanDownload.setOnClickListener(this);
        mNavDeedback.setOnClickListener(this);
        mNavAbout.setOnClickListener(this);
        mNavExit.setOnClickListener(this);
    }

    private void loadAvatar(final ImageView view) {

        OkGo.get(ConstantsImageUrls.AVATAR).tag(this).execute(new BitmapCallback() {
            @Override
            public void onSuccess(Bitmap bitmap, Call call, Response response) {
                ImgLoadUtil.displayCircleByBitmap(mContext, view, bitmap);
            }

            @Override
            public void onError(Call call, Response response, Exception e) {
                super.onError(call, response, e);
                XLog.e("用户头像加载错误！");
            }
        });

    }


    private void initBar() {
        //自定义状态栏颜色
        XStatusBar.setColorNoTranslucentForDrawerLayout(AFengActivity.this, drawerLayout, ContextCompat.getColor(this, R.color.colorAccent));

        //自定义标题栏
        setSupportActionBar(toolbar);
        ActionBar actionBar = getSupportActionBar();
        if (actionBar != null) {
            //去除默认Title显示
            actionBar.setDisplayShowTitleEnabled(false);
        }
    }

    private void initListener() {
        mTitleOne.setOnClickListener(this);
        mTitleTwo.setOnClickListener(this);
        mTitleThr.setOnClickListener(this);
        TitleMenuFra.setOnClickListener(this);

    }


    @Override
    public void onClick(View view) {
        switch (view.getId()) {
            case R.id.ll_title_menu:
                drawerLayout.openDrawer(GravityCompat.START);
                XToast.info("打开侧滑菜单");
                break;
            case R.id.iv_title_thr:
                if (mViewPager.getCurrentItem() != 2) {
                    mTitleThr.setSelected(true);
                    mTitleTwo.setSelected(false);
                    mTitleOne.setSelected(false);
                    mViewPager.setCurrentItem(2);
                    XToast.info("thr");
                }
                break;
            case R.id.iv_title_two:
                if (mViewPager.getCurrentItem() != 1) {
                    mTitleThr.setSelected(false);
                    mTitleTwo.setSelected(true);
                    mTitleOne.setSelected(false);
                    mViewPager.setCurrentItem(1);
                    XToast.info("two");
                }
                break;
            case R.id.iv_title_one:
                if (mViewPager.getCurrentItem() != 0) {
                    mTitleThr.setSelected(false);
                    mTitleTwo.setSelected(false);
                    mTitleOne.setSelected(true);
                    mViewPager.setCurrentItem(0);
                    XToast.info("one");
                }
                break;
            case R.id.ll_nav_homepage:// 主页
//                drawerLayout.closeDrawer(GravityCompat.START);
                XToast.info("打开主页");
                break;

            case R.id.ll_nav_scan_download://扫码下载
//                drawerLayout.closeDrawer(GravityCompat.START);
                XToast.info("扫码下载");
                break;
            case R.id.ll_nav_deedback:// 问题反馈
//                drawerLayout.closeDrawer(GravityCompat.START);
                XToast.info("问题反馈");
                break;
            case R.id.ll_nav_about:// 关于
//                drawerLayout.closeDrawer(GravityCompat.START);
                XToast.info("打开关于");
                break;
            case R.id.ll_nav_exit:// 退出应用
                finish();
                break;
        }
    }

    /**
     * 初始化内容
     */
    private void initContent() {
        ArrayList<Fragment> mFragmentList = new ArrayList<>();

//        mFragmentList.add(SimpleCardFragment.getInstance("一"));
        mFragmentList.add(new OneFragment());
        mFragmentList.add(SimpleCardFragment.getInstance("二"));
        mFragmentList.add(SimpleCardFragment.getInstance("三"));

        MyFragmentPagerAdapter adapter = new MyFragmentPagerAdapter(getSupportFragmentManager(), mFragmentList);
        mViewPager.setAdapter(adapter);
        mViewPager.setOffscreenPageLimit(2);
        mViewPager.addOnPageChangeListener(this);

        //默认选中第一项
        mTitleOne.setSelected(true);
        mViewPager.setCurrentItem(0);
    }


    @Override
    public boolean onKeyDown(int keyCode, KeyEvent event) {
        if (keyCode == KeyEvent.KEYCODE_BACK) {
            if (drawerLayout.isDrawerOpen(GravityCompat.START)) {
                drawerLayout.closeDrawer(GravityCompat.START);
            } else {
                moveTaskToBack(true);
            }
            return true;
        }
        return super.onKeyDown(keyCode, event);
    }


    @Override
    public void onBackPressed() {
        if (drawerLayout.isDrawerOpen(GravityCompat.START)) {
            drawerLayout.closeDrawer(GravityCompat.START);
        } else {
            super.onBackPressed();
        }
    }

    @Override
    public void onPageScrolled(int position, float positionOffset, int positionOffsetPixels) {

    }

    @Override
    public void onPageSelected(int position) {
        switch (position) {
            case 0:
                mTitleOne.setSelected(true);
                mTitleThr.setSelected(false);
                mTitleTwo.setSelected(false);
                break;
            case 1:
                mTitleOne.setSelected(false);
                mTitleThr.setSelected(false);
                mTitleTwo.setSelected(true);
                break;
            case 2:
                mTitleOne.setSelected(false);
                mTitleThr.setSelected(true);
                mTitleTwo.setSelected(false);
                break;
        }
    }

    @Override
    public void onPageScrollStateChanged(int state) {

    }
}
