package com.afeng.xf.ui.fuli;

import android.content.ClipboardManager;
import android.content.Context;
import android.content.Intent;
import android.graphics.Bitmap;
import android.graphics.Rect;
import android.os.Bundle;
import android.support.annotation.Nullable;
import android.support.v4.widget.SwipeRefreshLayout;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.text.InputType;
import android.text.TextUtils;
import android.view.View;
import android.widget.ImageView;
import android.widget.ProgressBar;
import android.widget.TextView;

import com.afeng.xf.R;
import com.afeng.xf.base.BaseFragment;
import com.afeng.xf.ui.constants.Constants;
import com.afeng.xf.ui.contribute.FuliContributeImgBean;
import com.afeng.xf.ui.data.FuLiContentBean;
import com.afeng.xf.ui.data.FuLiHeadBean;
import com.afeng.xf.ui.data.SplashBannerItem;
import com.afeng.xf.ui.meizi.MeiZiActivity;
import com.afeng.xf.ui.meizi.MeiZiBigImageActivity;
import com.afeng.xf.ui.meizi.MeiziEvent;
import com.afeng.xf.utils.AFengUtils.AppImageMgr;
import com.afeng.xf.utils.AFengUtils.AppTimeUtils;
import com.afeng.xf.utils.AFengUtils.AppValidationMgr;
import com.afeng.xf.utils.AFengUtils.PerfectClickListener;
import com.afeng.xf.utils.GlideHelper.ImgLoadUtil;
import com.afeng.xf.widget.multipleImageView.MultiImageView;
import com.afeng.xf.widget.ninegrid.GlideImageLoader;
import com.afeng.xf.widget.ninegrid.ImageInfo;
import com.afeng.xf.widget.ninegrid.MyMeiZiClickViewAdapter;
import com.afeng.xf.widget.ninegrid.NineGridView;
import com.afeng.xf.widget.ninegrid.NineGridViewClickAdapter;
import com.afollestad.materialdialogs.MaterialDialog;
import com.avos.avoscloud.AVException;
import com.avos.avoscloud.AVFile;
import com.avos.avoscloud.AVObject;
import com.avos.avoscloud.AVQuery;
import com.avos.avoscloud.AVUser;
import com.avos.avoscloud.FindCallback;
import com.avos.avoscloud.GetCallback;
import com.avos.avoscloud.GetDataCallback;
import com.avos.avoscloud.ProgressCallback;
import com.avos.avoscloud.RequestPasswordResetCallback;
import com.bumptech.glide.Glide;
import com.bumptech.glide.load.engine.DiskCacheStrategy;
import com.chad.library.adapter.base.BaseQuickAdapter;
import com.chad.library.adapter.base.BaseViewHolder;

import org.greenrobot.eventbus.EventBus;
import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import java.util.ArrayList;
import java.util.Date;
import java.util.Iterator;
import java.util.List;

import butterknife.BindView;

/**
 * Created by Administrator on 2017/7/11.
 */

public class FuLiFragment extends BaseFragment {

    @BindView(R.id.fuli_progressBar)
    ProgressBar mProgressBar;

    @BindView(R.id.fuli_content_rv)
    RecyclerView fuli_content_rv;

    @BindView(R.id.fuli_content_SwipeRefresh)
    SwipeRefreshLayout mSwipeLayout;

    View headerView; //头部
    RecyclerView fuli_head_rv;

    headAdapter mHeadAdapter;
    contentAdapter mContentAdapter;

    List<FuLiHeadBean> mFuLiHeadList = new ArrayList<>();
    List<FuLiContentBean> mFuLiContentList = new ArrayList<>();

    int limitCount = 10;  //每页数量
    int skipCount = 0;  //跳过的数量


    int headlimitCount = 10;  // 头部每页数量
    int headskipCount = 0;  //头部跳过的数量


    private static FuLiFragment instance;

    public static FuLiFragment getInstance() {
        if (instance == null) {
            synchronized (FuLiFragment.class) {
                if (instance == null) {
                    instance = new FuLiFragment();
                }
            }
        }
        return instance;
    }


    @Override
    protected int getLayoutId() {
        return R.layout.fragment_fuli;
    }

