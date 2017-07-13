package com.afeng.xf.ui.contribute;

import java.io.Serializable;

/**
 * Created by Administrator on 2017/7/13.
 */

public class FuliContributeImgBean implements Serializable {
    private static final long serialVersionUID = -4563014645658795816L;


    private String imgUrl;

    public FuliContributeImgBean() {
    }

    public String getImgUrl() {
        return imgUrl;
    }

    public void setImgUrl(String imgUrl) {
        this.imgUrl = imgUrl;
    }

    public FuliContributeImgBean(String imgUrl) {
        this.imgUrl = imgUrl;
    }

}
