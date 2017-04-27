package com.youth.xf.ui.demo.book;

import android.os.Bundle;
import android.support.v4.view.ViewPager;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

import com.flyco.tablayout.SlidingTabLayout;
import com.youth.xf.R;
import com.youth.xf.base.AFengFragment;

import butterknife.BindView;
import butterknife.ButterKnife;
import butterknife.Unbinder;

/**
 * Created by AFeng on 2017/4/2.
 */

public class BookFragment extends AFengFragment implements BookContract.View {


    @BindView(R.id.book_tab)
    SlidingTabLayout mBookTab;
    @BindView(R.id.book_vp)
    ViewPager mBookVp;

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

    }

    @Override
    protected void onVisible() {

    }

    @Override
    protected void onInvisible() {

    }


    @Override
    public void setPresenter(Object presenter) {

    }

    @Override
    public void setUpFAB(BookContract.View view) {

    }

    @Override
    public void startFABAnimation() {

    }

    @Override
    public boolean isActive() {
        return false;
    }
}
