package com.github.tifezh.kchartlib.chart.view;

import android.content.Context;
import android.content.res.ColorStateList;
import android.util.AttributeSet;
import android.view.View;
import android.widget.RelativeLayout;
import android.widget.TextView;

import com.github.tifezh.kchartlib.R;

/**
 * Description
 * Author puyantao
 * Email 1067899750@qq.com
 * Date 2018-11-6 11:29
 */


public class TabView extends RelativeLayout {
    private TextView mTextView;
    private View mIndicator;

    public TabView(Context context) {
        super(context);
        initView(context);
    }

    public TabView(Context context, AttributeSet attrs) {
        super(context, attrs);
        initView(context);
    }

    private void initView(Context mContext) {
        inflate(mContext, R.layout.view_item_tab, this);
        mTextView = (TextView) findViewById(R.id.tvTabText);
        mIndicator = (View) findViewById(R.id.indicator);
    }

    public void setTextColor(ColorStateList color) {
        if (color != null) {
            mTextView.setTextColor(color);
        }
    }

    public void setText(String text) {
        mTextView.setText(text);
    }

    public void setIndicatorColor(int color) {
        mIndicator.setBackgroundColor(color);
    }

    @Override
    public void setSelected(boolean selected) {
        super.setSelected(selected);
        mIndicator.setVisibility(selected ? VISIBLE : INVISIBLE);
    }
}