    @Override
    protected void initView(Bundle savedInstanceState) {

//        NineGridView.setImageLoader(new GlideImageLoader());  //设置九宫格图片加载器

        mSwipeLayout.setColorSchemeResources(R.color.holo_blue_bright,
                R.color.holo_green_light, R.color.holo_orange_light,
                R.color.holo_red_light);

        initHead();
        initContent();

    }

    @Override
    protected void setListener() {

        mHeadAdapter.setOnItemClickListener(new BaseQuickAdapter.OnItemClickListener() {
            @Override
            public void onItemClick(BaseQuickAdapter adapter, View view, int position) {
                FuLiHeadBean item = (FuLiHeadBean) adapter.getItem(position);
                if (item.getTag().equals("meizi")) {
                    startActivity(new Intent(getContext(), MeiZiActivity.class));
                }
            }
        });

        //下拉刷新
        mSwipeLayout.setOnRefreshListener(() -> {
            if (!mSwipeLayout.isRefreshing()) {
                mSwipeLayout.setRefreshing(true);
            }

            mFuLiContentList.clear();
            mFuLiHeadList.clear();

            initHeadData();

            SwipeContentData();

            mSwipeLayout.setRefreshing(false);

        });

//        mHeadAdapter.setOnLoadMoreListener(() ->
//        {
//            xToastShow("上拉加载");
//        });
//

        mContentAdapter.setOnLoadMoreListener(() -> {
                    initContentData();
                }
        );


    }

    @Override
    protected void processLogic(Bundle savedInstanceState) {

    }

    @Override
    protected void onLazyLoadOnce() {
        mProgressBar.setVisibility(View.VISIBLE);
        if (mFuLiHeadList.size() == 0) {
            initHeadData();
        }

        if (mFuLiContentList.size() == 0) {
            initContentData();
        }
        mProgressBar.setVisibility(View.GONE);
    }

    @Override
    protected void onVisibleToUser() {
        mHeadAdapter.notifyDataSetChanged();
        mContentAdapter.notifyDataSetChanged();
    }

    @Override
    protected void onInvisibleToUser() {

    }


    private void initHead() {

        //动态嵌入布局
        if (headerView == null) {
            headerView = View.inflate(getContext(), R.layout.view_fuli_head, null);
        }

        fuli_head_rv = (RecyclerView) headerView.findViewById(R.id.fuli_head_rv);

        mHeadAdapter = new headAdapter(R.layout.item_fuli_head, mFuLiHeadList);
        mHeadAdapter.setEnableLoadMore(false);
        //设置布局管理器
        LinearLayoutManager linearLayoutManager = new LinearLayoutManager(getContext());
        linearLayoutManager.setOrientation(LinearLayoutManager.HORIZONTAL);
        fuli_head_rv.setLayoutManager(linearLayoutManager);
        fuli_head_rv.setAdapter(mHeadAdapter);

    }


    private void initContent() {

        mContentAdapter = new contentAdapter(R.layout.item_fuli_content, mFuLiContentList);

        mContentAdapter.addHeaderView(headerView);
        mContentAdapter.setUpFetchEnable(false);  //关闭自带的下拉
        mContentAdapter.setEnableLoadMore(false);
        //设置布局管理器
        LinearLayoutManager linearLayoutManager = new LinearLayoutManager(getContext());
        linearLayoutManager.setOrientation(LinearLayoutManager.VERTICAL);
        fuli_content_rv.setLayoutManager(linearLayoutManager);
        fuli_content_rv.setAdapter(mContentAdapter);

//        fuli_content_rv.addItemDecoration(new SpaceItemDecoration(30));  //设置item间隔
    }


    private void initHeadData() {

        FuLiHeadBean fuLiHeadBean = new FuLiHeadBean();
        fuLiHeadBean.setImgUrl("http://ot2y5fs2o.bkt.clouddn.com/3075.jpg");
        fuLiHeadBean.setName("Genk妹子");
        fuLiHeadBean.setTag("meizi");

        mFuLiHeadList.add(fuLiHeadBean);

        mHeadAdapter.notifyDataSetChanged();
    }

