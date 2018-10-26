package com.github.tifezh.kchartlib.chart.view;

import android.app.Activity;
import android.content.Context;
import android.content.pm.ActivityInfo;
import android.content.res.ColorStateList;
import android.content.res.Configuration;
import android.util.AttributeSet;
import android.view.MotionEvent;
import android.view.View;
import android.widget.LinearLayout;
import android.widget.RelativeLayout;
import android.widget.TextView;

import com.github.tifezh.kchartlib.R;

/**
 *
 * Description K线图中间位置的TabBar
 * Author puyantao
 * Email 1067899750@qq.com
 * Date 2018-10-26 17:37
 */

public class KChartTabView extends RelativeLayout implements View.OnClickListener {

    LinearLayout mLlContainer;
    TextView mTvFullScreen;
    private TabSelectListener mTabSelectListener = null;
    //当前选择的index
    private int mSelectedIndex = 0;
    private ColorStateList mColorStateList;
    private int mIndicatorColor;

    public KChartTabView(Context context) {
        super(context);
        init(context);
    }

    public KChartTabView(Context context, AttributeSet attrs) {
        super(context, attrs);
        init(context);
    }

    public KChartTabView(Context context, AttributeSet attrs, int defStyleAttr) {
        super(context, attrs, defStyleAttr);
        init(context);
    }

    private void init(Context context) {
        inflate(context, R.layout.layout_tab, this);
        mLlContainer = (LinearLayout) findViewById(R.id.ll_container);
        mTvFullScreen = (TextView) findViewById(R.id.tv_fullScreen);
        mTvFullScreen.setOnClickListener(new OnClickListener() {
            @Override
            public void onClick(View v) {
                Activity activity = (Activity) getContext();
                boolean isVertical = (getContext().getResources().getConfiguration().orientation == Configuration.ORIENTATION_PORTRAIT);
                if (isVertical) {
                    activity.setRequestedOrientation(ActivityInfo.SCREEN_ORIENTATION_LANDSCAPE);

                } else {
                    activity.setRequestedOrientation(ActivityInfo.SCREEN_ORIENTATION_SENSOR_PORTRAIT);
                }
            }
        });
        mTvFullScreen.setSelected(true);
        if (mColorStateList != null) {
            mTvFullScreen.setTextColor(mColorStateList);
        }
    }

    @Override
    public void onClick(View v) {
        if (mSelectedIndex >= 0 && mSelectedIndex < mLlContainer.getChildCount()) {
            mLlContainer.getChildAt(mSelectedIndex).setSelected(false);
        }
        mSelectedIndex = mLlContainer.indexOfChild(v);
        v.setSelected(true);
        mTabSelectListener.onTabSelected(mSelectedIndex);
    }

    public interface TabSelectListener {
        /**
         * 选择tab的位置序号
         *
         * @param position
         */
        void onTabSelected(int position);
    }

    @Override
    public boolean dispatchTouchEvent(MotionEvent ev) {
        getParent().requestDisallowInterceptTouchEvent(true);
        return super.dispatchTouchEvent(ev);
    }

    /**
     * 添加选项卡
     *
     * @param text 选项卡文字
     */
    public void addTab(String text) {
        TabView tabView = new TabView(getContext());
        tabView.setOnClickListener(this);
        tabView.setText(text);
        tabView.setTextColor(mColorStateList);
        tabView.setIndicatorColor(mIndicatorColor);
        mLlContainer.addView(tabView);
        //第一个默认选中
        if (mLlContainer.getChildCount() == 1) {
            tabView.setSelected(true);
            mSelectedIndex = 0;
            onTabSelected(mSelectedIndex);
        }
    }

    /**
     * 获取选项总数
     * @return
     */
    public int getTabCount() {
        return mLlContainer.getChildCount();
    }

    /**
     * 设置选项卡监听
     *
     * @param listener TabSelectListener
     */
    public void setOnTabSelectListener(TabSelectListener listener) {
        this.mTabSelectListener = listener;
        //默认选中上一个位置
        if (mLlContainer.getChildCount() > 0 && mTabSelectListener != null) {
            mTabSelectListener.onTabSelected(mSelectedIndex);
        }
    }

    public void onTabSelected(int position) {
        if (mTabSelectListener != null) {
            mTabSelectListener.onTabSelected(position);
        }
    }


    /**
     * 获取当前选中的位置
     *
     * @return
     */
    public int getTabSelectedIndex() {
        return mSelectedIndex;
    }

    public void setTextColor(ColorStateList color) {
        mColorStateList = color;
        for (int i = 0; i < mLlContainer.getChildCount(); i++) {
            TabView tabView = (TabView) mLlContainer.getChildAt(i);
            tabView.setTextColor(mColorStateList);
        }
        if (mColorStateList != null) {
            mTvFullScreen.setTextColor(mColorStateList);
        }
    }

    public void setIndicatorColor(int color) {
        mIndicatorColor = color;
        for (int i = 0; i < mLlContainer.getChildCount(); i++) {
            TabView tabView = (TabView) mLlContainer.getChildAt(i);
            tabView.setIndicatorColor(mIndicatorColor);
        }
    }

}
