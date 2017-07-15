package com.afeng.xf.ui.contribute;

import android.content.Intent;

import java.io.Serializable;

/**
 * Created by Administrator on 2017/7/15.
 */

public class QinIuEvent implements Serializable {

    Intent data;

    public QinIuEvent(Intent data) {
        this.data = data;
    }

    public Intent getData() {
        return data;
    }

    public void setData(Intent data) {
        this.data = data;
    }
}
