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
import android.support.design.widget.CoordinatorLayout;
import android.support.design.widget.FloatingActionButton;
import android.support.design.widget.Snackbar;
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
import com.avos.avoscloud.AVFile;
import com.avos.avoscloud.AVObject;
import com.avos.avoscloud.AVQuery;
import com.avos.avoscloud.AVUser;
import com.avos.avoscloud.GetCallback;
import com.avos.avoscloud.SaveCallback;
import com.avos.avoscloud.SignUpCallback;
import com.youth.xf.R;
import com.youth.xf.base.BaseActivity;
import com.youth.xf.ui.demo.MainActivity;
import com.youth.xf.utils.AFengUtils.AppPhoneMgr;
import com.youth.xf.utils.AFengUtils.AppValidationMgr;
import com.youth.xf.utils.cache.AppSharePreferenceMgr;
import com.youth.xf.widget.hipermission.HiPermission;
import com.youth.xf.widget.hipermission.PermissionCallback;
import com.youth.xf.widget.hipermission.PermissionItem;
import com.youth.xf.widget.snackbarlight.Light;

import org.greenrobot.eventbus.EventBus;

import java.util.HashMap;

import butterknife.BindView;

/**
 * Created by Administrator on 2017/6/27.
 */

public class RegisterActivity extends BaseActivity {

    @BindView(R.id.register_coordinator_layout)
    CoordinatorLayout mCoordinatorLayout;

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

    @Override
    protected void onPause() {
        super.onPause();
        String phoneNum = mIdEtUsername.getText().toString();
        String possWord = mIdEtPassword.getText().toString();
        String RepeatPossWord = mIdEtRepeatPassword.getText().toString();

        xPut("phoneNum", phoneNum);
        xPut("possWord", possWord);
        xPut("RepeatPossWord", RepeatPossWord);

        xLogger("onPause：" + phoneNum);
    }

    @Override
    protected void onResume() {
        super.onResume();

        String phoneNum = (String) xGet("phoneNum", "默认值");
        String possWord = (String) xGet("possWord", "默认值");
        String RepeatPossWord = (String) xGet("RepeatPossWord", "默认值");

        //操作view实例
        //恢复输入框里面的内容（设置） set
        mIdEtUsername.setText(phoneNum);
        mIdEtPassword.setText(possWord);
        mIdEtRepeatPassword.setText(RepeatPossWord);
        xLogger("onResume：" + phoneNum);
    }


//    经测试，onSaveInstanceState会被调用的条件是:
//
//    条件一.从当前activityA启动ActivityB时，在onPause之后onStop之前调用，
//
//    条件二.当前activtiy未被主动销毁（比如调用finish()）,而是被压入栈中。

    //保存用户输入的数据
    @Override
    protected void onSaveInstanceState(Bundle outState) {
        super.onSaveInstanceState(outState);

        String phoneNum = mIdEtUsername.getText().toString();
        String possWord = mIdEtPassword.getText().toString();
        String RepeatPossWord = mIdEtRepeatPassword.getText().toString();
        //将获取到内容保存起来 put
        outState.putString("phoneNum", phoneNum);
        outState.putString("possWord", possWord);
        outState.putString("RepeatPossWord", RepeatPossWord);

        xLogger("onSaveInstanceState：" + phoneNum);
    }

    // 只有在异常重启时才会调用
    //恢复用户输入的数据
    @Override
    protected void onRestoreInstanceState(Bundle savedInstanceState) {
        super.onRestoreInstanceState(savedInstanceState);

        //恢复用户输入的数据
        //先获取数据 get
        String phoneNum = savedInstanceState.getString("phoneNum");
        String possWord = savedInstanceState.getString("possWord");
        String RepeatPossWord = savedInstanceState.getString("RepeatPossWord");

        //操作view实例
        //恢复输入框里面的内容（设置） set
        mIdEtUsername.setText(phoneNum);
        mIdEtPassword.setText(possWord);
        mIdEtRepeatPassword.setText(RepeatPossWord);

        xLogger("onRestoreInstanceState：" + phoneNum);

    }


    private void registerUser() {

        String phoneNum = mIdEtUsername.getText().toString();

        if (!AppValidationMgr.isPhone(phoneNum)) {
            Light.error(mCoordinatorLayout, "请填写正确的手机号码", Light.LENGTH_SHORT).show();
            return;
        }


        String possWord = mIdEtPassword.getText().toString();
        String RepeatPossWord = mIdEtRepeatPassword.getText().toString();

        if (!RepeatPossWord.equals(possWord)) {
            Light.error(mCoordinatorLayout, "密码输入不一致", Light.LENGTH_SHORT).show();
            return;
        }

        //  关联表 一对一
        AVFile file = new AVFile("avatar_test.png", "http://oki2v8p4s.bkt.clouddn.com/avatar_test.png", new HashMap<String, Object>());
        AVObject info = new AVObject("UserInfo");// 构建对象
        info.put("nickname", "昵称");
        info.put("desc", "签名");
        info.put("avatar", file);

        AVUser user = new AVUser();// 新建 AVUser 对象实例
        user.setUsername(phoneNum);// 设置用户名
        user.setPassword(possWord);// 设置密码
        user.setMobilePhoneNumber(phoneNum); //手机号
        user.put("UserInfo", info); //在默认用户表_User 中关联 UserInfo 表
        user.signUpInBackground(new SignUpCallback() {
            @Override
            public void done(AVException e) {
                if (e == null) {
                    // 注册成功
                    goToMainActivity(new LoginEvent(phoneNum, possWord, true));

                } else {
                    // 失败的原因可能有多种，常见的是用户名已经存在。
                    //用户名已经被另一个用户注册，错误代码 202
                    //邮件 email 和手机号码 mobilePhoneNumber 字段也要求在各自的列中不能有重复值出现，否则会出现 203、214 错误。
                    switch (e.getCode()) {
                        case 202:
                            Light.error(mCoordinatorLayout, "手机号已经被占用", Light.LENGTH_SHORT).show();
                            break;
                        case 214:
                            Light.error(mCoordinatorLayout, "手机号已经被占用", Light.LENGTH_SHORT).show();
                            break;
                    }
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


    private void goToMainActivity(LoginEvent event) {

        EventBus.getDefault().post(event);

        // 启动普通转场动画 , 记得对方Activity要有动画设置
        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.LOLLIPOP) {

            Explode explode = new Explode();
            explode.setDuration(500);

            getWindow().setExitTransition(explode);
            getWindow().setEnterTransition(explode);

            ActivityOptionsCompat oc2 = ActivityOptionsCompat.makeSceneTransitionAnimation(this);
            Intent i2 = new Intent(this, MainActivity.class);
            startActivity(i2, oc2.toBundle());

        } else {
            startActivity(new Intent(this, MainActivity.class));
        }

        finish();

    }


}
