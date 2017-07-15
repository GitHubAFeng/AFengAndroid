package com.afeng.xf.ui.contribute;

import android.os.Bundle;
import android.support.annotation.Nullable;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.support.v7.widget.StaggeredGridLayoutManager;
import android.text.TextUtils;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.ProgressBar;
import android.widget.TextView;

import com.afeng.xf.R;
import com.afeng.xf.base.BaseFragment;
import com.afeng.xf.ui.constants.Constants;
import com.afeng.xf.ui.data.HomeBannerItem;
import com.afeng.xf.utils.AFengUtils.PerfectClickListener;
import com.afollestad.materialdialogs.MaterialDialog;
import com.avos.avoscloud.AVException;
import com.avos.avoscloud.AVObject;
import com.avos.avoscloud.AVQuery;
import com.avos.avoscloud.AVUser;
import com.avos.avoscloud.FindCallback;
import com.avos.avoscloud.GetCallback;
import com.avos.avoscloud.SaveCallback;
import com.bumptech.glide.Glide;
import com.bumptech.glide.load.engine.DiskCacheStrategy;
import com.chad.library.adapter.base.BaseQuickAdapter;
import com.chad.library.adapter.base.BaseViewHolder;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;

import butterknife.BindView;

/**
 * Created by Administrator on 2017/7/13.
 */

public class HomeBannerContributeFragment extends BaseFragment {


    @BindView(R.id.contribute_hoem_banner_rv)
    RecyclerView mRecyclerView;

    @BindView(R.id.contribute_hoem_banner_progressbar)
    ProgressBar mProgressBar;

    List<HomeContributeBannerBean> HomeBannerItemList = new ArrayList<>();

    itemAdapter mAdapter;


    private static HomeBannerContributeFragment instance;

    public static HomeBannerContributeFragment getInstance() {
        if (instance == null) {
            synchronized (HomeBannerContributeFragment.class) {
                if (instance == null) {
                    instance = new HomeBannerContributeFragment();
                }
            }
        }
        return instance;
    }


    @Override
    protected int getLayoutId() {
        return R.layout.fragment_contribute_home_banner;
    }

    @Override
    protected void initView(Bundle savedInstanceState) {
        initRV();

    }

    @Override
    protected void setListener() {

    }

    @Override
    protected void processLogic(Bundle savedInstanceState) {

    }

    @Override
    protected void onLazyLoadOnce() {

    }

    @Override
    protected void onVisibleToUser() {

        if (HomeBannerItemList.size() == 0) {
            initData();
        }
    }

    @Override
    protected void onInvisibleToUser() {

    }


    private void initRV() {

        mAdapter = new itemAdapter(R.layout.item_contribute_home_banner, HomeBannerItemList);
        mAdapter.setUpFetchEnable(false);  //关闭自带的下拉
        mAdapter.setEnableLoadMore(false);
        //设置布局管理器
        mRecyclerView.setLayoutManager(new LinearLayoutManager(mActivity));
        mRecyclerView.setAdapter(mAdapter);
    }


    private void initData() {

        mProgressBar.setVisibility(View.VISIBLE);

        AVQuery<AVObject> query = new AVQuery<>("HomeBannerItem");
        query.findInBackground(new FindCallback<AVObject>() {
            @Override
            public void done(List<AVObject> list, AVException e) {
                if (e == null) {
                    if (list != null && list.size() > 0) {
                        for (AVObject avObject : list) {
                            String desc = avObject.getString("desc");
                            String url = avObject.getString("url");
                            String img = avObject.getString("img");
                            String js = avObject.getString("jsCode");
                            String id = avObject.getObjectId();

                            HomeContributeBannerBean homeBannerItem = new HomeContributeBannerBean();
                            homeBannerItem.setDesc(desc);
                            homeBannerItem.setImg(img);
                            homeBannerItem.setUrl(url);
                            homeBannerItem.setJsCode(js);
                            homeBannerItem.setObjectId(id);

                            HomeBannerItemList.add(homeBannerItem);
                            mAdapter.notifyDataSetChanged();
                            mProgressBar.setVisibility(View.GONE);

                        }
                    }
                }
            }
        });

    }


    //列表适配器
    class itemAdapter extends BaseQuickAdapter<HomeContributeBannerBean, BaseViewHolder> {


        public itemAdapter(int layoutResId, @Nullable List<HomeContributeBannerBean> data) {
            super(layoutResId, data);
        }

        @Override
        protected void convert(BaseViewHolder helper, HomeContributeBannerBean item) {

            EditText desc = helper.getView(R.id.contribute_home_banner_desc);
            EditText img = helper.getView(R.id.contribute_home_banner_img);
            EditText url = helper.getView(R.id.contribute_home_banner_url);
            EditText js = helper.getView(R.id.contribute_home_banner_js);
            Button jsBtn = helper.getView(R.id.contribute_home_banner_jsbtn);
            Button goBtn = helper.getView(R.id.contribute_home_banner_go);

            desc.setText(item.getDesc());
            img.setText(item.getImg());
            url.setText(item.getUrl());
            js.setText(item.getJsCode());


            jsBtn.setOnClickListener(new PerfectClickListener() {
                @Override
                protected void onNoDoubleClick(View v) {
                    new MaterialDialog.Builder(mActivity)
                            .title("选择JS模板")
                            .items(Arrays.asList("A站", "B站", "网易云"))
                            .itemsCallback((MaterialDialog dialog, View view, int which, CharSequence text) -> {
                                switch (which) {
                                    case 0:
                                        js.post(() -> js.setText(Constants.ACFUN_WEI_JS_CODE));
                                        break;

                                    case 1:
                                        js.post(() -> js.setText(Constants.BILI_JS_CODE));
                                        break;

                                    case 2:
                                        js.post(() -> js.setText(Constants.WANGYI163_JS_CODE));
                                        break;
                                }

                            })
                            .show();
                }
            });


            goBtn.setOnClickListener(new PerfectClickListener() {
                @Override
                protected void onNoDoubleClick(View v) {

                    xToastShow("正在更新中，请不要退出页面");
                    String img_temp = img.getText().toString().trim();
                    String desc_temp = desc.getText().toString().trim();
                    String url_temp = url.getText().toString().trim();
                    String js_temp = js.getText().toString().trim();

                    AVObject todoFolder = AVObject.createWithoutData("HomeBannerItem", item.getObjectId());

                    if (!TextUtils.isEmpty(img_temp)) {
                        if (!item.getImg().equals(img_temp)) {
                            todoFolder.put("img", img_temp);
                        }
                    }

                    if (!TextUtils.isEmpty(desc_temp)) {
                        if (!item.getDesc().equals(desc_temp)) {
                            todoFolder.put("desc", desc_temp);
                        }
                    }

                    if (!TextUtils.isEmpty(url_temp)) {
                        if (!item.getUrl().equals(url_temp)) {
                            todoFolder.put("url", url_temp);
                        }
                    }

                    if (!TextUtils.isEmpty(js_temp)) {
                        if (!item.getJsCode().equals(js_temp)) {
                            todoFolder.put("jsCode", js_temp);
                        }
                    }


                    todoFolder.saveInBackground(new SaveCallback() {
                        @Override
                        public void done(AVException e) {
                            if (e == null)
                                xToastShow("更新成功");
                            else
                                xToastShow("更新失败");
                        }
                    });
                }
            });
        }
    }


}
