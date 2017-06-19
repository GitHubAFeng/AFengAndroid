package com.youth.xf.base;

import android.os.Bundle;
import android.support.annotation.IdRes;
import android.view.MenuItem;
import android.view.View;

import com.orhanobut.logger.Logger;
import com.youth.xf.utils.GlideHelper.GlideUtils;

import butterknife.ButterKnife;
import butterknife.Unbinder;
import io.reactivex.disposables.CompositeDisposable;
import io.reactivex.disposables.Disposable;
import me.yokeyword.fragmentation.SupportActivity;

/**
 * 适合拥有多个Fragment的Activity基类，方便管理
 */
public abstract class AFengActivity extends SupportActivity {

    protected String TAG;
    protected App mApp;
    protected boolean mIsLoadedData = false;
    private CompositeDisposable mdisposables;


    Unbinder mbinder;


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(getLayoutId());
        mbinder = ButterKnife.bind(this);
        TAG = this.getClass().getSimpleName();
        mApp = App.getInstance();
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

}
