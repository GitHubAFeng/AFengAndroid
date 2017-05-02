package com.youth.xf.ui.demo.api;

import com.youth.xf.ui.demo.book.BookBean;

import java.util.List;

import io.reactivex.Observable;
import retrofit2.http.GET;

/**
 * Created by Administrator on 2017/5/2.
 */

public interface BookRemoteApi {

    @GET("Android/10/1")
    Observable<List<BookBean>> getData();

}
