package com.github.tifezh.kchartlib.chart.comInterface;

/**
 *
 * Description  成交量接口
 * Author puyantao
 * Email 1067899750@qq.com
 * Date 2018-10-26 17:41
 */


public interface IVolume {

    /**
     * 开盘价
     */
    float getOpenPrice();

    /**
     * 收盘价
     */
    float getClosePrice();

    /**
     * 成交量
     */
    float getVolume();


    /**
     * 持仓量
     */
    float getInterest();

    /**
     * 五(月，日，时，分，5分等)均量
     */
    float getMA5Volume();

    /**
     * 十(月，日，时，分，5分等)均量
     */
    float getMA10Volume();
}
