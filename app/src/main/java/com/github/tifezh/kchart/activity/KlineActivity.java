package com.github.tifezh.kchart.activity;

import android.app.Activity;
import android.content.Context;
import android.content.res.Configuration;
import android.graphics.Bitmap;
import android.os.Bundle;
import android.support.annotation.Nullable;
import android.support.v4.content.ContextCompat;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.GridLayoutManager;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.util.Log;
import android.view.Gravity;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.PopupWindow;
import android.widget.RelativeLayout;
import android.widget.TextView;

import com.github.tifezh.kchart.R;
import com.github.tifezh.kchart.adapter.KChartAdapter;
import com.github.tifezh.kchart.adapter.RvChoosemhAdapter;
import com.github.tifezh.kchart.data.DataRequest;
import com.github.tifezh.kchart.data.MinuteDataHelper;
import com.github.tifezh.kchart.model.KLine;
import com.github.tifezh.kchart.model.Minute;
import com.github.tifezh.kchart.model.MinuteParent;
import com.github.tifezh.kchart.model.MinuteTime;
import com.github.tifezh.kchart.model.RvChoosemh;
import com.github.tifezh.kchart.utils.Constant;
import com.github.tifezh.kchart.utils.PictureMergeManager;
import com.github.tifezh.kchart.utils.SharedUtil;
import com.github.tifezh.kchart.utils.ValueUtil;
import com.github.tifezh.kchart.view.ExplainDialog;
import com.github.tifezh.kchart.view.InvOrVolumeaPopwindow;
import com.github.tifezh.kchart.view.RvChoosemhPopuWindow;
import com.github.tifezh.kchart.view.ShareDialog;
import com.github.tifezh.kchart.view.TopLeftHorizontalView;
import com.github.tifezh.kchartlib.chart.formatter.ShortDateFormatter;
import com.github.tifezh.kchartlib.chart.formatter.TimeFormatter;
import com.github.tifezh.kchartlib.chart.minute.BaseMinuteView;
import com.github.tifezh.kchartlib.chart.minute.MinuteTimeView;
import com.github.tifezh.kchartlib.chart.view.KChartView;
import com.github.tifezh.kchartlib.utils.DensityUtil;


import java.util.ArrayList;
import java.util.Date;
import java.util.List;

import butterknife.BindView;
import butterknife.ButterKnife;
import butterknife.OnClick;
import butterknife.Unbinder;

/**
 *
 * Description K线图
 * Author puyantao
 * Email 1067899750@qq.com
 * Date 2018-11-30 10:27
 */


public class KlineActivity extends AppCompatActivity implements KChartView.KChartRefreshListener {
    @BindView(R.id.tvName)
    TextView tvName;
    @BindView(R.id.rvChoosemh)
    RecyclerView rvChoosemh;
    @BindView(R.id.rlLayout)
    RelativeLayout rlLayout;
    @BindView(R.id.tvValue)
    TextView tvValue;
    @BindView(R.id.tvValueRose)
    TextView tvValueRose;
    @BindView(R.id.tvLme)
    TextView tvLme;
    @BindView(R.id.tvImportsProfit)
    TextView tvImportsProfit;
    @BindView(R.id.ivDownOrUp)
    ImageView ivDownOrUp;
    @BindView(R.id.tvSell)
    TextView tvSell;
    @BindView(R.id.tvSellNum)
    TextView tvSellNum;
    @BindView(R.id.tvBuy)
    TextView tvBuy;
    @BindView(R.id.tvBuyNum)
    TextView tvBuyNum;
    @BindView(R.id.llRoseSellOrBuy)
    LinearLayout llRoseSellOrBuy;
    @BindView(R.id.llPlus)
    LinearLayout llPlus;
    @BindView(R.id.llSpecs)
    LinearLayout llSpecs;
    @BindView(R.id.llWarn)
    LinearLayout llWarn;
    @BindView(R.id.llShare)
    LinearLayout llShare;

