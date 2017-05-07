package com.youth.xf.ui.demo.meizi;


import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.os.Bundle;
import android.os.Environment;
import android.support.v4.view.PagerAdapter;
import android.support.v4.view.ViewPager;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.ProgressBar;
import android.widget.TextView;
import android.widget.Toast;

import com.bumptech.glide.Glide;
import com.bumptech.glide.load.resource.drawable.GlideDrawable;
import com.bumptech.glide.request.RequestListener;
import com.bumptech.glide.request.target.Target;
import com.youth.xf.R;
import com.youth.xf.base.AFengActivity;
import com.youth.xf.utils.ToastUtil;

import java.util.List;


import butterknife.BindView;
import uk.co.senab.photoview.PhotoView;
import uk.co.senab.photoview.PhotoViewAttacher;

import static com.youth.xf.utils.GlideHelper.GlideUtils.getImagePath;
import static com.youth.xf.utils.GlideHelper.ImgLoadUtil.saveImageToGallery;

/**
 * Created by Administrator on 2017/5/7.
 */

public class MeiZiBigImageActivity extends AFengActivity implements ViewPager.OnPageChangeListener, PhotoViewAttacher.OnPhotoTapListener {


    // 保存图片
    @BindView(R.id.to_save_big_image)
    TextView to_save_big_image;
    // 接收传过来的uri地址
    List<String> imageuri;

    /**
     * 显示当前图片的页数
     */
    @BindView(R.id.meizi_image_viewpager_text)
    TextView image_viewpager_text;

    // 用于管理图片的滑动
    @BindView(R.id.meizi_image_viewpager)
    ViewPager image_viewpager;

    //保存到该相册专辑
    String albumName = "AFeng相册";


    /**
     * 用于判断是否是加载本地图片
     */
    private boolean isLocal;

    ViewPagerAdapter adapter;

    // 当前页数
    private int page;

    // 接收穿过来当前选择的图片的数量
    int code;
    // 用于判断是头像还是文章图片 1:头像 2：文章大图
    int selet;

    /**
     * 本应用图片的id
     */
    private int imageId;
    /**
     * 是否是本应用中的图片
     */
    private boolean isApp;


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
        getView();
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


    /**
     * This method will be invoked when the current page is scrolled, either as part
     * of a programmatically initiated smooth scroll or a user initiated touch scroll.
     *
     * @param position             Position index of the first page currently being displayed.
     *                             Page position+1 will be visible if positionOffset is nonzero.
     * @param positionOffset       Value from [0, 1) indicating the offset from the page at position.
     * @param positionOffsetPixels Value in pixels indicating the offset from position.
     */
    @Override
    public void onPageScrolled(int position, float positionOffset, int positionOffsetPixels) {

    }

    /**
     * This method will be invoked when a new page becomes selected. Animation is not
     * necessarily complete.
     * 本方法主要监听viewpager滑动的时候的操作
     *
     * @param position Position index of the new selected page.
     */
    @Override
    public void onPageSelected(int position) {
        // 每当页数发生改变时重新设定一遍当前的页数和总页数
        image_viewpager_text.setText((position + 1) + " / " + imageuri.size());
        page = position;

    }

    /**
     * Called when the scroll state changes. Useful for discovering when the user
     * begins dragging, when the pager is automatically settling to the current page,
     * or when it is fully stopped/idle.
     *
     * @param state The new scroll state.
     * @see ViewPager#SCROLL_STATE_IDLE
     * @see ViewPager#SCROLL_STATE_DRAGGING
     * @see ViewPager#SCROLL_STATE_SETTLING
     */
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


