package com.afeng.xf.ui.meizi;


import android.app.WallpaperManager;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.graphics.Color;
import android.os.AsyncTask;
import android.os.Bundle;
import android.os.Environment;
import android.support.design.widget.FloatingActionButton;
import android.support.v4.view.PagerAdapter;
import android.support.v4.view.ViewPager;
import android.support.v7.widget.Toolbar;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.ProgressBar;
import android.widget.TextView;
import android.widget.Toast;

import com.afeng.xf.base.BaseActivity;
import com.bumptech.glide.Glide;
import com.bumptech.glide.load.engine.DiskCacheStrategy;
import com.bumptech.glide.load.resource.drawable.GlideDrawable;
import com.bumptech.glide.request.RequestListener;
import com.bumptech.glide.request.target.Target;
import com.afeng.xf.R;
import com.afeng.xf.base.App;
import com.afeng.xf.utils.AFengUtils.xToastUtil;
import com.afeng.xf.utils.GlideHelper.ImgLoadUtil;
import com.afeng.xf.widget.bottomsheetdialog.xBottomMenuDialog;
import com.afeng.xf.widget.downloadingview.GADownloadingView;
import com.afeng.xf.widget.snackbarlight.Light;

import org.greenrobot.eventbus.EventBus;
import org.greenrobot.eventbus.Subscribe;
import org.greenrobot.eventbus.ThreadMode;

import java.io.IOException;
import java.util.List;


import butterknife.BindView;
import uk.co.senab.photoview.PhotoView;
import uk.co.senab.photoview.PhotoViewAttacher;

import static com.afeng.xf.utils.GlideHelper.GlideUtils.getImagePath;
import static com.afeng.xf.utils.GlideHelper.ImgLoadUtil.saveImageToGallery;


/**
 * Created by Administrator on 2017/5/7.
 */

public class MeiZiBigImageActivity extends BaseActivity implements ViewPager.OnPageChangeListener, PhotoViewAttacher.OnPhotoTapListener {


    // 保存图片
    @BindView(R.id.meizi_toolbar_title)
    TextView mToolbartitle;
    // 接收传过来的uri地址
    List<String> imageuriList;

    @BindView(R.id.meizi_fab)
    FloatingActionButton mTabBtn;

    @BindView(R.id.meizi_toolbar)
    Toolbar mToolbar;

    private int mProgress;  //下载进度

    int currentPageIndex;  //当前ViewPage的下标

    /**
     * 显示当前图片的页数
     */
    @BindView(R.id.meizi_image_viewpager_text)
    TextView image_viewpager_text;

    @BindView(R.id.ga_downloading)
    GADownloadingView mGADownloadingView;

    // 用于管理图片的滑动
    @BindView(R.id.meizi_image_viewpager)
    ViewPager image_viewpager;

    //保存到该相册专辑
    final String albumName = "AFeng相册";

    ViewPagerAdapter adapter;


    /**
     * 初始化布局,返回layout
     */
    @Override
    protected int getLayoutId() {
        return R.layout.activity_meizi_bigimage;
    }

    /**
     * 初始化布局以及View控件
     *
     * @param savedInstanceState
     */
    @Override
    protected void initView(Bundle savedInstanceState) {

        if (!EventBus.getDefault().isRegistered(this)) {
            EventBus.getDefault().register(this);
        }

        // 设置标题栏
        mToolbar.setTitleTextColor(Color.WHITE);
        mToolbar.setTitle("");
        if (getSupportActionBar() != null) {
            getSupportActionBar().setDisplayHomeAsUpEnabled(true);
        }
        this.setSupportActionBar(mToolbar);

        // 设置了回退按钮，及点击事件的效果
        getSupportActionBar().setDisplayHomeAsUpEnabled(true);
        mToolbar.setNavigationOnClickListener(v -> finish());

    }

    /**
     * 给View控件添加事件监听器
     */
    @Override
    protected void setListener() {

    }

    /**
     * 处理业务逻辑，状态恢复等操作
     *
     * @param savedInstanceState
     */
    @Override
    protected void processLogic(Bundle savedInstanceState) {

    }


