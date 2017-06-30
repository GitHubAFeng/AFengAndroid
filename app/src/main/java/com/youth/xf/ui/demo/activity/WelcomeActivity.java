package com.youth.xf.ui.demo.activity;

import android.app.Activity;
import android.content.Intent;
import android.os.Bundle;
import android.support.v4.view.ViewPropertyAnimatorListener;
import android.view.View;
import android.widget.ImageView;

import com.avos.avoscloud.AVException;
import com.avos.avoscloud.AVObject;
import com.avos.avoscloud.AVQuery;
import com.avos.avoscloud.FindCallback;
import com.avos.avoscloud.GetCallback;
import com.bumptech.glide.Glide;
import com.youth.xf.base.AFengActivity;
import com.youth.xf.R;
import com.youth.xf.ui.demo.MainActivity;
import com.youth.xf.utils.AFengUtils.AnimHelper;
import com.youth.xf.utils.AFengUtils.XOutdatedUtils;
import com.youth.xf.utils.cache.AppSharePreferenceMgr;

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

public class WelcomeActivity extends AFengActivity {

    private static final String WELCOME_KEY = "welcome";
    private boolean isInMain;
    private boolean isInSplash;

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

        mImageViewPic.setAlpha(0f);
        //默认启动图
        mImageViewDefPic.setImageDrawable(XOutdatedUtils.getDrawable(R.drawable.welcome_def));
        requestImage();

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
    }

    @Override
    protected void setListener() {

    }

    @Override
    protected void processLogic(Bundle savedInstanceState) {

    }

    // 延时启动
    private Observable<Long> getObservable() {
        return Observable.defer(() -> Observable.timer(2, TimeUnit.SECONDS));
    }

    //启动图
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

                if (list.size() > 0) {
                    for (AVObject avObject : list) {

//                        String desc = avObject.getString("desc");
                        String img = avObject.getString("img");

                        if (!mActivity.isFinishing()) {
                            Glide.with(mActivity).load(img).into(mImageViewPic);
                            AnimHelper.alphaHideByMs(mImageViewDefPic, 1000, ProAnimListener);
                            AnimHelper.alphaShow(mImageViewPic, ProAnimListener);
                        }
                    }
                } else {

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


    private void enterApp() {

        try {
            AVQuery<AVObject> avQuery = new AVQuery<>("NetConfig");
            avQuery.getInBackground("5955b85a128fe100581c51b5", new GetCallback<AVObject>() {
                @Override
                public void done(AVObject avObject, AVException e) {

                    if (e == null) {
                        if (avObject != null) {
                            boolean isShowSplash = avObject.getBoolean("isShowSplash");
                            if (isShowSplash) {
                                toSplashActivity();
                                // 显示一次就关掉
                                AVObject todo = AVObject.createWithoutData("NetConfig", "5955b85a128fe100581c51b5");
                                todo.put("isShowSplash", false);
                                todo.saveInBackground();
                            } else {
                                toShow();
                            }
                        } else {
                            toShow();
                        }
                    } else {

                        toShow();
                    }

                }
            });

        } catch (Exception e) {
            toShow();
        } finally {
            finish();

        }

//        toSplashActivity();

    }


    private void toShow() {

        boolean isshow = (boolean) AppSharePreferenceMgr.get(WELCOME_KEY, true);

        if (isshow) {
            toSplashActivity();
            AppSharePreferenceMgr.put(WELCOME_KEY, false);
        } else {
            toMainActivity();
        }
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
        startActivity(intent);
        overridePendingTransition(R.anim.screen_zoom_in, R.anim.screen_zoom_out);
        isInSplash = true;
    }

}
