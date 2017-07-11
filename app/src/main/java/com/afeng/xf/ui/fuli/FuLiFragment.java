package com.afeng.xf.ui.fuli;

import android.content.Intent;
import android.os.Bundle;
import android.support.annotation.Nullable;
import android.support.v4.widget.SwipeRefreshLayout;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.view.View;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.TextView;

import com.afeng.xf.R;
import com.afeng.xf.base.BaseFragment;
import com.afeng.xf.ui.meizi.MeiZiActivity;
import com.afeng.xf.utils.AFengUtils.AppImageMgr;
import com.afeng.xf.utils.AFengUtils.XOutdatedUtils;
import com.bumptech.glide.Glide;
import com.bumptech.glide.load.engine.DiskCacheStrategy;
import com.chad.library.adapter.base.BaseQuickAdapter;
import com.chad.library.adapter.base.BaseViewHolder;

import java.util.ArrayList;
import java.util.List;

import butterknife.BindView;

/**
 * Created by Administrator on 2017/7/11.
 */

public class FuLiFragment extends BaseFragment {


    @BindView(R.id.fuli_head_rv)
    RecyclerView fuli_head_rv;

    @BindView(R.id.fuli_content_rv)
    RecyclerView fuli_content_rv;

    @BindView(R.id.fuli_content_SwipeRefresh)
    SwipeRefreshLayout mSwipeLayout;


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
    }

    @Override
    protected void processLogic(Bundle savedInstanceState) {

    }

    @Override
    protected void onLazyLoadOnce() {

        if (mFuLiHeadList.size() == 0) {
            initHeadData();
        }

        if (mFuLiContentList.size() == 0) {
            initContentData();
        }

    }

    @Override
    protected void onVisibleToUser() {

    }

    @Override
    protected void onInvisibleToUser() {

    }


    private void initHead() {

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

        mContentAdapter.setEnableLoadMore(true);  //开启上拉加载
        //设置布局管理器
        LinearLayoutManager linearLayoutManager = new LinearLayoutManager(getContext());
        linearLayoutManager.setOrientation(LinearLayoutManager.VERTICAL);
        fuli_content_rv.setLayoutManager(linearLayoutManager);
        fuli_content_rv.setAdapter(mContentAdapter);

    }


    private void initHeadData() {

        mFuLiHeadList.add(new FuLiHeadBean("", XOutdatedUtils.getDrawable(R.drawable.fuli_head_meizi), "Genk妹子", "meizi"));
        mFuLiHeadList.add(new FuLiHeadBean("", XOutdatedUtils.getDrawable(R.drawable.fuli_head_meizi), "Genk妹子", "meizi"));
        mFuLiHeadList.add(new FuLiHeadBean("", XOutdatedUtils.getDrawable(R.drawable.fuli_head_meizi), "Genk妹子", "meizi"));
        mFuLiHeadList.add(new FuLiHeadBean("", XOutdatedUtils.getDrawable(R.drawable.fuli_head_meizi), "Genk妹子", "meizi"));
        mFuLiHeadList.add(new FuLiHeadBean("", XOutdatedUtils.getDrawable(R.drawable.fuli_head_meizi), "Genk妹子", "meizi"));
        mFuLiHeadList.add(new FuLiHeadBean("", XOutdatedUtils.getDrawable(R.drawable.fuli_head_meizi), "Genk妹子", "meizi"));
        mFuLiHeadList.add(new FuLiHeadBean("", XOutdatedUtils.getDrawable(R.drawable.fuli_head_meizi), "Genk妹子", "meizi"));
        mFuLiHeadList.add(new FuLiHeadBean("", XOutdatedUtils.getDrawable(R.drawable.fuli_head_meizi), "Genk妹子", "meizi"));
        mFuLiHeadList.add(new FuLiHeadBean("", XOutdatedUtils.getDrawable(R.drawable.fuli_head_meizi), "Genk妹子", "meizi"));
        mFuLiHeadList.add(new FuLiHeadBean("", XOutdatedUtils.getDrawable(R.drawable.fuli_head_meizi), "Genk妹子", "meizi"));
        mFuLiHeadList.add(new FuLiHeadBean("", XOutdatedUtils.getDrawable(R.drawable.fuli_head_meizi), "Genk妹子", "meizi"));
        mFuLiHeadList.add(new FuLiHeadBean("", XOutdatedUtils.getDrawable(R.drawable.fuli_head_meizi), "Genk妹子", "meizi"));

    }

    private void initContentData() {


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

            if (item.getImgRes() != null) {
//                Glide.with(helper.getConvertView().getContext())
//                        .load(item.getImgRes())
//                        .placeholder(R.drawable.pic_placeholder)
//                        .diskCacheStrategy(DiskCacheStrategy.ALL)
//                        .into(imageView);

                imageView.setImageDrawable(item.getImgRes());

            }


        }


    }


    private class contentAdapter extends BaseQuickAdapter<FuLiContentBean, BaseViewHolder> {


        public contentAdapter(int layoutResId, @Nullable List<FuLiContentBean> data) {
            super(layoutResId, data);
        }

        @Override
        protected void convert(BaseViewHolder helper, FuLiContentBean item) {

        }
    }

}
