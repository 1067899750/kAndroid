package com.github.tifezh.kchartlib.chart.formatter;

import com.github.tifezh.kchartlib.chart.base.IDateTimeFormatter;
import com.github.tifezh.kchartlib.utils.DateUtil;

import java.util.Date;

/**
 *
 * Description 时间格式化器
 * Author puyantao
 * Email 1067899750@qq.com
 * Date 2018-10-26 17:38
 */


public class DateFormatter implements IDateTimeFormatter {
    @Override
    public String format(Date date) {
        if (date != null) {
            return DateUtil.dateFormat.format(date);
        } else {
            return "";
        }
    }
}
