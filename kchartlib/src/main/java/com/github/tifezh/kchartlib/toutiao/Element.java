package com.github.tifezh.kchartlib.toutiao;

import android.graphics.Bitmap;

/**
 *
 * @description
 * @author puyantao
 * @date 2020/7/29 16:21
 */

public interface Element {

    int getX();

    int getY();

    Bitmap getBitmap();

    void evaluate(int start_x, int start_y, double time);

}
