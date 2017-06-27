package com.youth.xf.ui.data;

import com.avos.avoscloud.AVClassName;
import com.avos.avoscloud.AVObject;

/**
 * Created by Administrator on 2017/6/26.
 */

@AVClassName("HomeBannerItem")
public class HomeBannerItem extends AVObject {


    private String img;   //图片地址
    private int isAdv;  //是否广告   0 否  , 1 是
    private int isShow;  //是否显示   0 否  , 1 是
    private String desc;  //描述
    private String url;  //播放链接

    private String jsCode;  //插入JS

    public int getIsAdv() {
        return isAdv;
    }

    public void setIsAdv(int isAdv) {
        this.isAdv = isAdv;
    }

    public String getJsCode() {
        return jsCode;
    }

    public void setJsCode(String jsCode) {
        this.jsCode = jsCode;
    }

    public String getImg() {
        return img;
    }

    public void setImg(String img) {
        this.img = img;
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

    public int getIsShow() {
        return isShow;
    }

    public void setIsShow(int isShow) {
        this.isShow = isShow;
    }
}
