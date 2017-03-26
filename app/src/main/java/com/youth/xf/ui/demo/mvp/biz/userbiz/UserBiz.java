package com.youth.xf.ui.demo.mvp.biz.userbiz;

import com.youth.xf.ui.demo.mvp.model.User;

/**
 * Created by AFeng on 2017/3/26.
 */

public class UserBiz implements IUserBiz{
    @Override
    public void login(String username, String password, OnLoginListener loginListener) {
        //模拟子线程耗时操作
        new Thread()
        {
            @Override
            public void run()
            {
                try
                {
                    Thread.sleep(2000);
                } catch (InterruptedException e)
                {
                    e.printStackTrace();
                }
                //模拟登录成功
                if ("zhy".equals(username) && "123".equals(password))
                {
                    User user = new User();
                    user.setUsername(username);
                    user.setPassword(password);
                    loginListener.loginSuccess(user);
                } else
                {
                    loginListener.loginFailed();
                }
            }
        }.start();
    }

}
