package com.youth.xf.ui.demo.fiction;

import org.jsoup.Jsoup;
import org.jsoup.nodes.Document;
import org.jsoup.nodes.Element;
import org.jsoup.select.Elements;

import java.io.IOException;
import java.util.ArrayList;
import java.util.List;

/**
 * Created by Administrator on 2017/6/22.
 */

public class JsoupFantasyManager {

    public static JsoupFantasyManager get() {

        return new JsoupFantasyManager();
    }


    public List<FictionModel> getData() {
        List<FictionModel> list = new ArrayList<>();
        initHeader(list,"http://www.zwdu.com/xuanhuan/");
        initHeader(list,"http://www.zwdu.com/xianxia/");
        initHeader(list,"http://www.zwdu.com/dushi/");
        initHeader(list,"http://www.zwdu.com/lishi/");
        initHeader(list,"http://www.zwdu.com/wangyou/");
        initHeader(list,"http://www.zwdu.com/kehuan/");
        initHeader(list,"http://www.zwdu.com/yanqing/");
        initHeader(list,"http://www.zwdu.com/qita/");
        return list;
    }


    /**
     * 获取玄幻页面6个小说
     */
    private void initFantasyHeader(List<FictionModel> list) {

        Document document = null;
        try {
            document = Jsoup.connect("http://www.zwdu.com/xuanhuan/")
                    .timeout(10000)
                    .get();
        } catch (IOException e) {
            e.printStackTrace();
        }

        FictionModel kswModel;
        Elements select = document.select("div.ll");
        for (Element element : select) {
            kswModel = new FictionModel();
            kswModel.setTitle(element.select("img[src]").attr("alt"));  //书名
            kswModel.setCoverImg(element.select("img[src]").attr("src"));  //封面
            kswModel.setDetailUrl(element.select("a:has(img)").attr("abs:href")); //abs:href 返回绝对路径
            kswModel.setDesc(element.select("dd").text());  //描述
            kswModel.setAuthor(element.select("span").text());  //作者
            list.add(kswModel);
        }
    }


    /**
     * 获取修真页面6个小说
     */
    private void initWuXiaHeader(List<FictionModel> list) {

        Document document = null;
        try {
            document = Jsoup.connect("http://www.zwdu.com/xianxia/")
                    .timeout(10000)
                    .get();
        } catch (IOException e) {
            e.printStackTrace();
        }

        FictionModel kswModel;
        Elements select = document.select("div.ll");
        for (Element element : select) {
            kswModel = new FictionModel();
            kswModel.setTitle(element.select("img[src]").attr("alt"));  //书名
            kswModel.setCoverImg(element.select("img[src]").attr("src"));  //封面
            kswModel.setDetailUrl(element.select("a:has(img)").attr("abs:href")); //abs:href 返回绝对路径
            kswModel.setDesc(element.select("dd").text());  //描述
            kswModel.setAuthor(element.select("span").text());  //作者
            list.add(kswModel);
        }
    }



    private void initHeader(List<FictionModel> list , String pageurl) {

        Document document = null;
        try {
            document = Jsoup.connect(pageurl)
                    .timeout(10000)
                    .get();
        } catch (IOException e) {
            e.printStackTrace();
        }

        FictionModel kswModel;
        Elements select = document.select("div#hotcontent").select("div.ll").select("div.item");
        for (Element element : select) {
            kswModel = new FictionModel();
            kswModel.setTitle(element.select("img[src]").attr("alt"));  //书名
            kswModel.setCoverImg(element.select("img[src]").attr("abs:src"));  //封面
            kswModel.setDetailUrl(element.select("a:has(img)").attr("abs:href")); //abs:href 返回绝对路径
            kswModel.setDesc(element.select("dd").text());  //描述
            kswModel.setAuthor(element.select("span").text());  //作者
            list.add(kswModel);
        }
    }





}
