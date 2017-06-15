package com.youth.xf.ui.demo.mv;

import android.os.Bundle;
import android.support.v4.widget.SwipeRefreshLayout;
import android.view.KeyEvent;
import android.view.MotionEvent;
import android.view.View;
import android.view.ViewGroup;
import android.webkit.WebChromeClient;
import android.webkit.WebSettings;
import android.webkit.WebView;
import android.webkit.WebViewClient;
import android.widget.Toast;

import com.youth.xf.R;
import com.youth.xf.base.BaseActivity;
import com.youth.xf.utils.network.NetworkAvailableUtils;

import butterknife.BindView;

/**
 * Created by Administrator on 2017/6/15.
 */

public class BiliActivity extends BaseActivity {

    //    final String mUrl = "http://live.bilibili.com/";
    final String mUrl = "http://m.acfun.cn/";

    WebView mWebView = null;

    @BindView(R.id.bili_swipe_container)
    SwipeRefreshLayout mSwipeLayout;


    /**
     * 初始化布局,返回layout
     */
    @Override
    protected int getLayoutId() {
        return R.layout.activity_bili;
    }

    /**
     * 初始化布局以及View控件
     *
     * @param savedInstanceState
     */
    @Override
    protected void initView(Bundle savedInstanceState) {

        mSwipeLayout.setColorSchemeResources(R.color.holo_blue_bright,
                R.color.holo_green_light, R.color.holo_orange_light,
                R.color.holo_red_light);

        if (mWebView == null) {
            mWebView = new WebView(this);
            mWebView.setLayoutParams(new ViewGroup.LayoutParams(ViewGroup.LayoutParams.MATCH_PARENT,
                    ViewGroup.LayoutParams.MATCH_PARENT));

            mSwipeLayout.addView(mWebView);
            mWebView.loadUrl(mUrl);

            //添加javaScript支持
            mWebView.getSettings().setJavaScriptEnabled(true);
            //设置通过JS打开新窗口的支持
            mWebView.getSettings().setJavaScriptCanOpenWindowsAutomatically(true);
            mWebView.requestFocus();

            // 设置 缓存模式
            if (NetworkAvailableUtils.isNetworkAvailable(this)) {
                //有网就直接在线加载
                mWebView.getSettings().setCacheMode(WebSettings.LOAD_DEFAULT);
            } else {
                //无网优先使用缓存
                mWebView.getSettings().setCacheMode(WebSettings.LOAD_CACHE_ELSE_NETWORK);
            }
            // webView.getSettings().setBlockNetworkImage(true);// 把图片加载放在最后来加载渲染
            mWebView.getSettings().setRenderPriority(WebSettings.RenderPriority.HIGH);
            // 支持多窗口
//            mWebView.getSettings().setSupportMultipleWindows(true);
            // 开启 DOM storage API 功能
            mWebView.getSettings().setDomStorageEnabled(true);
            // 开启 Application Caches 功能
            mWebView.getSettings().setAppCacheEnabled(true);


            //取消滚动条
            mWebView.setScrollBarStyle(WebView.SCROLLBARS_OUTSIDE_OVERLAY);
            //触摸焦点起作用
            mWebView.requestFocus();
            //点击链接继续在当前browser中响应
            mWebView.setWebViewClient(new WebViewClient() {
                @Override
                public boolean shouldOverrideUrlLoading(WebView view, String url) {
                    view.loadUrl(url);
                    return true;
                }
            });
            //设置进度条
            mWebView.setWebChromeClient(new WebChromeClient() {
                @Override
                public void onProgressChanged(WebView view, int newProgress) {
                    if (newProgress == 100) {
                        //隐藏进度条
                        mSwipeLayout.setRefreshing(false);
                    } else {
                        if (!mSwipeLayout.isRefreshing())
                            mSwipeLayout.setRefreshing(true);
                    }

                    super.onProgressChanged(view, newProgress);
                }
            });

            //防止下拉冲突
            mWebView.setOnTouchListener(new View.OnTouchListener() {
                @Override
                public boolean onTouch(View v, MotionEvent event) {
                    switch (event.getAction()) {
                        case MotionEvent.ACTION_DOWN: {
                            if (mWebView.getScrollY() <= 0) {
                                mSwipeLayout.setEnabled(true);
                            } else {
                                mSwipeLayout.setEnabled(false);
                            }
                        }
                        default:
                            break;

                    }
                    return false;
                }
            });
        }

    }

    /**
     * 给View控件添加事件监听器
     */
    @Override
    protected void setListener() {
        //下拉刷新当前网页
        mSwipeLayout.setOnRefreshListener(() -> mWebView.loadUrl(mWebView.getUrl()));


    }

    /**
     * 处理业务逻辑，状态恢复等操作
     *
     * @param savedInstanceState
     */
    @Override
    protected void processLogic(Bundle savedInstanceState) {


    }


    //两秒内按返回键两次退出程序
    private long exitTime = 0;

    @Override
    public boolean onKeyDown(int keyCode, KeyEvent event) {
        if (keyCode == KeyEvent.KEYCODE_BACK && mWebView.canGoBack()) {

//            mWebView.getSettings().setCacheMode(WebSettings.LOAD_NO_CACHE);  //无缓存模式
            // 返回上一页面
            mWebView.goBack();
            return true;
        }

        if(keyCode == KeyEvent.KEYCODE_BACK && event.getAction() == KeyEvent.ACTION_DOWN){
            if((System.currentTimeMillis()-exitTime) > 2000){
                Toast.makeText(getApplicationContext(), "2秒内再按一次返回键将退出A站~", Toast.LENGTH_SHORT).show();
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
        mWebView.removeAllViews();
        mWebView.destroy();
        super.onDestroy();
    }


}
