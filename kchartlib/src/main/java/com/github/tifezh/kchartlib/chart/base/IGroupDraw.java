package com.github.tifezh.kchartlib.chart.base;

import android.graphics.Canvas;
import android.support.annotation.NonNull;
import android.support.annotation.Nullable;

import com.github.tifezh.kchartlib.chart.groupview.BaseGroupView;
import com.github.tifezh.kchartlib.chart.view.BaseKChartView;

public interface IGroupDraw<T> {

    void drawTranslated(@Nullable T lastPoint, @NonNull T curPoint, float lastX, float curX, @NonNull Canvas canvas,
                        @NonNull BaseGroupView view, int position);

    void drawText(@NonNull Canvas canvas, @NonNull BaseGroupView view, int position, float x, float y);


    float getMaxValue(T point);


    /**
     * 获取当前实体中最小的值
     *
     * @param point
     * @return
     */
    float getMinValue(T point);



    /**
     * 获取value格式化器
     */
    IValueFormatter getValueFormatter();


    /**
     * 设置指标文字颜色
     */

    void setTargetColor( int ...color);




}












