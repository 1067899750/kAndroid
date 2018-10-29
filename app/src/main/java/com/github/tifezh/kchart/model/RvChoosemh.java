package com.github.tifezh.kchart.model;

/**
 * Description：选择时间字段
 * Author: chenshanshan
 * Email: 1175558532@qq.com
 * Date: 2018-10-12  14:22
 */
public class RvChoosemh {
    private String Value;
    private boolean isChoose;
    private String id;
    private boolean isMoreOpon;

    public boolean isMoreOpon() {
        return isMoreOpon;
    }

    public void setMoreOpon(boolean moreOpon) {
        isMoreOpon = moreOpon;
    }

    public String getValue() {
        return Value;
    }

    public void setValue(String value) {
        Value = value;
    }

    public boolean isChoose() {
        return isChoose;
    }

    public void setChoose(boolean choose) {
        isChoose = choose;
    }

    public String getId() {
        return id;
    }

    public void setId(String id) {
        this.id = id;
    }
}
