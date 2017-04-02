package com.youth.xf.ui.demo.activity;

import android.content.Intent;
import android.graphics.Bitmap;
import android.os.Bundle;
import android.support.v4.view.ViewPropertyAnimatorListener;
import android.view.View;
import android.widget.ImageView;

import com.lzy.okgo.OkGo;
import com.lzy.okgo.callback.BitmapCallback;
import com.orhanobut.logger.Logger;
import com.youth.xf.base.AFengActivity;
import com.youth.xf.R;
import com.youth.xf.ui.constants.ConstantsImageUrls;
import com.youth.xf.ui.demo.MainActivity;
import com.youth.xf.utils.AFengUtils.AnimHelper;
import com.youth.xf.utils.AFengUtils.SPDataUtils;
import com.youth.xf.utils.xutils.XOutdatedUtils;

import java.util.concurrent.Callable;
import java.util.concurrent.TimeUnit;

import butterknife.BindView;
import io.reactivex.Observable;
import io.reactivex.ObservableSource;
import io.reactivex.Observer;
import io.reactivex.android.schedulers.AndroidSchedulers;
import io.reactivex.disposables.Disposable;
import io.reactivex.observers.DisposableObserver;
import okhttp3.Call;
import okhttp3.Response;


/**
 * Created by AFeng on 2017/3/12.
 */

public class WelcomeActivity extends AFengActivity {

    private static final String WELCOME_KEY = "welcome";
    private boolean isInMain;
    private boolean isInSplash;

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
        mImageViewPic.setAlpha(0f);
        //默认启动图
        mImageViewDefPic.setImageDrawable(XOutdatedUtils.getDrawable(R.drawable.welcome_def));
        requestImage();

        Disposable dis = getObservable().observeOn(AndroidSchedulers.mainThread()).subscribeWith(new DisposableObserver<Long>() {
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


    private Observable<Long> getObservable() {
        return Observable.defer(new Callable<ObservableSource<? extends Long>>() {
            @Override public ObservableSource<? extends Long> call() throws Exception {
                return Observable.timer(2, TimeUnit.SECONDS);
            }
        });
    }


    private void requestImage() {
        OkGo.get(ConstantsImageUrls.WELCOME_PIC)
                .tag(this)
                .execute(new BitmapCallback() {
                    @Override
                    public void onSuccess(Bitmap bitmap, Call call, Response response) {
                        AnimHelper.alphaHideByMs(mImageViewDefPic, 1000, ProAnimListener);
//                        mImageViewDefPic.setVisibility(View.GONE);
                        mImageViewPic.setImageBitmap(bitmap);
                        AnimHelper.alphaShow(mImageViewPic, ProAnimListener);
                    }

                    @Override
                    public void onError(Call call, Response response, Exception e) {
                        super.onError(call, response, e);
                        Logger.e("启动图下载失败！");
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

        if (SPDataUtils.getBoolean(WELCOME_KEY, true)) {
            toSplashActivity();
            SPDataUtils.putBoolean(WELCOME_KEY, false);
        } else {
            toMainActivity();
//            startActivity(new Intent(this, UserLoginActivity.class));

        }
//        toSplashActivity();
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
        startActivity(intent);
        isInSplash = true;
    }

}
