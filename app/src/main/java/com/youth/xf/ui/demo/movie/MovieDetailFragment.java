package com.youth.xf.ui.demo.movie;

import android.os.Bundle;
import android.widget.TextView;

import com.youth.xf.R;
import com.youth.xf.base.AFengFragment;

import butterknife.BindView;

/**
 * Created by Administrator on 2017/5/3.
 * 电影详情
 */

public class MovieDetailFragment extends AFengFragment {

    @BindView(R.id.tvInfo)
    TextView tvInfo;

    public static MovieDetailFragment newInstance(String info) {
        MovieDetailFragment fragment = new MovieDetailFragment();
        Bundle bundle = new Bundle();
        bundle.putString("str", info);
        fragment.setArguments(bundle);
        return fragment;
    }

    @Override
    protected int getLayoutId() {
        return R.layout.fragment_movie_detail;
    }

    @Override
    protected void initView(Bundle savedInstanceState) {
        tvInfo.setText(getArguments().getString("str"));
    }

    @Override
    protected void setListener() {

    }

    @Override
    protected void processLogic(Bundle savedInstanceState) {

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

}
