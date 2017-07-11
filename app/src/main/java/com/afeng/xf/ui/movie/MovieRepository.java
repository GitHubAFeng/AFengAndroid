package com.afeng.xf.ui.movie;

import com.afeng.xf.ui.api.HttpClient;

import io.reactivex.Observable;


/**
 * Created by Administrator on 2017/5/7.
 */

public class MovieRepository {


    private HttpClient RemoteApi;


    private static MovieRepository instance;

    private MovieRepository() {
        RemoteApi = HttpClient.Builder.getDouBanService();
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
    public Observable<HotMovieBean> getMovieTop250(int start, int count, int HotMovieId, final boolean update) {
        //这里设置HotMovieId为DynamicKey,如果update为true,将会重新获取数据并清理缓存。
        return RemoteApi.getMovieTop250(start, count);
    }


    public Observable<MovieDetailBean> getMovieDetail(String id, String key, final boolean update) {
        //这里设置HotMovieId为DynamicKey,如果update为true,将会重新获取数据并清理缓存。
        return RemoteApi.getMovieDetail(id);
    }


}
