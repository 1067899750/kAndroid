package com.github.tifezh.kchart.activity;

import android.os.Bundle;
import android.support.v7.app.AppCompatActivity;
import android.widget.TextView;

import com.github.tifezh.kchart.R;
import com.github.tifezh.kchart.model.DetailModel;
import com.github.tifezh.kchartlib.chart.detail.DetailView;

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

    private ArrayList<DetailModel> mDetailModels;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_rldetail);
        ButterKnife.bind(this);
        mDetailModels = new ArrayList<>();
        detailView.setGridRows(1);
        detailView.setGridColumns(1);

        initDatas();

        detailView.initData(mDetailModels);

    }

    private void initDatas() {
        for (int i = 0; i < 30; i++) {
            DetailModel model = new DetailModel();
            model.data = i;
            model.value = (float) (1 + Math.random() * 300);
            mDetailModels.add(model);
        }
    }


}




















