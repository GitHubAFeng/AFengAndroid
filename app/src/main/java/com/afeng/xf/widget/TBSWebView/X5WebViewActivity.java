package com.afeng.xf.widget.TBSWebView;

import android.annotation.SuppressLint;
import android.content.DialogInterface;
import android.content.Intent;
import android.content.res.Configuration;
import android.graphics.Color;
import android.graphics.PixelFormat;
import android.os.Bundle;
import android.support.design.widget.AppBarLayout;
import android.support.v4.widget.NestedScrollView;
import android.support.v4.widget.SwipeRefreshLayout;
import android.support.v7.app.AlertDialog;
import android.support.v7.widget.Toolbar;
import android.view.KeyEvent;
import android.view.View;
import android.view.ViewGroup;
import android.webkit.JavascriptInterface;
import android.widget.FrameLayout;
import android.widget.ImageView;
import android.widget.ProgressBar;
import android.widget.TextView;
import android.widget.Toast;

import com.afeng.xf.R;
import com.afeng.xf.base.BaseActivity;
import com.afeng.xf.ui.constants.Constants;
import com.afeng.xf.widget.loadingview.SunBabyLoadingView;
import com.tencent.smtt.export.external.interfaces.IX5WebChromeClient;
import com.tencent.smtt.export.external.interfaces.JsResult;
import com.tencent.smtt.sdk.CookieSyncManager;
import com.tencent.smtt.sdk.DownloadListener;
import com.tencent.smtt.sdk.WebChromeClient;
import com.tencent.smtt.sdk.WebSettings;
import com.tencent.smtt.sdk.WebView;
import com.tencent.smtt.sdk.WebViewClient;
import com.tencent.smtt.utils.TbsLog;

import butterknife.BindView;

/**
 * Created by Administrator on 2017/7/1.
 */

public class X5WebViewActivity extends BaseActivity {


    @BindView(R.id.webview_loading)
    SunBabyLoadingView mLoading;

    @BindView(R.id.bili_swipe_container)
    SwipeRefreshLayout mSwipeLayout;
    @BindView(R.id.bili_toolbar)
    Toolbar mToolbar;
    @BindView(R.id.bili_toolbar_title)
    TextView mTitleTextView;

    @BindView(R.id.bili_web_container)
    NestedScrollView mwebContainer;

    @BindView(R.id.bili_appbar_layout)
    AppBarLayout mAppbarlayout;

    @BindView(R.id.bili_progressBar1)
    ProgressBar mPageLoadingProgressBar;

    WebView mWebView = null;

    WebEvent mWebEvent = null;  //传值，传入URL或者HTML


    @Override
    protected int getLayoutId() {
        return R.layout.activity_tbs_webview;
    }

    @Override
    protected void initView(Bundle savedInstanceState) {

        initData();

        // 这个对宿主没什么影响，建议声明
        getWindow().setFormat(PixelFormat.TRANSLUCENT);

        try {
            if (Integer.parseInt(android.os.Build.VERSION.SDK) >= 11) {
                getWindow()
                        .setFlags(
                                android.view.WindowManager.LayoutParams.FLAG_HARDWARE_ACCELERATED,
                                android.view.WindowManager.LayoutParams.FLAG_HARDWARE_ACCELERATED);
            }
        } catch (Exception e) {
            TbsLog.w(TAG, e.getMessage());
        }


        mSwipeLayout.setColorSchemeResources(R.color.holo_blue_bright,
                R.color.holo_green_light, R.color.holo_orange_light,
                R.color.holo_red_light);


        initToolbar();

        initProgressBar();

        initWebView();

    }

    @Override
    protected void setListener() {

        //防止滑动冲突
        mAppbarlayout.addOnOffsetChangedListener((appBarLayout, verticalOffset) -> {

            if (verticalOffset >= 0) {
                mSwipeLayout.setEnabled(true);
            } else {
                mSwipeLayout.setEnabled(false);
            }
        });

        //下拉刷新当前网页
        mSwipeLayout.setOnRefreshListener(() -> {
                    mWebView.reload();
                    mSwipeLayout.setRefreshing(false);
                }
        );


    }


    //两秒内按返回键两次退出程序
    private long exitTime = 0;

