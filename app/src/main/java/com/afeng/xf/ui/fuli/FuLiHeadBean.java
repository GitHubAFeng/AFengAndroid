package com.afeng.xf.ui.fuli;

import android.graphics.Bitmap;
import android.graphics.drawable.Drawable;

import java.io.Serializable;

/**
 * Created by Administrator on 2017/7/11.
 */

public class FuLiHeadBean implements Serializable {
    private static final long serialVersionUID = 8393746841070408553L;

    private String imgUrl;
    private Drawable imgRes;
    private String name;
    private String tag;  //标记是什么

    public FuLiHeadBean(String imgUrl, Drawable imgRes, String name, String tag) {
        this.imgUrl = imgUrl;
        this.imgRes = imgRes;
        this.name = name;
        this.tag = tag;
    }

    public String getImgUrl() {
        return imgUrl;
    }

    public void setImgUrl(String imgUrl) {
        this.imgUrl = imgUrl;
    }

    public Drawable getImgRes() {
        return imgRes;
    }

    public void setImgRes(Drawable imgRes) {
        this.imgRes = imgRes;
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
