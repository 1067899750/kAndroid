package com.github.tifezh.kchartlib.chart.rate;

import android.content.Context;
import android.graphics.Canvas;
import android.graphics.Paint;
import android.util.AttributeSet;
import android.util.Log;

import com.github.tifezh.kchartlib.R;
import com.github.tifezh.kchartlib.chart.rate.base.IRate;
import com.github.tifezh.kchartlib.chart.rate.draw.RateDraw;
import com.github.tifezh.kchartlib.utils.DateUtil;
import com.github.tifezh.kchartlib.utils.DensityUtil;
import com.github.tifezh.kchartlib.utils.StrUtil;

/**
 * Description 利率试图
 * Author puyantao
 * Email 1067899750@qq.com
 * Date 2018-12-4 10:43
 */

public class RateView extends BaseRateView {
    private Paint mSelectedLinePaint = new Paint(Paint.ANTI_ALIAS_FLAG);
    private Paint mTextPaint = new Paint(Paint.ANTI_ALIAS_FLAG);
    private int mTextPaddingLeft = DensityUtil.dp2px(10);//指标文字间距
    private int mTimeLeftPadding = DensityUtil.dp2px(5);//时间距左边距离

    private int mLeftCount = 4; //默认等分个数

    //变化时，获取当前的最大和最小值
    private float mMaxValue;
    private float mMinValue;
    private RateDraw mRateDraw;

    public RateView(Context context) {
        super(context);
        initView();
    }

    public RateView(Context context, AttributeSet attrs) {
        super(context, attrs);
        initView();
    }

    public RateView(Context context, AttributeSet attrs, int defStyleAttr) {
        super(context, attrs, defStyleAttr);
        initView();
    }

    private void initView() {
        mSelectedLinePaint.setStrokeWidth(1f);
        mSelectedLinePaint.setColor(getColor(R.color.chart_press_xian));

        mTextPaint.setColor(getColor(R.color.c6A798E));
        mTextPaint.setTextSize(sp2px(11));


        mRateDraw = new RateDraw(this);
        setMainDraw(mRateDraw);

    }

    @Override
    protected void onDraw(Canvas canvas) {
        super.onDraw(canvas);

        if (mWidth == 0 || mMainRect.height() == 0 || mItemCount == 0) {
            return;
        }

        calculateValue();
        canvas.save();
        canvas.scale(1, 1);

        drawLine(canvas);
        drawText(canvas);

        drawValue(canvas, (isLongPress || !isClosePress) ? mSelectedIndex : mStopIndex);
        canvas.restore();

    }


    /**
     * 计算当前的显示区域
     */
    private void calculateValue() {
        if (!isLongPress()) {
            mSelectedIndex = -1;
        }



        Log.i("mStartIndex : ", mTranslateX + "-->mTranslateX");
        Log.i("mStartIndex : ", xToTranslateX(0) + "-->mStartIndex");
        Log.i("mStartIndex : ", xToTranslateX(mWidth) + "-->mStopIndex");
        mStartIndex = indexOfTranslateX(xToTranslateX(0));
        mStopIndex = indexOfTranslateX(xToTranslateX(mWidth));


        mMainMaxValue = getItem(mStartIndex).getValue();
        mMainMinValue = getItem(mStartIndex).getValue();

        for (int i = mStartIndex; i <= mStopIndex; i++) {
            IRate point = getItem(i);
            if (mMainDraw != null) {
                mMainMaxValue = Math.max(mMainMaxValue, mMainDraw.getValue(point));
                mMainMinValue = Math.min(mMainMinValue, mMainDraw.getValue(point));
            }

        }
        if (mMainMaxValue != mMainMinValue) {
            float padding = (mMainMaxValue - mMainMinValue) * 0.05f;
            mMainMaxValue += padding;
            mMainMinValue -= padding;
        } else {
            //当最大值和最小值都相等的时候 分别增大最大值和 减小最小值
            mMainMaxValue += Math.abs(mMainMaxValue * 0.05f);
            mMainMinValue -= Math.abs(mMainMinValue * 0.05f);
            if (mMainMaxValue == 0) {
                mMainMaxValue = 1;
            }
        }
        mMainScaleY = mMainRect.height() * 1f / (mMainMaxValue - mMainMinValue);


        if (mAnimator.isRunning()) {
            float value = (float) mAnimator.getAnimatedValue();
            mStopIndex = mStartIndex + Math.round(value * (mStopIndex - mStartIndex));
        }
    }


