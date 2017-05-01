package com.youth.xf;

import android.app.Application;

import com.orhanobut.logger.LogLevel;
import com.orhanobut.logger.Logger;
import com.youth.xf.base.AFengConfig;
import com.youth.xf.utils.DB.GreenDaoManager;


public class App extends Application {

    private static App instance;

    public static App getInstance() {
        return instance;
    }

    @Override
    public void onCreate() {
        super.onCreate();
        instance = this;
        AFengConfig.init(this);
        GreenDaoManager.getInstance(); //ORM初始化

        if (BuildConfig.DEBUG) {
            Logger
                    .init("AFeng")              // default PRETTYLOGGER or use just init()
                    .methodCount(3)                 // default 2
                    .logLevel(LogLevel.FULL)        // default LogLevel.FULL
                    .methodOffset(2)                // default 0
            ;
        }
    }
}
