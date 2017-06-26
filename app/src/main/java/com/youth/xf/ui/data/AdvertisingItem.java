package com.youth.xf.ui.data;

import com.avos.avoscloud.AVClassName;
import com.avos.avoscloud.AVObject;

/**
 * Created by Administrator on 2017/6/26.
 * 广告图
 */

@AVClassName("AdvertisingItem")
public class AdvertisingItem extends AVObject {

    private String img;  //图片URL
    private int isShow;  //是否显示   0 否  , 1 是
    private int isAdv;  //是否为广告   0 否  , 1 是
    private int advType;  //广告性质, 0 APP启动图
    private String desc;  //描述
    private String url;  //点击跳转的URL

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

    public int getIsAdv() {
        return isAdv;
    }

    public void setIsAdv(int isAdv) {
        this.isAdv = isAdv;
    }

    public int getAdvType() {
        return advType;
    }

    public void setAdvType(int advType) {
        this.advType = advType;
    }

    public String getDesc() {
        return desc;
    }

    public void setDesc(String desc) {
        this.desc = desc;
    }

    public String getUrl() {
        return url;
    }

    public void setUrl(String url) {
        this.url = url;
    }
}
