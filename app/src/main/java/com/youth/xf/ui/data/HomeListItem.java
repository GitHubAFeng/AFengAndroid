package com.youth.xf.ui.data;

import cn.bmob.v3.BmobObject;

/**
 * Created by Administrator on 2017/6/25.
 * 首页推荐列表
 */

public class HomeListItem extends BmobObject {

    private String title;   //题目
    private String img;   //图片地址
    private Integer watchCount;  //点击次数
    private String desc;  //描述
    private String url;  //播放链接


    public String getUrl() {
        return url;
    }

    public void setUrl(String url) {
        this.url = url;
    }


    public String getTitle() {
        return title;
    }

    public void setTitle(String title) {
        this.title = title;
    }

    public String getImg() {
        return img;
    }

    public void setImg(String img) {
        this.img = img;
    }

    public Integer getWatchCount() {
        return watchCount;
    }

    public void setWatchCount(Integer watchCount) {
        this.watchCount = watchCount;
    }

    public String getDesc() {
        return desc;
    }

    public void setDesc(String desc) {
        this.desc = desc;
    }
}
