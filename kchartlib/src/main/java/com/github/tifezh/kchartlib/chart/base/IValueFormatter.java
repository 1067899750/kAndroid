package com.github.tifezh.kchartlib.chart.base;

/**
 *
 * Description Value格式化接口
 * Author puyantao
 * Email 1067899750@qq.com
 * Date 2018-10-26 17:41
 */


public interface IValueFormatter {
    /**
     * 格式化value
     *
     * @param value 传入的value值
     * @return 返回字符串
     */
    String format(float value);
}
