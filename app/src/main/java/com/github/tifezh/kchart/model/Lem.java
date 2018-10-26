package com.github.tifezh.kchart.model;

import com.github.tifezh.kchartlib.chart.comInterface.ILem;

import java.util.Date;

public class Lem implements ILem {

    public String chgInterest;
    public String chgVolume;
    public String close;
    public String contract;
    public String highest;
    public String interest;
    public String lowest;
    public String open;
    public String percent;
    public String preClose;
    public String preSettle;
    public long ruleAt;
    public String settle;
    public Object source;
    public String updown;
    public String volume;

    @Override
    public Date getDate() {
        return new Date(ruleAt);
    }

    @Override
    public float getValue() {
        return Float.valueOf(highest);
    }

    @Override
    public float getPrice() {
        return Float.valueOf(settle);
    }


}











