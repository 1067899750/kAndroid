package com.github.tifezh.kchart.activity;

import android.os.Bundle;
import android.support.v7.app.AppCompatActivity;
import android.util.Log;

import com.github.tifezh.kchart.R;
import com.github.tifezh.kchart.data.DataRequest;
import com.github.tifezh.kchart.model.Lem;
import com.github.tifezh.kchart.utils.ValueUtil;
import com.github.tifezh.kchartlib.chart.comInterface.ILem;
import com.github.tifezh.kchartlib.chart.lem.LmeView;

import java.util.ArrayList;
import java.util.List;

import butterknife.BindView;
import butterknife.ButterKnife;
/**
 *
 * Description
 * Author puyantao
 * Email 1067899750@qq.com
 * Date 2018-10-26 17:42
 */

public class LmeActivity extends AppCompatActivity {

    @BindView(R.id.lme_view)
    LmeView lmeView;

    List<Lem> mLemModels;
    List<Lem> mLemData;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_lme);
        ButterKnife.bind(this);

        mLemData = new ArrayList<>();

        mLemModels = DataRequest.getLmeData(this);
        mLemData.addAll(mLemModels.subList(0, 20));
        Log.d("-->", mLemModels.size() + "");

        lmeView.setDateStr("月差价");
        lmeView.setSelectedIndex(0);
        lmeView.initData(mLemData);
        lmeView.setOnClickPointListener(new LmeView.ClickLmePointListener() {
            @Override
            public void onClickPointListener(int postion, ILem iLem) {
                if (ValueUtil.isEmpty(iLem) || postion == -1) {
                }
            }
        });


    }






}
















