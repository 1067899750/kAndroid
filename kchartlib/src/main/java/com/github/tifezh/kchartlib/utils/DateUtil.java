package com.github.tifezh.kchartlib.utils;

import android.text.TextUtils;

import java.text.DateFormat;
import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.Calendar;
import java.util.Date;
import java.util.Locale;

/**
 *
 * Description 时间工具类
 * Author puyantao
 * Email 1067899750@qq.com
 * Date 2018-11-15 14:37
 */

public class DateUtil {
    public static SimpleDateFormat longTimeFormat = new SimpleDateFormat("yyyy-MM-dd HH:mm");
    public static SimpleDateFormat shortTimeFormat = new SimpleDateFormat("HH:mm");
    public static SimpleDateFormat shortDateFormat = new SimpleDateFormat("MM/dd");
    public static SimpleDateFormat dateFormat = new SimpleDateFormat("yyyy/MM/dd");

    /**
     *    时间蹉  -->  日期
     * @param dateLong  时间蹉
     * @param type  类型
     * @return  日期
     */
    public static String getStringDateByLong(long dateLong, int type) {
        String template = null;
        switch (type) {
            case 1:
                template = "yyyy-MM-dd HH:mm";
                break;
            case 2:
                template = "yyyy/MM/dd";
                break;
            case 3:
                template = "yyyy年MM月dd日";
                break;
            case 4:
                template = "yyyy-MM-dd";
                break;
            case 5:
                template = "yyyy-MM-dd HH:mm:ss";
                break;
            case 6:
                template = "MM月dd号";
                break;
            case 7:
                template = "yyyy/MM/dd HH:mm:ss";
                break;
            case 8:
                template = "HH:mm";
                break;
            case 9:
                template = "MM/dd";
                break;
            case 10:
                template = "yyyy/MM";
                break;
        }

        SimpleDateFormat sdf = new SimpleDateFormat(template, Locale.CHINA);
        Date date = new Date(dateLong );//* 1000L
        String dateString = sdf.format(date);
        return dateString;
    }

    /**
     *
     * @param dateString
     * @return
     */
    public static Date getDateByByStringDate(String dateString) {
        if (TextUtils.isEmpty(dateString)) {
            return null;
        }
        DateFormat sdf = new SimpleDateFormat("yyyy/MM/dd HH:mm:ss", Locale.CHINA);
        Date date = null;
        try {
            date = sdf.parse(dateString);
        } catch (ParseException e) {
        }
        return date;
    }

    /**
     *  日期 --> 时间蹉
     * @param s  日期
     * @param type 类型
     * @return 时间蹉
     */
    public static long dateToStamp(String s, int type) {
        String template = null;
        switch (type) {
            case 1:
                template = "yyyy-MM-dd HH:mm";
                break;
            case 2:
                template = "yyyy/MM/dd";
                break;
            case 3:
                template = "yyyy年MM月dd日";
                break;
            case 4:
                template = "yyyy-MM-dd";
                break;
            case 5:
                template = "yyyy-MM-dd HH:mm:ss";
                break;
            case 6:
                template = "MM月dd号";
                break;
            case 7:
                template = "yyyy/MM/dd HH:mm:ss";
                break;
            case 8:
                template = "HH:mm";
                break;
            case 9:
                template = "MM/dd";
                break;
            case 10:
                template = "yyyy/MM";
                break;
        }
        SimpleDateFormat simpleDateFormat = new SimpleDateFormat(template);
        Date date = null;
        try {
            date = simpleDateFormat.parse(s);
        } catch (ParseException e) {
            e.printStackTrace();
        }
        return date.getTime();
    }

    /**
     *  日期加一天
     * @param dateLen
     * @return
     */
    public static String addOneDayDate(long dateLen){
        Date today = new Date(dateLen);
        Calendar c = Calendar.getInstance();
        c.setTime(today);
        c.add(Calendar.DAY_OF_MONTH, 1);
        return getStringDateByLong(c.getTime().getTime(), 9);
    }

    public static void main(String[] argc){

    }

}
