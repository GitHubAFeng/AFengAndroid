package com.youth.xf.ui.demo.book;

import com.youth.xf.App;
import com.youth.xf.base.mvp.BaseModelCallback;
import com.youth.xf.ui.demo.api.BookCacheApi;
import com.youth.xf.ui.demo.api.BookRemoteApi;
import com.youth.xf.utils.okhttp.UnsafeOkHttpUtils;

import java.io.File;
import java.util.Arrays;
import java.util.List;
import java.util.concurrent.TimeUnit;

import io.reactivex.Observer;
import io.reactivex.android.schedulers.AndroidSchedulers;
import io.reactivex.functions.Consumer;
import io.reactivex.schedulers.Schedulers;
import io.rx_cache2.DynamicKey;
import io.rx_cache2.EvictDynamicKey;
import io.rx_cache2.internal.RxCache;
import io.victoralbertos.jolyglot.GsonSpeaker;
import okhttp3.OkHttpClient;
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

    private BookRepository() {
        cacheApi = new RxCache.Builder()
                .useExpiredDataIfLoaderNotAvailable(true)
                .persistence(App.getInstance().getCacheDir(), new GsonSpeaker())
                .using(BookCacheApi.class);

//        OkHttpClient.Builder builder = new OkHttpClient().newBuilder();
//        builder.addInterceptor(new GetOkHttpCookieInterceptor(MyApplication.getContext()));
//        builder.addInterceptor(new AddOkHttpCookieIntercept(MyApplication.getContext()));
//        builder.writeTimeout(60, TimeUnit.SECONDS);
//        builder.readTimeout(60, TimeUnit.SECONDS);
//        builder.connectTimeout(60, TimeUnit.SECONDS);

        remoteApi = new Retrofit.Builder()
                .baseUrl(URL_BASE)
                .addCallAdapterFactory(RxJava2CallAdapterFactory.create())
                .addConverterFactory(GsonConverterFactory.create())
//                .client(UnsafeOkHttpUtils.getClient())
                .build()
                .create(BookRemoteApi.class);
    }

    public static BookRepository getInstance() {
        if (INSTANCE == null) {
            INSTANCE = new BookRepository();
        }
        return INSTANCE;
    }

    public static void destroyInstance() {
        INSTANCE = null;
    }


    public void getJoke(Observer<List<BookBean>> observer){
        remoteApi.getData()
                .subscribeOn(Schedulers.io())
                .unsubscribeOn(Schedulers.io())
                .observeOn(AndroidSchedulers.mainThread())
                .subscribe(observer);
    }

    @Override
    public void getBooks(final BaseModelCallback listener) {
        try {
//            cacheApi.getList(remoteApi.getData(), new DynamicKey("test"), new EvictDynamicKey(false))
////                    .subscribeOn(Schedulers.io())
////                    .unsubscribeOn(Schedulers.io())
////                    .observeOn(AndroidSchedulers.mainThread())
//                    .subscribe(new Consumer<List<BookBean>>() {
//                        @Override
//                        public void accept(List<BookBean> bookBeans) throws Exception {
//                            listener.onSuccess(bookBeans);
//                        }
//                    });
            remoteApi.getData().subscribe(new Consumer<List<BookBean>>() {
                @Override
                public void accept(List<BookBean> bookBeans) throws Exception {
                    listener.onSuccess(bookBeans);
                }
            });

        } catch (Exception e) {
            e.printStackTrace();
        }
    }


    private String generateKey(String[] keywords) {
        StringBuilder keyBuilder = new StringBuilder();
        if (null != keywords && keywords.length > 0) {
            Arrays.sort(keywords);
            for (String keyword : keywords) {
                keyBuilder.append("&keyword=" + keyword);
            }
        }
        return keyBuilder.toString();
    }


}
