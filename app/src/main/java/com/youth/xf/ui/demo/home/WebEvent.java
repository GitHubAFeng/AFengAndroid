package com.youth.xf.ui.demo.home;

/**
 * Created by Administrator on 2017/6/23.
 */

public class WebEvent {

    private String injectJS;
    private String webUrl;
    private String imgUrl;

    public WebEvent(String injectJS, String webUrl, String imgUrl) {
        this.injectJS = injectJS;
        this.webUrl = webUrl;
        this.imgUrl = imgUrl;
    }

    public WebEvent(String injectJS, String webUrl) {
        this.injectJS = injectJS;
        this.webUrl = webUrl;
    }

    public String getImgUrl() {
        return imgUrl;
    }

    public void setImgUrl(String imgUrl) {
        this.imgUrl = imgUrl;
    }

    public String getInjectJS() {
        return injectJS;
    }

    public void setInjectJS(String injectJS) {
        this.injectJS = injectJS;
    }

    public String getWebUrl() {
        return webUrl;
    }

    public void setWebUrl(String webUrl) {
        this.webUrl = webUrl;
    }
}
