package com.afeng.xf.utils.AFengUtils;

import java.net.URL;
import java.net.URLConnection;
import java.text.SimpleDateFormat;
import java.util.Calendar;
import java.util.Date;

/**
 * Created by Administrator on 2017/7/12.
 */

public class AppTimeUtils {

    /**
     * 日期格式：yyyy-MM-dd HH:mm:ss
     **/
    public static final String DF_YYYY_MM_DD_HH_MM_SS = "yyyy-MM-dd HH:mm:ss";

    private final static long MINUTE = 60 * 1000;// 1分钟
    private final static long HOUR = 60 * MINUTE;// 1小时
    private final static long DAY = 24 * HOUR;// 1天
    private final static long MONTH = 31 * DAY;// 月
    private final static long YEAR = 12 * MONTH;// 年

    /**
     * 将日期格式化成友好的字符串：几分钟前、几小时前、几天前、几月前、几年前、刚刚
     *
     * @param date
     * @return
     */
    public static String formatFriendly(Date date) {
        if (date == null) {
            return null;
        }
        long diff = new Date().getTime() - date.getTime();
        long r = 0;
        if (diff > YEAR) {
            r = (diff / YEAR);
            return r + "年前";
        }
        if (diff > MONTH) {
            r = (diff / MONTH);
            return r + "个月前";
        }
        if (diff > DAY) {
            r = (diff / DAY);
            return r + "天前";
        }
        if (diff > HOUR) {
            r = (diff / HOUR);
            return r + "个小时前";
        }
        if (diff > MINUTE) {
            r = (diff / MINUTE);
            return r + "分钟前";
        }
        return "刚刚";
    }

    /**
     * 将日期以yyyy-MM-dd HH:mm:ss格式化
     *
     * @param dateL 日期
     * @return
     */
    public static String formatDateTime(long dateL) {
        SimpleDateFormat sdf = new SimpleDateFormat(DF_YYYY_MM_DD_HH_MM_SS);
        Date date = new Date(dateL);
        return sdf.format(date);
    }

    /**
     * 通过Calendar类来获取系统当前的时间
     *
     * @return 当前时间为：2016年 6月 13日 14时 38分 58秒
     */
    public static String getTimeByCalendar() {
        Calendar calendar = Calendar.getInstance();
        long unixTime = calendar.getTimeInMillis();//这是时间戳
        StringBuilder stringBuilder = new StringBuilder();
        stringBuilder.append(calendar.get(Calendar.YEAR) + "年 ");
        stringBuilder.append(calendar.get(Calendar.MONTH) + "月 ");
        stringBuilder.append(calendar.get(Calendar.DAY_OF_MONTH) + "日 ");
        stringBuilder.append(calendar.get(Calendar.HOUR_OF_DAY) + "时 ");
        stringBuilder.append(calendar.get(Calendar.MINUTE) + "分 ");
        stringBuilder.append(calendar.get(Calendar.SECOND) + "秒 ");
        return stringBuilder.toString();
    }


    /**
     * 获取网络时间
     *
     * @return
     */
    public static String getNetTime() {
        URL url = null;//取得资源对象
        try {
            url = new URL("http://www.baidu.com");
            URLConnection uc = url.openConnection();//生成连接对象
            uc.connect(); //发出连接
            return formatDateTime(uc.getDate()); //取得网站日期时间
        } catch (Exception e) {
            e.printStackTrace();
        }

        return null;
    }


}
