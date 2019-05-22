package com.github.tifezh.kchartlib.chart.rate;

import android.content.Context;
import android.graphics.Canvas;
import android.graphics.Color;
import android.graphics.Paint;
import android.graphics.RectF;
import android.util.AttributeSet;
import android.util.Log;

import com.github.tifezh.kchartlib.R;

import com.github.tifezh.kchartlib.utils.DateUtil;
import com.github.tifezh.kchartlib.utils.StrUtil;

import java.util.ArrayList;
import java.util.Collection;
import java.util.List;

/**
 * Description 利率试图
 * Author puyantao
 * Email 1067899750@qq.com
 * Date 2018-12-4 10:43
 */

public class RateView extends BaseRateView {
    private Paint mSelectedLinePaint = new Paint(Paint.ANTI_ALIAS_FLAG);
    private Paint mTextPaint = new Paint(Paint.ANTI_ALIAS_FLAG);

    private Paint mLinePaint = new Paint(Paint.ANTI_ALIAS_FLAG);
    private Paint mTextRactPaint = new Paint(Paint.ANTI_ALIAS_FLAG);
    private Paint mSelectorBackgroundPaint = new Paint(Paint.ANTI_ALIAS_FLAG);
    private Paint mSelectorTextPaint = new Paint(Paint.ANTI_ALIAS_FLAG);
    private Paint mDotPaint = new Paint(Paint.ANTI_ALIAS_FLAG); //圆点

    private float mTextSize = 10;


    private int mTextPaddingLeft;//指标文字间距
    private int mTimeLeftPadding;//时间距左边距离

    private int mLeftCount = 4; //默认等分个数

    //变化时，获取当前的最大和最小值
    private float mMaxValue;
    private float mMinValue;


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
        mTextPaddingLeft = dp2px(10);
        mTimeLeftPadding = dp2px(5);

        mSelectedLinePaint.setStrokeWidth(dp2px(1));
        mSelectedLinePaint.setColor(getColor(R.color.chart_press_xian));

        mTextPaint.setColor(getColor(R.color.c6A798E));
        mTextPaint.setTextSize(sp2px(11));


        mLinePaint.setColor(getColor(R.color.cffffff));

        mTextPaint.setColor(getColor(R.color.cffffff));
        mTextPaint.setTextSize(sp2px(11));


        mTextRactPaint.setColor(Color.parseColor("#6A798E"));
        mTextRactPaint.setTextSize(sp2px(mTextSize));
        mTextRactPaint.setStrokeWidth(dp2px(0.5f));

        mSelectorTextPaint.setColor(Color.parseColor("#E7EDF5"));
        mSelectorTextPaint.setTextSize(sp2px(13));


        mDotPaint.setStyle(Paint.Style.FILL);   //圆点
        mDotPaint.setColor(getColor(R.color.chart_FF6600));

