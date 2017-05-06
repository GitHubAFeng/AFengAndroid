package com.youth.xf.ui.demo.fragments;

import android.annotation.SuppressLint;
import android.os.Bundle;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.view.View;
import android.widget.TextView;

import com.chad.library.adapter.base.BaseQuickAdapter;
import com.chad.library.adapter.base.BaseViewHolder;
import com.chad.library.adapter.base.listener.OnItemClickListener;
import com.orhanobut.logger.Logger;
import com.youth.xf.R;
import com.youth.xf.base.AFengFragment;
import com.youth.xf.ui.entity.testStatus;
import com.youth.xf.utils.ToastUtil;

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
public class SimpleFragment extends AFengFragment {
    private static final String TYPE = "mType";
    private String mType = "Android";
    private String mTitle;
    private BGABanner mBGABanner;
    @BindView(R.id.one_rv_list)
    RecyclerView mRecyclerView;
    View headerView;


    public static SimpleFragment getInstance(String title) {
        SimpleFragment sf = new SimpleFragment();
        sf.mTitle = title;
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

        if (headerView == null) {
            headerView = View.inflate(getContext(), R.layout.afeng_fra_one_header, null);
        }

    }

    @Override
    protected void setListener() {

    }

    @Override
    protected void processLogic(Bundle savedInstanceState) {
        TextView card_title_tv = (TextView) headerView.findViewById(R.id.card_title_tv);
        card_title_tv.setText(mTitle);
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
        mBGABanner.setData(R.drawable.test_baner01, R.drawable.test_baner02, R.drawable.test_banner03);

        mBGABanner.setDelegate((banner, itemView, model, position) -> ToastUtil.showToast("点击了" + position));


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
                ToastUtil.showToast(Integer.toString(position));
            }
        });

        adapter.addHeaderView(headerView);
    }


    class oneAdapter extends BaseQuickAdapter<testStatus, BaseViewHolder> {

        public oneAdapter(int layoutResId, List<testStatus> data) {
            super(layoutResId, data);
        }

        @Override
        protected void convert(BaseViewHolder helper, testStatus item) {
            switch (helper.getLayoutPosition() %
                    3) {
                case 0:
                    helper.setImageResource(R.id.iv, R.mipmap.animation_img1);
                    break;
                case 1:
                    helper.setImageResource(R.id.iv, R.mipmap.animation_img2);
                    break;
                case 2:
                    helper.setImageResource(R.id.iv, R.mipmap.animation_img3);
                    break;
            }
        }
    }


}