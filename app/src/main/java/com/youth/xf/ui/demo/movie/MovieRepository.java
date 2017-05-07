package com.youth.xf.ui.demo.movie;

import com.youth.xf.App;
import com.youth.xf.ui.demo.api.HttpClient;

import io.reactivex.Observable;
import io.rx_cache2.DynamicKey;
import io.rx_cache2.EvictDynamicKey;
import io.rx_cache2.Reply;
import io.rx_cache2.internal.RxCache;
import io.victoralbertos.jolyglot.GsonSpeaker;


/**
 * Created by Administrator on 2017/5/7.
 */

public class MovieRepository {


    private HttpClient RemoteApi;
    private MovieCache CacheApi;

    private static MovieRepository instance;

    private MovieRepository() {
        RemoteApi = HttpClient.Builder.getDouBanService();
        CacheApi = new RxCache.Builder().persistence(App.getInstance().getCacheDir(), new GsonSpeaker()).using(MovieCache.class);
    }

    public static MovieRepository getInstance() {
        if (instance == null) {
            synchronized (MovieRepository.class) {
                if (instance == null) {
                    instance = new MovieRepository();
                }
            }
        }
        return instance;
    }


    /**
     * @param update 是否更新,如果设置为true，缓存数据将被清理，并且向服务器请求数据
     * @return
     */
    public Observable<Reply<HotMovieBean>> getMovieTop250(int start, int count, int HotMovieId, final boolean update) {
        //这里设置HotMovieId为DynamicKey,如果update为true,将会重新获取数据并清理缓存。
        return CacheApi.getMovieTop250(RemoteApi.getMovieTop250(start, count), new DynamicKey(HotMovieId), new EvictDynamicKey(update));
    }


    public Observable<Reply<MovieDetailBean>> getMovieDetail(String id, String key, final boolean update) {
        //这里设置HotMovieId为DynamicKey,如果update为true,将会重新获取数据并清理缓存。
        return CacheApi.getMovieDetail(RemoteApi.getMovieDetail(id), new DynamicKey(key), new EvictDynamicKey(update));
    }


}
