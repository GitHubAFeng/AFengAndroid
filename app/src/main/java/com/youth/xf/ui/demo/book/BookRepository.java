package com.youth.xf.ui.demo.book;

import com.youth.xf.base.mvp.BaseModelCallback;
import com.youth.xf.ui.demo.api.BookCacheApi;
import com.youth.xf.ui.demo.api.BookRemoteApi;
import com.youth.xf.utils.okhttp.UnsafeOkHttpUtils;

import java.io.File;
import java.util.Arrays;

import io.rx_cache2.DynamicKey;
import io.rx_cache2.EvictDynamicKey;
import io.rx_cache2.internal.RxCache;
import io.victoralbertos.jolyglot.GsonSpeaker;
import retrofit2.Retrofit;
import retrofit2.adapter.rxjava2.RxJava2CallAdapterFactory;
import retrofit2.converter.gson.GsonConverterFactory;

/**
 * Created by Administrator on 2017/4/28.
 */

public class BookRepository implements BookContract.DataSource {
    private static BookRepository INSTANCE = null;

    public static final String URL_BASE = "http://gank.io/api/data/";
    private BookRemoteApi remoteApi;
    private BookCacheApi cacheApi;

    private BookRepository(File cacheDir) {
        cacheApi = new RxCache.Builder()
                .useExpiredDataIfLoaderNotAvailable(true)
                .persistence(cacheDir, new GsonSpeaker())
                .using(BookCacheApi.class);

        remoteApi = new Retrofit.Builder()
                .baseUrl(URL_BASE)
                .addCallAdapterFactory(RxJava2CallAdapterFactory.create())
                .addConverterFactory(GsonConverterFactory.create())
                .client(UnsafeOkHttpUtils.getClient())
                .build()
                .create(BookRemoteApi.class);
    }

    public static BookRepository getInstance(File cacheDir) {
        if (INSTANCE == null) {
            INSTANCE = new BookRepository(cacheDir);
        }
        return INSTANCE;
    }

    public static void destroyInstance() {
        INSTANCE = null;
    }



    @Override
    public void getBooks(BaseModelCallback listener) {

    }


    private String generateKey(int status, String[] keywords) {
        StringBuilder keyBuilder = new StringBuilder();

        keyBuilder.append("&status=" + status);
        if (null != keywords && keywords.length > 0) {
            Arrays.sort(keywords);
            for (String keyword : keywords) {
                keyBuilder.append("&keyword=" + keyword);
            }
        }
        return keyBuilder.toString();
    }


}
