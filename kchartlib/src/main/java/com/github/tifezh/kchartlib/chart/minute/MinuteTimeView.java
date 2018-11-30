package com.github.tifezh.kchartlib.chart.minute;

import android.content.Context;
import android.graphics.Canvas;
import android.graphics.Color;
import android.graphics.Paint;
import android.graphics.RectF;
import android.support.v4.content.ContextCompat;
import android.util.AttributeSet;
import android.util.Log;

import com.github.tifezh.kchartlib.R;
import com.github.tifezh.kchartlib.chart.comInterface.IMinuteLine;
import com.github.tifezh.kchartlib.utils.DateUtil;
import com.github.tifezh.kchartlib.utils.DensityUtil;
import com.github.tifezh.kchartlib.utils.StrUtil;

import java.util.ArrayList;
import java.util.List;

/**
 * Description 分时图
 * Author puyantao
 * Email 1067899750@qq.com
 * Date 2018-10-17 11:23
 */

public class MinuteTimeView extends BaseMinuteView {
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

    private float mVolume = 0; //成交量
    private float mInterest = 0; //持仓量
    private float mVolumeMax = 0; //最大成交量
    private float mVolumeMin = 0; //最小成交量
    private float mInterestMax = 0; //最大持仓量
    private float mInterestMin = 0; //最小持仓量

    private float mMACDValueY = 0;//MACD Y轴

    private long mCount = 0;

    private float mVolumeTimeScaleY; //各时间点成交量缩放比
    private float mInterestTimeScaleY; //各时间点持仓量缩放比
    private float mMACDLineScaleY; //MACD指标线的缩放量
    private float mTextSize = 10;

    private int selectedIndex;
    private boolean isCJL = true; //是否为CJL图，默认true


    public MinuteTimeView(Context context) {
        super(context);
        initData();
    }

    public MinuteTimeView(Context context, AttributeSet attrs) {
        super(context, attrs);
        initData();
    }

    public MinuteTimeView(Context context, AttributeSet attrs, int defStyleAttr) {
        super(context, attrs, defStyleAttr);
        initData();
    }


