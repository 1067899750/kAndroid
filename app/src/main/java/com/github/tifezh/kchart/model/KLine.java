package com.github.tifezh.kchart.model;

import com.github.tifezh.kchartlib.chart.comInterface.IKLine;
import com.github.tifezh.kchartlib.utils.DateUtil;

/**
 *
 * Description K线图实体
 * Author puyantao
 * Email 1067899750@qq.com
 * Date 2018-10-26 17:43
 */

public class KLine implements IKLine {
    public String contract;//合约名

    public String preClose;//结算价
    public String preSettle;//昨日结算价
    public String source;
    public float interest;
    public String chgInterest;//持仓量变化
    public String updown;
    public String percent;

    public long ruleAt;//时间
    public float open;//开盘价
    public float highest;//最高价
    public float lowest;//最低价
    public float close;//收盘价
    public float volume;//成交量


    public float MA5Price;

    public float MA10Price;

    public float MA20Price;

    public float MA26Price;

    public float MA40Price;

    public float MA60Price;

    public float dea;

    public float dif;

    public float macd;

    public float k;

    public float d;

    public float j;

    public float rsi1;

    public float rsi2;

    public float rsi3;

    public float up;

    public float mb;

    public float dn;

    public float MA5Volume;

    public float MA10Volume;


    public String getPreSettle() {
        return preSettle;
    }

    public String getContract() {
        return contract;
    }

    public void setContract(String contract) {
        this.contract = contract;
    }

    public String getSource() {
        return source;
    }

    public void setSource(String source) {
        this.source = source;
    }

    @Override
    public String getPreClose() {
        return preClose==null?"0":preClose;
    }

    @Override
    public String getChgInterest() {
        return chgInterest==null?"0":chgInterest;
    }

    public void setChgInterest(String chgInterest) {
        this.chgInterest = chgInterest;
    }

    public String getDatetime() {
        return DateUtil.getStringDateByLong(ruleAt, 7);
    }

    @Override
    public float getOpenPrice() {
        return open;
    }

    @Override
    public float getHighPrice() {
        return highest;
    }

    @Override
    public float getLowPrice() {
        return lowest;
    }

    @Override
    public float getClosePrice() {
        return close;
    }

    @Override
    public float getMA5Price() {
        return MA5Price;
    }

    @Override
    public float getMA10Price() {
        return MA10Price;
    }

    @Override
    public float getMA20Price() {
        return MA20Price;
    }

    @Override
    public float getMA26Price() {
        return MA26Price;
    }

    @Override
    public float getMA40Price() {
        return MA40Price;
    }

    @Override
    public float getMA60Price() {
        return MA60Price;
    }

    @Override
    public float getDea() {
        return dea;
    }

    @Override
    public float getDif() {
        return dif;
    }

    @Override
    public float getMacd() {
        return macd;
    }

    @Override
    public float getK() {
        return k;
    }

    @Override
    public float getD() {
        return d;
    }

    @Override
    public float getJ() {
        return j;
    }

    @Override
    public float getRsi1() {
        return rsi1;
    }

    @Override
    public float getRsi2() {
        return rsi2;
    }

    @Override
    public float getRsi3() {
        return rsi3;
    }

    @Override
    public float getUp() {
        return up;
    }

    @Override
    public float getMb() {
        return mb;
    }

    @Override
    public float getDn() {
        return dn;
    }

    @Override
    public float getVolume() {
        return volume;
    }

    @Override
    public float getChgVolume() {//成交量变化
        return 0;
    }

    @Override
    public float getInterest() {//持仓量
        return interest;
    }


    @Override
    public String getUpDown() {
        return updown==null?"0":updown;
    }

    @Override
    public String getPercent() {
        return percent==null?"0%":percent;
    }


    @Override
    public float getMA5Volume() {
        return MA5Volume;
    }

    @Override
    public float getMA10Volume() {
        return MA10Volume;
    }


}
