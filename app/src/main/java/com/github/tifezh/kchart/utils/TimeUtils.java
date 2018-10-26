package com.github.tifezh.kchart.utils;

import java.text.SimpleDateFormat;
import java.util.Date;

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
