    @Override
    public void onPageScrolled(int position, float positionOffset, int positionOffsetPixels) {

    }


    // position 为当前单元的下标
    @Override
    public void onPageSelected(int position) {
        // 每当页数发生改变时重新设定一遍当前的页数和总页数
        image_viewpager_text.setText((position + 1) + " / " + imageuriList.size());
        currentPageIndex = position;

    }


    @Override
    public void onPageScrollStateChanged(int state) {

    }

    //退出
    @Override
    public void onPhotoTap(View view, float v, float v1) {
        finish();
    }

    @Override
    public void onOutsidePhotoTap() {

    }


    @Override
    public void onDestroy() {
        MeiziEvent stickyEvent = EventBus.getDefault().getStickyEvent(MeiziEvent.class);
        if (stickyEvent != null) {
            EventBus.getDefault().removeStickyEvent(stickyEvent);
        }

        EventBus.getDefault().unregister(this);
        super.onDestroy();
    }


    @Subscribe(threadMode = ThreadMode.MAIN, sticky = true)
    public void ReceviceMessage(MeiziEvent event) {
        if (adapter == null) {
            adapter = new ViewPagerAdapter();
        }

        if (imageuriList == null) {
            imageuriList = event.getImgData();
        }

        image_viewpager.setAdapter(adapter);
        image_viewpager.setCurrentItem(event.getIndex());
        image_viewpager.addOnPageChangeListener(this);
        image_viewpager.setEnabled(false);

        // 设定当前的页数和总页数
        image_viewpager_text.setText((event.getIndex() + 1) + " / " + imageuriList.size());
        currentPageIndex = event.getIndex();
    }

    //保存图片
    private void toDownLoadImg() {

        new Thread(() -> {

            //TODO  通过缓存获取


            // 子线程获得图片路径
            final String imagePath = getImagePath(imageuriList.get(currentPageIndex));
            Bitmap bitmap = BitmapFactory.decodeFile(imagePath, new BitmapFactory.Options());
            boolean saveBitmapToSD = ImgLoadUtil.saveImageToGallery(bitmap, albumName);  //存入手机

            // 主线程更新
            MeiZiBigImageActivity.this.runOnUiThread(() -> {
                if (imagePath != null) {
                    if (bitmap != null) {
                        if (saveBitmapToSD) {
                            String tempAdd = "已保存至" + Environment.getExternalStorageDirectory().getAbsolutePath() + "/" + albumName;
                            Light.success(mTabBtn, "保存成功," + tempAdd, Light.LENGTH_SHORT).show();
                        } else {
                            Light.success(mTabBtn, "保存失败!", Light.LENGTH_SHORT).show();
                        }
                    }
                }
            });
        }).start();

        // 显示进度条的下载，但这个下载图片的速度太快了，不适合用
//            new downImgTask().execute();

    }


    //设置系统壁纸
    public void setWallpaper() {

        Light.info(mTabBtn, "正在设置壁纸...", Light.LENGTH_SHORT).show();

        new Thread(() -> {

            try {
                // 子线程获得图片路径
                final String imagePath = getImagePath(imageuriList.get(currentPageIndex));
                Bitmap bitmap = BitmapFactory.decodeFile(imagePath, new BitmapFactory.Options());

                if (bitmap != null) {
                    WallpaperManager manager = WallpaperManager.getInstance(App.getInstance());
                    manager.setBitmap(bitmap);
                }

            } catch (IOException e) {
                e.printStackTrace();
                xLogger(e.getMessage());
            }
        }).start();

    }


    /**
     * ViewPager的适配器
     * 网络图片
     *
     * @author guolin
     */
    class ViewPagerAdapter extends PagerAdapter {

        LayoutInflater inflater;

        ViewPagerAdapter() {
            inflater = getLayoutInflater();
        }

        xBottomMenuDialog dialog = new xBottomMenuDialog.BottomMenuBuilder()
                .addItem("下载图片", v -> toDownLoadImg())
                .addItem("设为壁纸", v -> setWallpaper())
                .build();


