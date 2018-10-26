package com.github.tifezh.kchart.model;

import com.github.tifezh.kchartlib.chart.comInterface.IMinuteTime;

import java.util.Date;
/**
 *
 * Description
 * Author puyantao
 * Email 1067899750@qq.com
 * Date 2018-10-26 17:44
 */

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










