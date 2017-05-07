package com.youth.xf.ui.demo.book;

import java.util.concurrent.TimeUnit;

import io.reactivex.Observable;
import io.rx_cache2.DynamicKey;
import io.rx_cache2.EvictDynamicKey;
import io.rx_cache2.LifeCache;
import io.rx_cache2.Reply;

/**
 * Created by Administrator on 2017/5/2.
 */

public interface BookCacheApi {

    @LifeCache(duration = 15, timeUnit = TimeUnit.MINUTES)
    Observable<Reply<BookBean>> getBook(Observable<BookBean> Book, DynamicKey key, EvictDynamicKey isUpdate);

    @LifeCache(duration = 15, timeUnit = TimeUnit.MINUTES)
    Observable<Reply<BookDetailBean>> getBookDetail(Observable<BookDetailBean> BookDetail, DynamicKey key, EvictDynamicKey isUpdate);

}
