package com.youth.xf.ui.demo.book;

import com.youth.xf.base.mvp.BaseModelCallback;
import com.youth.xf.base.mvp.BasePresenter;
import com.youth.xf.base.mvp.BaseView;

/**
 * Created by Administrator on 2017/4/27.
 */

public interface BookContract {
    interface View extends BaseView{
        void setUpFAB(View view);
        void startFABAnimation();
    }

    interface Presenter extends BasePresenter{
        void doSearch(String keyword);
        void setUpRepository();
    }

    interface DataSource {
        void getBooks(BaseModelCallback listener);
    }


}
