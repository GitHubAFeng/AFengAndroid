package com.youth.xf.ui.demo;

import android.Manifest;
import android.content.Context;
import android.content.Intent;
import android.os.Build;
import android.os.Bundle;
import android.support.annotation.NonNull;
import android.support.design.widget.NavigationView;
import android.support.v4.app.ActivityOptionsCompat;
import android.support.v4.app.Fragment;
import android.support.v4.app.FragmentManager;
import android.support.v4.app.FragmentPagerAdapter;
import android.support.v4.content.ContextCompat;
import android.support.v4.view.GravityCompat;
import android.support.v4.view.ViewPager;
import android.support.v4.widget.DrawerLayout;
import android.support.v7.app.ActionBar;
import android.support.v7.widget.Toolbar;
import android.transition.Explode;
import android.view.KeyEvent;
import android.view.Menu;
import android.view.MenuItem;
import android.view.View;
import android.widget.FrameLayout;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.ProgressBar;
import android.widget.RelativeLayout;
import android.widget.Toast;

import com.avos.avoscloud.AVException;
import com.avos.avoscloud.AVObject;
import com.avos.avoscloud.AVQuery;
import com.avos.avoscloud.AVUser;
import com.avos.avoscloud.GetCallback;
import com.avos.avoscloud.feedback.FeedbackAgent;
import com.flyco.tablayout.SlidingTabLayout;
import com.flyco.tablayout.listener.OnTabSelectListener;
import com.youth.xf.base.AFengActivity;
import com.youth.xf.R;

import com.youth.xf.ui.constants.Constants;
import com.youth.xf.ui.demo.Login.LoginEvent;
import com.youth.xf.ui.demo.Login.UserLoginActivity;
import com.youth.xf.ui.demo.book.BookFragment;
import com.youth.xf.ui.demo.fiction.FictionFragment;
import com.youth.xf.ui.demo.home.AboutMeActivity;
import com.youth.xf.ui.demo.home.SimpleFragment;
import com.youth.xf.ui.demo.meizi.MeiZiFragment;
import com.youth.xf.ui.demo.more.MoreFragment;
import com.youth.xf.ui.demo.movie.MovieFragment;
import com.youth.xf.utils.GlideHelper.ImgLoadUtil;
import com.youth.xf.utils.AFengUtils.StatusBarUtil;
import com.youth.xf.utils.AFengUtils.xToastUtil;
import com.youth.xf.widget.hipermission.HiPermission;
import com.youth.xf.widget.hipermission.PermissionCallback;
import com.youth.xf.widget.hipermission.PermissionItem;
import com.youth.xf.widget.searchbox.SearchFragment;

