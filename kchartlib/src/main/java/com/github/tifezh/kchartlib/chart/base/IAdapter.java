package com.github.tifezh.kchartlib.chart.base;

import android.database.DataSetObserver;

import java.util.Date;


/**
 *
 * Description 数据适配器
 * Author puyantao
 * Email 1067899750@qq.com
 * Date 2018-10-26 17:41
 */


public interface IAdapter {
    /**
     * 获取点的数目
     *
     * @return
     */
    int getCount();

    /**
     * 通过序号获取item
     *
     * @param position 对应的序号
     * @return 数据实体
     */
    Object getItem(int position);

    /**
     * 通过序号获取时间
     *
     * @param position
     * @return
     */
    Date getDate(int position);


    /**
     * 注册一个数据观察者
     *
     * @param observer 数据观察者
     */
    void registerDataSetObserver(DataSetObserver observer);

    /**
     * 移除一个数据观察者
     *
     * @param observer 数据观察者
     */
    void unregisterDataSetObserver(DataSetObserver observer);

    /**
     * 当数据发生变化时调用
     */
    void notifyDataSetChanged();
}
