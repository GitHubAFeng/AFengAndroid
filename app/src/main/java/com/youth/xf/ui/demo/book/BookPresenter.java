package com.youth.xf.ui.demo.book;

/**
 * Created by Administrator on 2017/4/27.
 */

public class BookPresenter implements BookContract.Presenter {

    private BookRepository mBookRepository;
    private BookContract.View mBookView;

    private String mTaskId;

    public BookPresenter(String taskId, BookRepository bookRepository, BookContract.View bookView) {
        mTaskId = taskId;
        mBookRepository = bookRepository;
        mBookView = bookView;

        mBookView.setPresenter(this);
    }

    @Override
    public void start() {

    }


    @Override
    public void doSearch(String keyword) {

    }

    @Override
    public void setUpRepository() {

    }
}
