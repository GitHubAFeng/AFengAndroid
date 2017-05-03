package com.youth.xf.ui.demo.bean;

import java.io.Serializable;

/**
 * Created by Administrator on 2017/5/3.
 */

public class ImagesBean implements Serializable {
    private static final long serialVersionUID = 8079075637581309903L;
    /**
     * small : https://img3.doubanio.com/view/movie_poster_cover/ipst/public/p2378133884.jpg
     * large : https://img3.doubanio.com/view/movie_poster_cover/lpst/public/p2378133884.jpg
     * medium : https://img3.doubanio.com/view/movie_poster_cover/spst/public/p2378133884.jpg
     */

    private String small;

    private String large;

    private String medium;

    public String getSmall() {
        return small;
    }

    public String getLarge() {
        return large;
    }

    public String getMedium() {
        return medium;
    }

    public void setSmall(String small) {
        this.small = small;

    }

    public void setLarge(String large) {
        this.large = large;

    }

    public void setMedium(String medium) {
        this.medium = medium;
    }
}
