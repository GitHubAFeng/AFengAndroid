package com.youth.xf.base;

import android.os.Bundle;
import android.support.annotation.IdRes;
import android.support.v7.app.AppCompatActivity;
import android.view.MenuItem;
import android.view.View;

import com.orhanobut.logger.Logger;
import com.youth.xf.App;
import com.youth.xf.utils.imageloader.GlideUtils;

import io.reactivex.disposables.CompositeDisposable;
import io.reactivex.disposables.Disposable;


public abstract class AFengActivity extends AppCompatActivity {

    protected String TAG;
    protected App mApp;
    //    protected Engine mEngine;

    private CompositeDisposable mdisposables;


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(getLayoutId());
        TAG = this.getClass().getSimpleName();
        mApp = App.getInstance();
//        mEngine = mApp.getEngine();
        initView(savedInstanceState);
        setListener();
        processLogic(savedInstanceState);

    }

    //获取布局文件
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
    public void finish() {
        super.finish();
        GlideUtils.clearMemory(this);
        System.gc();
    }

}