    //画走势图
    private void drawLine(Canvas canvas) {
        //保存之前的平移，缩放
        canvas.save();
        canvas.translate(mTranslateX * mScaleX, 0); //平移
        canvas.scale(mScaleX, 1); //缩放

        mMaxValue = getItem(mStartIndex).getValue();
        mMinValue = getItem(mStartIndex).getValue();

        for (int i = mStartIndex; i <= mStopIndex; i++) {
            IRate currentPoint = getItem(i);
            float currentPointX = getX(i);
            IRate lastPoint = i == 0 ? currentPoint : getItem(i - 1);
            float lastX = i == 0 ? currentPointX : getX(i - 1);
            if (mMainDraw != null) {
                if (mMaxValue < getItem(i).getValue()) {
                    mMaxValue = getItem(i).getValue();
                } else if (mMinValue > getItem(i).getValue()) {
                    mMinValue = getItem(i).getValue();
                }
                mMainDraw.drawTranslated(lastPoint, currentPoint, lastX, currentPointX, canvas, this, i);
            }
        }


        //画选择线
        if (isLongPress || !isClosePress) {
            IRate point = getItem(mSelectedIndex);
            if (point == null) {
                return;
            }
            float x = getX(mSelectedIndex);
            float y = getMainY(point.getValue());

            mSelectedLinePaint.setStrokeWidth(mSelectedLinePaint.getStrokeWidth() / mScaleX);
            canvas.drawLine(x, mMainRect.top, x, mMainRect.bottom, mSelectedLinePaint);
//            canvas.drawLine(-mTranslateX, y, -mTranslateX + mWidth / mScaleX, y, mSelectedLinePaint);//隐藏横线
        }
        //还原 平移缩放
        canvas.restore();
    }


    //画文字
    private void drawText(Canvas canvas) {
        Paint.FontMetrics fm = mTextPaint.getFontMetrics();
        float textHeight = fm.descent - fm.ascent;
        float baseLine = (textHeight - fm.bottom - fm.top) / 2;
        //--------------左边文字-------------
        if (mMainDraw != null) {
            canvas.drawText(StrUtil.floatToString(mMainMaxValue, 4), mTextPaddingLeft, baseLine + mMainRect.top, mTextPaint);
            canvas.drawText(StrUtil.floatToString(mMainMinValue, 4), mTextPaddingLeft,
                    mMainRect.bottom - textHeight + baseLine, mTextPaint);
            float rowValue = (mMainMaxValue - mMainMinValue) / mLeftCount;
            float rowSpace = mMainRect.height() / mLeftCount;

            for (int i = 1; i < mLeftCount; i++) {
                String text = StrUtil.floatToString(rowValue * (mLeftCount - i) + mMainMinValue, 4);
                canvas.drawText(text, mTextPaddingLeft, fixTextY(rowSpace * i + mMainRect.top), mTextPaint);
            }
        }

        //--------------画时间 共五个点---------------------
        float columnSpace = (mWidth) / mLeftCount;
        float y = mMainRect.bottom + baseLine;
        float startX = getX(mStartIndex) - mPointWidth / 2;
        float stopX = getX(mStopIndex) + mPointWidth / 2;

        float translateX = xToTranslateX(0);
        if (translateX >= startX && translateX <= stopX) {
            canvas.drawText(DateUtil.getStringDateByLong(mAdapter.getDate(mStartIndex).getTime(), 9),
                    mTimeLeftPadding,
                    y + mTimeLeftPadding, mTextPaint);
        }
        translateX = xToTranslateX(mWidth);
        if (translateX >= startX && translateX <= stopX) {
            String text = DateUtil.getStringDateByLong(mAdapter.getDate(mStopIndex).getTime(), 9);
            canvas.drawText(text,
                    mWidth - mTextPaint.measureText(text) - mTimeLeftPadding,
                    y + mTimeLeftPadding, mTextPaint);
        }


        for (int i = 1; i < mLeftCount; i++) {
            translateX = xToTranslateX(columnSpace * i);
            if (translateX >= startX && translateX <= stopX) {
                int index = indexOfTranslateX(translateX);
                String text = DateUtil.getStringDateByLong(mAdapter.getDate(index).getTime(), 9);
                canvas.drawText(text,
                        columnSpace * i - mTextPaint.measureText(text) / 2,
                        y + mTimeLeftPadding, mTextPaint);
            }
        }

    }

    //画值
    private void drawValue(Canvas canvas, int position) {
        Paint.FontMetrics fm = mTextPaint.getFontMetrics();
        float textHeight = fm.descent - fm.ascent;
        float baseLine = (textHeight - fm.bottom - fm.top) / 2;
        if (position >= 0 && position < mItemCount) {
            if (mMainDraw != null) {
                float y = mMainRect.top + baseLine - textHeight;
                float x = 0;
                if (isLongPress || !isClosePress) {
                    mMainDraw.drawText(canvas, this, position, x, y);
                }
            }
        }
    }


    @Override
    public void onLeftSide() {

    }

    @Override
    public void onRightSide() {

    }

    /**
     * 解决text居中的问题
     */
    public float fixTextY(float y) {
        Paint.FontMetrics fontMetrics = mTextPaint.getFontMetrics();
        return (y + (fontMetrics.descent - fontMetrics.ascent) / 2 - fontMetrics.descent);
    }


}















