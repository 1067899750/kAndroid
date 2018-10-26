package com.github.tifezh.kchartlib.chart.comInterface;

/**
 *
 * Description MACD指标(指数平滑移动平均线)接口
 * Author puyantao
 * Email 1067899750@qq.com
 * Date 2018-10-26 17:40
 */


public interface IMACD {


    /**
     * DEA值
     */
    float getDea();

    /**
     * DIF值
     */
    float getDif();

    /**
     * MACD值
     */
    float getMacd();

}
