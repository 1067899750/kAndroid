package com.github.tifezh.kchartlib.chart.comInterface;

/**
 *
 * Description  蜡烛图实体接口
 * Author puyantao
 * Email 1067899750@qq.com
 * Date 2018-10-26 17:39
 */

public interface ICandle {

    /**
     * 开盘价
     */
    float getOpenPrice();

    /**
     * 最高价
     */
    float getHighPrice();

    /**
     * 最低价
     */
    float getLowPrice();

    /**
     * 收盘价
     */
    float getClosePrice();

    /**
     * 五(月，日，时，分，5分等)均价
     */
    float getMA5Price();

    /**
     * 十(月，日，时，分，5分等)均价
     */
    float getMA10Price();

    /**
     * 二十(月，日，时，分，5分等)均价
     */
    float getMA20Price();



    /**
     * 二十六(月，日，时，分，5分等)均价
     */
    float getMA26Price();

    /**
     * 四十(月，日，时，分，5分等)均价
     */
    float getMA40Price();

    /**
     * 六十(月，日，时，分，5分等)均价
     */
    float getMA60Price();


    /**
     * 成交量
     */
    float getVolume();

    /**
     * 成交量变化
     */
    float getChgVolume();


    /**
     * 持仓量
     */
    float getInterest();

    /**
     * 持仓量变化
     */
    String getChgInterest();

    /**
     * 结算价
     */
    String getPreClose();



    /**
     * 价格涨跌
     */
    String getUpDown();

    /**
     * 价格幅度百分比
     */
    String getPercent();


   //boll

    /**
     * 上轨线
     */
    float getUp();

    /**
     * 中轨线
     */
    float getMb();

    /**
     * 下轨线
     */
    float getDn();
}
