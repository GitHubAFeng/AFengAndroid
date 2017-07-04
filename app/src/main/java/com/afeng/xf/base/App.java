package com.afeng.xf.base;

import android.app.Application;
import android.content.pm.PackageInfo;
import android.content.pm.PackageManager;
import android.os.Handler;

import com.avos.avoscloud.AVOSCloud;
import com.avos.avoscloud.AVObject;
import com.orhanobut.logger.LogLevel;
import com.orhanobut.logger.Logger;
import com.squareup.leakcanary.LeakCanary;
import com.tencent.smtt.sdk.QbSdk;
import com.afeng.xf.BuildConfig;
import com.afeng.xf.ui.data.AdvertisingItem;
import com.afeng.xf.ui.data.HomeBannerItem;
import com.afeng.xf.ui.data.HomeListItem;
import com.afeng.xf.ui.data.NetConfig;
import com.afeng.xf.ui.data.SplashBannerItem;
import com.afeng.xf.ui.data.UserInfo;
import com.afeng.xf.utils.cache.ACache;

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
        initLeakCanary();  //初始化内存泄露检测
        instance = this;
        AFengConfig.init(this);

        //初始化ACache类
        aCache = ACache.get(this);


        ///////////////////后端云设置开始

        // 注册此类为数据表
        AVObject.registerSubclass(HomeListItem.class);
        AVObject.registerSubclass(HomeBannerItem.class);
        AVObject.registerSubclass(AdvertisingItem.class);
        AVObject.registerSubclass(UserInfo.class);
        AVObject.registerSubclass(NetConfig.class);
        AVObject.registerSubclass(SplashBannerItem.class);  //引导图

        // 节省流量
        AVOSCloud.setLastModifyEnabled(true);

        // 初始化参数依次为 this, AppId, AppKey
        AVOSCloud.initialize(this, "DsBf5jxiorz90M0wIsJTYjAo-gzGzoHsz", "9iLSxbsh1tJ3L4wMRx1MhX2M");

        ///////////////////后端云设置结束


        if (BuildConfig.DEBUG) {
            Logger
                    .init("AFeng")              // default PRETTYLOGGER or use just init()
                    .methodCount(3)                 // default 2
                    .logLevel(LogLevel.FULL)        // default LogLevel.FULL
                    .methodOffset(2)                // default 0
            ;
        }


        //Fragment管理器， 栈视图等功能，建议在Application里初始化
        Fragmentation.builder()
                // 设置 栈视图 模式为 悬浮球模式  BUBBLE 可见，  SHAKE: 摇一摇唤出   NONE：隐藏
                .stackViewMode(Fragmentation.NONE)
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


        initTBS();  //预加载 TBS浏览器

    }

    private void initLeakCanary() {

        if (LeakCanary.isInAnalyzerProcess(this)) {
            // This process is dedicated to LeakCanary for heap analysis.
            // You should not init your app in this process.
            return;
        }
        LeakCanary.install(this);
        // Normal app init code...
    }


    private void initTBS() {

        //搜集本地tbs内核信息并上报服务器，服务器返回结果决定使用哪个内核。

        QbSdk.PreInitCallback cb = new QbSdk.PreInitCallback() {

            @Override
            public void onViewInitFinished(boolean arg0) {
                // TODO Auto-generated method stub
                //x5內核初始化完成的回调，为true表示x5内核加载成功，否则表示x5内核加载失败，会自动切换到系统内核。
                Logger.d("app", " onViewInitFinished is " + arg0);
            }

            @Override
            public void onCoreInitFinished() {
                // TODO Auto-generated method stub
                // x5内核初始化完成回调接口，此接口回调并表示已经加载起来了x5，有可能特殊情况下x5内核加载失败，切换到系统内核。

            }
        };
        //x5内核初始化接口
        QbSdk.initX5Environment(getApplicationContext(), cb);

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