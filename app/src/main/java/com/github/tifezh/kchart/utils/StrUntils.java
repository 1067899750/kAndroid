package com.github.tifezh.kchart.utils;

import java.text.DecimalFormat;
import java.text.NumberFormat;
import java.text.ParseException;
import java.util.regex.Matcher;
import java.util.regex.Pattern;
/**
 *
 * Description
 * Author puyantao
 * Email 1067899750@qq.com
 * Date 2019-1-14 17:18
 */

public class StrUntils {

    public static String deletePerCent(String str){
        if (str.contains("%")) {
            return str.substring(0, str.indexOf("%"));
        } else {
            return str;
        }
    }
    /**
     * 格式化空判断
     * @param str
     * @return
     */
    public static float strToFloat(String str){
        if (str == null || str.equals("-")|| str.equals("- -")) {
            return 0;
        }
        try {
            return Float.parseFloat(str);
        } catch (Exception e) {
            e.printStackTrace();
            throw new IllegalStateException("数据异常");
        }
    }

    //匹配正负号
    public static boolean matchAddSubMark(String str) {
        Pattern pattern = Pattern.compile("^[-\\+]?");
        Matcher isNum = pattern.matcher(str);
        if (!isNum.matches()) {
            return false;
        }
        return true;
    }

    //判断末尾是小数点
    public static boolean matchFinishPoint(String str) {

        Pattern pattern = Pattern.compile("^[+\\-]+([1-9][0-9]*)+(.[0-9]{1,})?\\.$");
        Matcher isNum = pattern.matcher(str);
        if (!isNum.matches()) {
            return false;
        }
        return true;
    }

    //去掉小数点后多余的0
    public static String subZeroAndDot(String s){
        if(s.indexOf(".") > 0){
            s = s.replaceAll("0+?$", "");//去掉多余的0
            s = s.replaceAll("[.]$", "");//如最后一位是.则去掉
        }
        return s;
    }


    public static boolean isEmpty(String str) {
        return str == null || str.trim().length() == 0;
    }

    /**
     * 字符串 千位符
     *
     * @param num
     * @return
     */
    public static String num2Thousand(String num) {
        String numStr = "";
        if (isEmpty(num)) {
            return numStr;
        }
        NumberFormat nf = NumberFormat.getInstance();
        try {
            DecimalFormat df = new DecimalFormat("#,###");
            numStr = df.format(nf.parse(num));
        } catch (ParseException e) {
            e.printStackTrace();
        }
        return numStr;
    }

    /**
     * 字符串 千位符  保留两位小数点后两位
     *
     * @param num
     * @return
     */
    public static String num2Thousand00(String num) {
        String numStr = "";
        if (isEmpty(num)) {
            return numStr;
        }
        NumberFormat nf = NumberFormat.getInstance();
        try {
            DecimalFormat df = new DecimalFormat("#,##0.00");
            numStr = df.format(nf.parse(num));
        } catch (ParseException e) {
            e.printStackTrace();
        }
        return numStr;
    }


    public static void main(String[] argc){
        System.out.println(num2Thousand00("125245579690.2222123"));
    }

}










