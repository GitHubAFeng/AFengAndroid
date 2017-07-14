package com.afeng.xf.ui.data;

import android.graphics.Bitmap;

import com.avos.avoscloud.AVClassName;
import com.avos.avoscloud.AVFile;
import com.avos.avoscloud.AVObject;

import java.io.Serializable;

/**
 * Created by Administrator on 2017/7/11.
 */
@AVClassName("FuLiHeadBean")
public class FuLiHeadBean extends AVObject implements Serializable {
    private static final long serialVersionUID = 8393746841070408553L;

    private AVFile avatar;  //头像
    private String imgUrl;
    private String name;
    private String tag;  //标记是什么

    public AVFile getAvatar() {
        return avatar;
    }

    public void setAvatar(AVFile avatar) {
        this.avatar = avatar;
    }

    public String getImgUrl() {
        return imgUrl;
    }

    public void setImgUrl(String imgUrl) {
        this.imgUrl = imgUrl;
    }

    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }

    public String getTag() {
        return tag;
    }

    public void setTag(String tag) {
        this.tag = tag;
    }
}
