package com.youth.xf.utils.AFengUtils;

import android.content.Context;
import android.graphics.drawable.Drawable;
import android.os.Build;
import android.support.annotation.ColorRes;
import android.support.annotation.DrawableRes;
import android.support.annotation.NonNull;
import android.view.View;

import com.youth.xf.base.AFengConfig;


/**
 * 此类主要是用来放一些系统过时方法的处理
 */
public class XOutdatedUtils {
    /**
     * setBackgroundDrawable过时方法处理
     *
     * @param view
     * @param drawable
     */
    public static void setBackground(@NonNull View view, Drawable drawable) {
        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.JELLY_BEAN)
            view.setBackground(drawable);
        else
            view.setBackgroundDrawable(drawable);
    }

    /**
     * getDrawable过时方法处理
     *
     * @param id
     * @return
     */
    public static Drawable getDrawable(@DrawableRes int id) {
        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.LOLLIPOP)
            return AFengConfig.getContext().getDrawable(id);
        else
            return AFengConfig.getContext().getResources().getDrawable(id);
    }

}
