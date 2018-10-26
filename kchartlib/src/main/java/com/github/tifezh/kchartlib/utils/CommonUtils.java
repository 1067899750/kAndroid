package com.github.tifezh.kchartlib.utils;

import android.annotation.SuppressLint;
import android.app.Activity;
import android.content.Context;
import android.content.pm.ApplicationInfo;
import android.content.pm.PackageManager;
import android.net.wifi.WifiInfo;
import android.net.wifi.WifiManager;
import android.os.Build;
import android.provider.Settings;
import android.support.annotation.StringRes;
import android.telephony.TelephonyManager;
import android.text.TextUtils;
import android.util.DisplayMetrics;
import android.util.Log;
import android.view.View;
import android.view.inputmethod.InputMethodManager;
import android.widget.Toast;

import java.io.File;
import java.io.FileInputStream;
import java.io.FileNotFoundException;
import java.io.IOException;
import java.io.InputStream;
import java.math.BigInteger;
import java.security.MessageDigest;
import java.security.NoSuchAlgorithmException;
import java.util.Locale;
import java.util.UUID;

public class CommonUtils {
    private static final String TAG = "MD5";
    private static final String DEFAULT_MAC_ADDRESS = "02:00:00:00:00:00";


    /**
     * 获取移动设备的mac地址
     *
     * @param context
     * @return
     */
    public static String getMacAddress(Context context) {
        if (context == null) {
            return "";
        }
        WifiManager wifi = (WifiManager) context.getSystemService(Context.WIFI_SERVICE);
        if (wifi == null) {
            return "";
        }
        WifiInfo info = wifi.getConnectionInfo();
        String mac = info.getMacAddress();
        return mac;
    }

    /**
     * 获取状态栏的高度
     *
     * @param context 上下文
     * @return 高度
     */
    public static int getStatusBarHeight(Context context) {
        int result = 0;
        int resourceId = context.getResources().getIdentifier("status_bar_height", "dimen", "android");
        if (resourceId > 0) {
            result = context.getResources().getDimensionPixelSize(resourceId);
        }

        return result;
    }

    /**
     * 显示Toast提示
     *
     * @param context 上下文
     * @param resId   文本资源
     */
    public static void showToast(Context context, @StringRes int resId) {
        if (context != null) {
            Toast.makeText(context, resId, Toast.LENGTH_SHORT).show();
        }
    }

    /**
     * 显示Toast提示
     *
     * @param context 上下文
     * @param content 显示的文本
     */
    public static void showToast(Context context, String content) {
        if (context != null) {
            Toast.makeText(context, content, Toast.LENGTH_SHORT).show();
        }
    }

    /**
     * 隐藏键盘
     *
     * @param ctx
     * @param v
     * @return
     */
    public static boolean hideSoftInputFromWindow(Context ctx, View v) {
        InputMethodManager imm = (InputMethodManager) ctx
                .getSystemService(Context.INPUT_METHOD_SERVICE);
        if (imm.isActive()) {
            imm.hideSoftInputFromWindow(v.getWindowToken(), 0);
            return true;
        }
        return false;
    }




    /**
     * 获取手机屏幕的宽度
     *
     * @param ctx
     * @return
     */
    public static int getScreenWidth(Context ctx) {
        if (ctx == null) {
            return 0;
        }
        if (ctx instanceof Activity) {
            DisplayMetrics dm = new DisplayMetrics();
            ((Activity) ctx).getWindowManager().getDefaultDisplay()
                    .getMetrics(dm);
            return dm.widthPixels;
        } else {
            return 0;
        }
    }

    /**
     * 获取手机屏幕的宽度
     *
     * @param ctx
     * @return
     */
    public static int getScreenHeight(Context ctx) {
        if (ctx == null) {
            return 0;
        }
        if (ctx instanceof Activity) {
            DisplayMetrics dm = new DisplayMetrics();
            ((Activity) ctx).getWindowManager().getDefaultDisplay()
                    .getMetrics(dm);
            return dm.heightPixels;
        } else {
            return 0;
        }
    }

    /**
     * 获取手机屏幕的密度
     *
     * @param ctx
     * @return
     */
    public static int getScreenDensity(Context ctx) {
        if (ctx == null) {
            return 0;
        }
        if (ctx instanceof Activity) {
            DisplayMetrics dm = new DisplayMetrics();
            ((Activity) ctx).getWindowManager().getDefaultDisplay()
                    .getMetrics(dm);
            return dm.densityDpi;
        } else {
            return 0;
        }
    }

