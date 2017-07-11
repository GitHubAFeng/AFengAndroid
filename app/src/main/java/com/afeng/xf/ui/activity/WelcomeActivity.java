package com.afeng.xf.ui.activity;

import android.Manifest;
import android.app.Activity;
import android.content.Intent;
import android.os.Bundle;
import android.support.v4.view.ViewPropertyAnimatorListener;
import android.view.View;
import android.widget.ImageView;

import com.afeng.xf.base.BaseActivity;
import com.afeng.xf.utils.AFengUtils.StatusBarUtil;
import com.afeng.xf.widget.hipermission.HiPermission;
import com.afeng.xf.widget.hipermission.PermissionCallback;
import com.afeng.xf.widget.hipermission.PermissionItem;
import com.avos.avoscloud.AVException;
import com.avos.avoscloud.AVObject;
import com.avos.avoscloud.AVQuery;
import com.avos.avoscloud.FindCallback;
import com.avos.avoscloud.GetCallback;
import com.avos.avoscloud.SaveCallback;
import com.bumptech.glide.Glide;
import com.afeng.xf.R;
import com.afeng.xf.ui.MainActivity;
import com.afeng.xf.utils.AFengUtils.AnimHelper;
import com.afeng.xf.utils.AFengUtils.XOutdatedUtils;
import com.afeng.xf.utils.cache.AppSharePreferenceMgr;

import java.util.ArrayList;
import java.util.List;
import java.util.concurrent.TimeUnit;

import butterknife.BindView;
import io.reactivex.Observable;
import io.reactivex.android.schedulers.AndroidSchedulers;
import io.reactivex.disposables.Disposable;
import io.reactivex.observers.DisposableObserver;


/**
 * Created by AFeng on 2017/3/12.
 */

public class WelcomeActivity extends BaseActivity {

    private static final String WELCOME_KEY = "welcome";
    private boolean isInMain;
    private boolean isInSplash;
    boolean isShowSplash = false;

    Activity mActivity;

    @BindView(R.id.iv_pic)
    ImageView mImageViewPic;
    @BindView(R.id.iv_defult_pic)
    ImageView mImageViewDefPic;

    @Override
    public int getLayoutId() {
        return R.layout.afeng_welcome;
    }

    @Override
    protected void initView(Bundle savedInstanceState) {
        mActivity = this;
        //此页面隐藏状态栏
        StatusBarUtil.hideSystemUI(this);
    }

    @Override
    protected void setListener() {

    }

    @Override
    protected void processLogic(Bundle savedInstanceState) {

        //默认启动图
        mImageViewDefPic.setImageDrawable(XOutdatedUtils.getDrawable(R.drawable.welcome_def));

        requestSomePermission();

    }


    // 申请权限
    private void requestSomePermission() {

        List<PermissionItem> permissionItems = new ArrayList<>();
        permissionItems.add(new PermissionItem(Manifest.permission.WRITE_EXTERNAL_STORAGE, "SD存储权限", R.drawable.permission_ic_storage));
        permissionItems.add(new PermissionItem(Manifest.permission.ACCESS_NETWORK_STATE, "检查是否联网", R.drawable.permission_ic_sensors));
        HiPermission.create(WelcomeActivity.this)
                .title("权限申请")
                .permissions(permissionItems)
                .msg("申请网络需要如下权限")
                .animStyle(R.style.PermissionAnimFade)
                .style(R.style.PermissionDefaultNormalStyle)
                .checkMutiPermission(new PermissionCallback() {
                    @Override
                    public void onClose() {
                        toShow();
                    }

                    @Override
                    public void onFinish() {
                        try {

                            requestImage();

                            loadShowSplash();

                            Disposable dis = getObservable()
                                    .observeOn(AndroidSchedulers.mainThread())
                                    .subscribeWith(new DisposableObserver<Long>() {
                                        @Override
                                        public void onNext(Long aLong) {

                                        }

                                        @Override
                                        public void onError(Throwable e) {

                                        }

                                        @Override
                                        public void onComplete() {
                                            enterApp();
                                        }
                                    });
                            addDisposable(dis);

                        } catch (Exception e) {

                            enterApp();
                        }


                    }

                    @Override
                    public void onDeny(String permission, int position) {
                        toShow();
                    }

                    @Override
                    public void onGuarantee(String permission, int position) {

                    }
                });

    }


    // 延时启动
    private Observable<Long> getObservable() {
//        return Observable.defer(() -> Observable.timer(2, TimeUnit.SECONDS));
        return Observable.timer(2, TimeUnit.SECONDS);
    }


