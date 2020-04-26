package com.github.tifezh.kchart.view.chart;

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
import android.support.v7.widget.GridLayoutManager;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.text.TextPaint;
import android.util.AttributeSet;
import android.view.Gravity;
import android.view.ViewGroup;
import android.widget.FrameLayout;
import android.widget.RelativeLayout;

import com.github.tifezh.kchart.R;
import com.github.tifezh.kchart.utils.ValueUtil;

import java.util.ArrayList;
import java.util.List;


/**
 * @author puyantao
 * @description :
 * @date 2020/4/25
 */
public class MyPieChartView extends FrameLayout {
    //画笔
    private Paint mPieChartPaint;
    private Float mPieChartWidth;
    //圆心位置
    private Point centerPosition;
    //半径
    private float raduis;
    private float dataRaduis;
    //声明边界矩形
    private RectF mRectF;
    //数据
    private List<PieChartData> mData = new ArrayList();
    //总数
    private float mTotalNum;
    //开始角度
    private float mStartAngle;
    //扫过的角度
    private Float mSweepAngle;
    //动画时间
    private int mAnimTime;
    //属性动画
    private ValueAnimator mAnimator;
    //动画进度
    private float mPercent;
    //字体
    private TextPaint mDataPaint;
    private float mDataSize;
    private int mDataColor;
    //单位
    private TextPaint mUnitPaint;
    private float mUnitSize;
    private int mUnitColor;
    //样式选择
    private PieChartType mType = PieChartType.CONTENT_PERCENT;
    //列表说明
    private RecyclerView mRecyclerView;
    //声明adapter
    private PicChartAdapter adapter;
    //布局样式
    //default 普通样式  vertical 竖向布局  horizontal 横向布局  pointingInstructions 指向说明
    private String mLayoutType;
    //横向间距
    private float mHoriMargin;
    //纵向间距
    private float mVerticalMargin;
    //指向说明
    private Paint mPointingPaint;
    private float mPointingWidth;
    private int mPointingColor;
    private Context mContext;


    public MyPieChartView(@NonNull Context context) {
        this(context, null);
    }

    public MyPieChartView(Context context, @Nullable AttributeSet attrs) {
        this(context, attrs, 0);
    }

    public MyPieChartView(Context context, @Nullable AttributeSet attrs, int defStyleAttr) {
        super(context, attrs, defStyleAttr);
        this.mContext = context;
        initView(attrs);
    }

    private void initView(AttributeSet attrs) {
        mPercent = 0f;
        //初始化属性动画
        mAnimator = new ValueAnimator();
        //初始化圆心属性
        centerPosition = new Point();
        mRectF = new RectF();
        //初始化属性
        initAttrs(attrs, mContext);
        //初始化画笔
        initPaint();
        addLabel();
    }

    private void initAttrs(AttributeSet attrs, Context context) {
        TypedArray typedArray = context.obtainStyledAttributes(attrs, R.styleable.MyPieChartView);
        mPieChartWidth = typedArray.getDimension(
                R.styleable.MyPieChartView_pieChartWidth,
                PieChartConstant.DEFAULT_PIECHART_WIDTH
        );

        mDataSize = typedArray.getDimension(R.styleable.MyPieChartView_dataSize,
                PieChartConstant.DEFAULT_DATA_SIZE);
        mDataColor = typedArray.getColor(R.styleable.MyPieChartView_dataColor, Color.WHITE);

        mUnitColor = typedArray.getColor(R.styleable.MyPieChartView_numColor, Color.WHITE);
        mUnitSize = typedArray.getFloat(R.styleable.MyPieChartView_numSize,
                PieChartConstant.DEFAULT_UNIT_SIZE);

        mHoriMargin = typedArray.getDimension(R.styleable.MyPieChartView_horiMargin,
                PieChartConstant.DEFAULT_HORI_MARGIN);
        mVerticalMargin = typedArray.getDimension(R.styleable.MyPieChartView_verticalMargin,
                PieChartConstant.DEFAULT_VERTICAL_MARGIN);

        mAnimTime = typedArray.getInt(R.styleable.MyPieChartView_animTime,
                PieChartConstant.DEFAULT_ANIM_TIME);

        mPointingColor = typedArray.getColor(R.styleable.MyPieChartView_pointingColor, Color.GRAY);
        mPointingWidth = typedArray.getDimension(R.styleable.MyPieChartView_pointingWidth,
                PieChartConstant.DEFAULT_POINTING_WIDTH);

        mLayoutType = typedArray.getString(R.styleable.MyPieChartView_layoutType);
        if (mLayoutType == null) {
            mLayoutType = "default";
        }
        typedArray.recycle();
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
            centerPosition.x = (int) (w / 2 - mHoriMargin);
            centerPosition.y = h / 2;
            //半径
            minWidth = (int) Math.min(w - getPaddingLeft() - getPaddingRight() - mHoriMargin * 2,
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
            raduis = (minWidth / 2) - 75;
        } else {
            raduis = (minWidth / 2);
        }
        dataRaduis = raduis * 3 / 4;
        //矩形坐标
        mRectF.left = centerPosition.x - raduis;
        mRectF.top = centerPosition.y - raduis;
        mRectF.right = centerPosition.x + raduis;
        mRectF.bottom = centerPosition.y + raduis;

    }

