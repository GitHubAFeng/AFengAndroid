package com.afeng.xf.ui.data;

import com.avos.avoscloud.AVClassName;
import com.avos.avoscloud.AVFile;
import com.avos.avoscloud.AVObject;

/**
 * Created by Administrator on 2017/6/27.
 */

@AVClassName("UserInfo")
public class UserInfo extends AVObject {

    private String nickname;  // 昵称
    private String desc;   //签名
    private AVFile avatar;  //头像


    public AVFile getAvatar() {
        return avatar;
    }

    public void setAvatar(AVFile avatar) {
        this.avatar = avatar;
    }


    public String getNickname() {
        return nickname;
    }

    public void setNickname(String nickname) {
        this.nickname = nickname;
    }

    public String getDesc() {
        return desc;
    }

    public void setDesc(String desc) {
        this.desc = desc;
    }
}
