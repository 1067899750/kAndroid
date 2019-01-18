package com.github.tifezh.kchartlib.chart.minute;

import android.content.Context;
import android.graphics.Canvas;
import android.graphics.Color;
import android.graphics.Paint;
import android.graphics.RectF;
import android.support.v4.content.ContextCompat;
import android.util.AttributeSet;

import com.github.tifezh.kchartlib.R;
import com.github.tifezh.kchartlib.chart.comInterface.IMinuteLine;
import com.github.tifezh.kchartlib.utils.DateUtil;
import com.github.tifezh.kchartlib.utils.DensityUtil;
import com.github.tifezh.kchartlib.utils.StrUtil;

import java.util.ArrayList;
import java.util.List;

public class MinuteMainView extends BaseMinuteView {
    private Paint mAvgPaint = new Paint(Paint.ANTI_ALIAS_FLAG);

    private Paint mTextPaint = new Paint(Paint.ANTI_ALIAS_FLAG); //文字
    private Paint mTextMACDPaint = new Paint(Paint.ANTI_ALIAS_FLAG); //文字
    private Paint mTextLeftPaint = new Paint(Paint.ANTI_ALIAS_FLAG); //左边文字
    private Paint mTextReightPaint = new Paint(Paint.ANTI_ALIAS_FLAG);//右边文字
    private Paint mLinePaint = new Paint(Paint.ANTI_ALIAS_FLAG); //轴线
    private Paint mTextBottomPaint = new Paint(Paint.ANTI_ALIAS_FLAG); //下边文字
    private Paint mPricePaint = new Paint(Paint.ANTI_ALIAS_FLAG);

    private Paint mVolumePaint = new Paint(Paint.ANTI_ALIAS_FLAG);

    private Paint mSelectorBackgroundPaint = new Paint(Paint.ANTI_ALIAS_FLAG);
    private Paint mSelectorTitlePaint = new Paint(Paint.ANTI_ALIAS_FLAG);
    private Paint mSelectorTextPaint = new Paint(Paint.ANTI_ALIAS_FLAG);

    private float mValueMin; //最大值
    private float mValueMax; //最小值

    private long mCount = 0;
    private float mTextSize = 10;

    private int selectedIndex;


    public MinuteMainView(Context context) {
        super(context);
        initData();
    }

    public MinuteMainView(Context context, AttributeSet attrs) {
        super(context, attrs);
        initData();
    }

    public MinuteMainView(Context context, AttributeSet attrs, int defStyleAttr) {
        super(context, attrs, defStyleAttr);
        initData();
    }


    private void initData() {
        setDrawChildView(false);
        mTopPadding = dp2px(mTopPadding);
        mBottomPadding = dp2px(mBottomPadding);

        mTextSize = sp2px(mTextSize);
        mVolumeHeight = dp2px(mVolumeHeight);
        mVolumeTextHeight = dp2px(mVolumeTextHeight);

        mTextPaint.setColor(Color.parseColor("#6774FF"));
        mTextPaint.setTextSize(sp2px(11));
        mTextPaint.setStrokeWidth(dp2px(0.5f));

        mTextMACDPaint.setColor(Color.parseColor("#6774FF"));
        mTextMACDPaint.setTextSize(sp2px(10));
        mTextMACDPaint.setStrokeWidth(dp2px(0.5f));

        mTextLeftPaint.setColor(Color.parseColor("#B1B2B6"));
        mTextLeftPaint.setTextSize(mTextSize);
        mTextLeftPaint.setStrokeWidth(dp2px(0.5f));

        mTextReightPaint.setColor(Color.parseColor("#B1B2B6"));
        mTextReightPaint.setTextSize(mTextSize);
        mTextReightPaint.setStrokeWidth(dp2px(0.5f));

        mTextBottomPaint.setColor(Color.parseColor("#6A798E"));
        mTextBottomPaint.setTextSize(mTextSize);
        mTextBottomPaint.setStrokeWidth(dp2px(0.5f));

        mLinePaint.setColor(Color.parseColor("#6774FF")); //轴线
        mLinePaint.setTextSize(mTextSize);
        mLinePaint.setStrokeWidth(dp2px(0.5f));

        mAvgPaint.setColor(Color.parseColor("#90A901"));
        mAvgPaint.setStrokeWidth(dp2px(0.5f));
        mAvgPaint.setTextSize(mTextSize);

        mPricePaint.setColor(Color.parseColor("#FF6600"));
        mPricePaint.setStrokeWidth(dp2px(0.5f));
        mPricePaint.setTextSize(mTextSize);

        mSelectorBackgroundPaint.setColor(Color.parseColor("#4F5490"));

        mSelectorTitlePaint.setColor(Color.parseColor("#9EB2CD"));
        mSelectorTitlePaint.setTextSize(sp2px(10));

        mSelectorTextPaint.setColor(Color.parseColor("#E7EDF5"));
        mSelectorTextPaint.setTextSize(sp2px(13));

        mVolumePaint.setColor(ContextCompat.getColor(getContext(), R.color.chart_red));

    }


