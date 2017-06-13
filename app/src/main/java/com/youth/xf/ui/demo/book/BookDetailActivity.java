package com.youth.xf.ui.demo.book;

import android.os.Bundle;
import android.widget.TextView;

import com.flaviofaria.kenburnsview.KenBurnsView;
import com.youth.xf.R;
import com.youth.xf.base.AFengActivity;

import org.greenrobot.eventbus.EventBus;
import org.greenrobot.eventbus.Subscribe;
import org.greenrobot.eventbus.ThreadMode;

import butterknife.BindView;
import io.reactivex.Observer;
import io.reactivex.android.schedulers.AndroidSchedulers;
import io.reactivex.disposables.Disposable;
import io.reactivex.schedulers.Schedulers;

/**
 * Created by Administrator on 2017/5/3.
 */

public class BookDetailActivity extends AFengActivity {


    @BindView(R.id.book_detail_des)
    TextView decsTextView;

    @BindView(R.id.book_detail_title_img)
    KenBurnsView mKenBurnsView;



    public static BookDetailActivity newInstance() {
        BookDetailActivity fragment = new BookDetailActivity();
        return fragment;
    }

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
        super.onDestroy();
        EventBus.getDefault().unregister(this);
    }


    @Subscribe(threadMode = ThreadMode.MAIN)
    public void onEventMainThread(String bookid) {
        if (bookid != null) {
            BookRepository.getInstance().getBookDetail(bookid,"",false)
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

//                            try {
//                                Bitmap myBitmap =ImgLoadUtil.getBitmapByUrl(getContext(),bookDetailBean.getImages().getLarge());
//                                mKenBurnsView.setImageBitmap(myBitmap);
//                                decsTextView.setText(bookDetailBean.getSummary());
//
//                            } catch (ExecutionException e) {
//                                e.printStackTrace();
//                            } catch (InterruptedException e) {
//                                e.printStackTrace();
//                            }
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

        }
    }


}
