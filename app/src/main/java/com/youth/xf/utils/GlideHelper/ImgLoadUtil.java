package com.youth.xf.utils.GlideHelper;

import android.content.Context;
import android.graphics.Bitmap;
import android.widget.ImageView;

import com.bumptech.glide.Glide;
import com.youth.xf.R;
import java.io.ByteArrayOutputStream;

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
     * 从网络RUL加载圆角图,暂时用到显示头像
     */
    public static void displayCircle(Context context, ImageView imageView, String imageUrl) {
        Glide.with(context)
                .load(imageUrl)
                .crossFade(500)
                .error(R.drawable.ic_avatar_default)
                .transform(new GlideCircleTransform(context))
                .into(imageView);
    }

    /**
     * 从Bitmap加载圆角图,暂时用到显示头像
     */
    public static void displayCircleByBitmap(Context context, final ImageView imageView, Bitmap bitmap) {
        ByteArrayOutputStream baos = new ByteArrayOutputStream();
        bitmap.compress(Bitmap.CompressFormat.PNG, 100, baos);  // 100表示不压缩。例如给值30的意思就是压缩70%
        byte[] bytes = baos.toByteArray();
        //Glide不能直接加载Bitmap，传递前需要先转为byte[]比特流就可以了
        Glide.with(context)
                .load(bytes)
                .crossFade(500)
                .error(R.drawable.ic_avatar_default)
                .transform(new GlideCircleTransform(context))
                .into(imageView);
    }

}
