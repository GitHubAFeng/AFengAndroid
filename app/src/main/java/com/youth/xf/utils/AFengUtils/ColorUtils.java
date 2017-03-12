package com.youth.xf.utils.AFengUtils;

import android.graphics.Color;

import java.util.Random;

/**
 * 作者： AFeng
 * 时间：2017/2/25
 */

public class ColorUtils {

    /**
     * 随机颜色
     */
    public static int randomColor() {
        Random random = new Random();
        int red = random.nextInt(150) + 50;//50-199
        int green = random.nextInt(150) + 50;//50-199
        int blue = random.nextInt(150) + 50;//50-199
        return Color.rgb(red, green, blue);
    }



}
