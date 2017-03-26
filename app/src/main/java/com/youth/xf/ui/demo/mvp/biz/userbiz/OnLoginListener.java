package com.youth.xf.ui.demo.mvp.biz.userbiz;

import com.youth.xf.ui.demo.mvp.model.User;

/**
 * Created by AFeng on 2017/3/26.
 */

public interface OnLoginListener {
    void loginSuccess(User user);

    void loginFailed();

}
