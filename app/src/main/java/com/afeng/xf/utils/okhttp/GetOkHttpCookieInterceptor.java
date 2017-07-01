package com.afeng.xf.utils.okhttp;

/**
 * Created by Administrator on 2017/5/2.
 */

import android.content.Context;
import android.content.SharedPreferences;
import android.util.Log;

import java.io.IOException;
import java.util.List;

import io.reactivex.Observable;
import io.reactivex.functions.Function;
import okhttp3.Interceptor;
import okhttp3.Response;


/**
 * Created by Credit on 2017/3/14.
 * 响应时,获取cookie拦截器
 */

public class GetOkHttpCookieInterceptor implements Interceptor {

    private static final String TAG = "GetOkHttpCookie";
    private Context context;

    public GetOkHttpCookieInterceptor(Context context) {
        super();
        this.context = context;
    }

    @Override
    public Response intercept(Chain chain) throws IOException {
        Response originalResponse = chain.proceed(chain.request());
        //这里获取请求返回的cookie
        if (!originalResponse.headers("Set-Cookie").isEmpty()) {
            final StringBuffer cookieBuffer = new StringBuffer();
            //最近在学习RxJava,这里用了RxJava的相关API大家可以忽略,用自己逻辑实现即可.大家可以用别的方法保存cookie数据

            List<String> headers = originalResponse.headers("Set-Cookie");
            SharedPreferences sharedPreferences = context.getSharedPreferences("cookie", Context.MODE_PRIVATE);
            SharedPreferences.Editor editor = sharedPreferences.edit();
            Observable.fromIterable(headers)
                    .map(new Function<String, String>() {
                        @Override
                        public String apply(String s) throws Exception {

                            String[] cookieArray = s.split(";");
                            return cookieArray[0];
                        }
                    })
                    .subscribe(cookie -> {
                        Log.e(TAG, " 保存 cookie ---intercept: " + cookie);
                        String[] split = cookie.split("=");
                        editor.putString(split[0], cookie + ";");
                        // cookieBuffer.append(cookie).append(";");
                    });
            editor.commit();
        }

        return originalResponse;
    }
}