    @BindView(R.id.viewKLeftMessage)
    TopLeftHorizontalView viewKLeftMessage;
    @BindView(R.id.kchart_view)
    KChartView kchartView;
    @BindView(R.id.viewMinuteLeftMessage)
    TopLeftHorizontalView viewMinuteLeftMessage;
    @BindView(R.id.minuteChartView)
    MinuteTimeView minuteChartView;
    @BindView(R.id.llMinuteView)
    View llMinuteView;//分时布局
    @BindView(R.id.llkChartView)
    View llKView;//k 线布局
    @BindView(R.id.lineKView)
    View lineKView;
    @BindView(R.id.lineMinuteView)
    View lineMinuteView;
    @BindView(R.id.tvBuyTitle)
    TextView tvBuyTitle;
    @BindView(R.id.llSellBuy)
    LinearLayout llSellBuy;
    @BindView(R.id.llDownOrUp)
    LinearLayout llDownOrUp;

    private Unbinder mUnbinder;
    private Context mContext;

    private RvChoosemhAdapter rvChoosemhAdapter;
    private List<RvChoosemh> rvList = new ArrayList<>();
    private String name[] = {"分时", "日K", "1m", "3m", "5m", "15m", "更多"};
    private RvChoosemhPopuWindow rvChoosemhPopuWindow = null;
    private InvOrVolumeaPopwindow invOrVolumeaPopwindow = null;
    private boolean isOrientation;//代表方向
    private int width;
    private ExplainDialog explainDialog = null;

    //分时图
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

    //K线图
    private KChartAdapter mKAdapter;
    private int kType = 0;//默认显示日K
    private int dataNum = 240;//一页显示多少条数据

    private String title = "沪铜1801";

