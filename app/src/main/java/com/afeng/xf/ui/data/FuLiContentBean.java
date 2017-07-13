package com.afeng.xf.ui.data;


import com.avos.avoscloud.AVClassName;
import com.avos.avoscloud.AVObject;

import java.io.Serializable;
import java.util.ArrayList;
import java.util.Date;

/**
 * Created by Administrator on 2017/7/11.
 */

@AVClassName("FuLiContentBean")
public class FuLiContentBean extends AVObject implements Serializable {
    private static final long serialVersionUID = 5095861543865608832L;

    private String userId;
    private ArrayList<String> imgUrlList;
    private String avaUrl;
    private String name;
    private Date time;
    private long watch;
    private String desc;

    public String getDesc() {
        return desc;
    }

    public FuLiContentBean(ArrayList<String> imgUrlList, String avaUrl, String name, Date time, long watch, String desc, String tag) {
        this.imgUrlList = imgUrlList;
        this.avaUrl = avaUrl;
        this.name = name;
        this.time = time;
        this.watch = watch;
        this.desc = desc;
        this.tag = tag;
    }

    public void setDesc(String desc) {
        this.desc = desc;
    }

    public String getUserId() {
        return userId;
    }

    public void setUserId(String userId) {
        this.userId = userId;
    }

    private String tag;  //标记是什么

    public ArrayList<String> getImgUrlList() {
        return imgUrlList;
    }

    public void setImgUrlList(ArrayList<String> imgUrlList) {
        this.imgUrlList = imgUrlList;
    }

    public String getAvaUrl() {
        return avaUrl;
    }

    public void setAvaUrl(String avaUrl) {
        this.avaUrl = avaUrl;
    }

    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }

    public Date getTime() {
        return time;
    }

    public void setTime(Date time) {
        this.time = time;
    }

    public long getWatch() {
        return watch;
    }

    public void setWatch(long watch) {
        this.watch = watch;
    }

    public String getTag() {
        return tag;
    }

    public void setTag(String tag) {
        this.tag = tag;
    }



    public FuLiContentBean() {
    }
}
