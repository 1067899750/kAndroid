package com.github.tifezh.kchartlib.chart.groupview;

import android.content.Context;
import android.support.annotation.ColorRes;
import android.support.annotation.DimenRes;
import android.support.v4.content.ContextCompat;
import android.util.AttributeSet;


import com.github.tifezh.kchartlib.R;
import com.github.tifezh.kchartlib.chart.comInterface.ILem;

import java.util.Collection;


public class GroupView extends BaseGroupView{
    private Context mContext;
    private MainView mMainView;
    private ChildView mChildView;

    public GroupView(Context context) {
        super(context);
        initView(context);
    }

    public GroupView(Context context, AttributeSet attrs) {
        super(context, attrs);
        initView(context);
    }

    public GroupView(Context context, AttributeSet attrs, int defStyleAttr) {
        super(context, attrs, defStyleAttr);
        initView(context);
    }

    private void initView(Context context){
        this.mContext = context;

        mMainView = new MainView(getContext());
        mChildView = new ChildView(getContext());

        setMainDraw(mMainView);
        setChildDraw(mChildView);

        addView(mMainView, 0);
        addView(mChildView, 1);

    }


    public void initData(Collection<? extends ILem> datas) {
       mMainView.initData(datas, mChildView);
    }



}











