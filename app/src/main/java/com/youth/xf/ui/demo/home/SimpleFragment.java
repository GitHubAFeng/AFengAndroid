package com.youth.xf.ui.demo.home;

import android.annotation.SuppressLint;
import android.content.Intent;
import android.os.Bundle;
import android.support.v7.widget.GridLayoutManager;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.text.TextUtils;
import android.view.View;
import android.widget.ImageButton;
import android.widget.ImageView;
import android.widget.ProgressBar;
import android.widget.TextView;

import com.avos.avoscloud.AVException;
import com.avos.avoscloud.AVObject;
import com.avos.avoscloud.AVQuery;
import com.avos.avoscloud.FindCallback;
import com.bumptech.glide.Glide;
import com.chad.library.adapter.base.BaseQuickAdapter;
import com.chad.library.adapter.base.BaseViewHolder;
import com.chad.library.adapter.base.listener.OnItemClickListener;
import com.youth.xf.R;
import com.youth.xf.base.AFengFragment;
import com.youth.xf.ui.constants.Constants;
import com.youth.xf.ui.data.HomeBannerItem;
import com.youth.xf.ui.data.HomeListItem;
import com.youth.xf.ui.demo.comic.ComicWebActivity;
import com.youth.xf.ui.demo.live.LiveWebActivity;
import com.youth.xf.ui.demo.mv.BiliAgentWebActivity;
import com.youth.xf.utils.AFengUtils.xToastUtil;

import org.greenrobot.eventbus.EventBus;

import java.util.ArrayList;
import java.util.List;

import butterknife.BindView;
import cn.bingoogolapple.bgabanner.BGABanner;

import static com.avos.avoscloud.AVObject.createWithoutData;

/**
 * 作者： AFeng
 * 时间：2017/3/2 , 再次整理时已经是3个多月后了，现在是6月24， 唉 ， 感觉发生了好多事。
 */

//此注解用来忽略检查
@SuppressLint("ValidFragment")
public class SimpleFragment extends AFengFragment implements View.OnClickListener {
    private static final String TYPE = "mType";
    private String mType = "Android";

    private List<HomeListItem> listDatas = new ArrayList<>();   //下拉列表里面的数据

    private List<HomeBannerItem> BannerDatas = new ArrayList<>();   //轮播图里面数据
    private List<String> BannerTips = new ArrayList<>();   //轮播图里面文字提示

    private BGABanner mBGABanner;

    @BindView(R.id.one_rv_list)
    RecyclerView mRecyclerView;

    View headerView;
    ImageButton mHomeMvBtn;

    oneAdapter adapter = null;

    int limitCount = 10;  //每页数量
    int skipCount = 0;  //跳过的数量

    @BindView(R.id.home_progressBar)
    ProgressBar mProgressBar;


    public static SimpleFragment getInstance() {
        SimpleFragment sf = new SimpleFragment();
        return sf;
    }

    @Override
    public int getLayoutId() {
        return R.layout.afeng_simple_one;
    }

    @Override
    protected void initView(Bundle savedInstanceState) {

        adapter = new oneAdapter(R.layout.afeng_one_item, listDatas);
        adapter.openLoadAnimation();  //默认渐隐动画

        if (getArguments() != null) {
            mType = getArguments().getString(TYPE);
        }

        //动态嵌入布局
        if (headerView == null) {
            headerView = View.inflate(getContext(), R.layout.afeng_fra_one_header, null);
        }

        mHomeMvBtn = (ImageButton) headerView.findViewById(R.id.home_mv_btn);

        initBanner();

    }

    @Override
    protected void setListener() {
        mHomeMvBtn.setOnClickListener(this);
        headerView.findViewById(R.id.comic_btn).setOnClickListener(this);
        headerView.findViewById(R.id.home_live).setOnClickListener(this);

        adapter.setOnLoadMoreListener(() -> initItemData());
    }

    @Override
    protected void processLogic(Bundle savedInstanceState) {

        initRecyclerView();
    }

    /**
     * 懒加载一次。如果只想在对用户可见时才加载数据，并且只加载一次数据，在子类中重写该方法
     */
    @Override
    protected void onLazyLoadOnce() {

    }