    /**
     * 从 manifest 获取 meta 数据
     *
     * @param context
     * @param key
     * @return
     */
    public static Object getMetaDataByKey(Context context, String key) {
        Object obj = null;
        try {
            ApplicationInfo info = context.getPackageManager().getApplicationInfo(
                    context.getPackageName(), PackageManager.GET_META_DATA);
            obj = info.metaData.get(key);
        } catch (Exception e) {
            e.printStackTrace();
        }
        return obj;
    }




    /**
     * 获取 Puid
     *
     * @return
     */
    public static String getPuid() {
        String serial;

        String mDevIDShort = "35" + Build.BOARD.length() % 10 + Build.BRAND.length() % 10

                + Build.CPU_ABI.length() % 10 + Build.DEVICE.length() % 10
                + Build.DISPLAY.length() % 10 + Build.HOST.length() % 10
                + Build.ID.length() % 10 + Build.MANUFACTURER.length() % 10
                + Build.MODEL.length() % 10 + Build.PRODUCT.length() % 10
                + Build.TAGS.length() % 10 + Build.TYPE.length() % 10
                + Build.USER.length() % 10; //13 位

        try {
            serial = Build.class.getField("SERIAL").get(null).toString();
            //API>=9 使用serial号
            return md5(new UUID(mDevIDShort.hashCode(), serial.hashCode()).toString());

        } catch (Exception exception) {
            //使用硬件信息拼凑出来的15位号码
            serial = "";
        }
        return md5(new UUID(mDevIDShort.hashCode(), serial.hashCode()).toString());
    }

    /**
     * 获取WiFi Mac
     *
     * @param context
     * @return
     */
    public static String getWifiMac(Context context) {

        WifiManager wm = (WifiManager) context.getSystemService(Context.WIFI_SERVICE);
        String wifiMac = wm.getConnectionInfo().getMacAddress();

        return wifiMac.equals(DEFAULT_MAC_ADDRESS) ? "" : wifiMac;

    }

    /**
     * 获取IMEI
     *
     * @param context
     * @return
     */
    public static String getImei(Context context) {

        TelephonyManager telephonyMgr = (TelephonyManager) context.getSystemService(Context.TELEPHONY_SERVICE);
        @SuppressLint("MissingPermission") String iMei = telephonyMgr.getDeviceId();

        return TextUtils.isEmpty(iMei) ? "" : iMei;
    }

    /**
     * 获取android ID
     *
     * @param context
     * @return
     */
    public static String getAndroidId(Context context) {
        String androidId = Settings.Secure.getString(context.getContentResolver(), Settings.Secure.ANDROID_ID);

        return TextUtils.isEmpty(androidId) ? "" : androidId;
    }

    /**
     * 用0补齐字符串至总长度
     *
     * @param strSrc   原字符串
     * @param totalLen 总长度
     * @return
     */
    public static String fillStr(String strSrc, int totalLen) {
        if (null == strSrc) {
            strSrc = "";
        }
        String tmp = "";
        int len = strSrc.length();
        if (len < totalLen) {
            for (int i = 0; i < totalLen - len; i++) {
                tmp += "0";
            }
            strSrc = tmp + strSrc;
        }

        return strSrc;
    }

    /**
     * md5 加密为16位
     *
     * @param sourceStr
     * @return
     */
    public static String md5(String sourceStr) {
        String result;
        try {
            MessageDigest md = MessageDigest.getInstance("MD5");
            md.update(sourceStr.getBytes());
            byte[] buffer = md.digest();
            StringBuilder buf = new StringBuilder("");

            for (int i : buffer) {
                if (i < 0) {
                    i += 256;
                }
                if (i < 16) {
                    buf.append("0");
                }
                buf.append(Integer.toHexString(i));
            }

            result = buf.toString().substring(8, 24);
        } catch (NoSuchAlgorithmException e) {
            e.printStackTrace();
            return "";
        }
        return result;
    }



    /** 获取屏幕的宽度 */
    public final static int getWindowsWidth(Activity activity) {
        DisplayMetrics dm = new DisplayMetrics();
        activity.getWindowManager().getDefaultDisplay().getMetrics(dm);
        return dm.widthPixels;
    }

    public final static int getScreenDensity(Activity activity) {

        DisplayMetrics dm = new DisplayMetrics();
        activity.getWindowManager().getDefaultDisplay().getMetrics(dm);

        return (dm.densityDpi / DisplayMetrics.DENSITY_DEFAULT);
    }

    public static int getSDKInt() {
        return android.os.Build.VERSION.SDK_INT;
    }
}
