package com.afeng.xf.ui.demo.book;

import android.graphics.Bitmap;
import android.os.Bundle;

import android.widget.TextView;

import com.flaviofaria.kenburnsview.KenBurnsView;
import com.afeng.xf.R;

import com.afeng.xf.base.BaseActivity;
import com.afeng.xf.utils.GlideHelper.ImgLoadUtil;
import com.afeng.xf.utils.AFengUtils.xToastUtil;

import org.greenrobot.eventbus.EventBus;
import org.greenrobot.eventbus.Subscribe;
import org.greenrobot.eventbus.ThreadMode;

import java.util.concurrent.ExecutionException;


import butterknife.BindView;
import io.reactivex.Observer;
import io.reactivex.android.schedulers.AndroidSchedulers;
import io.reactivex.disposables.Disposable;
import io.reactivex.schedulers.Schedulers;

import static com.afeng.xf.base.AFengConfig.getContext;

/**
 * Created by Administrator on 2017/5/3.
 */

public class BookDetailActivity extends BaseActivity {


    @BindView(R.id.book_detail_des)
    TextView decsTextView;

    @BindView(R.id.book_detail_author)
    TextView authorTextView;

    @BindView(R.id.book_detail_cata)
    TextView cataTextView;

    @BindView(R.id.book_detail_title_img)
    KenBurnsView mKenBurnsView;




    @Override
    protected int getLayoutId() {
        return R.layout.activity_book_detail;
    }

    @Override
    protected void initView(Bundle savedInstanceState) {


        if (!EventBus.getDefault().isRegistered(this)) {
            EventBus.getDefault().register(this);
        }
    }

    @Override
    protected void setListener() {

    }

    @Override
    protected void processLogic(Bundle savedInstanceState) {

    }

    /**
     * 懒加载一次。如果只想在对用户可见时才加载数据，并且只加载一次数据，在子类中重写该方法
     */
    @Override
    protected void onLazyLoadOnce() {

    }


    @Override
    public void onDestroy() {
        //移除所有的粘性事件
        EventBus.getDefault().removeAllStickyEvents();
        EventBus.getDefault().unregister(this);
        super.onDestroy();
    }



    //参数一致才能收到
    @Subscribe(threadMode = ThreadMode.MAIN, sticky = true)
    public void onEventMainThread(BookEvent event) {

        if (event.getbook() != null) {
            BookRepository.getInstance().getBookDetail(event.getbook().getId(), "", false)
                    .subscribeOn(Schedulers.io())
                    .unsubscribeOn(Schedulers.io())
                    .observeOn(AndroidSchedulers.mainThread())
                    .subscribe(new Observer<BookDetailBean>() {
                        Disposable d;

                        @Override
                        public void onSubscribe(Disposable disposable) {
                            d = disposable;
                        }

                        @Override
                        public void onNext(BookDetailBean bookDetailBean) {

                            decsTextView.setText(bookDetailBean.getSummary());
                            authorTextView.setText(bookDetailBean.getAuthor_intro());
                            cataTextView.setText(bookDetailBean.getCatalog());

                            new Thread(() -> {
                                try {
                                    Bitmap myBitmap = ImgLoadUtil.getBitmapByUrl(getContext(), bookDetailBean.getImages().getLarge());
                                    BookDetailActivity.this.runOnUiThread(()-> mKenBurnsView.setImageBitmap(myBitmap));
                                } catch (ExecutionException e) {
                                    e.printStackTrace();
                                } catch (InterruptedException e) {
                                    e.printStackTrace();
                                }
                            }).start();
                        }

                        @Override
                        public void onError(Throwable e) {
                            d.dispose();
                        }

                        @Override
                        public void onComplete() {
                            d.dispose();
                        }
                    });

        } else {

            xToastUtil.showToast("bookid为空");
        }
    }


    @Override
    public void onBackPressed() {
        super.onBackPressed();
        this.finish();
    }
}
