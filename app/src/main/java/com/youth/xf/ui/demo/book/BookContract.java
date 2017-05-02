package com.youth.xf.ui.demo.book;

import com.youth.xf.base.mvp.BaseModelCallback;
import com.youth.xf.base.mvp.BasePresenter;
import com.youth.xf.base.mvp.BaseView;

import java.util.List;

/**
 * Created by Administrator on 2017/4/27.
 */

public interface BookContract {
    interface View extends BaseView<Presenter>{
        void setUpFAB();
        void startFABAnimation();
        void setUpRecyclerView();
    }

    interface Presenter extends BasePresenter{
        void doSearch(String keyword);
        void setUpRepository();
    }

    interface DataSource {
        void getBooks(BaseModelCallback listener);
    }


}
