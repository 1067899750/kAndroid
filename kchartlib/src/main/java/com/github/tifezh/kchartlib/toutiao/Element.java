package com.github.tifezh.kchartlib.toutiao;

import android.graphics.Bitmap;

/**
 *
 * @description 估值器
 * @author puyantao
 * @date 2020/7/29 16:21
 */

public interface Element {

    int getX();

    int getY();

    /**
     * 获取图片
     * @return
     */
    Bitmap getBitmap();

    /**
     * 根据 start_x 和 start_y 计算 x, y
     * @param start_x
     * @param start_y
     * @param time
     */
    void evaluate(int start_x, int start_y, double time);

}
