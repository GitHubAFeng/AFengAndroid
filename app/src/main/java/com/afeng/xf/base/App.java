package com.afeng.xf.base;

import android.app.Application;
import android.content.pm.PackageInfo;
import android.content.pm.PackageManager;
import android.os.Handler;
import android.util.Log;

import com.afeng.xf.ui.data.FuLiContentBean;
import com.afeng.xf.ui.data.FuLiHeadBean;
import com.afeng.xf.utils.AFengUtils.AppLogMessageMgr;
import com.avos.avoscloud.AVOSCloud;
import com.avos.avoscloud.AVObject;

import com.qiniu.android.http.ResponseInfo;
import com.qiniu.android.storage.UpCompletionHandler;
import com.qiniu.android.storage.UploadManager;
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

import org.json.JSONObject;


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
        AVObject.registerSubclass(HomeListItem.class);  //主页下拉
        AVObject.registerSubclass(HomeBannerItem.class);  //主页轮播
        AVObject.registerSubclass(AdvertisingItem.class);  //广告
        AVObject.registerSubclass(UserInfo.class);  //用户信息
        AVObject.registerSubclass(NetConfig.class); //网络配置
        AVObject.registerSubclass(SplashBannerItem.class);  //引导图
        AVObject.registerSubclass(FuLiHeadBean.class);  //福利头部
        AVObject.registerSubclass(FuLiContentBean.class);  //福利内容

        // 节省流量
        AVOSCloud.setLastModifyEnabled(true);

        // 初始化参数依次为 this, AppId, AppKey
        AVOSCloud.initialize(this, "DsBf5jxiorz90M0wIsJTYjAo-gzGzoHsz", "9iLSxbsh1tJ3L4wMRx1MhX2M");

        ///////////////////后端云设置结束

        // 是否开启日志
        AppLogMessageMgr.isEnableDebug(BuildConfig.DEBUG);

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
