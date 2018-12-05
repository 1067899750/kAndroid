package com.github.tifezh.kchart.adapter;

import com.github.tifezh.kchart.model.RateModel;
import com.github.tifezh.kchartlib.chart.base.BaseKChartAdapter;
import java.util.ArrayList;
import java.util.Date;
import java.util.List;

public class RateAdapter extends BaseKChartAdapter {

    private List<RateModel> datas = new ArrayList<>();

    public RateAdapter() {

    }

    @Override
    public int getCount() {
        return datas.size();
    }

    @Override
    public Object getItem(int position) {
        return datas.get(position);
    }

    @Override
    public Date getDate(int position) {
        try {
            return datas.get(position).getDate();
        } catch (Exception e) {
            e.printStackTrace();
        }
        return null;
    }


    /**
     * 向头部添加数据
     */
    public void addHeaderData(List<RateModel> data) {
        if (data != null && !data.isEmpty()) {
            datas.addAll(data);
            notifyDataSetChanged();
        }
    }

    /**
     * 向尾部添加数据
     */
    public void addFooterData(List<RateModel> data) {
        if (data != null && !data.isEmpty()) {
            datas.addAll(0, data);
            notifyDataSetChanged();
        }
    }

    /**
     * 改变某个点的值
     *
     * @param position 索引值
     */
    public void changeItem(int position, RateModel data) {
        datas.set(position, data);
        notifyDataSetChanged();
    }





}
