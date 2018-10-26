package com.github.tifezh.kchartlib.chart.formatter;

import com.github.tifezh.kchartlib.chart.base.IValueFormatter;

/**
 *
 * Description Value格式化类
 * Author puyantao
 * Email 1067899750@qq.com
 * Date 2018-10-26 17:37
 */


public class ValueFormatter implements IValueFormatter {
    @Override
    public String format(float value) {
        return String.format("%.2f", value);
    }
}
