package com.youth.xf.ui.demo.book;

import com.youth.xf.App;

import com.youth.xf.ui.demo.api.HttpClient;

import io.reactivex.Observable;

import io.rx_cache2.DynamicKey;
import io.rx_cache2.EvictDynamicKey;
import io.rx_cache2.Reply;
import io.rx_cache2.internal.RxCache;
import io.victoralbertos.jolyglot.GsonSpeaker;


/**
 * Created by Administrator on 2017/4/28.
 */

public class BookRepository {

    private HttpClient RemoteApi;
    private BookCacheApi CacheApi;

    private static BookRepository instance;

    private BookRepository() {
        RemoteApi = HttpClient.Builder.getDouBanService();
        CacheApi = new RxCache.Builder().persistence(App.getInstance().getCacheDir(), new GsonSpeaker()).using(BookCacheApi.class);
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
    public Observable<Reply<BookBean>> getBook(String tag, int start, int count, int key, final boolean update) {
        //这里设置HotMovieId为DynamicKey,如果update为true,将会重新获取数据并清理缓存。
        return CacheApi.getBook(RemoteApi.getBook(tag, start, count), new DynamicKey(key), new EvictDynamicKey(update));
    }


    public Observable<Reply<BookDetailBean>> getBookDetail(String bookid, int key, final boolean update) {
        //这里设置HotMovieId为DynamicKey,如果update为true,将会重新获取数据并清理缓存。
        return CacheApi.getBookDetail(RemoteApi.getBookDetail(bookid), new DynamicKey(key), new EvictDynamicKey(update));
    }


}
