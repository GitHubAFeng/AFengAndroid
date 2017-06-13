package com.youth.xf.ui.demo.movie;

import android.os.Bundle;
import android.support.design.widget.CollapsingToolbarLayout;
import android.support.design.widget.TabLayout;
import android.support.v4.app.Fragment;
import android.support.v4.app.FragmentManager;
import android.support.v4.app.FragmentPagerAdapter;
import android.support.v4.view.ViewPager;
import android.support.v7.widget.Toolbar;
import android.view.View;
import android.widget.ImageView;

import com.bumptech.glide.Glide;
import com.youth.xf.R;
import com.youth.xf.base.AFengActivity;
import com.youth.xf.ui.demo.api.HttpClient;


import java.util.ArrayList;
import java.util.List;

import butterknife.BindView;
import io.reactivex.Observer;
import io.reactivex.android.schedulers.AndroidSchedulers;
import io.reactivex.disposables.Disposable;
import io.reactivex.schedulers.Schedulers;


/**
 * Created by Administrator on 2017/5/3.
 * 电影详情页
 */

public class MovieDetailActivity extends AFengActivity {

    @BindView(R.id.viewpager)
    ViewPager mViewPager;
    @BindView(R.id.toolbar)
    Toolbar toolbar;
    @BindView(R.id.collapsing_toolbar)
    CollapsingToolbarLayout collapsingToolbar;
    @BindView(R.id.ivImage)
    ImageView ivImage;
    @BindView(R.id.sliding_tabs)
    TabLayout tabLayout;

    SubjectsBean mMovie;

    MovieDetailActivity.MyPagerAdapter adapter = null;

    @Override
    protected int getLayoutId() {
        return R.layout.activity_moive_detail;
    }

    @Override
    protected void initView(Bundle savedInstanceState) {

        adapter = new MovieDetailActivity.MyPagerAdapter(getSupportFragmentManager());


        setSupportActionBar(toolbar);
        getSupportActionBar().setDisplayHomeAsUpEnabled(true);

        //在电影列表页传递该对象过来，根据ID再去请求详情
        mMovie = (SubjectsBean) getIntent().getSerializableExtra("movie");

        //设置顶部大图
        Glide.with(ivImage.getContext())
                .load(mMovie.getImages().getLarge())
                .fitCenter()
                .into(ivImage);

        setupViewPager(mViewPager);
        // 新建选项卡
        tabLayout.addTab(tabLayout.newTab().setText("剧情简介"));
        tabLayout.addTab(tabLayout.newTab().setText("导演简介"));
        tabLayout.addTab(tabLayout.newTab().setText("演员列表"));
        tabLayout.setupWithViewPager(mViewPager);

    }


    @Override
    protected void processLogic(Bundle savedInstanceState) {

    }

    @Override
    protected void setListener() {
        toolbar.setNavigationOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                onBackPressed();
            }
        });
    }

    private void setupViewPager(ViewPager mViewPager) {

        MovieRepository.getInstance().getMovieDetail(mMovie.getId(), mMovie.getId(), false)
                .subscribeOn(Schedulers.io())
                .unsubscribeOn(Schedulers.io())
                .observeOn(AndroidSchedulers.mainThread())
                .subscribe(new Observer<MovieDetailBean>() {
                    Disposable d;

                    @Override
                    public void onSubscribe(Disposable disposable) {
                        d = disposable;
                    }

                    @Override
                    public void onNext(MovieDetailBean movieDetailBean) {

                        StringBuilder NameBuilder = new StringBuilder();
                        for (PersonBean personBean : movieDetailBean.getDirectors()) {
                            NameBuilder.append("\n" + personBean.getName());
                        }

                        StringBuilder CastBuilder = new StringBuilder();
                        for (PersonBean personBean : movieDetailBean.getCasts()) {
                            CastBuilder.append("\n" + personBean.getName());
                        }

//                        adapter.addFragment(MovieDetailFragment.newInstance(movieDetailBean.getSummary()), "剧情简介");
//                        adapter.addFragment(MovieDetailFragment.newInstance(NameBuilder.toString()), "导演简介");
//                        adapter.addFragment(MovieDetailFragment.newInstance(CastBuilder.toString()), "演员列表");
                        adapter.notifyDataSetChanged();  //因为是异步请求，所以数据请求回来后要通知更新
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

 /*
* 在初始化ViewPager时，应先给Adapter初始化内容后再将该adapter传给ViewPager，
如果不这样处理，在更新adapter的内容后，应该调用一下adapter的notifyDataSetChanged方法，
否则在ADT22以上使用会报
The application’s PagerAdapter changed the adapter’s contents without
calling PagerAdapter#notifyDataSetChanged  的异常
*/
        mViewPager.setAdapter(adapter);
    }

    static class MyPagerAdapter extends FragmentPagerAdapter {
        private final List<Fragment> mFragments = new ArrayList<>();
        private final List<String> mFragmentTitles = new ArrayList<>();

        public MyPagerAdapter(FragmentManager fm) {
            super(fm);
        }

        public void addFragment(Fragment fragment, String title) {
            mFragments.add(fragment);
            mFragmentTitles.add(title);
        }

        @Override
        public Fragment getItem(int position) {
            return mFragments.get(position);
        }

        @Override
        public int getCount() {
            return mFragments.size();
        }

        @Override
        public CharSequence getPageTitle(int position) {
            return mFragmentTitles.get(position);
        }
    }


}