    @Override
    public boolean onKeyDown(int keyCode, KeyEvent event) {
        // press back
        if (keyCode == KeyEvent.KEYCODE_BACK) {
            if (mWebView != null && mWebView.canGoBack()) {
                mWebView.goBack();
                if (Integer.parseInt(android.os.Build.VERSION.SDK) >= 16)
                    changGoForwardButton(mWebView);
                return true;
            }
        }

        if (keyCode == KeyEvent.KEYCODE_BACK && event.getAction() == KeyEvent.ACTION_DOWN) {
            if ((System.currentTimeMillis() - exitTime) > 2000) {
                Toast.makeText(getApplicationContext(), "2秒内再按一次将返回主页~", Toast.LENGTH_SHORT).show();
                exitTime = System.currentTimeMillis();
            } else {
                finish();
            }
            return true;
        }

        return super.onKeyDown(keyCode, event);
    }


    @Override
    public void onDestroy() {
        if (mWebView != null)
            mWebView.destroy();
        super.onDestroy();
    }

    // 横竖屏幕切换
    @Override
    public void onConfigurationChanged(Configuration newConfig) {
        // TODO Auto-generated method stub
        try {
            super.onConfigurationChanged(newConfig);
            if (getResources().getConfiguration().orientation == Configuration.ORIENTATION_LANDSCAPE) {

            } else if (getResources().getConfiguration().orientation == Configuration.ORIENTATION_PORTRAIT) {

            }
        } catch (Exception e) {
            e.printStackTrace();
        }
    }


    @Override
    protected void processLogic(Bundle savedInstanceState) {

    }


    private void initToolbar() {

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

    }

    //初始化传值数据
    private void initData() {

        try {
            Intent intent = this.getIntent();
            mWebEvent = (WebEvent) intent.getSerializableExtra(Constants.WEBEVENT);
        } catch (NullPointerException e) {
            TbsLog.w(TAG, e.getMessage());
        } catch (Exception e) {
            TbsLog.w(TAG, e.getMessage());
        }
    }