    /**
     * 对用户可见时触发该方法。如果只想在对用户可见时才加载数据，在子类中重写该方法
     */
    @Override
    protected void onVisibleToUser() {

        if (listDatas.size() == 0) {
            initItemData();
        }

        if (BannerDatas.size() == 0) {
            initBannerData();
        }

    }

    /**
     * 对用户不可见时触发该方法
     */
    @Override
    protected void onInvisibleToUser() {

    }


    private void initBannerData() {

//        AVObject todoFolder = new AVObject("HomeBannerItem");// 构建对象
//        todoFolder.put("img", "http://oki2v8p4s.bkt.clouddn.com/home_ban_4.png");
//        todoFolder.put("isAdv", 0);
//        todoFolder.put("isShow", 1);
//        todoFolder.put("desc", "花儿与少年 第三季 2017-06-25期");
//        todoFolder.put("url", "http://m.mgtv.com/#/b/312692/3989477?ref=");
//        todoFolder.put("jsCode", Constants.MGTV_JS_CODE);
//
//        todoFolder.saveInBackground();// 保存到服务端


        AVQuery<AVObject> avQuery = new AVQuery<>("HomeBannerItem");
        // 按时间，降序排列
        avQuery.orderByDescending("createdAt");
        avQuery.whereEqualTo("isShow", 1);  //确认为1时才下载显示
        avQuery.limit(5);// 最多返回 5 条结果
        avQuery.findInBackground(new FindCallback<AVObject>() {
            @Override
            public void done(List<AVObject> list, AVException e) {

                if (list.size() > 0) {
                    for (AVObject avObject : list) {

                        String desc = avObject.getString("desc");
                        String img = avObject.getString("img");
                        String url = avObject.getString("url");
                        String jsCode = avObject.getString("jsCode");

                        HomeBannerItem data = new HomeBannerItem();
                        data.setDesc(desc);
                        data.setJsCode(jsCode);
                        data.setUrl(url);
                        data.setImg(img);
                        BannerDatas.add(data);
                        BannerTips.add(desc);
                    }
                } else {

                }

                //绑定轮播图片与提示
                mBGABanner.setData(BannerDatas, BannerTips);
            }
        });


    }


    private void initBanner() {

        mBGABanner = (BGABanner) headerView.findViewById(R.id.banner_main);

        // 设置图片加载方法
        mBGABanner.setAdapter(new BGABanner.Adapter<ImageView, HomeBannerItem>() {

            @Override
            public void fillBannerItem(BGABanner bgaBanner, ImageView imageView, HomeBannerItem homeBannerItems, int i) {
                Glide.with(getActivity())
                        .load(homeBannerItems.getImg())
                        .placeholder(R.drawable.img_loading_git)  //临时占位图
                        .error(R.drawable.load_err)  //加载错误时显示图
                        .centerCrop()
                        .dontAnimate()
                        .into(imageView);
            }
        });

        // 设置点击事件
        mBGABanner.setDelegate(new BGABanner.Delegate<ImageView, HomeBannerItem>() {

            @Override
            public void onBannerItemClick(BGABanner bgaBanner, ImageView imageView, HomeBannerItem homeBannerItems, int i) {

                String url = homeBannerItems.getUrl();
                String jscode = homeBannerItems.getJsCode();

                EventBus.getDefault().postSticky(new WebEvent(jscode, url));
                SimpleFragment.this.startActivity(new Intent(SimpleFragment.this.getContext(), WebActivity.class));

            }
        });

    }

