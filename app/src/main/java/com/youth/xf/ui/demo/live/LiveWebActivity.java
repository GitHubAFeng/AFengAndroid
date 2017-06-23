package com.youth.xf.ui.demo.live;

import android.annotation.SuppressLint;
import android.content.Intent;
import android.graphics.Color;
import android.os.Bundle;
import android.support.design.widget.AppBarLayout;
import android.support.v4.widget.NestedScrollView;
import android.support.v4.widget.SwipeRefreshLayout;
import android.support.v7.widget.Toolbar;
import android.view.KeyEvent;
import android.webkit.WebView;
import android.webkit.WebViewClient;
import android.widget.TextView;
import android.widget.Toast;

import com.just.library.AgentWeb;
import com.just.library.ChromeClientCallbackManager;
import com.youth.xf.R;
import com.youth.xf.base.BaseActivity;


import butterknife.BindView;

/**
 * Created by Administrator on 2017/6/21.
 */

public class LiveWebActivity extends BaseActivity{

    final String mUrl = "https://m.douyu.com/";

    AgentWeb mAgentWeb = null;

    @BindView(R.id.web_swipe_container)
    SwipeRefreshLayout mSwipeLayout;
    @BindView(R.id.web_toolbar)
    Toolbar mToolbar;
    @BindView(R.id.web_toolbar_title)
    TextView mTitleTextView;

    @BindView(R.id.web_container)
    NestedScrollView mwebContainer;

    @BindView(R.id.web_appbar_layout)
    AppBarLayout mAppbarlayout;

    WebView mWebView = null;


    /**
     * 初始化布局,返回layout
     */
    @Override
    protected int getLayoutId() {
        return R.layout.activity_web;
    }

    /**
     * 初始化布局以及View控件
     *
     * @param savedInstanceState
     */
    @SuppressLint("ClickableViewAccessibility")
    @Override
    protected void initView(Bundle savedInstanceState) {

        mSwipeLayout.setColorSchemeResources(R.color.holo_blue_bright,
                R.color.holo_green_light, R.color.holo_orange_light,
                R.color.holo_red_light);


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


        if (mAgentWeb == null) {

            mAgentWeb = AgentWeb.with(this)//传入Activity
                    .setAgentWebParent(mwebContainer, new NestedScrollView.LayoutParams(-1, -1))//传入AgentWeb 的父控件 ，如果父控件为 RelativeLayout ， 那么第二参数需要传入 RelativeLayout.LayoutParams
                    .useDefaultIndicator()// 使用默认进度条
                    .defaultProgressBarColor() // 使用默认进度条颜色
                    .setReceivedTitleCallback(mCallback) //设置 Web 页面的 title 回调
                    .setSecutityType(AgentWeb.SecurityType.strict)
                    .createAgentWeb()//
                    .ready()
                    .go(mUrl);

            mAgentWeb.getLoader().loadUrl(mUrl);

        }

        if (mWebView == null) {
            mWebView = mAgentWeb.getWebCreator().get();
        }

        mWebView.getSettings().setJavaScriptEnabled(true);
        mWebView.setWebViewClient(new MyWebClient());

    }


    /**
     * 给View控件添加事件监听器
     */
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
                    mAgentWeb.getLoader().reload();
                    mSwipeLayout.setRefreshing(false);
                }
        );

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

        if (mAgentWeb.handleKeyEvent(keyCode, event)) {
            return true;
        }

        if (keyCode == KeyEvent.KEYCODE_BACK && event.getAction() == KeyEvent.ACTION_DOWN) {
            if ((System.currentTimeMillis() - exitTime) > 2000) {
                Toast.makeText(getApplicationContext(), "2秒内再按一次返回键将退出本站~", Toast.LENGTH_SHORT).show();
                exitTime = System.currentTimeMillis();
            } else {
                finish();
            }
            return true;
        }
        return super.onKeyDown(keyCode, event);
    }

    @Override
    protected void onPause() {
        mAgentWeb.getWebLifeCycle().onPause();
        super.onPause();

    }

    @Override
    protected void onResume() {
        mAgentWeb.getWebLifeCycle().onResume();
        super.onResume();
    }


    @Override
    protected void onActivityResult(int requestCode, int resultCode, Intent data) {
        mAgentWeb.uploadFileResult(requestCode, resultCode, data);
        super.onActivityResult(requestCode, resultCode, data);
    }


    @Override
    public void onDestroy() {
        super.onDestroy();
        //mAgentWeb.destroy();
        mAgentWeb.getWebLifeCycle().onDestroy();
    }


    private ChromeClientCallbackManager.ReceivedTitleCallback mCallback = new ChromeClientCallbackManager.ReceivedTitleCallback() {
        @Override
        public void onReceivedTitle(WebView view, String title) {

            //标题
            if (mTitleTextView != null)
                mTitleTextView.setText(title);

        }
    };




    //插入JS代码
    private void injectJS(WebView webview) {
        webview.loadUrl("javascript:(function() " +
                "{ " +
                "document.getElementsByClassName('fix-download')[0].style.display='none'; " +
//                "document.getElementsByClassName('m-footer')[0].style.display='none';" +
//                "document.getElementsByClassName('m-page')[0].style.display='none';" +
                "})()");
    }

    private class MyWebClient extends WebViewClient {

        @Override
        public void onPageFinished(WebView view, String url) {
            super.onPageFinished(view, url);
            injectJS(view);
        }


    }


}
