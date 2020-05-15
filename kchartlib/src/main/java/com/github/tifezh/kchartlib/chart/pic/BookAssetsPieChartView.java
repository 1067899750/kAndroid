package com.github.tifezh.kchartlib.chart.pic;

import android.animation.ValueAnimator;
import android.content.Context;
import android.content.res.TypedArray;
import android.graphics.Canvas;
import android.graphics.Color;
import android.graphics.Paint;
import android.graphics.Path;
import android.graphics.Point;
import android.graphics.RectF;
import android.support.annotation.NonNull;
import android.support.annotation.Nullable;
import android.text.TextPaint;
import android.util.AttributeSet;
import android.widget.FrameLayout;


import com.github.tifezh.kchartlib.R;
import com.github.tifezh.kchartlib.utils.StrUtil;

import java.util.ArrayList;
import java.util.List;


/**
 * @author puyantao
 * @description :
 * @date 2020/4/26
 */
public class BookAssetsPieChartView extends FrameLayout {
    /**
     * 画笔
     */
    private Paint mPieChartPaint;
    private Float mPieChartWidth;
    /**
     * 圆心位置
     */
    private Point centerPosition;
    /**
     * 半径
     */
    private float raduis;
    /**
     * 声明外圆边界矩形
     */
    private RectF mRectF;
    /**
     * 声明内圆边界矩形
     */
    private RectF mInRectF;
    /**
     * 数据集合
     */
    private List<BookPieChartData> mDataList = new ArrayList();
    /**
     * 数据总数
     */
    private float mTotalNum;
    /**
     * 开始角度
     */
    private float mStartAngle;
    /**
     * 扫过的角度
     */
    private Float mSweepAngle;
    /**
     * 动画时间
     */
    private int mAnimTime;
    /**
     * 属性动画
     */
    private ValueAnimator mAnimator;
    /**
     * 动画进度
     */
    private float mAnimationPercent;
    /**
     * 字体
     */
    private TextPaint mDataPaint;
    private float mDataSize = 8;
    private int mDataColor;

    private float mRingWidth;
    private float mPadding;
    private float mTextPadding;
    private boolean isRing = false;
    /**
     * 内园颜色
     */
    private int mRingBgColor;

    /**
     * 横向间距
     */
    private float mHorizontalMargin;
    /**
     * 纵向间距
     */
    private float mVerticalMargin;
    /**
     * 指向说明线
     */
    private Paint mPointingPaint;

    private float mBaseHeight;
    private float mBaseWidth;
    private float mHeightMessageLeftTop;
    private int mLeftTopDataCount = 0;
    private float mHeightMessageRightTop;
    private int mRightTopDataCount = 0;

    private float mHeightMessageLeftBottom;
    private int mLeftBottomDataCount = 0;
    private float mHeightMessageRightBottom;
    private int mRightBottomDataCount = 0;

    private float mPointingWidth;
    private int mPointingColor;
    private Context mContext;


    public BookAssetsPieChartView(@NonNull Context context) {
        this(context, null);
    }

    public BookAssetsPieChartView(Context context, @Nullable AttributeSet attrs) {
        this(context, attrs, 0);
    }

    public BookAssetsPieChartView(Context context, @Nullable AttributeSet attrs, int defStyleAttr) {
        super(context, attrs, defStyleAttr);
        this.mContext = context;
        initView(attrs);
    }

    private void initView(AttributeSet attrs) {
        //设置动画进度
        mAnimationPercent = 0f;
        //初始化属性动画
        mAnimator = new ValueAnimator();
        //初始化圆心属性
        centerPosition = new Point();
        mRectF = new RectF();
        mInRectF = new RectF();
        //初始化属性
        initAttrs(attrs, mContext);
        //初始化画笔
        initPaint();
        mPadding = dip2px(mContext, 45);
        mTextPadding = dip2px(mContext, 20);
    }

    private void initAttrs(AttributeSet attrs, Context context) {
        TypedArray typedArray = context.obtainStyledAttributes(attrs, R.styleable.MyPieChartView);
        mPieChartWidth = typedArray.getDimension(R.styleable.MyPieChartView_pieChartWidth, 20F);

        mDataColor = typedArray.getColor(R.styleable.MyPieChartView_dataColor, Color.WHITE);

        mHorizontalMargin = typedArray.getDimension(R.styleable.MyPieChartView_horiMargin, 0F);
        mVerticalMargin = typedArray.getDimension(R.styleable.MyPieChartView_verticalMargin, 0F);

        mAnimTime = typedArray.getInt(R.styleable.MyPieChartView_animTime, 0);

        mPointingColor = typedArray.getColor(R.styleable.MyPieChartView_pointingColor, Color.GRAY);
        mPointingWidth = typedArray.getDimension(R.styleable.MyPieChartView_pointingWidth, 1F);

        mRingWidth = typedArray.getDimension(R.styleable.MyPieChartView_ringWidth, 25F);
        isRing = typedArray.getBoolean(R.styleable.MyPieChartView_isRing, false);
        mRingBgColor = typedArray.getColor(R.styleable.MyPieChartView_ringBgColor, Color.WHITE);
        typedArray.recycle();
    }


