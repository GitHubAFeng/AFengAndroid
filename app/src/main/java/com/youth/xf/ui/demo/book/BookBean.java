package com.youth.xf.ui.demo.book;

import java.io.Serializable;
import java.util.Arrays;
import java.util.List;

/**
 * Created by Administrator on 2017/4/28.
 */

public class BookBean implements Serializable {
    private static final long serialVersionUID = -2727300739188328907L;
    /**
     * error : false
     * results : [{"_id":"58fdb71d421aa954511ebeff","createdAt":"2017-04-24T16:28:13.266Z","desc":"A Complete Guide To Learn RxJava","publishedAt":"2017-04-28T11:49:38.12Z","source":"web","type":"Android","url":"https://blog.mindorks.com/a-complete-guide-to-learn-rxjava-b55c0cea3631","used":true,"who":"AMIT SHEKHAR"},{"_id":"5901c025421aa954511ebf1f","createdAt":"2017-04-27T17:55:49.206Z","desc":"正常情况下，每个子线程完成各自的任务就可以结束了。不过有的时候，我们希望多个线程协同工作来完成某个任务，这时就涉及到了线程间通信了。","publishedAt":"2017-04-28T11:49:38.12Z","source":"web","type":"Android","url":"http://wingjay.com/2017/04/09/Java%E9%87%8C%E5%A6%82%E4%BD%95%E5%AE%9E%E7%8E%B0%E7%BA%BF%E7%A8%8B%E9%97%B4%E9%80%9A%E4%BF%A1%EF%BC%9F/","used":true,"who":"wingjay"},{"_id":"590237a4421aa9544b77408d","createdAt":"2017-04-28T02:25:40.420Z","desc":"Understanding Android Core: Looper, Handler, and HandlerThread","publishedAt":"2017-04-28T11:49:38.12Z","source":"web","type":"Android","url":"https://blog.mindorks.com/android-core-looper-handler-and-handlerthread-bd54d69fe91a","used":true,"who":"AMIT SHEKHAR"},{"_id":"5902add0421aa9544825f928","createdAt":"2017-04-28T10:49:52.350Z","desc":"效果很棒的一款 Float Text Placeholder 效果组件","publishedAt":"2017-04-28T11:49:38.12Z","source":"chrome","type":"Android","url":"https://github.com/rafakob/FloatingEditText","used":true,"who":"代码家"},{"_id":"59007c87421aa954511ebf15","createdAt":"2017-04-26T18:55:03.485Z","desc":"深入理解ServiceManager","publishedAt":"2017-04-27T13:16:46.955Z","source":"web","type":"Android","url":"https://pqpo.me/2017/04/26/learn-servicemanager/","used":true,"who":"Linmin Qiu"},{"_id":"59008fa3421aa9544825f919","createdAt":"2017-04-26T20:16:35.816Z","desc":"浅谈RxJava中的线程管理","publishedAt":"2017-04-27T13:16:46.955Z","source":"web","type":"Android","url":"http://www.zjutkz.net/2017/04/26/%E6%B5%85%E8%B0%88RxJava%E4%B8%AD%E7%9A%84%E7%BA%BF%E7%A8%8B%E7%AE%A1%E7%90%86/","used":true,"who":null},{"_id":"5900daa7421aa9544825f91b","createdAt":"2017-04-27T01:36:39.650Z","desc":"Android Development Useful Tools","publishedAt":"2017-04-27T13:16:46.955Z","source":"web","type":"Android","url":"https://blog.mindorks.com/android-development-useful-tools-fd73283e82e3","used":true,"who":"AMIT SHEKHAR"},{"_id":"59013172421aa9544ed889f1","createdAt":"2017-04-27T07:46:58.470Z","desc":"图片阴影新玩法，让阴影不再单调","images":["http://img.gank.io/a5ace6ba-1349-492e-ab5e-edbfbab4d34a"],"publishedAt":"2017-04-27T13:16:46.955Z","source":"web","type":"Android","url":"https://github.com/DingMouRen/PaletteImageView","used":true,"who":null},{"_id":"59017cb9421aa9544ed889f8","createdAt":"2017-04-27T13:08:09.279Z","desc":"又一个漂亮的日历组件","images":["http://img.gank.io/1af5b311-d09e-4b21-873a-d15be6fc879d"],"publishedAt":"2017-04-27T13:16:46.955Z","source":"chrome","type":"Android","url":"https://github.com/mahendramahi/CalendarView","used":true,"who":"代码家"},{"_id":"58ff6fbc421aa9544825f90e","createdAt":"2017-04-25T23:48:12.455Z","desc":"Amazing Open Source Android Apps","publishedAt":"2017-04-26T11:30:43.767Z","source":"web","type":"Android","url":"https://blog.mindorks.com/android-amazing-open-source-apps-e44f520593cc","used":true,"who":"AMIT SHEKHAR"}]
     */

    private boolean error;
    private List<BookBean.ResultsBean> results;

    public boolean isError() {
        return error;
    }

    public void setError(boolean error) {
        this.error = error;
    }

    public List<BookBean.ResultsBean> getResults() {
        return results;
    }

    public void setResults(List<BookBean.ResultsBean> results) {
        this.results = results;
    }

    public static class ResultsBean {
        /**
         * _id : 58fdb71d421aa954511ebeff
         * createdAt : 2017-04-24T16:28:13.266Z
         * desc : A Complete Guide To Learn RxJava
         * publishedAt : 2017-04-28T11:49:38.12Z
         * source : web
         * type : Android
         * url : https://blog.mindorks.com/a-complete-guide-to-learn-rxjava-b55c0cea3631
         * used : true
         * who : AMIT SHEKHAR
         * images : ["http://img.gank.io/a5ace6ba-1349-492e-ab5e-edbfbab4d34a"]
         */

        private String _id;
        private String createdAt;
        private String desc;
        private String publishedAt;
        private String source;
        private String type;
        private String url;
        private boolean used;
        private String who;
        private List<String> images;

        public String get_id() {
            return _id;
        }

        public void set_id(String _id) {
            this._id = _id;
        }

        public String getCreatedAt() {
            return createdAt;
        }

        public void setCreatedAt(String createdAt) {
            this.createdAt = createdAt;
        }

        public String getDesc() {
            return desc;
        }

        public void setDesc(String desc) {
            this.desc = desc;
        }

        public String getPublishedAt() {
            return publishedAt;
        }

        public void setPublishedAt(String publishedAt) {
            this.publishedAt = publishedAt;
        }

        public String getSource() {
            return source;
        }

        public void setSource(String source) {
            this.source = source;
        }

        public String getType() {
            return type;
        }

        public void setType(String type) {
            this.type = type;
        }

        public String getUrl() {
            return url;
        }

        public void setUrl(String url) {
            this.url = url;
        }

        public boolean isUsed() {
            return used;
        }

        public void setUsed(boolean used) {
            this.used = used;
        }

        public String getWho() {
            return who;
        }

        public void setWho(String who) {
            this.who = who;
        }

        public List<String> getImages() {
            return images;
        }

        public void setImages(List<String> images) {
            this.images = images;
        }
    }
}
