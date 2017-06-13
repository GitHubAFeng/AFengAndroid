package com.youth.xf.ui.demo.book;

import android.os.Bundle;
import android.support.design.widget.CollapsingToolbarLayout;
import android.support.design.widget.TabLayout;
import android.support.v4.app.FragmentStatePagerAdapter;
import android.support.v4.app.Fragment;
import android.support.v4.app.FragmentManager;

import android.support.v4.view.ViewPager;
import android.support.v7.widget.Toolbar;
import android.view.View;

import android.widget.ImageView;

import com.bumptech.glide.Glide;
import com.youth.xf.R;
import com.youth.xf.base.AFengActivity;

import java.util.ArrayList;
import java.util.List;

import butterknife.BindView;

import io.reactivex.Observer;
import io.reactivex.android.schedulers.AndroidSchedulers;
import io.reactivex.disposables.Disposable;
import io.reactivex.schedulers.Schedulers;


/**
 * Created by Administrator on 2017/5/3.
 */

public class BookDetailActivity extends AFengActivity {

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


    MyPagerAdapter adapter = null;

    BookDetailBean bookDetailBean;

    BooksBean mBook;

    @Override
    protected int getLayoutId() {
        return R.layout.activity_book_detail;
    }

    @Override
    protected void initView(Bundle savedInstanceState) {
        setSupportActionBar(toolbar);
        getSupportActionBar().setDisplayHomeAsUpEnabled(true);

        mBook = (BooksBean) getIntent().getSerializableExtra("book");

        Glide.with(ivImage.getContext())
                .load(mBook.getImages().getLarge())
                .fitCenter()
                .into(ivImage);

        setupViewPager(mViewPager);


        adapter = new MyPagerAdapter(getSupportFragmentManager());

        if(bookDetailBean!=null){
            adapter.addFragment(BookDetailFragment.newInstance(bookDetailBean.getSummary()), "内容简介");
            adapter.addFragment(BookDetailFragment.newInstance(bookDetailBean.getAuthor_intro()), "作者简介");
            adapter.addFragment(BookDetailFragment.newInstance(bookDetailBean.getCatalog()), "目录");
        }

        mViewPager.setAdapter(adapter);


        tabLayout.addTab(tabLayout.newTab().setText("内容简介"));
        tabLayout.addTab(tabLayout.newTab().setText("作者简介"));
        tabLayout.addTab(tabLayout.newTab().setText("目录"));
        tabLayout.setupWithViewPager(mViewPager);

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

    @Override
    protected void processLogic(Bundle savedInstanceState) {

    }


    private void setupViewPager(ViewPager mViewPager) {

        BookRepository.getInstance().getBookDetail(mBook.getId(), mBook.getId(), false)
                .subscribeOn(Schedulers.io())
                .unsubscribeOn(Schedulers.io())
                .observeOn(AndroidSchedulers.mainThread())
                .subscribe(new Observer<BookDetailBean>() {
                    Disposable d;

                    @Override
                    public void onSubscribe(Disposable disposable) {
                        d = disposable;
                    }

                    @Override
                    public void onNext(BookDetailBean bookDetailBeanReply) {
//                        adapter.clearAll();
                        bookDetailBean = bookDetailBeanReply;
                        adapter.notifyDataSetChanged();
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

    }


    static class MyPagerAdapter extends FragmentStatePagerAdapter {
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

        public void clearAll() {
            mFragmentTitles.clear();
            mFragments.clear();
        }

    }


}
