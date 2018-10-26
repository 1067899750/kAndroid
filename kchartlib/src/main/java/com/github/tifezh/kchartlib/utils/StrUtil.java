package com.github.tifezh.kchartlib.utils;

import android.os.Build;
import android.util.Log;

import java.text.DateFormat;
import java.text.DecimalFormat;
import java.text.NumberFormat;
import java.text.SimpleDateFormat;
import java.util.Date;
import java.util.regex.Matcher;
import java.util.regex.Pattern;

public class StrUtil {

    /**
     * 判断汉字
     *
     * @param str
     * @return
     */
    public static boolean isChinaText(String str) {
        Pattern pattern = Pattern.compile("[\\u4e00-\\u9fa5]+");
        return pattern.matcher(str).matches();
    }

    /**
     * 判断 时间
     *
     * @param str
     * @return
     */
    public static boolean isTimeText(String str) {
        return str.contains(":");
    }

    /**
     * 判断 正负数
     *
     * @param str
     * @return
     */
    public static boolean isPositiveOrNagativeNumberText(String str) {
        return str.substring(0, 1).matches("-");
    }

    /**
     * 保留2位小数
     */
    public static String floatToString(float value) {
        String s = String.format("%.2f", value);
        char end = s.charAt(s.length() - 1);
        while (s.contains(".") && (end == '0' || end == '.')) {
            s = s.substring(0, s.length() - 1);
            end = s.charAt(s.length() - 1);
        }
        return s;
    }

    //保留一位小数
    public static String getOneDecimals(double d) {
        DecimalFormat decimalFormat = new DecimalFormat("0.0");//构造方法的字符格式这里如果小数不足1位,会以0补足.
        String p = decimalFormat.format(d);//format 返回的是字符串
        return p;
    }


    //保留正数
    public static String getPositiveNumber(double d) {
        NumberFormat nf = new DecimalFormat("#");
        return nf.format(d);
    }

    /**
     * 将double类型数据转换为百分比格式，并保留小数点前IntegerDigits位和小数点后FractionDigits位
     *
     * @param d
     * @param integerDigits  当为0或负数时不设置保留位数
     * @param fractionDigits 当为0或负数时不设置保留位数
     * @return
     */
    public static String getPercentFormat(double d, int integerDigits, int fractionDigits) {
        NumberFormat nf = java.text.NumberFormat.getPercentInstance();
        if (integerDigits > 0) {
            nf.setMaximumIntegerDigits(integerDigits);//小数点前保留几位
        }
        if (fractionDigits > 0) {
            nf.setMinimumFractionDigits(fractionDigits);// 小数点后保留几位
        }
        String str = nf.format(d);
        if (d > 0) {
            return "+" + str;
        } else if (d == 0) {
            return 0 + "";
        } else {
            return str;
        }
    }

    public static int getNumberFrontTwoNumber(long l) {
        String str = String.valueOf(l);
        if (str.length() > 2) {
            return Integer.valueOf(str.substring(0, 2));
        } else {
            return Integer.valueOf(str);
        }
    }


    //获取长度
    public static int getNumberDigit(long l) {
        String str = String.valueOf(l);
        return str.length();
    }


    //取5的倍数
    public static long getFaveMultipleMinimum(long text) {
        if (text > 0) {
            long a = getNumberFrontTwoNumber(text);
            if (getNumberDigit(text) < 2){
                a = (long) ((a / 5 * 5 + 5));
            } else {
                a = (long) ((a / 5 * 5 + 5) * Math.pow(10, getNumberDigit(text) - 2));
            }
            return a;
        } else if (text == 0) {
            return 0;
        } else {
            long aa = Math.abs(text);
            long a = getNumberFrontTwoNumber(aa);
            if (getNumberDigit(text) < 2){
                a = (long) ((a / 5 * 5 + 5));
            } else {
                a = (long) ((a / 5 * 5 + 5) * Math.pow(10, getNumberDigit(text) - 2));
            }
            return -a;
        }
    }

    //取10的倍数
    public static long getZeroMultipleMinimum(long text) {
        if (text > 0) {
            text /= 10;
            text = text * 10 + 10;
            return text;
        } else if (text == 0) {
            return 0;
        } else {
            text /= 10;
            text = text * 10 - 10;
            return text;
        }
    }

    //取100的倍数
    public static long getMillionMultipleMinimum(long text) {
        if (text > 0) {
            text /= 100;
            text = text * 100 + 100;
            return text;
        } else if (text == 0) {
            return 0;
        } else {
            text /= 100;
            text = text * 100 - 100;
            return text;
        }
    }

    //LEM 时间格式化
    public static String getLEMDataStr(Long l) {
        String format = null;
        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.N) {
            SimpleDateFormat sdf = new SimpleDateFormat("YYMM");
            format = sdf.format(l);       //将Date类型转换成String类型
        } else {
            String year = String.valueOf(new Date(l).getYear() + 1900);
            year = year.substring(year.length() - 2, year.length());
            String month = String.valueOf(new Date(l).getMonth());
            if (month.length() == 1){
                month = 0 + month;
            }
            format = year + month;
        }
        return format + "-3M";
    }

    public static void main(String[] argc) {
        System.out.println(StrUtil.getLEMDataStr(Long.parseLong("1539932400000")));

        System.out.println(StrUtil.getFaveMultipleMinimum(-2));

    }


}















