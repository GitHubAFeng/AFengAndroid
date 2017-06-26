package com.youth.xf.base;

import android.app.Application;
import android.content.pm.PackageInfo;
import android.content.pm.PackageManager;
import android.os.Handler;

import com.avos.avoscloud.AVOSCloud;
import com.avos.avoscloud.AVObject;
import com.orhanobut.logger.LogLevel;
import com.orhanobut.logger.Logger;
import com.youth.xf.BuildConfig;
import com.youth.xf.ui.data.AdvertisingItem;
import com.youth.xf.ui.data.HomeBannerItem;
import com.youth.xf.ui.data.HomeListItem;
import com.youth.xf.ui.data.SplashBannerItem;
import com.youth.xf.utils.cache.ACache;
import me.yokeyword.fragmentation.Fragmentation;
import me.yokeyword.fragmentation.helper.ExceptionHandler;


public class App extends Application {

    private static Handler mHandler;

    private static ACache aCache;

    private static App instance;

    public static App getInstance() {
        return instance;
    }

    @Override
    public void onCreate() {
        super.onCreate();
        instance = this;
        AFengConfig.init(this);

        //初始化ACache类
        aCache = ACache.get(this);


        ///////////////////后端云设置开始

        // 注册此类为数据表
        AVObject.registerSubclass(HomeListItem.class);
        AVObject.registerSubclass(HomeBannerItem.class);
        AVObject.registerSubclass(AdvertisingItem.class);
        AVObject.registerSubclass(SplashBannerItem.class);  //引导图

        // 节省流量
        AVOSCloud.setLastModifyEnabled(true);

        // 初始化参数依次为 this, AppId, AppKey
        AVOSCloud.initialize(this,"DsBf5jxiorz90M0wIsJTYjAo-gzGzoHsz","9iLSxbsh1tJ3L4wMRx1MhX2M");

        ///////////////////后端云设置结束



        if (BuildConfig.DEBUG) {
            Logger
                    .init("AFeng")              // default PRETTYLOGGER or use just init()
                    .methodCount(3)                 // default 2
                    .logLevel(LogLevel.FULL)        // default LogLevel.FULL
                    .methodOffset(2)                // default 0
            ;
        }

        Fragmentation.builder()
                // 设置 栈视图 模式为 悬浮球模式   SHAKE: 摇一摇唤出   NONE：隐藏
                .stackViewMode(Fragmentation.BUBBLE)
                // ture时，遇到异常："Can not perform this action after onSaveInstanceState!"时，会抛出
                // false时，不会抛出，会捕获，可以在handleException()里监听到
                .debug(BuildConfig.DEBUG)
                // 在debug=false时，即线上环境时，上述异常会被捕获并回调ExceptionHandler
                .handleException(new ExceptionHandler() {
                    @Override
                    public void onException(Exception e) {
                        // 建议在该回调处上传至我们的Crash监测服务器
                        // 以Bugtags为例子: 手动把捕获到的 Exception 传到 Bugtags 后台。
                        // Bugtags.sendException(e);
                        Logger.e(e.toString());
                    }
                })
                .install();

    }

    //全局线程
    public static Handler getHandler() {
        if (mHandler == null) {
            mHandler = new Handler();
        }
        return mHandler;
    }

    //取缓存实例
    public static ACache getACache() {
        return aCache;
    }


    //版本名
    public static String getVersionName() {
        return getPackageInfo().versionName;
    }

    //版本号
    public static int getVersionCode() {
        return getPackageInfo().versionCode;
    }

    private static PackageInfo getPackageInfo() {
        PackageInfo pi = null;
        try {
            PackageManager pm = instance.getPackageManager();
            pi = pm.getPackageInfo(instance.getPackageName(),
                    PackageManager.GET_CONFIGURATIONS);
            return pi;
        } catch (Exception e) {
            e.printStackTrace();
        }
        return pi;
    }


}
