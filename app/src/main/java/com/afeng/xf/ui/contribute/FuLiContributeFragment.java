package com.afeng.xf.ui.contribute;

import android.Manifest;
import android.content.Intent;
import android.content.pm.ActivityInfo;
import android.graphics.Bitmap;
import android.net.Uri;
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
import android.widget.ProgressBar;
import android.widget.TextView;

import com.afeng.xf.R;
import com.afeng.xf.base.BaseFragment;
import com.afeng.xf.ui.MainActivity;
import com.afeng.xf.ui.constants.Constants;
import com.afeng.xf.ui.home.UserInfoActivity;
import com.afeng.xf.ui.meizi.MeiZiBigImageActivity;
import com.afeng.xf.ui.meizi.MeiziEvent;
import com.afeng.xf.utils.AFengUtils.AppImageMgr;
import com.afeng.xf.utils.AFengUtils.DensityUtil;
import com.afeng.xf.utils.AFengUtils.PathUtils;
import com.afeng.xf.utils.AFengUtils.PerfectClickListener;
import com.afeng.xf.utils.AFengUtils.QinIuManager;
import com.afeng.xf.utils.GlideHelper.GlideCircleTransform;
import com.afeng.xf.widget.hipermission.HiPermission;
import com.afeng.xf.widget.hipermission.PermissionCallback;
import com.afeng.xf.widget.hipermission.PermissionItem;
import com.afollestad.materialdialogs.DialogAction;
import com.afollestad.materialdialogs.GravityEnum;
import com.afollestad.materialdialogs.MaterialDialog;
import com.avos.avoscloud.AVException;
import com.avos.avoscloud.AVFile;
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
import top.zibin.luban.OnCompressListener;

import static android.app.Activity.RESULT_OK;

/**
 * Created by Administrator on 2017/7/13.
 */

public class FuLiContributeFragment extends BaseFragment {


    @BindView(R.id.contribute_fuli_item_clear)
    Button mCleanBtn;

    @BindView(R.id.contribute_fuli_item_addcount)
    TextView mCount;

    @BindView(R.id.contribute_fuli_item_btn)
    Button mImgBtn;

    @BindView(R.id.contribute_fuli_item_go)
    Button mButton;

    @BindView(R.id.contribute_fuli_item_loaclbtn)
    Button mImgLoaclBtn;


    @BindView(R.id.contribute_fuli_item_addr)
    EditText mAddr;

    @BindView(R.id.contribute_fuli_item_title)
    EditText mTitle;

    @BindView(R.id.contribute_fuli_item_desc)
    EditText mDesc;

    @BindView(R.id.contribute_fuli_item_imgrv)
    RecyclerView mRecyclerView;


    ProgressBar mProgressBar;

    List<FuliContributeImgBean> imgList = new ArrayList<>();
    imgAdapter mAdapter;

    public static final int REQUEST_CODE_CHOOSE = 23;


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

        if (!EventBus.getDefault().isRegistered(this)) {
            EventBus.getDefault().register(this);
        }

        mProgressBar = (ProgressBar) getActivity().findViewById(R.id.contribute_progressBar);

        initRV();

    }

    @Override
    protected void setListener() {

        mImgBtn.setOnClickListener(new PerfectClickListener() {
            @Override
            protected void onNoDoubleClick(View v) {
                addImgBtn();
                mAdapter.notifyDataSetChanged();
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
    public void ReceviceMessage(Intent event) {

        qiniuUpdataImg(event);
    }


    private void goFunc() {
        xToastShow("正在保存中，请不要退出页面");
        String title = mTitle.getText().toString().trim();
        String desc = mDesc.getText().toString().trim();
        String add = mAddr.getText().toString().trim();


        if (TextUtils.isEmpty(title)) {
            xToastShow("请填写标题");
            return;
        }

        if (TextUtils.isEmpty(desc)) {
            xToastShow("请填写描述");
            return;
        }

        String userId = AVUser.getCurrentUser().getObjectId();

        AVObject todoFolder = new AVObject("FuLiContentBean");// 构建对象
        todoFolder.put("desc", desc);
        todoFolder.put("title", title);
        todoFolder.put("imgUrlList", imgList);
        todoFolder.put("userId", userId);
        todoFolder.put("address", add);

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
                                mAdapter.notifyDataSetChanged();
                                mCount.setText(String.format("%d", imgList.size()));
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
                        .map(new Function<File, File>() {
                            @Override
                            public File apply(@NonNull File file) throws Exception {
                                return Luban.with(mActivity).load(file).get();
                            }
                        })
                        .observeOn(AndroidSchedulers.mainThread())
                        .subscribe(new Consumer<File>() {
                            @Override
                            public void accept(@NonNull File file) throws Exception {

                                // 设置图片名字
                                SimpleDateFormat sdf = new SimpleDateFormat("yyyyMMddHHmmss");
                                String upkey = "fuli_" + sdf.format(new Date()) + "_" + (int) (Math.random() * (10));

                                new Thread(() -> {
                                    QinIuManager.getUploadManager().put(
                                            file,
                                            upkey,
                                            QinIuManager.getSimpleToKen(),
                                            new UpCompletionHandler() {
                                                @Override
                                                public void complete(String key, ResponseInfo rinfo, JSONObject response) {

                                                    String filePath = QinIuManager.filePrefix + key;

                                                    imgList.add(new FuliContributeImgBean(filePath));
                                                    mAdapter.notifyDataSetChanged();
                                                    mCount.setText(String.format("%d", imgList.size()));
                                                    //显示上传后文件的url
                                                    xLogger(filePath);
                                                }
                                            }, new UploadOptions(null, null, true, new UpProgressHandler() {
                                                @Override
                                                public void progress(String key, double percent) {


                                                }
                                            }, null));
                                }).start();

                            }
                        });


//                Glide.with(mActivity)
//                        .load(uri)
//                        .asBitmap()
//                        .diskCacheStrategy(DiskCacheStrategy.ALL)
//                        .into(new SimpleTarget<Bitmap>() {
//                            @Override
//                            public void onResourceReady(Bitmap resource, GlideAnimation<? super Bitmap> glideAnimation) {
//
//
//                                //压缩图片
//                                Bitmap small = AppImageMgr.decodeBitmapToThumbnail(resource);
//                                //转换比特流
//                                byte[] img = AppImageMgr.getBitmap2Byte(small);
//                                // 设置图片名字
//                                SimpleDateFormat sdf = new SimpleDateFormat("yyyyMMddHHmmss");
//                                String upkey = "fuli_" + sdf.format(new Date()) + "_" + (int) (Math.random() * (10));
//
//                                new Thread(() -> {
//                                    QinIuManager.getUploadManager().put(
//                                            img,
//                                            upkey,
//                                            QinIuManager.getSimpleToKen(),
//                                            new UpCompletionHandler() {
//                                                @Override
//                                                public void complete(String key, ResponseInfo rinfo, JSONObject response) {
//
//                                                    String filePath = QinIuManager.filePrefix + key;
//
//                                                    imgList.add(new FuliContributeImgBean(filePath));
//                                                    mAdapter.notifyDataSetChanged();
//                                                    mCount.setText(String.format("%d", imgList.size()));
//                                                    //显示上传后文件的url
//                                                    xLogger(filePath);
//
//                                                }
//                                            }, new UploadOptions(null, null, true, new UpProgressHandler() {
//                                                @Override
//                                                public void progress(String key, double percent) {
//
//                                                }
//                                            }, null));
//                                }).start();
//
//                            }
//
//                        });

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
