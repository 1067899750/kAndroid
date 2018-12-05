package com.github.tifezh.kchart.model;

import com.github.tifezh.kchartlib.chart.rate.base.IRate;
import com.github.tifezh.kchartlib.chart.rate.base.IRateDraw;

import java.util.Date;

public class RateModel implements IRate {
    public Date date;
    public float value;
    public String change;



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
}
