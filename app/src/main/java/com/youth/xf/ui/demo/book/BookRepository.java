package com.youth.xf.ui.demo.book;

import com.youth.xf.base.mvp.BaseModelCallback;

/**
 * Created by Administrator on 2017/4/28.
 */

public class BookRepository implements BookContract.DataSource {
    private static BookRepository INSTANCE = null;
    public String key;

    public void setKey(String key) {
        this.key = key;
    }

    private BookRepository() {
    }

    public static BookRepository getInstance() {
        if (INSTANCE == null) {
            INSTANCE = new BookRepository();
        }
        return INSTANCE;
    }

    public static void destroyInstance() {
        INSTANCE = null;
    }

    private static final String BASE_URL = "http://gank.io/api/data/Android/10/1";

    private static String getAbsoluteUrl(String relativeUrl) {
        return BASE_URL + relativeUrl;
    }

    @Override
    public void getBooks(BaseModelCallback listener) {

    }




}
