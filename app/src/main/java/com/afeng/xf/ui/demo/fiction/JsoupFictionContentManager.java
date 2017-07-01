package com.afeng.xf.ui.demo.fiction;

import org.jsoup.Jsoup;
import org.jsoup.nodes.Document;
import org.jsoup.select.Elements;

import java.io.IOException;


/**
 * Created by Administrator on 2017/6/24.
 */

public class JsoupFictionContentManager {

    public static JsoupFictionContentManager get() {

        return new JsoupFictionContentManager();
    }

    // 取正文内容
    public FictionContentEvent getData(String pageurl) {

        Document document = null;
        try {
            document = Jsoup.connect(pageurl)
                    .timeout(10000)
                    .get();
        } catch (IOException e) {
            e.printStackTrace();
        }

        Elements selects = document.select("div.bottem1").select("a");

        FictionContentEvent model = new FictionContentEvent();
        model.setPrePageUrl(selects.get(0).attr("abs:href"));
        model.setNextPageUrl(selects.get(2).attr("abs:href"));
        model.setFictionContent(document.select("div#content").text());

        model.setTitle(document.select("div.bookname").select("h1").text());

        return model;
    }


}
