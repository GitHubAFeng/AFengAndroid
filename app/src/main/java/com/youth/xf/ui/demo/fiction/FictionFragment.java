package com.youth.xf.ui.demo.fiction;

import android.os.Bundle;
import android.support.annotation.Nullable;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.widget.ImageView;

import com.bumptech.glide.Glide;
import com.chad.library.adapter.base.BaseQuickAdapter;
import com.chad.library.adapter.base.BaseViewHolder;
import com.youth.xf.R;
import com.youth.xf.base.AFengFragment;


import java.util.ArrayList;
import java.util.List;

import butterknife.BindView;

/**
 * Created by Administrator on 2017/6/21.
 *
 * 小说
 */

public class FictionFragment extends AFengFragment {

    @BindView(R.id.fiction_recyclerView)
    RecyclerView mRecyclerView;

    myAdapter adapter = null;

    List<FictionModel> fictionDatas = new ArrayList<>();



    public static FictionFragment newInstance() {
        FictionFragment fragment = new FictionFragment();
        return fragment;
    }

    /**
     * 初始化布局,返回layout
     */
    @Override
    protected int getLayoutId() {
        return R.layout.fragment_fiction_list;
    }

    /**
     * 初始化View控件
     *
     * @param savedInstanceState
     */
    @Override
    protected void initView(Bundle savedInstanceState) {

        adapter = new myAdapter(R.layout.item_fiction_list_item, fictionDatas);

        mRecyclerView.setLayoutManager(new LinearLayoutManager(getContext()));
        mRecyclerView.setAdapter(adapter);

    }

    /**
     * 给View控件添加事件监听器
     */
    @Override
    protected void setListener() {

    }

    /**
     * 处理业务逻辑，状态恢复等操作
     *
     * @param savedInstanceState
     */
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



    void initData(){
        new Thread(()->fictionDatas = JsoupFictionHomeManager.get().getKswHome()).start();

    }



    class myAdapter extends BaseQuickAdapter<FictionModel, BaseViewHolder> {


        /**
         * Same as QuickAdapter#QuickAdapter(Context,int) but with
         * some initialization data.
         *
         * @param layoutResId The layout resource id of each item.
         * @param data        A new list is created out of this one to avoid mutable list
         */
        public myAdapter(int layoutResId, @Nullable List<FictionModel> data) {
            super(layoutResId, data);
        }

        public myAdapter(@Nullable List<FictionModel> data) {
            super(data);
        }

        public myAdapter(int layoutResId) {
            super(layoutResId);
        }

        /**
         * Implement this method and use the helper to adapt the view to the given item.
         *
         * @param helper A fully initialized helper.
         * @param item   The item that needs to be displayed.
         */
        @Override
        protected void convert(BaseViewHolder helper, FictionModel item) {
            helper.setText(R.id.fic_title, item.getTitle());

        }
    }


}
