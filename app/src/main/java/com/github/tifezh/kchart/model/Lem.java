package com.github.tifezh.kchart.model;

import com.github.tifezh.kchartlib.chart.comInterface.ILem;

import java.util.Date;

public class Lem implements ILem {
    public long ruleAt;
    public String curve;
    public String volume;

    @Override
    public Date getDate() {
        return new Date(ruleAt);
    }

    @Override
    public Float getCurve() {
        return Float.valueOf(curve);
    }

    @Override
    public Float getVolume() {
        return Float.valueOf(volume);
    }


}











