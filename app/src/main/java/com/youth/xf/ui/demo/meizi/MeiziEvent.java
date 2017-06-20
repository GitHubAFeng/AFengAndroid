package com.youth.xf.ui.demo.meizi;

import com.youth.xf.ui.demo.bean.GankIoDataBean;

import java.util.List;

/**
 * Created by Administrator on 2017/6/19.
 */

public class MeiziEvent {

    private int index;  //第几张下标
    private List<String> imgData;  //图片数据

    public MeiziEvent(int index, List<String> imgData) {
        this.index = index;
        this.imgData = imgData;
    }

    public int getIndex() {
        return index;
    }

    public void setIndex(int index) {
        this.index = index;
    }


    public List<String> getImgData() {
        return imgData;
    }

    public void setImgData(List<String> imgData) {
        this.imgData = imgData;
    }
}
