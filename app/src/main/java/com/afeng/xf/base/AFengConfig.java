package com.afeng.xf.base;

import android.content.Context;

/**
 * Created by AFeng on 2017/3/14.
 */

public class AFengConfig {

    // #log是否打开
    public static final String TAG = "AFeng";
    public static final boolean IS_DEBUG = true;

    private static Context context;

    private AFengConfig() {
        throw new UnsupportedOperationException("请在Application中调用init方法进行初始化配置！");
    }

    /**
     * 初始化工具类
     *
     * @param context 上下文
     */
    public static void init(Context context) {
        AFengConfig.context = context.getApplicationContext();
    }

    /**
     * 获取ApplicationContext
     *
     * @return ApplicationContext
     */
    public static Context getContext() {
        if (context != null) return context;
        throw new NullPointerException("未初始化配置！无法获取全局context！");
    }


}
