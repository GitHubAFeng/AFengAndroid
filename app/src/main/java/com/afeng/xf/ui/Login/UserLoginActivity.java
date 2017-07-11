package com.afeng.xf.ui.Login;

import android.app.ActivityOptions;
import android.content.Intent;
import android.os.Build;
import android.os.Bundle;
import android.support.design.widget.CoordinatorLayout;
import android.support.design.widget.FloatingActionButton;
import android.support.v4.app.ActivityOptionsCompat;
import android.text.InputType;
import android.text.TextUtils;
import android.transition.Explode;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.TextView;

import com.afeng.xf.utils.AFengUtils.PerfectClickListener;
import com.afollestad.materialdialogs.MaterialDialog;
import com.avos.avoscloud.AVException;
import com.avos.avoscloud.AVUser;
import com.avos.avoscloud.LogInCallback;
import com.afeng.xf.R;
import com.afeng.xf.base.BaseActivity;
import com.afeng.xf.ui.MainActivity;
import com.afeng.xf.utils.AFengUtils.AppValidationMgr;
import com.afeng.xf.widget.snackbarlight.Light;
import com.avos.avoscloud.RequestPasswordResetCallback;

import org.greenrobot.eventbus.EventBus;

import butterknife.BindView;


/**
 * Created by AFeng on 2017/3/26.
 */

public class UserLoginActivity extends BaseActivity {


    @BindView(R.id.login_et_username)
    EditText mIdEtUsername;

    @BindView(R.id.login_et_password)
    EditText mIdEtPassword;

    @BindView(R.id.login_btn_go)
    Button mIdBtnGo;

    //忘记密码
    @BindView(R.id.login_btn_forget)
    TextView mForget;


    @BindView(R.id.login_fab)
    FloatingActionButton mFab;

    @BindView(R.id.login_coordinatorlayout)
    CoordinatorLayout mCoordinatorLayout;


    @Override
    protected int getLayoutId() {
        return R.layout.activity_login;
    }

    @Override
    protected void initView(Bundle savedInstanceState) {

        // 转场动画
        if (android.os.Build.VERSION.SDK_INT >= android.os.Build.VERSION_CODES.LOLLIPOP) {
            Explode explode = new Explode();
            explode.setDuration(500);
            getWindow().setExitTransition(explode);
            getWindow().setEnterTransition(explode);
        }


    }

    @Override
    protected void setListener() {


        mFab.setOnClickListener(view ->
        {
            // 转场动画只支持5.0以上，更多实例可参考  https://github.com/lgvalle/Material-Animations
            // http://blog.csdn.net/a396901990/article/details/40187203/
            if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.LOLLIPOP) {

                getWindow().setExitTransition(null);
                getWindow().setEnterTransition(null);

                //共享元素转换效果
                ActivityOptions options =
                        ActivityOptions.makeSceneTransitionAnimation(this, mFab, mFab.getTransitionName());
                startActivity(new Intent(this, RegisterActivity.class), options.toBundle());
            } else {
                startActivity(new Intent(this, RegisterActivity.class));
            }
        });


        mIdBtnGo.setOnClickListener((View v) -> userLogin());

        mForget.setOnClickListener(new PerfectClickListener() {
            @Override
            protected void onNoDoubleClick(View v) {
                forgetPassWord();
            }
        });

    }


    @Override
    protected void onPause() {
        super.onPause();
        String phoneNum = mIdEtUsername.getText().toString();
        String possWord = mIdEtPassword.getText().toString();

        xPut("phoneNum", phoneNum);
        xPut("possWord", possWord);

    }

    @Override
    protected void onResume() {
        super.onResume();

        String phoneNum = (String) xGet("phoneNum", "");
        String possWord = (String) xGet("possWord", "");

        //操作view实例
        //恢复输入框里面的内容（设置） set
        mIdEtUsername.setText(phoneNum);
        mIdEtPassword.setText(possWord);

    }


    @Override
    protected void processLogic(Bundle savedInstanceState) {

    }


    private void userLogin() {

        String phoneNum = mIdEtUsername.getText().toString();

        if (!AppValidationMgr.isPhone(phoneNum)) {
            Light.error(mCoordinatorLayout, "请填写正确的手机号码", Light.LENGTH_SHORT).show();
            return;
        }

        String possWord = mIdEtPassword.getText().toString();

        AVUser.logInInBackground(phoneNum, possWord, new LogInCallback<AVUser>() {
            @Override
            public void done(AVUser avUser, AVException e) {
                if (e == null) {

                    goToMainActivity(new LoginEvent(phoneNum, possWord, true));

                } else {
                    switch (e.getCode()) {
                        case 210:
                            Light.error(mCoordinatorLayout, "用户名和密码不匹配", Light.LENGTH_SHORT).show();
                            break;
                        case 211:
                            Light.error(mCoordinatorLayout, "没有此用户", Light.LENGTH_SHORT).show();
                            break;
                        case 219:
                            Light.error(mCoordinatorLayout, "登录失败次数超过限制，请15分钟后再试，或者通过忘记密码重设密码", Light.LENGTH_SHORT).show();
                            break;
                    }
                }

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


    private void forgetPassWord() {

        new MaterialDialog.Builder(this)
                .title("找回密码")
                .content("请填写账号绑定的电子邮箱地址")
                .inputType(InputType.TYPE_TEXT_VARIATION_EMAIL_ADDRESS)
                .inputRange(2, 20)
                .positiveText("确定")
                .input(
                        "",
                        "",
                        false,
                        (dialog, input) -> {
                            if (!TextUtils.isEmpty(input.toString())) {

                                AVUser.requestPasswordResetInBackground(input.toString().trim(), new RequestPasswordResetCallback() {
                                    @Override
                                    public void done(AVException e) {
                                        if (e == null) {
                                            xToastShow("重置密码邮件已发送至" + input.toString().trim() + "，请注意及时确定");
                                        } else {
                                            if (e.getCode() == 205) {
                                                xToastShow("找不到此电子邮箱地址绑定的用户");
                                            }

                                            e.printStackTrace();
                                        }
                                    }
                                });
                            }

                        }
                )
                .show();
    }


}
