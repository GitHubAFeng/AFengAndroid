package com.afeng.xf.ui.demo.fiction;

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

public class JsoupFictionManager {

    private Document document;

    private JsoupFictionManager(Document document) {
        this.document = document;
    }

    // 首页
    public static JsoupFictionManager get() {

        Document document = null;
        try {
            document = Jsoup.connect("http://www.81zw.com/")
                    .timeout(10000)
                    .get();
        } catch (IOException e) {
            e.printStackTrace();
        }

        return new JsoupFictionManager(document);
    }

    // 取 首页数据
    public List<FictionModel> getData() {
        List<FictionModel> list = new ArrayList<>();
        initHeader(list);
        initCenterHeader(list);
        return list;
    }


    /**
     * 获取首页四个小说
     */
    private void initHeader(List<FictionModel> list) {
        FictionModel kswModel;
        Elements select = document.select("div.item");
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
     * 获取中间6个头部有封面的小说
     */
    private void initCenterHeader(List<FictionModel> list) {
        FictionModel kswHomeModel;
        Elements select = document.select("div.content");
        int size = select.size();
        for (int i = 0; i < size; i++) {
            kswHomeModel = new FictionModel();
            Elements topSelect = select.get(i).select("div.top");  //顶部区域
            kswHomeModel.setCoverImg(topSelect.select("img[src]").attr("src"));  //封面
            kswHomeModel.setTitle(topSelect.select("img[src]").attr("alt"));  //书名
            kswHomeModel.setDetailUrl(topSelect.select("a:has(img)").attr("abs:href")); //拥有img标签的a标签，取a标签的绝对路径
            kswHomeModel.setDesc(topSelect.select("dd").text());  //描述
            list.add(kswHomeModel);
        }

    }







//TODO 我发现在有意思的是，例如用F12调试 ，选择区 如  "div#newscontent" 这样的标签，浏览器已经帮你筛选好了

    /**
     * 最近更新小说列表 ， 没有封面图
     */
    private void initRecent(List<FictionModel> list) {

        FictionModel kswHomeModel;
        Elements select = document.select("div#newscontent").select("div.l").select("span.s2").select("a");
        for (Element element : select) {
            kswHomeModel = new FictionModel();
            kswHomeModel.setTitle(element.text());
            kswHomeModel.setDetailUrl(element.attr("abs:href"));
            list.add(kswHomeModel);
        }

    }




}
