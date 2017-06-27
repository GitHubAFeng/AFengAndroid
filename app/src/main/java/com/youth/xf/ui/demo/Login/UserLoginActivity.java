package com.youth.xf.ui.demo.Login;

import android.app.ActivityOptions;
import android.content.Intent;
import android.os.Build;
import android.os.Bundle;
import android.support.design.widget.FloatingActionButton;
import android.support.v4.app.ActivityOptionsCompat;
import android.transition.Explode;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.TextView;

import com.youth.xf.R;
import com.youth.xf.base.AFengActivity;
import com.youth.xf.base.BaseActivity;
import com.youth.xf.ui.demo.MainActivity;

import butterknife.BindView;
import butterknife.ButterKnife;
import butterknife.OnClick;

/**
 * Created by AFeng on 2017/3/26.
 */

public class UserLoginActivity extends BaseActivity{


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


        mIdBtnGo.setOnClickListener(v -> {
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

        });

    }

    @Override
    protected void processLogic(Bundle savedInstanceState) {

    }




}
