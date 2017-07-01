package com.afeng.xf.utils.GlideHelper;

import com.bumptech.glide.load.model.GlideUrl;

/**
 * Created by Administrator on 2017/6/14.
 * 动态URL，适用于URL后面带token的动态地址，例如千牛云上的图片URL，防止重复缓存
 * 使用示例：
 *  Glide.with(this).load(new GlideDynamicUrl(url)).into(imageView);
 */

public class GlideDynamicUrl extends GlideUrl{


    private String mUrl;

    public GlideDynamicUrl(String url) {
        super(url);
        mUrl = url;
    }

    @Override
    public String getCacheKey() {
        return mUrl.replace(findTokenParam(), "");
    }

    private String findTokenParam() {
        String tokenParam = "";
        int tokenKeyIndex = mUrl.indexOf("?token=") >= 0 ? mUrl.indexOf("?token=") : mUrl.indexOf("&token=");
        if (tokenKeyIndex != -1) {
            int nextAndIndex = mUrl.indexOf("&", tokenKeyIndex + 1);
            if (nextAndIndex != -1) {
                tokenParam = mUrl.substring(tokenKeyIndex + 1, nextAndIndex + 1);
            } else {
                tokenParam = mUrl.substring(tokenKeyIndex);
            }
        }
        return tokenParam;
    }
}
