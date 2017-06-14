package com.youth.xf.ui.demo.movie;

import android.content.Intent;
import android.os.Bundle;
import android.support.design.widget.FloatingActionButton;

import android.support.v4.app.ActivityCompat;
import android.support.v4.app.ActivityOptionsCompat;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.support.v7.widget.StaggeredGridLayoutManager;
import android.text.TextUtils;
import android.view.View;
import android.view.animation.OvershootInterpolator;
import android.widget.ImageView;
import android.widget.ProgressBar;

import com.afollestad.materialdialogs.MaterialDialog;
import com.bumptech.glide.Glide;
import com.chad.library.adapter.base.BaseQuickAdapter;
import com.chad.library.adapter.base.BaseViewHolder;
import com.chad.library.adapter.base.listener.OnItemClickListener;
import com.youth.xf.R;
import com.youth.xf.base.AFengFragment;
import com.youth.xf.ui.demo.api.HttpClient;


import org.greenrobot.eventbus.EventBus;

import java.util.List;

import butterknife.BindView;
import io.reactivex.Observer;
import io.reactivex.android.schedulers.AndroidSchedulers;
import io.reactivex.disposables.Disposable;
import io.reactivex.schedulers.Schedulers;


/**
 * Created by Administrator on 2017/5/3.
 * 电影列表页
 */

public class MovieFragment extends AFengFragment {

    @BindView(R.id.moive_recyclerView)
    RecyclerView mRecyclerView;
    @BindView(R.id.moive_progressBar)
    ProgressBar mProgressBar;
    @BindView(R.id.moive_fab_normal)
    FloatingActionButton mFabButton;

    private int mStart = 0;
    private int mCount = 21;

    private static final int ANIM_DURATION_FAB = 400;

    MovieFragment.oneAdapter mAdapter = null;


    public static MovieFragment newInstance() {
        MovieFragment fragment = new MovieFragment();
        return fragment;
    }

    @Override
    protected int getLayoutId() {
        return R.layout.fragment_moive_top;
    }

    @Override
    protected void initView(Bundle savedInstanceState) {

        mAdapter = new MovieFragment.oneAdapter(R.layout.moive_item, null);

        //构造器中，第一个参数表示列数或者行数，第二个参数表示滑动方向,瀑布流
//        mRecyclerView.setLayoutManager(new StaggeredGridLayoutManager(3, StaggeredGridLayoutManager.VERTICAL));
        mRecyclerView.setLayoutManager(new LinearLayoutManager(getContext()));
        mRecyclerView.setAdapter(mAdapter);


    }

    @Override
    protected void setListener() {
        setUpFAB();
    }

    @Override
    protected void processLogic(Bundle savedInstanceState) {

    }

    /**
     * 懒加载一次。如果只想在对用户可见时才加载数据，并且只加载一次数据，在子类中重写该方法
     */
    @Override
    protected void onLazyLoadOnce() {
        setUpRecyclerView();
    }

    /**
     * 对用户可见时触发该方法。如果只想在对用户可见时才加载数据，在子类中重写该方法
     */
    @Override
    protected void onVisibleToUser() {
//        startFABAnimation();
    }

    /**
     * 对用户不可见时触发该方法
     */
    @Override
    protected void onInvisibleToUser() {

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


    private void doSearch(String keyword) {
        mProgressBar.setVisibility(View.VISIBLE);
//        mAdapter.clearItems();
//        searchObservable.doSearch(keyword);
    }


    public void startFABAnimation() {

        mFabButton.animate()
                .translationY(0)
                .setInterpolator(new OvershootInterpolator(1.f))
                .setStartDelay(500)
                .setDuration(ANIM_DURATION_FAB)
                .start();

    }


    public void setUpRecyclerView() {
        mProgressBar.setVisibility(View.VISIBLE);
        MovieRepository.getInstance().getMovieTop250(mStart, mCount, mStart, false)
                .subscribeOn(Schedulers.io())
                .unsubscribeOn(Schedulers.io())
                .observeOn(AndroidSchedulers.mainThread())
                .subscribe(new Observer<HotMovieBean>() {
                    Disposable d;

                    @Override
                    public void onSubscribe(Disposable disposable) {
                        d = disposable;
                    }

                    @Override
                    public void onNext(HotMovieBean hotMovieBean) {

                        if (mStart == 0) {
                            if (hotMovieBean != null && hotMovieBean.getSubjects() != null && hotMovieBean.getSubjects().size() > 0) {
                                mAdapter.setNewData(hotMovieBean.getSubjects());
                                mAdapter.notifyDataSetChanged();
                                mAdapter.openLoadAnimation();
                                mRecyclerView.addOnItemTouchListener(new OnItemClickListener() {
                                    @Override
                                    public void onSimpleItemClick(BaseQuickAdapter baseQuickAdapter, View view, int i) {

                                        //传递该点击单元的数据对象给详情页
                                        SubjectsBean movie = hotMovieBean.getSubjects().get(i);
                                        EventBus.getDefault().postSticky(movie.getId().toString());

                                        Intent intent = new Intent(getActivity(), MovieDetailActivity.class);

                                        ActivityOptionsCompat options =
                                                ActivityOptionsCompat.makeSceneTransitionAnimation(getActivity(),
                                                        view.findViewById(R.id.iv_top_photo), getString(R.string.transition_book_img));

                                        ActivityCompat.startActivity(getActivity(), intent, options.toBundle());
                                    }
                                });
                            }
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


    //列表适配器
    class oneAdapter extends BaseQuickAdapter<SubjectsBean, BaseViewHolder> {

        public oneAdapter(int layoutResId, List<SubjectsBean> data) {
            super(layoutResId, data);
        }

        private void setData(BaseViewHolder helper, SubjectsBean item) {
            helper.setText(R.id.tv_name, item.getTitle());

            String desc = "导演: " + (item.getCasts().size() > 0 ? item.getCasts().get(0) : "")
                    + "\n首映年: " + item.getYear() + "\n评分: " + item.getCollect_count();

            helper.setText(R.id.tv_rate, desc);

            Glide.with(helper.getConvertView().getContext())
                    .load(item.getImages().getSmall())
                    .fitCenter()
                    .into((ImageView) helper.getView(R.id.iv_top_photo));
        }

        @Override
        protected void convert(BaseViewHolder helper, SubjectsBean item) {
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
