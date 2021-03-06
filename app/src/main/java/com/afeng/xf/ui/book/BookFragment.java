package com.afeng.xf.ui.book;

import android.Manifest;
import android.content.Intent;
import android.os.Bundle;
import android.support.design.widget.FloatingActionButton;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.text.TextUtils;
import android.view.View;
import android.view.animation.OvershootInterpolator;
import android.widget.ImageView;
import android.widget.ProgressBar;

import com.afeng.xf.base.BaseFragment;
import com.afeng.xf.widget.hipermission.HiPermission;
import com.afeng.xf.widget.hipermission.PermissionCallback;
import com.afeng.xf.widget.hipermission.PermissionItem;
import com.afollestad.materialdialogs.MaterialDialog;
import com.bumptech.glide.Glide;
import com.chad.library.adapter.base.BaseQuickAdapter;
import com.chad.library.adapter.base.BaseViewHolder;
import com.afeng.xf.R;

import org.greenrobot.eventbus.EventBus;

import java.util.ArrayList;
import java.util.List;

import butterknife.BindView;
import io.reactivex.Observer;
import io.reactivex.android.schedulers.AndroidSchedulers;
import io.reactivex.disposables.Disposable;
import io.reactivex.schedulers.Schedulers;


/**
 * Created by AFeng on 2017/4/2.
 */

public class BookFragment extends BaseFragment {


    @BindView(R.id.book_recyclerView)
    RecyclerView mRecyclerView;
    @BindView(R.id.book_progressBar)
    ProgressBar mProgressBar;
    @BindView(R.id.book_fab_normal)
    FloatingActionButton mFabButton;

    private static final int ANIM_DURATION_FAB = 400;

    private String mType = "综合";
    private boolean mIsPrepared;
    private boolean mIsFirst = true;
    // 开始请求的角标
    private int mStart = 0;
    // 一次请求的数量
    private int mCount = 18;

    BookFragment.oneAdapter adapter = null;


    public static BookFragment newInstance() {

        Bundle args = new Bundle();

        BookFragment fragment = new BookFragment();
        fragment.setArguments(args);
        return fragment;
    }


    @Override
    protected int getLayoutId() {
        return R.layout.fragment_book;
    }

    @Override
    protected void initView(Bundle savedInstanceState) {

        adapter = new BookFragment.oneAdapter(R.layout.book_item, null);

        mRecyclerView.setLayoutManager(new LinearLayoutManager(getContext()));
        mRecyclerView.setAdapter(adapter);

    }

    @Override
    protected void setListener() {

        adapter.setOnItemClickListener((adapter, view, position) -> {
            BooksBean book = (BooksBean) adapter.getData().get(position);
            //先发送后注册的要使用postSticky粘性事件
            EventBus.getDefault().postSticky(new BookEvent(book));
            Intent intent = new Intent(getActivity(), BookDetailActivity.class);
            startActivity(intent);

        });

    }

    @Override
    protected void processLogic(Bundle savedInstanceState) {
        setUpFAB();
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
        startFABAnimation();
    }

    /**
     * 对用户不可见时触发该方法
     */
    @Override
    protected void onInvisibleToUser() {

    }


    private void doSearch(String keyword) {
        mProgressBar.setVisibility(View.VISIBLE);

    }


    public void setUpFAB() {
        mFabButton.setOnClickListener(view -> new MaterialDialog.Builder(getActivity())
                .title("搜索")
                //.inputType(InputType.TYPE_CLASS_TEXT | InputType.TYPE_TEXT_VARIATION_PASSWORD)
                .input("请输入关键字", "", (dialog, input) -> {
                    // Do something
                    if (!TextUtils.isEmpty(input)) {
                        doSearch(input.toString());
                    }
                }).show());
    }


    public void startFABAnimation() {

        mFabButton.animate()
                .translationY(0)
                .setInterpolator(new OvershootInterpolator(1.f))
                .setStartDelay(500)
                .setDuration(ANIM_DURATION_FAB)
                .start();

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
                        setUpRecyclerView();
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


    public void setUpRecyclerView() {
        mProgressBar.setVisibility(View.VISIBLE);

        BookRepository.getInstance().getBook(mType, mStart, mCount, mStart, false)
                .subscribeOn(Schedulers.io())
                .unsubscribeOn(Schedulers.io())
                .observeOn(AndroidSchedulers.mainThread())
                .subscribe(new Observer<BookBean>() {

                    Disposable d;

                    @Override
                    public void onSubscribe(Disposable disposable) {
                        d = disposable;
                    }

                    @Override
                    public void onNext(BookBean bookBean) {

                        if (mStart == 0) {
                            if (bookBean != null && bookBean.getBooks() != null && bookBean.getBooks().size() > 0) {
                                adapter.setNewData(bookBean.getBooks());
                                adapter.notifyDataSetChanged();
                                adapter.openLoadAnimation();
                            }
                            mIsFirst = false;
                        }

                    }

                    @Override
                    public void onError(Throwable throwable) {
                        d.dispose();
                        mProgressBar.setVisibility(View.GONE);
                    }

                    @Override
                    public void onComplete() {
                        d.dispose();
                        mProgressBar.setVisibility(View.GONE);
                    }
                });


    }


    class oneAdapter extends BaseQuickAdapter<BooksBean, BaseViewHolder> {

        public oneAdapter(int layoutResId, List<BooksBean> data) {
            super(layoutResId, data);
        }

        private void setData(BaseViewHolder helper, BooksBean item) {
            helper.setText(R.id.tvTitle, item.getTitle());

            String desc = "作者: " + (item.getAuthor().size() > 0 ? item.getAuthor().get(0) : "") + "\n副标题: " + item.getSubtitle()
                    + "\n出版年: " + item.getPubdate() + "\n页数: " + item.getPages() + "\n定价:" + item.getPrice() + "\n简介:" + item.getSummary();
            helper.setText(R.id.tvDesc, desc);

            Glide.with(helper.getConvertView().getContext())
                    .load(item.getImage())
                    .fitCenter()
                    .into((ImageView) helper.getView(R.id.ivBook_img));
        }

        @Override
        protected void convert(BaseViewHolder helper, BooksBean item) {
            switch (helper.getLayoutPosition() %
                    3) {
                case 0:
                    setData(helper, item);
                    break;
                case 1:
                    setData(helper, item);
                    break;
                case 2:
                    setData(helper, item);
                    break;
            }
        }
    }


}
