package com.github.tifezh.kchart.activity;

import android.content.res.Configuration;
import android.os.Build;
import android.os.Bundle;
import android.support.v7.app.AppCompatActivity;
import android.util.Log;
import android.view.View;
import android.view.Window;
import android.view.WindowManager;
import android.widget.LinearLayout;
import android.widget.RelativeLayout;
import android.widget.TextView;

import com.github.tifezh.kchart.R;
import com.github.tifezh.kchart.data.DataRequest;
import com.github.tifezh.kchart.data.MinuteDataHelper;
import com.github.tifezh.kchart.model.Minute;
import com.github.tifezh.kchart.model.MinuteParent;
import com.github.tifezh.kchart.model.MinuteTime;
import com.github.tifezh.kchart.utils.ValueUtil;
import com.github.tifezh.kchart.view.TopLeftHorizontalView;
import com.github.tifezh.kchartlib.chart.minute.BaseMinuteView;
import com.github.tifezh.kchartlib.chart.minute.MinuteMainView;
import com.github.tifezh.kchartlib.chart.minute.MinuteRateView;
import com.github.tifezh.kchartlib.chart.minute.MinuteTimeView;

import java.util.ArrayList;
import java.util.Date;
import java.util.List;

import butterknife.BindView;
import butterknife.ButterKnife;
/**
 *
 * Description 分时图
 * Author puyantao
 * Email 1067899750@qq.com
 * Date 2018-10-26 17:42
 */

public class TimeMainActivity extends AppCompatActivity {

    @BindView(R.id.title_view)
    RelativeLayout titleView;
    @BindView(R.id.tv_price)
    TextView tvPrice;
    @BindView(R.id.tv_percent)
    TextView tvPercent;
    @BindView(R.id.ll_status)
    LinearLayout llStatus;
    @BindView(R.id.minuteChartView)
    MinuteTimeView minuteChartView;
    @BindView(R.id.viewLeftMessage)
    TopLeftHorizontalView viewLeftMessage;
    @BindView(R.id.lineView)
    View lineView;
    private List<MinuteTime> mMinuteTimeModels;
    private List<Minute> mMinuteDataMadels1;
    private List<Minute> mMinuteDataMadels2;
    private List<Minute> mMinuteDataMadels3;
    private List<Minute> mMinuteDataMadels4;
    private List<Minute> mMinuteDataMadels5;

