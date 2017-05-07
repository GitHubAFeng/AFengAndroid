package com.youth.xf.utils.GlideHelper;

import android.content.Context;
import android.content.Intent;
import android.graphics.Bitmap;
import android.net.Uri;
import android.os.Environment;
import android.provider.MediaStore;
import android.widget.ImageView;

import com.bumptech.glide.Glide;
import com.youth.xf.R;

import java.io.ByteArrayOutputStream;
import java.io.File;
import java.io.FileNotFoundException;
import java.io.FileOutputStream;
import java.io.IOException;

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


    /**
     * 保存图片至相册
     *
     * @param context   上下文
     * @param bmp       图片
     * @param albumName 保存的专辑名字，没有则新建
     */
    public static void saveImageToGallery(Context context, Bitmap bmp, String albumName) {
        // 首先保存图片
        File appDir = new File(Environment.getExternalStorageDirectory(), albumName);
        if (!appDir.exists()) {
            appDir.mkdir();
        }
        String fileName = System.currentTimeMillis() + ".jpg";
        File file = new File(appDir, fileName);
        try {
            FileOutputStream fos = new FileOutputStream(file);
            bmp.compress(Bitmap.CompressFormat.JPEG, 100, fos);
            fos.flush();
            fos.close();
        } catch (IOException e) {
            e.printStackTrace();
        }

        // 其次把文件插入到系统图库
        try {
            MediaStore.Images.Media.insertImage(context.getContentResolver(),
                    file.getAbsolutePath(), fileName, null);
        } catch (FileNotFoundException e) {
            e.printStackTrace();
        }
        // 最后通知图库更新
        context.sendBroadcast(new Intent(Intent.ACTION_MEDIA_SCANNER_SCAN_FILE, Uri.parse("file://" + file.getAbsoluteFile())));
    }


}
