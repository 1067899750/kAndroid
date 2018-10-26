package com.github.tifezh.kchart.model;

import com.github.tifezh.kchartlib.chart.comInterface.IMinuteTime;

import java.util.Date;

public class MinuteTime implements IMinuteTime {
    public Date start;
    public Date end;

    @Override
    public Date getStartDate() {
        return start;
    }

    @Override
    public Date getEndDate() {
        return end;
    }
}