    @Override
    protected void onLayout(boolean changed, int left, int top, int right, int bottom) {
        super.onLayout(changed, left, top, right, bottom);
    }

    @Override
    protected void onDraw(Canvas canvas) {
        super.onDraw(canvas);

    }


    @Override
    protected void dispatchDraw(Canvas canvas) {
        super.dispatchDraw(canvas);
        drawPieChart(canvas);
    }


    private void drawPieChart(Canvas canvas) {
        canvas.save();
        mStartAngle = 0f;
        mSweepAngle = 0f;
        for (int i = 0; i < mData.size(); i++) {
            mPieChartPaint.setColor(mData.get(i).getColor());
            mSweepAngle = (mData.get(i).getNum() / mTotalNum) * 360 * mPercent;
            //画圆
            canvas.drawArc(mRectF, mStartAngle, mSweepAngle, true, mPieChartPaint);
            mStartAngle = mStartAngle + mSweepAngle;
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
            xLast = xEdP + 30;
        } else {
            xLast = xEdP - 30;
        }
        canvas.drawLine(xP, yP, xEdP, yEdP, mPointingPaint);
        canvas.drawLine(xEdP, yEdP, xLast, yEdP, mPointingPaint);
        canvas.drawText(mData.get(i).getName(), xLast, yEdP, mDataPaint);
        canvas.drawText(mData.get(i).getNum() + mData.get(i).getUnit(),
                xLast,
                yEdP - mDataPaint.ascent() + 5,
                mUnitPaint);
    }

