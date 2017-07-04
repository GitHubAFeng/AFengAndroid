package com.afeng.xf.ui.demo;

import android.Manifest;
import android.content.Context;
import android.content.Intent;
import android.graphics.Bitmap;
import android.os.Build;
import android.os.Bundle;
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
import android.text.TextUtils;
import android.transition.Explode;
import android.view.KeyEvent;
import android.view.Menu;
import android.view.View;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.RelativeLayout;
import android.widget.TextView;
import android.widget.Toast;

import com.afeng.xf.base.BaseActivity;
import com.avos.avoscloud.AVException;
import com.avos.avoscloud.AVFile;
import com.avos.avoscloud.AVObject;
import com.avos.avoscloud.AVQuery;
import com.avos.avoscloud.AVUser;
import com.avos.avoscloud.GetCallback;
import com.avos.avoscloud.GetDataCallback;
import com.avos.avoscloud.ProgressCallback;
import com.avos.avoscloud.feedback.FeedbackAgent;
import com.flyco.tablayout.SlidingTabLayout;
import com.flyco.tablayout.listener.OnTabSelectListener;
import com.afeng.xf.R;

import com.afeng.xf.ui.constants.Constants;
import com.afeng.xf.ui.demo.Login.LoginEvent;
import com.afeng.xf.ui.demo.Login.UserInfoEvent;
import com.afeng.xf.ui.demo.Login.UserLoginActivity;
import com.afeng.xf.ui.demo.book.BookFragment;
import com.afeng.xf.ui.demo.fiction.FictionFragment;
import com.afeng.xf.ui.demo.fiction.FictionSearchActivity;
import com.afeng.xf.ui.demo.home.AboutMeActivity;
import com.afeng.xf.ui.demo.home.MySearchEvent;
import com.afeng.xf.ui.demo.home.SimpleFragment;
import com.afeng.xf.ui.demo.home.UserInfoActivity;
import com.afeng.xf.ui.demo.meizi.MeiZiFragment;
import com.afeng.xf.ui.demo.more.MoreFragment;
import com.afeng.xf.ui.demo.movie.MovieFragment;
import com.afeng.xf.utils.AFengUtils.AppImageMgr;
import com.afeng.xf.utils.GlideHelper.ImgLoadUtil;
import com.afeng.xf.utils.AFengUtils.StatusBarUtil;
import com.afeng.xf.utils.AFengUtils.xToastUtil;
import com.afeng.xf.widget.hipermission.HiPermission;
import com.afeng.xf.widget.hipermission.PermissionCallback;
import com.afeng.xf.widget.hipermission.PermissionItem;
import com.afeng.xf.widget.searchbox.SearchFragment;

import org.greenrobot.eventbus.EventBus;
import org.greenrobot.eventbus.Subscribe;
import org.greenrobot.eventbus.ThreadMode;

import java.util.ArrayList;
import java.util.List;

import butterknife.BindView;


/**
 * 作者： AFeng
 * 时间：2017/2/26
 */

public class MainActivity extends BaseActivity implements View.OnClickListener, ViewPager.OnPageChangeListener {

    public final static int USERINFO_REQUEST_CODE = 100;

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

    ImageView mNavAvatar;   //侧边栏头像
    LinearLayout mNavBg;   //侧边栏头部背景布局
    TextView mNavDesc;  //侧边栏签名
    TextView mNavUserName;

    @BindView(R.id.main_user_avatar)
    ImageView mUserAva;

    @BindView(R.id.main_user_name)
    TextView mUserName;

    public String[] mTitles = {"首页", "小说", "妹纸", "读书", "电影", "更多"};


    public Fragment[] mFragments = new Fragment[6];

    UserInfoEvent mEventdata = new UserInfoEvent();  //当前用户资料

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

        mFragments[0] = SimpleFragment.getInstance();
        mFragments[1] = FictionFragment.newInstance();
        mFragments[2] = MeiZiFragment.getInstance();
        mFragments[3] = BookFragment.newInstance();
        mFragments[4] = MovieFragment.newInstance();
        mFragments[5] = MoreFragment.getInstance();

