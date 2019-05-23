package com.github.tifezh.kchart.model;



import com.github.tifezh.kchartlib.chart.line.ILineRate;

import java.util.Date;

/**
 * @Describe
 * @Author puyantao
 * @Email 1067899750@qq.com
 * @create 2019/5/23 9:43
 */
public class LineModel implements ILineRate {

    public Date date;
    public float value;
    public String change;
    public String percent;

    @Override
    public Date getDate() {
        return date;
    }

    @Override
    public float getValue() {
        return value;
    }

    @Override
    public String getChange() {
        return change;
    }

    @Override
    public String getPercent() {
        return percent;
    }
}
