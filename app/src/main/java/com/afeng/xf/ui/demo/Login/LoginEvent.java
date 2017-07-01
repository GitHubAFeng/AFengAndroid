package com.afeng.xf.ui.demo.Login;

/**
 * Created by Administrator on 2017/6/28.
 */

public class LoginEvent {

    private String userName;
    private String passWord;
    private boolean isLogin;

    public LoginEvent(String userName, String passWord, boolean isLogin) {
        this.userName = userName;
        this.passWord = passWord;
        this.isLogin = isLogin;
    }

    public String getUserName() {
        return userName;
    }

    public void setUserName(String userName) {
        this.userName = userName;
    }

    public String getPassWord() {
        return passWord;
    }

    public void setPassWord(String passWord) {
        this.passWord = passWord;
    }

    public boolean isLogin() {
        return isLogin;
    }

    public void setLogin(boolean login) {
        isLogin = login;
    }
}
