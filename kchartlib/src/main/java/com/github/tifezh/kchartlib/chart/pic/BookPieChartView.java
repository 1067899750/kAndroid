package com.github.tifezh.kchartlib.chart.pic;

import android.animation.ValueAnimator;
import android.content.Context;
import android.content.res.TypedArray;
import android.graphics.Canvas;
import android.graphics.Color;
import android.graphics.Paint;
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
public class BookPieChartView extends FrameLayout {
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
    private float dataRaduis;
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
    private float mDataSize;
    private int mDataColor;

    private float mRingWidth;
    private float mPadding;
    private boolean isRing = false;
    /**
     * 内园颜色
     */
    private int mRingBgColor;
    /**
     * 单位
     */
    private TextPaint mUnitPaint;
    private float mUnitSize;
    private int mUnitColor;
    /**
     * 样式选择
     */
    private BookPieChartType mType = BookPieChartType.CONTENT_PERCENT;
    /**
     * 布局样式
     * default 普通样式  vertical 竖向布局  horizontal 横向布局  pointingInstructions 指向说明
     */
    private String mLayoutType;
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
    private float mPointingWidth;
    private int mPointingColor;
    private Context mContext;


    public BookPieChartView(@NonNull Context context) {
        this(context, null);
    }

    public BookPieChartView(Context context, @Nullable AttributeSet attrs) {
        this(context, attrs, 0);
    }

    public BookPieChartView(Context context, @Nullable AttributeSet attrs, int defStyleAttr) {
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
    }

    private void initAttrs(AttributeSet attrs, Context context) {
        TypedArray typedArray = context.obtainStyledAttributes(attrs, R.styleable.MyPieChartView);
        mPieChartWidth = typedArray.getDimension(R.styleable.MyPieChartView_pieChartWidth, 20F);

        mDataSize = typedArray.getDimension(R.styleable.MyPieChartView_dataSize, 20F);
        mDataColor = typedArray.getColor(R.styleable.MyPieChartView_dataColor, Color.WHITE);

        mUnitColor = typedArray.getColor(R.styleable.MyPieChartView_numColor, Color.WHITE);
        mUnitSize = typedArray.getFloat(R.styleable.MyPieChartView_numSize, 20F);

        mHorizontalMargin = typedArray.getDimension(R.styleable.MyPieChartView_horiMargin, 0F);
        mVerticalMargin = typedArray.getDimension(R.styleable.MyPieChartView_verticalMargin, 0F);

        mAnimTime = typedArray.getInt(R.styleable.MyPieChartView_animTime, 3000);

        mPointingColor = typedArray.getColor(R.styleable.MyPieChartView_pointingColor, Color.GRAY);
        mPointingWidth = typedArray.getDimension(R.styleable.MyPieChartView_pointingWidth, 2F);

        mLayoutType = typedArray.getString(R.styleable.MyPieChartView_layoutType);
        if (mLayoutType == null) {
            mLayoutType = "default";
        }
        mRingWidth = typedArray.getDimension(R.styleable.MyPieChartView_ringWidth, 25F);
        isRing = typedArray.getBoolean(R.styleable.MyPieChartView_isRing, false);
        mRingBgColor = typedArray.getColor(R.styleable.MyPieChartView_ringBgColor, Color.WHITE);
        mPadding = typedArray.getDimension(R.styleable.MyPieChartView_padding, 50F);
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
        mDataPaint.setTextSize(mDataSize);
        mDataPaint.setColor(mDataColor);
        //从中间向两边绘制，不需要再次计算文字
        mDataPaint.setTextAlign(Paint.Align.CENTER);

        //单位
        mUnitPaint = new TextPaint();
        mUnitPaint.setDither(true);
        mUnitPaint.setAntiAlias(true);
        mUnitPaint.setTextSize(mUnitSize);
        mUnitPaint.setColor(mUnitColor);
        //从中间向两边绘制，不需要再次计算文字
        mUnitPaint.setTextAlign(Paint.Align.CENTER);

        mPointingPaint = new Paint();
        mPointingPaint.setDither(true);
        mPointingPaint.setAntiAlias(true);
        mPointingPaint.setColor(mPointingColor);
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
        int minWidth = 0;
        if (mLayoutType.equals("horizontal")) {
            //圆心位置
            centerPosition.x = (int) (w / 2 - mHorizontalMargin);
            centerPosition.y = h / 2;
            //半径
            minWidth = (int) Math.min(w - getPaddingLeft() - getPaddingRight() - mHorizontalMargin * 2,
                    h - getPaddingBottom() - getPaddingTop());
        } else if (mLayoutType.equals("vertical")) {
            //圆心位置
            centerPosition.x = w / 2;
            centerPosition.y = (int) (h / 2 - mVerticalMargin);
            //半径
            minWidth = (int) Math.min(w - getPaddingLeft() - getPaddingRight(),
                    h - getPaddingBottom() - getPaddingTop() - mVerticalMargin * 2);
        } else {
            //圆心位置
            centerPosition.x = w / 2;
            centerPosition.y = h / 2;
            //半径
            minWidth = Math.min(w - getPaddingLeft() - getPaddingRight(),
                    h - getPaddingBottom() - getPaddingTop());
        }

        if (mLayoutType.equals("pointingInstructions")) {
            raduis = (minWidth / 2) - mPadding;
        } else {
            raduis = (minWidth / 2);
        }
        dataRaduis = raduis * 3 / 4;
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
        drawPieChart(canvas);
    }


