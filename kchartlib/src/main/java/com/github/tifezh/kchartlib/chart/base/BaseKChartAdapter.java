package com.github.tifezh.kchartlib.chart.base;

import android.database.DataSetObservable;
import android.database.DataSetObserver;

import com.github.tifezh.kchartlib.chart.base.IAdapter;

/**
 *
 * Description k线图的数据适配器
 * Author puyantao
 * Email 1067899750@qq.com
 * Date 2018-10-26 17:41
 */


public abstract class BaseKChartAdapter implements IAdapter {

    private final DataSetObservable mDataSetObservable = new DataSetObservable();

    public void notifyDataSetChanged() {
        if (getCount() > 0) {
            mDataSetObservable.notifyChanged();
        } else {
            mDataSetObservable.notifyInvalidated();
        }
    }


    @Override
    public void registerDataSetObserver(DataSetObserver observer) {
        mDataSetObservable.registerObserver(observer);
    }

    @Override
    public void unregisterDataSetObserver(DataSetObserver observer) {
        mDataSetObservable.unregisterObserver(observer);
    }
}