    private void initPaint() {
        mPieChartPaint = new Paint();
        //是否开启抗锯齿
        mPieChartPaint.setAntiAlias(true);
        //防抖动
        mPieChartPaint.setDither(true);
        //画笔样式 STROKE 只绘制图形轮廓（描边） FILL 只绘制图形内容 FILL_AND_STROKE 既绘制轮廓也绘制内容
        mPieChartPaint.setStyle(Paint.Style.FILL);
        //画笔宽度
        mPieChartPaint.setStrokeWidth(mPieChartWidth);
        ///笔刷样式
        // 当画笔样式为STROKE或FILL_OR_STROKE时，
        mPieChartPaint.setStrokeCap(Paint.Cap.SQUARE);
        // 设置笔刷的图形样式，如圆形样式Cap.ROUND,或方形样式Cap.SQUARE
        mPieChartPaint.setColor(Color.RED);

        //数字
        mDataPaint = new TextPaint();
        mDataPaint.setDither(true);
        mDataPaint.setAntiAlias(true);
        mDataPaint.setTextSize(sp2px(mContext, mDataSize));
        mDataPaint.setColor(mDataColor);

        mPointingPaint = new Paint();
        mPointingPaint.setDither(true);
        mPointingPaint.setAntiAlias(true);
        mPointingPaint.setStyle(Paint.Style.STROKE);
        //从中间向两边绘制，不需要再次计算文字
        mPointingPaint.setStrokeWidth(mPointingWidth);
    }


    @Override
    protected void onMeasure(int widthMeasureSpec, int heightMeasureSpec) {
        super.onMeasure(widthMeasureSpec, heightMeasureSpec);
    }

    @Override
    protected void onSizeChanged(int w, int h, int oldw, int oldh) {
        super.onSizeChanged(w, h, oldw, oldh);
        mBaseHeight = h;
        mBaseWidth = w;
        //圆心位置
        centerPosition.x = w / 2;
        centerPosition.y = h / 2;
        //半径
        int minWidth = Math.min(w - getPaddingLeft() - getPaddingRight(),
                h - getPaddingBottom() - getPaddingTop());
        raduis = (minWidth / 2) - mPadding;
        //外园矩形坐标
        mRectF.left = centerPosition.x - raduis;
        mRectF.top = centerPosition.y - raduis;
        mRectF.right = centerPosition.x + raduis;
        mRectF.bottom = centerPosition.y + raduis;
        //内园矩形坐标
        mInRectF.left = mRectF.left + mRingWidth;
        mInRectF.top = mRectF.top + mRingWidth;
        mInRectF.right = mRectF.right - mRingWidth;
        mInRectF.bottom = mRectF.bottom - mRingWidth;
    }

    @Override
    protected void onLayout(boolean changed, int left, int top, int right, int bottom) {
        super.onLayout(changed, left, top, right, bottom);
    }

    @Override
    protected void onDraw(Canvas canvas) {
        super.onDraw(canvas);
    }

