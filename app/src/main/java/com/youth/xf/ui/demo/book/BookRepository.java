package com.youth.xf.ui.demo.book;

import com.youth.xf.ui.demo.mvp.basemvp.BaseModelCallback;
import com.youth.xf.ui.demo.mvp.basemvp.RequestBiz;

/**
 * Created by Administrator on 2017/4/28.
 */

public class BookRepository implements RequestBiz {
    private static BookRepository INSTANCE;
    public String key;

    public void setKey(String key) {
        this.key = key;
    }

    private BookRepository() {}

    public static BookRepository getInstance() {
        if (INSTANCE == null) {
            INSTANCE = new BookRepository();
        }
        return INSTANCE;
    }

    private static final String BASE_URL = "https://api.douban.com/v2/";

    private static String getAbsoluteUrl(String relativeUrl) {
        return BASE_URL + relativeUrl;
    }

    @Override
    public void requestForData(BaseModelCallback listener) {




    }
}