    //网络启动图，多数为广告
    private void requestImage() {

//        AVObject todoFolder = new AVObject("AdvertisingItem");// 构建对象
//        todoFolder.put("img", "http://oki2v8p4s.bkt.clouddn.com/wec_1.jpg");
//        todoFolder.put("isAdv", 0);
//        todoFolder.put("isShow", 1);
//        todoFolder.put("advType", 0);
//        todoFolder.put("desc", "APP启动图");
//        todoFolder.put("url", "");
//
//        todoFolder.saveInBackground();// 保存到服务端


        AVQuery<AVObject> avQuery = new AVQuery<>("AdvertisingItem");
        // 按时间，降序排列
        avQuery.orderByDescending("createdAt");
        avQuery.whereEqualTo("isShow", 1);  //确认为1时才下载显示
        avQuery.whereEqualTo("advType", 0);  //确认为启动图
        avQuery.limit(1);// 最多返回 1 条结果
        avQuery.findInBackground(new FindCallback<AVObject>() {
            @Override
            public void done(List<AVObject> list, AVException e) {

                if (list != null) {
                    if (list.size() > 0) {
                        for (AVObject avObject : list) {

//                        String desc = avObject.getString("desc");
                            String img = avObject.getString("img");

                            if (!mActivity.isFinishing()) {
                                Glide.with(mActivity).load(img).into(mImageViewPic);
                                AnimHelper.alphaHideByMs(mImageViewDefPic, 1000, ProAnimListener);
                                AnimHelper.alphaShow(mImageViewPic, ProAnimListener);
//                            mImageViewPic.setAlpha(0f);
                            }
                        }
                    } else {

                    }
                }


            }
        });

    }


    private ViewPropertyAnimatorListener ProAnimListener = new ViewPropertyAnimatorListener() {
        @Override
        public void onAnimationStart(View view) {

        }

        @Override
        public void onAnimationEnd(View view) {

        }

        @Override
        public void onAnimationCancel(View view) {

        }
    };


    private void loadShowSplash() {

        AVQuery<AVObject> avQuery = new AVQuery<>("NetConfig");
        avQuery.getInBackground("5955b85a128fe100581c51b5", new GetCallback<AVObject>() {
            @Override
            public void done(AVObject avObject, AVException e) {
                if (e == null) {
                    if (avObject != null) {
                        isShowSplash = avObject.getBoolean("isShowSplash");
                    }
                }
            }
        });
    }


    private void enterApp() {

        if (isShowSplash) {

            // 显示一次就关掉
            AVObject todo = AVObject.createWithoutData("NetConfig", "5955b85a128fe100581c51b5");
            todo.put("isShowSplash", false);
            todo.saveInBackground(new SaveCallback() {
                @Override
                public void done(AVException e) {
                    toSplashActivity();
                }
            });

        } else {

            toShow();
        }

        //测试
//        isShowSplash = false;
//        toSplashActivity();

//        try {
//            AVQuery<AVObject> avQuery = new AVQuery<>("NetConfig");
//            avQuery.getInBackground("5955b85a128fe100581c51b5", new GetCallback<AVObject>() {
//                @Override
//                public void done(AVObject avObject, AVException e) {
//
//                    if (e == null) {
//                        if (avObject != null) {
//                            boolean isShowSplash = avObject.getBoolean("isShowSplash");
//                            if (isShowSplash) {
//                                // 显示一次就关掉
//                                AVObject todo = AVObject.createWithoutData("NetConfig", "5955b85a128fe100581c51b5");
//                                todo.put("isShowSplash", false);
//                                todo.saveInBackground(new SaveCallback() {
//                                    @Override
//                                    public void done(AVException e) {
//                                        toSplashActivity();
//                                    }
//                                });
//
//                            } else {
//                                toShow();
//                            }
//                        } else {
//                            toShow();
//                        }
//                    } else {
//
//                        toShow();
//                    }
//
//                }
//            });
//
//        } catch (Exception e) {
//            toShow();
//        }

    }


    private void toShow() {

        boolean isshow = (boolean) AppSharePreferenceMgr.get(WELCOME_KEY, true);

        if (isshow) {
            toSplashActivity();
            AppSharePreferenceMgr.put(WELCOME_KEY, false);
        } else {
            toMainActivity();
        }

        finish();
    }


    private void toMainActivity() {
        if (isInMain) {
            return;
        }
        Intent intent = new Intent(this, MainActivity.class);
        startActivity(intent);
        overridePendingTransition(R.anim.screen_zoom_in, R.anim.screen_zoom_out);
        isInMain = true;
    }

    private void toSplashActivity() {
        if (isInSplash) {
            return;
        }
        Intent intent = new Intent(this, SplashActivity.class);
        intent.putExtra("isshownet", isShowSplash);
        startActivity(intent);
        overridePendingTransition(R.anim.screen_zoom_in, R.anim.screen_zoom_out);
        isInSplash = true;
    }

}