    private void initItemData() {

//        HomeListItem data = new HomeListItem();
//        data.setDesc("改编自日暮里漫画家岸本♂齐湿的同名漫画，于2069年6月9日在东茎电视台放送首勃♂");
//        data.setTitle("蕉忍♂疾风传");
//        data.setWatchCount(10);
//        data.setUrl("http://m.bilibili.com/video/av11009508.html");
//        data.setImg("http://oki2v8p4s.bkt.clouddn.com/home_list_01.png");
//        listDatas.add(data);
//
//        HomeListItem data2 = new HomeListItem();
//        data2.setDesc("讲述原作的故事完结后漩涡鸣人之子漩涡博人的冒险故事。");
//        data2.setTitle("博人传 火影忍者新时代");
//        data2.setWatchCount(100);
//        data2.setUrl("http://bangumi.bilibili.com/mobile/anime/5978/play/103308");
//        data2.setImg("http://oki2v8p4s.bkt.clouddn.com/home_list_2.jpg");
//        listDatas.add(data2);
//
//        // 第一参数是 className,第二个参数是 objectId
//        AVObject todoFolder = createWithoutData("HomeListItem","594fcdd4ac502e006c80d24d");// 构建对象
//        todoFolder.put("title", "博人传 火影忍者新时代");// 设置名称
//        todoFolder.put("img", "http://oki2v8p4s.bkt.clouddn.com/home_list_2.jpg");
//        todoFolder.put("desc", "讲述原作的故事完结后漩涡鸣人之子漩涡博人的冒险故事。");
//        todoFolder.put("url", "http://bangumi.bilibili.com/mobile/anime/5978/play/103308");
//        todoFolder.put("watchCount", 100);
//        todoFolder.saveInBackground();// 保存到服务端

        mProgressBar.setVisibility(View.VISIBLE);

        AVQuery<AVObject> avQuery = new AVQuery<>("HomeListItem");
        // 按时间，降序排列
        avQuery.orderByDescending("createdAt");
        avQuery.limit(limitCount);// 最多返回 10 条结果
        avQuery.skip(skipCount);// 跳过
        avQuery.findInBackground(new FindCallback<AVObject>() {
            @Override
            public void done(List<AVObject> list, AVException e) {

                if (list.size() > 0) {
                    for (AVObject avObject : list) {
                        String title = avObject.getString("title");// 读取 title
                        String img = avObject.getString("img");
                        String desc = avObject.getString("desc");
                        String url = avObject.getString("url");

                        String temp = avObject.getString("watchCount");
                        String watchCount = TextUtils.isEmpty(temp) ? "0" : temp;

                        HomeListItem data = new HomeListItem();
                        data.setDesc(desc);
                        data.setTitle(title);
                        data.setWatchCount(Integer.parseInt(watchCount));
                        data.setUrl(url);
                        data.setImg(img);
                        listDatas.add(data);
                    }
                    adapter.notifyDataSetChanged();
                    skipCount += limitCount;
                    adapter.loadMoreComplete();
                } else {
                    adapter.loadMoreEnd();
                }

                mProgressBar.setVisibility(View.GONE);
            }
        });


    }


    private void initRecyclerView() {

        mRecyclerView.setLayoutManager(new GridLayoutManager(getContext(), 2));

        mRecyclerView.setAdapter(adapter);

        adapter.addHeaderView(headerView);


        adapter.setOnItemClickListener((adapter1, view, position) -> {
            HomeListItem data = adapter.getData().get(position);
            String url2 = data.getUrl();
            EventBus.getDefault().postSticky(new WebEvent(Constants.BILI_JS_CODE, url2));
            startActivity(new Intent(getContext(), WebActivity.class));
        });

    }

    /**
     * Called when a view has been clicked.
     *
     * @param v The view that was clicked.
     */
    @Override
    public void onClick(View v) {
        switch (v.getId()) {
            case R.id.home_mv_btn:
                startActivity(new Intent(this.getContext(), BiliAgentWebActivity.class));
                break;

            case R.id.comic_btn:
                startActivity(new Intent(this.getContext(), ComicWebActivity.class));
                break;

            case R.id.home_live:
                startActivity(new Intent(this.getContext(), LiveWebActivity.class));
                break;
        }
    }


    class oneAdapter extends BaseQuickAdapter<HomeListItem, BaseViewHolder> {

        public oneAdapter(int layoutResId, List<HomeListItem> data) {
            super(layoutResId, data);
        }

        @Override
        protected void convert(BaseViewHolder helper, HomeListItem item) {

            ImageView img = helper.getView(R.id.home_item_img);
            String watch = item.getWatchCount() + "";

            helper.setText(R.id.home_item_title, item.getTitle())
                    .setText(R.id.home_item_desc, item.getDesc())
                    .setText(R.id.home_item_watch, watch);

            Glide.with(img.getContext())
                    .load(item.getImg())
                    .placeholder(R.drawable.loading_list_item)  //临时占位图
                    .error(R.drawable.load_err)  //加载错误时显示图
                    .fitCenter()
                    .into(img);

        }
    }


}