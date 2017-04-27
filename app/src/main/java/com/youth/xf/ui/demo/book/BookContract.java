package com.youth.xf.ui.demo.book;

import com.youth.xf.ui.demo.mvp.basemvp.BasePresenter;
import com.youth.xf.ui.demo.mvp.basemvp.BaseView;

/**
 * Created by Administrator on 2017/4/27.
 */

public interface BookContract {
    interface View extends BaseView{
        void setUpFAB(View view);
        void startFABAnimation();
        boolean isActive();
    }

    interface Presenter extends BasePresenter{
        void doSearch(String keyword);
        void setUpRepository();
    }


}
