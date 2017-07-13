package com.afeng.xf.ui.contribute;

import android.content.Intent;
import android.graphics.Bitmap;
import android.os.Bundle;
import android.support.annotation.Nullable;
import android.support.v7.widget.CardView;
import android.support.v7.widget.RecyclerView;
import android.support.v7.widget.StaggeredGridLayoutManager;
import android.text.InputType;
import android.text.TextUtils;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ImageView;
import android.widget.TextView;

import com.afeng.xf.R;
import com.afeng.xf.base.BaseFragment;
import com.afeng.xf.ui.meizi.MeiZiBigImageActivity;
import com.afeng.xf.ui.meizi.MeiziEvent;
import com.afeng.xf.utils.AFengUtils.DensityUtil;
import com.afeng.xf.utils.AFengUtils.PerfectClickListener;
import com.afollestad.materialdialogs.MaterialDialog;
import com.avos.avoscloud.AVException;
import com.avos.avoscloud.AVObject;
import com.avos.avoscloud.AVUser;
import com.avos.avoscloud.RequestPasswordResetCallback;
import com.avos.avoscloud.SaveCallback;
import com.bumptech.glide.Glide;
import com.bumptech.glide.load.engine.DiskCacheStrategy;
import com.bumptech.glide.request.animation.GlideAnimation;
import com.bumptech.glide.request.target.SimpleTarget;
import com.chad.library.adapter.base.BaseQuickAdapter;
import com.chad.library.adapter.base.BaseViewHolder;

import org.greenrobot.eventbus.EventBus;

import java.util.ArrayList;
import java.util.List;

import butterknife.BindView;

/**
 * Created by Administrator on 2017/7/13.
 */

public class FuLiContributeFragment extends BaseFragment {


    @BindView(R.id.contribute_fuli_item_addcount)
    TextView mCount;

    @BindView(R.id.contribute_fuli_item_btn)
    Button mImgBtn;

    @BindView(R.id.contribute_fuli_item_go)
    Button mButton;

    @BindView(R.id.contribute_fuli_item_title)
    EditText mTitle;

    @BindView(R.id.contribute_fuli_item_desc)
    EditText mDesc;

    @BindView(R.id.contribute_fuli_item_imgrv)
    RecyclerView mRecyclerView;

    List<FuliContributeImgBean> imgList = new ArrayList<>();
    imgAdapter mAdapter;


    private static FuLiContributeFragment instance;

    public static FuLiContributeFragment getInstance() {
        if (instance == null) {
            synchronized (FuLiContributeFragment.class) {
                if (instance == null) {
                    instance = new FuLiContributeFragment();
                }
            }
        }
        return instance;
    }


    @Override
    protected int getLayoutId() {
        return R.layout.fragment_contribute_fuli_item;
    }

    @Override
    protected void initView(Bundle savedInstanceState) {
        initRV();


    }

    @Override
    protected void setListener() {

        mImgBtn.setOnClickListener(new PerfectClickListener() {
            @Override
            protected void onNoDoubleClick(View v) {
                addImgBtn();
                mAdapter.notifyDataSetChanged();
                mCount.setText(String.format("%d", imgList.size()));
            }
        });

        mAdapter.setOnItemClickListener((adapter, view, position) -> {
            //点击进入详情页

            ArrayList<String> imgUrlList = new ArrayList<>();
            for (int i = 0; i < imgList.size(); i++) {
                imgUrlList.add(imgList.get(i).getImgUrl());
            }

            //TODO 缓存一下URL

            EventBus.getDefault().postSticky(new MeiziEvent(position, imgUrlList));

            startActivity(new Intent(getContext(), MeiZiBigImageActivity.class));

        });

        mButton.setOnClickListener(new PerfectClickListener() {
            @Override
            protected void onNoDoubleClick(View v) {
                goFunc();
            }
        });

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


    private void goFunc() {
        xToastShow("正在保存中，请不要退出页面");
        String title = mTitle.getText().toString().trim();
        String desc = mDesc.getText().toString().trim();

        String userId = AVUser.getCurrentUser().getObjectId();

        AVObject todoFolder = new AVObject("FuLiContentBean");// 构建对象
        todoFolder.put("desc", desc);
        todoFolder.put("title", title);
        todoFolder.put("imgUrlList", imgList);
        todoFolder.put("userId", userId);

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


    private void addImgBtn() {

        new MaterialDialog.Builder(mActivity)
                .title("添加网络图片")
                .content("请填写网络图片链接(例如：http://www.aa.com.b.jpg)")
                .inputType(InputType.TYPE_CLASS_TEXT)
                .inputRange(2, 200)
                .positiveText("确定")
                .input(
                        "",
                        "",
                        false,
                        (dialog, input) -> {
                            if (!TextUtils.isEmpty(input.toString())) {

                                imgList.add(new FuliContributeImgBean(input.toString().trim()));
                            }

                        }
                )
                .show();
    }


    private void initRV() {

        mAdapter = new imgAdapter(R.layout.item_meizi, imgList);
        mAdapter.setUpFetchEnable(false);  //关闭自带的下拉
        mAdapter.setEnableLoadMore(false);
        //设置布局管理器
        //第一个参数表示列数或者行数，第二个参数表示滑动方向,瀑布流
        mRecyclerView.setLayoutManager(new StaggeredGridLayoutManager(2, StaggeredGridLayoutManager.VERTICAL));
        mRecyclerView.setAdapter(mAdapter);
    }


    //列表适配器
    class imgAdapter extends BaseQuickAdapter<FuliContributeImgBean, BaseViewHolder> {


        public imgAdapter(int layoutResId, @Nullable List<FuliContributeImgBean> data) {
            super(layoutResId, data);
        }

        @Override
        protected void convert(BaseViewHolder helper, FuliContributeImgBean item) {

            ImageView image = helper.getView(R.id.meizi_photo);

            Glide.with(helper.getConvertView().getContext())
                    .load(item.getImgUrl())
                    .placeholder(R.drawable.pic_placeholder)
                    .diskCacheStrategy(DiskCacheStrategy.ALL)
                    .centerCrop()
                    .into(image);
        }
    }


}
