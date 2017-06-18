package com.youth.xf.ui.demo;

import android.Manifest;
import android.content.Context;
import android.os.Bundle;
import android.support.design.widget.NavigationView;
import android.support.v4.app.Fragment;
import android.support.v4.app.FragmentManager;
import android.support.v4.app.FragmentPagerAdapter;
import android.support.v4.content.ContextCompat;
import android.support.v4.view.GravityCompat;
import android.support.v4.view.ViewPager;
import android.support.v4.widget.DrawerLayout;
import android.support.v7.app.ActionBar;
import android.support.v7.widget.Toolbar;
import android.view.KeyEvent;
import android.view.Menu;
import android.view.View;
import android.widget.FrameLayout;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.Toast;

import com.flyco.tablayout.SlidingTabLayout;
import com.flyco.tablayout.listener.OnTabSelectListener;
import com.youth.xf.base.AFengActivity;
import com.youth.xf.R;

import com.youth.xf.ui.constants.ConstantsImageUrls;
import com.youth.xf.ui.demo.book.BookFragment;
import com.youth.xf.ui.demo.home.SimpleFragment;
import com.youth.xf.ui.demo.meizi.MeiZiFragment;
import com.youth.xf.ui.demo.more.MoreFragment;
import com.youth.xf.ui.demo.movie.MovieFragment;
import com.youth.xf.ui.demo.news.NewsFragment;
import com.youth.xf.utils.GlideHelper.ImgLoadUtil;
import com.youth.xf.utils.AFengUtils.StatusBarUtil;
import com.youth.xf.utils.AFengUtils.xToastUtil;
import com.youth.xf.widget.hipermission.HiPermission;
import com.youth.xf.widget.hipermission.PermissionCallback;
import com.youth.xf.widget.hipermission.PermissionItem;
import com.youth.xf.widget.searchbox.SearchFragment;

import java.util.ArrayList;
import java.util.List;

import butterknife.BindView;
import me.yokeyword.fragmentation.SupportFragment;


/**
 * 作者： AFeng
 * 时间：2017/2/26
 */

public class MainActivity extends AFengActivity implements View.OnClickListener, ViewPager.OnPageChangeListener {

    @BindView(R.id.main_viewPager)
    ViewPager mMainViewPager;
    @BindView(R.id.main_tab)
    SlidingTabLayout mMainTabLayout;

    private SearchFragment searchFragment;

    private Toolbar toolbar;
    private FrameLayout TitleMenuFra;
    private DrawerLayout drawerLayout;
    private NavigationView mNavigationView;
    private ImageView mTitleOne, mTitleTwo, mTitleThr, mTitleMenu;
    private Context mContext;

    public String[] mTitles = {"首页", "头条", "妹纸", "更多", "读书", "电影"};

    public static final int MAIN = 0;
    public static final int NEW = 1;
    public static final int MEIZI = 2;
    public static final int MORE = 3;
    public static final int BOOK = 4;
    public static final int MOVIEW = 5;


