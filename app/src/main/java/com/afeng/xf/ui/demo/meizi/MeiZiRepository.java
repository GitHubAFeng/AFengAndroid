package com.afeng.xf.ui.demo.meizi;

import com.afeng.xf.ui.demo.api.HttpClient;
import com.afeng.xf.ui.demo.bean.GankIoDataBean;

import io.reactivex.Observable;


/**
 * Created by Administrator on 2017/5/7.
 */

public class MeiZiRepository {


    private HttpClient RemoteApi;

    private static MeiZiRepository instance;

    private MeiZiRepository() {
        RemoteApi = HttpClient.Builder.getGankIOServer();
    }

    public static MeiZiRepository getInstance() {
        if (instance == null) {
            synchronized (MeiZiRepository.class) {
                if (instance == null) {
                    instance = new MeiZiRepository();
                }
            }
        }
        return instance;
    }


    /**
     * @param update 是否更新,如果设置为true，缓存数据将被清理，并且向服务器请求数据
     * @return
     */
    public Observable<GankIoDataBean> getGankIoData(String id, int page, int pre_page, String key, final boolean update) {
        //如果update为true,将会重新获取数据并清理缓存。
        return RemoteApi.getGankIoData(id, page, pre_page);
    }


}
