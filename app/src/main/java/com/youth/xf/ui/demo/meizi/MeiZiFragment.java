package com.youth.xf.ui.demo.meizi;

import android.content.Intent;
import android.os.Bundle;

import android.support.v7.widget.RecyclerView;
import android.support.v7.widget.StaggeredGridLayoutManager;
import android.view.View;
import android.widget.ImageView;
import android.widget.ProgressBar;

import com.bumptech.glide.Glide;
import com.chad.library.adapter.base.BaseQuickAdapter;
import com.chad.library.adapter.base.BaseViewHolder;
import com.chad.library.adapter.base.listener.OnItemClickListener;
import com.orhanobut.logger.Logger;
import com.youth.xf.R;
import com.youth.xf.base.AFengFragment;

import com.youth.xf.ui.demo.bean.GankIoDataBean;


import java.util.ArrayList;
import java.util.List;

import butterknife.BindView;
import io.reactivex.Observer;
import io.reactivex.android.schedulers.AndroidSchedulers;
import io.reactivex.disposables.Disposable;
import io.reactivex.schedulers.Schedulers;


/**
 * Created by Administrator on 2017/5/6.
 */

public class MeiZiFragment extends AFengFragment {

    @BindView(R.id.meizi_recyclerView)
    RecyclerView mRecyclerView;
    @BindView(R.id.meizi_progressBar)
    ProgressBar mProgressBar;

    private String id = "福利";
    private int page = 1;  //当前页数
    private int per_page = 10; //每页图片数量
    ArrayList<String> imgUrlList = new ArrayList<>();

    MeiZiFragment.oneAdapter mAdapter = null;

    private GankIoDataBean meiziBean;  //本地数据

    private boolean isFirst = true;
    private boolean isPrepared = false;


    private static MeiZiFragment instance;

    public static MeiZiFragment getInstance() {
        if (instance == null) {
            synchronized (MeiZiFragment.class) {
                if (instance == null) {
                    instance = new MeiZiFragment();
                }
            }
        }
        return instance;
    }


    @Override
    protected int getLayoutId() {
        return R.layout.fragment_meizi;
    }

    @Override
    protected void initView(Bundle savedInstanceState) {

        mAdapter = new MeiZiFragment.oneAdapter(R.layout.item_meizi, null);
        mAdapter.setEnableLoadMore(true);  //开启上拉加载
        //第一个参数表示列数或者行数，第二个参数表示滑动方向,瀑布流
        mRecyclerView.setLayoutManager(new StaggeredGridLayoutManager(2, StaggeredGridLayoutManager.VERTICAL));
        mRecyclerView.setAdapter(mAdapter);
    }

    @Override
    protected void setListener() {

        //加载更多
        mAdapter.setOnLoadMoreListener(() -> {
            page++;
            loadNetData(true);
        });

        isPrepared = true;
    }

    @Override
    protected void processLogic(Bundle savedInstanceState) {

    }

    /**
     * 懒加载一次。如果只想在对用户可见时才加载数据，并且只加载一次数据，在子类中重写该方法
     */
    @Override
    protected void onLazyLoadOnce() {
        loadData();
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


    protected void loadData() {

        if (!isPrepared || !isFirst) {
            return;
        }

        if (meiziBean != null && meiziBean.getResults() != null && meiziBean.getResults().size() > 0) {

            imgUrlList.clear();
            for (int i = 0; i < meiziBean.getResults().size(); i++) {
                imgUrlList.add(meiziBean.getResults().get(i).getUrl());
            }

            //获取缓存

        } else {
            loadNetData(false);
        }
    }


    private void loadNetData(boolean isupdate) {
        mProgressBar.setVisibility(View.VISIBLE);
        mAdapter.openLoadAnimation();

        MeiZiRepository.getInstance().getGankIoData(id, page, per_page, id, isupdate)
                .subscribeOn(Schedulers.io())
                .unsubscribeOn(Schedulers.io())
                .observeOn(AndroidSchedulers.mainThread())
                .subscribe(new Observer<GankIoDataBean>() {
                    Disposable d;

                    @Override
                    public void onSubscribe(Disposable disposable) {
                        d = disposable;

                    }

                    @Override
                    public void onNext(GankIoDataBean gankIoDataBean) {

                        meiziBean = gankIoDataBean;
                        Logger.d("onNext" + page);
                        if (page == 1) {
                            if (gankIoDataBean != null && gankIoDataBean.getResults() != null && gankIoDataBean.getResults().size() > 0) {
                                imgUrlList.clear();
                                for (int i = 0; i < gankIoDataBean.getResults().size(); i++) {
                                    imgUrlList.add(gankIoDataBean.getResults().get(i).getUrl());
                                }
                                mAdapter.setNewData(gankIoDataBean.getResults());

                                //TODO  进行本地缓存

                                mAdapter.notifyDataSetChanged();

                                mRecyclerView.addOnItemTouchListener(new OnItemClickListener() {
                                    @Override
                                    public void onSimpleItemClick(BaseQuickAdapter baseQuickAdapter, View view, int i) {
                                        //点击进入详情页

                                        Bundle bundle = new Bundle();
                                        bundle.putInt("selet", 2);// 2,大图显示当前页数，1,头像，不显示页数
                                        bundle.putInt("code", i);//第几张
                                        bundle.putStringArrayList("imageuri", imgUrlList);
                                        Intent intent = new Intent(getContext(), MeiZiBigImageActivity.class);
                                        intent.putExtras(bundle);
                                        getContext().startActivity(intent);

                                    }
                                });
                                // 显示成功后就不是第一次了，不再刷新
                                isFirst = false;
                            }

                        } else {
                            if (gankIoDataBean != null && gankIoDataBean.getResults() != null && gankIoDataBean.getResults().size() > 0) {
                                mAdapter.loadMoreComplete();  //加载完成
                                mAdapter.setNewData(gankIoDataBean.getResults());
                                mAdapter.notifyDataSetChanged();

                                imgUrlList.clear();
                                for (int i = 0; i < gankIoDataBean.getResults().size(); i++) {
                                    imgUrlList.add(gankIoDataBean.getResults().get(i).getUrl());
                                }
                                Logger.d("加载");
                            } else {
                                mAdapter.loadMoreEnd();  //没有更多的内容加载
                                Logger.d("没有更多的内容加载");
                            }
                        }

                    }

                    @Override
                    public void onError(Throwable throwable) {
                        d.dispose();
                        if (page > 1) {
                            page--;
                        }

                        mAdapter.loadMoreFail();  //加载失败
                        if (mAdapter.getItemCount() == 0) {

                        }
                        mProgressBar.setVisibility(View.GONE);
                        Logger.d("HH3 网络请求错误：" + throwable.toString());
                    }

                    @Override
                    public void onComplete() {
                        d.dispose();
                        mProgressBar.setVisibility(View.GONE);
                        mAdapter.loadMoreComplete();

                        Logger.d("MoreEnd");
                    }
                });

    }


    //列表适配器
    class oneAdapter extends BaseQuickAdapter<GankIoDataBean.ResultBean, BaseViewHolder> {

        public oneAdapter(int layoutResId, List<GankIoDataBean.ResultBean> data) {
            super(layoutResId, data);
        }


        private void setData(BaseViewHolder helper, GankIoDataBean.ResultBean item) {

            Glide.with(helper.getConvertView().getContext())
                    .load(item.getUrl())
                    .fitCenter()
                    .into((ImageView) helper.getView(R.id.meizi_photo));
        }

        @Override
        protected void convert(BaseViewHolder helper, GankIoDataBean.ResultBean item) {
            setData(helper, item);
        }
    }


}
