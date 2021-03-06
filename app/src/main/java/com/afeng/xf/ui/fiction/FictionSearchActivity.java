package com.afeng.xf.ui.fiction;

import android.content.Intent;
import android.os.Bundle;
import android.support.annotation.Nullable;

import android.support.v7.app.ActionBar;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.support.v7.widget.Toolbar;
import android.text.TextUtils;
import android.view.Menu;
import android.view.View;
import android.widget.ImageButton;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.ProgressBar;
import android.widget.TextView;

import com.afeng.xf.ui.constants.Constants;
import com.bumptech.glide.Glide;
import com.chad.library.adapter.base.BaseQuickAdapter;
import com.chad.library.adapter.base.BaseViewHolder;
import com.afeng.xf.R;
import com.afeng.xf.base.BaseActivity;
import com.afeng.xf.ui.home.MySearchEvent;
import com.afeng.xf.utils.AFengUtils.PerfectClickListener;
import com.afeng.xf.widget.searchbox.SearchFragment;

import org.greenrobot.eventbus.EventBus;

import java.util.ArrayList;
import java.util.List;

import butterknife.BindView;
import io.reactivex.Observable;
import io.reactivex.ObservableOnSubscribe;
import io.reactivex.android.schedulers.AndroidSchedulers;
import io.reactivex.schedulers.Schedulers;


/**
 * Created by Administrator on 2017/6/30.
 */

public class FictionSearchActivity extends BaseActivity {


    @BindView(R.id.search_error_root)
    LinearLayout mErrorRoot;

    @BindView(R.id.search_error_btn)
    ImageButton mErrorBtn;

    @BindView(R.id.search_toolbar_title)
    TextView mToolTitle;


    @BindView(R.id.fiction_recyclerView)
    RecyclerView mRecyclerView;

    @BindView(R.id.fiction_progressBar)
    ProgressBar mProgressBar;

    myAdapter adapter = null;

    List<FictionModel> fictionDatas = new ArrayList<>();

    private int mPage = 1;

    private String fictionName;

    private SearchFragment searchFragment;

    @BindView(R.id.search_toolbar)
    Toolbar toolbar;


    @Override
    protected int getLayoutId() {
        return R.layout.activity_search;
    }

    @Override
    protected void initView(Bundle savedInstanceState) {

//        if (!EventBus.getDefault().isRegistered(this)) {
//            EventBus.getDefault().register(this);
//        }

        MySearchEvent mySearchEvent = (MySearchEvent) getSerializDataByKey(Constants.SEARCH_KEY);
        if (mySearchEvent != null) {
            fictionName = mySearchEvent.getSearchDesc();
        }


        initBar();

        adapter = new myAdapter(R.layout.item_fiction_search, fictionDatas);

        mRecyclerView.setLayoutManager(new LinearLayoutManager(this));
        mRecyclerView.setAdapter(adapter);
    }

    @Override
    protected void setListener() {
        adapter.setOnItemClickListener((adapter, view, position) -> {
            EventBus.getDefault().postSticky(adapter.getData().get(position));

            startActivity(new Intent(this, FictionChapterActivity.class));
        });

        adapter.setOnLoadMoreListener(() -> loadMoreData(fictionName));

        mErrorBtn.setOnClickListener(new PerfectClickListener() {
            @Override
            protected void onNoDoubleClick(View v) {
                initData(fictionName);
                mErrorRoot.setVisibility(View.GONE);
            }
        });
    }

    @Override
    protected void processLogic(Bundle savedInstanceState) {

        initData(fictionName);
    }


//    @Override
//    public void onDestroy() {
//        MySearchEvent stickyEvent = EventBus.getDefault().getStickyEvent(MySearchEvent.class);
//        if (stickyEvent != null) {
//            EventBus.getDefault().removeStickyEvent(stickyEvent);
//        }
//
//        EventBus.getDefault().unregister(this);
//        super.onDestroy();
//    }