        initViewPager();
    }


    private void initViewPager() {


        mMainViewPager.setAdapter(new MainViewPagerAdapter(getSupportFragmentManager(), mFragments, mTitles));
        mMainTabLayout.setViewPager(mMainViewPager);
        mMainTabLayout.setOnTabSelectListener(new OnTabSelectListener() {

            @Override
            public void onTabSelect(int position) {


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
        mUserName.setOnClickListener(this);
    }

    @Override
    protected void processLogic(Bundle savedInstanceState) {

        initBar();
        initNav();
        initContent();
        //申请权限
        requestSomePermission();
    }


    // 申请权限
    private void requestSomePermission() {

        List<PermissionItem> permissionItems = new ArrayList<>();
        permissionItems.add(new PermissionItem(Manifest.permission.WRITE_EXTERNAL_STORAGE, "数据存储", R.drawable.permission_ic_storage));
        permissionItems.add(new PermissionItem(Manifest.permission.READ_PHONE_STATE, "电话状态", R.drawable.permission_ic_phone));
        permissionItems.add(new PermissionItem(Manifest.permission.ACCESS_NETWORK_STATE, "网络状态", R.drawable.permission_ic_network));
        permissionItems.add(new PermissionItem(Manifest.permission.ACCESS_WIFI_STATE, "WIFI状态", R.drawable.permission_ic_wifi));
        HiPermission.create(MainActivity.this)
                .title("权限申请")
                .permissions(permissionItems)
//                .msg("此APP运行需要此项权限！")
                .animStyle(R.style.PermissionAnimFade)
                .style(R.style.PermissionDefaultNormalStyle)
                .checkMutiPermission(new PermissionCallback() {
                    @Override
                    public void onClose() {
                        //用户关闭权限申请

                    }

                    @Override
                    public void onFinish() {
                        //所有权限申请完成
                        initUser();

                        initNavHeadBG();

                    }

                    @Override
                    public void onDeny(String permission, int position) {
                        //用户不同意
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

        mNavAvatar = (ImageView) view.findViewById(R.id.nav_iv_avatar);
        mNavBg = (LinearLayout) view.findViewById(R.id.nav_header_bg);
        mNavDesc = (TextView) view.findViewById(R.id.nav_tv_desc);
        mNavUserName = (TextView) view.findViewById(R.id.nav_tv_username);


        mNavigationView.setNavigationItemSelectedListener(menuItem -> {

            switch (menuItem.getItemId()) {

                case R.id.ll_nav_me:// 我的信息

                    if (AVUser.getCurrentUser() != null) {
//                        startActivity(new Intent(this, UserInfoActivity.class));
                        goToActivityForResult(UserInfoActivity.class, null, USERINFO_REQUEST_CODE);

                    } else {
                        xToastShow("您还未登录用户！");
                    }

                    break;

                case R.id.ll_nav_deedback:// 问题反馈

                    agent.startDefaultThreadActivity();

                    break;
                case R.id.ll_nav_about:// 关于

                    startActivity(new Intent(mContext, AboutMeActivity.class));

                    break;
                case R.id.ll_nav_exit:// 登出

                    if (AVUser.getCurrentUser() != null) {
                        userlogOut();
                    } else {
                        userLoginOrReg();  //登录
                    }

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
        searchFragment.setOnSearchClickListener((info) ->
        {
//            EventBus.getDefault().postSticky(new MySearchEvent(info));
//            startActivity(new Intent(this, FictionSearchActivity.class));

            goToActivity(FictionSearchActivity.class, Constants.SEARCH_KEY, new MySearchEvent(info));


        });
    }


    @Override
    public void onClick(View view) {
        switch (view.getId()) {
            case R.id.iv_title_thr:

                mTitleThr.setSelected(true);
                mTitleTwo.setSelected(false);
                mTitleOne.setSelected(false);

                xToastUtil.showToast("频道3正在内测,敬请期待");

                break;
            case R.id.iv_title_two:
                mTitleThr.setSelected(false);
                mTitleTwo.setSelected(true);
                mTitleOne.setSelected(false);

                xToastUtil.showToast("2频道正在内测,敬请期待");

                break;
            case R.id.iv_title_one:
                mTitleThr.setSelected(false);
                mTitleTwo.setSelected(false);
                mTitleOne.setSelected(true);

                xToastUtil.showToast("1频道正在内测,敬请期待");

                break;
            case R.id.ll_title_menu:
                drawerLayout.openDrawer(GravityCompat.START);
                break;

            case R.id.main_user_avatar:

                // 用户注册登录
                userLoginOrReg();

                break;

            case R.id.main_user_name:
                // 用户注册登录
                userLoginOrReg();
                break;


        }
    }


    // 用户注册登录
    private void userLoginOrReg() {

        if (AVUser.getCurrentUser() == null) {

            if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.LOLLIPOP) {
                // 启动普通转场动画
                ActivityOptionsCompat oc2 = ActivityOptionsCompat.makeSceneTransitionAnimation(this);
                Intent i2 = new Intent(this, UserLoginActivity.class);
                startActivity(i2, oc2.toBundle());

            } else {
                startActivity(new Intent(this, UserLoginActivity.class));
            }
        } else {
            drawerLayout.openDrawer(GravityCompat.START);
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
    public void onBackPressed() {
        if (drawerLayout.isDrawerOpen(GravityCompat.START)) {
            drawerLayout.closeDrawer(GravityCompat.START);
        } else {
            super.onBackPressed();
        }
    }

    @Override
    public void onPageScrolled(int i, float v, int i1) {

    }

    // 头部工具栏按钮状态切换
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


    @Override
    protected void onActivityResult(int requestCode, int resultCode, Intent data) {
        super.onActivityResult(requestCode, resultCode, data);
        if (requestCode == USERINFO_REQUEST_CODE) {

            // 接收 用户信息修改后的数据并且进行保存
            if (resultCode == UserInfoActivity.RESULT_CODE) {

                try {
                    UserInfoEvent userinfo = (UserInfoEvent) data.getSerializableExtra(UserInfoActivity.RESULT_KEY);
                    saveUserInfo(userinfo);
                } catch (Exception e) {
                    xLogger(e.toString());
                }

            }
        }

    }


    // 保存用户信息
    private void saveUserInfo(UserInfoEvent data) {

        if (AVUser.getCurrentUser() != null) {

            String email = data.getUserEmail();
            String desc = data.getDesc();
            String nick = data.getNickname();

            if (!TextUtils.isEmpty(nick)) {
                mNavUserName.post(() -> mNavUserName.setText(nick));
                mUserName.post(() -> mUserName.setText(nick));
            }

            if (!TextUtils.isEmpty(desc)) {
                mNavDesc.post(() -> mNavDesc.setText(desc));
            }

            AVUser.getCurrentUser().setEmail(email);
            AVUser.getCurrentUser().saveInBackground();

            // 修改表数据
            // 第一参数是 className,第二个参数是 objectId
            AVObject todo = AVObject.createWithoutData("UserInfo", Constants.USER_INFO_ID);
            // 修改 content
            todo.put("desc", desc);
            todo.put("nickname", nick);
            // 保存到云端
            todo.saveInBackground();

            mCache.put(Constants.USER_INFO_KEY, data);

        }

    }


    //登录或者注册成功后
    @Subscribe(threadMode = ThreadMode.MAIN)
    public void ReceviceMessage(LoginEvent event) {
        if (event != null) {
            initUser();
        }

    }


    //用户信息修改后更新UI
    @Subscribe(threadMode = ThreadMode.MAIN)
    public void ReceviceUserInfo(UserInfoEvent event) {
        if (event != null) {

            mUserName.post(() -> mUserName.setText(event.getNickname()));
            mNavDesc.post(() -> mNavDesc.setText(event.getDesc()));

            if (!TextUtils.isEmpty(event.getNickname())) {
                mNavUserName.post(() -> mNavUserName.setText(event.getNickname()));
            }


            if (event.isAvatarUpdate()) {

//                Bitmap img = AppImageMgr.getBitmapByteArray(event.getAvatar(), 512, 512);
//                ImgLoadUtil.displayCircle(mContext, mUserAva, img);
//                ImgLoadUtil.displayCircle(mContext, mNavAvatar, img);

                AVQuery<AVObject> avQuery = new AVQuery<>("UserInfo");
                avQuery.getInBackground(Constants.USER_INFO_ID, new GetCallback<AVObject>() {
                    @Override
                    public void done(AVObject avObject, AVException e) {
                        AVFile avatar = avObject.getAVFile("avatar");
                        avatar.getDataInBackground(new GetDataCallback() {
                            @Override
                            public void done(byte[] bytes, AVException e) {
                                // bytes 就是文件的数据流
                                if (e == null) {
                                    if (bytes != null) {

                                        Bitmap img = AppImageMgr.getBitmapByteArray(bytes, 512, 512);
                                        ImgLoadUtil.displayCircle(mContext, mUserAva, img);
                                        ImgLoadUtil.displayCircle(mContext, mNavAvatar, img);

                                    }
                                }
                            }
                        }, new ProgressCallback() {
                            @Override
                            public void done(Integer integer) {
                                // 下载进度数据，integer 介于 0 和 100。
                            }
                        });
                    }
                });

            }

        }

    }


    private void initUser() {

        List<PermissionItem> permissionItems = new ArrayList<>();
        permissionItems.add(new PermissionItem(Manifest.permission.WRITE_EXTERNAL_STORAGE, "数据存储", R.drawable.permission_ic_storage));
        permissionItems.add(new PermissionItem(Manifest.permission.READ_PHONE_STATE, "电话状态", R.drawable.permission_ic_phone));
        permissionItems.add(new PermissionItem(Manifest.permission.ACCESS_NETWORK_STATE, "网络状态", R.drawable.permission_ic_network));
        permissionItems.add(new PermissionItem(Manifest.permission.ACCESS_WIFI_STATE, "WIFI状态", R.drawable.permission_ic_wifi));
        HiPermission.create(MainActivity.this)
                .title("权限申请")
                .permissions(permissionItems)
//                .msg("此APP运行需要此项权限！")
                .animStyle(R.style.PermissionAnimFade)
                .style(R.style.PermissionDefaultNormalStyle)
                .checkMutiPermission(new PermissionCallback() {
                    @Override
                    public void onClose() {
                        //用户关闭权限申请

                    }

                    @Override
                    public void onFinish() {
                        //所有权限申请完成

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
                                                String desc = TextUtils.isEmpty(info.getString("desc")) ? "心情" : info.getString("desc");
                                                AVFile avatar = info.getAVFile("avatar");
                                                String nickname = info.getString("nickname");  //昵称
                                                String username = TextUtils.isEmpty(nickname) ? currentUser.getUsername() : nickname;
                                                if (avatar != null) {
                                                    // 把图片下载回来
                                                    avatar.getDataInBackground(new GetDataCallback() {
                                                        @Override
                                                        public void done(byte[] bytes, AVException e) {
                                                            // bytes 就是文件的数据流
                                                            if (e == null) {
                                                                if (bytes != null) {
                                                                    Bitmap img = AppImageMgr.getBitmapByteArray(bytes, 512, 512);
                                                                    ImgLoadUtil.displayCircle(mContext, mUserAva, img);
                                                                    ImgLoadUtil.displayCircle(mContext, mNavAvatar, img);
                                                                }
                                                            }
                                                        }
                                                    }, new ProgressCallback() {
                                                        @Override
                                                        public void done(Integer integer) {
                                                            // 下载进度数据，integer 介于 0 和 100。
                                                        }
                                                    });
                                                }

                                                mUserName.post(() -> mUserName.setText(username));
                                                mNavDesc.post(() -> mNavDesc.setText(desc));
                                                mNavUserName.post(() -> mNavUserName.setText(username));

                                                mNavigationView.getMenu().findItem(R.id.ll_nav_exit).setTitle("登出用户");


                                                Constants.USER_INFO_ID = info.getObjectId();  //保存用户关联的信息表，方便下次查询


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
//            xToastShow("当前未登录");
                        }

                    }

                    @Override
                    public void onDeny(String permission, int position) {
                        //用户不同意
                    }

                    @Override
                    public void onGuarantee(String permission, int position) {

                    }
                });

    }


    private void userlogOut() {
        AVUser.logOut();// 清除缓存用户对象

        mUserName.post(() -> mUserName.setText("未登录"));
        mNavUserName.post(() -> mNavUserName.setText("未登录"));
        mNavDesc.post(() -> mNavDesc.setText("心情"));
        mUserAva.post(() -> mUserAva.setImageResource(R.drawable.user_def_avatar));
        mNavAvatar.post(() -> mNavAvatar.setImageResource(R.drawable.user_def_avatar));
        mNavigationView.getMenu().findItem(R.id.ll_nav_exit).setTitle("用户登录");

        xToastShow("当前未登录");
    }


    // 下载侧边头部背景图
    private void initNavHeadBG() {

//        AVQuery<AVObject> avQuery = new AVQuery<>("AdvertisingItem");
//        // 按时间，降序排列
//        avQuery.orderByDescending("createdAt");
//        avQuery.whereEqualTo("isShow", 1);  //确认为1时才下载显示
//        avQuery.whereEqualTo("advType", 1);  //确认为背景图
//        avQuery.limit(1);// 最多返回 1 条结果
//        avQuery.findInBackground(new FindCallback<AVObject>() {
//            @Override
//            public void done(List<AVObject> list, AVException e) {
//                if (list.size() > 0) {
//
//                    String img = list.get(0).getString("img");
//
//                    GlideUtils.loadImageBitmap(mContext, img, new GlideUtils.ImageLoadListener<String, Bitmap>() {
//                        @Override
//                        public void onLoadingComplete(String uri, ImageView view, Bitmap resource) {
//                            if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.JELLY_BEAN) {
//                                mNavBg.setBackground(new BitmapDrawable(mContext.getResources(), resource));
//                            }
//
//                        }
//
//                        @Override
//                        public void onLoadingError(String source, Exception e) {
//
//                        }
//                    });
//                }
//
//            }
//        });

    }


// 判断ImageView的图片是否为默认
//    ImageView imageView = (ImageView)view.findViewById(R.id.pic);
//     if(imageView.getDrawable().getCurrent().getConstantState()==getResources().getDrawable(R.drawable.pic).getConstantState()){
//        Toast.makeText(view.getContext(), "图片未发生变化", 0).show();
//    }else{
//        Toast.makeText(view.getContext(), "图片加载完成，跳转", 0).show();
//    }


}


