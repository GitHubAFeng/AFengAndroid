package com.afeng.xf.ui.demo.fiction;

/**
 * Created by Administrator on 2017/6/24.
 */

public class FictionContentEvent {

    private String fictionContent;
    private String prePageUrl;
    private String nextPageUrl;
    private String title;

    public FictionContentEvent() {
    }

    public FictionContentEvent(String fictionContent, String prePageUrl, String nextPageUrl, String title) {
        this.fictionContent = fictionContent;
        this.prePageUrl = prePageUrl;
        this.nextPageUrl = nextPageUrl;
        this.title = title;
    }

    public String getTitle() {
        return title;
    }

    public void setTitle(String title) {
        this.title = title;
    }

    public String getFictionContent() {
        return fictionContent;
    }

    public void setFictionContent(String fictionContent) {
        this.fictionContent = fictionContent;
    }

    public String getPrePageUrl() {
        return prePageUrl;
    }

    public void setPrePageUrl(String prePageUrl) {
        this.prePageUrl = prePageUrl;
    }

    public String getNextPageUrl() {
        return nextPageUrl;
    }

    public void setNextPageUrl(String nextPageUrl) {
        this.nextPageUrl = nextPageUrl;
    }
}
