package com.github.tifezh.kchart.activity;

import android.os.Bundle;
import android.support.v7.app.AppCompatActivity;

import com.github.tifezh.kchart.R;
import com.github.tifezh.kchart.adapter.RateAdapter;
import com.github.tifezh.kchart.data.DataRequest;
import com.github.tifezh.kchart.model.RateModel;
import com.github.tifezh.kchart.model.TrendChartModel;
import com.github.tifezh.kchartlib.chart.rate.RateView;

import java.util.ArrayList;
import java.util.Collections;
import java.util.Date;
import java.util.List;

import butterknife.BindView;
import butterknife.ButterKnife;

/**
 * Description
 * Author puyantao
 * Email 1067899750@qq.com
 * Date 2018-12-4 10:37
 */

public class RateActivity extends AppCompatActivity {
    @BindView(R.id.my_rate_view)
    RateView myRateView;

    private TrendChartModel mTrendChartModel;
    private RateAdapter mRateAdapter;
    private List<RateModel> mRateModels;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_rate);
        ButterKnife.bind(this);
        mRateModels = new ArrayList<>();

        mTrendChartModel = DataRequest.getRateData(this);
        List<TrendChartModel.DataBean> dataBeans = mTrendChartModel.getData();
        for (int i =0; i < dataBeans.size(); i ++){
            RateModel rateModel = new RateModel();
            rateModel.date = new Date(dataBeans.get(i).getDate());
            rateModel.value = Float.valueOf(dataBeans.get(i).getValue());
            rateModel.change = dataBeans.get(i).getChange();
            mRateModels.add(rateModel);

        }

        Collections.reverse(mRateModels);//对histories 集合中的数据进行倒叙排序

        mRateAdapter = new RateAdapter();
        mRateAdapter.addFooterData(mRateModels);
        myRateView.setAdapter(mRateAdapter);
        myRateView.setScrollEnable(true); //是否滑动
        myRateView.setGridRows(7);//横线
        myRateView.setGridColumns(5);//竖线


    }


}


















