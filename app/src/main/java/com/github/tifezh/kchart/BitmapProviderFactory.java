package com.github.tifezh.kchart;

import android.content.Context;

import com.github.tifezh.kchartlib.toutiao.BitmapProvider;

/**
 * @author puyantao
 * @description
 * @date 2020/7/29 16:32
 */
public class BitmapProviderFactory {

    public static BitmapProvider.Provider getHDProvider(Context context) {
        return new BitmapProvider.Builder(context)
                .setDrawableArray(new int[]{R.drawable.mei_ic_praise_1, R.drawable.mei_ic_praise_2, R.drawable.mei_ic_praise_3,
                        R.drawable.mei_ic_praise_4, R.drawable.mei_ic_praise_5, R.drawable.mei_ic_praise_6,
                        R.drawable.mei_ic_praise_7, R.drawable.mei_ic_praise_8, R.drawable.mei_ic_praise_9,
                        R.drawable.mei_ic_praise_10, R.drawable.mei_ic_praise_11, R.drawable.mei_ic_praise_12, R.drawable.mei_ic_praise_13,
                        R.drawable.mei_ic_praise_14})

                .setNumberDrawableArray(new int[]{R.drawable.multi_digg_num_0, R.drawable.multi_digg_num_1, R.drawable.multi_digg_num_2, R.drawable.multi_digg_num_3,
                        R.drawable.multi_digg_num_4, R.drawable.multi_digg_num_5, R.drawable.multi_digg_num_6, R.drawable.multi_digg_num_7,
                        R.drawable.multi_digg_num_8, R.drawable.multi_digg_num_9})

                .setLevelDrawableArray(new int[]{R.drawable.multi_digg_word_level_1, R.drawable.multi_digg_word_level_2, R.drawable.multi_digg_word_level_3})
                .build();
    }


    public static BitmapProvider.Provider getSmoothProvider(Context context) {
        return new BitmapProvider.Builder(context).build();
    }

}