        mSelectorBackgroundPaint.setColor(getColor(R.color.c4F5490));

    }


    public void initData(Collection<? extends IRate> datas) {
        mPoints.clear();
        if (datas != null) {
            this.mPoints.addAll(datas);
            mItemCount = mPoints.size();
        }
        notifyChanged();

        setScaleValue();  //计算缩放率
        setTranslateXFromScrollX(mScrollX);
    }


    @Override
    protected void onDraw(Canvas canvas) {
        super.onDraw(canvas);

        if (mPoints == null || mPoints.size() == 0 || mItemCount == 0) {
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
            if (point != null) {
                mMainMaxValue = Math.max(mMainMaxValue, point.getValue());
                mMainMinValue = Math.min(mMainMinValue, point.getValue());
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
        mMainScaleY = mMainHeight * 1f / (mMainMaxValue - mMainMinValue);


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

        mLinePaint.setStrokeWidth(dp2px(1) / mScaleX); //避免缩放对画笔粗细的影响

        mMaxValue = getItem(mStartIndex).getValue();
        mMinValue = getItem(mStartIndex).getValue();

        for (int i = mStartIndex; i <= mStopIndex; i++) {
            IRate currentPoint = getItem(i);
            float currentPointX = getX(i);
            IRate lastPoint = i == 0 ? currentPoint : getItem(i - 1);
            float lastX = i == 0 ? currentPointX : getX(i - 1);

            if (mMaxValue < getItem(i).getValue()) {
                mMaxValue = getItem(i).getValue();
            } else if (mMinValue > getItem(i).getValue()) {
                mMinValue = getItem(i).getValue();
            }
            drawMainLine(canvas, mLinePaint, lastX, lastPoint.getValue(), currentPointX, currentPoint.getValue());

        }


        //画选择线
        if (isLongPress || !isClosePress) {
            IRate point = getItem(mSelectedIndex);
            if (point == null) {
                return;
            }
            float x = getX(mSelectedIndex);
            float y = getMainY(point.getValue());

            mSelectedLinePaint.setStrokeWidth(dp2px(1) / mScaleX); //避免缩放对横轴画笔粗细的影响
            canvas.drawLine(x, mTopPadding, x, mMainHeight, mSelectedLinePaint);

            mSelectedLinePaint.setStrokeWidth(dp2px(1)); //避免缩放对纵轴画笔粗细的影响
            canvas.drawLine(-mTranslateX, y, -mTranslateX + mWidth / mScaleX, y, mSelectedLinePaint);//隐藏横线
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

        canvas.drawText(StrUtil.floatToString(mMainMaxValue, 4), mTextPaddingLeft,
                baseLine + mTopPadding, mTextPaint); //顶部

        canvas.drawText(StrUtil.floatToString(mMainMinValue, 4), mTextPaddingLeft,
                mMainHeight - textHeight + baseLine, mTextPaint); //底部

        float rowValue = (mMainMaxValue - mMainMinValue) / mLeftCount;
        float rowSpace = mMainHeight / mLeftCount;
        for (int i = 1; i < mLeftCount; i++) {
            String text = StrUtil.floatToString(rowValue * (mLeftCount - i) + mMainMinValue, 4);
            canvas.drawText(text, mTextPaddingLeft, fixTextY(rowSpace * i + mTopPadding), mTextPaint);

        }

        //--------------画时间 共五个点---------------------
        float columnSpace = (mWidth) / mLeftCount;
        float y = mTopPadding + baseLine + mMainHeight;
        float startX = getX(mStartIndex) - mPointWidth / 2;
        float stopX = getX(mStopIndex) + mPointWidth / 2;

        float translateX = xToTranslateX(0);
        if (translateX >= startX && translateX <= stopX) {
            canvas.drawText(DateUtil.getStringDateByLong(mPoints.get(mStartIndex).getDate().getTime(), 9),
                    mTimeLeftPadding,
                    y, mTextPaint);
        }
        translateX = xToTranslateX(mWidth);
        if (translateX >= startX && translateX <= stopX) {
            String text = DateUtil.getStringDateByLong(mPoints.get(mStopIndex).getDate().getTime(), 9);
            canvas.drawText(text,
                    mWidth - mTextPaint.measureText(text) - mTimeLeftPadding,
                    y, mTextPaint);
        }


        for (int i = 1; i < mLeftCount; i++) {
            translateX = xToTranslateX(columnSpace * i);
            if (translateX >= startX && translateX <= stopX) {
                int index = indexOfTranslateX(translateX);
                String text = DateUtil.getStringDateByLong(mPoints.get(index).getDate().getTime(), 9);
                canvas.drawText(text,
                        columnSpace * i - mTextPaint.measureText(text) / 2,
                        y, mTextPaint);
            }
        }

    }

    //画值
    private void drawValue(Canvas canvas, int position) {
        Paint.FontMetrics fm = mTextPaint.getFontMetrics();
        float textHeight = fm.descent - fm.ascent;
        float baseLine = (textHeight - fm.bottom - fm.top) / 2;
        if (position >= 0 && position < mItemCount) {

            float y = mTopPadding + baseLine - textHeight;
            float x = 0;
            if (isLongPress || !isClosePress) {
                drawText(canvas, position);
            }

        }
    }


    //在试图区域画线
    public void drawMainLine(Canvas canvas, Paint paint, float startX, float startValue, float stopX, float stopValue) {
//        canvas.drawCircle(stopX, getMainY(stopValue), 5 / mScaleX, mDotPaint);
        canvas.drawLine(startX, getMainY(startValue), stopX, getMainY(stopValue), paint);
    }


    private void drawText(Canvas canvas, int position) {
        Paint.FontMetrics metrics = mTextRactPaint.getFontMetrics();
        float textHeight = metrics.descent - metrics.ascent;

        IRate point = getItem(position);

        float padding = dp2px(5);
        float margin = dp2px(5);
        float width = 0;
        float left = 5;
        float top = dp2px(10);
        float bottom = 10;

        List<String> strings = new ArrayList<>();
        strings.add(DateUtil.getStringDateByLong(point.getDate().getTime(), 2));
        strings.add("数值");
        strings.add(point.getValue() + "");

        strings.add("涨跌");
        strings.add(point.getChange());
        strings.add(point.getPercent());

        width = sp2px(78);

        float x1 = translateXtoX(getX(position));
        if (x1 > getChartWidth() / 2) {
            left = margin;
        } else {
            left = getChartWidth() - width - margin;
        }
        float height = top + padding * 2 + (textHeight - metrics.bottom - metrics.top) / 2 +
                (textHeight + padding) * (strings.size() - 1);
        RectF r = new RectF(left, top, left + width, top + height + bottom);
        canvas.drawRoundRect(r, padding, padding, mSelectorBackgroundPaint);

        float h = top + padding * 2 + (textHeight - metrics.bottom - metrics.top) / 2;
        for (String s : strings) {
            if (StrUtil.isTimeText(s)) {
                mSelectorTextPaint.setTextSize(sp2px(12));
                mSelectorTextPaint.setColor(getColor(R.color.color_text_positive_paint));
                canvas.drawText(s, left + padding, h, mSelectorTextPaint);

            } else if (StrUtil.isChinaText(s)) {
                mTextRactPaint.setTextSize(sp2px(10));
                canvas.drawText(s, left + padding, h, mTextRactPaint);

            } else {
                mSelectorTextPaint.setTextSize(sp2px(13));
                if (StrUtil.isPositiveOrNagativeNumberText(s)) {
                    mSelectorTextPaint.setColor(getColor(R.color.color_negative_value));
                    canvas.drawText(s, left + padding, h, mSelectorTextPaint);
                } else {
                    mSelectorTextPaint.setColor(getColor(R.color.color_text_positive_paint));
                    canvas.drawText(s, left + padding, h, mSelectorTextPaint);
                }
            }

            h += textHeight + padding;
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















