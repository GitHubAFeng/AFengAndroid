package com.youth.xf.ui.demo.book;

import android.os.Bundle;
import android.support.design.widget.FloatingActionButton;
import android.support.v4.view.ViewPager;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.text.TextUtils;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.view.animation.OvershootInterpolator;
import android.widget.ProgressBar;

import com.afollestad.materialdialogs.MaterialDialog;
import com.chad.library.adapter.base.BaseQuickAdapter;
import com.chad.library.adapter.base.BaseViewHolder;
import com.chad.library.adapter.base.listener.OnItemClickListener;
import com.flyco.tablayout.SlidingTabLayout;
import com.orhanobut.logger.Logger;
import com.youth.xf.R;
import com.youth.xf.base.AFengFragment;
import com.youth.xf.base.mvp.BaseModelCallback;
import com.youth.xf.ui.demo.fragments.SimpleFragment;
import com.youth.xf.ui.entity.testStatus;
import com.youth.xf.utils.ToastUtil;

import java.util.ArrayList;
import java.util.List;

import butterknife.BindView;
import butterknife.ButterKnife;
import butterknife.Unbinder;
import io.reactivex.Observer;
import io.reactivex.disposables.Disposable;

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
        BookRepository.getInstance().getJoke(new Observer<List<BookBean>>() {
            Disposable d;
            @Override
            public void onSubscribe(Disposable disposable) {
                d=disposable;
            }

            @Override
            public void onNext(List<BookBean> bookBeans) {
                ToastUtil.showToast(bookBeans.get(0).getResults().get(0).getDesc());
                BookFragment.oneAdapter adapter = new BookFragment.oneAdapter(R.layout.book_item, bookBeans);
                adapter.openLoadAnimation();
                mRecyclerView.setAdapter(adapter);
                mRecyclerView.addOnItemTouchListener(new OnItemClickListener() {
                    @Override
                    public void onSimpleItemClick(BaseQuickAdapter adapter, View view, int position) {
                        ToastUtil.showToast(Integer.toString(position));
                    }
                });
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


    class oneAdapter extends BaseQuickAdapter<BookBean, BaseViewHolder> {

        public oneAdapter(int layoutResId, List<BookBean> data) {
            super(layoutResId, data);
        }

        @Override
        protected void convert(BaseViewHolder helper, BookBean item) {
            switch (helper.getLayoutPosition() %
                    3) {
                case 0:
                    helper.setText(R.id.tvDesc, item.getResults().get(0).getDesc());
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
