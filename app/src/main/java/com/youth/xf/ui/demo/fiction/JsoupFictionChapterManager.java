package com.youth.xf.ui.demo.fiction;

import android.text.TextUtils;

import org.jsoup.Jsoup;
import org.jsoup.nodes.Document;
import org.jsoup.nodes.Element;
import org.jsoup.select.Elements;

import java.io.IOException;
import java.util.ArrayList;
import java.util.List;

/**
 * Created by Administrator on 2017/6/23.
 */

public class JsoupFictionChapterManager {

    public static JsoupFictionChapterManager get() {

        return new JsoupFictionChapterManager();
    }


    public List<FictionModel> getData(String pageurl) {
        List<FictionModel> list = new ArrayList<>();
        initHeader(list, pageurl);

        return list;
    }


    // 取章节
    private void initHeader(List<FictionModel> list, String pageurl) {

        if (TextUtils.isEmpty(pageurl)) {
            throw new NullPointerException("URL为空！");
        }

        Document document = null;
        try {

                document = Jsoup.connect(pageurl)
                        .timeout(10000)
                        .get();
        } catch (IOException e) {
            e.printStackTrace();
        }

        FictionModel kswModel;
        Elements select = document.select("div#list").select("dd");
        for (Element element : select) {
            kswModel = new FictionModel();
            kswModel.setChapterName(element.select("a").text());  //章节名字
            kswModel.setChapterUrl(element.select("a").attr("abs:href"));  //章节链接
            kswModel.setType(100);  //标记为章节
            list.add(kswModel);
        }


        kswModel = new FictionModel();
        kswModel.setTitle(document.select("div#maininfo").select("div#info").select("h1").text());  //书名
        kswModel.setDesc(document.select("div#maininfo").select("div#intro").select("p").get(0).text());  //简介
        kswModel.setType(99);  //标记为书信息

        Elements infoSelect = document.select("div#maininfo").select("div#info").select("p");
        kswModel.setAuthor(infoSelect.get(0).text());  //作者
        kswModel.setState(infoSelect.get(1).text());  //状态
        kswModel.setTime(infoSelect.get(2).text());  //时间
        kswModel.setLastChapter(infoSelect.get(3).select("a").text());  //最后章节
        kswModel.setUrl(infoSelect.get(3).select("a").attr("abs:href")); //最后章节URL
        kswModel.setCoverImg(document.select("div#fmimg").select("img[src]").attr("src"));  //封面


//
//        for (Element element : infoSelect) {
//
//            if (element.text().contains("作")) {
//                kswModel.setAuthor(element.text());  //作者
//            }
//
//            if (element.text().contains("状")) {
//                kswModel.setState(element.text());  //状态
//            }
//
//            if (element.text().contains("后")) {
//                kswModel.setTime(element.text());  //时间
//            }
//
//            if (element.text().contains("章")) {
//                kswModel.setLastChapter(element.select("a").text());
//                kswModel.setUrl(element.select("a").attr("abs:href"));
//            }
//
//        }

        list.add(kswModel);

    }


}
