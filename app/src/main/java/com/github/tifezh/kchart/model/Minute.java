package com.github.tifezh.kchart.model;

import com.github.tifezh.kchartlib.chart.comInterface.IMinuteLine;

import java.util.Date;

/**
 *
 * Description： 分时数据Model
 * Author: puyantao
 * Email: 1067899750@qq.com
 * Date: 2018-10-15 9:39
 */

public class Minute implements IMinuteLine {
    /**
     "ruleAt": 1539867600000, 日期  X轴值
     "last": null, 最新报价 Y轴值
     "open": null, 开盘价
     "ask1p": null, 卖价
     "ask1v": null, 卖量
     "bid1p": null, 买价
     "bid1v": null, 买量
     "highest": null, 最高价
     "lowest": null, 最低价
     "upLimit": null,
     "loLimit": null,
     "interest": null, 持仓量
     "volume": null, 成交量
     "turnover": null,
     "average": null, 均价
     "settle": null,结算价
     "close": null,收盘价
     "preSettle": null,前一日结算价
     "preClose": null,前一日收盘价
     "preInterest": null,前一日持仓量
     "chgInterest": null,持仓变化量
     "updown": null,涨跌
     "percent": null 涨跌幅度
     */

    public Date ruleAt;
    public String last; //成交价 最新报价 Y轴值
    public String average; //均价
    public String interest; //持仓量
    public String volume; //成交量

    public String settle; //结算价
    public String highest; //最高价
    public String lowest; //最低价

    public String open; //开盘价
    public String close; //收盘价


    public String ask1p; //卖价
    public String ask1v; //卖量
    public String bid1p; //买价
    public String bid1v; //买量
    public String updown; //涨跌
    public String percent; //涨跌幅度

    public String upLimit;
    public String loLimit;
    public String turnover;
    public String preSettle; //前一日结算价
    public String preClose; //前一日收盘价
    public String preInterest; //前一日持仓量
    public String chgInterest; //持仓变化量

    public float count; //总成交量


    /**
     *  用于MACD
     */
    public float dea;
    public float diff;
    public float macd;

    @Override
    public float getAverage() {
        if (average == null){
            return 0;
        }
        return Float.parseFloat(average);
    }

    @Override
    public float getLast() {
        if (last == null){
            return 0;
        }
        return Float.parseFloat(last);
    }

    @Override
    public Date getDate() {
        return ruleAt;
    }

    @Override
    public float getVolume() {
        if (volume == null){
            return 0;
        }
        return Float.parseFloat(volume);
    }

    @Override
    public float getOpen() {
        if (open == null){
            return 0;
        }
        return Float.parseFloat(open);
    }

    @Override
    public float getClose() {
        if (close == null){
            return 0;
        }
        return Float.parseFloat(close);
    }

    @Override
    public float getCount() {
        return count;
    }

    @Override
    public float getInterest() {
        if (interest == null){
            return 0;
        }
        return Float.parseFloat(interest);
    }

    @Override
    public float getChgInterest() {
        if (chgInterest != null) {
            return Float.parseFloat(chgInterest);
        } else {
            return 0;
        }
    }

    @Override
    public float getSettle() {
        if (settle == null){
            return 0;
        }
        return Float.valueOf(settle);
    }

    @Override
    public float getHighest() {
        if (highest == null){
            return 0;
        }
        return Float.valueOf(highest);
    }

    @Override
    public float getLowest() {
        if (lowest == null){
            return 0;
        }
        return Float.valueOf(lowest);
    }

    @Override
    public float getAsk1p() {
        if (ask1p == null){
            return 0;
        }
        return Float.valueOf(ask1p);
    }

    @Override
    public float getAsk1v() {
        if (ask1v == null){
            return 0;
        }
        return Float.valueOf(ask1v);
    }

    @Override
    public float getBid1p() {
        if (bid1p == null){
            return 0;
        }
        return Float.valueOf(bid1p);
    }

    @Override
    public float getBid1v() {
        if (bid1v == null){
            return 0;
        }
        return Float.valueOf(bid1v);
    }

    @Override
    public float getPreSettle() {
        if (preSettle == null){
            return 0;
        }
        return Float.valueOf(preSettle);
    }

    @Override
    public float getPreClose() {
        if (preClose == null){
            return 0;
        }
        return Float.valueOf(preClose);
    }

    @Override
    public float getPreInterest() {
        if (preInterest == null){
            return 0;
        }
        return Float.valueOf(preInterest);
    }

    @Override
    public float getUpdown() {
        if (updown == null){
            return 0;
        }
        return Float.parseFloat(updown);
    }

    @Override
    public String getPercent() {
        return percent;
    }

    @Override
    public float getUpLimit() {
        if (upLimit == null){
            return 0;
        }
        return Float.parseFloat(upLimit);
    }

    @Override
    public float getLoLimit() {
        if (loLimit == null){
            return 0;
        }
        return Float.parseFloat(loLimit);
    }

    @Override
    public float getTurnover() {
        if (turnover == null){
            return 0;
        }
        return Float.parseFloat(turnover);
    }

    @Override
    public float getDea() {
        return dea;
    }

    @Override
    public float getDiff() {
        return diff;
    }

    @Override
    public float getMacd() {
        return macd;
    }


}







