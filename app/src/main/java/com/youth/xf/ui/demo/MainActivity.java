package com.youth.xf.ui.demo;

import android.content.Context;
import android.os.Bundle;
import android.support.design.widget.NavigationView;
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
import android.widget.Toast;

import com.youth.xf.base.AFengActivity;
import com.youth.xf.R;

import com.youth.xf.ui.constants.ConstantsImageUrls;
import com.youth.xf.ui.demo.book.BookFragment;
import com.youth.xf.ui.demo.movie.MovieDetailFragment;
import com.youth.xf.ui.demo.movie.MovieFragment;
import com.youth.xf.utils.GlideHelper.ImgLoadUtil;
import com.youth.xf.utils.AFengUtils.StatusBarUtil;
import com.youth.xf.utils.ToastUtil;


import me.yokeyword.fragmentation.SupportFragment;


/**
 * 作者： AFeng
 * 时间：2017/2/26
 */

public class MainActivity extends AFengActivity implements View.OnClickListener, ViewPager.OnPageChangeListener {

    private Toolbar toolbar;
    private FrameLayout TitleMenuFra;
    private DrawerLayout drawerLayout;
    private NavigationView mNavigationView;
    private ImageView mTitleOne, mTitleTwo, mTitleThr, mTitleMenu;
    private Context mContext;


    public static final int MAIN = 0;
    public static final int BOOK = 1;
    public static final int MOVIEW = 2;


    private SupportFragment[] mFragments = new SupportFragment[3];


    @Override
    protected int getLayoutId() {
        return R.layout.afeng_main;
    }

    @Override
    protected void initView(Bundle savedInstanceState) {
        mTitleOne = getViewById(R.id.iv_title_one);
        mTitleTwo = getViewById(R.id.iv_title_two);
        mTitleThr = getViewById(R.id.iv_title_thr);
        mTitleMenu = getViewById(R.id.iv_title_menu);
        toolbar = getViewById(R.id.toolbar);
        TitleMenuFra = getViewById(R.id.ll_title_menu);
        drawerLayout = getViewById(R.id.drawer_layout);
        drawerLayout = getViewById(R.id.drawer_layout);
        mNavigationView = getViewById(R.id.nav_view);


        //装载Fragments
        if (savedInstanceState == null) {
            mFragments[MAIN] = MainFragment.newInstance();
            mFragments[BOOK] = BookFragment.newInstance();
            mFragments[MOVIEW] = MovieFragment.newInstance();

            loadMultipleRootFragment(R.id.main_content, MAIN
                    , mFragments[MAIN]
                    , mFragments[BOOK]
                    , mFragments[MOVIEW]

            );

        } else {
            // 这里库已经做了Fragment恢复工作，不需要额外的处理
            // 这里我们需要拿到mFragments的引用，用下面的方法查找更方便些，也可以通过getSupportFragmentManager.getFragments()自行进行判断查找(效率更高些)
            mFragments[MAIN] = findFragment(MainFragment.class);
            mFragments[BOOK] = findFragment(BookFragment.class);
            mFragments[MOVIEW] = findFragment(MovieFragment.class);

        }

    }

    @Override
    protected void setListener() {

        mTitleOne.setOnClickListener(this);
        mTitleTwo.setOnClickListener(this);
        mTitleThr.setOnClickListener(this);
        TitleMenuFra.setOnClickListener(this);
    }

    @Override
    protected void processLogic(Bundle savedInstanceState) {
        mContext = this;

        initBar();
        initNav();
        initContent();
    }


    private void initNav() {
        //添加头部视图
        mNavigationView.inflateHeaderView(R.layout.afeng_nav_main);
        View view = mNavigationView.getHeaderView(0);

        ImageView mAvatar = (ImageView) view.findViewById(R.id.iv_avatar);
        //使用Glide来加载网络图片
        ImgLoadUtil.displayCircle(this.getApplication(), mAvatar, ConstantsImageUrls.AVATAR);

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


    private void initBar() {
        //自定义状态栏颜色
        StatusBarUtil.setColorNoTranslucentForDrawerLayout(MainActivity.this, drawerLayout, ContextCompat.getColor(this, R.color.colorAccent));

        //自定义标题栏
        setSupportActionBar(toolbar);
        ActionBar actionBar = getSupportActionBar();
        if (actionBar != null) {
            //去除默认Title显示
            actionBar.setDisplayShowTitleEnabled(false);
        }
    }


    @Override
    public void onClick(View view) {
        switch (view.getId()) {
            case R.id.iv_title_thr:

                mTitleThr.setSelected(true);
                mTitleTwo.setSelected(false);
                mTitleOne.setSelected(false);
                showHideFragment(mFragments[MOVIEW]);

                break;
            case R.id.iv_title_two:
                mTitleThr.setSelected(false);
                mTitleTwo.setSelected(true);
                mTitleOne.setSelected(false);
                showHideFragment(mFragments[BOOK]);
                break;
            case R.id.iv_title_one:
                mTitleThr.setSelected(false);
                mTitleTwo.setSelected(false);
                mTitleOne.setSelected(true);
                showHideFragment(mFragments[MAIN]);
                break;
            case R.id.ll_title_menu:
                drawerLayout.openDrawer(GravityCompat.START);
                ToastUtil.showToast("打开侧滑菜单");
                break;

            case R.id.ll_nav_homepage:// 主页
//                drawerLayout.closeDrawer(GravityCompat.START);
                ToastUtil.showToast("打开主页");
                break;

            case R.id.ll_nav_scan_download://扫码下载
//                drawerLayout.closeDrawer(GravityCompat.START);
                ToastUtil.showToast("扫码下载");
                break;
            case R.id.ll_nav_deedback:// 问题反馈
//                drawerLayout.closeDrawer(GravityCompat.START);
                ToastUtil.showToast("问题反馈");
                break;
            case R.id.ll_nav_about:// 关于
//                drawerLayout.closeDrawer(GravityCompat.START);
                ToastUtil.showToast("打开关于");
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

        //默认选中第一项
        mTitleOne.setSelected(true);
    }


    //两秒内按返回键两次退出程序
    private long exitTime = 0;

    @Override
    public boolean onKeyDown(int keyCode, KeyEvent event) {

        if (keyCode == KeyEvent.KEYCODE_BACK && event.getAction() == KeyEvent.ACTION_DOWN) {

            if (drawerLayout.isDrawerOpen(GravityCompat.START)) {
                drawerLayout.closeDrawer(GravityCompat.START);
            } else if ((System.currentTimeMillis() - exitTime) > 2000) {
                Toast.makeText(getApplicationContext(), "2秒内再按一次返回键将退出本应用！", Toast.LENGTH_SHORT).show();
                exitTime = System.currentTimeMillis();
            } else {

                //退回后台并且返回桌面
                moveTaskToBack(true);

//                finish();
//                System.exit(0); //终止程序 ， 不常用
            }
            return true;
        }

        return super.onKeyDown(keyCode, event);
    }


    @Override
    public void onBackPressedSupport() {

        if (drawerLayout.isDrawerOpen(GravityCompat.START)) {
            drawerLayout.closeDrawer(GravityCompat.START);
        } else {
            super.onBackPressedSupport();
        }

    }


    @Override
    public void onPageScrolled(int i, float v, int i1) {

    }

    @Override
    public void onPageSelected(int i) {
        switch (i) {
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
    public void onPageScrollStateChanged(int i) {

    }


}