    @Override
    protected void calculateSelectedX(float x) {
        selectedIndex = (int) (x * 1f / getX(mPoints.size() - 1) * (mPoints.size() - 1) + 0.5f);
        if (selectedIndex < 0) {
            selectedIndex = 0;
        }
        if (selectedIndex > mPoints.size() - 1) {
            selectedIndex = mPoints.size() - 1;
        }
    }

    @Override
    protected void jumpToCJLAndMACL(float downX, float downY) {

    }


    /**
     * 根据索引获取x的值
     */
    private float getX(int position) {
        mCount = 0;
        Long dateTime = mPoints.get(position).getDate().getTime();
        for (int i = 0; i < mTimes.size(); i++) {
            Long startTime = mTimes.get(i).getStartDate().getTime();
            Long endTime = mTimes.get(i).getEndDate().getTime();
            if (dateTime >= startTime && dateTime <= endTime) {
                mCount += (dateTime - startTime) / ONE_MINUTE;
                break;
            } else {
                mCount += (endTime - startTime) / ONE_MINUTE;
            }
        }
        float c = mCount * mScaleX;
        return mCount * mScaleX;
    }


    /**
     * 当数据发生变化时调用
     */
    @Override
    protected void notifyChanged() {
        if (mPoints.size() > 0) {
            mValueMax = mPoints.get(0).getHighest();
            mValueMin = mPoints.get(0).getLowest();

        }

        for (int i = 0; i < mPoints.size(); i++) {
            IMinuteLine point = mPoints.get(i);
            mValueMax = Math.max(mValueMax, point.getHighest());
            mValueMin = Math.min(mValueMin, point.getLowest());

        }

        //判断最大值和最小值是否一致
        if (mValueMax == mValueMin && mValueStart == mValueMax) {
            //当最大值和最小值都相等的时候 分别增大最大值和 减小最小值
            mValueMax += Math.abs(mValueMax * 0.05f);
            mValueMin -= Math.abs(mValueMin * 0.05f);
            if (mValueMax == 0) {
                mValueMax = 1;
            }
        } else {
            //最大值和开始值的单位差值
            float offsetValueMax = (Math.abs(mValueMax - mValueStart)) / (mGridRows / 2);
            float offsetValueMin = (Math.abs(mValueStart - mValueMin)) / (mGridRows / 2);

            //以开始的点为中点值   上下间隙多出20%
            float offset = (offsetValueMax > offsetValueMin ? offsetValueMax : offsetValueMin) * 1.05f;
            //坐标轴高度以开始的点对称
            mValueMax = mValueStart + offset * (mGridRows / 2);
            mValueMin = mValueStart - offset * (mGridRows / 2);
            //y轴的缩放值
        }
        mScaleY = mMainHeight / (mValueMax - mValueMin);


        //x轴的缩放值
        mScaleX = (float) mWidth / getMaxPointCount(1);

        //设置主状图的宽度
        mPointWidth = (float) mWidth / getMaxPointCount(1);
        mVolumePaint.setStrokeWidth(dp2px((float) 0.5));

        invalidate();
    }

