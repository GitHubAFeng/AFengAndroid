package com.afeng.xf.ui.demo.fiction;

import android.graphics.Color;
import android.os.Bundle;
import android.support.design.widget.BottomNavigationView;
import android.support.design.widget.FloatingActionButton;
import android.support.v7.widget.Toolbar;
import android.text.Html;
import android.text.method.ScrollingMovementMethod;
import android.view.MotionEvent;
import android.view.View;
import android.webkit.WebView;
import android.widget.ScrollView;
import android.widget.TextView;

import com.afeng.xf.R;
import com.afeng.xf.base.BaseActivity;
import com.afeng.xf.widget.byeburgernavigationview.ByeBurgerBehavior;

import org.greenrobot.eventbus.EventBus;
import org.greenrobot.eventbus.Subscribe;
import org.greenrobot.eventbus.ThreadMode;

import butterknife.BindView;
import io.reactivex.Observable;
import io.reactivex.ObservableOnSubscribe;
import io.reactivex.android.schedulers.AndroidSchedulers;
import io.reactivex.schedulers.Schedulers;

/**
 * Created by Administrator on 2017/6/23.
 */

public class FictionReadActivity extends BaseActivity {


    @BindView(R.id.read_toolbar_title)
    TextView mread_toolbar_title;

    @BindView(R.id.read_scrollview)
    ScrollView mScrollView;

    @BindView(R.id.read_bye_burger)
    BottomNavigationView mBottomView;

    @BindView(R.id.read_toolbar)
    Toolbar mToolbar;

    @BindView(R.id.read_floatButton)
    FloatingActionButton mFloatButton;

    @BindView(R.id.read_content)
    TextView mTextView;

//    @BindView(R.id.read_webview)
//    WebView mRead_webview;


    ByeBurgerBehavior mBehavior, mBehavior2;

    String preUrl, nestUrl;


    @Override
    protected int getLayoutId() {
        return R.layout.activity_fiction_read;
    }

    @Override
    protected void initView(Bundle savedInstanceState) {
//        mBehavior3 = ByeBurgerBehavior.from(mFloatButton);
        mBehavior = ByeBurgerBehavior.from(mBottomView);
        mBehavior2 = ByeBurgerBehavior.from(mToolbar);

        if (!EventBus.getDefault().isRegistered(this)) {
            EventBus.getDefault().register(this);
        }

        // 设置标题栏
        mToolbar.setTitleTextColor(Color.WHITE);
        mToolbar.setTitle("");
        if (getSupportActionBar() != null) {
            getSupportActionBar().setDisplayHomeAsUpEnabled(true);
        }
        this.setSupportActionBar(mToolbar);

        // 设置了回退按钮，及点击事件的效果
        getSupportActionBar().setDisplayHomeAsUpEnabled(true);
        mToolbar.setNavigationOnClickListener(v -> finish());

        mTextView.setMovementMethod(ScrollingMovementMethod.getInstance());

    }


    @Override
    protected void setListener() {

        mTextView.setOnClickListener(view -> {

            if (mBehavior.isShow()) {
//                StatusBarUtil.hideSystemUI(this);

                mBehavior.hide();
                mBehavior2.hide();
            } else {
                mBehavior.show();
                mBehavior2.show();
//                StatusBarUtil.showSystemUI(this);

            }

        });

//        mRead_webview.setOnTouchListener(new View.OnTouchListener() {
//            @Override
//            public boolean onTouch(View v, MotionEvent event) {
//                if(event.getAction() == MotionEvent.ACTION_DOWN){
//                    if (mBehavior.isShow()) {
////                StatusBarUtil.hideSystemUI(this);
//
//                        mBehavior.hide();
//                        mBehavior2.hide();
//                    } else {
//                        mBehavior.show();
//                        mBehavior2.show();
////                StatusBarUtil.showSystemUI(this);
//
//                    }
//                }
//
//                return false;
//            }
//        });


        mBottomView.setOnNavigationItemSelectedListener(
                item -> {

                    switch (item.getItemId()) {
                        case R.id.menu_read_pre_page:
                            // 上一章

                            Observable.create((ObservableOnSubscribe<FictionContentEvent>) e -> {
                                FictionContentEvent data = JsoupFictionContentManager.get().getData(preUrl);
                                if (data == null) {
                                    data.setFictionContent("内容读取错误，请重新打开页面");
                                }

                                e.onNext(data);
                            }).subscribeOn(Schedulers.io())
                                    .observeOn(AndroidSchedulers.mainThread())
                                    .subscribe(s -> loadData(s));

                            //回到顶部
                            mScrollView.post(() -> mScrollView.fullScroll(ScrollView.FOCUS_UP));

                            break;

                        case R.id.menu_read_page:
                            //回到目录
                            finish();
                            break;

                        case R.id.menu_read_nest_page:
                            // 下一章

                            Observable.create((ObservableOnSubscribe<FictionContentEvent>) e -> {
                                FictionContentEvent data = JsoupFictionContentManager.get().getData(nestUrl);
                                if (data == null) {
                                    data.setFictionContent("内容读取错误，请重新打开页面");
                                }
                                e.onNext(data);
                            }).subscribeOn(Schedulers.io())
                                    .observeOn(AndroidSchedulers.mainThread())
                                    .subscribe(s -> loadData(s));

                            //回到顶部
                            mScrollView.post(() -> mScrollView.fullScroll(ScrollView.FOCUS_UP));

                            break;
                    }

                    return false;
                });


    }

    @Override
    protected void processLogic(Bundle savedInstanceState) {

    }


    @Override
    public void onDestroy() {
        FictionContentEvent stickyEvent = EventBus.getDefault().getStickyEvent(FictionContentEvent.class);
        if (stickyEvent != null) {
            EventBus.getDefault().removeStickyEvent(stickyEvent);
        }

        EventBus.getDefault().unregister(this);

        super.onDestroy();
    }


    @Subscribe(threadMode = ThreadMode.MAIN, sticky = true)
    public void ReceviceMessage(FictionContentEvent event) {
        if (event != null) {

            loadData(event);

        }
    }


    private void loadData(FictionContentEvent event) {
        preUrl = event.getPrePageUrl();
        nestUrl = event.getNextPageUrl();

        mread_toolbar_title.setText(event.getTitle());

        mTextView.setText(Html.fromHtml(event.getFictionContent()));
//        mRead_webview.loadDataWithBaseURL(event.getCurrUrl(), event.getFictionContent(), "text/html", "utf-8", null);

    }


}
