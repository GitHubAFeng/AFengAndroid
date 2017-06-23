package com.youth.xf.ui.demo.fiction;

import android.app.Activity;
import android.content.Intent;
import android.graphics.Color;
import android.os.Bundle;
import android.support.annotation.Nullable;
import android.support.design.widget.FloatingActionButton;
import android.support.v7.widget.GridLayoutManager;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.support.v7.widget.Toolbar;
import android.text.AndroidCharacter;
import android.view.View;
import android.widget.ImageView;
import android.widget.TextView;

import com.bumptech.glide.Glide;
import com.chad.library.adapter.base.BaseQuickAdapter;
import com.chad.library.adapter.base.BaseViewHolder;
import com.flaviofaria.kenburnsview.KenBurnsView;
import com.youth.xf.R;
import com.youth.xf.base.AFengActivity;

import org.greenrobot.eventbus.EventBus;
import org.greenrobot.eventbus.Subscribe;
import org.greenrobot.eventbus.ThreadMode;

import java.util.ArrayList;
import java.util.List;

import butterknife.BindView;
import io.reactivex.Observable;
import io.reactivex.ObservableEmitter;
import io.reactivex.ObservableOnSubscribe;
import io.reactivex.android.schedulers.AndroidSchedulers;
import io.reactivex.functions.Consumer;
import io.reactivex.schedulers.Schedulers;


/**
 * Created by Administrator on 2017/6/23.
 */

public class FictionChapterActivity extends AFengActivity {


    @BindView(R.id.chapter_author_des)
    TextView mchapter_author_des;

    @BindView(R.id.chapter_state_des)
    TextView mchapter_state_des;

    @BindView(R.id.chapter_lastupdate_des)
    TextView mchapter_lastupdate_des;

    @BindView(R.id.chapter_latestchapter_des)
    TextView mchapter_latestchapter_des;

    @BindView(R.id.chapter_decs)
    TextView mchapter_decs;

    @BindView(R.id.chapter_title)
    TextView mchapter_title;


    @BindView(R.id.chapter_detail_title_img)
    KenBurnsView mchapter_detail_title_img;


    @BindView(R.id.chapter_toolbar_title)
    TextView mToolbartitle;

    @BindView(R.id.chapter_fab)
    FloatingActionButton mTabBtn;

    @BindView(R.id.chapter_toolbar)
    Toolbar mToolbar;

    @BindView(R.id.chapter_detail_list)
    RecyclerView mRecyclerView;

    myAdapter adapter = null;

    List<FictionModel> fictionDatas = new ArrayList<>();

    Activity activity;

    /**
     * 初始化布局,返回layout
     */
    @Override
    protected int getLayoutId() {
        return R.layout.activity_fiction_chapter;
    }

    /**
     * 初始化布局以及View控件
     *
     * @param savedInstanceState
     */
    @Override
    protected void initView(Bundle savedInstanceState) {

        activity = this;

        if (!EventBus.getDefault().isRegistered(this)) {
            EventBus.getDefault().register(this);
        }

        // 设置标题栏
        mToolbar.setTitleTextColor(Color.WHITE);
        mToolbar.setTitle("");
        if (getSupportActionBar() != null) {
            getSupportActionBar().setDisplayHomeAsUpEnabled(true);
        }
        this.setSupportActionBar(mToolbar);

        // 设置了回退按钮，及点击事件的效果
        getSupportActionBar().setDisplayHomeAsUpEnabled(true);
        mToolbar.setNavigationOnClickListener(v -> finish());


        adapter = new myAdapter(R.layout.item_fiction_chapter_item, fictionDatas);
        //第一个参数为上下文环境，第二个显示列数，第三个参数为方向，第四个参数为是否反转
        mRecyclerView.setLayoutManager(new GridLayoutManager(this, 2));

        mRecyclerView.setAdapter(adapter);

    }

    /**
     * 给View控件添加事件监听器
     */
    @Override
    protected void setListener() {
        // Toolbar 单击，Recyclerview 跳到顶部
        mToolbar.setOnClickListener(v -> {
            RecyclerView.LayoutManager layoutManager = mRecyclerView.getLayoutManager();
            if (layoutManager instanceof GridLayoutManager) {
                GridLayoutManager Manager = (GridLayoutManager) layoutManager;
                Manager.scrollToPositionWithOffset(0, 0);
                Manager.setStackFromEnd(true);
            }
        });

        adapter.setOnItemClickListener((adapter, view, position) -> {
            FictionModel model = (FictionModel) adapter.getData().get(position);
            String url = model.getChapterUrl();

            Observable.create(new ObservableOnSubscribe<FictionModel>() {
                @Override
                public void subscribe(ObservableEmitter<FictionModel> e) throws Exception {
                    FictionModel data = JsoupFictionContentManager.get().getData(url);
                    e.onNext(data);
                }
            }).subscribeOn(Schedulers.io())
                    .observeOn(AndroidSchedulers.mainThread())
                    .subscribe(new Consumer<FictionModel>() {
                        @Override
                        public void accept(FictionModel s) throws Exception {
                            EventBus.getDefault().postSticky(s);
                            startActivity(new Intent(activity, FictionReadActivity.class));
                        }
                    });

        });

    }

    /**
     * 处理业务逻辑，状态恢复等操作
     *
     * @param savedInstanceState
     */
    @Override
    protected void processLogic(Bundle savedInstanceState) {

    }


    @Override
    public void onDestroy() {
        FictionModel stickyEvent = EventBus.getDefault().getStickyEvent(FictionModel.class);
        if (stickyEvent != null) {
            EventBus.getDefault().removeStickyEvent(stickyEvent);
        }

        EventBus.getDefault().unregister(this);
        super.onDestroy();
    }


    @Subscribe(threadMode = ThreadMode.MAIN, sticky = true)
    public void ReceviceMessage(FictionModel event) {
        if (event != null) {

            String url = event.getDetailUrl();

            Observable.create((ObservableOnSubscribe<List<FictionModel>>) e -> {
                List<FictionModel> data = JsoupFictionChapterManager.get().getData(url);
                e.onNext(data);
            })
                    .subscribeOn(Schedulers.io())
                    .observeOn(AndroidSchedulers.mainThread())
                    .subscribe(fictionModels -> {
                        for (FictionModel fictionModel : fictionModels) {
                            if (fictionModel.getType() == 100) {
                                fictionDatas.add(fictionModel);
                            }

                            if (fictionModel.getType() == 99) {
                                mchapter_author_des.setText(fictionModel.getAuthor());
                                mchapter_title.setText(fictionModel.getTitle());

                                mchapter_decs.setText(fictionModel.getDesc());
                                mchapter_state_des.setText(fictionModel.getState().split(",")[0]);
                                mchapter_lastupdate_des.setText(fictionModel.getTime());
                                mchapter_latestchapter_des.setText(fictionModel.getLastChapter());

                                Glide.with(this)
                                        .load(fictionModel.getCoverImg().trim())
                                        .fitCenter()
                                        .into(mchapter_detail_title_img);
                            }
                        }


                        adapter.notifyDataSetChanged();

                    });


        }

    }


    class myAdapter extends BaseQuickAdapter<FictionModel, BaseViewHolder> {


        public myAdapter(int layoutResId, @Nullable List<FictionModel> data) {
            super(layoutResId, data);
        }

        @Override
        protected void convert(BaseViewHolder helper, FictionModel item) {
            helper.setText(R.id.chapter_item_des, item.getChapterName().trim());
        }
    }

}
