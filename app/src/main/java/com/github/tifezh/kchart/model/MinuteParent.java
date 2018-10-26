package com.github.tifezh.kchart.model;

import java.util.List;

public class MinuteParent {

    private String code;
    private String message;
    private DataBeanX data;

    public String getCode() {
        return code;
    }

    public void setCode(String code) {
        this.code = code;
    }

    public String getMessage() {
        return message;
    }

    public void setMessage(String message) {
        this.message = message;
    }

    public DataBeanX getData() {
        return data;
    }

    public void setData(DataBeanX data) {
        this.data = data;
    }

    public static class DataBeanX {
        private long min;
        private long max;
        private String preClose;
        private String preSettle; //中值
        private int allTradeTotal;
        private boolean businessStop;
        private List<DataBean> data; //数据
        private List<TradeRangesBean> tradeRanges;//交易时间

        public long getMin() {
            return min;
        }

        public void setMin(long min) {
            this.min = min;
        }

        public long getMax() {
            return max;
        }

        public void setMax(long max) {
            this.max = max;
        }

        public String getPreClose() {
            return preClose;
        }

        public void setPreClose(String preClose) {
            this.preClose = preClose;
        }

        public String getPreSettle() {
            return preSettle;
        }

        public void setPreSettle(String preSettle) {
            this.preSettle = preSettle;
        }

        public int getAllTradeTotal() {
            return allTradeTotal;
        }

        public void setAllTradeTotal(int allTradeTotal) {
            this.allTradeTotal = allTradeTotal;
        }

        public boolean isBusinessStop() {
            return businessStop;
        }

        public void setBusinessStop(boolean businessStop) {
            this.businessStop = businessStop;
        }

        public List<DataBean> getData() {
            return data;
        }

        public void setData(List<DataBean> data) {
            this.data = data;
        }

        public List<TradeRangesBean> getTradeRanges() {
            return tradeRanges;
        }

        public void setTradeRanges(List<TradeRangesBean> tradeRanges) {
            this.tradeRanges = tradeRanges;
        }

        public static class DataBean {


            /**,
             * <p>
             * "ruleAt": 1539867600000, 日期  X轴值
             * "last": null, 最新报价 Y轴值
             * "open": null, 开盘价
             * "ask1p": null, 卖价
             * "ask1v": null, 卖量
             * "bid1p": null, 买价
             * "bid1v": null, 买量
             * "highest": null, 最高价
             * "lowest": null, 最低价
             * "upLimit": null,
             * "loLimit": null,
             * "interest": null, 持仓量
             * "volume": null, 成交量
             * "turnover": null,
             * "average": null, 均价
             * "settle": null,结算价
             * "close": null,收盘价
             * "preSettle": null,前一日结算价
             * "preClose": null,前一日收盘价
             * "preInterest": null,前一日持仓量
             * "chgInterest": null,持仓变化量
             * "updown": null,涨跌
             * "percent": null 涨跌幅度
             */



            private long ruleAt; //日期  X轴值
            private String last; //成交价 最新报价 Y轴值
            private String interest; //持仓量
            private String chgInterest; //持仓变化量
            private String volume; //成交量
            private String average; //均价
            private String updown; //涨跌
            private String percent; //涨跌幅度

            private String open; //开盘价
            private String ask1p; //卖价
            private String ask1v; //卖量
            private String bid1p; //买价
            private String bid1v; //买量
            private String highest; //最高价
            private String lowest; //最低价
            private String upLimit;
            private String loLimit;
            private String turnover;
            private String settle; //结算价
            private String close; //收盘价
            private String preSettle; //前一日结算价
            private String preClose; //前一日持仓量
            private String preInterest; //持仓变化量

            public String getChgInterest() {
                return chgInterest;
            }

            public void setChgInterest(String chgInterest) {
                this.chgInterest = chgInterest;
            }
            public String getOpen() {
                return open;
            }

            public void setOpen(String open) {
                this.open = open;
            }

            public String getAsk1p() {
                return ask1p;
            }

            public void setAsk1p(String ask1p) {
                this.ask1p = ask1p;
            }

            public String getAsk1v() {
                return ask1v;
            }

            public void setAsk1v(String ask1v) {
                this.ask1v = ask1v;
            }

            public String getBid1p() {
                return bid1p;
            }

            public void setBid1p(String bid1p) {
                this.bid1p = bid1p;
            }

            public String getBid1v() {
                return bid1v;
            }

            public void setBid1v(String bid1v) {
                this.bid1v = bid1v;
            }

            public String getHighest() {
                return highest;
            }

            public void setHighest(String highest) {
                this.highest = highest;
            }

            public String getLowest() {
                return lowest;
            }

            public void setLowest(String lowest) {
                this.lowest = lowest;
            }

            public String getUpLimit() {
                return upLimit;
            }

            public void setUpLimit(String upLimit) {
                this.upLimit = upLimit;
            }

            public String getLoLimit() {
                return loLimit;
            }

            public void setLoLimit(String loLimit) {
                this.loLimit = loLimit;
            }

            public String getTurnover() {
                return turnover;
            }

            public void setTurnover(String turnover) {
                this.turnover = turnover;
            }

            public String getSettle() {
                return settle;
            }

            public void setSettle(String settle) {
                this.settle = settle;
            }

            public String getClose() {
                return close;
            }

            public void setClose(String close) {
                this.close = close;
            }

            public String getPreSettle() {
                return preSettle;
            }

            public void setPreSettle(String preSettle) {
                this.preSettle = preSettle;
            }

            public String getPreClose() {
                return preClose;
            }

            public void setPreClose(String preClose) {
                this.preClose = preClose;
            }

            public String getPreInterest() {
                return preInterest;
            }

            public void setPreInterest(String preInterest) {
                this.preInterest = preInterest;
            }

            public long getRuleAt() {
                return ruleAt;
            }

            public void setRuleAt(long ruleAt) {
                this.ruleAt = ruleAt;
            }

            public String getLast() {
                return last;
            }

            public void setLast(String last) {
                this.last = last;
            }

            public String getInterest() {
                return interest;
            }

            public void setInterest(String interest) {
                this.interest = interest;
            }

            public String getVolume() {
                return volume;
            }

            public void setVolume(String volume) {
                this.volume = volume;
            }

            public String getAverage() {
                return average;
            }

            public void setAverage(String average) {
                this.average = average;
            }

            public String getUpdown() {
                return updown;
            }

            public void setUpdown(String updown) {
                this.updown = updown;
            }

            public String getPercent() {
                return percent;
            }

            public void setPercent(String percent) {
                this.percent = percent;
            }
        }

        public static class TradeRangesBean {
            /**
             * start : 1539090000000
             * end : 1539104400000
             */

            private long start;
            private long end;

            public long getStart() {
                return start;
            }

            public void setStart(long start) {
                this.start = start;
            }

            public long getEnd() {
                return end;
            }

            public void setEnd(long end) {
                this.end = end;
            }
        }
    }
}
