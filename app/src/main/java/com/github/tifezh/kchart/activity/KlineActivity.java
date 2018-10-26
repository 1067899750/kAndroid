package com.github.tifezh.kchart.activity;

import android.content.res.Configuration;
import android.os.Bundle;
import android.support.v4.content.ContextCompat;
import android.support.v7.app.AppCompatActivity;
import android.util.Log;
import android.view.View;
import android.widget.Button;
import android.widget.LinearLayout;
import android.widget.TextView;

import com.github.tifezh.kchart.R;
import com.github.tifezh.kchart.adapter.KChartAdapter;
import com.github.tifezh.kchart.data.DataRequest;
import com.github.tifezh.kchart.model.KLine;
import com.github.tifezh.kchart.view.TopLeftHorizontalView;
import com.github.tifezh.kchartlib.chart.view.BaseKChartView;
import com.github.tifezh.kchartlib.chart.view.KChartView;
import com.github.tifezh.kchartlib.chart.formatter.ShortDateFormatter;
import com.github.tifezh.kchartlib.chart.formatter.TimeFormatter;
import com.github.tifezh.kchartlib.utils.DensityUtil;

import java.util.List;

import butterknife.BindView;
import butterknife.ButterKnife;

/**
 * Description：K线图
 * Author: star
 * Email: guimingxing@163.com
 * Date: 2018-10-23 11:08
 */

