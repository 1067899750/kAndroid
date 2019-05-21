package com.github.tifezh.kchart.activity;

import android.os.Bundle;
import android.support.v7.app.AppCompatActivity;
import android.widget.TextView;

import com.github.tifezh.kchart.R;
import com.github.tifezh.kchart.model.DetailModel;
import com.github.tifezh.kchartlib.chart.detail.DetailView;
import com.github.tifezh.kchartlib.chart.detail.HistogramModel;
import com.github.tifezh.kchartlib.chart.detail.HistogramView;

import java.util.ArrayList;

import butterknife.BindView;
import butterknife.ButterKnife;

/**
 * Description 明细Activity
 * Author puyantao
 * Email 1067899750@qq.com
 * Date 2019/5/20 11:09
 */
public class RLDetailActivity extends AppCompatActivity {

    @BindView(R.id.all_input_tv)
    TextView allInputTv;
    @BindView(R.id.average_input_tv)
    TextView averageInputTv;
    @BindView(R.id.detail_view)
    DetailView detailView;
    @BindView(R.id.histogrm_view)
    HistogramView histogrmView;

    private ArrayList<DetailModel> mDetailModels;
    private ArrayList<HistogramModel> mHistogramModels;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_rldetail);
        ButterKnife.bind(this);
        mDetailModels = new ArrayList<>();
        mHistogramModels = new ArrayList<>();

        detailView.setGridRows(1);
        detailView.setGridColumns(1);

        initDatas();

        detailView.initData(mDetailModels);
        histogrmView.initDatas(mHistogramModels);

    }

    private void initDatas() {
        for (int i = 0; i < 30; i++) {
            DetailModel model = new DetailModel();
            model.data = i;
            model.value = (float) (1 + Math.random() * 300);
            mDetailModels.add(model);
        }


        HistogramModel histogramModel1 = new HistogramModel();
        histogramModel1.setName("工资");
        histogramModel1.setMoney(1000);

        HistogramModel histogramModel2 = new HistogramModel();
        histogramModel2.setName("分红");
        histogramModel2.setMoney(500);

        HistogramModel histogramModel3 = new HistogramModel();
        histogramModel3.setName("补助");
        histogramModel3.setMoney(600);


        HistogramModel histogramModel4 = new HistogramModel();
        histogramModel4.setName("其他");
        histogramModel4.setMoney(500);

        mHistogramModels.add(histogramModel1);
        mHistogramModels.add(histogramModel2);
        mHistogramModels.add(histogramModel3);
        mHistogramModels.add(histogramModel4);

     }


}




















