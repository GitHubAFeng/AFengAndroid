package com.afeng.xf.ui.fuli;


import java.io.Serializable;
import java.util.Date;
import java.util.List;

/**
 * Created by Administrator on 2017/7/11.
 */

public class FuLiContentBean implements Serializable {
    private static final long serialVersionUID = 5095861543865608832L;

    private List<String> imgUrlList;
    private String avaUrl;
    private String name;
    private Date time;
    private long watch;
    private String desc;

    public String getDesc() {
        return desc;
    }

    public FuLiContentBean(List<String> imgUrlList, String avaUrl, String name, Date time, long watch, String desc, String tag) {
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

    private String tag;  //标记是什么

    public List<String> getImgUrlList() {
        return imgUrlList;
    }

    public void setImgUrlList(List<String> imgUrlList) {
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
