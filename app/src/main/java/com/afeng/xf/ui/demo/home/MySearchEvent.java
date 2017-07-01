package com.afeng.xf.ui.demo.home;

/**
 * Created by Administrator on 2017/6/30.
 */

// 搜索事件
public class MySearchEvent {

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