    private void drawData(Canvas canvas, int i) {
        float x = (float) (centerPosition.x + dataRaduis *
                Math.sin(Math.toRadians((90 + mStartAngle - mSweepAngle / 2))));
        float y = (float) (centerPosition.y - dataRaduis *
                Math.cos(Math.toRadians((90 + mStartAngle - mSweepAngle / 2))));

        switch (mType) {
            case NUM:
                canvas.drawText(mData.get(i).getNum() + mData.get(i).getUnit(),
                        x, y, mUnitPaint);
                break;
            case PERCENT:
                canvas.drawText(mData.get(i).getNum() * 100 / mTotalNum + "%",
                        x, y, mUnitPaint);
                break;
            case CONTENT_NUM:
                canvas.drawText(mData.get(i).getName(), x, y, mDataPaint);
                canvas.drawText(mData.get(i).getNum() + mData.get(i).getUnit(),
                        x, y - mDataPaint.ascent() + 5, mUnitPaint);
                break;
            case CONTENT_PERCENT:
                canvas.drawText(mData.get(i).getName(), x, y, mDataPaint);
                canvas.drawText(mData.get(i).getNum() * 100 / mTotalNum + "%",
                        x, y - mDataPaint.ascent() + 5, mUnitPaint);
                break;
        }
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
        /*mPieChartPaint!!.strokeCap =
          Paint.Cap.SQUARE*///笔刷样式 //当画笔样式为STROKE或FILL_OR_STROKE时，
        // 设置笔刷的图形样式，如圆形样式Cap.ROUND,或方形样式Cap.SQUARE
        //mPieChartPaint!!.color = Color.RED

        mDataPaint = new TextPaint();
        mDataPaint.setDither(true);
        mDataPaint.setAntiAlias(true);
        mDataPaint.setTextSize(mDataSize);
        mDataPaint.setColor(mDataColor);
        //从中间向两边绘制，不需要再次计算文字
        mDataPaint.setTextAlign(Paint.Align.CENTER);

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

    /**
     * 增加列表说明
     */
    private void addLabel() {
        if (mLayoutType.equals("horizontal")) {
            addHorizontal();
        } else {
            addVertical();
        }
    }

    private void addHorizontal() {
        mRecyclerView = new RecyclerView(mContext);
        mRecyclerView.setLayoutManager(new LinearLayoutManager(mContext));
        mRecyclerView.setNestedScrollingEnabled(false);
        if (adapter == null) {
            adapter = new PicChartAdapter(mContext, mData);
            mRecyclerView.setAdapter(adapter);
        } else {
            adapter.notifyDataSetChanged();
        }

        RelativeLayout relativeLayout = new RelativeLayout(mContext);
        LayoutParams params = new LayoutParams(
                ViewGroup.LayoutParams.WRAP_CONTENT,
                ViewGroup.LayoutParams.WRAP_CONTENT);
        params.gravity = Gravity.RIGHT | Gravity.CENTER_VERTICAL;
        relativeLayout.setLayoutParams(params);

        RelativeLayout.LayoutParams p2 = new RelativeLayout.LayoutParams(
                ViewGroup.LayoutParams.WRAP_CONTENT,
                ViewGroup.LayoutParams.WRAP_CONTENT);
        relativeLayout.addView(mRecyclerView, p2);
        addView(relativeLayout);
    }


    private void addVertical() {
        mRecyclerView = new RecyclerView(mContext);
        mRecyclerView.setLayoutManager(new GridLayoutManager(mContext, 3));
        mRecyclerView.setNestedScrollingEnabled(false);
        if (adapter == null) {
            adapter = new PicChartAdapter(mContext, mData);
            mRecyclerView.setAdapter(adapter);
        } else {
            adapter.notifyDataSetChanged();
        }

        RelativeLayout relativeLayout = new RelativeLayout(mContext);
        LayoutParams params = new LayoutParams(
                ViewGroup.LayoutParams.MATCH_PARENT,
                ViewGroup.LayoutParams.WRAP_CONTENT);

        params.gravity = Gravity.BOTTOM | Gravity.CENTER_HORIZONTAL;
        relativeLayout.setLayoutParams(params);
        RelativeLayout.LayoutParams p2 = new RelativeLayout.LayoutParams(
                ViewGroup.LayoutParams.MATCH_PARENT,
                ViewGroup.LayoutParams.WRAP_CONTENT);
        relativeLayout.addView(mRecyclerView, p2);
        addView(relativeLayout);
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
                mPercent = (float) animation.getAnimatedValue();
                postInvalidate();
            }
        });
        mAnimator.start();
    }

    /**
     * 设置数据
     */
    public MyPieChartView setData(List<PieChartData> data) {
        if (ValueUtil.isListNotEmpty(data)) {
            for (int i = 0; i < data.size(); i++) {
                mTotalNum = data.get(i).getNum() + mTotalNum;
            }
        }
        mData.addAll(data);
        startAnim(mAnimTime);
        if (mLayoutType != "default" && mLayoutType != "pointingInstructions") {
            if (adapter == null) {
                adapter = new PicChartAdapter(mContext, mData);
                mRecyclerView.setAdapter(adapter);
            } else {
                adapter.notifyDataSetChanged();
            }
        }
        invalidate();
        return this;
    }


    /**
     * 设置显示类型
     */
    public MyPieChartView setType(PieChartType type) {
        this.mType = type;
        invalidate();
        return this;
    }


}













