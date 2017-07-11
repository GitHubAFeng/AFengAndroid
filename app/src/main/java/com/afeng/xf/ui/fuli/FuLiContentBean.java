package com.afeng.xf.ui.fuli;

import java.io.Serializable;

/**
 * Created by Administrator on 2017/7/11.
 */

public class FuLiContentBean implements Serializable {
    private static final long serialVersionUID = 5095861543865608832L;

    private String img;
    private String name;
    private String time;
    private String watch;

    public String getImg() {
        return img;
    }

    public void setImg(String img) {
        this.img = img;
    }

    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }

    public String getTime() {
        return time;
    }

    public void setTime(String time) {
        this.time = time;
    }

    public String getWatch() {
        return watch;
    }

    public void setWatch(String watch) {
        this.watch = watch;
    }

    public FuLiContentBean() {
    }

    public FuLiContentBean(String img, String name, String time, String watch) {
        this.img = img;
        this.name = name;
        this.time = time;
        this.watch = watch;
    }
}
