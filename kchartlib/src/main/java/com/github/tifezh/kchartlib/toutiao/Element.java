package com.github.tifezh.kchartlib.toutiao;

import android.graphics.Bitmap;

/**
 * @author puyantao
 * @description 估值器
 * @date 2020/7/29 16:21
 */

public interface Element {
    /**
     * 获取透明度
     * @return
     */
    int getAlpha();

    int getX();

    int getY();

    /**
     * 获取图片
     *
     * @return
     */
    Bitmap getBitmap();

    /**
     * 根据 start_x 和 start_y 计算 x, y
     *
     * @param width   点击试图的宽
     * @param height  点击试图的高
     * @param start_x 起始点 x
     * @param start_y 起始点 y
     * @param time
     */
    void evaluate(int width, int height, int start_x, int start_y, double time);

}
