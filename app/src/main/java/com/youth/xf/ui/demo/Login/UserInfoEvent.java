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
    private String userName;
    private String userEmail;
    private String userPhone;


    public UserInfoEvent() {
    }

    public UserInfoEvent(String nickname, String desc, boolean isAvatarUpdate, byte[] avatar, String userName, String userEmail, String userPhone) {
        this.nickname = nickname;
        this.desc = desc;
        this.isAvatarUpdate = isAvatarUpdate;
        this.avatar = avatar;
        this.userName = userName;
        this.userEmail = userEmail;
        this.userPhone = userPhone;
    }

    public String getUserName() {
        return userName;
    }

    public void setUserName(String userName) {
        this.userName = userName;
    }

    public String getUserEmail() {
        return userEmail;
    }

    public void setUserEmail(String userEmail) {
        this.userEmail = userEmail;
    }

    public String getUserPhone() {
        return userPhone;
    }

    public void setUserPhone(String userPhone) {
        this.userPhone = userPhone;
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
