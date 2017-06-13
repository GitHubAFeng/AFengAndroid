package com.youth.xf.ui.demo.book;

import com.youth.xf.ui.demo.api.HttpClient;

import io.reactivex.Observable;


/**
 * Created by Administrator on 2017/4/28.
 */

public class BookRepository {

    private HttpClient RemoteApi;

    private static BookRepository instance;

    private BookRepository() {
        RemoteApi = HttpClient.Builder.getDouBanService();
    }

    public static BookRepository getInstance() {
        if (instance == null) {
            synchronized (BookRepository.class) {
                if (instance == null) {
                    instance = new BookRepository();
                }
            }
        }
        return instance;
    }


    /**
     * @param update 是否更新,如果设置为true，缓存数据将被清理，并且向服务器请求数据
     * @return
     */
    public Observable<BookBean> getBook(String tag, int start, int count, int key, final boolean update) {
        //这里设置HotMovieId为DynamicKey,如果update为true,将会重新获取数据并清理缓存。
        return RemoteApi.getBook(tag,start,count);
    }


    public Observable<BookDetailBean> getBookDetail(String bookid, String key, final boolean update) {
        //这里设置HotMovieId为DynamicKey,如果update为true,将会重新获取数据并清理缓存。
        return RemoteApi.getBookDetail(bookid);
    }


}