    private void initData() {
        setDrawChildView(true);
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
//        selectedIndex = (int) (x * 1f / getX(mPoints.size() - 1) * (mPoints.size() - 1) + mPointWidth * 0.2f);
        selectedIndex = (int) (x * 1f / getX(mPoints.size() - 1) * (mPoints.size() - 1));
        if (selectedIndex < 0) {
            selectedIndex = 0;
        }
        if (selectedIndex > mPoints.size() - 1) {
            selectedIndex = mPoints.size() - 1;
        }
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
        mMACDValueY = 0;
        if (mPoints.size() <= 0) {
            return;
        }
        if (mPoints.size() > 0) {
            mValueMax = mPoints.get(0).getHighest();
            mValueMin = mPoints.get(0).getLowest();

            mVolumeMax = mPoints.get(0).getVolume();//成交量
            mVolume = mPoints.get(0).getVolume();
            mVolumeMin = mPoints.get(0).getVolume();

            mInterestMax = mPoints.get(0).getInterest(); //持仓量
            mInterest = mPoints.get(0).getInterest();
            mInterestMin = mPoints.get(0).getInterest(); //持仓量
        }

        for (int i = 0; i < mPoints.size(); i++) {
            IMinuteLine point = mPoints.get(i);
            mValueMax = Math.max(mValueMax, point.getHighest());
            mValueMin = Math.min(mValueMin, point.getLowest());

            mVolumeMax = Math.max(mVolumeMax, point.getVolume());
            mVolumeMin = Math.max(mVolumeMin, point.getVolume());
            mInterestMax = Math.max(mInterestMax, point.getInterest());
            mInterestMin = Math.min(mInterestMin, point.getInterest());

            //MACD Y轴
            mMACDValueY = Math.max(mMACDValueY, Math.max(Math.abs(point.getDea()),
                    Math.max(Math.abs(point.getDiff()), Math.abs(point.getMacd()))));
            Log.i("mMACDValueY :", mMACDValueY + "--; --" + i);

        }
        mMACDValueY = StrUtil.getFaveMultipleMinimum(Long.parseLong(StrUtil.getPositiveNumber(mMACDValueY)));
        Log.i("mMACDValue :", mMACDValueY + "");
        //最大值和开始值的单位差值
        float offsetValueMax = (Math.abs(mValueMax - mValueStart)) / (mGridRows / 2);
        float offsetValueMin = (Math.abs(mValueStart - mValueMin)) / (mGridRows / 2);

        //以开始的点为中点值   上下间隙多出20%
        float offset = (offsetValueMax > offsetValueMin ? offsetValueMax : offsetValueMin) * 1.2f;
        //坐标轴高度以开始的点对称
        mValueMax = mValueStart + offset * (mGridRows / 2);
        mValueMin = mValueStart - offset * (mGridRows / 2);
        //y轴的缩放值
        mScaleY = mMainHeight / (mValueMax - mValueMin);

        //判断最大值和最小值是否一致
        if (mValueMax == mValueMin) {
            //当最大值和最小值都相等的时候 分别增大最大值和 减小最小值
            mValueMax += Math.abs(mValueMax * 0.05f);
            mValueMin -= Math.abs(mValueMax * 0.05f);
            if (mValueMax == 0) {
                mValueMax = 1;
            }
        }

        if (mVolumeMax == 0) {
            mVolumeMax = 1;
        }


        //CJL右轴的缩放值
        float volume = Math.max(Math.abs(mVolumeMax - mVolume), Math.abs(mVolume - mVolumeMin));
        mVolumeMax = mVolume + volume;
        mVolumeMin = mVolume - volume;
        if (mVolumeMin < 0) {
            mVolumeMin = 0;
        }
        mVolumeTimeScaleY = mVolumeHeight / Math.abs(mVolumeMax - mVolumeMin);
        Log.i("---> VolumeScaleY ", mInterestTimeScaleY + "");

        //CJL左轴的缩放值
        float interest = Math.max(Math.abs(mInterestMax - mInterest), Math.abs(mInterest - mInterestMin));
        mInterestMax = mInterest + interest;
        mInterestMin = mInterest - interest;
        if (mInterestMin < 0) {
            mInterestMin = 0;
        }
        mInterestTimeScaleY = mVolumeHeight / Math.abs(mInterestMax - mInterestMin);
        Log.i("---> InterestScaleY ", mInterestTimeScaleY + "");

        //MACD右轴的缩放值
        mMACDLineScaleY = mVolumeHeight / (2 * mMACDValueY);

        //x轴的缩放值
        mScaleX = (float) (mWidth - mBaseTimePadding * 2) / getMaxPointCount(1);

        //设置主状图的宽度
        mPointWidth = (float) (mWidth - mBaseTimePadding * 2) / getMaxPointCount(1);
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

                canvas.drawLine(lastX + mBaseTimePadding - mScaleX / 2,
                        getY(lastPoint.getLast()),
                        curX + mBaseTimePadding - mScaleX / 2,
                        getY(curPoint.getLast()),
                        mPricePaint); //成交价

                if (isDrawChildView) {
                    if (isCJL) {
                        //CJL成交量(柱状图)
                        if (curPoint.getOpen() < curPoint.getClose()) {
                            mVolumePaint.setColor(getResources().getColor(R.color.color_positive_value));

                        } else if (curPoint.getOpen() > curPoint.getClose()) {
                            mVolumePaint.setColor(getResources().getColor(R.color.color_negative_value));

                        } else if (curPoint.getOpen() == curPoint.getClose()) {
                            mVolumePaint.setColor(getResources().getColor(R.color.chart_text));
                        }

                        canvas.drawLine(curX + mBaseTimePadding - mPointWidth * 0.5f,
                                mMainHeight + mVolumeTextHeight + mVolumeHeight,
                                curX + mBaseTimePadding  - mPointWidth * 0.5f,
                                getVolumeCJLY(curPoint.getVolume()),
                                mVolumePaint);

                    } else {
                        //MACD成交量(柱状图)
                        if (curPoint.getMacd() > 0) {
                            mVolumePaint.setColor(getResources().getColor(R.color.color_positive_value));
                            canvas.drawLine(curX + mBaseTimePadding  - mPointWidth * 0.5f,
                                    getMACDLineY(0),
                                    curX + mBaseTimePadding  - mPointWidth * 0.5f,
                                    getMACDLineY(curPoint.getMacd()),
                                    mVolumePaint);

                        } else if (curPoint.getMacd() < 0) {
                            mVolumePaint.setColor(getResources().getColor(R.color.color_negative_value));
                            canvas.drawLine(curX + mBaseTimePadding  - mPointWidth * 0.5f,
                                    getMACDLineY(0),
                                    curX + mBaseTimePadding  - mPointWidth * 0.5f,
                                    getMACDLineY(curPoint.getMacd()),
                                    mVolumePaint);

                        }

                    }
                }

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
            float x = getX(selectedIndex) + mBaseTimePadding  - mPointWidth * 0.5f;
            //轴线
            canvas.drawLine(x, 0, x, mMainHeight + mVolumeHeight + mVolumeTextHeight, mLinePaint);//Y
//            canvas.drawLine(0, getY(point.getLast()), mWidth, getY(point.getLast()), mLinePaint);//X


                drawMainSelector(selectedIndex, point, canvas);

        }
    }


    /**
     * draw选择器
     *
     * @param canvas
     */

    private void drawMainSelector(int selectedIndex, IMinuteLine point, Canvas canvas) {
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
        strings.add("价格");
        strings.add(point.getLast() + "");
        strings.add("均价");
        strings.add(point.getAverage() + "");

        strings.add("涨跌");
        strings.add(point.getUpdown() + "");
        strings.add(point.getPercent() + "");

        strings.add("持仓量");
        strings.add(point.getInterest() + "");
        strings.add(point.getChgInterest() + ""); //???

        strings.add("成交量");
        strings.add(point.getVolume() + "");
        strings.add(point.getChgInterest() + "");

        for (int i = 0; i < strings.size(); i++) {
            width = Math.max(width, mSelectorTextPaint.measureText(strings.get(i)));
        }
        width = width + dp2px(6) * 2;

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

        //绘制 MACD, DIFF, DEA, STICK 的指标
        if (isDrawChildView) {
            //GJL
            if (!isCJL) {
                drawMACDText(selectedIndex, point, canvas);
                invalidate();
            }
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
        String text = StrUtil.floatToString((mValueMax - mValueStart) * 100f / mValueStart) + "%";
        canvas.drawText("+" + text, mWidth - mBaseTextPaddingRight, baseLine, mTextReightPaint);

        mTextReightPaint.setColor(getResources().getColor(R.color.color_negative_value));
        text = StrUtil.floatToString(Math.abs(mValueMin - mValueStart) * 100f / mValueStart) + "%";
        canvas.drawText("-" + text, mWidth - mBaseTextPaddingRight,
                mMainHeight - textHeight + baseLine, mTextReightPaint);

        for (int i = 0; i < 3; i++) {
            if (i == 0) {
                text = StrUtil.floatToString((rowValue * 1.5f) * 100f / mValueStart) + "%";
                mTextReightPaint.setColor(getResources().getColor(R.color.color_positive_value));
                canvas.drawText("+" + text, mWidth - mBaseTextPaddingRight,
                        (float) (rowSpace * 1.5 + baseLine / 2), mTextReightPaint);

            } else if (i == 1) {
                text = "0";
                mTextReightPaint.setColor(getResources().getColor(R.color.color_central_paint));
                canvas.drawText(text, mWidth - mBaseTextPaddingRight,
                        fixTextY(rowSpace * 3), mTextReightPaint);

            } else if (i == 2) {
                text = StrUtil.floatToString((rowValue * 1.5f) * 100f / mValueStart) + "%";
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

        if (isDrawChildView) {
            //GJL
            if (isCJL) {
                drawCJL(canvas);
            } else {
                drawMACD(canvas);
            }
        }

    }

    //MACD , 存在问题
    private void drawMACD(Canvas canvas) {
        //上面文字
        mTextPaint.setColor(getResources().getColor(R.color.color_child_text));
        Paint.FontMetrics fm = mTextPaint.getFontMetrics();
        float textHeight = fm.descent - fm.ascent;
        float baseLine = (textHeight - fm.bottom - fm.top) / 2;
        mTextPaint.setTextAlign(Paint.Align.LEFT);
        canvas.drawText("MACD", mBaseTextPaddingLeft, mMainHeight + mVolumeTextHeight / 2 + textHeight - baseLine, mTextPaint); //存在问题??

        //绘制 MACD, DIFF, DEA, STICK 的指标
        drawMACDText(0, mPoints.get(mMACDClickPoint), canvas);

        //左边文字
        float rowChildSpace = mVolumeHeight / mGridChildRows;

        mTextPaint.setColor(getResources().getColor(R.color.color_candle_paint));
        canvas.drawText(StrUtil.getPositiveNumber(mMACDValueY), mBaseTextPaddingLeft,
                mMainHeight + baseLine + mVolumeTextHeight, mTextPaint); //绘制最大值
        canvas.drawText("0", mBaseTextPaddingLeft,
                mMainHeight + mVolumeTextHeight + fixTextY(rowChildSpace * 2), mTextPaint); //中间值
        canvas.drawText(StrUtil.getPositiveNumber(-mMACDValueY), mBaseTextPaddingLeft,
                mMainHeight - textHeight + baseLine + mVolumeTextHeight + mVolumeHeight, mTextPaint); //绘制最小值


        //DIFF线
        if (mPoints.size() > 0) {
            mPricePaint.setColor(getResources().getColor(R.color.chart_FFFFFF));
            mPricePaint.setStyle(Paint.Style.STROKE);
            IMinuteLine lastPoint = mPoints.get(0);
            float lastX = getX(0);


            for (int i = 0; i < mPoints.size(); i++) {
                IMinuteLine curPoint = mPoints.get(i);
                float curX = getX(i);

//                Log.i("diff --> :" , curPoint.getDiff() + "");
                canvas.drawLine(lastX + mBaseTimePadding - mScaleX / 2,
                        getMACDLineY(lastPoint.getDiff()),
                        curX + mBaseTimePadding  - mScaleX / 2,
                        getMACDLineY(curPoint.getDiff()),
                        mPricePaint); //成交价

                //给上一个只赋值
                lastPoint = curPoint;
                lastX = curX;
            }
        }

        //DEA线
        if (mPoints.size() > 0) {
            mPricePaint.setColor(getResources().getColor(R.color.color_cjl_line_paint));
            IMinuteLine lastPoint = mPoints.get(0);
            float lastX = getX(0);
            for (int i = 0; i < mPoints.size(); i++) {
                IMinuteLine curPoint = mPoints.get(i);
                float curX = getX(i);

//                Log.i("dea --> :" , curPoint.getDea() + "");
                canvas.drawLine(lastX + mBaseTimePadding  - mScaleX / 2,
                        getMACDLineY(lastPoint.getDea()),
                        curX + mBaseTimePadding  - mScaleX / 2,
                        getMACDLineY(curPoint.getDea()),
                        mPricePaint); //成交价

                //给上一个只赋值
                lastPoint = curPoint;
                lastX = curX;
            }
        }

    }

    //绘制 MACD, DIFF, DEA, STICK 的指标
    private void drawMACDText(int selectedIndex, IMinuteLine point, Canvas canvas) {
        mMACDClickPoint = selectedIndex;
        Paint.FontMetrics fm = mTextMACDPaint.getFontMetrics();
        float textHeight = fm.descent - fm.ascent;
        float baseLine = (textHeight - fm.bottom - fm.top) / 2;

        float x = dp2px(6) + mTextPaint.measureText("MACD   ") + mBaseTextPaddingLeft;
        float countX = (mWidth - x) / 4;
        mTextMACDPaint.setTextAlign(Paint.Align.LEFT);
        mTextMACDPaint.setColor(getResources().getColor(R.color.color_macd_text));
        canvas.drawText("MACD(12,26,9)", x,
                mMainHeight + mVolumeTextHeight / 2 + textHeight - baseLine, mTextMACDPaint); //MACD

        mTextMACDPaint.setColor(getResources().getColor(R.color.color_diff_text));
        canvas.drawText("DIFF:" + StrUtil.floatToString(point.getDiff()), x + countX,
                mMainHeight + mVolumeTextHeight / 2 + textHeight - baseLine, mTextMACDPaint); //DIFF

        mTextMACDPaint.setColor(getResources().getColor(R.color.color_dea_text));
        canvas.drawText("DEA:" + StrUtil.floatToString(point.getDea()), x + countX * 2,
                mMainHeight + mVolumeTextHeight / 2 + textHeight - baseLine, mTextMACDPaint); //DEA

        mTextMACDPaint.setColor(getResources().getColor(R.color.c6774FF));
        canvas.drawText("STICK:" + StrUtil.floatToString(point.getMacd()), x + countX * 3,
                mMainHeight + mVolumeTextHeight / 2 + textHeight - baseLine, mTextMACDPaint); //STICK
    }

    //GJL
    private void drawCJL(Canvas canvas) {
        //上面文字
        mTextPaint.setColor(getResources().getColor(R.color.color_child_text));
        Paint.FontMetrics fm = mTextPaint.getFontMetrics();
        float textHeight = fm.descent - fm.ascent;
        float baseLine = (textHeight - fm.bottom - fm.top) / 2;
        mTextPaint.setTextAlign(Paint.Align.LEFT);
        canvas.drawText("CJL", mBaseTextPaddingLeft, mMainHeight + mVolumeTextHeight / 2 + textHeight - baseLine, mTextPaint);//存在问题 ???

        //左边文字
        float rowChildSpace = mVolumeHeight / mGridChildRows;
        mTextPaint.setColor(getResources().getColor(R.color.color_candle_paint));
        canvas.drawText(StrUtil.getFaveMultipleMinimum((long) mVolumeMax) + "",
                mBaseTextPaddingLeft, mMainHeight + baseLine + mVolumeTextHeight, mTextPaint); //绘制最大值
        String text = StrUtil.getPositiveNumber(mVolume);
        canvas.drawText(text, mBaseTextPaddingLeft,
                mMainHeight + mVolumeTextHeight + fixTextY(rowChildSpace * 2), mTextPaint); //中间值
        canvas.drawText(StrUtil.getFaveMultipleMinimum((long) mVolumeMin) + "", mBaseTextPaddingLeft,
                mMainHeight - textHeight + baseLine + mVolumeTextHeight + mVolumeHeight, mTextPaint); //绘制最小值

        //GJL线
        if (mPoints.size() > 0) {
            mPricePaint.setColor(getResources().getColor(R.color.color_cjl_line_paint));
            IMinuteLine lastPoint = mPoints.get(0);
            float lastX = getX(0);
            for (int i = 0; i < mPoints.size(); i++) {
                IMinuteLine curPoint = mPoints.get(i);
                float curX = getX(i);

                canvas.drawLine(lastX + mBaseTimePadding - mScaleX / 2,
                        getInterestCJLY(lastPoint.getInterest()),
                        curX + mBaseTimePadding - mScaleX / 2,
                        getInterestCJLY(curPoint.getInterest()),
                        mPricePaint); //成交价

                //给上一个只赋值
                lastPoint = curPoint;
                lastX = curX;
            }
        }


        //右边文字
        mTextPaint.setTextAlign(Paint.Align.RIGHT);
        mTextPaint.setColor(getResources().getColor(R.color.color_candle_paint));
        canvas.drawText(StrUtil.getZeroMultipleMinimum((long) mInterestMax) + "", mWidth - mBaseTextPaddingRight,
                mMainHeight + baseLine + mVolumeTextHeight, mTextPaint); //绘制最大值
        String text1 = StrUtil.getPositiveNumber(mInterest);
        canvas.drawText(text1, mWidth - mBaseTextPaddingRight,
                mMainHeight + mVolumeTextHeight + fixTextY(rowChildSpace * 2), mTextPaint); //中间值
        canvas.drawText(StrUtil.getZeroMultipleMinimum((long) mInterestMin) + "", mWidth - mBaseTextPaddingRight,
                mMainHeight - textHeight + baseLine + mVolumeTextHeight + mVolumeHeight, mTextPaint); //绘制最小值


    }


    //CJL和MACL之间的切换
    @Override
    protected void jumpToCJLAndMACL(float downX, float downY) {
        //点击子试图判断
        if (downX > 0 && downX < mWidth) {
            if (downY > mMainHeight + mVolumeTextHeight && downY < mMainHeight + mVolumeTextHeight + mVolumeHeight) {
                Log.d("--->", "x = " + downX + ";" + "y = " + downY);
                isCJL = !isCJL;
                invalidate();
            }

        }

        //点击文字判断
        if (downX > 0 && downX < mBaseTextPaddingLeft + mTextPaint.measureText("MACD") + 10) {
            if (downY > mMainHeight && downY < mMainHeight + mVolumeTextHeight) {
                Log.d("--->", "x = " + downX + ";" + "y = " + downY);
                isCJL = !isCJL;
                invalidate();
            }
        }

    }

    /**
     * 修正y值
     */
    private float getY(float value) {
        return (mValueMax - value) * mScaleY;
    }

    //CJL柱形
    private float getVolumeCJLY(float value) {
        return mMainHeight + mVolumeTextHeight + mVolumeHeight - (value - mVolumeMin) * mVolumeTimeScaleY;
    }

    //CJL线
    private float getInterestCJLY(float value) {
        return mMainHeight + mVolumeTextHeight + mVolumeHeight - (value - mInterestMin) * mInterestTimeScaleY;
    }


    //MACD指标线
    private float getMACDLineY(float value) {
        if (value == 0) {
            return mMainHeight + mVolumeTextHeight + mVolumeHeight / 2;

        } else if (value > 0) {
            return mMainHeight + mVolumeTextHeight + mVolumeHeight / 2 - value * mMACDLineScaleY;

        } else if (value < 0) {
            return mMainHeight + mVolumeTextHeight + mVolumeHeight / 2 - value * mMACDLineScaleY;
        } else {
            return 0;
        }
    }


    /**
     * 解决text居中的问题
     */
    public float fixTextY(float y) {
        Paint.FontMetrics fontMetrics = mTextLeftPaint.getFontMetrics();
        return (y + (fontMetrics.descent - fontMetrics.ascent) / 2 - fontMetrics.descent);
    }
}
















