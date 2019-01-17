package com.github.tifezh.kchartlib.chart.football;

import android.content.Context;
import android.graphics.Canvas;
import android.graphics.Color;
import android.graphics.Paint;
import android.support.annotation.ColorRes;
import android.support.annotation.Nullable;
import android.support.annotation.StringRes;
import android.util.AttributeSet;
import android.view.MotionEvent;
import android.view.View;

import com.github.tifezh.kchartlib.chart.comInterface.ILem;
import com.github.tifezh.kchartlib.utils.DensityUtil;

import java.util.Collection;

public abstract class BaseView extends View {
    protected long mHomePointCount = 0; //点的个数
    protected long mAwayPointCount = 0; //点的个数
    protected float mScaleY = 1; //Y轴单位量
    protected float mScaleX = 1; //x轴的单位量
    protected long mTotalTimers; //总时间
    protected Paint mBackgroundPaint = new Paint(Paint.ANTI_ALIAS_FLAG);
    protected int mBackgroundColor;

    protected int selectedIndex;

    //测量的控件宽高，会在onMeasure中进行测量。
    protected int mBaseWidth;//折线图宽度
    protected int mBaseHeight;//折线图高度
    protected int mHeight; //试图高度
    protected int mWidth;  //试图宽度

    protected int mTopPadding = dp2px(20); //据顶部
    protected int mBottomPadding = dp2px(0);//距底部


    public BaseView(Context context) {
        super(context);
        initView(context);
    }

    public BaseView(Context context, @Nullable AttributeSet attrs) {
        super(context, attrs);
        initView(context);
    }

    public BaseView(Context context, @Nullable AttributeSet attrs, int defStyleAttr) {
        super(context, attrs, defStyleAttr);
        initView(context);
    }


    private void initView(Context context) {
        mBackgroundColor = Color.parseColor("#FFF7F7F7");
        mBackgroundPaint.setColor(mBackgroundColor);

    }


    @Override
    protected void onMeasure(int widthMeasureSpec, int heightMeasureSpec) {
        super.onMeasure(widthMeasureSpec, heightMeasureSpec);
        mBaseWidth = mWidth = getMeasuredWidth();
        mHeight = getMeasuredHeight();
        mBaseHeight = mHeight - mTopPadding - mBottomPadding;
        notifyChanged();
    }

    @Override
    protected void onSizeChanged(int w, int h, int oldw, int oldh) {
        super.onSizeChanged(w, h, oldw, oldh);
        this.mHeight = h;
        this.mBaseHeight = mHeight - mTopPadding - mBottomPadding;
        this.mWidth = this.mBaseWidth = w;
        notifyChanged();
    }

    @Override
    protected void onLayout(boolean changed, int left, int top, int right, int bottom) {
        super.onLayout(changed, left, top, right, bottom);
    }


    @Override
    protected void onDraw(Canvas canvas) {
        super.onDraw(canvas);

        //绘制背景颜色
        canvas.drawColor(mBackgroundColor);

    }



    protected abstract void notifyChanged();
    protected abstract void calculateSelectedX(float x);

    protected String getString(@StringRes int stringId) {
        return getResources().getString(stringId);
    }


    //高度
    protected float getFontBaseLineHeight(Paint paint) {
        Paint.FontMetrics fm = paint.getFontMetrics();
        float textHeight = fm.descent - fm.ascent;
        float baseLine = (textHeight - fm.bottom - fm.top) / 2;
        return baseLine;
    }

    //文字的高度
    protected float getFontHeight(Paint paint) {
        Paint.FontMetrics fm = paint.getFontMetrics();
        float textHeight = fm.descent - fm.ascent;
        return textHeight;
    }

    /**
     * 根据颜色id获取颜色
     *
     * @param colorId
     * @return
     */
    protected int getColor(@ColorRes int colorId) {
        return getResources().getColor(colorId);
    }


    protected int dp2px(float dp) {
        final float scale = getContext().getResources().getDisplayMetrics().density;
        return (int) (dp * scale + 0.5f);
    }

    protected int sp2px(float spValue) {
        final float fontScale = getContext().getResources().getDisplayMetrics().scaledDensity;
        return (int) (spValue * fontScale + 0.5f);
    }


}























