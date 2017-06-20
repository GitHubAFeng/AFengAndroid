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
import com.youth.xf.base.App;

import java.io.ByteArrayOutputStream;
import java.io.File;
import java.io.FileNotFoundException;
import java.io.FileOutputStream;
import java.io.IOException;
import java.util.concurrent.ExecutionException;

/**
 * 作者： AFeng
 * 时间：2017/2/28
 */

public class ImgLoadUtil {

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
     * @param bmp
     * @param imgDirName 目录名字
     * @return
     */
    public static boolean saveImageToGallery(Bitmap bmp, String imgDirName) {
        // 首先保存图片
        String storePath = Environment.getExternalStorageDirectory().getAbsolutePath() + File.separator + imgDirName;
        File appDir = new File(storePath);
        if (!appDir.exists()) {
            appDir.mkdir();
        }
        String fileName = System.currentTimeMillis() + ".jpg";
        File file = new File(appDir, fileName);
        try {
            FileOutputStream fos = new FileOutputStream(file);
            //通过io流的方式来压缩保存图片
            boolean isSuccess = bmp.compress(Bitmap.CompressFormat.JPEG, 60, fos);
            fos.flush();
            fos.close();

            //把文件插入到系统图库
            //MediaStore.Images.Media.insertImage(App.getInstance().getContentResolver(), file.getAbsolutePath(), fileName, null);

            //保存图片后发送广播通知更新数据库
            Uri uri = Uri.fromFile(file);
            App.getInstance().sendBroadcast(new Intent(Intent.ACTION_MEDIA_SCANNER_SCAN_FILE, uri));
            if (isSuccess) {
                return true;
            } else {
                return false;
            }
        } catch (IOException e) {
            e.printStackTrace();
        }
        return false;
    }


    /**
     * 保存图片至相册
     *
     * @param context
     * @param bmp
     * @param imgDirName 目录名字
     * @return
     */
    public static boolean saveImageToGallery(Context context, Bitmap bmp, String imgDirName) {
        // 首先保存图片
        String storePath = Environment.getExternalStorageDirectory().getAbsolutePath() + File.separator + imgDirName;
        File appDir = new File(storePath);
        if (!appDir.exists()) {
            appDir.mkdir();
        }
        String fileName = System.currentTimeMillis() + ".jpg";
        File file = new File(appDir, fileName);
        try {
            FileOutputStream fos = new FileOutputStream(file);
            //通过io流的方式来压缩保存图片
            boolean isSuccess = bmp.compress(Bitmap.CompressFormat.JPEG, 60, fos);
            fos.flush();
            fos.close();

            //把文件插入到系统图库
            //MediaStore.Images.Media.insertImage(context.getContentResolver(), file.getAbsolutePath(), fileName, null);

            //保存图片后发送广播通知更新数据库
            Uri uri = Uri.fromFile(file);
            context.sendBroadcast(new Intent(Intent.ACTION_MEDIA_SCANNER_SCAN_FILE, uri));
            if (isSuccess) {
                return true;
            } else {
                return false;
            }
        } catch (IOException e) {
            e.printStackTrace();
        }
        return false;
    }


    /**
     * 根据URL获取Bitmap
     *
     * @param context
     * @param url
     * @return
     * @throws ExecutionException
     * @throws InterruptedException
     */
    public static Bitmap getBitmapByUrl(Context context, String url) throws ExecutionException, InterruptedException {
        Bitmap myBitmap = Glide.with(context)
                .load(url)
                .asBitmap() //必须
                .centerCrop()
                .into(500, 500)
                .get();
        return myBitmap;
    }


}
