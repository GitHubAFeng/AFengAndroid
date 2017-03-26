package com.youth.xf.ui.demo.mvp.view;

import com.youth.xf.ui.demo.mvp.model.User;

/**
 * Created by AFeng on 2017/3/26.
 */

public interface IUserLoginView {

    String getUserName();

    String getPassword();

    void clearUserName();

    void clearPassword();

    void showLoading();

    void hideLoading();

    void toMainActivity(User user);

    void showFailedError();
}