    private int mScaleValue = 1;
    private Date minTime;
    private Date maxTime;
    private float preSettle;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_time_main);
        ButterKnife.bind(this);
        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.KITKAT) {
            Window window = getWindow();
            window.setFlags(
                    WindowManager.LayoutParams.FLAG_TRANSLUCENT_STATUS,
                    WindowManager.LayoutParams.FLAG_TRANSLUCENT_STATUS);
        }
        ButterKnife.bind(this);
        initView();
        initData();
    }


    private void initView() {
        if (this.getResources().getConfiguration().orientation == Configuration.ORIENTATION_LANDSCAPE) {//横屏
            llStatus.setVisibility(View.GONE);
            minuteChartView.setGridRows(6);
            minuteChartView.setGridColumns(5);
            minuteChartView.setGridChildRows(4);
            viewLeftMessage.setVisibility(View.VISIBLE);
            lineView.setVisibility(View.VISIBLE);

        } else if (this.getResources().getConfiguration().orientation == Configuration.ORIENTATION_PORTRAIT) {//竖屏
            llStatus.setVisibility(View.VISIBLE);
            minuteChartView.setGridRows(6);
            minuteChartView.setGridColumns(5);
            minuteChartView.setGridChildRows(4);
            viewLeftMessage.setVisibility(View.GONE);
            lineView.setVisibility(View.GONE);
        }
    }

    private void initData() {
        mMinuteTimeModels = new ArrayList<>();

        mMinuteDataMadels1 = new ArrayList<>();
        mMinuteDataMadels2 = new ArrayList<>();
        mMinuteDataMadels3 = new ArrayList<>();
        mMinuteDataMadels4 = new ArrayList<>();
        mMinuteDataMadels5 = new ArrayList<>();


        new Thread(new Runnable() {
            @Override
            public void run() {
                //获取随机生成的数据
                MinuteParent.DataBean minuteData = DataRequest.getMinuteData(TimeMainActivity.this);
                minTime = new Date(minuteData.getMin());
                maxTime = new Date(minuteData.getMax());
                preSettle = Float.valueOf(minuteData.getPreSettle());
//        float preSettle = Float.valueOf(minuteData.getPreClose());
                List<MinuteParent.DataBean.MinuteDatasBean> dataBeans = minuteData.getMinuteDatas();
                List<MinuteParent.DataBean.TradeRangesBean> tradeRanges = minuteData.getTradeRanges();


                for (int i = 1; i < dataBeans.size(); i++) {
                    Minute minuteDataMadel = new Minute();

                    minuteDataMadel.ruleAt = new Date(dataBeans.get(i).getRuleAt());
                    minuteDataMadel.last = dataBeans.get(i).getLast(); //成交价 最新报价 Y轴值
                    minuteDataMadel.average = dataBeans.get(i).getAverage(); //均价
                    minuteDataMadel.interest = dataBeans.get(i).getInterest();  //持仓量
                    minuteDataMadel.chgInterest = dataBeans.get(i).getChgInterest(); //持仓变化量
                    minuteDataMadel.volume = dataBeans.get(i).getVolume(); //成交量
                    minuteDataMadel.chgVolume = dataBeans.get(i).getChgVolume(); //成交量变化量
                    minuteDataMadel.settle = dataBeans.get(i).getSettle(); //结算价
                    minuteDataMadel.highest = dataBeans.get(i).getHighest(); //最高价
                    minuteDataMadel.lowest = dataBeans.get(i).getLowest(); //最低价
                    minuteDataMadel.open = dataBeans.get(i).getOpen();//开盘价
                    minuteDataMadel.close = dataBeans.get(i).getLast();//收盘价
                    minuteDataMadel.ask1p = dataBeans.get(i).getAsk1p();  //卖价
                    minuteDataMadel.ask1v = dataBeans.get(i).getAsk1v(); //卖量
                    minuteDataMadel.bid1p = dataBeans.get(i).getBid1p(); //买价
                    minuteDataMadel.bid1v = dataBeans.get(i).getBid1v(); //买量
                    minuteDataMadel.updown = dataBeans.get(i).getUpdown(); //涨跌
                    minuteDataMadel.percent = dataBeans.get(i).getPercent();//涨跌幅度
                    minuteDataMadel.preSettle = dataBeans.get(i).getPreSettle();  //买量
                    minuteDataMadel.preClose = dataBeans.get(i).getPreClose(); //涨跌
                    minuteDataMadel.preInterest = dataBeans.get(i).getPreInterest(); //涨跌幅度
                    minuteDataMadel.upLimit = dataBeans.get(i).getUpLimit(); //
                    minuteDataMadel.loLimit = dataBeans.get(i).getLoLimit();//
                    minuteDataMadel.turnover = dataBeans.get(i).getTurnover();//

                    mMinuteDataMadels1.add(minuteDataMadel);
                    MinuteDataHelper.calculateMACD(mMinuteDataMadels1);
                }

                for (int j = 1; j < mMinuteDataMadels1.size(); j++) {
                    if (j % 2 == 0) {
                        mMinuteDataMadels2.add(mMinuteDataMadels1.get(j));
                    }

                    if (j % 3 == 0) {
                        mMinuteDataMadels3.add(mMinuteDataMadels1.get(j));
                    }

                    if (j % 4 == 0) {
                        mMinuteDataMadels4.add(mMinuteDataMadels1.get(j));
                    }

                    if (j % 5 == 0) {
                        mMinuteDataMadels5.add(mMinuteDataMadels1.get(j));
                    }
                }

                for (int i = 0; i < tradeRanges.size(); i++) {
                    MinuteTime minuteTimeModel = new MinuteTime();
                    minuteTimeModel.start = new Date(tradeRanges.get(i).getStart());
                    minuteTimeModel.end = new Date(tradeRanges.get(i).getEnd());
                    mMinuteTimeModels.add(minuteTimeModel);
                }

                runOnUiThread(new Runnable() {
                    @Override
                    public void run() {
                        minuteChartView.setOpenMinute(mScaleValue);
                        minuteChartView.initData(mMinuteDataMadels1, minTime, maxTime, mMinuteTimeModels, preSettle);
                    }
                });


            }
        }).start();

        minuteChartView.setScaleEnable(true); //是否可以缩放
        minuteChartView.setGridRows(6);
        minuteChartView.setGridColumns(5);
        minuteChartView.setGridChildRows(4);

        minuteChartView.setViewScaleGestureListener(new BaseMinuteView.OnScaleGestureListener() {
            @Override
            public void setLoseNumber() {
                Log.d("---> :", "缩小");
                mScaleValue++;
                if (mScaleValue <= 5 && mScaleValue >= 1) {
                    Log.d("---> mScaleValue :", mScaleValue + "");
                    minuteChartView.setOpenMinute(mScaleValue);
                    if (mScaleValue == 1) {
                        minuteChartView.initData(mMinuteDataMadels1, minTime, maxTime, mMinuteTimeModels, preSettle);

                    } else if (mScaleValue == 2) {
                        minuteChartView.initData(mMinuteDataMadels2, minTime, maxTime, mMinuteTimeModels, preSettle);

                    } else if (mScaleValue == 3) {
                        minuteChartView.initData(mMinuteDataMadels3, minTime, maxTime, mMinuteTimeModels, preSettle);

                    } else if (mScaleValue == 4) {
                        minuteChartView.initData(mMinuteDataMadels4, minTime, maxTime, mMinuteTimeModels, preSettle);

                    } else if (mScaleValue == 5) {
                        minuteChartView.initData(mMinuteDataMadels5, minTime, maxTime, mMinuteTimeModels, preSettle);
                    }


                } else {
                    mScaleValue = 5;
                }
            }

            @Override
            public void setAddNumber() {
                Log.d("---> :", "放大");
                mScaleValue--;
                if (mScaleValue <= 5 && mScaleValue >= 1) {
                    Log.d("---> mScaleValue :", mScaleValue + "");
                    minuteChartView.setOpenMinute(mScaleValue);
                    if (mScaleValue == 1) {
                        minuteChartView.initData(mMinuteDataMadels1, minTime, maxTime, mMinuteTimeModels, preSettle);

                    } else if (mScaleValue == 2) {
                        minuteChartView.initData(mMinuteDataMadels2, minTime, maxTime, mMinuteTimeModels, preSettle);

                    } else if (mScaleValue == 3) {
                        minuteChartView.initData(mMinuteDataMadels3, minTime, maxTime, mMinuteTimeModels, preSettle);

                    } else if (mScaleValue == 4) {
                        minuteChartView.initData(mMinuteDataMadels4, minTime, maxTime, mMinuteTimeModels, preSettle);

                    } else if (mScaleValue == 5) {
                        minuteChartView.initData(mMinuteDataMadels5, minTime, maxTime, mMinuteTimeModels, preSettle);
                    }
                } else {
                    mScaleValue = 1;
                }

            }
        });


    }

    @Override
    public void onConfigurationChanged(Configuration newConfig) {
        super.onConfigurationChanged(newConfig);
        if (this.getResources().getConfiguration().orientation == Configuration.ORIENTATION_LANDSCAPE) {//横屏
            llStatus.setVisibility(View.GONE);
            minuteChartView.setGridRows(6);
            minuteChartView.setGridColumns(5);
            minuteChartView.setGridChildRows(4);
            viewLeftMessage.setVisibility(View.VISIBLE);
            lineView.setVisibility(View.VISIBLE);

        } else if (this.getResources().getConfiguration().orientation == Configuration.ORIENTATION_PORTRAIT) {//竖屏
            llStatus.setVisibility(View.VISIBLE);
            minuteChartView.setGridRows(6);
            minuteChartView.setGridColumns(5);
            minuteChartView.setGridChildRows(4);;
            viewLeftMessage.setVisibility(View.GONE);
            lineView.setVisibility(View.GONE);
        }
    }

    @Override
    protected void onDestroy() {
        super.onDestroy();
        if (minuteChartView != null){
            minuteChartView.releaseMemory();
        }
    }

}





























