package com.youth.xf.ui.demo.meizi;

import android.content.Intent;
import android.graphics.Bitmap;
import android.os.Bundle;

import android.support.annotation.Nullable;
import android.support.v7.widget.CardView;
import android.support.v7.widget.RecyclerView;
import android.support.v7.widget.StaggeredGridLayoutManager;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.ProgressBar;

import com.bumptech.glide.Glide;
import com.bumptech.glide.load.engine.DiskCacheStrategy;
import com.bumptech.glide.request.animation.GlideAnimation;
import com.bumptech.glide.request.target.SimpleTarget;
import com.chad.library.adapter.base.BaseQuickAdapter;
import com.chad.library.adapter.base.BaseViewHolder;
import com.orhanobut.logger.Logger;
import com.youth.xf.R;
import com.youth.xf.base.AFengFragment;

import com.youth.xf.ui.demo.bean.GankIoDataBean;
import com.youth.xf.utils.AFengUtils.DensityUtil;
import com.youth.xf.utils.AFengUtils.xToastUtil;


import org.greenrobot.eventbus.EventBus;

import java.io.FileNotFoundException;
import java.util.ArrayList;
import java.util.List;

import butterknife.BindView;
import io.reactivex.Observer;
import io.reactivex.android.schedulers.AndroidSchedulers;
import io.reactivex.disposables.Disposable;
import io.reactivex.schedulers.Schedulers;

import static com.youth.xf.utils.network.NetworkAvailableUtils.isNetworkAvailable;


/**
 * Created by Administrator on 2017/5/6.
 */

public class MeiZiFragment extends AFengFragment {

    @BindView(R.id.meizi_recyclerView)
    RecyclerView mRecyclerView;
    @BindView(R.id.meizi_progressBar)
    ProgressBar mProgressBar;

    private String cacheKey = "meiziurl";
    private String id = "福利";
    private int page = 1;  //当前页数
    private int per_page = 10; //每页图片数量

    private List<GankIoDataBean.ResultBean> mMeizhiList = new ArrayList<>();

    MeiZiFragment.oneAdapter mAdapter = null;


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

        mAdapter = new MeiZiFragment.oneAdapter(R.layout.item_meizi, mMeizhiList);
        mAdapter.setEnableLoadMore(true);  //开启上拉加载
        //第一个参数表示列数或者行数，第二个参数表示滑动方向,瀑布流
        mRecyclerView.setLayoutManager(new StaggeredGridLayoutManager(2, StaggeredGridLayoutManager.VERTICAL));
        mRecyclerView.setAdapter(mAdapter);
    }

    @Override
    protected void setListener() {

        mAdapter.setOnItemClickListener((adapter, view, position) -> {
            //点击进入详情页

            ArrayList<String> imgUrlList = new ArrayList<>();
            for (int i = 0; i < mMeizhiList.size(); i++) {
                imgUrlList.add(mMeizhiList.get(i).getUrl());
            }

            //TODO 缓存一下URL
//            new Thread(() -> App.getACache().put(cacheKey,new MeiZiCache(imgUrlList))).start();

            EventBus.getDefault().postSticky(new MeiziEvent(position, imgUrlList));

            startActivity(new Intent(getContext(), MeiZiBigImageActivity.class));

        });

        //加载更多
        mAdapter.setOnLoadMoreListener(() -> {
            page++;
            try {
                loadNetData(true);

            } catch (FileNotFoundException e) {
                e.printStackTrace();
            }
        });

    }

    @Override
    protected void processLogic(Bundle savedInstanceState) {

    }

    /**
     * 懒加载一次。如果只想在对用户可见时才加载数据，并且只加载一次数据，在子类中重写该方法
     */
    @Override
    protected void onLazyLoadOnce() {
        try {
            loadNetData(false);
        } catch (FileNotFoundException e) {
            e.printStackTrace();
        }
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


    private void loadNetData(boolean isupdate) throws FileNotFoundException {
        mProgressBar.setVisibility(View.VISIBLE);
        mAdapter.openLoadAnimation();

        if (isNetworkAvailable(getContext())) {

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

                            if (gankIoDataBean != null && gankIoDataBean.getResults() != null && gankIoDataBean.getResults().size() > 0) {

                                mMeizhiList.addAll(gankIoDataBean.getResults());

                                mAdapter.loadMoreComplete();

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

        } else {

            //TODO 读取缓存
//            MeiZiCache mcache = (MeiZiCache) App.getACache().getAsObject(cacheKey);
//            List<String> img_urls = mcache.getImgUrlList();

//            xToastUtil.showToast(img_urls.get(0));

            xToastUtil.showToast("图片加载失败！请打开网络连接！");
        }


    }


    //列表适配器
    class oneAdapter extends BaseQuickAdapter<GankIoDataBean.ResultBean, BaseViewHolder> {

        private int screenWidth;

        /**
         * Same as QuickAdapter#QuickAdapter(Context,int) but with
         * some initialization data.
         *
         * @param layoutResId The layout resource id of each item.
         * @param data        A new list is created out of this one to avoid mutable list
         */
        public oneAdapter(int layoutResId, @Nullable List<GankIoDataBean.ResultBean> data) {
            super(layoutResId, data);
        }


        /**
         * Implement this method and use the helper to adapt the view to the given item.
         *
         * @param helper A fully initialized helper.
         * @param item   The item that needs to be displayed.
         */
        @Override
        protected void convert(BaseViewHolder helper, GankIoDataBean.ResultBean item) {
            setData(helper, item);
        }


        private void setData(BaseViewHolder helper, GankIoDataBean.ResultBean item) {

            screenWidth = DensityUtil.getScreenWidth();

            CardView cardView = helper.getView(R.id.item_meizi_root);
            ImageView image = helper.getView(R.id.meizi_photo);

            Glide.with(helper.getConvertView().getContext())
                    .load(item.getUrl())
                    .asBitmap()
                    .placeholder(R.drawable.pic_placeholder)
                    .diskCacheStrategy(DiskCacheStrategy.ALL)
                    .centerCrop()
                    .into(new SimpleTarget<Bitmap>(screenWidth / 2, screenWidth / 2) {
                        @Override
                        public void onResourceReady(Bitmap resource, GlideAnimation<? super Bitmap> glideAnimation) {
                            int width = resource.getWidth();
                            int height = resource.getHeight();
                            //计算高宽比
                            int finalHeight = (screenWidth / 2) * height / width;
                            ViewGroup.LayoutParams layoutParams = cardView.getLayoutParams();
                            layoutParams.height = finalHeight;
                            image.setImageBitmap(resource);
                        }
                    });

        }

    }


}
