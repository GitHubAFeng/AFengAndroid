package com.afeng.xf.widget.button;

import android.content.Context;
import android.os.Handler;
import android.os.Message;
import android.support.v7.widget.AppCompatButton;
import android.util.AttributeSet;
import android.view.View;


import java.util.Timer;
import java.util.TimerTask;

/**
 * Created by Administrator on 2017/7/6.
 * 倒计时按钮
 */

public class CountDownTimerButton extends AppCompatButton implements View.OnClickListener {

    private Context mContext;
    private OnClickListener mOnClickListener;
    private Timer mTimer;//调度器
    private TimerTask mTask;
    private long duration = 60000;//倒计时时长 设置默认60秒
    private long temp_duration;
    private String clickBeffor = "倒计时开始";//点击前
    private String clickAfter = "秒后重新开始";//点击后
    private boolean isStart = false;  //点击是否可以开始

    public boolean isStart() {
        return isStart;
    }

    public void setStart(boolean start) {
        isStart = start;
    }

    public long getDuration() {
        return duration;
    }

    public void setDuration(long duration) {
        this.duration = duration;
    }

    public String getClickBeffor() {
        return clickBeffor;
    }

    public void setClickBeffor(String clickBeffor) {
        this.clickBeffor = clickBeffor;
    }

    public String getClickAfter() {
        return clickAfter;
    }

    public void setClickAfter(String clickAfter) {
        this.clickAfter = clickAfter;
    }


    public CountDownTimerButton(Context context) {
        super(context);
        mContext = context;
        setOnClickListener(this);
    }

    public CountDownTimerButton(Context context, AttributeSet attrs) {
        super(context, attrs);
        mContext = context;
        setOnClickListener(this);
    }

    private Handler mHandler = new Handler() {
        @Override
        public void handleMessage(Message msg) {
            CountDownTimerButton.this.setText(temp_duration / 1000 + getClickAfter());
            temp_duration -= 1000;
            if (temp_duration < 0) {//倒计时结束
                CountDownTimerButton.this.setEnabled(true);
                CountDownTimerButton.this.setText(getClickBeffor());
                stopTimer();
            }
        }
    };

    @Override
    public void setOnClickListener(OnClickListener onClickListener) {//提供外部访问方法
        if (onClickListener instanceof CountDownTimerButton) {
            super.setOnClickListener(onClickListener);
        } else {
            this.mOnClickListener = onClickListener;
        }
    }

    @Override
    public void onClick(View view) {
        if (mOnClickListener != null) {
            mOnClickListener.onClick(view);
        }

        if (isStart()) {
            startTimer();
        }
    }

    //计时开始
    public void startTimer() {
        temp_duration = getDuration();
        CountDownTimerButton.this.setEnabled(false);
        mTimer = new Timer();
        mTask = new TimerTask() {
            @Override
            public void run() {
                mHandler.sendEmptyMessage(0x01);
            }
        };
        mTimer.schedule(mTask, 0, 1000);//调度分配，延迟0秒，时间间隔为1秒
    }

    //计时结束
    private void stopTimer() {
        if (mTask != null) {
            mTask.cancel();
            mTask = null;
        }
        if (mTimer != null) {
            mTimer.cancel();
            mTimer = null;
        }
    }
}
