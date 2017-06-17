package com.youth.xf.widget.bottomsheetdialog;

import android.app.Activity;
import android.content.Context;
import android.content.res.Resources;
import android.os.Bundle;
import android.support.annotation.NonNull;
import android.support.annotation.StyleRes;
import android.support.design.widget.BottomSheetDialogFragment;
import android.util.DisplayMetrics;
import android.view.ViewGroup;

import com.youth.xf.R;
import com.youth.xf.base.App;

/**
 * 解决使用BottomSheetDialog时状态栏变黑的问题
 * Created by Gordn on 2017/3/23.
 */

public class xBottomSheetDialog extends android.support.design.widget.BottomSheetDialog{
    public xBottomSheetDialog(@NonNull Context context) {
        super(context);
        BottomSheetDialogFragment dd;
    }

    public xBottomSheetDialog(@NonNull Context context, @StyleRes int theme) {
        super(context, theme);

    }

    protected xBottomSheetDialog(@NonNull Context context, boolean cancelable, OnCancelListener cancelListener) {
        super(context, cancelable, cancelListener);
    }

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        int screenHeight = getScreenHeight(getOwnerActivity());
        int statusBarHeight = getStatusBarHeight(getContext());
        int dialogHeight = screenHeight - statusBarHeight;
        getWindow().setLayout(ViewGroup.LayoutParams.MATCH_PARENT, dialogHeight == 0 ? ViewGroup.LayoutParams.MATCH_PARENT : dialogHeight);

        //设置菜单背景色，默认透明
//        getWindow().findViewById(R.id.design_bottom_sheet).setBackgroundResource(R.color.transparent);
    }

    private static int getScreenHeight(Activity activity) {
        DisplayMetrics displaymetrics = new DisplayMetrics();
        activity.getWindowManager().getDefaultDisplay().getMetrics(displaymetrics);
        return displaymetrics.heightPixels;
    }

    private static int getStatusBarHeight(Context context) {
        int statusBarHeight = 0;
        Resources res = context.getResources();
        int resourceId = res.getIdentifier("status_bar_height", "dimen", "android");
        if (resourceId > 0) {
            statusBarHeight = res.getDimensionPixelSize(resourceId);
        }
        return statusBarHeight;
    }
}