    private void initContentData() {

        AVQuery<AVObject> avQuery = new AVQuery<>("FuLiContentBean");
        avQuery.orderByDescending("updatedAt");
        avQuery.limit(limitCount);// 最多返回 10 条结果
        avQuery.skip(skipCount);// 跳过
        avQuery.findInBackground(new FindCallback<AVObject>() {
            @Override
            public void done(List<AVObject> list, AVException e) {

                if (list != null && list.size() > 0) {
                    for (AVObject avObject : list) {

                        FuLiContentBean fuLiContentBean = new FuLiContentBean();

                        ArrayList<String> temps = new ArrayList<>();

                        String desc = avObject.getString("desc");
                        String title = avObject.getString("title");
                        Date date = avObject.getUpdatedAt();
                        String userid = avObject.getString("userId");
                        String add = avObject.getString("address");

                        JSONArray imgUrlList = avObject.getJSONArray("imgUrlList");
                        if (imgUrlList != null) {

                            for (int i = 0; i < imgUrlList.length(); i++) {
                                JSONObject job = null; // 遍历 jsonarray 数组，把每一个对象转成 json 对象
                                try {
                                    job = imgUrlList.getJSONObject(i);
                                    String img = job.getString("imgUrl");
                                    temps.add(img);
                                } catch (JSONException e1) {
                                    e1.printStackTrace();
                                }
                            }

                        }

                        fuLiContentBean.setImgUrlList(temps);
                        fuLiContentBean.setTime(date);
                        fuLiContentBean.setWatch(0);
                        fuLiContentBean.setDesc(desc);
                        fuLiContentBean.setAddress(add);

                        AVQuery<AVUser> userQuery = new AVQuery<>("_User");
                        userQuery.getInBackground(userid, new GetCallback<AVUser>() {
                            @Override
                            public void done(AVUser user, AVException e) {

                                if (e == null) {
                                    if (user != null) {
                                        fuLiContentBean.setName(user.getUsername());
                                    }
                                }
                            }
                        });


                        // 联表查询
                        AVObject _User = AVObject.createWithoutData("_User", userid);
                        _User.fetchInBackground("UserInfo", new GetCallback<AVObject>() {
                            @Override
                            public void done(AVObject avObject, AVException e) {
                                if (e == null) {
                                    if (avObject != null) {

                                        AVObject info = avObject.getAVObject("UserInfo");

                                        if (info != null) {
                                            AVFile avatar = info.getAVFile("avatar");
                                            String nickname = info.getString("nickname");  //昵称

                                            if (!TextUtils.isEmpty(nickname)) {
                                                fuLiContentBean.setName(nickname);
                                            }

                                            if (avatar != null) {
                                                fuLiContentBean.setAvatar(avatar);
                                            }

                                            mFuLiContentList.add(fuLiContentBean);

                                            mContentAdapter.notifyDataSetChanged();
                                            skipCount += limitCount;
                                            mContentAdapter.loadMoreComplete();

                                        } else {
                                            xToastShow("info为空");
                                        }

                                    } else {
                                        xToastShow("avObject为空");
                                    }
                                }
                            }
                        });

                    }
                } else {
                    mContentAdapter.loadMoreEnd();
                }
            }
        });
    }


    //下拉刷新
    private void SwipeContentData() {
        mFuLiContentList.clear();
        skipCount = 0;  //跳过的数量
        initContentData();
    }


    private class headAdapter extends BaseQuickAdapter<FuLiHeadBean, BaseViewHolder> {


        public headAdapter(int layoutResId, @Nullable List<FuLiHeadBean> data) {
            super(layoutResId, data);
        }

        @Override
        protected void convert(BaseViewHolder helper, FuLiHeadBean item) {

            ImageView imageView = helper.getView(R.id.item_fuli_head_image);
            TextView textView = helper.getView(R.id.item_fuli_head_name);

            //缩省过长的名字
            String name = AppValidationMgr.cutString(item.getName(), 8, "…");
            textView.setText(name);

            if (!TextUtils.isEmpty(item.getImgUrl())) {
                Glide.with(helper.getConvertView().getContext())
                        .load(item.getImgUrl())
                        .placeholder(R.drawable.img_loading_git)
                        .diskCacheStrategy(DiskCacheStrategy.ALL)
                        .into(imageView);
            }
        }

    }


    private class contentAdapter extends BaseQuickAdapter<FuLiContentBean, BaseViewHolder> {


        public contentAdapter(int layoutResId, @Nullable List<FuLiContentBean> data) {
            super(layoutResId, data);
        }

