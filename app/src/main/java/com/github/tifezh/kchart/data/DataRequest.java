package com.github.tifezh.kchart.data;

import android.content.Context;
import android.support.annotation.NonNull;
import android.support.annotation.Nullable;

import com.github.tifezh.kchart.model.FootballModel;
import com.github.tifezh.kchart.model.KLine;
import com.github.tifezh.kchart.model.Lem;
import com.github.tifezh.kchart.model.MinuteLine;
import com.github.tifezh.kchart.model.MinuteParent;
import com.github.tifezh.kchart.model.TrendChartModel;
import com.google.gson.Gson;
import com.google.gson.reflect.TypeToken;

import java.io.InputStream;
import java.util.ArrayList;
import java.util.Date;
import java.util.List;
import java.util.Random;

/**
 *
 * Description 模拟网络请求
 * Author puyantao
 * Email 1067899750@qq.com
 * Date 2018-10-26 17:43
 */


public class DataRequest {
//    private static List<KLine> datas = null;
    private static Random random = new Random();

    public static String getStringFromAssert(Context context, String fileName) {
        try {
            InputStream in = context.getResources().getAssets().open(fileName);
            int length = in.available();
            byte[] buffer = new byte[length];
            in.read(buffer);
            return new String(buffer, 0, buffer.length, "UTF-8");
        } catch (Exception e) {
            e.printStackTrace();
        }
        return "";
    }

    public static List<KLine> getALL(Context context, int type) {
//        if (datas == null) {
            String json=null;
            switch (type){
                case 0:
                    json=getStringFromAssert(context, "kline_day.json");//日K
                    break;
                case 1:
                    json=getStringFromAssert(context, "kline_1m.json");//1 min
                    break;
                case 3:
                    json=getStringFromAssert(context, "kline_3m.json");//3 min
                    break;
                case 5:
                    json=getStringFromAssert(context, "kline_5m.json");//5 min
                    break;
            }
            final List<KLine> data = new Gson().fromJson(json, new TypeToken<List<KLine>>() {}.getType());
            DataHelper.calculate(data);
//            datas = data;
//        }
        return data;
    }

    /**
     * 分页查询
     *
     * @param context
     * @param offset  开始的索引
     * @param size    每次查询的条数
     */
    public static List<KLine> getData(Context context, int offset, int size, int type) {
        List<KLine> all = getALL(context,type);
        List<KLine> data = new ArrayList<>();
        int start = Math.max(0, all.size() - 1 - offset - size);
        int stop = Math.min(all.size(), all.size() - offset);
        for (int i = start; i < stop; i++) {
            data.add(all.get(i));
        }
        return data;
    }

    /**
     * 随机生成分时数据
     */
    public static List<MinuteLine> getMinuteData(@NonNull Date startTime,
                                                 @NonNull Date endTime,
                                                 @Nullable Date firstEndTime,
                                                 @Nullable Date secondStartTime) {
        List<MinuteLine> list = new ArrayList<>();
        long startDate = startTime.getTime();
        if (firstEndTime == null && secondStartTime == null) {
            while (startDate <= endTime.getTime()) {
                MinuteLine data = new MinuteLine();
                data.time = new Date(startDate);
                startDate += 60000;
                list.add(data);
            }
        } else {
            while (startDate <= firstEndTime.getTime()) {
                MinuteLine data = new MinuteLine();
                data.time = new Date(startDate);
                startDate += 60000;
                list.add(data);
            }
            startDate = secondStartTime.getTime();
            while (startDate <= endTime.getTime()) {
                MinuteLine data = new MinuteLine();
                data.time = new Date(startDate);
                startDate += 60000;
                list.add(data);
            }
        }
        randomLine(list);
        randomVolume(list);
        float sum = 0;
        for (int i = 0; i < list.size(); i++) {
            MinuteLine data = list.get(i);
            sum += data.price;
            data.avg = 1f * sum / (i + 1);
        }
        return list;
    }

    private static void randomVolume(List<MinuteLine> list) {
        for (MinuteLine data : list) {
            data.volume = (int) (Math.random() * Math.random() * Math.random() * Math.random() * 10000000);
        }
    }

    /**
     * 生成随机曲线
     */
    private static void randomLine(List<MinuteLine> list) {
        float STEP_MAX = 0.9f;
        float STEP_CHANGE = 1f;
        float HEIGHT_MAX = 200;

        float height = (float) (Math.random() * HEIGHT_MAX);
        float slope = (float) ((Math.random() * STEP_MAX) * 2 - STEP_MAX);

        for (int x = 0; x < list.size(); x++) {
            height += slope;
            slope += (Math.random() * STEP_CHANGE) * 2 - STEP_CHANGE;

            if (slope > STEP_MAX) {
                slope = STEP_MAX;
            }
            if (slope < -STEP_MAX) {
                slope = -STEP_MAX;
            }

            if (height > HEIGHT_MAX) {
                height = HEIGHT_MAX;
                slope *= -1;
            }
            if (height < 0) {
                height = 0;
                slope *= -1;
            }

            list.get(x).price = height + 1000;
        }
    }


    /**
     * 模拟数据
     *
     * @return
     */
    public static MinuteParent.DataBean getMinuteData(Context context) {
        MinuteParent minuteModel = new Gson().fromJson(getStringFromAssert(context, "cu12.json"),
                MinuteParent.class);

//        MinuteParent minuteModel = new Gson().fromJson(getStringFromAssert(context, "testMinuteline.json"),
//                MinuteParent.class);

//        MinuteParent minuteModel = new Gson().fromJson(getStringFromAssert(context, "testTimeShare.json"),
//                MinuteParent.class);

        MinuteParent.DataBean data = minuteModel.getData();
        return data;
    }




    /**
     * 模拟数据
     *
     * @return
     */
    public static  List<Lem>  getLmeData(Context context) {
        List<Lem> lemModels = new Gson().fromJson(getStringFromAssert(context, "lem.json"),
                new TypeToken<List<Lem>>() {}.getType());

        return lemModels;
    }


    /**
     *  利率模拟数据
     * @param context
     * @return
     */
    public static  TrendChartModel getRateData(Context context) {
        TrendChartModel trendChartModel = new Gson().fromJson(getStringFromAssert(context, "rate.json"),
                new TypeToken<TrendChartModel>() {}.getType());

        return trendChartModel;
    }


    /**
     *  利率模拟数据
     * @param context
     * @return
     */
    public static FootballModel getFootBallData(Context context) {
        FootballModel footballModel = new Gson().fromJson(getStringFromAssert(context, "football.json"),
                new TypeToken<FootballModel>() {}.getType());

        return footballModel;
    }
}

















