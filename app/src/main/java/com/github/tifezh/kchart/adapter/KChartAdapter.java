package com.github.tifezh.kchart.adapter;

import com.github.tifezh.kchart.model.KLine;
import com.github.tifezh.kchartlib.chart.base.BaseKChartAdapter;
import com.github.tifezh.kchartlib.utils.DateUtil;

import java.util.ArrayList;
import java.util.Date;
import java.util.List;


public class KChartAdapter extends BaseKChartAdapter {

    private List<KLine> datas = new ArrayList<>();

    public KChartAdapter() {

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
            String s = datas.get(position).getDatetime();
            return DateUtil.getDateByByStringDate(s);
        } catch (Exception e) {
            e.printStackTrace();
        }
        return null;
    }

    /**
     * 向头部添加数据
     */
    public void addHeaderData(List<KLine> data) {
        if (data != null && !data.isEmpty()) {
            datas.addAll(data);
            notifyDataSetChanged();
        }
    }

    /**
     * 向尾部添加数据
     */
    public void addFooterData(List<KLine> data) {
        if (data != null && !data.isEmpty()) {
            datas.addAll(0, data);
            notifyDataSetChanged();
        }
    }

    /**
     * 改变某个点的值
     * @param position 索引值
     */
    public void changeItem(int position,KLine data)
    {
        datas.set(position,data);
        notifyDataSetChanged();
    }

}
