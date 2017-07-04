package com.afeng.xf.ui.demo.fiction;

import android.support.annotation.NonNull;


import org.jsoup.Jsoup;
import org.jsoup.nodes.Document;
import org.jsoup.nodes.Element;
import org.jsoup.select.Elements;

import java.io.IOException;
import java.util.ArrayList;
import java.util.List;


/**
 * by y on 2017/3/25.
 */

public class JsoupFictionHomeManager {

    String ON_PAGE = "上一章";
    String NEXT_PAGE = "下一章";

    //搜索
    String SEARCH_URL = "http://zhannei.baidu.com/cse/search?q=";
    String SEARCH_SUFFIX = "&s=3975864432584690275";

//    //81中文网
//    String ZW81_URL = "http://www.81zw.com/";
//
//    //笔趣阁
//    String BI_QU_GE_URL = "http://www.biqiuge.com/";
//
//    //零点看书
//    String KSW_URL = "http://www.00ksw.net/";


    public static final int TYPE_HEADER = 0; //首页四个大图小说
    public static final int TYPE_HOT = 1;   //热门小说
    public static final int TYPE_CENTER = 2;   //中间6个小说区
    public static final int TYPE_RECENT = 3;   //最近更新小说列表
    public static final int TYPE_ADD = 4;   //最新入库小说
    public static final int TYPE_TITLE = 5;
    public static final String TYPE_TITLE_XUAN_HUAN = "玄幻:";
    public static final String TYPE_TITLE_XIU_ZHEN = "修真:";
    public static final String TYPE_TITLE_DU_SHI = "都市:";
    public static final String TYPE_TITLE_CHUAN_YUE = "穿越:";
    public static final String TYPE_TITLE_WANG_YOU = "网游:";
    public static final String TYPE_TITLE_KE_HUAN = "科幻:";
    private static final String TYPE_TITLE_HOT = "热门小说:";
    private static final String TYPE_TITLE_RETCENT = "最近更新:";
    private static final String TYPE_TITLE_ADD = "最新入库:";
    private Document document;

    private JsoupFictionHomeManager(Document document) {
        this.document = document;
    }

    public static JsoupFictionHomeManager get() {

        Document document = null;
        try {
            document = Jsoup.connect("http://www.81zw.com/")
                    .timeout(10000)
                    .get();
        } catch (IOException e) {
            e.printStackTrace();
        }

        return new JsoupFictionHomeManager(document);
    }

    public static JsoupFictionHomeManager get(@NonNull Document document) {
        return new JsoupFictionHomeManager(document);
    }


    public List<FictionModel> getKswHome() {
        List<FictionModel> list = new ArrayList<>();
        initHeader(list);
        return list;
    }

    /**
     * 获取首页四个小说
     */
    private void initHeader(List<FictionModel> list) {
        if (document != null) {
            FictionModel kswModel;
            Elements select = document.select("div.item");
            for (Element element : select) {
                kswModel = new FictionModel();
                kswModel.setTitle(element.select("img[src]").attr("alt"));  //书名
                kswModel.setCoverImg(element.select("img[src]").attr("src"));  //封面
                kswModel.setDetailUrl(element.select("a:has(img)").attr("abs:href")); //abs:href 返回绝对路径
                kswModel.setDesc(element.select("dd").text());  //描述
                kswModel.setType(TYPE_HEADER);
                list.add(kswModel);
            }
        }

        initPush(list);
    }


    /**
     * 获取热门小说
     */
    private void initPush(List<FictionModel> list) {
        if (document != null) {
            FictionModel kswModel;
            Elements select = document.select("div.r").eq(0).select("a[href]");

            FictionModel hotTitle = new FictionModel();
            hotTitle.setTitle(TYPE_TITLE_HOT);
            hotTitle.setType(TYPE_TITLE);
            list.add(hotTitle);

            for (Element element : select) {
                kswModel = new FictionModel();
                kswModel.setTitle(element.text());
                kswModel.setDetailUrl(element.attr("abs:href"));
                kswModel.setType(TYPE_HOT);
                list.add(kswModel);
            }
        }
        initCenterHeader(list);
    }