    @Override
    protected void onDraw(Canvas canvas) {
        super.onDraw(canvas);
        if (mWidth == 0 || mMainHeight == 0 || mPoints == null || mPoints.size() == 0) {
            return;
        }

        //绘制平均线和成交价
        if (mPoints.size() > 0) {
            mPricePaint.setColor(getResources().getColor(R.color.chart_FFFFFF));
            IMinuteLine lastPoint = mPoints.get(0);
            float lastX = getX(0);
            for (int i = 0; i < mPoints.size(); i++) {
                IMinuteLine curPoint = mPoints.get(i);
                float curX = getX(i);

                canvas.drawLine(lastX + mBaseTimePadding,
                        getY(lastPoint.getLast()),
                        curX + mBaseTimePadding,
                        getY(curPoint.getLast()),
                        mPricePaint); //成交价

                //给上一个只赋值
                lastPoint = curPoint;
                lastX = curX;
            }
        }


        drawText(canvas); //绘制文字

        //画指示线
        if (isLongPress || !isClosePress) {
            if (selectedIndex >= mPoints.size() || selectedIndex < 0 || mPoints.size() == 0) {
                return;
            }
            IMinuteLine point = mPoints.get(selectedIndex);
            float x = getX(selectedIndex);
            //轴线
            canvas.drawLine(x, 0, x, mMainHeight + mVolumeHeight + mVolumeTextHeight, mLinePaint);//Y
//            canvas.drawLine(0, getY(point.getLast()), mWidth, getY(point.getLast()), mLinePaint);//X

            drawSelector(selectedIndex, point, canvas);
        }
    }


    /**
     * draw选择器
     *
     * @param canvas
     */
    private void drawSelector(int selectedIndex, IMinuteLine point, Canvas canvas) {
        Paint.FontMetrics metrics = mTextLeftPaint.getFontMetrics();
        float textHeight = metrics.descent - metrics.ascent;

        float padding = DensityUtil.dp2px(5);
        float margin = DensityUtil.dp2px(5);
        float width = 0;
        float left = 5;
        float top = 10;
        float bottom = 10;

        List<String> strings = new ArrayList<>();
        strings.add(DateUtil.shortTimeFormat.format(point.getDate().getTime()));
        strings.add("比值");
        strings.add(point.getLast() + "");

        strings.add("涨跌");
        strings.add(point.getUpdown() + "");
        strings.add(point.getPercent() + "");

        width = sp2px(78);

        float x = getX(selectedIndex);
        if (x > mWidth / 2) {
            left = margin;
        } else {
            left = mWidth - width - margin;
        }
        float height = top + padding * 2 + (textHeight - metrics.bottom - metrics.top) / 2 +
                (textHeight + padding) * (strings.size() - 1);
        RectF r = new RectF(left, top, left + width, top + height + bottom);
        canvas.drawRoundRect(r, padding, padding, mSelectorBackgroundPaint);

        float y = top + padding * 2 + (textHeight - metrics.bottom - metrics.top) / 2;
//        if (x > mWidth / 2) {
//            left = margin + padding;
//            mSelectorTextPaint.setTextAlign(Paint.Align.LEFT);
//            mSelectorTitlePaint.setTextAlign(Paint.Align.LEFT);
//        } else {
//            left = mWidth - margin - padding;
//            mSelectorTextPaint.setTextAlign(Paint.Align.RIGHT);
//            mSelectorTitlePaint.setTextAlign(Paint.Align.RIGHT);
//        }
        for (String s : strings) {
            if (StrUtil.isTimeText(s)) {
                mSelectorTextPaint.setColor(getResources().getColor(R.color.color_text_positive_paint));
                canvas.drawText(s, left + padding, y, mSelectorTextPaint);

            } else if (StrUtil.isChinaText(s)) {
                canvas.drawText(s, left + padding, y, mSelectorTitlePaint);

            } else {
                if (StrUtil.isPositiveOrNagativeNumberText(s)) {
                    mSelectorTextPaint.setColor(getResources().getColor(R.color.color_negative_value));
                    canvas.drawText(s, left + padding, y, mSelectorTextPaint);
                } else {
                    mSelectorTextPaint.setColor(getResources().getColor(R.color.color_text_positive_paint));
                    canvas.drawText(s, left + padding, y, mSelectorTextPaint);
                }
            }

            y += textHeight + padding;
        }

    }

