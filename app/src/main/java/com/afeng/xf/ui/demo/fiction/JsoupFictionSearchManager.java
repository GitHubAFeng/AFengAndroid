package com.afeng.xf.ui.demo.fiction;

import android.text.TextUtils;

import com.orhanobut.logger.Logger;

import org.jsoup.Jsoup;
import org.jsoup.nodes.Document;
import org.jsoup.nodes.Element;
import org.jsoup.select.Elements;

import java.io.IOException;
import java.util.ArrayList;
import java.util.List;

/**
 * Created by Administrator on 2017/6/30.
 */

public class JsoupFictionSearchManager {

    //搜索
    String SEARCH_URL = "http://zhannei.baidu.com/cse/search?q=";
    String SEARCH_SUFFIX = "&s=15390153038627446418&nsid=";

//    http://zhannei.baidu.com/cse/search?s=15390153038627446418&ie=gbk&q=%CA%A5%D0%E6
//    http://zhannei.baidu.com/cse/search?q=虎牙&s=15390153038627446418&srt=def&nsid=0
//    http://zhannei.baidu.com/cse/search?s=15390153038627446418&ie=gbk&q=%B9%FE%B9%FE
//    http://zhannei.baidu.com/cse/search?q=世界&click=1&s=15390153038627446418&nsid=

//    http://zhannei.baidu.com/cse/search?q=测试&p=0&s=15390153038627446418&nsid=
//    http://zhannei.baidu.com/cse/search?q=测试&p=1&s=15390153038627446418&nsid=
//    http://zhannei.baidu.com/cse/search?q=测试&p=2&s=15390153038627446418&nsid=


    public static JsoupFictionSearchManager get() {

        return new JsoupFictionSearchManager();
    }


    public List<FictionModel> getData(String name, int page) {

        String pageurl = SEARCH_URL + name + "&p=" + page + SEARCH_SUFFIX;

        List<FictionModel> list = new ArrayList<>();

        if (TextUtils.isEmpty(pageurl)) {
            throw new NullPointerException("URL为空！");
        }

        try {

            Document document = Jsoup.connect(pageurl)
                    .timeout(10000)
                    .get();

            if (document.baseUri().equals("https://www.baidu.com/search/error.html")) {
                Logger.e("搜索失败");
            } else {

                FictionModel kswModel;

                if (document != null) {
                    Elements selects = document.select("div.result-list");
                    for (Element element : selects) {
                        Elements items = element.select("div.result-item.result-game-item");
                        for (Element item : items) {

                            kswModel = new FictionModel();

                            kswModel.setTitle(item.select("a.result-game-item-title-link").attr("title"));  //书名
                            kswModel.setDesc(item.select("div.result-game-item-detail").select("p.result-game-item-desc").get(0).text());  //简介
                            kswModel.setAuthor(item.select("p.result-game-item-info-tag").get(0).select("span").text());  //作者
                            kswModel.setTime(item.select("p.result-game-item-info-tag").get(2).select("span.result-game-item-info-tag-title").text());  //更新时间
                            kswModel.setLastChapter(item.select("p.result-game-item-info-tag").get(3).select("a.result-game-item-info-tag-item").text());  //最后章节
                            kswModel.setChapterUrl(item.select("p.result-game-item-info-tag").get(2).select("a.result-game-item-info-tag-item").attr("href"));   //最后章节URL

                            kswModel.setDetailUrl(item.select("a.result-game-item-title-link").attr("href"));  //小说链接
                            kswModel.setCoverImg(item.select("img.result-game-item-pic-link-img").get(0).attr("src"));  //封面

                            list.add(kswModel);

                        }
                    }
                }
            }

        } catch (IOException e) {
            e.printStackTrace();
        }

        return list;
    }


}
