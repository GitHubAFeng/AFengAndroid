package com.afeng.xf.utils.AFengUtils;

import android.support.annotation.StringRes;
import android.widget.Toast;
import com.afeng.xf.base.AFengConfig;


/**
 * 作者:王浩 邮件:bingoogolapple@gmail.com
 * 创建时间:15/7/2 10:17
 * 描述:
 */
public class xToastUtil {

    private static Toast sToast;

    private xToastUtil() {
    }

    /**
     * 显示资源文件中文本
     * @param resId
     */
    public static void showToast(@StringRes int resId) {
        showToast(AFengConfig.getContext().getString(resId));
    }

    /**
     * 自动选择显示时间
     * @param text
     */
    public static void showToast(CharSequence text) {
        int duration = text.length() < 10 ? Toast.LENGTH_SHORT : Toast.LENGTH_LONG;
        showToast(text, duration);
    }

    /**
     * 显示吐司
     *
     * @param text     文本
     * @param duration 显示时长
     */
    public static void showToast(CharSequence text, int duration) {
        if (sToast == null) {
            sToast = Toast.makeText(AFengConfig.getContext(), text, duration);
        } else {
            sToast.setText(text);
            sToast.setDuration(duration);
        }
        sToast.show();
    }

    /**
     * 取消吐司显示
     */
    public static void cancelToast() {
        if (sToast != null) {
            sToast.cancel();
            sToast = null;
        }
    }

}