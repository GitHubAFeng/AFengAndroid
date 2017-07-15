package com.afeng.xf.ui.contribute;

import android.os.Bundle;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;

import com.afeng.xf.R;
import com.afeng.xf.base.BaseFragment;
import com.afeng.xf.ui.constants.Constants;
import com.afeng.xf.utils.AFengUtils.PerfectClickListener;
import com.afollestad.materialdialogs.MaterialDialog;
import com.avos.avoscloud.AVException;
import com.avos.avoscloud.AVObject;
import com.avos.avoscloud.SaveCallback;

import java.util.Arrays;

import butterknife.BindView;

/**
 * Created by Administrator on 2017/7/13.
 */

public class HomeRvItemContributeFragment extends BaseFragment {


    @BindView(R.id.contribute_home_item_jsbtn)
    Button mJsBtn;

    @BindView(R.id.contribute_home_item_go)
    Button mButton;

    @BindView(R.id.contribute_home_item_title)
    EditText mTitle;

    @BindView(R.id.contribute_home_item_desc)
    EditText mDesc;

    @BindView(R.id.contribute_home_item_img)
    EditText mImg;

    @BindView(R.id.contribute_home_item_url)
    EditText mUrl;

    @BindView(R.id.contribute_home_item_js)
    EditText mJS;


    private static HomeRvItemContributeFragment instance;

    public static HomeRvItemContributeFragment getInstance() {
        if (instance == null) {
            synchronized (HomeRvItemContributeFragment.class) {
                if (instance == null) {
                    instance = new HomeRvItemContributeFragment();
                }
            }
        }
        return instance;
    }


    @Override
    protected int getLayoutId() {
        return R.layout.fragment_home_item_contribute;
    }

    @Override
    protected void initView(Bundle savedInstanceState) {

    }

    @Override
    protected void setListener() {
        mButton.setOnClickListener(new PerfectClickListener() {
            @Override
            protected void onNoDoubleClick(View v) {
                goFunc();
            }
        });

        mJsBtn.setOnClickListener(v -> clooseJS());


    }

    @Override
    protected void processLogic(Bundle savedInstanceState) {

    }

    @Override
    protected void onLazyLoadOnce() {

    }

    @Override
    protected void onVisibleToUser() {

    }

    @Override
    protected void onInvisibleToUser() {

    }


    private void clooseJS() {

        new MaterialDialog.Builder(mActivity)
                .title("选择JS模板")
                .items(Arrays.asList("A站", "B站", "网易云"))
                .itemsCallback((MaterialDialog dialog, View view, int which, CharSequence text) -> {
                    switch (which) {
                        case 0:
                            mJS.post(() -> mJS.setText(Constants.ACFUN_WEI_JS_CODE));
                            break;

                        case 1:
                            mJS.post(() -> mJS.setText(Constants.BILI_JS_CODE));
                            break;

                        case 2:
                            mJS.post(() -> mJS.setText(Constants.WANGYI163_JS_CODE));
                            break;
                    }

                })
                .show();
    }


    private void goFunc() {
        xToastShow("正在保存中，请不要退出页面");
        String title = mTitle.getText().toString().trim();
        String desc = mDesc.getText().toString().trim();
        String img = mImg.getText().toString().trim();
        String url = mUrl.getText().toString().trim();
        String jscode = mJS.getText().toString().trim();


        AVObject todoFolder = new AVObject("HomeListItem");// 构建对象
        todoFolder.put("desc", desc);
        todoFolder.put("title", title);
        todoFolder.put("url", url);
        todoFolder.put("img", img);
        todoFolder.put("jsCode", jscode);
        todoFolder.saveInBackground(new SaveCallback() {
            @Override
            public void done(AVException e) {
                if (e == null)
                    xToastShow("保存成功");
                else
                    xToastShow("保存失败");
            }
        });


    }


}
