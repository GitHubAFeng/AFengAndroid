package com.youth.xf.ui.demo.Login;

import android.Manifest;
import android.animation.Animator;
import android.animation.AnimatorListenerAdapter;
import android.annotation.TargetApi;
import android.app.ActivityOptions;
import android.content.Intent;
import android.os.Build;
import android.os.Bundle;
import android.support.annotation.RequiresApi;
import android.support.design.widget.FloatingActionButton;
import android.support.v4.app.ActivityOptionsCompat;
import android.support.v7.widget.CardView;
import android.text.TextUtils;
import android.transition.Explode;
import android.transition.Transition;
import android.transition.TransitionInflater;
import android.view.View;
import android.view.ViewAnimationUtils;
import android.view.animation.AccelerateInterpolator;
import android.widget.Button;
import android.widget.EditText;

import com.avos.avoscloud.AVException;
import com.avos.avoscloud.AVUser;
import com.avos.avoscloud.SignUpCallback;
import com.youth.xf.R;
import com.youth.xf.base.BaseActivity;
import com.youth.xf.ui.demo.MainActivity;
import com.youth.xf.utils.AFengUtils.AppPhoneMgr;
import com.youth.xf.utils.AFengUtils.AppValidationMgr;
import com.youth.xf.widget.hipermission.HiPermission;
import com.youth.xf.widget.hipermission.PermissionCallback;
import com.youth.xf.widget.hipermission.PermissionItem;
import com.youth.xf.widget.snackbarlight.Light;

import butterknife.BindView;

/**
 * Created by Administrator on 2017/6/27.
 */

public class RegisterActivity extends BaseActivity {


    @BindView(R.id.register_et_username)
    EditText mIdEtUsername;

    @BindView(R.id.register_et_password)
    EditText mIdEtPassword;

    @BindView(R.id.register_et_repeat_password)
    EditText mIdEtRepeatPassword;

    @BindView(R.id.register_btn_go)
    Button mIdBtnGo;

    @BindView(R.id.register_fab)
    FloatingActionButton mFab;

    @BindView(R.id.register_cv_add)
    CardView cvAdd;


    @Override
    protected int getLayoutId() {
        return R.layout.activity_register;
    }

    @Override
    protected void initView(Bundle savedInstanceState) {

        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.LOLLIPOP) {
            ShowEnterAnimation();
        }


    }

    @Override
    protected void setListener() {

        mFab.setOnClickListener(v -> {
            if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.LOLLIPOP) {
                animateRevealClose();
            }
        });

        mIdBtnGo.setOnClickListener(v -> registerUser());

    }

    @Override
    protected void processLogic(Bundle savedInstanceState) {
        initData();
    }


    @TargetApi(Build.VERSION_CODES.LOLLIPOP)
    private void ShowEnterAnimation() {
        Transition transition = TransitionInflater.from(this).inflateTransition(R.transition.resister_fab_transition);
        getWindow().setSharedElementEnterTransition(transition);

        transition.addListener(new Transition.TransitionListener() {
            @Override
            public void onTransitionStart(Transition transition) {
                cvAdd.setVisibility(View.GONE);
            }

            @Override
            public void onTransitionEnd(Transition transition) {
                transition.removeListener(this);
                animateRevealShow();
            }

            @Override
            public void onTransitionCancel(Transition transition) {

            }

            @Override
            public void onTransitionPause(Transition transition) {

            }

            @Override
            public void onTransitionResume(Transition transition) {

            }


        });
    }

    @TargetApi(Build.VERSION_CODES.LOLLIPOP)
    public void animateRevealShow() {
        Animator mAnimator = null;

        mAnimator = ViewAnimationUtils.createCircularReveal(cvAdd, cvAdd.getWidth() / 2, 0, mFab.getWidth() / 2, cvAdd.getHeight());

        mAnimator.setDuration(500);
        mAnimator.setInterpolator(new AccelerateInterpolator());
        mAnimator.addListener(new AnimatorListenerAdapter() {
            @Override
            public void onAnimationEnd(Animator animation) {
                super.onAnimationEnd(animation);
            }

            @Override
            public void onAnimationStart(Animator animation) {
                cvAdd.setVisibility(View.VISIBLE);
                super.onAnimationStart(animation);
            }
        });
        mAnimator.start();
    }

    @TargetApi(Build.VERSION_CODES.LOLLIPOP)
    public void animateRevealClose() {
        Animator mAnimator = ViewAnimationUtils.createCircularReveal(cvAdd, cvAdd.getWidth() / 2, 0, cvAdd.getHeight(), mFab.getWidth() / 2);
        mAnimator.setDuration(500);
        mAnimator.setInterpolator(new AccelerateInterpolator());
        mAnimator.addListener(new AnimatorListenerAdapter() {
            @Override
            public void onAnimationEnd(Animator animation) {
                cvAdd.setVisibility(View.INVISIBLE);
                super.onAnimationEnd(animation);
                mFab.setImageResource(R.drawable.login_plus);
                RegisterActivity.super.onBackPressed();
            }

            @Override
            public void onAnimationStart(Animator animation) {
                super.onAnimationStart(animation);
            }
        });
        mAnimator.start();
    }


    @Override
    public void onBackPressed() {
        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.LOLLIPOP) {
            animateRevealClose();
        }
    }


    private void registerUser() {

        String phoneNum = mIdEtUsername.getText().toString();

        if (!AppValidationMgr.isPhone(phoneNum)) {
            Light.error(mIdBtnGo, "请填写正确的手机号码", Light.LENGTH_SHORT);
            return;
        }

        String possWord = mIdEtPassword.getText().toString();
        String RepeatPossWord = mIdEtRepeatPassword.getText().toString();

        if (!RepeatPossWord.equals(possWord)) {
            Light.error(mIdBtnGo, "两次密码不一致", Light.LENGTH_SHORT);
            return;
        }

        AVUser user = new AVUser();// 新建 AVUser 对象实例
        user.setUsername(phoneNum);// 设置用户名
        user.setPassword(possWord);// 设置密码
//        user.setEmail("tom@leancloud.cn");// 设置邮箱
        user.signUpInBackground(new SignUpCallback() {
            @Override
            public void done(AVException e) {
                if (e == null) {
                    // 注册成功
                    Light.success(mIdBtnGo, "注册成功", Light.LENGTH_SHORT);
                } else {
                    // 失败的原因可能有多种，常见的是用户名已经存在。
                }
            }
        });


    }


    private void initData() {

        HiPermission.create(this)
                .title("权限申请")
                .msg("获取本机手机号码用于注册账号")
                .animStyle(R.style.PermissionAnimFade)
                .style(R.style.PermissionDefaultNormalStyle)
                .checkSinglePermission(Manifest.permission.READ_PHONE_STATE, new PermissionCallback() {
                    @Override
                    public void onClose() {

                    }

                    @Override
                    public void onFinish() {

                        String locPhoneNum = AppPhoneMgr.getInstance().getPhoneNum(getApplicationContext());

                        if (!TextUtils.isEmpty(locPhoneNum)) {

                            mIdEtUsername.setText(locPhoneNum);
                        }

                    }

                    @Override
                    public void onDeny(String permission, int position) {

                    }

                    @Override
                    public void onGuarantee(String permission, int position) {

                    }
                });

    }


}
