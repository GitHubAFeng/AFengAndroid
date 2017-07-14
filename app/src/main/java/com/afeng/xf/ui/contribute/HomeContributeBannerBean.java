package com.afeng.xf.ui.contribute;

import java.io.Serializable;

/**
 * Created by Administrator on 2017/7/14.
 */

public class HomeContributeBannerBean  implements Serializable {
    private static final long serialVersionUID = 206940028816978758L;

    private String objectId;
    private String img;   //图片地址
    private int isAdv;  //是否广告   0 否  , 1 是
    private int isShow;  //是否显示   0 否  , 1 是
    private String desc;  //描述
    private String url;  //播放链接
    private String jsCode;  //插入JS

    public String getObjectId() {
        return objectId;
    }

    public void setObjectId(String objectId) {
        this.objectId = objectId;
    }

    public String getImg() {
        return img;
    }

    public void setImg(String img) {
        this.img = img;
    }

    public int getIsAdv() {
        return isAdv;
    }

    public void setIsAdv(int isAdv) {
        this.isAdv = isAdv;
    }

    public int getIsShow() {
        return isShow;
    }

    public void setIsShow(int isShow) {
        this.isShow = isShow;
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

    public String getJsCode() {
        return jsCode;
    }

    public void setJsCode(String jsCode) {
        this.jsCode = jsCode;
    }
}