    @Override
    protected void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_kline);
        mUnbinder = ButterKnife.bind(this);
        mContext = KlineActivity.this;

        width = getWindowManager().getDefaultDisplay().getWidth();
        rvChoosemhPopuWindow = new RvChoosemhPopuWindow(this);
        //针对最后一个设置展开关闭回复初始
        rvChoosemhPopuWindow.setOnDismissListener(new PopupWindow.OnDismissListener() {
            @Override
            public void onDismiss() {
                rvList.get(6).setMoreOpon(false);
                rvChoosemhAdapter.setData(rvList);
            }
        });
        //持仓量与成交量的弹框
        invOrVolumeaPopwindow = new InvOrVolumeaPopwindow(this);
        invOrVolumeaPopwindow.setOnDismissListener(new PopupWindow.OnDismissListener() {
            @Override
            public void onDismiss() {
                ivDownOrUp.setImageResource(R.mipmap.iv_chart_details_nor);
            }
        });
        setRvChoosemh();
        setShowDialog();
        showMinuteView(); //默认显示分时图
        setToCanvas(false);
    }

    @OnClick({R.id.llDownOrUp, R.id.tvShare, R.id.llPlus, R.id.llSpecs, R.id.llWarn, R.id.llShare})
    public void onViewClicked(View view) {
        switch (view.getId()) {
            case R.id.llDownOrUp:
                ivDownOrUp.setImageResource(R.mipmap.iv_chart_details_res);
                invOrVolumeaPopwindow.setShow(llRoseSellOrBuy, width);
                break;
            case R.id.llPlus:
                break;
            case R.id.llSpecs:
                explainDialog.show();

                break;
            case R.id.llWarn:
                break;
            case R.id.llShare:
                boolean isShow = SharedUtil.getBoolean(Constant.BALL_SHOW);

                try {
                    Bitmap bitmap = getMargerBitmap();
//                    image.setImageBitmap(getMargerBitmap());
                    setShareDialog(bitmap);
                } catch (Exception e) {
                    e.printStackTrace();
                }

                break;
        }
    }


    @Override
    protected void onDestroy() {
        super.onDestroy();
        mUnbinder.unbind();
        if (kchartView != null){
            kchartView.releaseMemory();
        }
        if (minuteChartView != null){
            minuteChartView.releaseMemory();
        }
    }


    /**
     * 显示分时图
     */
    private void showMinuteView() {
        llMinuteView.setVisibility(View.VISIBLE);
        llKView.setVisibility(View.GONE);
        initMinuteData();
        initMinuteView();
    }

    /**
     * 显示K线
     */
    private void showKview() {
        initKView();
        llMinuteView.setVisibility(View.GONE);
        llKView.setVisibility(View.VISIBLE);
    }


    @Override
    public void onConfigurationChanged(Configuration newConfig) {
        super.onConfigurationChanged(newConfig);
        width = getWindowManager().getDefaultDisplay().getWidth();
        if (explainDialog != null && explainDialog.isShowing()) {
            explainDialog.dismiss();
        }
        if (rvChoosemhPopuWindow != null && rvChoosemhPopuWindow.isShowing()) {
            rvChoosemhPopuWindow.dismiss();
        }
        if (invOrVolumeaPopwindow != null && invOrVolumeaPopwindow.isShowing()) {
            invOrVolumeaPopwindow.dismiss();
        }
        if (getResources().getConfiguration().orientation == Configuration.ORIENTATION_LANDSCAPE) {//横屏
            isOrientation = true;
            setRvChoosemhWidth(true);
        } else if (getResources().getConfiguration().orientation == Configuration.ORIENTATION_PORTRAIT) {//竖屏
            isOrientation = false;
            setRvChoosemhWidth(false);
        }
    }

    //计算设置横竖屏recycleview的宽度
    private void setRvChoosemhWidth(boolean isOrientation) {
        ViewGroup.LayoutParams layoutParams = rvChoosemh.getLayoutParams();
        if (isOrientation) {
            layoutParams.width = (width / 2);
            tvName.setVisibility(View.VISIBLE);
            llSellBuy.setVisibility(View.GONE);
            tvName.setText("ssss");
            //分时图
            minuteChartView.setGridRows(6);
            minuteChartView.setGridColumns(5);
            minuteChartView.setGridChildRows(4);
            minuteChartView.setVolumeHeight(80);
            minuteChartView.setVolumeTextHeight(20);
            viewMinuteLeftMessage.setVisibility(View.VISIBLE);
            lineMinuteView.setVisibility(View.VISIBLE);

            //k 线图
            kchartView.setGridRows(3);
            kchartView.setGridColumns(8);
            viewKLeftMessage.setVisibility(View.VISIBLE);
            lineKView.setVisibility(View.VISIBLE);
            setToCanvas(true);
        } else {
            layoutParams.width = width;
            llSellBuy.setVisibility(View.VISIBLE);
            tvName.setVisibility(View.GONE);

            //分时图
            minuteChartView.setGridRows(6);
            minuteChartView.setGridColumns(5);
            minuteChartView.setGridChildRows(4);
            minuteChartView.setVolumeHeight(100);
            minuteChartView.setVolumeTextHeight(20);
            viewMinuteLeftMessage.setVisibility(View.GONE);
            lineMinuteView.setVisibility(View.GONE);
            //K 线图
            kchartView.setGridRows(4);
            kchartView.setGridColumns(4);
            viewKLeftMessage.setVisibility(View.GONE);
            lineKView.setVisibility(View.GONE);
            setToCanvas(false);

        }
        rvChoosemh.setLayoutParams(layoutParams);
    }


    //初始化配置
    private void setRvChoosemh() {
        GridLayoutManager gridLayoutManager = new GridLayoutManager(this, 7);
        gridLayoutManager.setOrientation(LinearLayoutManager.VERTICAL);
        rvChoosemh.setLayoutManager(gridLayoutManager);
        for (int i = 0; i < name.length; i++) {
            RvChoosemh rvChoosemh = new RvChoosemh();
            if (0 == i) {
                rvChoosemh.setChoose(true);
            } else {
                rvChoosemh.setChoose(false);
            }
            rvChoosemh.setValue(name[i]);
            rvList.add(rvChoosemh);
        }
        rvChoosemhAdapter = new RvChoosemhAdapter(this, false);
        rvChoosemhAdapter.setData(rvList);
        rvChoosemhAdapter.setMyItemLister(new RvChoosemhAdapter.MyItemLister() {
            @Override
            public void setItem(View v, List<RvChoosemh> data, int position) {
                for (int i = 0; i < rvList.size(); i++) {
                    rvList.get(i).setChoose(false);
                }
                rvList.get(position).setChoose(true);
                if (position == 6) {
                    if (rvList.get(position).isMoreOpon()) {
                        rvList.get(position).setMoreOpon(false);
                    } else {
                        rvList.get(position).setMoreOpon(true);
                        if (isOrientation) {
                            rvChoosemhPopuWindow.setShow(rvChoosemh, width / 2);
                        } else {
                            rvChoosemhPopuWindow.setShow(rvChoosemh, width);
                        }

                        //选中刷新最后一个数据的值
                        rvChoosemhPopuWindow.setMyClick(new RvChoosemhPopuWindow.OnClickListener() {
                            @Override
                            public void onClick(View view, String name) {
                                rvList.get(6).setValue(name);
                                rvChoosemhAdapter.setData(rvList);
                            }
                        });
                    }
                } else {
                    rvList.get(6).setMoreOpon(false);
                    rvList.get(6).setValue(name[6]);
                }
                rvChoosemhAdapter.setData(rvList);
                switch (position) {
                    case 0:
                        showMinuteView();
                        break;
                    case 1:
                        kType = 0;
                        showKview();
                        break;
                    case 2:
                        kType = 1;
                        showKview();
                        break;
                    case 3:
                        kType = 3;
                        showKview();
                        break;
                    case 4:
                        kType = 5;
                        showKview();
                        break;
                }
            }
        });
        rvChoosemh.setAdapter(rvChoosemhAdapter);
    }

    //说明
    private void setShowDialog() {
        explainDialog = new ExplainDialog(this, R.style.Theme_dialog);
        explainDialog.setCancelable(true);
        explainDialog.getWindow().setWindowAnimations(R.style.dialogWindowAnim);
        explainDialog.getWindow().setGravity(Gravity.CENTER);
//        explainDialog.show();
    }

    //得到所有的bitmap 合并一张图片
    private Bitmap getMargerBitmap() {
        Bitmap bitmap = null;
        try {
            //顶部画取文字的bitmap
            Bitmap top = PictureMergeManager.getPictureMergeManager().getCanvasBitmap(this, "sss", 50);
            //得到行情的bitmap
            Bitmap bottom = PictureMergeManager.getPictureMergeManager().getScreenBitmap(this, rlLayout);
            //合并画取的与行情的bitmap
            Bitmap marger = PictureMergeManager.getPictureMergeManager().mergeBitmap_TB(top, bottom, true);
            //添加二维码
            bitmap = PictureMergeManager.getPictureMergeManager().getBitmap(this, marger, true);
        } catch (Exception e) {
            e.printStackTrace();
        }
        return bitmap;
    }

    //分享
    private void setShareDialog(Bitmap bitmap) {
        ShareDialog shareDialog = new ShareDialog(this, R.style.Theme_dialog, bitmap);
        shareDialog.setCancelable(false);
        shareDialog.getWindow().setWindowAnimations(R.style.dialogWindowAnim);
        shareDialog.getWindow().setGravity(Gravity.CENTER);
        shareDialog.show();
    }

    /**
     * 分时图模块
     */
    private void initMinuteView() {
        if (this.getResources().getConfiguration().orientation == Configuration.ORIENTATION_LANDSCAPE) {//横屏
            minuteChartView.setGridRows(6);
            minuteChartView.setGridColumns(5);
            minuteChartView.setGridChildRows(4);
            minuteChartView.setVolumeHeight(80);
            minuteChartView.setVolumeTextHeight(20);
            viewMinuteLeftMessage.setVisibility(View.VISIBLE);
            lineMinuteView.setVisibility(View.VISIBLE);
        } else if (this.getResources().getConfiguration().orientation == Configuration.ORIENTATION_PORTRAIT) {//竖屏
            minuteChartView.setGridRows(6);
            minuteChartView.setGridColumns(5);
            minuteChartView.setGridChildRows(4);
            minuteChartView.setVolumeHeight(100);
            minuteChartView.setVolumeTextHeight(20);
            viewMinuteLeftMessage.setVisibility(View.GONE);
            lineMinuteView.setVisibility(View.GONE);
        }
    }

    private void initMinuteData() {
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
                MinuteParent.DataBeanX minuteData = DataRequest.getMinuteData(mContext);
                minTime = new Date(minuteData.getMin());
                maxTime = new Date(minuteData.getMax());
                preSettle = Float.valueOf(minuteData.getPreSettle());
//        float preSettle = Float.valueOf(minuteData.getPreClose());
                List<MinuteParent.DataBeanX.DataBean> data = minuteData.getData();
                List<MinuteParent.DataBeanX.TradeRangesBean> tradeRanges = minuteData.getTradeRanges();

                for (int i = 1; i < data.size(); i++) {
                    Minute minuteDataMadel = new Minute();

                    minuteDataMadel.ruleAt = new Date(data.get(i).getRuleAt());

                    if (ValueUtil.isStrNotEmpty(data.get(i).getLast())) { //成交价 最新报价 Y轴值
                        minuteDataMadel.last = data.get(i).getLast();
                    }
                    if (ValueUtil.isStrNotEmpty(data.get(i).getAverage())) {
                        minuteDataMadel.average = data.get(i).getAverage(); //均价
                    }
                    if (ValueUtil.isStrNotEmpty(data.get(i).getInterest())) {
                        minuteDataMadel.interest = data.get(i).getInterest(); //持仓量
                    }
                    if (ValueUtil.isStrNotEmpty(data.get(i).getChgInterest())) { //持仓变化量
                        minuteDataMadel.chgInterest = data.get(i).getChgInterest();
                    }
                    if (ValueUtil.isStrNotEmpty(data.get(i).getVolume())) { //成交量
                        minuteDataMadel.volume = data.get(i).getVolume();
                    }

                    if (ValueUtil.isStrNotEmpty(data.get(i).getSettle())) { //结算价
                        minuteDataMadel.settle = data.get(i).getSettle();
                    }
                    if (ValueUtil.isStrNotEmpty(data.get(i).getHighest())) {
                        minuteDataMadel.highest = data.get(i).getHighest(); //最高价
                    }
                    if (ValueUtil.isStrNotEmpty(data.get(i).getLowest())) {
                        minuteDataMadel.lowest = data.get(i).getLowest(); //最低价
                    }
                    if (ValueUtil.isStrNotEmpty(data.get(i).getOpen())) { //开盘价
                        minuteDataMadel.open = data.get(i).getOpen();
                    }
                    if (ValueUtil.isStrNotEmpty(data.get(i).getLast())) { //收盘价
                        minuteDataMadel.close = data.get(i).getLast();
                    }


                    if (ValueUtil.isStrNotEmpty(data.get(i).getAsk1p())) { //卖价
                        minuteDataMadel.ask1p = data.get(i).getAsk1p();
                    }
                    if (ValueUtil.isStrNotEmpty(data.get(i).getAsk1v())) {
                        minuteDataMadel.ask1v = data.get(i).getAsk1v(); //卖量
                    }
                    if (ValueUtil.isStrNotEmpty(data.get(i).getBid1p())) {
                        minuteDataMadel.bid1p = data.get(i).getBid1p(); //买价
                    }
                    if (ValueUtil.isStrNotEmpty(data.get(i).getBid1v())) { //买量
                        minuteDataMadel.bid1v = data.get(i).getBid1v();
                    }
                    if (ValueUtil.isStrNotEmpty(data.get(i).getUpdown())) { //涨跌
                        minuteDataMadel.updown = data.get(i).getUpdown();
                    }
                    if (ValueUtil.isStrNotEmpty(data.get(i).getPercent())) { //涨跌幅度
                        minuteDataMadel.percent = data.get(i).getPercent();
                    }

                    if (ValueUtil.isStrNotEmpty(data.get(i).getPreSettle())) { //买量
                        minuteDataMadel.preSettle = data.get(i).getPreSettle();
                    }
                    if (ValueUtil.isStrNotEmpty(data.get(i).getPreClose())) { //涨跌
                        minuteDataMadel.preClose = data.get(i).getPreClose();
                    }
                    if (ValueUtil.isStrNotEmpty(data.get(i).getPreInterest())) { //涨跌幅度
                        minuteDataMadel.preInterest = data.get(i).getPreInterest();
                    }


                    if (ValueUtil.isStrNotEmpty(data.get(i).getUpLimit())) {//
                        minuteDataMadel.upLimit = data.get(i).getUpLimit();
                    }
                    if (ValueUtil.isStrNotEmpty(data.get(i).getLoLimit())) { //
                        minuteDataMadel.loLimit = data.get(i).getLoLimit();
                    }
                    if (ValueUtil.isStrNotEmpty(data.get(i).getTurnover())) { //
                        minuteDataMadel.turnover = data.get(i).getTurnover();
                    }


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
        minuteChartView.setVolumeHeight(100);
        minuteChartView.setVolumeTextHeight(20);

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


    /**
     * K 线图
     */
    private void initKView() {
        if (this.getResources().getConfiguration().orientation == Configuration.ORIENTATION_LANDSCAPE) {//横屏
            kchartView.setGridRows(3);
            kchartView.setGridColumns(8);
            viewKLeftMessage.setVisibility(View.VISIBLE);
        } else if (this.getResources().getConfiguration().orientation == Configuration.ORIENTATION_PORTRAIT) {//竖屏
            kchartView.setGridRows(4);
            kchartView.setGridColumns(4);
            viewKLeftMessage.setVisibility(View.GONE);
        }

        mKAdapter = new KChartAdapter();
        kchartView.setAdapter(mKAdapter);
        kchartView.setDateTimeFormatter(kType == 0 ? new ShortDateFormatter() : new TimeFormatter());//09/11 日K格式  09:11 分格式
        kchartView.setGridRows(6);//横线
        kchartView.setGridColumns(5);//竖线
        kchartView.setGridLineWidth(3);
        kchartView.setLongPress(false);
        kchartView.setClosePress(true);
        kchartView.setSelectedLineWidth(1);//长按选中线宽度
        kchartView.setSelectorBackgroundColor(ContextCompat.getColor(this, R.color.c4F5490));//选择器背景色
        kchartView.setSelectorTextColor(ContextCompat.getColor(this, R.color.cE7EDF5));//选择器文字颜色
        kchartView.setBackgroundColor(ContextCompat.getColor(this, R.color.c2A2D4F));//背景
        kchartView.setGridLineColor(ContextCompat.getColor(this, R.color.cFF333556));//表格线颜色
        kchartView.setCandleSolid(true);//蜡柱是否实心

        kchartView.showLoading();
        kchartView.setRefreshListener(this);
        onLoadMoreBegin(kchartView);
    }

    /**
     * 填充K线数据
     *
     * @param type
     */
    private void fillKData(final int type) {
        new Thread(new Runnable() {
            @Override
            public void run() {
                final List<KLine> kdata = DataRequest.getData(KlineActivity.this, mKAdapter.getCount(), dataNum, type);
                runOnUiThread(new Runnable() {
                    @Override
                    public void run() {
                        //第一次加载时开始动画
                        if (mKAdapter.getCount() == 0) {
                            kchartView.startAnimation();
                        }
                        mKAdapter.addFooterData(kdata);
                        //加载完成，还有更多数据
                        if (kdata.size() > 0) {
                            kchartView.refreshComplete();
                        }
                        //加载完成，没有更多数据
                        else {
                            kchartView.refreshEnd();
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
                kchartView.setSelectorTextSize(DensityUtil.dp2px(6));//选择器文字大小
            } else {
                kchartView.setSelectorTextSize(DensityUtil.dp2px(8));//选择器文字大小
            }
        } else {
            if (width <= 480) {
                kchartView.setSelectorTextSize(DensityUtil.dp2px(10));//选择器文字大小
            } else {
                kchartView.setSelectorTextSize(DensityUtil.dp2px(12));//选择器文字大小
            }
        }

    }

    @Override
    public void onLoadMoreBegin(KChartView chart) {
        fillKData(kType);
    }



}
