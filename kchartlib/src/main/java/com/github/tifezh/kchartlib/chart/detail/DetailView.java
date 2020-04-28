package com.github.tifezh.kchartlib.chart.detail;

import android.animation.ValueAnimator;
import android.content.Context;
import android.graphics.Canvas;
import android.graphics.Paint;
import android.graphics.RectF;
import android.util.AttributeSet;

import com.github.tifezh.kchartlib.R;
import com.github.tifezh.kchartlib.chart.comInterface.ILem;
import com.github.tifezh.kchartlib.chart.comInterface.IMinuteLine;
import com.github.tifezh.kchartlib.utils.DateUtil;
import com.github.tifezh.kchartlib.utils.DensityUtil;
import com.github.tifezh.kchartlib.utils.StrUtil;

import java.util.ArrayList;
import java.util.List;

/**
 * @Describe 明细试图
 * @Author puyantao
 * @Email 1067899750@qq.com
 * @create 2019/5/20 10:59
 */
public class DetailView extends BaseDetailView {
    private Paint mTextPaint = new Paint(Paint.ANTI_ALIAS_FLAG); //文字
    private Paint mLinePaint = new Paint(Paint.ANTI_ALIAS_FLAG); //线
    private Paint mSelectorBackgroundPaint = new Paint(Paint.ANTI_ALIAS_FLAG);

    private float mValueMin; //Volume最小值
    private float mValueMax; //Volume最大值
    private float mCurveScaleY = 1; //Y轴单位量
    private float mVolumeScaleY = 1; //Y轴单位量
    private float mScaleX = 1; //x轴的单位量
    private int selectedIndex = 0;

    private ValueAnimator mAnimator;
    private float mAnimationPercent;

    public DetailView(Context context) {
        super(context);
        initView();
    }

    public DetailView(Context context, AttributeSet attrs) {
        super(context, attrs);
        initView();
    }

    public DetailView(Context context, AttributeSet attrs, int defStyleAttr) {
        super(context, attrs, defStyleAttr);
        initView();
    }


    private void initView() {
        mTextPaint.setTextSize(sp2px(10)); //文字

        mLinePaint.setStrokeWidth(dp2px(1)); //线
    }

    @Override
    protected void notifyChanged() {
        if (mPoints.size() <= 0) {
            invalidate();
            return;
        }

        if (mPoints.size() > 0) {
            mValueMax = Float.MIN_VALUE;
            mValueMin = Float.MAX_VALUE;
        }

        for (int i = 0; i < mPoints.size(); i++) {
            IDetailLine point = mPoints.get(i);
            mValueMax = Math.max(mValueMax, point.getValue());
            mValueMin = Math.min(mValueMin, point.getValue());
        }

        mValueMax = StrUtil.getLemMultipleMinimum(mValueMax, 50);
        //y Volume 轴的缩放值
        mVolumeScaleY = mBaseHeight / Math.abs(mValueMax - 0);

        //x轴的缩放值
        mScaleX = mBaseWidth / (mPointCount - 1);

        startAnim(3000);
    }

    @Override
    protected void calculateSelectedX(float x) {
//        Log.i("---> ； x ：", x + "");
        selectedIndex = (int) (x * 1f / getX(mPoints.size() - 1) * (mPoints.size() - 1) + 0.5f);
        if (selectedIndex < 0) {
            selectedIndex = 0;
        }
        if (selectedIndex > mPoints.size() - 1) {
            selectedIndex = mPoints.size() - 1;
        }
    }


    @Override
    protected void onDraw(Canvas canvas) {
        super.onDraw(canvas);
        if (mPoints.size() <= 0 || mWidth == 0 || mHeight == 0) {
            return;
        }
        drawLine(canvas); //绘制线
        drawText(canvas); //绘制文字


        //画指示线
        if (isLongPress || !isClosePress) {
            if (selectedIndex >= mPoints.size() || selectedIndex < 0 || mPoints.size() == 0) {
                return;
            }
            IDetailLine point = mPoints.get(selectedIndex);
            float x = getX(selectedIndex);
            //轴线
            canvas.drawLine(x, mTopPadding, x, mHeight - mBottomPadding, mLinePaint);//Y
//            canvas.drawLine(0, getY(point.getLast()), mWidth, getY(point.getLast()), mLinePaint);//X

            drawSelector(selectedIndex, point, canvas);
        }
    }


