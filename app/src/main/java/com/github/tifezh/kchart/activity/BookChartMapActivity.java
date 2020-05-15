package com.github.tifezh.kchart.activity;

import android.graphics.Color;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;

import com.github.tifezh.kchart.R;
import com.github.tifezh.kchartlib.chart.pic.BookAssetsPieChartView;
import com.github.tifezh.kchartlib.chart.pic.BookPieChartData;
import com.github.tifezh.kchartlib.chart.pic.BookPieChartType;
import com.github.tifezh.kchartlib.chart.pic.BookPieChartView;

import java.util.ArrayList;
import java.util.List;
/**
  *@description
  *@author puyantao
  *@date 2020/4/26 19:37
  */
public class BookChartMapActivity extends AppCompatActivity {
    private BookAssetsPieChartView mMyPieChartView;
    private BookPieChartView mBookPieChartView;
    private List<BookPieChartData> mDataList;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_book_chart_map);
        mMyPieChartView = findViewById(R.id.pie_chart);
        mBookPieChartView = findViewById(R.id.pie_chart2);

        mDataList = new ArrayList<>();
        mDataList.add(new BookPieChartData(Color.parseColor("#7FFF00"), "哥哥", 585f, "人"));
        mDataList.add(new BookPieChartData(Color.parseColor("#DEB887"), "啧啧", 999f, "人"));
        mDataList.add(new BookPieChartData(Color.parseColor("#5F9EA0"), "弟弟", 666f, "人"));
        mDataList.add(new BookPieChartData(Color.parseColor("#A52A2A"), "呵呵", 1764f, "人"));
        mDataList.add(new BookPieChartData(Color.parseColor("#D2691E"), "妹妹", 38f, "人"));
        mDataList.add(new BookPieChartData(Color.parseColor("#C78479"), "哈哈", 39f, "人"));
        mDataList.add(new BookPieChartData(Color.parseColor("#0000FF"), "哈哈", 5718f, "人"));
        mDataList.add(new BookPieChartData(Color.parseColor("#8A2BE2"), "嘻嘻", 4733.5f, "人"));
        mMyPieChartView.setDataList(mDataList);

        mBookPieChartView.setType(BookPieChartType.CONTENT_PERCENT);
        mBookPieChartView.setDataList(mDataList);
    }
}










