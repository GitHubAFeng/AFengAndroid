package com.youth.xf.ui.demo.book;

import java.util.ArrayList;

/**
 * Created by Administrator on 2017/4/28.
 */

public class BookResponseJson {
    public int count;
    public int start;
    public int total;
    public ArrayList<BookBean> books;

    public int getCount() {
        return count;
    }

    public int getStart() {
        return start;
    }

    public int getTotal() {
        return total;
    }

    public ArrayList<BookBean> getBooks() {
        return books;
    }

    public void setCount(int count) {
        this.count = count;
    }

    public void setStart(int start) {
        this.start = start;
    }

    public void setTotal(int total) {
        this.total = total;
    }

    public void setBooks(ArrayList<BookBean> books) {
        this.books = books;
    }

}