import org.greenrobot.eventbus.EventBus;
import org.greenrobot.eventbus.Subscribe;
import org.greenrobot.eventbus.ThreadMode;

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
    private RelativeLayout TitleMenuFra;
    private DrawerLayout drawerLayout;
    private NavigationView mNavigationView;
    private ImageView mTitleOne, mTitleTwo, mTitleThr;
    private Context mContext;

    @BindView(R.id.main_user_avatar)
    ImageView mUserAva;

    public String[] mTitles = {"首页", "小说", "妹纸", "读书", "电影", "更多"};


    public SupportFragment[] mFragments = new SupportFragment[6];


    FeedbackAgent agent = null;


    @Override
    protected int getLayoutId() {
        return R.layout.afeng_main;
    }

    @Override
    protected void initView(Bundle savedInstanceState) {

        mContext = this;

//        explode：从场景的中心移入或移出
//        slide：从场景的边缘移入或移出
//        fade：调整透明度产生渐变效果

        // 转场动画
        if (android.os.Build.VERSION.SDK_INT >= android.os.Build.VERSION_CODES.LOLLIPOP) {
            Explode explode = new Explode();
            explode.setDuration(500);
            getWindow().setExitTransition(explode);  //退出一个Activity的效果
            getWindow().setEnterTransition(explode);   //进入一个Activity的效果
        }


        if (!EventBus.getDefault().isRegistered(this)) {
            EventBus.getDefault().register(this);
        }

        mTitleOne = getViewById(R.id.iv_title_one);
        mTitleTwo = getViewById(R.id.iv_title_two);
        mTitleThr = getViewById(R.id.iv_title_thr);
        toolbar = getViewById(R.id.toolbar);
        TitleMenuFra = getViewById(R.id.ll_title_menu);
        drawerLayout = getViewById(R.id.drawer_layout);
        drawerLayout = getViewById(R.id.drawer_layout);
        mNavigationView = getViewById(R.id.nav_view);

        // 设置菜单图标显示图片本身颜色
        mNavigationView.setItemIconTintList(null);

        if (agent == null) {
            agent = new FeedbackAgent(this);
        }

        agent.sync();  //通知用户新的反馈回复


        //装载Fragments
        if (savedInstanceState == null) {
            mFragments[0] = SimpleFragment.getInstance();
            mFragments[1] = FictionFragment.newInstance();
            mFragments[2] = MeiZiFragment.getInstance();
            mFragments[3] = BookFragment.newInstance();
            mFragments[4] = MovieFragment.newInstance();
            mFragments[5] = MoreFragment.getInstance();


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
            mFragments[0] = findFragment(SimpleFragment.class);
            mFragments[1] = findFragment(FictionFragment.class);
            mFragments[2] = findFragment(MeiZiFragment.class);
            mFragments[3] = findFragment(BookFragment.class);
            mFragments[4] = findFragment(MovieFragment.class);
            mFragments[5] = findFragment(MoreFragment.class);


        }

        initViewPager();
    }


    private void initViewPager() {


        mMainViewPager.setAdapter(new MainViewPagerAdapter(getSupportFragmentManager(), mFragments, mTitles));
        mMainTabLayout.setViewPager(mMainViewPager);
        mMainTabLayout.setOnTabSelectListener(new OnTabSelectListener() {

            @Override
            public void onTabSelect(int position) {

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
        mUserAva.setOnClickListener(this);
    }

    @Override
    protected void processLogic(Bundle savedInstanceState) {

        initBar();
        initNav();
        initContent();
        //申请权限
        requestSomePermission();

        initUser();

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
        ImgLoadUtil.displayCircle(this.getApplication(), mAvatar, Constants.AVATAR);


        mNavigationView.setNavigationItemSelectedListener(menuItem -> {

            switch (menuItem.getItemId()) {

                case R.id.ll_nav_deedback:// 问题反馈

                    agent.startDefaultThreadActivity();

                    break;
                case R.id.ll_nav_about:// 关于

                    startActivity(new Intent(mContext, AboutMeActivity.class));

                    break;
                case R.id.ll_nav_exit:// 退出应用
                    finish();
                    break;

            }

            return true;
        });

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

                xToastUtil.showToast("3频道建设中……");

                break;
            case R.id.iv_title_two:
                mTitleThr.setSelected(false);
                mTitleTwo.setSelected(true);
                mTitleOne.setSelected(false);

                xToastUtil.showToast("2频道建设中……");

                break;
            case R.id.iv_title_one:
                mTitleThr.setSelected(false);
                mTitleTwo.setSelected(false);
                mTitleOne.setSelected(true);

                xToastUtil.showToast("1频道建设中……");

                break;
            case R.id.ll_title_menu:
                drawerLayout.openDrawer(GravityCompat.START);
                break;

            case R.id.main_user_avatar:

                if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.LOLLIPOP) {
                    // 启动普通转场动画
                    ActivityOptionsCompat oc2 = ActivityOptionsCompat.makeSceneTransitionAnimation(this);
                    Intent i2 = new Intent(this, UserLoginActivity.class);
                    startActivity(i2, oc2.toBundle());

                } else {
                    startActivity(new Intent(this, UserLoginActivity.class));
                }

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
                Toast.makeText(getApplicationContext(), "再按一次返回键退出", Toast.LENGTH_SHORT).show();
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


    @Override
    public void onDestroy() {

        EventBus.getDefault().unregister(this);
        super.onDestroy();
    }


    @Subscribe(threadMode = ThreadMode.MAIN)
    public void ReceviceMessage(LoginEvent event) {
        if (event != null) {
            xToastShow(event.getUserName());

        }

    }


    private void initUser() {

        AVUser currentUser = AVUser.getCurrentUser();
        if (currentUser != null) {
            // 联表查询
            AVObject _User = AVObject.createWithoutData("_User", currentUser.getObjectId());
            _User.fetchInBackground("UserInfo", new GetCallback<AVObject>() {
                @Override
                public void done(AVObject avObject, AVException e) {
                    if (e == null) {
                        if (avObject != null) {

                            AVObject info = avObject.getAVObject("UserInfo");

                            if (info != null) {
                                String desc = info.getString("desc");
                                String avatar = info.getString("avatar");
                                ImgLoadUtil.displayCircle(mContext, mUserAva, avatar);


                            } else {
                                xToastShow("info为空");
                            }

                        } else {
                            xToastShow("avObject为空");
                        }
                    }
                }
            });


        } else {
            xToastShow("当前未登录");
        }


    }


}
