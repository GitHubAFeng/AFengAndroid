package com.youth.xf.ui.data;

import com.avos.avoscloud.AVClassName;
import com.avos.avoscloud.AVObject;

/**
 * Created by Administrator on 2017/6/26.
 * 引导图
 */

@AVClassName("SplashBannerItem")
public class SplashBannerItem  extends AVObject {

    private String img;  //图片URL
    private int isShow;  //是否显示   0 否  , 1 是
    private int isBack;  //是否为背景   0 否  , 1 是
    private String desc;  //描述

    public String getImg() {
        return img;
    }

    public void setImg(String img) {
        this.img = img;
    }

    public int getIsShow() {
        return isShow;
    }

    public void setIsShow(int isShow) {
        this.isShow = isShow;
    }

    public int getIsBack() {
        return isBack;
    }

    public void setIsBack(int isBack) {
        this.isBack = isBack;
    }

    public String getDesc() {
        return desc;
    }

    public void setDesc(String desc) {
        this.desc = desc;
    }
}
