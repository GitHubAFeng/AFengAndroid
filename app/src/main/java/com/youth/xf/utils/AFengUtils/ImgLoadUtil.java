package com.youth.xf.utils.AFengUtils;

import android.content.Context;
import android.widget.ImageView;

import com.bumptech.glide.Glide;
import com.youth.xf.R;
import com.youth.xf.view.GlideCircleTransform;

/**
 * 作者： AFeng
 * 时间：2017/2/28
 */

public class ImgLoadUtil {

    private static ImgLoadUtil instance;

    private ImgLoadUtil() {
    }

    public static ImgLoadUtil getInstance() {
        if (instance == null) {
            instance = new ImgLoadUtil();
        }
        return instance;
    }


    /**
     * 加载圆角图,暂时用到显示头像
     */
    public static void displayCircle(Context context, ImageView imageView, String imageUrl) {
        Glide.with(context)
                .load(imageUrl)
                .crossFade(500)
                .error(R.drawable.ic_avatar_default)
                .transform(new GlideCircleTransform(context))
                .into(imageView);
    }



}
