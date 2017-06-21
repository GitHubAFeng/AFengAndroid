package com.youth.xf.ui.demo.meizi;

import java.io.Serializable;
import java.util.ArrayList;
import java.util.List;


/**
 * Created by Administrator on 2017/6/21.
 */

public class MeiZiCache implements Serializable {

    private static final long serialVersionUID = -6541682182260929071L;

    private List<String> imgUrlList = new ArrayList<>();

    public List<String> getImgUrlList() {
        return imgUrlList;
    }

    public void setImgUrlList(List<String> imgUrlList) {
        this.imgUrlList = imgUrlList;
    }

    public MeiZiCache(List<String> imgUrlList) {
        this.imgUrlList = imgUrlList;
    }
}