    //接收控件
    private void getView() {
        /************************* 接收控件 ***********************/

        to_save_big_image.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {

                ToastUtil.showToast("开始下载图片");
                if (isApp) {// 本地图片
                    Bitmap bitmap = BitmapFactory.decodeResource(getResources(), imageId);
                    if (bitmap != null) {
                        saveImageToGallery(MeiZiBigImageActivity.this, bitmap, albumName);
                        ToastUtil.showToast("保存成功");
//                        Toast.makeText(ViewBigImageActivity.this, "保存成功", Toast.LENGTH_SHORT).show();
                    }

                } else {// 网络图片
                    final BitmapFactory.Options bmOptions = new BitmapFactory.Options();
                    new Thread(new Runnable() {
                        @Override
                        public void run() {
                            // 子线程获得图片路径
                            final String imagePath = getImagePath(MeiZiBigImageActivity.this, imageuri.get(page));
                            // 主线程更新
                            MeiZiBigImageActivity.this.runOnUiThread(new Runnable() {
                                @Override
                                public void run() {
                                    if (imagePath != null) {
                                        Bitmap bitmap = BitmapFactory.decodeFile(imagePath, bmOptions);
                                        if (bitmap != null) {
                                            saveImageToGallery(MeiZiBigImageActivity.this, bitmap, albumName);
                                            ToastUtil.showToast("已保存至" + Environment.getExternalStorageDirectory().getAbsolutePath() + "/" + albumName);
//                                            Toast.makeText(ViewBigImageActivity.this, "保存成功", Toast.LENGTH_SHORT).show();
                                        }
                                    }
                                }
                            });
                        }
                    }).start();
                }
            }
        });
        /************************* 接收传值 ***********************/
        Bundle bundle = getIntent().getExtras();
        code = bundle.getInt("code");
        selet = bundle.getInt("selet");
        isLocal = bundle.getBoolean("isLocal", false);
        imageuri = bundle.getStringArrayList("imageuri");
        /**是否是本应用中的图片*/
        isApp = bundle.getBoolean("isApp", false);
        /**本应用图片的id*/
        imageId = bundle.getInt("id", 0);

        /**
         * 给viewpager设置适配器
         */
        if (isApp) {
            MyPageAdapter myPageAdapter = new MyPageAdapter();
            image_viewpager.setAdapter(myPageAdapter);
            image_viewpager.setEnabled(false);
        } else {
            adapter = new ViewPagerAdapter();
            image_viewpager.setAdapter(adapter);
            image_viewpager.setCurrentItem(code);
            page = code;
            image_viewpager.setOnPageChangeListener(this);
            image_viewpager.setEnabled(false);
            // 设定当前的页数和总页数
            if (selet == 2) {
                image_viewpager_text.setText((code + 1) + " / " + imageuri.size());
            }
        }
    }


    /**
     * 本地应用图片适配器
     */

    class MyPageAdapter extends PagerAdapter {

        @Override
        public int getCount() {
            return 1;
        }

        @Override
        public boolean isViewFromObject(View view, Object object) {
            return view == object;
        }

        @Override
        public Object instantiateItem(ViewGroup container, int position) {
            View view = getLayoutInflater().inflate(R.layout.viewpager_meizi_image, container, false);
            PhotoView zoom_image_view = (PhotoView) view.findViewById(R.id.zoom_image_view);
            ProgressBar spinner = (ProgressBar) view.findViewById(R.id.loading);
            spinner.setVisibility(View.GONE);
            if (imageId != 0) {
                zoom_image_view.setImageResource(imageId);
            }
            zoom_image_view.setOnPhotoTapListener(MeiZiBigImageActivity.this);
            container.addView(view, 0);
            return view;
        }

        @Override
        public void destroyItem(ViewGroup container, int position, Object object) {
            View view = (View) object;
            container.removeView(view);
        }
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

        @Override
        public Object instantiateItem(ViewGroup container, int position) {
            View view = inflater.inflate(R.layout.viewpager_meizi_image, container, false);
            final PhotoView zoom_image_view = (PhotoView) view.findViewById(R.id.zoom_image_view);
            final ProgressBar spinner = (ProgressBar) view.findViewById(R.id.loading);
            // 保存网络图片的路径
            String adapter_image_Entity = (String) getItem(position);
            //TODO
            String imageUrl;
            if (isLocal) {
                imageUrl = "file://" + adapter_image_Entity;
                to_save_big_image.setVisibility(View.GONE);
            } else {
                imageUrl = adapter_image_Entity;
            }

            spinner.setVisibility(View.VISIBLE);
            spinner.setClickable(false);
            Glide.with(MeiZiBigImageActivity.this).load(imageUrl)
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
//                            Toast.makeText(getApplicationContext(), "图片加载完成", Toast.LENGTH_SHORT).show();
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
            if (imageuri == null || imageuri.size() == 0) {
                return 0;
            }
            return imageuri.size();
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
            return imageuri.get(position);
        }
    }


}
