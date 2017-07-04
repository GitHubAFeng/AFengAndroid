package com.afeng.xf.ui.demo.fiction;

import android.Manifest;
import android.content.Intent;
import android.os.Bundle;
import android.support.annotation.Nullable;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.text.TextUtils;
import android.view.View;
import android.widget.ImageView;
import android.widget.ProgressBar;

import com.afeng.xf.base.BaseFragment;
import com.afeng.xf.widget.hipermission.HiPermission;
import com.afeng.xf.widget.hipermission.PermissionCallback;
import com.afeng.xf.widget.hipermission.PermissionItem;
import com.bumptech.glide.Glide;
import com.chad.library.adapter.base.BaseQuickAdapter;
import com.chad.library.adapter.base.BaseViewHolder;
import com.afeng.xf.R;
import com.afeng.xf.base.AFengFragment;


import org.greenrobot.eventbus.EventBus;

import java.util.ArrayList;
import java.util.List;

import butterknife.BindView;
import io.reactivex.Observable;
import io.reactivex.ObservableOnSubscribe;
import io.reactivex.android.schedulers.AndroidSchedulers;
import io.reactivex.schedulers.Schedulers;

/**
 * Created by Administrator on 2017/6/21.
 * <p>
 * 小说
 */

public class FictionFragment extends BaseFragment {

    @BindView(R.id.fiction_recyclerView)
    RecyclerView mRecyclerView;

    @BindView(R.id.fiction_progressBar)
    ProgressBar mProgressBar;

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

        adapter.setOnItemClickListener((adapter, view, position) -> {
            EventBus.getDefault().postSticky(adapter.getData().get(position));

            startActivity(new Intent(this.getContext(), FictionChapterActivity.class));
        });

        adapter.setOnLoadMoreListener(() -> loadMoreData());

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
        requestSomePermission();
    }

    /**
     * 对用户可见时触发该方法。如果只想在对用户可见时才加载数据，在子类中重写该方法
     */
    @Override
    protected void onVisibleToUser() {

        if (fictionDatas.size() == 0) {
            initData();
        }

    }

    /**
     * 对用户不可见时触发该方法
     */
    @Override
    protected void onInvisibleToUser() {

    }


    // 申请权限
    private void requestSomePermission() {

        List<PermissionItem> permissionItems = new ArrayList<>();
        permissionItems.add(new PermissionItem(Manifest.permission.WRITE_EXTERNAL_STORAGE, "数据存储", R.drawable.permission_ic_storage));
        permissionItems.add(new PermissionItem(Manifest.permission.READ_PHONE_STATE, "电话状态", R.drawable.permission_ic_phone));
        permissionItems.add(new PermissionItem(Manifest.permission.ACCESS_NETWORK_STATE, "网络状态", R.drawable.permission_ic_network));
        permissionItems.add(new PermissionItem(Manifest.permission.ACCESS_WIFI_STATE, "WIFI状态", R.drawable.permission_ic_wifi));
        HiPermission.create(getContext())
                .title("权限申请")
                .permissions(permissionItems)
//                .msg("此APP运行需要此项权限！")
                .animStyle(R.style.PermissionAnimFade)
                .style(R.style.PermissionDefaultNormalStyle)
                .checkMutiPermission(new PermissionCallback() {
                    @Override
                    public void onClose() {
                        //用户关闭权限申请

                    }

                    @Override
                    public void onFinish() {
                        //所有权限申请完成
                        initData();
                    }

                    @Override
                    public void onDeny(String permission, int position) {
                        //用户不同意
                    }

                    @Override
                    public void onGuarantee(String permission, int position) {

                    }
                });

    }


    void initData() {


        mProgressBar.setVisibility(View.VISIBLE);
        //Consumer是简易版的Observer
        Observable.create((ObservableOnSubscribe<List<FictionModel>>) e -> {

            List<FictionModel> alldata = JsoupFictionManager.get().getData();

            e.onNext(alldata);
        })
                .subscribeOn(Schedulers.io())
                .observeOn(AndroidSchedulers.mainThread())
                .subscribe(fictionModels -> {
                    fictionDatas.addAll(fictionModels);
                    adapter.notifyDataSetChanged();
                    adapter.openLoadAnimation();
                    mProgressBar.setVisibility(View.GONE);
                });


    }


    void loadMoreData() {

        mProgressBar.setVisibility(View.VISIBLE);
        //Consumer是简易版的Observer
        Observable.create((ObservableOnSubscribe<List<FictionModel>>) e -> {

            List<FictionModel> alldata = JsoupFantasyManager.get().getData();

            e.onNext(alldata);
        })
                .subscribeOn(Schedulers.io())
                .observeOn(AndroidSchedulers.mainThread())
                .subscribe(fictionModels -> {
                    fictionDatas.addAll(fictionModels);
                    adapter.notifyDataSetChanged();
                    adapter.openLoadAnimation();
                    mProgressBar.setVisibility(View.GONE);
//                    adapter.loadMoreComplete();
                    adapter.loadMoreEnd();
                });


    }


    class myAdapter extends BaseQuickAdapter<FictionModel, BaseViewHolder> {


        public myAdapter(int layoutResId, @Nullable List<FictionModel> data) {
            super(layoutResId, data);
        }

        @Override
        protected void convert(BaseViewHolder helper, FictionModel item) {

            String title = TextUtils.isEmpty(item.getTitle()) ? "" : item.getTitle().trim();
            String desc = TextUtils.isEmpty(item.getDesc()) ? "" : item.getDesc().trim();
            String author = TextUtils.isEmpty(item.getAuthor()) ? "" : item.getAuthor().trim();
            String cover = TextUtils.isEmpty(item.getCoverImg()) ? "" : item.getCoverImg().trim();

            helper.setText(R.id.fiction_title, title)
                    .setText(R.id.fiction_desc, desc)
                    .setText(R.id.fiction_author, author);

            Glide.with(helper.getConvertView().getContext())
                    .load(cover)
                    .fitCenter()
                    .into((ImageView) helper.getView(R.id.fiction_image));
        }
    }


}
