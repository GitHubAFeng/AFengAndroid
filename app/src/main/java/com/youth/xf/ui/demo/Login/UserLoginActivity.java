package com.youth.xf.ui.demo.Login;

import android.os.Bundle;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;

import com.youth.xf.R;
import com.youth.xf.base.AFengActivity;

import butterknife.BindView;
import butterknife.ButterKnife;
import butterknife.OnClick;

/**
 * Created by AFeng on 2017/3/26.
 */

public class UserLoginActivity extends AFengActivity{
//    private UserLoginPresenter mUserLoginPresenter = new UserLoginPresenter(this);

    @BindView(R.id.id_et_username)
    EditText mIdEtUsername;
    @BindView(R.id.id_et_password)
    EditText mIdEtPassword;
    @BindView(R.id.id_btn_login)
    Button mIdBtnLogin;
    @BindView(R.id.id_btn_clear)
    Button mIdBtnClear;

    @Override
    protected int getLayoutId() {
        return R.layout.activity_user_login;
    }

    @Override
    protected void initView(Bundle savedInstanceState) {

    }

    @Override
    protected void setListener() {

    }

    @Override
    protected void processLogic(Bundle savedInstanceState) {

    }



    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        // TODO: add setContentView(...) invocation
        ButterKnife.bind(this);
    }

    @OnClick({R.id.id_btn_login, R.id.id_btn_clear})
    public void onViewClicked(View view) {
        switch (view.getId()) {
            case R.id.id_btn_login:

                break;
            case R.id.id_btn_clear:

                break;
        }
    }


}
