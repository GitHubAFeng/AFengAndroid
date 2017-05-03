package com.youth.xf.ui.demo.book;

import android.content.Intent;
import android.os.Bundle;
import android.support.design.widget.FloatingActionButton;
import android.support.v4.app.ActivityCompat;
import android.support.v4.app.ActivityOptionsCompat;
import android.support.v4.view.ViewPager;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.text.TextUtils;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.view.animation.OvershootInterpolator;
import android.widget.ImageView;
import android.widget.ProgressBar;

import com.afollestad.materialdialogs.MaterialDialog;
import com.bumptech.glide.Glide;
import com.chad.library.adapter.base.BaseQuickAdapter;
import com.chad.library.adapter.base.BaseViewHolder;
import com.chad.library.adapter.base.listener.OnItemClickListener;
import com.flyco.tablayout.SlidingTabLayout;
import com.orhanobut.logger.Logger;
import com.youth.xf.R;
import com.youth.xf.base.AFengFragment;
import com.youth.xf.base.mvp.BaseModelCallback;
import com.youth.xf.ui.demo.api.HttpClient;
import com.youth.xf.ui.demo.api.HttpUtils;
import com.youth.xf.ui.demo.fragments.SimpleFragment;
import com.youth.xf.ui.entity.testStatus;
import com.youth.xf.utils.ToastUtil;

import java.util.ArrayList;
import java.util.List;

import butterknife.BindView;
import butterknife.ButterKnife;
import butterknife.Unbinder;
import io.reactivex.Observer;
import io.reactivex.android.schedulers.AndroidSchedulers;
import io.reactivex.disposables.Disposable;
import io.reactivex.schedulers.Schedulers;

/**
 * Created by AFeng on 2017/4/2.
 */

public class BookFragment extends AFengFragment implements BookContract.View {


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


//    private BookContract.Presenter mPresenter;


    @Override
    protected int getLayoutId() {
        return R.layout.fragment_book;
    }

    @Override
    protected void initView(Bundle savedInstanceState) {

    }

    @Override
    protected void setListener() {

    }

    @Override
    protected void processLogic(Bundle savedInstanceState) {
        setUpFAB();
        setUpRecyclerView();
    }

    @Override
    protected void onVisible() {
        startFABAnimation();
    }

    @Override
    protected void onInvisible() {

    }


    private void doSearch(String keyword) {
        mProgressBar.setVisibility(View.VISIBLE);
//        mAdapter.clearItems();
//        searchObservable.doSearch(keyword);
    }

    @Override
    public void setUpFAB() {
        mFabButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                new MaterialDialog.Builder(getActivity())
                        .title("搜索")
                        //.inputType(InputType.TYPE_CLASS_TEXT | InputType.TYPE_TEXT_VARIATION_PASSWORD)
                        .input("请输入关键字", "", new MaterialDialog.InputCallback() {
                            @Override
                            public void onInput(MaterialDialog dialog, CharSequence input) {
                                // Do something
                                if (!TextUtils.isEmpty(input)) {
                                    doSearch(input.toString());
                                }
                            }
                        }).show();
            }
        });
    }

    @Override
    public void startFABAnimation() {

        mFabButton.animate()
                .translationY(0)
                .setInterpolator(new OvershootInterpolator(1.f))
                .setStartDelay(500)
                .setDuration(ANIM_DURATION_FAB)
                .start();

    }

    @Override
    public void setUpRecyclerView() {
        mRecyclerView.setLayoutManager(new LinearLayoutManager(getContext()));

        HttpClient.Builder.getDouBanService()
                .getBook(mType, mStart, mCount)
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
//                                ToastUtil.showToast(bookBean.getBooks());
                                BookFragment.oneAdapter adapter = new BookFragment.oneAdapter(R.layout.book_item, bookBean.getBooks());
                                adapter.openLoadAnimation();
                                mRecyclerView.setAdapter(adapter);
                                mRecyclerView.addOnItemTouchListener(new OnItemClickListener() {
                                    @Override
                                    public void onSimpleItemClick(BaseQuickAdapter adapter, View view, int position) {
//                                        ToastUtil.showToast(Integer.toString(position));

                                        BooksBean book = bookBean.getBooks().get(position);
                                        Intent intent = new Intent(getActivity(), BookDetailActivity.class);
                                        intent.putExtra("book", book);

                                        ActivityOptionsCompat options =
                                                ActivityOptionsCompat.makeSceneTransitionAnimation(getActivity(),
                                                        view.findViewById(R.id.ivBook_img), getString(R.string.transition_book_img));

                                        ActivityCompat.startActivity(getActivity(), intent, options.toBundle());

                                    }
                                });

                            }
                            mIsFirst = false;
                        }

                    }

                    @Override
                    public void onError(Throwable throwable) {
                        d.dispose();
                    }

                    @Override
                    public void onComplete() {
                        d.dispose();
                    }
                });


//        BookRepository.getInstance().getJoke(new Observer<List<BookBean>>() {
//            Disposable d;
//            @Override
//            public void onSubscribe(Disposable disposable) {
//                d=disposable;
//            }
//
//            @Override
//            public void onNext(List<BookBean> bookBeans) {
//                ToastUtil.showToast(bookBeans.get(0).getResults().get(0).getDesc());
//                BookFragment.oneAdapter adapter = new BookFragment.oneAdapter(R.layout.book_item, bookBeans);
//                adapter.openLoadAnimation();
//                mRecyclerView.setAdapter(adapter);
//                mRecyclerView.addOnItemTouchListener(new OnItemClickListener() {
//                    @Override
//                    public void onSimpleItemClick(BaseQuickAdapter adapter, View view, int position) {
//                        ToastUtil.showToast(Integer.toString(position));
//                    }
//                });
//            }
//
//            @Override
//            public void onError(Throwable throwable) {
//                d.dispose();
//            }
//
//            @Override
//            public void onComplete() {
//                d.dispose();
//            }
//        });


//        adapter.addHeaderView(headerView);
    }


    @Override
    public void setPresenter(BookContract.Presenter presenter) {
//        mPresenter = presenter;
    }

    @Override
    public boolean isActive() {
        return false;
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