    /**
     * onDraw()先于dispatchDraw()执行,用于本身控件的绘制,dispatchDraw()用于子控件的绘制
     * onDraw()绘制的内容可能会被子控件覆盖而dispatchDraw()是子控件的绘制,所以是覆盖在onDraw()上的
     *
     * @param canvas
     */
    @Override
    protected void dispatchDraw(Canvas canvas) {
        super.dispatchDraw(canvas);
        canvas.save();
        mStartAngle = -90f;
        mSweepAngle = 0f;

        mRightTopDataCount = 0;
        mRightBottomDataCount = 0;
        mLeftTopDataCount = 0;
        mLeftBottomDataCount = 0;
        for (int i = 0; i < mDataList.size(); i++) {
            mStartAngle = mStartAngle + mSweepAngle;
            mSweepAngle = (mDataList.get(i).getNum() / mTotalNum) * 360 * mAnimationPercent;
            if (mStartAngle + mSweepAngle / 2 >= 270 || mStartAngle + mSweepAngle / 2 <= 0) {
                mRightTopDataCount++;

            } else if (mStartAngle + mSweepAngle / 2 >= 0 && mStartAngle + mSweepAngle / 2 <= 90) {
                mRightBottomDataCount++;

            } else if (mStartAngle + mSweepAngle / 2 >= 90 && mStartAngle + mSweepAngle / 2 <= 180) {
                mLeftBottomDataCount++;

            } else if (mStartAngle + mSweepAngle / 2 >= 180 && mStartAngle + mSweepAngle / 2 <= 270) {
                mLeftTopDataCount++;

            }
        }

        mHeightMessageRightTop = (mBaseHeight / 2 - mTextPadding) / mRightTopDataCount;
        mHeightMessageRightBottom = (mBaseHeight / 2 - mTextPadding) / mRightBottomDataCount;
        mHeightMessageLeftTop = (mBaseHeight / 2 - mTextPadding) / mLeftTopDataCount;
        mHeightMessageLeftBottom = (mBaseHeight / 2 - mTextPadding) / mLeftBottomDataCount;

        mStartAngle = -90f;
        mSweepAngle = 0f;
        mRightTopDataCount = 0;
        mRightBottomDataCount = 0;
        mLeftTopDataCount = 0;
        mLeftBottomDataCount = 0;
        //绘制数字
        drawText(canvas);

        mStartAngle = -90f;
        mSweepAngle = 0f;
        mRightTopDataCount = 0;
        mRightBottomDataCount = 0;
        mLeftTopDataCount = 0;
        mLeftBottomDataCount = 0;
        for (int i = 0; i < mDataList.size(); i++) {
            mPieChartPaint.setColor(mDataList.get(i).getColor());
            mSweepAngle = (mDataList.get(i).getNum() / mTotalNum) * 360 * mAnimationPercent;
            //外圆
            canvas.drawArc(mRectF, mStartAngle, mSweepAngle, true, mPieChartPaint);
            //内圆
            if (isRing) {
                mPieChartPaint.setColor(mRingBgColor);
                canvas.drawArc(mInRectF, mStartAngle - 1, mSweepAngle + 1, true, mPieChartPaint);
            }
            mStartAngle = mStartAngle + mSweepAngle;
            drawLine(canvas, i);
        }
        canvas.restore();
    }


    /**
     * 绘制文字
     */
    private void drawText(Canvas canvas) {
        for (int i = 0; i < mDataList.size(); i++) {
            String nameStr = mDataList.get(i).getName() + StrUtil.deleteEndSurplusZero(
                    StrUtil.floatToString(mDataList.get(i).getNum() * 100 / mTotalNum, 4)) + "%";
            mStartAngle = mStartAngle + mSweepAngle;
            mSweepAngle = (mDataList.get(i).getNum() / mTotalNum) * 360 * mAnimationPercent;
            float line = dip2px(mContext, 22);

            if (mStartAngle + mSweepAngle / 2 >= 270 || mStartAngle + mSweepAngle / 2 <= 0) {
                mDataPaint.setTextAlign(Paint.Align.LEFT);
                canvas.drawText(nameStr,
                        mRectF.right + line,
                        (float) (mTextPadding + mHeightMessageRightTop * (mRightTopDataCount + 0.5) - getFontHeight(mDataPaint) / 2),
                        mDataPaint);
                mRightTopDataCount++;

            } else if (mStartAngle + mSweepAngle / 2 >= 0 && mStartAngle + mSweepAngle / 2 <= 90) {
                mDataPaint.setTextAlign(Paint.Align.LEFT);
                canvas.drawText(nameStr,
                        mRectF.right + line,
                        (float) (mBaseHeight / 2 + mHeightMessageRightBottom * (mRightBottomDataCount + 0.5) - getFontHeight(mDataPaint) / 2),
                        mDataPaint);
                mRightBottomDataCount++;

            } else if (mStartAngle + mSweepAngle / 2 >= 90 && mStartAngle + mSweepAngle / 2 <= 180) {
                mDataPaint.setTextAlign(Paint.Align.RIGHT);
                canvas.drawText(nameStr,
                        mRectF.left - line,
                        (float) (mBaseHeight - mTextPadding - mHeightMessageLeftBottom * (mLeftBottomDataCount + 0.5) - getFontHeight(mDataPaint) / 2),
                        mDataPaint);
                mLeftBottomDataCount++;

            } else if (mStartAngle + mSweepAngle / 2 >= 180 && mStartAngle + mSweepAngle / 2 <= 270) {
                mDataPaint.setTextAlign(Paint.Align.RIGHT);
                canvas.drawText(nameStr,
                        mRectF.left - line,
                        (float) (mBaseHeight / 2 - mHeightMessageLeftTop * (mLeftTopDataCount + 0.5) - getFontHeight(mDataPaint) / 2),
                        mDataPaint);
                mLeftTopDataCount++;

            }
        }

    }

