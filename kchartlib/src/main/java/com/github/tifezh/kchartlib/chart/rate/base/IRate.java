package com.github.tifezh.kchartlib.chart.rate.base;

import java.util.Date;

public interface IRate {

    /**
     * 该指标对应的时间
     */
    Date getDate();

    /**
     * 值
     */
    float getValue();

    /**
     * 变化量
     */
    String getChange();

    String getPercent();

}



