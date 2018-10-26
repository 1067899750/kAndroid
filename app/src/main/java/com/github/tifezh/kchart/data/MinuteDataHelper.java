package com.github.tifezh.kchart.data;

import android.util.Log;

import com.github.tifezh.kchart.model.Minute;

import java.util.List;

public class MinuteDataHelper {

    /**
     * 计算macd
     *
     * @param datas
     */
    public static void calculateMACD(List<Minute> datas) {
        float ema12 = 0;
        float ema26 = 0;
        float dif = 0;
        float dea = 0;
        float macd = 0;

        for (int i = 0; i < datas.size(); i++) {
            Minute point = datas.get(i);
            final float closePrice = point.getClose();
            if (i == 0) {
                ema12 = closePrice;
                ema26 = closePrice;
            } else {
//                EMA（12） = 前一日EMA（12） X 11/13 + 今日收盘价 X 2/13
//                EMA（26） = 前一日EMA（26） X 25/27 + 今日收盘价 X 2/27
                ema12 = ema12 * 11f / 13f + closePrice * 2f / 13f;
                ema26 = ema26 * 25f / 27f + closePrice * 2f / 27f;
            }
//            DIF = EMA（12） - EMA（26） 。
//            今日DEA = （前一日DEA X 8/10 + 今日DIF X 2/10）
//            用（DIF-DEA）*2即为MACD柱状图。
            dif = ema12 - ema26;
            dea = dea * 8f / 10f + dif * 2f / 10f;
            macd = (dif - dea) * 2f;
//            Log.i("---> diff :", dif + "--; --" + i);
//            Log.i("---> dea :", dea + "--; --" + i);
//            Log.i("---> macd :", macd + "--; --" + i);
            point.diff = dif;
            point.dea = dea;
            point.macd = macd;
        }

    }
}
