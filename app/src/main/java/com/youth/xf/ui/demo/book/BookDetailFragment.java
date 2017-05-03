package com.youth.xf.ui.demo.book;

import android.os.Bundle;
import android.widget.TextView;

import com.youth.xf.R;
import com.youth.xf.base.AFengFragment;

import butterknife.BindView;

/**
 * Created by Administrator on 2017/5/3.
 */

public class BookDetailFragment extends AFengFragment {

    @BindView(R.id.tvInfo)
    TextView tvInfo;

    public static BookDetailFragment newInstance(String info) {
        Bundle args = new Bundle();
        BookDetailFragment fragment = new BookDetailFragment();
        args.putString("info", info);
        fragment.setArguments(args);
        return fragment;
    }

    @Override
    protected int getLayoutId() {
        return R.layout.fragment_book_detail;
    }

    @Override
    protected void initView(Bundle savedInstanceState) {
        tvInfo.setText(getArguments().getString("info"));
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
}