    private void initWebView() {


        mWebView = new X5WebView(this, null);

        mwebContainer.addView(mWebView, new NestedScrollView.LayoutParams(-1, -1));
        mWebView.getView().setOverScrollMode(View.OVER_SCROLL_ALWAYS);
        mWebView.setWebViewClient(new WebViewClient() {
            @Override
            public boolean shouldOverrideUrlLoading(WebView view, String url) {
                return false;
            }

            @Override
            public void onPageFinished(WebView view, String url) {
                super.onPageFinished(view, url);

                if (Integer.parseInt(android.os.Build.VERSION.SDK) >= 16)
                    changGoForwardButton(view);
            }
        });

        mWebView.setWebChromeClient(new WebChromeClient() {

            @Override
            public boolean onJsConfirm(WebView arg0, String arg1, String arg2,
                                       JsResult arg3) {
                return super.onJsConfirm(arg0, arg1, arg2, arg3);
            }

            View myVideoView;
            View myNormalView;
            IX5WebChromeClient.CustomViewCallback callback;


            /**
             * 全屏播放配置
             */
            @Override
            public void onShowCustomView(View view,
                                         IX5WebChromeClient.CustomViewCallback customViewCallback) {
                FrameLayout normalView = (FrameLayout) findViewById(R.id.web_filechooser);
                ViewGroup viewGroup = (ViewGroup) normalView.getParent();
                viewGroup.removeView(normalView);
                viewGroup.addView(view);
                myVideoView = view;
                myNormalView = normalView;
                callback = customViewCallback;
            }

            @Override
            public void onHideCustomView() {
                if (callback != null) {
                    callback.onCustomViewHidden();
                    callback = null;
                }
                if (myVideoView != null) {
                    ViewGroup viewGroup = (ViewGroup) myVideoView.getParent();
                    viewGroup.removeView(myVideoView);
                    viewGroup.addView(myNormalView);
                }
            }

            @Override
            public boolean onJsAlert(WebView arg0, String arg1, String arg2,
                                     JsResult arg3) {
                /**
                 * write your own custom window alert
                 */
                return super.onJsAlert(null, arg1, arg2, arg3);
            }

            /**
             * set progress of the webView
             */
            @Override
            public void onProgressChanged(WebView webView, int progress) {
                if (progress == 25) {
                    webView.loadUrl(mWebEvent.getInjectJS());  //插入JS
                }

                if (progress >= 30) {
                    mLoading.setVisibility(View.GONE);
                } else {
                    mLoading.setVisibility(View.VISIBLE);
                }

                if (progress == 100) {
                    mPageLoadingProgressBar.setVisibility(View.INVISIBLE);
                } else {
                    if (View.INVISIBLE == mPageLoadingProgressBar.getVisibility()) {
                        mPageLoadingProgressBar.setVisibility(View.VISIBLE);
                    }
                    mPageLoadingProgressBar.setProgress(progress);
                }

                super.onProgressChanged(webView, progress);
            }

            @Override
            public void onReceivedTitle(WebView webView, String s) {
                super.onReceivedTitle(webView, s);
                //标题
                if (mTitleTextView != null)
                    mTitleTextView.setText(s);
            }
        });

        mWebView.setDownloadListener(new DownloadListener() {

            @Override
            public void onDownloadStart(String arg0, String arg1, String arg2,
                                        String arg3, long arg4) {
                TbsLog.d(TAG, "url: " + arg0);
                new AlertDialog.Builder(X5WebViewActivity.this)
                        .setTitle("allow to download？")
                        .setPositiveButton("yes",
                                new DialogInterface.OnClickListener() {
                                    @Override
                                    public void onClick(DialogInterface dialog,
                                                        int which) {
                                        // TODO: 2017/6/7 download
                                        Toast.makeText(
                                                X5WebViewActivity.this,
                                                "fake message: i'll download...",
                                                Toast.LENGTH_SHORT).show();
                                    }
                                })
                        .setNegativeButton("no",
                                new DialogInterface.OnClickListener() {

                                    @Override
                                    public void onClick(DialogInterface dialog,
                                                        int which) {
                                        // TODO: 2017/6/7 download negatived
                                        Toast.makeText(
                                                X5WebViewActivity.this,
                                                "fake message: refuse download...",
                                                Toast.LENGTH_SHORT).show();
                                    }
                                })
                        .setOnCancelListener(
                                new DialogInterface.OnCancelListener() {

                                    @Override
                                    public void onCancel(DialogInterface dialog) {
                                        // TODO: 2017/6/7 download cancelled
                                        Toast.makeText(
                                                X5WebViewActivity.this,
                                                "fake message: refuse download...",
                                                Toast.LENGTH_SHORT).show();
                                    }
                                }).create().show();
            }
        });


        mWebView.addJavascriptInterface(new WebViewJavaScriptFunction() {

            @Override
            public void onJsFunctionCalled(String tag) {
                // TODO Auto-generated method stub
                Toast.makeText(X5WebViewActivity.this, "xian shi", Toast.LENGTH_SHORT).show();
            }

            // JS 交互 X5模式
            @JavascriptInterface
            public void onX5ButtonClicked() {
                enableX5FullscreenFunc();
            }

            // JS 交互 系统webView模式
            @JavascriptInterface
            public void onCustomButtonClicked() {
                disableX5FullscreenFunc();
            }

            // JS 交互 X5小屏模式
            @JavascriptInterface
            public void onLiteWndButtonClicked() {
                enableLiteWndFunc();
            }

            // JS 交互 X5页面内模式
            @JavascriptInterface
            public void onPageVideoClicked() {
                enablePageVideoFunc();
            }
        }, "Android");


        initWebViewSetting();
    }


    private void initProgressBar() {
        mPageLoadingProgressBar.setMax(100);
        mPageLoadingProgressBar.setProgressDrawable(this.getResources()
                .getDrawable(R.drawable.webview_color_progressbar));
    }


    @SuppressLint("SetJavaScriptEnabled")
    private void initWebViewSetting() {
        WebSettings webSetting = mWebView.getSettings();
        webSetting.setAllowFileAccess(true);
        webSetting.setLayoutAlgorithm(WebSettings.LayoutAlgorithm.NARROW_COLUMNS);
        webSetting.setSupportZoom(true);
        webSetting.setBuiltInZoomControls(true);
        webSetting.setUseWideViewPort(true);
        webSetting.setSupportMultipleWindows(false);
        webSetting.setAppCacheEnabled(true);
        webSetting.setDomStorageEnabled(true);
        webSetting.setJavaScriptEnabled(true);
        webSetting.setGeolocationEnabled(true);
        webSetting.setAppCacheMaxSize(Long.MAX_VALUE);
        webSetting.setAppCachePath(this.getDir("appcache", 0).getPath());
        webSetting.setDatabasePath(this.getDir("databases", 0).getPath());
        webSetting.setGeolocationDatabasePath(this.getDir("geolocation", 0)
                .getPath());
        webSetting.setPluginState(WebSettings.PluginState.ON_DEMAND);


        long time = System.currentTimeMillis();

        mWebView.loadUrl(mWebEvent.getWebUrl());

        // 显示加载调试信息,打印webView负载
        TbsLog.d("time-cost", "cost time: " + (System.currentTimeMillis() - time));

        // manage Cookie
        CookieSyncManager.createInstance(this);
        CookieSyncManager.getInstance().sync();
    }


