package com.youth.xf.base;

import android.content.Context;
import android.os.Bundle;
import android.support.annotation.IdRes;
import android.support.annotation.Nullable;
import android.support.v4.app.Fragment;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

import com.orhanobut.logger.Logger;
import com.youth.xf.App;

import butterknife.ButterKnife;
import butterknife.Unbinder;
import io.reactivex.disposables.CompositeDisposable;
import io.reactivex.disposables.Disposable;


/**
 * 作者： AFeng
 * 时间：2017/3/2
 */

public abstract class AFengFragment extends Fragment {

    private CompositeDisposable mdisposables;

    protected String TAG;
    protected App mApp;
    protected View mContentView;
    //    protected Engine mEngine;
    protected AFengActivity mActivity;

    protected boolean mIsLoadedData = false;

    Unbinder mbinder;

    @Override
    public void onAttach(Context context) {
        super.onAttach(context);
        TAG = this.getClass().getSimpleName();
        mApp = App.getInstance();
        mActivity = (AFengActivity) getActivity();
//        mEngine = mApp.getEngine();
    }

    @Nullable
    @Override
    public View onCreateView(LayoutInflater inflater, @Nullable ViewGroup container, @Nullable Bundle savedInstanceState) {
        // 避免多次从xml中加载布局文件
        if (mContentView == null) {
            mContentView = inflater.inflate(getLayoutId(), container, false);
            initView(savedInstanceState);
            setListener();
            processLogic(savedInstanceState);
        } else {
            ViewGroup parent = (ViewGroup) mContentView.getParent();
            if (parent != null) {
                parent.removeView(mContentView);
            }
        }
        mbinder = ButterKnife.bind(this,mContentView);
        return mContentView;
    }


    //初始化布局
    protected abstract int getLayoutId();

    /**
     * 初始化View控件
     */
    protected abstract void initView(Bundle savedInstanceState);

    /**
     * 给View控件添加事件监听器
     */
    protected abstract void setListener();

    /**
     * 处理业务逻辑，状态恢复等操作
     *
     * @param savedInstanceState
     */
    protected abstract void processLogic(Bundle savedInstanceState);


    /**
     * 实现懒加载，可见时才加载数据
     *
     * @param isVisibleToUser
     */
    @Override
    public void setUserVisibleHint(boolean isVisibleToUser) {
        super.setUserVisibleHint(isVisibleToUser);
        if (isResumed()) {
            handleOnVisibilityChangedToUser(isVisibleToUser);
        }
    }

    @Override
    public void onResume() {
        super.onResume();
        if (getUserVisibleHint()) {
            handleOnVisibilityChangedToUser(true);
        }
    }

    @Override
    public void onPause() {
        super.onPause();
        if (getUserVisibleHint()) {
            handleOnVisibilityChangedToUser(false);
        }
    }

    /**
     * 处理对用户是否可见
     *
     * @param isVisibleToUser
     */
    private void handleOnVisibilityChangedToUser(boolean isVisibleToUser) {
        if (isVisibleToUser) {
            // 对用户可见
            if (!mIsLoadedData) {
                mIsLoadedData = true;
                onLazyLoadOnce();
            }
            onVisibleToUser();
        } else {
            // 对用户不可见
            onInvisibleToUser();
        }
    }

    /**
     * 懒加载一次。如果只想在对用户可见时才加载数据，并且只加载一次数据，在子类中重写该方法
     */
    protected void onLazyLoadOnce() {
    }

    /**
     * 对用户可见时触发该方法。如果只想在对用户可见时才加载数据，在子类中重写该方法
     */
    protected void onVisibleToUser() {
    }

    /**
     * 对用户不可见时触发该方法
     */
    protected void onInvisibleToUser() {
    }


    protected abstract void onVisible();

    protected abstract void onInvisible();

    @SuppressWarnings("unchecked")
    public final <E extends View> E getViewById(@IdRes int id) {
        try {
            return (E) mContentView.findViewById(id);
        } catch (ClassCastException ex) {
            Logger.w("视图元素类型强制失败！", ex);
            throw ex;
        }
    }

    @Override
    public void onDestroy() {
        super.onDestroy();
        if (this.mdisposables != null && this.mdisposables.size() > 0) {
            this.mdisposables.clear(); // do not send event after activity has been destroyed
        }
    }

    public void removeDisposable(Disposable d) {
        if (this.mdisposables != null && this.mdisposables.size() > 0) {
            this.mdisposables.remove(d);
        }
    }

    public void addDisposable(Disposable d) {
        if (this.mdisposables == null) {
            this.mdisposables = new CompositeDisposable();
        }
        this.mdisposables.add(d);
    }

    @Override
    public void onDestroyView() {
        super.onDestroyView();
        mbinder.unbind();
    }
}
