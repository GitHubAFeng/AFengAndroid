package com.youth.xf.ui.demo.mvp.presenter;

import android.os.Handler;

import com.youth.xf.ui.demo.mvp.biz.userbiz.IUserBiz;
import com.youth.xf.ui.demo.mvp.biz.userbiz.OnLoginListener;
import com.youth.xf.ui.demo.mvp.biz.userbiz.UserBiz;
import com.youth.xf.ui.demo.mvp.model.User;
import com.youth.xf.ui.demo.mvp.view.IUserLoginView;



/**
 * Created by AFeng on 2017/3/26.
 */

public class UserLoginPresenter {

    private IUserBiz userBiz;
    private IUserLoginView userLoginView;
    private Handler mHandler = new Handler();

    public UserLoginPresenter(IUserLoginView userLoginView)
    {
        this.userLoginView = userLoginView;
        this.userBiz = new UserBiz();
    }

    public void login()
    {
        userLoginView.showLoading();
        userBiz.login(userLoginView.getUserName(), userLoginView.getPassword(), new OnLoginListener()
        {
            @Override
            public void loginSuccess(final User user)
            {
                //需要在UI线程执行
                mHandler.post(new Runnable()
                {
                    @Override
                    public void run()
                    {
                        userLoginView.toMainActivity(user);
                        userLoginView.hideLoading();
                    }
                });

            }

            @Override
            public void loginFailed()
            {
                //需要在UI线程执行
                mHandler.post(new Runnable()
                {
                    @Override
                    public void run()
                    {
                        userLoginView.showFailedError();
                        userLoginView.hideLoading();
                    }
                });

            }
        });
    }

    public void clear()
    {
        userLoginView.clearUserName();
        userLoginView.clearPassword();
    }



}