    private void drawText(Canvas canvas) {
        Paint.FontMetrics fm = mTextLeftPaint.getFontMetrics();
        float textHeight = fm.descent - fm.ascent;
        float baseLine = (textHeight - fm.bottom - fm.top) / 2;

        float rowValue = (mValueMax - mValueMin) / mGridRows;
        float rowSpace = mMainHeight / mGridRows;
        //画左边的值
        mTextLeftPaint.setColor(getResources().getColor(R.color.color_positive_value));
        canvas.drawText(StrUtil.getOneDecimals(mValueStart + rowValue * 3), mBaseTextPaddingLeft, baseLine, mTextLeftPaint); //绘制最大值

        mTextLeftPaint.setColor(getResources().getColor(R.color.color_negative_value));
        canvas.drawText(StrUtil.getOneDecimals(mValueStart - rowValue * 3), mBaseTextPaddingLeft, mMainHeight - textHeight + baseLine, mTextLeftPaint); //绘制最小值

        for (int i = 0; i < 3; i++) {

            if (i == 0) {
                String text = StrUtil.getOneDecimals(mValueStart + rowValue * 1.5);
                mTextLeftPaint.setColor(getResources().getColor(R.color.color_positive_value));
                canvas.drawText(text, mBaseTextPaddingLeft, (float) (rowSpace * 1.5 + baseLine / 2), mTextLeftPaint);

            } else if (i == 1) {
                String text = StrUtil.getOneDecimals(mValueStart);
                mTextLeftPaint.setColor(getResources().getColor(R.color.color_central_paint));
                canvas.drawText(text, mBaseTextPaddingLeft, fixTextY(rowSpace * 3), mTextLeftPaint);

            } else if (i == 2) {
                String text = StrUtil.getOneDecimals(mValueStart - rowValue * 1.5);
                mTextLeftPaint.setColor(getResources().getColor(R.color.color_negative_value));
                canvas.drawText(text, mBaseTextPaddingLeft,
                        (float) (mMainHeight - textHeight / 2 - rowSpace * 1.5 + baseLine / 2), mTextLeftPaint);

            }
        }

        //画右边的值
        mTextReightPaint.setTextAlign(Paint.Align.RIGHT);
        mTextReightPaint.setColor(getResources().getColor(R.color.color_positive_value));
        String text = StrUtil.floatToString((mValueMax - mValueStart) * 100f / mValueStart, 2) + "%";
        canvas.drawText("+" + text, mWidth - mBaseTextPaddingRight, baseLine, mTextReightPaint);

        mTextReightPaint.setColor(getResources().getColor(R.color.color_negative_value));
        text = StrUtil.floatToString(Math.abs(mValueMin - mValueStart) * 100f / mValueStart, 2) + "%";
        canvas.drawText("-" + text, mWidth - mBaseTextPaddingRight,
                mMainHeight - textHeight + baseLine, mTextReightPaint);

        for (int i = 0; i < 3; i++) {
            if (i == 0) {
                text = StrUtil.floatToString((rowValue * 1.5f) * 100f / mValueStart, 2) + "%";
                mTextReightPaint.setColor(getResources().getColor(R.color.color_positive_value));
                canvas.drawText("+" + text, mWidth - mBaseTextPaddingRight,
                        (float) (rowSpace * 1.5 + baseLine / 2), mTextReightPaint);

            } else if (i == 1) {
                text = "0";
                mTextReightPaint.setColor(getResources().getColor(R.color.color_central_paint));
                canvas.drawText(text, mWidth - mBaseTextPaddingRight,
                        fixTextY(rowSpace * 3), mTextReightPaint);

            } else if (i == 2) {
                text = StrUtil.floatToString((rowValue * 1.5f) * 100f / mValueStart, 2) + "%";
                mTextReightPaint.setColor(getResources().getColor(R.color.color_negative_value));
                canvas.drawText("-" + text, mWidth - mBaseTextPaddingRight,
                        (float) (mMainHeight - textHeight / 2 - rowSpace * 1.5 + baseLine / 2), mTextReightPaint);
            }
        }


        //画时间
        float y = mMainHeight + mVolumeHeight + mVolumeTextHeight + baseLine;
        mTextBottomPaint.setTextAlign(Paint.Align.LEFT);
        canvas.drawText(DateUtil.shortTimeFormat.format(mMainStartTime), mBaseTimePadding, y, mTextBottomPaint); //起始时间
        mCount = 0;
        for (int i = 0; i < mTimes.size() - 1; i++) {
            mCount += (mTimes.get(i).getEndDate().getTime() - mTimes.get(i).getStartDate().getTime()) / ONE_MINUTE;
            float x = mScaleX * mCount;
            canvas.drawText(DateUtil.shortTimeFormat.format(mTimes.get(i + 1).getStartDate().getTime()),
                    mBaseTimePadding + x - mTextBottomPaint.measureText(DateUtil.shortTimeFormat.format(mMainEndTime)),
                    y, mTextBottomPaint); //中间起始时间
        }
        canvas.drawText(DateUtil.shortTimeFormat.format(mMainEndTime),
                mWidth - mBaseTimePadding - mTextBottomPaint.measureText(DateUtil.shortTimeFormat.format(mMainEndTime)),
                y, mTextBottomPaint);//结束时间


//        float y = mMainHeight + mVolumeHeight + mVolumeTextHeight + baseLine;
//        mTextBottomPaint.setTextAlign(Paint.Align.LEFT);
//        mCount = 0;
//        if (mIndex == 1) {
//            canvas.drawText(DateUtil.getStringDateByLong(mMainStartTime.getTime(), 8), mBaseTimePadding, y, mTextBottomPaint); //起始时间
//            for (int i = 0; i < mTimes.size() - 1; i++) {
//                mCount += (mTimes.get(i).getEndDate().getTime() - mTimes.get(i).getStartDate().getTime()) / ONE_MINUTE;
//                float x = mScaleX * mCount;
//                mTextBottomPaint.setTextAlign(Paint.Align.CENTER);
//                canvas.drawText(DateUtil.getStringDateByLong(mTimes.get(i + 1).getStartDate().getTime(), 8),
//                        mBaseTimePadding + x,
//                        y, mTextBottomPaint); //中间起始时间
//            }
//            mTextBottomPaint.setTextAlign(Paint.Align.RIGHT);
//            canvas.drawText(DateUtil.getStringDateByLong(mMainEndTime.getTime(), 8),
//                    mWidth - mBaseTimePadding,
//                    y, mTextBottomPaint);//结束时间
//
//        } else { //大于1天时
//            String startStr = DateUtil.getStringDateByLong(mMainStartTime.getTime(), 8);
//            if (startStr.equals("21:00")) {
//                canvas.drawText(DateUtil.addOneDayDate(mMainStartTime.getTime() + 1), mBaseTimePadding, y, mTextBottomPaint); //起始时间
//            } else {
//                canvas.drawText(DateUtil.getStringDateByLong(mMainStartTime.getTime(), 9), mBaseTimePadding, y, mTextBottomPaint); //起始时间
//            }
//            for (int i = 0; i < mTimes.size() - 1; i++) {
//                mCount += (mTimes.get(i).getEndDate().getTime() - mTimes.get(i).getStartDate().getTime()) / ONE_MINUTE;
//                float x = mScaleX * mCount;
//                String dataStr = DateUtil.getStringDateByLong(mTimes.get(i + 1).getStartDate().getTime(), 8);
//
//                if (dataStr.equals(startStr)) { //上期所
//                    mTextBottomPaint.setTextAlign(Paint.Align.CENTER);
//                    if (startStr.equals("21:00")) {
//                        canvas.drawText(DateUtil.addOneDayDate(mTimes.get(i + 1).getStartDate().getTime()),
//                                mBaseTimePadding + x,
//                                y, mTextBottomPaint);
//                    } else {
//                        canvas.drawText(DateUtil.getStringDateByLong(mTimes.get(i + 1).getStartDate().getTime(), 9),
//                                mBaseTimePadding + x,
//                                y, mTextBottomPaint);
//                    }
//                }
//            }
//        }


    }


    /**
     * 修正y值
     */
    private float getY(float value) {
        return mMainHeight / 2 - (value - mValueStart) * mScaleY;
//        return (mValueMax - value) * mScaleY;
    }

    /**
     * 解决text居中的问题
     */
    public float fixTextY(float y) {
        Paint.FontMetrics fontMetrics = mTextLeftPaint.getFontMetrics();
        return (y + (fontMetrics.descent - fontMetrics.ascent) / 2 - fontMetrics.descent);
    }


}


