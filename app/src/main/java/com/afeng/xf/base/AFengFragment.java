package com.afeng.xf.base;

import android.content.Context;
import android.content.Intent;
import android.os.Bundle;
import android.support.annotation.IdRes;
import android.support.annotation.Nullable;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

import com.orhanobut.logger.Logger;
import com.afeng.xf.utils.AFengUtils.SnackbarUtils;
import com.afeng.xf.utils.AFengUtils.xToastUtil;
import com.afeng.xf.utils.cache.ACache;
import com.afeng.xf.utils.cache.AppSharePreferenceMgr;

import java.io.Serializable;

import butterknife.ButterKnife;
import butterknife.Unbinder;
import io.reactivex.disposables.CompositeDisposable;
import io.reactivex.disposables.Disposable;
import me.yokeyword.fragmentation.SupportFragment;



/**
 * 作者： AFeng
 * 时间：2017/3/2
 */

public abstract class AFengFragment extends SupportFragment {

    private CompositeDisposable mdisposables;

    protected String TAG;
    protected App mApp;
    protected View mContentView;
    protected AFengActivity mActivity;
    protected ACache mCache;
    protected boolean mIsLoadedData = false;

    Unbinder mbinder;

    @Override
    public void onAttach(Context context) {
        super.onAttach(context);
        TAG = this.getClass().getSimpleName();
        mApp = App.getInstance();
        mActivity = (AFengActivity) getActivity();
    }

    @Nullable
    @Override
    public View onCreateView(LayoutInflater inflater, @Nullable ViewGroup container, @Nullable Bundle savedInstanceState) {
        // 避免多次从xml中加载布局文件
        if (mContentView == null) {
            mContentView = inflater.inflate(getLayoutId(), container, false);
            mbinder = ButterKnife.bind(this,mContentView);
            mCache = ACache.get(mActivity);
            initView(savedInstanceState);
            setListener();
            processLogic(savedInstanceState);
        } else {
            ViewGroup parent = (ViewGroup) mContentView.getParent();
            if (parent != null) {
                parent.removeView(mContentView);
            }
        }

        return mContentView;
    }


    public void xToastShow(String s) {
        xToastUtil.showToast(s);
    }

    public void xSnackBarShow(String s) {
        SnackbarUtils.showSnackBar(getActivity(), s);
    }

    public void xLogger(String s){
        Logger.e(s);
    }

    /**
     * 初始化布局,返回layout
     */
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
    protected abstract void onLazyLoadOnce();

    /**
     * 对用户可见时触发该方法。如果只想在对用户可见时才加载数据，在子类中重写该方法
     */
    protected abstract void onVisibleToUser();

    /**
     * 对用户不可见时触发该方法
     */
    protected abstract void onInvisibleToUser();



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
//        如果onCreateView里面view是全局的或者你在其它地方调用了，那么onDestroyView调用unbind函数，那么就会出现空指针异常
//        mbinder.unbind();
    }


    /**
     * 保存数据的方法，我们需要拿到保存数据的具体类型，然后根据类型调用不同的保存方法
     *
     * @param key
     * @param object
     */
    public void xPut(String key, Object object) {
        AppSharePreferenceMgr.put(mActivity, key, object);
    }


    /**
     * 得到保存数据的方法，我们根据默认值得到保存的数据的具体类型，然后调用相对于的方法获取值
     *
     * @param key
     * @param defaultObject 默认值
     * @return
     */
    public Object xGet(String key, Object defaultObject) {

        return AppSharePreferenceMgr.get(mActivity, key, defaultObject);
    }




    /**
     * 传递数据对象到新启动的Activity
     *
     * @param target 要启动的Activity
     * @param key    键值
     * @param event  要传递的对象，必须Serializable化
     */
    public void goToActivity(Class<?> target, String key, Serializable event) {

        Intent intent = new Intent();
        intent.setClass(mActivity, target);
        Bundle bundle = new Bundle();
        bundle.putSerializable(key, event);
        intent.putExtras(bundle);
        this.startActivity(intent);
    }


    /**
     * 接收 来自 源Activity 的数据对象
     *
     * @param key
     * @return
     */
    public Object getIntentData(String key) {

        try {
            Intent intent = mActivity.getIntent();
            return intent.getSerializableExtra(key);

        } catch (NullPointerException e) {
            Logger.w(e.getMessage());
        } catch (Exception e) {
            Logger.w(e.getMessage());
        }

        return null;
    }


    /**
     * 含有Bundle通过Class打开编辑界面
     *
     * @param cls
     * @param bundle
     * @param requestCode
     */
    public void goToActivityForResult(Class<?> cls, Bundle bundle, int requestCode) {
        Intent intent = new Intent();
        intent.setClass(mActivity, cls);
        if (bundle != null) {
            intent.putExtras(bundle);
        }
        startActivityForResult(intent, requestCode);
    }


}
