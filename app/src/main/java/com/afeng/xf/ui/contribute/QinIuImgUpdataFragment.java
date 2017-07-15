package com.afeng.xf.ui.contribute;

import android.Manifest;
import android.content.ClipboardManager;
import android.content.Context;
import android.content.Intent;
import android.content.pm.ActivityInfo;
import android.net.Uri;
import android.os.Bundle;
import android.support.annotation.Nullable;
import android.support.v7.widget.RecyclerView;
import android.support.v7.widget.StaggeredGridLayoutManager;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ImageView;
import android.widget.ProgressBar;
import android.widget.TextView;

import com.afeng.xf.R;
import com.afeng.xf.base.BaseFragment;
import com.afeng.xf.ui.meizi.MeiZiBigImageActivity;
import com.afeng.xf.ui.meizi.MeiziEvent;
import com.afeng.xf.utils.AFengUtils.PathUtils;
import com.afeng.xf.utils.AFengUtils.PerfectClickListener;
import com.afeng.xf.utils.AFengUtils.QinIuManager;
import com.afeng.xf.widget.hipermission.HiPermission;
import com.afeng.xf.widget.hipermission.PermissionCallback;
import com.afeng.xf.widget.hipermission.PermissionItem;
import com.afollestad.materialdialogs.MaterialDialog;
import com.bumptech.glide.Glide;
import com.bumptech.glide.load.engine.DiskCacheStrategy;
import com.chad.library.adapter.base.BaseQuickAdapter;
import com.chad.library.adapter.base.BaseViewHolder;
import com.qiniu.android.http.ResponseInfo;
import com.qiniu.android.storage.UpCompletionHandler;
import com.qiniu.android.storage.UpProgressHandler;
import com.qiniu.android.storage.UploadOptions;
import com.zhihu.matisse.Matisse;
import com.zhihu.matisse.MimeType;
import com.zhihu.matisse.engine.impl.GlideEngine;

import org.greenrobot.eventbus.EventBus;
import org.greenrobot.eventbus.Subscribe;
import org.greenrobot.eventbus.ThreadMode;
import org.json.JSONObject;

import java.io.File;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Date;
import java.util.List;

import butterknife.BindView;
import io.reactivex.Flowable;
import io.reactivex.android.schedulers.AndroidSchedulers;
import io.reactivex.annotations.NonNull;
import io.reactivex.functions.Consumer;
import io.reactivex.functions.Function;
import io.reactivex.schedulers.Schedulers;
import top.zibin.luban.Luban;

/**
 * Created by Administrator on 2017/7/15.
 */

public class QinIuImgUpdataFragment extends BaseFragment {


    @BindView(R.id.contribute_qiniu_clear)
    Button mCleanBtn;

    @BindView(R.id.contribute_qiniu_addcount)
    TextView mCount;

    @BindView(R.id.contribute_qiniu_loaclbtn)
    Button mImgLoaclBtn;


    @BindView(R.id.contribute_qiniu_addr)
    EditText mAddr;


    @BindView(R.id.contribute_qiniu_imgrv)
    RecyclerView mRecyclerView;


    ProgressBar mProgressBar;

    List<FuliContributeImgBean> imgList = new ArrayList<>();
    imgAdapter mAdapter;

    public static final int REQUEST_CODE_CHOOSE = 99;


    private static QinIuImgUpdataFragment instance;

    public static QinIuImgUpdataFragment getInstance() {
        if (instance == null) {
            synchronized (QinIuImgUpdataFragment.class) {
                if (instance == null) {
                    instance = new QinIuImgUpdataFragment();
                }
            }
        }
        return instance;
    }


    @Override
    protected int getLayoutId() {
        return R.layout.fragment_qinluimgupdata;
    }

    @Override
    protected void initView(Bundle savedInstanceState) {
        if (!EventBus.getDefault().isRegistered(this)) {
            EventBus.getDefault().register(this);
        }

        mProgressBar = (ProgressBar) getActivity().findViewById(R.id.contribute_progressBar);

        initRV();
    }