    /**
     * 获取中间6个小说区头部
     */
    private void initCenterHeader(List<FictionModel> list) {
        if (document != null) {

            FictionModel kswHomeModel;
            Elements select = document.select("div.content");
            int size = select.size();
            for (int i = 0; i < size; i++) {

                initTitle(list, i);


                kswHomeModel = new FictionModel();
                Elements topSelect = select.get(i).select("div.top");
                kswHomeModel.setUrl(topSelect.select("img[src]").attr("src"));
                kswHomeModel.setTitle(topSelect.select("img[src]").attr("alt"));
                kswHomeModel.setDetailUrl(topSelect.select("a:has(img)").attr("abs:href"));
                kswHomeModel.setDesc(topSelect.select("dd").text());
                kswHomeModel.setType(TYPE_HEADER);
                list.add(kswHomeModel);

                initCenter(list, select.get(i).select("li:has(a)"));
            }
        }

        initRecent(list);
    }

    /**
     * 添加6个小说区的title
     */
    private void initTitle(List<FictionModel> list, int i) {
        FictionModel pushTitle = new FictionModel();
        pushTitle.setType(TYPE_TITLE);
        switch (i) {
            case 0:
                pushTitle.setTitle(TYPE_TITLE_XUAN_HUAN);
                break;
            case 1:
                pushTitle.setTitle(TYPE_TITLE_XIU_ZHEN);
                break;
            case 2:
                pushTitle.setTitle(TYPE_TITLE_DU_SHI);
                break;
            case 3:
                pushTitle.setTitle(TYPE_TITLE_CHUAN_YUE);
                break;
            case 4:
                pushTitle.setTitle(TYPE_TITLE_WANG_YOU);
                break;
            case 5:
                pushTitle.setTitle(TYPE_TITLE_KE_HUAN);
                break;
        }
        list.add(pushTitle);
    }

    /**
     * 获取小说中间6个区详情
     */
    private void initCenter(List<FictionModel> list, Elements elements) {
        FictionModel kswHomeModel;
        Elements a = elements.select("a");
        for (Element element : a) {
            kswHomeModel = new FictionModel();
            kswHomeModel.setTitle(element.text());
            kswHomeModel.setDetailUrl(element.attr("abs:href"));
            kswHomeModel.setType(TYPE_CENTER);
            list.add(kswHomeModel);
        }
    }

    /**
     * 最近更新小说列表
     */
    private void initRecent(List<FictionModel> list) {

        FictionModel pushTitle = new FictionModel();
        pushTitle.setTitle(TYPE_TITLE_RETCENT);
        pushTitle.setType(TYPE_TITLE);
        list.add(pushTitle);

        if (document != null) {
            FictionModel kswHomeModel;
            Elements select = document.select("div#newscontent").select("div.l").select("span.s2").select("a");
            for (Element element : select) {
                kswHomeModel = new FictionModel();
                kswHomeModel.setTitle(element.text());
                kswHomeModel.setDetailUrl(element.attr("abs:href"));
                kswHomeModel.setType(TYPE_RECENT);
                list.add(kswHomeModel);
            }
        }

        initAdd(list);
    }

    /**
     * 获取最新入库小说
     */
    private void initAdd(List<FictionModel> list) {

        FictionModel pushTitle = new FictionModel();
        pushTitle.setTitle(TYPE_TITLE_ADD);
        pushTitle.setType(TYPE_TITLE);
        list.add(pushTitle);

        if (document != null) {
            FictionModel kswHomeModel;
            Elements select = document.select("div.r").eq(1).select("a[href]");
            for (Element element : select) {
                kswHomeModel = new FictionModel();
                kswHomeModel.setTitle(element.text());
                kswHomeModel.setDetailUrl(element.attr("abs:href"));
                kswHomeModel.setType(TYPE_ADD);
                list.add(kswHomeModel);
            }

        }


    }


}
