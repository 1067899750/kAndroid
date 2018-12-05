package com.github.tifezh.kchartlib.chart.rate.base;

import android.graphics.Canvas;
import android.support.annotation.NonNull;
import android.support.annotation.Nullable;

import com.github.tifezh.kchartlib.chart.base.IValueFormatter;
import com.github.tifezh.kchartlib.chart.rate.BaseRateView;
import com.github.tifezh.kchartlib.chart.view.BaseKChartView;

public interface IRateDraw<T> {


    void drawTranslated(@Nullable T lastPoint, @NonNull T curPoint, float lastX, float curX,
                        @NonNull Canvas canvas, @NonNull BaseRateView view, int position);


    void drawText(@NonNull Canvas canvas, @NonNull BaseRateView view, int position, float x, float y);

    /**
     * 获取当前实体中的值
     *
     * @param point
     * @return
     */
    float getValue(T point);



    /**
     * 获取value格式化器
     */
    IValueFormatter getValueFormatter();


    /**
     * 设置指标文字颜色
     */

    void setTargetColor( int ...color);
}
