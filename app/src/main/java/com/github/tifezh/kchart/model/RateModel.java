package com.github.tifezh.kchart.model;



import com.github.tifezh.kchartlib.chart.rate.IRate;

import java.util.Date;

public class RateModel implements IRate {
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