    @Override
    protected void setListener() {
        initListener();
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


    @Override
    public void onDestroy() {
        EventBus.getDefault().unregister(this);
        super.onDestroy();
    }


    @Subscribe(threadMode = ThreadMode.MAIN)
    public void ReceviceMessage(QinIuEvent event) {

        qiniuUpdataImg(event.getData());
    }


    private void initListener() {

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

        mImgLoaclBtn.setOnClickListener(new PerfectClickListener() {
            @Override
            protected void onNoDoubleClick(View v) {
                getImg();
            }
        });

        mCleanBtn.setOnClickListener(new PerfectClickListener() {
            @Override
            protected void onNoDoubleClick(View v) {

                new MaterialDialog.Builder(mActivity)
                        .content("是否清空图片？")
                        .positiveText("是的")
                        .negativeText("不要")
                        .onAny((dialog, which) -> {
                            switch (which.name()) {
                                case "POSITIVE":

                                    imgList.clear();
                                    mAdapter.notifyDataSetChanged();
                                    mCount.setText(String.format("%d", imgList.size()));

                                    break;

                                case "NEGATIVE":
                                    // 否
                                    break;
                            }
                        })
                        .show();

            }
        });

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


    private void getImg() {

        List<PermissionItem> permissionItems = new ArrayList<>();
        permissionItems.add(new PermissionItem(Manifest.permission.READ_EXTERNAL_STORAGE, "相册访问权限", R.drawable.permission_ic_storage));
        permissionItems.add(new PermissionItem(Manifest.permission.WRITE_EXTERNAL_STORAGE, "SD卡访问权限", R.drawable.permission_card1));

        HiPermission.create(mActivity)
                .title("权限申请")
                .permissions(permissionItems)
                .msg("访问相册需要您同意以下权限！")
                .animStyle(R.style.PermissionAnimFade)
                .style(R.style.PermissionDefaultNormalStyle)
                .checkMutiPermission(new PermissionCallback() {
                    @Override
                    public void onClose() {

                    }

                    @Override
                    public void onFinish() {

                        Matisse.from(mActivity)
                                .choose(MimeType.of(MimeType.JPEG, MimeType.PNG))
                                .countable(true)
                                .maxSelectable(9)  //最多选择数量
                                .gridExpectedSize(getResources().getDimensionPixelSize(R.dimen.grid_expected_size))
                                .restrictOrientation(ActivityInfo.SCREEN_ORIENTATION_UNSPECIFIED)
                                .thumbnailScale(0.85f)
                                .imageEngine(new GlideEngine())
                                .forResult(REQUEST_CODE_CHOOSE);

                    }

                    @Override
                    public void onDeny(String permission, int position) {

                    }

                    @Override
                    public void onGuarantee(String permission, int position) {

                    }
                });


    }


    private void qiniuUpdataImg(Intent data) {

        mProgressBar.setVisibility(View.VISIBLE);

        if (data != null) {

            for (Uri uri : Matisse.obtainResult(data)) {

                String filePath = PathUtils.getAbsoluteUriPath(mActivity, uri);
                File filetemp = new File(filePath);

                if (filetemp == null) {
                    xLogger("filetemp为空");
                    return;
                }

                Flowable.just(filetemp)
                        .observeOn(Schedulers.io())
                        .map(file -> Luban.with(mActivity).load(file).get())
                        .observeOn(AndroidSchedulers.mainThread())
                        .subscribe(file -> {

                            // 设置图片名字
                            SimpleDateFormat sdf = new SimpleDateFormat("yyyyMMddHHmmss");
                            String upkey = "updata_" + sdf.format(new Date()) + "_" + (int) (Math.random() * (10));

                            new Thread(() -> {
                                QinIuManager.getUploadManager().put(
                                        file,
                                        upkey,
                                        QinIuManager.getSimpleToKen(),
                                        (key, rinfo, response) -> {

                                            String filePath1 = QinIuManager.filePrefix + key;
                                            mAddr.append(filePath1 + "\n");
                                            imgList.add(new FuliContributeImgBean(filePath1));
                                            mAdapter.notifyDataSetChanged();
                                            mCount.setText(String.format("%d", imgList.size()));

                                            // 从API11开始android推荐使用android.content.ClipboardManager
                                            // 为了兼容低版本我们这里使用旧版的android.text.ClipboardManager，虽然提示deprecated，但不影响使用。
                                            ClipboardManager cm = (ClipboardManager) mActivity.getSystemService(Context.CLIPBOARD_SERVICE);
                                            // 将文本内容放到系统剪贴板里。
                                            cm.setText(filePath1);

                                            //显示上传后文件的url
                                            xLogger(filePath1);
                                        }, new UploadOptions(null, null, true, (key, percent) -> {


                                        }, null));
                            }).start();

                        });

            }

        }

        mProgressBar.setVisibility(View.GONE);

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
