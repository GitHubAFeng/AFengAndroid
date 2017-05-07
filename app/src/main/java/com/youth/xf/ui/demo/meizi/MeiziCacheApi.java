package com.youth.xf.ui.demo.meizi;

import com.youth.xf.ui.demo.bean.GankIoDataBean;

import java.util.concurrent.TimeUnit;

import io.reactivex.Observable;
import io.rx_cache2.DynamicKey;
import io.rx_cache2.EvictDynamicKey;
import io.rx_cache2.LifeCache;
import io.rx_cache2.Reply;

/**
 * Created by Administrator on 2017/5/7.
 */

public interface MeiziCacheApi {

    @LifeCache(duration = 15, timeUnit = TimeUnit.MINUTES)
    Observable<Reply<GankIoDataBean>> getGankIoData(Observable<GankIoDataBean> GankIoData, DynamicKey key, EvictDynamicKey isUpdate);


}
