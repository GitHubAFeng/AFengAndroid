package com.afeng.xf.ui.demo.api;

import com.afeng.xf.utils.okhttp.UnsafeOkHttpUtils;

import retrofit2.Retrofit;
import retrofit2.adapter.rxjava2.RxJava2CallAdapterFactory;
import retrofit2.converter.gson.GsonConverterFactory;

/**
 * Created by Administrator on 2017/5/3.
 * 创建 Retrofit.Builder
 */

public class HttpUtils {

    private static HttpUtils instance;
    private Object gankHttps;
    private Object doubanHttps;
    private Object dongtingHttps;
    private boolean debug;
    // gankio、豆瓣、（轮播图）
    private final static String API_GANKIO = "https://gank.io/api/";
    private final static String API_DOUBAN = "Https://api.douban.com/";
    private final static String API_TING = "https://tingapi.ting.baidu.com/v1/restserver/";

    /**
     * 分页数据，每页的数量
     */
    public static int per_page = 10;
    public static int per_page_more = 20;

    public static HttpUtils getInstance() {
        if (instance == null) {
            synchronized (HttpUtils.class) {
                if (instance == null) {
                    instance = new HttpUtils();
                }
            }
        }
        return instance;
    }

    public <T> T getDouBanServer(Class<T> a) {
        if (doubanHttps == null) {
            synchronized (HttpUtils.class) {
                if (doubanHttps == null) {
                    doubanHttps = getBuilder(API_DOUBAN).build().create(a);
                }
            }
        }
        return (T) doubanHttps;
    }


    public <T> T getGankIOServer(Class<T> a) {
        if (gankHttps == null) {
            synchronized (HttpUtils.class) {
                if (gankHttps == null) {
                    gankHttps = getBuilder(API_GANKIO).build().create(a);
                }
            }
        }
        return (T) gankHttps;
    }

    public <T> T getTingServer(Class<T> a) {
        if (dongtingHttps == null) {
            synchronized (HttpUtils.class) {
                if (dongtingHttps == null) {
                    dongtingHttps = getBuilder(API_TING).build().create(a);
                }
            }
        }
        return (T) dongtingHttps;
    }

    private Retrofit.Builder getBuilder(String apiUrl) {
        Retrofit.Builder builder = new Retrofit.Builder();
        builder.client(UnsafeOkHttpUtils.getClient());
        builder.baseUrl(apiUrl);//设置远程地址
        builder.addConverterFactory(GsonConverterFactory.create());
        builder.addCallAdapterFactory(RxJava2CallAdapterFactory.create());
        return builder;
    }

}