    private void drawLine(Canvas canvas) {
        //绘制平均线和成交价
        if (mPoints.size() > 0) {
            mLinePaint.setColor(getResources().getColor(R.color.chart_FFFFFF));
            IDetailLine lastPoint = mPoints.get(0);
            float lastX = getX(0);
            for (int i = 0; i < mPoints.size() * mAnimationPercent; i++) {
                IDetailLine curPoint = mPoints.get(i);
                float curX = getX(i);

                canvas.drawLine(lastX,
                        getY(lastPoint.getValue()),
                        curX,
                        getY(curPoint.getValue()),
                        mLinePaint); //成交价

                //给上一个只赋值
                lastPoint = curPoint;
                lastX = curX;
            }
        }
    }

    private void drawText(Canvas canvas) {
        float baseLine = getFontBaseLineHeight(mTextPaint);
        float textHeight = getFontHeight(mTextPaint);

        int valueCount = (int) (mValueMax / 50);

        float rowValue = (float) StrUtil.getAndOnePositiveNumber((mValueMax - 0) / (valueCount - 1));
        float rowSpace = mBaseHeight / valueCount;

        float textWeight = mTextPaint.measureText(StrUtil.getPositiveNumber(mValueMax));

        //画左边的值
        mTextPaint.setTextAlign(Paint.Align.RIGHT);
        mTextPaint.setColor(getResources().getColor(R.color.c000000));
        canvas.drawText(StrUtil.getPositiveNumber(mValueMax), mBasePaddingLeft - 5, baseLine + mTopPadding, mTextPaint);

        for (int i = 0; i < valueCount; i++) {
            canvas.drawText(StrUtil.getPositiveNumber(50 * i),
                    mBasePaddingLeft - 5,
                    mHeight - mBottomPadding - textHeight + baseLine - rowSpace * i,
                    mTextPaint);
        }


        //画时间
        mTextPaint.setTextAlign(Paint.Align.CENTER);
        mTextPaint.setColor(getResources().getColor(R.color.c000000));
        float y = mHeight - mBottomPadding + baseLine;
        float dataWeight = mTextPaint.measureText(StrUtil.getPositiveNumber(mPoints.get((int) mPointCount - 1).getDate()));
        canvas.drawText(StrUtil.getPositiveNumber(1), getX(0), y, mTextPaint); //起始时间
        int dataCount = (int) (mPointCount / 5);
        for (int i = 1; i < dataCount; i++) {
            canvas.drawText(StrUtil.getPositiveNumber(mPoints.get(5 * i - 1).getDate()),
                    getX(5 * i - 1),
                    y, mTextPaint); //中间起始时间
        }
        canvas.drawText(StrUtil.getPositiveNumber(mPoints.get((int) mPointCount - 1).getDate()),
                mWidth - mBasePaddingRight - dataWeight / 2,
                y, mTextPaint);//结束时间

    }

    /**
     * draw选择器
     *
     * @param canvas
     */
    private void drawSelector(int selectedIndex, IDetailLine point, Canvas canvas) {
        Paint.FontMetrics metrics = mTextPaint.getFontMetrics();
        float textHeight = metrics.descent - metrics.ascent;

        float padding = DensityUtil.dp2px(5);
        float margin = DensityUtil.dp2px(5);
        float width = 0;
        float left = 5;
        float top = 10;
        float bottom = 10;

        List<String> strings = new ArrayList<>();
        strings.add("时间");
        strings.add(point.getDate() + "");

        strings.add("涨跌");
        strings.add(StrUtil.floatToString(point.getValue(), 2));

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
        for (String s : strings) {
            mTextPaint.setTextAlign(Paint.Align.LEFT);
            mTextPaint.setColor(getResources().getColor(R.color.color_text_positive_paint));
            canvas.drawText(s, left + padding, y, mTextPaint);
            y += textHeight + padding;
        }

    }

    /**
     * 动画
     */
    private void startAnim(final int animTime) {
        mAnimator = ValueAnimator.ofFloat(0f, 1f);
        mAnimator.setDuration(animTime);
        mAnimator.addUpdateListener(new ValueAnimator.AnimatorUpdateListener() {
            @Override
            public void onAnimationUpdate(ValueAnimator animation) {
                mAnimationPercent = (float) animation.getAnimatedValue();
                postInvalidate();
            }
        });
        mAnimator.start();
    }


    /**
     * 获取最大能有多少个点
     */
    public long getDataPointCount() {
        return mPoints.size() / 5 + 1;
    }

    private float getX(int i) {
        return mBasePaddingLeft + mScaleX * i;
    }

    /**
     * 修正y值
     */
    private float getY(float value) {
        return mTopPadding + (mValueMax - value) * mVolumeScaleY;
    }

}



















