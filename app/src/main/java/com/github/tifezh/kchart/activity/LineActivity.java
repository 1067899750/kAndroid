package com.github.tifezh.kchart.activity;

import android.os.Bundle;
import android.support.v7.app.AppCompatActivity;

import com.github.tifezh.kchart.R;
import com.github.tifezh.kchart.data.DataRequest;
import com.github.tifezh.kchart.model.LineModel;
import com.github.tifezh.kchart.model.TrendChartModel;
import com.github.tifezh.kchartlib.chart.line.LineRateView;

import java.util.ArrayList;
import java.util.Date;
import java.util.List;

import butterknife.BindView;
import butterknife.ButterKnife;

public class LineActivity extends AppCompatActivity {
    @BindView(R.id.my_rate_view)
    LineRateView myRateView;
    private TrendChartModel mTrendChartModel;
    private List<LineModel> mRateModels;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_line);
        ButterKnife.bind(this);
        mRateModels = new ArrayList<>();

        mTrendChartModel = DataRequest.getRateData(this);
        myRateView.setScrollEnable(true); //是否滑动
        myRateView.setGridRows(8);//横线
        myRateView.setGridColumns(6);//竖线

        initData();


    }

    public void initData() {
        List<TrendChartModel.DataBean> dataBeans = mTrendChartModel.getData();
        for (int i = 0; i < dataBeans.size(); i++) {
            LineModel lineModel = new LineModel();
            lineModel.date = new Date(dataBeans.get(i).getDate());
            lineModel.value = Float.valueOf(dataBeans.get(i).getValue());
            lineModel.change = dataBeans.get(i).getChange();
            lineModel.percent = dataBeans.get(i).getPercent();
            mRateModels.add(lineModel);

        }

//        Collections.reverse(mRateModels);//对histories 集合中的数据进行倒叙排序
        myRateView.initData(mRateModels);


    }

    @Override
    protected void onDestroy() {
        super.onDestroy();
        if (myRateView != null) {
            myRateView.releaseMemory();
        }
    }


}
