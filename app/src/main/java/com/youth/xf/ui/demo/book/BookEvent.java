package com.youth.xf.ui.demo.book;

/**
 * Created by Administrator on 2017/6/13.
 */

public class BookEvent {

    private BooksBean mBook;

    public BookEvent(BooksBean book) {
        mBook = book;
    }

    public BooksBean getbook() {
        return mBook;
    }

    public void setbook(BooksBean book) {
        mBook = book;
    }
}
