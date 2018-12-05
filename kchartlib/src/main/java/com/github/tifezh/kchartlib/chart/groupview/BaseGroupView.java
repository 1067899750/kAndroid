package com.github.tifezh.kchartlib.chart.groupview;

import android.content.Context;
import android.graphics.Canvas;
import android.util.AttributeSet;
import android.view.MotionEvent;
import android.view.View;
import android.view.ViewGroup;

import com.github.tifezh.kchartlib.chart.base.IGroupDraw;

import static android.view.View.MeasureSpec.AT_MOST;

public class BaseGroupView extends ViewGroup {
    private Context mContext;

    protected final float DEF_WIDTH = 650;
    protected final float DEF_HIGHT = 400;

    protected IGroupDraw mChildDraw; //子试图
    protected IGroupDraw mMainDraw; //主视图



    public BaseGroupView(Context context) {
        super(context);
        initView(context);
    }

    public BaseGroupView(Context context, AttributeSet attrs) {
        super(context, attrs);
        initView(context);
    }

    public BaseGroupView(Context context, AttributeSet attrs, int defStyleAttr) {
        super(context, attrs, defStyleAttr);
        initView(context);
    }

    private void initView(Context context) {
        this.mContext = context;

    }

    @Override
    protected void onMeasure(int widthMeasureSpec, int heightMeasureSpec) {
        super.onMeasure(widthMeasureSpec, heightMeasureSpec);
        int widthSpecMode = MeasureSpec.getMode(widthMeasureSpec);
        int widthSpecSize = MeasureSpec.getSize(widthMeasureSpec);
        int heightSpecMode = MeasureSpec.getMode(heightMeasureSpec);
        int heightSpecSize = MeasureSpec.getSize(heightMeasureSpec);
        if (widthSpecMode == AT_MOST && heightSpecMode == AT_MOST) {
            setMeasuredDimension((int) DEF_WIDTH, (int) DEF_HIGHT);
        } else if (widthSpecMode == AT_MOST) {
            setMeasuredDimension((int) DEF_WIDTH, heightSpecSize);
        } else if (heightSpecMode == AT_MOST) {
            setMeasuredDimension(widthSpecSize, (int) DEF_HIGHT);
        } else {
            setMeasuredDimension(widthSpecSize, heightSpecSize);
        }

        initRect(getMeasuredWidth(), getMeasuredHeight()); //绘制试图里的子示图
    }

    @Override
    protected void onSizeChanged(int w, int h, int oldw, int oldh) {
        super.onSizeChanged(w, h, oldw, oldh);
        initRect(w, h); //绘制试图里的子示图
    }


    //绘制试图里的子示图
    private void initRect(int w, int h) {
        int childCount = getChildCount();
        for (int i = 0; i < childCount; i++) {
            View childView = getChildAt(i);
            // 每一个子控件测量大小
            measureChild(childView, w, h);
            childView.setLayoutParams(new LayoutParams(w, h));
        }
    }

    @Override
    protected void onLayout(boolean changed, int l, int t, int r, int b) {
        int childCount = getChildCount();
        for (int i = 0; i < childCount; i++) {
            View childView = getChildAt(i);
            // 每一个子控件进行布局
            childView.layout(0, 0, childView.getMeasuredWidth(), childView.getMeasuredHeight());
        }
    }

    @Override
    protected void onDraw(Canvas canvas) {
        super.onDraw(canvas);

    }

    @Override
    public boolean onTouchEvent(MotionEvent event) {
        getChildAt(0).onTouchEvent(event);
        return super.onTouchEvent(event);

    }

    //设置主区域的 IChartDraw
    public void setMainDraw(IGroupDraw mainDraw) {
        mMainDraw = mainDraw;
    }

    public void setChildDraw(IGroupDraw childDraw) {
        mChildDraw = childDraw;
    }

    public IGroupDraw getChildDraw() {
        return mChildDraw;
    }

    public IGroupDraw getMainDraw() {
        return mMainDraw;
    }

}




