public class KlineActivity extends AppCompatActivity implements KChartView.KChartRefreshListener,View.OnClickListener {
    @BindView(R.id.tv_price)
    TextView mTvPrice;
    @BindView(R.id.tv_percent)
    TextView mTvPercent;
    @BindView(R.id.ll_status)
    LinearLayout mLlStatus;
    @BindView(R.id.kchart_view)
    KChartView mKChartView;
    @BindView(R.id.viewLeftMessage)
    TopLeftHorizontalView viewLeftMessage;
    @BindView(R.id.btnDay)
    Button btnDay;
    @BindView(R.id.btn1Min)
    Button btn1Min;
    @BindView(R.id.btn3Min)
    Button btn3Min;
    @BindView(R.id.btn5Min)
    Button btn5Min;
    @BindView(R.id.lineView)
    View lineView;
    private KChartAdapter mAdapter;
    private int kType = 0;//默认显示日K
    private int width;
    private int dataNum=240;//一页显示多少条数据

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_example_light);
        ButterKnife.bind(this);
        width = getWindowManager().getDefaultDisplay().getWidth();
        setToCanvas(false);
        initView();

    }

    private void initView() {
        if (this.getResources().getConfiguration().orientation == Configuration.ORIENTATION_LANDSCAPE) {//横屏
            mLlStatus.setVisibility(View.GONE);
            mKChartView.setGridRows(3);
            mKChartView.setGridColumns(8);
            viewLeftMessage.setVisibility(View.VISIBLE);
        } else if (this.getResources().getConfiguration().orientation == Configuration.ORIENTATION_PORTRAIT) {//竖屏
            mLlStatus.setVisibility(View.VISIBLE);
            mKChartView.setGridRows(4);
            mKChartView.setGridColumns(4);
            viewLeftMessage.setVisibility(View.GONE);
        }

        mAdapter = new KChartAdapter();
        mKChartView.setAdapter(mAdapter);
        mKChartView.setDateTimeFormatter(kType == 0 ? new ShortDateFormatter() : new TimeFormatter());//09/11 日K格式  09:11 分格式
        mKChartView.setGridRows(6);//横线
        mKChartView.setGridColumns(5);//竖线
        mKChartView.setGridLineWidth(3);
        mKChartView.setLongPress(false);
        mKChartView.setClosePress(true);
        mKChartView.setSelectedLineWidth(1);//长按选中线宽度

        mKChartView.setSelectorBackgroundColor(ContextCompat.getColor(this, R.color.c4F5490));//选择器背景色
        mKChartView.setSelectorTextColor(ContextCompat.getColor(this, R.color.cE7EDF5));//选择器文字颜色


        mKChartView.setBackgroundColor(ContextCompat.getColor(this, R.color.c2A2D4F));//背景
        mKChartView.setGridLineColor(ContextCompat.getColor(this, R.color.cFF333556));//表格线颜色
        mKChartView.setCandleSolid(true);//蜡柱是否实心

        mKChartView.setOnSelectedChangedListener(new BaseKChartView.OnSelectedChangedListener() {
            @Override
            public void onSelectedChanged(BaseKChartView view, Object point, int index) {
                KLine data = (KLine) point;
                Log.i("onSelectedChanged", "index:" + index + " closePrice:" + data.getClosePrice());
            }
        });

        mKChartView.showLoading();
        mKChartView.setRefreshListener(this);
        onLoadMoreBegin(mKChartView);
        btnDay.setOnClickListener(this);
        btn1Min.setOnClickListener(this);
        btn3Min.setOnClickListener(this);
        btn5Min.setOnClickListener(this);
    }


    @Override
    public void onConfigurationChanged(Configuration newConfig) {
        super.onConfigurationChanged(newConfig);
        if (this.getResources().getConfiguration().orientation == Configuration.ORIENTATION_LANDSCAPE) {//横屏
            mLlStatus.setVisibility(View.GONE);
            mKChartView.setGridRows(3);
            mKChartView.setGridColumns(8);
            lineView.setVisibility(View.VISIBLE);
            viewLeftMessage.setVisibility(View.VISIBLE);

            setToCanvas(true);
        } else if (this.getResources().getConfiguration().orientation == Configuration.ORIENTATION_PORTRAIT) {//竖屏
            mLlStatus.setVisibility(View.VISIBLE);
            mKChartView.setGridRows(4);
            mKChartView.setGridColumns(4);
            lineView.setVisibility(View.GONE);
            viewLeftMessage.setVisibility(View.GONE);

            setToCanvas(false);
        }
    }

    @Override
    public void onLoadMoreBegin(KChartView chart) {
        fillData(kType);
    }


    @Override
    public void onClick(View v) {
        switch (v.getId()) {
            case R.id.btnDay:
                kType = 0;
                initView();
                break;
            case R.id.btn1Min:
                kType = 1;
                initView();
                break;
            case R.id.btn3Min:
                kType = 3;
                initView();
                break;
            case R.id.btn5Min:
                kType = 5;
                initView();
                break;
        }
    }

    /**
     * 填充K线数据
     *
     * @param type
     */
    private void fillData(int type) {
        new Thread(new Runnable() {
            @Override
            public void run() {
                List<KLine> data = DataRequest.getData(KlineActivity.this, mAdapter.getCount(), dataNum, type);

                if (!data.isEmpty()) {
                    Log.i("onLoadMoreBegin", "start:" + data.get(0).getDatetime() + " stop:" + data.get(data.size() - 1).getDatetime());
                }
                runOnUiThread(new Runnable() {
                    @Override
                    public void run() {
                        //第一次加载时开始动画
                        if (mAdapter.getCount() == 0) {
                            mKChartView.startAnimation();
                        }
                        mAdapter.addFooterData(data);
                        //加载完成，还有更多数据
                        if (data.size() > 0) {
                            mKChartView.refreshComplete();
                        }
                        //加载完成，没有更多数据
                        else {
                            mKChartView.refreshEnd();
                        }
                    }
                });
            }
        }).start();
    }

    //对弹框字体大小根据横竖屏来设置 横屏位true 否则false
    public void setToCanvas(boolean ishv) {
        if (ishv) {
            if (width <= 480) {
                mKChartView.setSelectorTextSize(DensityUtil.dp2px( 8));//选择器文字大小
            } else {
                mKChartView.setSelectorTextSize(DensityUtil.dp2px(10));//选择器文字大小
            }
        } else {
            if (width <= 480) {
                mKChartView.setSelectorTextSize(DensityUtil.dp2px( 10));//选择器文字大小
            } else {
                mKChartView.setSelectorTextSize(DensityUtil.dp2px( 12));//选择器文字大小
            }
        }

    }
}