    private void drawPieChart(Canvas canvas) {
        canvas.save();
        mStartAngle = 0f;
        mSweepAngle = 0f;
        for (int i = 0; i < mDataList.size(); i++) {
            mPieChartPaint.setColor(mDataList.get(i).getColor());
            mPointingPaint.setColor(mDataList.get(i).getColor());
            mSweepAngle = (mDataList.get(i).getNum() / mTotalNum) * 360 * mAnimationPercent;
            //外圆
            canvas.drawArc(mRectF, mStartAngle, mSweepAngle, true, mPieChartPaint);
            //内圆
            if (isRing) {
                mPieChartPaint.setColor(mRingBgColor);
                canvas.drawArc(mInRectF, mStartAngle - 1, mSweepAngle + 1, true, mPieChartPaint);
            }
            mStartAngle = mStartAngle + mSweepAngle;

            //画数据
            if (mLayoutType.equals("pointingInstructions")) {
                //指向说明
                pointData(canvas, i);
            } else {
                //画数据
                drawData(canvas, i);
            }
        }
        canvas.restore();
    }


    /**
     * 指向说明
     *
     * @param canvas
     * @param i
     */
    private void pointData(Canvas canvas, int i) {
        float xP = (float) (centerPosition.x + raduis *
                Math.sin(Math.toRadians((90 + mStartAngle - mSweepAngle / 2))));
        float yP = (float) (centerPosition.y - raduis *
                Math.cos(Math.toRadians((90 + mStartAngle - mSweepAngle / 2))));
        float xEdP = (float) (centerPosition.x + (raduis + 20) *
                Math.sin(Math.toRadians((90 + mStartAngle - mSweepAngle / 2))));
        float yEdP = (float) (centerPosition.y - (raduis + 20) *
                Math.cos(Math.toRadians((90 + mStartAngle - mSweepAngle / 2))));
        float xLast = 0f;
        if (mStartAngle - mSweepAngle / 2 >= 270 || mStartAngle - mSweepAngle / 2 <= 90) {
            xLast = xEdP + 40;
        } else {
            xLast = xEdP - 40;
        }
        canvas.drawLine(xP, yP, xEdP, yEdP, mPointingPaint);
        canvas.drawLine(xEdP, yEdP, xLast, yEdP, mPointingPaint);
        switch (mType) {
            case NUM:
                canvas.drawText(mDataList.get(i).getName(), xLast, yEdP, mDataPaint);
                break;
            case PERCENT:
                canvas.drawText(StrUtil.floatToString(mDataList.get(i).getNum() * 100 / mTotalNum, 2) + "%",
                        xLast,
                        yEdP - mDataPaint.ascent() + 5,
                        mUnitPaint);
                break;
            case CONTENT_NUM:
                canvas.drawText(mDataList.get(i).getName(), xLast, yEdP, mDataPaint);
                canvas.drawText(mDataList.get(i).getNum() + mDataList.get(i).getUnit(),
                        xLast,
                        yEdP - mDataPaint.ascent() + 5,
                        mUnitPaint);
                break;
            case CONTENT_PERCENT:
                canvas.drawText(mDataList.get(i).getName(), xLast, yEdP, mDataPaint);
                canvas.drawText(StrUtil.floatToString(mDataList.get(i).getNum() * 100 / mTotalNum, 2) + "%",
                        xLast,
                        yEdP - mDataPaint.ascent() + 5,
                        mUnitPaint);
                break;
        }
    }

    /**
     * 内部数据
     *
     * @param canvas
     * @param i
     */
    private void drawData(Canvas canvas, int i) {
        float x = (float) (centerPosition.x + dataRaduis *
                Math.sin(Math.toRadians((90 + mStartAngle - mSweepAngle / 2))));
        float y = (float) (centerPosition.y - dataRaduis *
                Math.cos(Math.toRadians((90 + mStartAngle - mSweepAngle / 2))));

        switch (mType) {
            case NUM:
                canvas.drawText(mDataList.get(i).getNum() + mDataList.get(i).getUnit(),
                        x, y, mUnitPaint);
                break;
            case PERCENT:
                canvas.drawText(StrUtil.floatToString(mDataList.get(i).getNum() * 100 / mTotalNum, 2) + "%",
                        x, y, mUnitPaint);
                break;
            case CONTENT_NUM:
                canvas.drawText(mDataList.get(i).getName(), x, y, mDataPaint);
                canvas.drawText(mDataList.get(i).getNum() + mDataList.get(i).getUnit(),
                        x, y - mDataPaint.ascent() + 5, mUnitPaint);
                break;
            case CONTENT_PERCENT:
                canvas.drawText(mDataList.get(i).getName(), x, y, mDataPaint);
                canvas.drawText(StrUtil.floatToString(mDataList.get(i).getNum() * 100 / mTotalNum, 2) + "%",
                        x, y - mDataPaint.ascent() + 5, mUnitPaint);
                break;
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
    public BookPieChartView setDataList(List<BookPieChartData> dataList) {
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


    /**
     * 设置显示类型
     */
    public BookPieChartView setType(BookPieChartType type) {
        this.mType = type;
        invalidate();
        return this;
    }


}


