    public SupportFragment[] mFragments = new SupportFragment[6];


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
            mFragments[MAIN] = SimpleFragment.getInstance();
            mFragments[NEW] = NewsFragment.getInstance();
            mFragments[MEIZI] = MeiZiFragment.getInstance();
            mFragments[MORE] = MoreFragment.getInstance();
            mFragments[BOOK] = BookFragment.newInstance();
            mFragments[MOVIEW] = MovieFragment.newInstance();

//            loadMultipleRootFragment(R.id.main_tab_container, MAIN
//                    , mFragments[MAIN]
//                    , mFragments[NEW]
//                    , mFragments[MEIZI]
//                    , mFragments[MORE]
//                    , mFragments[BOOK]
//                    , mFragments[MOVIEW]
//
//            );

        } else {
            // 这里库已经做了Fragment恢复工作，不需要额外的处理
            // 这里我们需要拿到mFragments的引用，用下面的方法查找更方便些，也可以通过getSupportFragmentManager.getFragments()自行进行判断查找(效率更高些)
            mFragments[MAIN] = findFragment(SimpleFragment.class);
            mFragments[NEW] = findFragment(NewsFragment.class);
            mFragments[MEIZI] = findFragment(MeiZiFragment.class);
            mFragments[MORE] = findFragment(MoreFragment.class);
            mFragments[BOOK] = findFragment(BookFragment.class);
            mFragments[MOVIEW] = findFragment(MovieFragment.class);

        }

        initViewPager();
    }


    private void initViewPager() {


        mMainViewPager.setAdapter(new MainViewPagerAdapter(getSupportFragmentManager(), mFragments, mTitles));
        mMainTabLayout.setViewPager(mMainViewPager);
        mMainTabLayout.setOnTabSelectListener(new OnTabSelectListener() {

            @Override
            public void onTabSelect(int position) {
//                mMainViewPager.setCurrentItem(position);
                showHideFragment(mFragments[position]);  //使用这个 在点击选项卡时不会出现滑动
            }

            @Override
            public void onTabReselect(int position) {

            }
        });

        mMainViewPager.addOnPageChangeListener(new ViewPager.OnPageChangeListener() {
            @Override
            public void onPageScrolled(int position, float positionOffset, int positionOffsetPixels) {

            }

            @Override
            public void onPageSelected(int position) {
                mMainTabLayout.setCurrentTab(position);
            }

            @Override
            public void onPageScrollStateChanged(int state) {

            }
        });

        mMainViewPager.setCurrentItem(0);
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
        //申请权限
        requestSomePermission();
    }


    // 申请权限
    private void requestSomePermission() {

        List<PermissionItem> permissionItems = new ArrayList<>();
        permissionItems.add(new PermissionItem(Manifest.permission.WRITE_EXTERNAL_STORAGE, "SD卡操作权限", R.drawable.permission_ic_storage));
        permissionItems.add(new PermissionItem(Manifest.permission.READ_PHONE_STATE, "访问电话状态", R.drawable.permission_ic_phone));
        permissionItems.add(new PermissionItem(Manifest.permission.ACCESS_FINE_LOCATION, "GPS定位", R.drawable.permission_ic_location));
        HiPermission.create(MainActivity.this)
                .title("权限申请")
                .permissions(permissionItems)
                .msg("此APP运行需要此项权限！")
                .animStyle(R.style.PermissionAnimFade)
                .style(R.style.PermissionDefaultNormalStyle)
                .checkMutiPermission(new PermissionCallback() {
                    @Override
                    public void onClose() {

                    }

                    @Override
                    public void onFinish() {

                    }

                    @Override
                    public void onDeny(String permission, int position) {

                    }

                    @Override
                    public void onGuarantee(String permission, int position) {

                    }
                });

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


    //创建工具栏右上角菜单
    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        //加载菜单文件
        getMenuInflater().inflate(R.menu.menu_main, menu);
        return super.onCreateOptionsMenu(menu);
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

        toolbar.setOnMenuItemClickListener(menuItem -> {
            switch (menuItem.getItemId()) {
                case R.id.action_search://点击搜索
                    searchFragment.show(getSupportFragmentManager(), SearchFragment.TAG);
                    break;
            }
            return true;
        });

        searchFragment = SearchFragment.newInstance();
        searchFragment.setOnSearchClickListener((info) -> xToastUtil.showToast("搜索:" + info));
    }


    @Override
    public void onClick(View view) {
        switch (view.getId()) {
            case R.id.iv_title_thr:

                mTitleThr.setSelected(true);
                mTitleTwo.setSelected(false);
                mTitleOne.setSelected(false);
//                showHideFragment(mFragments[MOVIEW]);
                xToastUtil.showToast("3频道建设中……");

                break;
            case R.id.iv_title_two:
                mTitleThr.setSelected(false);
                mTitleTwo.setSelected(true);
                mTitleOne.setSelected(false);
//                showHideFragment(mFragments[BOOK]);
                xToastUtil.showToast("2频道建设中……");

                break;
            case R.id.iv_title_one:
                mTitleThr.setSelected(false);
                mTitleTwo.setSelected(false);
                mTitleOne.setSelected(true);
//                showHideFragment(mFragments[MAIN]);
                xToastUtil.showToast("1频道建设中……");

                break;
            case R.id.ll_title_menu:
                drawerLayout.openDrawer(GravityCompat.START);
                xToastUtil.showToast("打开侧滑菜单");
                break;

            case R.id.ll_nav_homepage:// 主页
//                drawerLayout.closeDrawer(GravityCompat.START);
                xToastUtil.showToast("打开主页");
                break;

            case R.id.ll_nav_scan_download://扫码下载
//                drawerLayout.closeDrawer(GravityCompat.START);
                xToastUtil.showToast("扫码下载");
                break;
            case R.id.ll_nav_deedback:// 问题反馈
//                drawerLayout.closeDrawer(GravityCompat.START);
                xToastUtil.showToast("问题反馈");
                break;
            case R.id.ll_nav_about:// 关于
//                drawerLayout.closeDrawer(GravityCompat.START);
                xToastUtil.showToast("打开关于");
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


    class MainViewPagerAdapter extends FragmentPagerAdapter {

        private Fragment[] mAdapterFragments;
        private String[] mAdapterTitles;

        public MainViewPagerAdapter(FragmentManager fm, Fragment[] fragments, String[] titles) {
            super(fm);
            this.mAdapterFragments = fragments;
            this.mAdapterTitles = titles;
        }

        @Override
        public Fragment getItem(int i) {
            return mAdapterFragments[i];
        }

        @Override
        public int getCount() {
            return mAdapterFragments.length;
        }

        @Override
        public CharSequence getPageTitle(int position) {
            return mAdapterTitles[position];
        }
    }


}
