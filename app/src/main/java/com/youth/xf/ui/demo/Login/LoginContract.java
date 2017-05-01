package com.youth.xf.ui.demo.Login;

import com.youth.xf.base.mvp.BaseModelCallback;
import com.youth.xf.base.mvp.BasePresenter;
import com.youth.xf.base.mvp.BaseView;


/**
 * Created by Administrator on 2017/5/1.
 */

public interface LoginContract {

    interface View extends BaseView {
        String getUserName();

        String getPassword();

        void clearUserName();

        void clearPassword();

        void showLoading();

        void hideLoading();

        void toMainActivity(User user);

        void showFailedError();
    }

    interface Presenter extends BasePresenter {
        void login(String username, String password, BaseModelCallback loginListener);
    }

    interface DataSource {

    }

}