    /**
     * 指向说明
     *
     * @param canvas
     */
    private void drawLine(Canvas canvas, int i) {
        float xP = (float) (centerPosition.x + raduis *
                Math.sin(Math.toRadians((90 + mStartAngle - mSweepAngle / 2))));
        float yP = (float) (centerPosition.y - raduis *
                Math.cos(Math.toRadians((90 + mStartAngle - mSweepAngle / 2))));

        float xEdP = (float) (centerPosition.x + (raduis + dip2px(mContext, 25)) *
                Math.sin(Math.toRadians((90 + mStartAngle - mSweepAngle / 2))));
        float yEdP = (float) (centerPosition.y - (raduis + dip2px(mContext, 25)) *
                Math.cos(Math.toRadians((90 + mStartAngle - mSweepAngle / 2))));

        mPointingPaint.setColor(mDataList.get(i).getColor());
        int len1 = dip2px(mContext, 5);
        int len2 = dip2px(mContext, 22);
        if (mStartAngle - mSweepAngle / 2 >= 270 || mStartAngle - mSweepAngle / 2 <= 0) {
            //right top
            Path mPath = new Path();
            mPath.moveTo(xP, yP);
            mPath.lineTo(mRectF.right + len1, yP);
            mPath.lineTo(mRectF.right + len2,
                    (float) (mTextPadding + mHeightMessageRightTop * (mRightTopDataCount + 0.5) - getFontHeight(mDataPaint)));
            canvas.drawPath(mPath, mPointingPaint);
            mRightTopDataCount++;

        } else if (mStartAngle - mSweepAngle / 2 >= 0 && mStartAngle - mSweepAngle / 2 <= 90) {
            //right bottom
            Path mPath = new Path();
            mPath.moveTo(xP, yP);
            mPath.lineTo(mRectF.right + len1, yP);
            mPath.lineTo(mRectF.right + len2,
                    (float) (mBaseHeight / 2 + mHeightMessageRightBottom * (mRightBottomDataCount + 0.5) - getFontHeight(mDataPaint)));
            canvas.drawPath(mPath, mPointingPaint);
            mRightBottomDataCount++;

        } else if (mStartAngle - mSweepAngle / 2 >= 90 && mStartAngle - mSweepAngle / 2 <= 180) {
            //left bottom
            Path mPath = new Path();
            mPath.moveTo(xP, yP);
            mPath.lineTo(mRectF.left - len1, yP);
            mPath.lineTo(mRectF.left - len2,
                    (float) (mBaseHeight - mTextPadding - mHeightMessageLeftBottom * (mLeftBottomDataCount + 0.5) - getFontHeight(mDataPaint)));
            canvas.drawPath(mPath, mPointingPaint);
            mLeftBottomDataCount++;

        } else if (mStartAngle - mSweepAngle / 2 >= 180 && mStartAngle - mSweepAngle / 2 <= 270) {
            //left top
            Path mPath = new Path();
            mPath.moveTo(xP, yP);
            mPath.lineTo(mRectF.left - len1, yP);
            mPath.lineTo(mRectF.left - len2,
                    (float) (mBaseHeight / 2 - mHeightMessageLeftTop * (mLeftTopDataCount + 0.5) - getFontHeight(mDataPaint)));
            canvas.drawPath(mPath, mPointingPaint);
            mLeftTopDataCount++;
        }

    }

    /**
     * 动画
     */
    private void startAnim(int animTime) {
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
     * 设置数据
     */
    public BookAssetsPieChartView setDataList(List<BookPieChartData> dataList) {
        mTotalNum = 0.0f;
        mDataList.clear();
        if (dataList.size() > 0) {
            for (int i = 0; i < dataList.size(); i++) {
                mTotalNum = dataList.get(i).getNum() + mTotalNum;
            }
        }
        mDataList.addAll(dataList);
        startAnim(mAnimTime);
        invalidate();
        return this;
    }


    public int dip2px(Context context, float dipValue) {
        final float scale = context.getResources().getDisplayMetrics().density;
        return (int) (dipValue * scale + 0.5f);
    }

    public int sp2px(Context context, float spValue) {
        final float fontScale = context.getResources().getDisplayMetrics().scaledDensity;
        return (int) (spValue * fontScale + 0.5f);
    }

    /**
     * 高度
     *
     * @param paint
     * @return
     */
    public float getFontBaseLineHeight(Paint paint) {
        Paint.FontMetrics fm = paint.getFontMetrics();
        float textHeight = fm.descent - fm.ascent;
        float baseLine = (textHeight - fm.bottom - fm.top) / 2;
        return baseLine;
    }

    /**
     * 文字的高度
     *
     * @param paint
     * @return
     */
    public float getFontHeight(Paint paint) {
        Paint.FontMetrics fm = paint.getFontMetrics();
        float textHeight = fm.descent - fm.ascent;
        return textHeight;
    }

    /**
     * 文字宽度
     *
     * @param paint
     * @param text
     * @return
     */
    public float getFontWidth(Paint paint, String text) {
        return paint.measureText(text);
    }


}


