        @Override
        protected void convert(BaseViewHolder helper, FuLiContentBean item) {

            ImageView avaimg = helper.getView(R.id.item_fuli_content_photo);
            TextView name = helper.getView(R.id.item_fuli_content_name);
            TextView time = helper.getView(R.id.item_fuli_content_time);
            TextView desc = helper.getView(R.id.item_fuli_content_desc);
            TextView watch = helper.getView(R.id.item_fuli_content_watch);
            ImageView btnImg = helper.getView(R.id.item_fuli_content_btn);

//            MultiImageView multiImageView = helper.getView(R.id.item_fuli_content_sub);
            NineGridView multiImageView = helper.getView(R.id.item_fuli_content_sub);


            btnImg.setOnClickListener(new PerfectClickListener() {
                @Override
                protected void onNoDoubleClick(View v) {

                    // 从API11开始android推荐使用android.content.ClipboardManager
                    // 为了兼容低版本我们这里使用旧版的android.text.ClipboardManager，虽然提示deprecated，但不影响使用。
                    ClipboardManager cm = (ClipboardManager) mActivity.getSystemService(Context.CLIPBOARD_SERVICE);
                    // 将文本内容放到系统剪贴板里。
                    cm.setText(item.getAddress());

//                    xToastShow(item.getAddress());
                    new MaterialDialog.Builder(mActivity)
                            .title("资源下载链接")
                            .content("地址已经自动复制到剪切板:\n" + item.getAddress())
                            .positiveText("确定")
                            .show();

                }
            });

            if (!TextUtils.isEmpty(item.getAvaUrl())) {
                ImgLoadUtil.displayCircle(mContext, avaimg, item.getAvaUrl());
            } else if (item.getAvatar() != null) {
                // 把图片下载回来
                item.getAvatar().getDataInBackground(new GetDataCallback() {
                    @Override
                    public void done(byte[] bytes, AVException e) {
                        // bytes 就是文件的数据流
                        if (e == null) {
                            if (bytes != null) {
                                Bitmap img = AppImageMgr.getBitmapByteArray(bytes, 512, 512);
                                ImgLoadUtil.displayCircle(mContext, avaimg, img);
                            }
                        }
                    }
                }, new ProgressCallback() {
                    @Override
                    public void done(Integer integer) {
                        // 下载进度数据，integer 介于 0 和 100。
                    }
                });
            }


            if (item.getImgUrlList() != null && item.getImgUrlList().size() > 0) {
//                multiImageView.setList(item.getImgUrlList());
//                multiImageView.setOnItemClickListener(new MultiImageView.OnItemClickListener() {
//                    @Override
//                    public void onItemClick(View view, int position) {
//                        EventBus.getDefault().postSticky(new MeiziEvent(position, item.getImgUrlList()));
//
//                        startActivity(new Intent(getContext(), MeiZiBigImageActivity.class));
//                    }
//                });

                List<ImageInfo> imageInfos = new ArrayList<>();
                for (String s : item.getImgUrlList()) {

                    imageInfos.add(new ImageInfo(s));
                }

//                multiImageView.setAdapter(new NineGridViewClickAdapter(mContext, imageInfos));
                multiImageView.setAdapter(new MyMeiZiClickViewAdapter(mContext, imageInfos));

                if (item.getImgUrlList() != null && item.getImgUrlList().size() == 1) {
                    multiImageView.setSingleImageRatio(1);
                }
            }

            name.setText(item.getName());
            desc.setText(item.getDesc());
            time.setText(AppTimeUtils.formatFriendly(item.getTime()));

            String watchStr;
            if (item.getWatch() >= 1000) {
                watchStr = item.getWatch() / 1000 + "K";
            } else {
                watchStr = item.getWatch() + "";
            }
            watch.setText("阅读 " + watchStr);


        }
    }


    //设置item间隔
    class SpaceItemDecoration extends RecyclerView.ItemDecoration {
        int mSpace;

        @Override
        public void getItemOffsets(Rect outRect, View view, RecyclerView parent, RecyclerView.State state) {
            super.getItemOffsets(outRect, view, parent, state);
            outRect.left = mSpace;
            outRect.right = mSpace;
            outRect.bottom = mSpace;
            if (parent.getChildAdapterPosition(view) == 0) {
                outRect.top = mSpace;
            }

        }

        public SpaceItemDecoration(int space) {
            this.mSpace = space;
        }
    }

}
