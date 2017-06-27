package com.youth.xf.ui.demo.Login;

import android.app.ActivityOptions;
import android.content.Intent;
import android.os.Build;
import android.os.Bundle;
import android.support.design.widget.CoordinatorLayout;
import android.support.design.widget.FloatingActionButton;
import android.support.v4.app.ActivityOptionsCompat;
import android.transition.Explode;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.TextView;

import com.avos.avoscloud.AVException;
import com.avos.avoscloud.AVUser;
import com.avos.avoscloud.LogInCallback;
import com.youth.xf.R;
import com.youth.xf.base.AFengActivity;
import com.youth.xf.base.BaseActivity;
import com.youth.xf.ui.demo.MainActivity;
import com.youth.xf.utils.AFengUtils.AppValidationMgr;
import com.youth.xf.widget.snackbarlight.Light;

import org.greenrobot.eventbus.EventBus;

import butterknife.BindView;
import butterknife.ButterKnife;
import butterknife.OnClick;

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

        String phoneNum = (String) xGet("phoneNum", "默认值");
        String possWord = (String) xGet("possWord", "默认值");

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


}
