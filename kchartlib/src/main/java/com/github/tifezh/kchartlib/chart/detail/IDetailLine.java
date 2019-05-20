package com.github.tifezh.kchartlib.chart.detail;


/**
 * @Describe 详情Model 实现类
 * @Author puyantao
 * @Email 1067899750@qq.com
 * @create 2019/5/20 11:19
 */
public interface IDetailLine {

    /**
     * 该指标对应的时间
     */
    Integer getDate();

    /**
     * @return 获取平均值
     */
    float getAverageValue();

    /**
     * @return 获取数值
     */
    float getValue();


}











