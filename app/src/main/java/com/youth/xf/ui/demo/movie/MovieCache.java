package com.youth.xf.ui.demo.movie;

import java.util.concurrent.TimeUnit;

import io.reactivex.Observable;
import io.rx_cache2.DynamicKey;
import io.rx_cache2.EvictDynamicKey;
import io.rx_cache2.LifeCache;
import io.rx_cache2.Reply;

/**
 * Created by Administrator on 2017/5/7.
 */

public interface MovieCache {

    //这里设置缓存失效时间为10分钟。
    @LifeCache(duration = 10, timeUnit = TimeUnit.MINUTES)
    Observable<Reply<HotMovieBean>> getMovieTop250(Observable<HotMovieBean> HotMovie, DynamicKey HotMovieId, EvictDynamicKey evictDynamicKey);


    @LifeCache(duration = 10, timeUnit = TimeUnit.MINUTES)
    Observable<Reply<MovieDetailBean>> getMovieDetail(Observable<MovieDetailBean> MovieDetail, DynamicKey key, EvictDynamicKey evictDynamicKey);


}
