package com.afeng.xf.ui.fuli;

import android.content.Intent;
import android.graphics.Rect;
import android.os.Bundle;
import android.support.annotation.Nullable;
import android.support.v4.widget.SwipeRefreshLayout;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.text.TextUtils;
import android.view.View;
import android.widget.Button;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.ProgressBar;
import android.widget.TextView;

import com.afeng.xf.R;
import com.afeng.xf.base.BaseFragment;
import com.afeng.xf.ui.meizi.MeiZiActivity;
import com.afeng.xf.ui.meizi.MeiZiBigImageActivity;
import com.afeng.xf.ui.meizi.MeiziEvent;
import com.afeng.xf.utils.AFengUtils.AppImageMgr;
import com.afeng.xf.utils.AFengUtils.AppTimeUtils;
import com.afeng.xf.utils.AFengUtils.AppValidationMgr;
import com.afeng.xf.utils.AFengUtils.PerfectClickListener;
import com.afeng.xf.utils.AFengUtils.XOutdatedUtils;
import com.afeng.xf.utils.GlideHelper.ImgLoadUtil;
import com.afeng.xf.widget.multipleImageView.MultiImageView;
import com.bumptech.glide.Glide;
import com.bumptech.glide.load.engine.DiskCacheStrategy;
import com.chad.library.adapter.base.BaseQuickAdapter;
import com.chad.library.adapter.base.BaseViewHolder;

import org.greenrobot.eventbus.EventBus;

import java.util.ArrayList;
import java.util.Date;
import java.util.List;
import java.util.concurrent.TimeUnit;

import butterknife.BindView;

/**
 * Created by Administrator on 2017/7/11.
 */

public class FuLiFragment extends BaseFragment {

    @BindView(R.id.fuli_progressBar)
    ProgressBar mProgressBar;

    @BindView(R.id.fuli_content_rv)
    RecyclerView fuli_content_rv;

    @BindView(R.id.fuli_content_SwipeRefresh)
    SwipeRefreshLayout mSwipeLayout;

    View headerView; //头部
    RecyclerView fuli_head_rv;

    headAdapter mHeadAdapter;
    contentAdapter mContentAdapter;

    List<FuLiHeadBean> mFuLiHeadList = new ArrayList<>();
    List<FuLiContentBean> mFuLiContentList = new ArrayList<>();


    private static FuLiFragment instance;

    public static FuLiFragment getInstance() {
        if (instance == null) {
            synchronized (FuLiFragment.class) {
                if (instance == null) {
                    instance = new FuLiFragment();
                }
            }
        }
        return instance;
    }


    @Override
    protected int getLayoutId() {
        return R.layout.fragment_fuli;
    }

    @Override
    protected void initView(Bundle savedInstanceState) {

        mSwipeLayout.setColorSchemeResources(R.color.holo_blue_bright,
                R.color.holo_green_light, R.color.holo_orange_light,
                R.color.holo_red_light);

        initHead();
        initContent();

    }

    @Override
    protected void setListener() {

        mHeadAdapter.setOnItemClickListener(new BaseQuickAdapter.OnItemClickListener() {
            @Override
            public void onItemClick(BaseQuickAdapter adapter, View view, int position) {
                FuLiHeadBean item = (FuLiHeadBean) adapter.getItem(position);
                if (item.getTag().equals("meizi")) {
                    startActivity(new Intent(getContext(), MeiZiActivity.class));
                }
            }
        });

        //下拉刷新
        mSwipeLayout.setOnRefreshListener(() -> {
            if (!mSwipeLayout.isRefreshing()) {
                mSwipeLayout.setRefreshing(true);
            }
            initHeadData();
            initContentData();

            mSwipeLayout.setRefreshing(false);

        });

        mHeadAdapter.setOnLoadMoreListener(() -> xToastShow("上拉加载"));
        mContentAdapter.setOnLoadMoreListener(() -> xToastShow("上拉加载"));


    }

    @Override
    protected void processLogic(Bundle savedInstanceState) {

    }