        @Override
        public Object instantiateItem(ViewGroup container, int position) {
            View view = inflater.inflate(R.layout.viewpager_meizi_image, container, false);
            final PhotoView zoom_image_view = (PhotoView) view.findViewById(R.id.zoom_image_view);
            final ProgressBar spinner = (ProgressBar) view.findViewById(R.id.loading);


            zoom_image_view.setOnLongClickListener(v -> {
                dialog.show(getSupportFragmentManager());
                //当return返回值为true的时候，代表这个事件已经消耗完了，返回值为false的时候他还会继续传递，结果再加上一个短按。
                return true;
            });


            // 保存网络图片的路径
            String imageUrl = (String) getItem(position);

            spinner.setVisibility(View.VISIBLE);
            spinner.setClickable(false);
            Glide.with(MeiZiBigImageActivity.this).load(imageUrl)
                    .diskCacheStrategy(DiskCacheStrategy.ALL)
                    .crossFade(700)
                    .listener(new RequestListener<String, GlideDrawable>() {
                        @Override
                        public boolean onException(Exception e, String model, Target<GlideDrawable> target, boolean isFirstResource) {
                            Toast.makeText(getApplicationContext(), "资源加载异常", Toast.LENGTH_SHORT).show();
                            spinner.setVisibility(View.GONE);
                            return false;
                        }

                        //这个用于监听图片是否加载完成
                        @Override
                        public boolean onResourceReady(GlideDrawable resource, String model, Target<GlideDrawable> target, boolean isFromMemoryCache, boolean isFirstResource) {

                            spinner.setVisibility(View.GONE);

                            /**这里应该是加载成功后图片的高*/
                            int height = zoom_image_view.getHeight();

                            int wHeight = getWindowManager().getDefaultDisplay().getHeight();
                            if (height > wHeight) {
                                zoom_image_view.setScaleType(ImageView.ScaleType.CENTER_CROP);
                            } else {
                                zoom_image_view.setScaleType(ImageView.ScaleType.FIT_CENTER);
                            }
                            return false;
                        }
                    }).into(zoom_image_view);

            zoom_image_view.setOnPhotoTapListener(MeiZiBigImageActivity.this);
            container.addView(view, 0);
            return view;
        }

        @Override
        public int getCount() {
            if (imageuriList == null || imageuriList.size() == 0) {
                return 0;
            }
            return imageuriList.size();
        }

        @Override
        public boolean isViewFromObject(View arg0, Object arg1) {
            return arg0 == arg1;
        }

        @Override
        public void destroyItem(ViewGroup container, int position, Object object) {
            View view = (View) object;
            container.removeView(view);
        }

        Object getItem(int position) {
            return imageuriList.get(position);
        }
    }


    //参数1 表示传入的值， Integer  代表进度 ，  参数3 代表返回的值
    class downImgTask extends AsyncTask<Void, Integer, String> {

        @Override
        protected void onPreExecute() {
            super.onPreExecute();
            //任务执行之前的准备工作。
//            mGADownloadingView.performAnimation();
        }

        @Override
        protected String doInBackground(Void... params) {
            //后台线程下载耗时操作

            // 子线程获得图片路径
            return getImagePath(imageuriList.get(currentPageIndex));
        }

        @Override
        protected void onProgressUpdate(Integer... values) {
            super.onProgressUpdate(values);
            //更新进度条
//            mGADownloadingView.updateProgress(values[0]);
        }

        @Override
        protected void onPostExecute(String imagePath) {
            super.onPostExecute(imagePath);
            //下载完成后UI操作
            if (imagePath != null) {
                Bitmap bitmap = BitmapFactory.decodeFile(imagePath, new BitmapFactory.Options());
                if (bitmap != null) {
                    saveImageToGallery(MeiZiBigImageActivity.this, bitmap, albumName);
                    xToastUtil.showToast("已保存至" + Environment.getExternalStorageDirectory().getAbsolutePath() + "/" + albumName);
                }
            }

//            mGADownloadingView.releaseAnimation();

        }
    }

}
