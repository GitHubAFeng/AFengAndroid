package com.youth.xf.ui.demo.api;

import com.youth.xf.ui.demo.book.BookBean;

import java.util.List;
import java.util.concurrent.TimeUnit;

import io.reactivex.Observable;
import io.rx_cache2.DynamicKey;
import io.rx_cache2.EvictDynamicKey;
import io.rx_cache2.LifeCache;

/**
 * Created by Administrator on 2017/5/2.
 */

public interface BookCacheApi {
    @LifeCache(duration = 15, timeUnit = TimeUnit.MINUTES)
    Observable<List<BookBean>> getList(Observable<List<BookBean>> repo, DynamicKey key, EvictDynamicKey isUpdate);

}
