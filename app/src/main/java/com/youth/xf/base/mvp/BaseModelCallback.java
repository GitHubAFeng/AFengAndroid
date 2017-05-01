package com.youth.xf.base.mvp;

import java.util.List;

/**
 * Created by Administrator on 2017/4/28.
 */

public interface BaseModelCallback<T> {

    void onSuccess(List<T> data);

    void onFailed();

}
