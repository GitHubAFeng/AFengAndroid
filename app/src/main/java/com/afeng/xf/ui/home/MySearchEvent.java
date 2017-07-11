package com.afeng.xf.ui.home;

import java.io.Serializable;

/**
 * Created by Administrator on 2017/6/30.
 */

// 搜索事件
public class MySearchEvent implements Serializable {

    private static final long serialVersionUID = -8069666200010254632L;

    public String getSearchDesc() {
        return SearchDesc;
    }

    public MySearchEvent(String searchDesc) {
        SearchDesc = searchDesc;
    }

    public void setSearchDesc(String searchDesc) {
        SearchDesc = searchDesc;
    }

    private String SearchDesc;


}
