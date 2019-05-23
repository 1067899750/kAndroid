package com.github.tifezh.kchartlib.chart.line;

import java.util.Date;

public interface ILineRate {

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



