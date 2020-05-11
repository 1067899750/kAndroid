package com.github.tifezh.kchart.activity;

import android.graphics.Color;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;

import com.github.tifezh.kchart.R;
import com.github.tifezh.kchart.view.chart.MyPieChartView;

import com.github.tifezh.kchart.view.chart.PieChartData;
import com.github.tifezh.kchart.view.chart.PieChartType;
import java.util.ArrayList;
import java.util.List;


/**
  *@description 饼图
  *@author puyantao
  *@date 2020/4/25 19:25
  */
public class SuspendBtnActivity extends AppCompatActivity {
    private MyPieChartView mMyPieChartView;
    private List<PieChartData> mDataList;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_suspend_btn);
        mMyPieChartView = findViewById(R.id.pie_chart);
        mDataList = new ArrayList<>();
        mDataList.add(new PieChartData(Color.parseColor("#0000FF"), "哈哈", 1f,"人"));
        mDataList.add(new  PieChartData(Color.parseColor("#8A2BE2"), "嘻嘻", 2f,"人"));
        mDataList.add(new PieChartData(Color.parseColor("#A52A2A"), "呵呵", 3f,"人"));
        mDataList.add(new  PieChartData(Color.parseColor("#DEB887"), "啧啧", 4f,"人"));
        mDataList.add(new PieChartData(Color.parseColor("#5F9EA0"), "弟弟", 5f,"人"));
        mDataList.add(new PieChartData(Color.parseColor("#7FFF00"), "哥哥", 6f,"人"));
        mDataList.add(new PieChartData(Color.parseColor("#D2691E"), "妹妹", 7f,"人"));
        mDataList.add(new PieChartData(Color.parseColor("#0000FF"), "哈哈", 1f,"人"));
        mDataList.add(new  PieChartData(Color.parseColor("#8A2BE2"), "嘻嘻", 2f,"人"));
        mDataList.add(new PieChartData(Color.parseColor("#A52A2A"), "呵呵", 3f,"人"));
        mDataList.add(new  PieChartData(Color.parseColor("#DEB887"), "啧啧", 4f,"人"));
        mDataList.add(new PieChartData(Color.parseColor("#5F9EA0"), "弟弟", 5f,"人"));
        mDataList.add(new PieChartData(Color.parseColor("#7FFF00"), "哥哥", 6f,"人"));
        mDataList.add(new PieChartData(Color.parseColor("#D2691E"), "妹妹", 7f,"人"));
        mMyPieChartView.setData(mDataList);
        mMyPieChartView.setType(PieChartType.PERCENT);

    }


}
































