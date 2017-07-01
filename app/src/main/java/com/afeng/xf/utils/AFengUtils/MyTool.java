package com.afeng.xf.utils.AFengUtils;

import android.app.Activity;
import android.content.Context;
import android.content.Intent;
import android.os.Bundle;

import com.orhanobut.logger.Logger;

import java.io.Serializable;


/**
 * Created by Administrator on 2017/7/2.
 * 小工具集
 */

public class MyTool {

    /**
     * 传递数据对象到新启动的Activity
     *
     * @param context
     * @param target  要启动的Activity
     * @param key     键值
     * @param event   要传递的对象，必须Serializable化
     */
    public static void goToActivity(Context context, Class target, String key, Serializable event) {

        Intent intent = new Intent();
        intent.setClass(context, target);
        Bundle bundle = new Bundle();
        bundle.putSerializable(key, event);
        intent.putExtras(bundle);
        context.startActivity(intent);
    }


    /**
     * 接收 来自 源Activity 的数据对象
     *
     * @param context
     * @param key
     * @return
     */
    public static Object getIntentData(Activity context, String key) {

        try {
            Intent intent = context.getIntent();
            return intent.getSerializableExtra(key);

        } catch (NullPointerException e) {
            Logger.w(e.getMessage());
        } catch (Exception e) {
            Logger.w(e.getMessage());
        }

        return null;
    }


}