    @Override
    protected void onLazyLoadOnce() {
        mProgressBar.setVisibility(View.VISIBLE);
        if (mFuLiHeadList.size() == 0) {
            initHeadData();
        }

        if (mFuLiContentList.size() == 0) {
            initContentData();
        }
        mProgressBar.setVisibility(View.GONE);
    }

    @Override
    protected void onVisibleToUser() {

    }

    @Override
    protected void onInvisibleToUser() {

    }


    private void initHead() {

        //动态嵌入布局
        if (headerView == null) {
            headerView = View.inflate(getContext(), R.layout.view_fuli_head, null);
        }

        fuli_head_rv = (RecyclerView) headerView.findViewById(R.id.fuli_head_rv);

        mHeadAdapter = new headAdapter(R.layout.item_fuli_head, mFuLiHeadList);
        mHeadAdapter.setEnableLoadMore(true);  //开启上拉加载
        //设置布局管理器
        LinearLayoutManager linearLayoutManager = new LinearLayoutManager(getContext());
        linearLayoutManager.setOrientation(LinearLayoutManager.HORIZONTAL);
        fuli_head_rv.setLayoutManager(linearLayoutManager);
        fuli_head_rv.setAdapter(mHeadAdapter);

    }


    private void initContent() {

        mContentAdapter = new contentAdapter(R.layout.item_fuli_content, mFuLiContentList);

        mContentAdapter.addHeaderView(headerView);
        mContentAdapter.setUpFetchEnable(false);  //关闭自带的下拉
        mContentAdapter.setEnableLoadMore(true);  //开启上拉加载
        //设置布局管理器
        LinearLayoutManager linearLayoutManager = new LinearLayoutManager(getContext());
        linearLayoutManager.setOrientation(LinearLayoutManager.VERTICAL);
        fuli_content_rv.setLayoutManager(linearLayoutManager);
        fuli_content_rv.setAdapter(mContentAdapter);

//        fuli_content_rv.addItemDecoration(new SpaceItemDecoration(30));  //设置item间隔
    }


    private void initHeadData() {

        AppImageMgr imageMgr = new AppImageMgr(getContext());

        String test = AppValidationMgr.cutString("Genk妹子妹子妹子妹子", 8, "…");

        mFuLiHeadList.add(
                new FuLiHeadBean("http://d.hiphotos.baidu.com/zhidao/pic/item/43a7d933c895d143b6a31c4270f082025baf07fd.jpg",
                        imageMgr.getBitmap(R.drawable.fuli_head_meizi),
                        test,
                        "meizi"));

        for (int i = 0; i < 10; i++) {
            mFuLiHeadList.add(
                    new FuLiHeadBean(
                            "",
                            imageMgr.getBitmap(R.drawable.fuli_head_meizi),
                            "Genk妹子" + i,
                            "meizi"
                    ));

        }

    }

    private void initContentData() {

        List<String> temps = new ArrayList<>();
        temps.add("http://e.hiphotos.baidu.com/zhidao/pic/item/b3b7d0a20cf431add7b125aa4836acaf2fdd98c5.jpg");
        temps.add("http://g.hiphotos.baidu.com/zhidao/pic/item/83025aafa40f4bfb7d29cdfe054f78f0f636189a.jpg");
        temps.add("http://h.hiphotos.baidu.com/zhidao/pic/item/962bd40735fae6cde4c1dde10eb30f2442a70f3c.jpg");
        temps.add("http://d.hiphotos.baidu.com/zhidao/pic/item/6159252dd42a28346fd5f75f5fb5c9ea15cebf77.jpg");
        temps.add("http://f.hiphotos.baidu.com/zhidao/pic/item/b3b7d0a20cf431ad76b7c42d4836acaf2edd98b9.jpg");
        temps.add("http://f.hiphotos.baidu.com/zhidao/pic/item/d0c8a786c9177f3e3ac8913b77cf3bc79f3d5603.jpg");

        for (int i = 0; i < 10; i++) {

            mFuLiContentList.add(
                    new FuLiContentBean(
                            temps,
                            "http://g.hiphotos.baidu.com/zhidao/wh%3D600%2C800/sign=8392481200087bf47db95fefc2e37b14/aa18972bd40735fa78ca39e09c510fb30f2408b4.jpg",
                            "妹子" + i,
                            new Date(),
                            0,
                            "测试单元" + i,
                            ""
                    ));

        }
    }


