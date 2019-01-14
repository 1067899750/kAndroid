package com.github.tifezh.kchartlib.chart.comInterface;

import java.util.Date;

/**
 * 分时图实体接口
 *
 */

public interface IMinuteLine {

    /**
     * @return 获取均价
     */
    float getAverage();

    /**
     * @return 获取成交价
     */
    float getLast();

    /**
     * 该指标对应的时间
     */
    Date getDate();

    /**
     * 成交量
     */
    float getVolume();

    /**
     * 成交量变化量
     */
    String getChgVolume();

    /**
     * 开盘价
     * @return
     */
    float getOpen();


    /**
     * 收盘价
     * @return
     */
    float getClose();

    /**
     * 总成交量
     * @return
     */
    float getCount();

    /**
     * 持仓量
     * @return
     */
    float getInterest();

    //持仓变化量
    String getChgInterest();

    //结算价
    float getSettle();

    //最高价
    float getHighest();

    //最低价
    float getLowest();

    //卖价
    float getAsk1p();

    //卖量
    float getAsk1v();

    //买价
    float getBid1p();

    //买量
    float getBid1v();

    //前一日结算价
    float getPreSettle();

    //前一日收盘价
    float getPreClose();

    //前一日持仓量
    float getPreInterest();

    //前一日收盘价
    String getUpdown();

    //前一日持仓量
    String getPercent();


    float getUpLimit();
    float getLoLimit();
    float getTurnover();


    /**
     * 用于MACD
     * @return
     */
    float getDea();
    float getDiff();
    float getMacd();





}



