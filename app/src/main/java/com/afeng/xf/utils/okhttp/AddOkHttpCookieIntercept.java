package com.afeng.xf.utils.okhttp;

/**
 * Created by Administrator on 2017/5/2.
 */

import android.content.Context;
import android.content.SharedPreferences;

import java.io.IOException;

import io.reactivex.Observable;
import okhttp3.Interceptor;
import okhttp3.Request;
import okhttp3.Response;

/**
 * Created by Credit on 2017/3/14.
 * 请求时，添加cookie拦截器
 */

public class AddOkHttpCookieIntercept implements Interceptor {
    private Context context;

    public AddOkHttpCookieIntercept(Context context) {
        super();
        this.context = context;
    }

    @Override
    public Response intercept(Chain chain) throws IOException {

        final Request.Builder builder = chain.request().newBuilder();
        SharedPreferences sharedPreferences = context.getSharedPreferences("cookie", Context.MODE_PRIVATE);
        //最近在学习RxJava,这里用了RxJava的相关API大家可以忽略,用自己逻辑实现即可

        /**
         *  beegosessionID  --beego默认返回的seession key
         *
         *this.Ctx.SetCookie("mobile", u.Mobile, maxTime, "/")
         this.Ctx.SetCookie("pwd", u.Pwd, maxTime, "/")
         服务器对应的cookie
         */

        String beegosessionID = sharedPreferences.getString("beegosessionID", "");
        String mobile = sharedPreferences.getString("mobile", "");
        String pwd = sharedPreferences.getString("pwd", "");

        Observable.just(beegosessionID, mobile, pwd)
                .subscribe(cookie -> {
                    //添加cookie
                    builder.addHeader("Cookie", cookie);
                });
        return chain.proceed(builder.build());
    }
}

