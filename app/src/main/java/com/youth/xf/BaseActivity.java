package com.youth.xf;

import android.os.Bundle;
import android.support.annotation.IdRes;
import android.view.MenuItem;
import android.view.View;

import com.youth.xframe.base.XActivity;
import com.youth.xframe.utils.log.XLog;

import rx.Subscription;
import rx.subscriptions.CompositeSubscription;

/**
 * 必须继承XActivity，你也可以每个都继承XActivity，这里进行再次封装是为了便于你维护和增加你需要的方法
 */
public abstract class BaseActivity extends XActivity {

    private CompositeSubscription mCompositeSubscription;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);

    }

    @Override
    public void setContentView(int layoutResID) {
        super.setContentView(layoutResID);

    }


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
            XLog.w("视图元素类型强制失败！", ex);
            throw ex;
        }
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

    public void addSubscription(Subscription s) {
        if (this.mCompositeSubscription == null) {
            this.mCompositeSubscription = new CompositeSubscription();
        }
        this.mCompositeSubscription.add(s);
    }

}
