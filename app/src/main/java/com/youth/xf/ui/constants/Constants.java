package com.youth.xf.ui.constants;


import com.avos.avoscloud.AVFile;

/**
 * Created by AFeng on 2017/3/12.
 */

public class Constants {

    // 用户信息缓存 KEY
    public static final String USER_INFO_KEY = "userinfocache";

    public static String USER_INFO_ID = "";

    public static final String WELCOME_PIC = "http://oki2v8p4s.bkt.clouddn.com/wec_1.jpg";   //启动图
    public static final String AVATAR = "http://oki2v8p4s.bkt.clouddn.com/avetar.jpg";


    // 空
    public static String None_JS_CODE = "javascript:(function() {})";


    // 哔哩B站 嵌入JS
    public static String BILI_JS_CODE =

            "javascript:(function() {" +
                    "document.getElementsByClassName('index__downloadBtn__src-home-topArea-')[0].style.display='none';" +
                    "document.getElementsByClassName('index__openClientBtn__src-videoPage-player-')[0].style.display='none';" +
                    "document.getElementsByClassName('index__downloadBtn__src-videoPage-topArea-')[0].style.display='none';" +
                    "})()";


    // 网易云音乐 嵌入JS
    public static String WANGYI163_JS_CODE =

            "javascript:(function() {" +
                    "document.getElementsByClassName('m-hometop')[0].style.display='none';" +
                    "document.getElementsByClassName('ftwrap')[0].style.display='none';" +
                    "})()";


    // 芒果TV 嵌入JS
    public static String MGTV_JS_CODE =

            "javascript:(function() {" +
                    "document.getElementsByClassName('mg-app-open on')[0].style.display='none';" +
                    "document.getElementsByClassName('btn mg-stat')[0].style.display='none';" +
                    "})()";


    //网易
//    String temp = "javascript:(function() { document.getElementsByClassName('m-hometop')[0].style.display='none'; document.getElementsByClassName('ftwrap')[0].style.display='none';})()";







}
