package com.youth.xf.ui.demo.home;

import android.annotation.SuppressLint;
import android.content.Intent;
import android.os.Bundle;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.view.View;
import android.widget.ImageButton;
import android.widget.TextView;

import com.chad.library.adapter.base.BaseQuickAdapter;
import com.chad.library.adapter.base.BaseViewHolder;
import com.chad.library.adapter.base.listener.OnItemClickListener;
import com.youth.xf.R;
import com.youth.xf.base.AFengFragment;
import com.youth.xf.ui.demo.comic.ComicWebActivity;
import com.youth.xf.ui.demo.live.LiveWebActivity;
import com.youth.xf.ui.demo.mv.BiliAgentWebActivity;
import com.youth.xf.utils.AFengUtils.xToastUtil;

import org.greenrobot.eventbus.EventBus;

import java.util.ArrayList;
import java.util.List;

import butterknife.BindView;
import cn.bingoogolapple.bgabanner.BGABanner;

/**
 * 作者： AFeng
 * 时间：2017/3/2
 */

//此注解用来忽略检查
@SuppressLint("ValidFragment")
public class SimpleFragment extends AFengFragment implements View.OnClickListener {
    private static final String TYPE = "mType";
    private String mType = "Android";

    private BGABanner mBGABanner;
    @BindView(R.id.one_rv_list)
    RecyclerView mRecyclerView;
    View headerView;
    ImageButton mHomeMvBtn;


    public static SimpleFragment getInstance() {
        SimpleFragment sf = new SimpleFragment();
        return sf;
    }

    @Override
    public int getLayoutId() {
        return R.layout.afeng_simple_one;
    }

    @Override
    protected void initView(Bundle savedInstanceState) {
        if (getArguments() != null) {
            mType = getArguments().getString(TYPE);
        }

        //动态嵌入布局
        if (headerView == null) {
            headerView = View.inflate(getContext(), R.layout.afeng_fra_one_header, null);
        }

        mHomeMvBtn = (ImageButton) headerView.findViewById(R.id.home_mv_btn);

    }

    @Override
    protected void setListener() {
        mHomeMvBtn.setOnClickListener(this);
        headerView.findViewById(R.id.comic_btn).setOnClickListener(this);
        headerView.findViewById(R.id.home_live).setOnClickListener(this);
    }

    @Override
    protected void processLogic(Bundle savedInstanceState) {

        initBanner();
        initRecyclerView();

    }

    /**
     * 懒加载一次。如果只想在对用户可见时才加载数据，并且只加载一次数据，在子类中重写该方法
     */
    @Override
    protected void onLazyLoadOnce() {

    }

    /**
     * 对用户可见时触发该方法。如果只想在对用户可见时才加载数据，在子类中重写该方法
     */
    @Override
    protected void onVisibleToUser() {

    }

    /**
     * 对用户不可见时触发该方法
     */
    @Override
    protected void onInvisibleToUser() {

    }


    private void initBanner() {

        mBGABanner = (BGABanner) headerView.findViewById(R.id.banner_main);
        //绑定轮播图片
        mBGABanner.setData(R.drawable.home_bili, R.drawable.home_ban_1, R.drawable.home_ban_2);

        mBGABanner.setDelegate((banner, itemView, model, position) -> {

            switch (position) {
                case 0:

                    String jscode = "javascript:(function() {" +
                            "document.getElementsByClassName('index__downloadBtn__src-home-topArea-')[0].style.display='none';" +
                            "document.getElementsByClassName('index__openClientBtn__src-videoPage-player-')[0].style.display='none';" +
                            "})()";
                    String url = "http://m.bilibili.com/ranking.html";
                    EventBus.getDefault().postSticky(new WebEvent(jscode, url));
                    startActivity(new Intent(getContext(), WebActivity.class));

                    break;

                case 1:

                    String jscode1 = "javascript:(function() {" +
                            "document.getElementsByClassName('index__downloadBtn__src-home-topArea-')[0].style.display='none';" +
                            "document.getElementsByClassName('index__openClientBtn__src-videoPage-player-')[0].style.display='none';" +
                            "})()";
                    String url1 = "http://m.bilibili.com/video/av4597268.html";
                    EventBus.getDefault().postSticky(new WebEvent(jscode1, url1));
                    startActivity(new Intent(getContext(), WebActivity.class));

                    break;

                case 2:

                    String jscode2 = "javascript:(function() {" +
                            "document.getElementsByClassName('m-hometop')[0].style.display='none';" +
                            "document.getElementsByClassName('ftwrap')[0].style.display='none';" +
                            "})()";
                    String url2 = "http://music.163.com/m";
                    EventBus.getDefault().postSticky(new WebEvent(jscode2, url2));
                    startActivity(new Intent(getContext(), WebActivity.class));

                    break;
            }
        });


    }


    private void initRecyclerView() {
        mRecyclerView.setLayoutManager(new LinearLayoutManager(getContext()));
        List<testStatus> statuses = new ArrayList<>();
        statuses.add(new testStatus(true, "11", "22", "33", "44"));
        statuses.add(new testStatus(true, "11", "22", "33", "44"));
        statuses.add(new testStatus(true, "11", "22", "33", "44"));

        oneAdapter adapter = new oneAdapter(R.layout.afeng_one_item, statuses);
        adapter.openLoadAnimation();
        mRecyclerView.setAdapter(adapter);
        mRecyclerView.addOnItemTouchListener(new OnItemClickListener() {
            @Override
            public void onSimpleItemClick(BaseQuickAdapter adapter, View view, int position) {

                if (position == 0 || position == 1 || position == 3) {
                    xToastUtil.showToast("正在施工中……");

                }
            }
        });

        adapter.addHeaderView(headerView);
    }

    /**
     * Called when a view has been clicked.
     *
     * @param v The view that was clicked.
     */
    @Override
    public void onClick(View v) {
        switch (v.getId()) {
            case R.id.home_mv_btn:
                startActivity(new Intent(this.getContext(), BiliAgentWebActivity.class));
                break;

            case R.id.comic_btn:
                startActivity(new Intent(this.getContext(), ComicWebActivity.class));
                break;

            case R.id.home_live:
                startActivity(new Intent(this.getContext(), LiveWebActivity.class));
                break;
        }
    }


    class oneAdapter extends BaseQuickAdapter<testStatus, BaseViewHolder> {

        public oneAdapter(int layoutResId, List<testStatus> data) {
            super(layoutResId, data);
        }

        @Override
        protected void convert(BaseViewHolder helper, testStatus item) {

        }
    }


}