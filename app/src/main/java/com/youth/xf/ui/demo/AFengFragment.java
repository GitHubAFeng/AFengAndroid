package com.youth.xf.ui.demo;

import android.graphics.drawable.AnimationDrawable;
import android.os.Bundle;
import android.support.annotation.IdRes;
import android.support.annotation.Nullable;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.RelativeLayout;

import com.youth.xf.utils.AFengUtils.PerfectClickListener;
import com.youth.xframe.base.XFragment;
import com.youth.xframe.utils.log.XLog;

import rx.Subscription;
import rx.subscriptions.CompositeSubscription;

/**
 * 作者： AFeng
 * 时间：2017/3/2
 */

public abstract class AFengFragment extends XFragment{

    protected boolean mIsVisible = false;
    private CompositeSubscription mCompositeSubscription;


    /**
     * 在这里实现Fragment数据的缓加载.
     */
    @Override
    public void setUserVisibleHint(boolean isVisibleToUser) {
        super.setUserVisibleHint(isVisibleToUser);
        if (getUserVisibleHint()) {
            mIsVisible = true;
            onVisible();
        } else {
            mIsVisible = false;
            onInvisible();
        }
    }

    protected abstract void onVisible();

    protected abstract void onInvisible();

    @SuppressWarnings("unchecked")
    public final <E extends View> E getViewById(@IdRes int id) {
        try {
            return (E) getView().findViewById(id);
        } catch (ClassCastException ex) {
            XLog.w("视图元素类型强制失败！", ex);
            throw ex;
        }
    }

    public void addSubscription(Subscription s) {
        if (this.mCompositeSubscription == null) {
            this.mCompositeSubscription = new CompositeSubscription();
        }
        this.mCompositeSubscription.add(s);
    }

    @Override
    public void onDestroy() {
        super.onDestroy();
        if (this.mCompositeSubscription != null && mCompositeSubscription.hasSubscriptions()) {
            this.mCompositeSubscription.unsubscribe();
        }
    }

    public void removeSubscription() {
        if (this.mCompositeSubscription != null && mCompositeSubscription.hasSubscriptions()) {
            this.mCompositeSubscription.unsubscribe();
        }
    }



}