    // /////////////////////////////////////////
    // 与JS交互，向webview发出信息
    private void enableX5FullscreenFunc() {

        if (mWebView.getX5WebViewExtension() != null) {
            Toast.makeText(this, "开启X5全屏播放模式", Toast.LENGTH_LONG).show();
            Bundle data = new Bundle();

            data.putBoolean("standardFullScreen", false);// true表示标准全屏，false表示X5全屏；不设置默认false，

            data.putBoolean("supportLiteWnd", false);// false：关闭小窗；true：开启小窗；不设置默认true，

            data.putInt("DefaultVideoScreen", 2);// 1：以页面内开始播放，2：以全屏开始播放；不设置默认：1

            mWebView.getX5WebViewExtension().invokeMiscMethod("setVideoParams",
                    data);
        }
    }

    private void disableX5FullscreenFunc() {
        if (mWebView.getX5WebViewExtension() != null) {
            Toast.makeText(this, "恢复webkit初始状态", Toast.LENGTH_LONG).show();
            Bundle data = new Bundle();

            data.putBoolean("standardFullScreen", true);// true表示标准全屏，会调起onShowCustomView()，false表示X5全屏；不设置默认false，

            data.putBoolean("supportLiteWnd", false);// false：关闭小窗；true：开启小窗；不设置默认true，

            data.putInt("DefaultVideoScreen", 2);// 1：以页面内开始播放，2：以全屏开始播放；不设置默认：1

            mWebView.getX5WebViewExtension().invokeMiscMethod("setVideoParams",
                    data);
        }
    }

    private void enableLiteWndFunc() {
        if (mWebView.getX5WebViewExtension() != null) {
            Toast.makeText(this, "开启小窗模式", Toast.LENGTH_LONG).show();
            Bundle data = new Bundle();

            data.putBoolean("standardFullScreen", false);// true表示标准全屏，会调起onShowCustomView()，false表示X5全屏；不设置默认false，

            data.putBoolean("supportLiteWnd", true);// false：关闭小窗；true：开启小窗；不设置默认true，

            data.putInt("DefaultVideoScreen", 2);// 1：以页面内开始播放，2：以全屏开始播放；不设置默认：1

            mWebView.getX5WebViewExtension().invokeMiscMethod("setVideoParams",
                    data);
        }
    }

    private void enablePageVideoFunc() {
        if (mWebView.getX5WebViewExtension() != null) {
            Toast.makeText(this, "页面内全屏播放模式", Toast.LENGTH_LONG).show();
            Bundle data = new Bundle();

            data.putBoolean("standardFullScreen", false);// true表示标准全屏，会调起onShowCustomView()，false表示X5全屏；不设置默认false，

            data.putBoolean("supportLiteWnd", false);// false：关闭小窗；true：开启小窗；不设置默认true，

            data.putInt("DefaultVideoScreen", 1);// 1：以页面内开始播放，2：以全屏开始播放；不设置默认：1

            mWebView.getX5WebViewExtension().invokeMiscMethod("setVideoParams",
                    data);
        }
    }


    private void changGoForwardButton(WebView view) {
//        if (view.canGoBack())
//            mBack.setAlpha(enable);
//        else
//            mBack.setAlpha(disable);
//        if (view.canGoForward())
//            mForward.setAlpha(enable);
//        else
//            mForward.setAlpha(disable);
//        if (view.getUrl() != null && view.getUrl().equalsIgnoreCase(mHomeUrl)) {
//            mHome.setAlpha(disable);
//            mHome.setEnabled(false);
//        } else {
//            mHome.setAlpha(enable);
//            mHome.setEnabled(true);
//        }
    }


}
