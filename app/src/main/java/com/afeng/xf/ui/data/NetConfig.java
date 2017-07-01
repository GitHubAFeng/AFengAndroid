package com.afeng.xf.ui.data;

import com.avos.avoscloud.AVClassName;
import com.avos.avoscloud.AVObject;

/**
 * Created by Administrator on 2017/6/30.
 * 网络控制单元
 */

@AVClassName("NetConfig")
public class NetConfig extends AVObject {

    private boolean isShowSplash;   //是否显示启动引导图
    private String desc;    //行性质描述

    public boolean isShowSplash() {
        return isShowSplash;
    }

    public void setShowSplash(boolean showSplash) {
        isShowSplash = showSplash;
    }

    public String getDesc() {
        return desc;
    }

    public void setDesc(String desc) {
        this.desc = desc;
    }
}
