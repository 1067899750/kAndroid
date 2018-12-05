package com.github.tifezh.kchartlib.chart.rate;

import android.content.Context;
import android.support.annotation.ColorRes;
import android.support.annotation.DimenRes;
import android.support.v4.content.ContextCompat;
import android.util.AttributeSet;

import com.github.tifezh.kchartlib.chart.rate.draw.RateDraw;

/**
 * Description 利率试图
 * Author puyantao
 * Email 1067899750@qq.com
 * Date 2018-12-4 10:43
 */

public class MyRateView extends BaseRateView {
    private RateDraw mRateDraw;


    public MyRateView(Context context) {
        super(context);
        initView();
    }

    public MyRateView(Context context, AttributeSet attrs) {
        super(context, attrs);
        initView();
    }

    public MyRateView(Context context, AttributeSet attrs, int defStyleAttr) {
        super(context, attrs, defStyleAttr);
        initView();
    }

    private void initView() {

        mRateDraw = new RateDraw(this);
        setMainDraw(mRateDraw);


    }

    @Override
    public void onLeftSide() {

    }

    @Override
    public void onRightSide() {

    }




}















