package com.github.tifezh.kchart.model;

import com.github.tifezh.kchartlib.chart.detail.IDetailLine;

/**
 * @Describe
 * @Author puyantao
 * @Email 1067899750@qq.com
 * @create 2019/5/20 13:53
 */
public class DetailModel implements IDetailLine {
    public Integer data;
    public float averageValue;
    public float value;

    @Override
    public Integer getDate() {
        return data;
    }

    @Override
    public float getAverageValue() {
        return averageValue;
    }

    @Override
    public float getValue() {
        return value;
    }
}
















