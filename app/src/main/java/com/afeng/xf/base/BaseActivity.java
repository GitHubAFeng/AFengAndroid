package com.afeng.xf.base;

import android.app.Activity;
import android.content.Context;
import android.content.Intent;
import android.os.Bundle;
import android.support.annotation.IdRes;
import android.support.v7.app.AppCompatActivity;
import android.view.MenuItem;
import android.view.View;

import com.orhanobut.logger.Logger;
import com.afeng.xf.utils.AFengUtils.SnackbarUtils;
import com.afeng.xf.utils.AFengUtils.xToastUtil;
import com.afeng.xf.utils.GlideHelper.GlideUtils;
import com.afeng.xf.utils.cache.ACache;
import com.afeng.xf.utils.cache.AppSharePreferenceMgr;

import java.io.Serializable;

import butterknife.ButterKnife;
import butterknife.Unbinder;
import io.reactivex.disposables.CompositeDisposable;
import io.reactivex.disposables.Disposable;


/**
 * 普通的Activity基类
 */
public abstract class BaseActivity extends AppCompatActivity {

    protected String TAG;
    protected App mApp;
    protected Context mContext;
    protected ACache mCache;
    protected boolean mIsLoadedData = false;
    private CompositeDisposable mdisposables;


    Unbinder mbinder;


    public void xToastShow(String s) {
        xToastUtil.showToast(s);
    }

    public void xSnackBarShow(String s) {
        SnackbarUtils.showSnackBar(this, s);
    }

    public void xLogger(String s) {
        Logger.e(s);
    }


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(getLayoutId());
        mbinder = ButterKnife.bind(this);
        TAG = this.getClass().getSimpleName();
        mApp = App.getInstance();
        mContext = this;
        mCache = ACache.get(this);
        initView(savedInstanceState);
        setListener();
        processLogic(savedInstanceState);

    }

    /**
     * 初始化布局,返回layout
     */
    protected abstract int getLayoutId();

    /**
     * 初始化布局以及View控件
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


    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        if (item.getItemId() == android.R.id.home) {
            finish();
            return true;
        }
        return super.onOptionsItemSelected(item);
    }


    @SuppressWarnings("unchecked")
    public final <E extends View> E getViewById(@IdRes int id) {
        try {
            return (E) findViewById(id);
        } catch (ClassCastException ex) {
            Logger.w("视图元素类型强制失败！", ex);
            throw ex;
        }
    }

    @Override
    public void onDestroy() {
        super.onDestroy();
        mbinder.unbind();
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
    protected void onResume() {
        super.onResume();
        if (!mIsLoadedData) {
            mIsLoadedData = true;
            onLazyLoadOnce();
        }
    }

    /**
     * 懒加载一次。如果只想在对用户可见时才加载数据，并且只加载一次数据，在子类中重写该方法
     */
    protected void onLazyLoadOnce() {
    }


    @Override
    public void finish() {
        super.finish();
        GlideUtils.clearMemory(this);
        System.gc();
    }


    /**
     * 保存数据的方法，我们需要拿到保存数据的具体类型，然后根据类型调用不同的保存方法
     *
     * @param key
     * @param object
     */
    public void xPut(String key, Object object) {
        AppSharePreferenceMgr.put(mContext, key, object);
    }


    /**
     * 得到保存数据的方法，我们根据默认值得到保存的数据的具体类型，然后调用相对于的方法获取值
     *
     * @param key
     * @param defaultObject 默认值
     * @return
     */
    public Object xGet(String key, Object defaultObject) {

        return AppSharePreferenceMgr.get(mContext, key, defaultObject);
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
        intent.setClass(this, target);
        Bundle bundle = new Bundle();
        bundle.putSerializable(key, event);
        intent.putExtras(bundle);
        this.startActivity(intent);
    }


    /**
     * 存入对象并且返回Intent
     * @param key
     * @param data
     * @return
     */
    public Intent getIntentBySerializData(String key, Serializable data) {
        Intent intent = new Intent();
        Bundle bundle = new Bundle();
        bundle.putSerializable(key, data);
        intent.putExtras(bundle);
        return intent;
    }


    /**
     * 接收 来自 源Activity 的数据对象
     *
     * @param key
     * @return
     */
    public Object getSerializDataByKey(String key) {

        try {
            Intent intent = this.getIntent();
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
        intent.setClass(this, cls);
        if (bundle != null) {
            intent.putExtras(bundle);
        }
        startActivityForResult(intent, requestCode);
    }


}
