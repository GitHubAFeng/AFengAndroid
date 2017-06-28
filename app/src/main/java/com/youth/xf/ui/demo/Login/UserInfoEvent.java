package com.youth.xf.ui.demo.Login;


import java.io.Serializable;

/**
 * Created by Administrator on 2017/6/29.
 */

public class UserInfoEvent implements Serializable{

    private static final long serialVersionUID = 406016641791080595L;

    private String nickname;  // 昵称
    private String desc;   //签名
    private boolean isAvatarUpdate;
    private byte[] avatar;

    public UserInfoEvent(String nickname, String desc, boolean isAvatarUpdate, byte[] avatar) {
        this.nickname = nickname;
        this.desc = desc;
        this.isAvatarUpdate = isAvatarUpdate;
        this.avatar = avatar;
    }


    public byte[] getAvatar() {
        return avatar;
    }

    public void setAvatar(byte[] avatar) {
        this.avatar = avatar;
    }



    public boolean isAvatarUpdate() {
        return isAvatarUpdate;
    }

    public void setAvatarUpdate(boolean avatarUpdate) {
        isAvatarUpdate = avatarUpdate;
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