    private class headAdapter extends BaseQuickAdapter<FuLiHeadBean, BaseViewHolder> {


        public headAdapter(int layoutResId, @Nullable List<FuLiHeadBean> data) {
            super(layoutResId, data);
        }

        @Override
        protected void convert(BaseViewHolder helper, FuLiHeadBean item) {

            ImageView imageView = helper.getView(R.id.item_fuli_head_image);
            TextView textView = helper.getView(R.id.item_fuli_head_name);

            textView.setText(item.getName());

            if (TextUtils.isEmpty(item.getImgUrl())) {
                imageView.setImageBitmap(item.getImgRes());

            } else {
                Glide.with(helper.getConvertView().getContext())
                        .load(item.getImgUrl())
                        .placeholder(R.drawable.img_loading_git)
                        .diskCacheStrategy(DiskCacheStrategy.ALL)
                        .into(imageView);

            }
        }

    }


    private class contentAdapter extends BaseQuickAdapter<FuLiContentBean, BaseViewHolder> {


        public contentAdapter(int layoutResId, @Nullable List<FuLiContentBean> data) {
            super(layoutResId, data);
        }

        @Override
        protected void convert(BaseViewHolder helper, FuLiContentBean item) {

            ImageView avaimg = helper.getView(R.id.item_fuli_content_photo);
            TextView name = helper.getView(R.id.item_fuli_content_name);
            TextView time = helper.getView(R.id.item_fuli_content_time);
            TextView desc = helper.getView(R.id.item_fuli_content_desc);
            TextView watch = helper.getView(R.id.item_fuli_content_watch);
            MultiImageView multiImageView = helper.getView(R.id.item_fuli_content_sub);
            ImageView btnImg = helper.getView(R.id.item_fuli_content_btn);


            btnImg.setOnClickListener(new PerfectClickListener() {
                @Override
                protected void onNoDoubleClick(View v) {

                    xToastShow("点击菜单按钮");

                }
            });

            if (TextUtils.isEmpty(item.getAvaUrl())) {
                ImgLoadUtil.displayCircle(mContext, avaimg, item.getAvaUrl());
            }

            if (item.getImgUrlList() != null && item.getImgUrlList().size() > 0) {
                multiImageView.setList(item.getImgUrlList());
                multiImageView.setOnItemClickListener(new MultiImageView.OnItemClickListener() {
                    @Override
                    public void onItemClick(View view, int position) {
//                        xToastShow("选中了" + position + "号小姐~");
                        EventBus.getDefault().postSticky(new MeiziEvent(position, item.getImgUrlList()));

                        startActivity(new Intent(getContext(), MeiZiBigImageActivity.class));
                    }
                });
            }

            name.setText(item.getName());
            desc.setText(item.getDesc());
            time.setText(AppTimeUtils.formatFriendly(item.getTime()));

            String watchStr;
            if (item.getWatch() >= 1000) {
                watchStr = item.getWatch() / 1000 + "K";
            } else {
                watchStr = item.getWatch() + "";
            }
            watch.setText("阅读 " + watchStr);


        }
    }


    //设置item间隔
    class SpaceItemDecoration extends RecyclerView.ItemDecoration {
        int mSpace;

        @Override
        public void getItemOffsets(Rect outRect, View view, RecyclerView parent, RecyclerView.State state) {
            super.getItemOffsets(outRect, view, parent, state);
            outRect.left = mSpace;
            outRect.right = mSpace;
            outRect.bottom = mSpace;
            if (parent.getChildAdapterPosition(view) == 0) {
                outRect.top = mSpace;
            }

        }

        public SpaceItemDecoration(int space) {
            this.mSpace = space;
        }
    }

}
