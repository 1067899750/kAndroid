package com.github.tifezh.kchart.utils;

import java.text.SimpleDateFormat;
import java.util.Date;
/**
 *
 * Description
 * Author puyantao
 * Email 1067899750@qq.com
 * Date 2018-10-26 17:42
 */

public class TimeUtils {

    //转化为时，分
    public static String setLongToDateHTime(Long time){
        SimpleDateFormat dateFormat = new SimpleDateFormat("HH:mm");
        String strTime = dateFormat.format(time);
        return strTime;
    }


    //转化为年，月，日
    public static String setLongToDateYTime(long beginTime) {
        SimpleDateFormat sdf = new SimpleDateFormat("yyyy-MM-dd");
        return sdf.format(beginTime);
    }


    public static void main(String[] argc) {
        System.out.println(TimeUtils.setLongToDateHTime(new Date().getTime()));

    }


}
















