package com.youth.xf.ui.demo.mvp.biz.userbiz;

/**
 * Created by AFeng on 2017/3/26.
 */

public interface IUserBiz {
    public void login(String username, String password, OnLoginListener loginListener);
}
