package com.youth.xf.base.mvp;

/**
 * Created by Administrator on 2017/4/27.
 */

public interface BaseView<T> {
    void setPresenter(T presenter);
    boolean isActive();
}