    //创建工具栏右上角菜单， 搜索工具
    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        //加载菜单文件
        getMenuInflater().inflate(R.menu.menu_main, menu);
        return super.onCreateOptionsMenu(menu);
    }


    private void initBar() {

        //自定义标题栏
        setSupportActionBar(toolbar);
        ActionBar actionBar = getSupportActionBar();
        if (actionBar != null) {
            //去除默认Title显示
            actionBar.setDisplayShowTitleEnabled(false);
        }

        toolbar.setOnMenuItemClickListener(menuItem -> {
            switch (menuItem.getItemId()) {
                case R.id.action_search://点击搜索
                    searchFragment.show(getSupportFragmentManager(), SearchFragment.TAG);
                    break;
            }
            return true;
        });

        searchFragment = SearchFragment.newInstance();
        searchFragment.setOnSearchClickListener((info) ->
        {
            fictionDatas.clear();
            initData(info);

        });
    }


//    @Subscribe(threadMode = ThreadMode.MAIN, sticky = true)
//    public void ReceviceMessage(MySearchEvent event) {
//        if (event != null) {
//            fictionName = event.getSearchDesc();
//            initData(fictionName);
//        }
//
//
//    }


    void initData(String name) {

        mProgressBar.setVisibility(View.VISIBLE);
        //Consumer是简易版的Observer
        Observable.create((ObservableOnSubscribe<List<FictionModel>>) e -> {

            List<FictionModel> alldata = JsoupFictionSearchManager.get().getData(name, 0);

            e.onNext(alldata);

        })
                .subscribeOn(Schedulers.io())
                .observeOn(AndroidSchedulers.mainThread())
                .subscribe(fictionModels -> {
                    if (fictionModels.size() > 0) {
                        fictionDatas.addAll(fictionModels);
                        adapter.notifyDataSetChanged();
                        adapter.openLoadAnimation();
                        mToolTitle.setText("搜索到" + fictionModels.size() + "条结果");
                        mErrorRoot.setVisibility(View.GONE);
                    } else {
//                        xToastShow("搜索过于频繁，可稍后重试");
                        mErrorRoot.setVisibility(View.VISIBLE);
                    }
                    mProgressBar.setVisibility(View.GONE);
                });


    }


    void loadMoreData(String name) {

        mProgressBar.setVisibility(View.VISIBLE);
        //Consumer是简易版的Observer
        Observable.create((ObservableOnSubscribe<List<FictionModel>>) e -> {

            List<FictionModel> alldata = JsoupFictionSearchManager.get().getData(name, mPage);

            e.onNext(alldata);
        })
                .subscribeOn(Schedulers.io())
                .observeOn(AndroidSchedulers.mainThread())
                .subscribe(fictionModels -> {

                    if (fictionModels.size() > 0) {
                        fictionDatas.addAll(fictionModels);
                        adapter.notifyDataSetChanged();
                        adapter.openLoadAnimation();

                        mPage++;

                        adapter.loadMoreComplete();
                        mToolTitle.setText("搜索到" + fictionModels.size() + "条结果");

                    } else {

//                        xToastShow("刷新过快，可稍后重试");
                        adapter.loadMoreEnd();

                    }

                    mProgressBar.setVisibility(View.GONE);

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
            String time = TextUtils.isEmpty(item.getTime()) ? "" : item.getTime().trim();
            String last = TextUtils.isEmpty(item.getLastChapter()) ? "" : item.getLastChapter().trim();

            helper.setText(R.id.fiction_title, title)
                    .setText(R.id.fiction_author, author)
                    .setText(R.id.fiction_time, time)
                    .setText(R.id.fiction_last, last)
                    .setText(R.id.fiction_desc, desc);


            Glide.with(helper.getConvertView().getContext())
                    .load(cover)
                    .placeholder(R.drawable.load_err)
                    .fitCenter()
                    .into((ImageView) helper.getView(R.id.fiction_image));
        }
    }


}
