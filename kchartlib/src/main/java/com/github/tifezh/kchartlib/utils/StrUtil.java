package com.github.tifezh.kchartlib.utils;

import android.os.Build;

import java.text.DecimalFormat;
import java.text.NumberFormat;
import java.text.SimpleDateFormat;
import java.util.Date;
import java.util.regex.Matcher;
import java.util.regex.Pattern;

public class StrUtil {

    //保留万位
    public static String reserveToThousandBits(String strBit) {
        if (strBit == null || strBit.equals("--") || strBit.equals("-") || strBit.equals("- -")) {
            return "- -";
        }
        Double dou = Double.valueOf(strBit);
        if (dou > 10000 || dou < -10000) {
            dou /= 10000;
            String souStr = getPositiveNumber(dou);
            if (souStr.contains("-")) {
                return souStr + "万";
            } else {
                return "+" + souStr + "万";
            }
        } else {
            return strBit;
        }
    }

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
        if (str.equals("- -")) return false;
        return str.substring(0, 1).matches("-");
    }

    /**
     * 保留2位小数
     */


    /**
     * 将float格式化为指定小数位的String，不足小数位用0补全
     *
     * @param v     需要格式化的数字
     * @param scale 小数点后保留几位
     * @return
     */
    public static String floatToString(double v, int scale) {
        if (scale < 0) {
            throw new IllegalArgumentException(
                    "The   scale   must   be   a   positive   integer   or   zero");
        }
        if (scale == 0) {
            return new DecimalFormat("0").format(v);
        }
        String formatStr = "0.";
        for (int i = 0; i < scale; i++) {
            formatStr = formatStr + "0";
        }
        return new DecimalFormat(formatStr).format(v);
    }


    //保留一位小数
    public static String getOneDecimals(double d) {
        DecimalFormat decimalFormat = new DecimalFormat("0.0");//构造方法的字符格式这里如果小数不足1位,会以0补足.
        String p = decimalFormat.format(d);//format 返回的是字符串
        return p;
    }


    //四舍五入保留正数
    public static String getPositiveNumber(double d) {
        NumberFormat nf = new DecimalFormat("#");
        return nf.format(d);
    }

    //进一法保留正数
    public static float getAndOnePositiveNumber(double d) {
        if (d > 0) {
            if (d > Double.valueOf(getPositiveNumber(d))) {
                return Float.valueOf(getPositiveNumber(d)) + 1;
            } else {
                return Float.valueOf(getPositiveNumber(d));
            }

        } else if (d < 0) {
            if (d < Float.valueOf(getPositiveNumber(d))) {
                return Float.valueOf(getPositiveNumber(d)) - 1;
            } else {
                return Float.valueOf(getPositiveNumber(d));
            }
        } else {
            return (float) d;
        }
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
        NumberFormat nf = NumberFormat.getPercentInstance();
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

    //截取数字的前两位，小于两位直接返回
    public static int getNumberFrontTwoNumber(long l) {
        String str = String.valueOf(l);
        if (str.length() > 2) {
            return Integer.valueOf(str.substring(0, 2));
        } else {
            return Integer.valueOf(str);
        }
    }


    //获取长度
    public static int getNumberDigit(String str) {
        return str.length();
    }


    //前两位取5的倍数(CJL, MACD)
    public static long getFaveMultipleMinimum(long text) {
        if (text > 0) {
            long a = getNumberFrontTwoNumber(text);
            if (a < 10) {
                a = (a / 5 * 5 + 5);
            } else {
                a = (long) ((a / 5 * 5 + 5) * Math.pow(10, getNumberDigit(getPositiveNumber(text)) - 2));
            }
            return a;
        } else if (text == 0) {
            return 0;
        } else {
            long aa = Math.abs(text);
            long a = getNumberFrontTwoNumber(aa);
            if (a < 10) {
                a = (a / 5 * 5 + 5);
            } else {
                a = (long) ((a / 5 * 5 + 5) * Math.pow(10, getNumberDigit(getPositiveNumber(aa)) - 2));
            }
            return -a;
        }
    }

    //取10的倍数(LME)
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


    /**
     * @param text
     * @param i    1 向上取， 2向下取
     * @return
     */
    public static long getZeroMultipleMinimum(long text, int i) {
        if (i == 1) {
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
        } else if (i == 2) {
            if (text > 0) {
                text /= 10;
                text = text * 10 - 10;
                return text;
            } else if (text == 0) {
                return 0;
            } else {
                text /= 10;
                text = text * 10 + 10;
                return text;
            }
        }
        return 0;
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

    //截取“+”，“-”号
    public static String subAddAndSubMark(String str) {
        if (matchAddSubMark(str.substring(0, 1))) {
            return str.substring(1, str.length());
        }
        return str;
    }

    /**
     * 取n的倍数(LEM)
     *
     * @param text 原数
     * @param n    倍数
     * @return n的被数
     */
    public static int getLemMultipleMinimum(double text, int n) {
        int a = 0;
        if (text > 0) {
            text /= n;
            a= (int) text;
            a = a * n + n;
            return (int) a;
        } else if (text == 0) {
            return 0;
        } else {
            int aa = (int) Math.abs(text);
            aa /= n;
            aa = aa * n + n;
            return -aa;
        }
    }

    //取100的倍数(LEM)
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
            if (month.length() == 1) {
                month = 0 + month;
            }
            format = year + month;
        }
        return format + "-3M";
    }

    /**
     * LEM 右边轴的计算 aa[0] 最大值， aa[1] 最小值， aa[2] 等分个数, aa[3] 缩放量
     *
     * @param height 传最大值
     * @param low    传最小值
     * @return 返回数组， aa[0] 最大值， aa[1] 最小值， aa[2] 等分个数, aa[3] 缩放量
     */
    public static int[] getLemRightValue(float height, float low) {
        int[] aa = new int[4];
        int count = 3;
        int c = 0;
        int scale = 0;
        int n = 3;
        int h = (int) Math.abs(getAndOnePositiveNumber(height));
        int l = (int) Math.abs(getAndOnePositiveNumber(low));
        if (height > 0 && low < 0) {
            if (Math.abs(height) > Math.abs(low)) {
                h = getLemMultipleMinimum(h, n);
                aa[0] = h;
                scale = h / n;
                aa[3] = scale;
                if (l % scale == 0) {
                    c = l / scale;
                } else {
                    c = l / scale + 1;
                }
                count += c;
                aa[2] = count;
                aa[1] = -scale * c;

            } else if (Math.abs(height) < Math.abs(low)) {
                l = getLemMultipleMinimum(l, n);
                aa[1] = -l;
                scale = l / n;
                aa[3] = scale;
                if (h % scale == 0) {
                    c = h / scale;
                } else {
                    c = h / scale + 1;
                }
                count += c;
                aa[2] = count;
                aa[0] = scale * c;

            } else if (Math.abs(height) == Math.abs(low)) {
                aa[0] = getLemMultipleMinimum(h, n);
                aa[1] = -getLemMultipleMinimum(l, n);
                aa[2] = count * 2;
                aa[3] = getLemMultipleMinimum(h, n) / n;
            }

        } else if (height > low && low >= 0) {
            aa[1] = 0;
            aa[0] = getLemMultipleMinimum(h, n);
            aa[2] = count;
            aa[3] = getLemMultipleMinimum(h, n) / n;

        } else if (height > low && height <= 0) {
            aa[0] = 0;
            aa[1] = -getLemMultipleMinimum(l, n);
            aa[2] = count;
            aa[3] = getLemMultipleMinimum(l, n) / n;
        }
        return aa;
    }


    public static void main(String[] argc) {
        System.out.println(StrUtil.getFaveMultipleMinimum((long) 7360.77));

    }


}















