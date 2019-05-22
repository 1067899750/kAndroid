package com.github.tifezh.kchartlib.chart.detail;

import android.content.Context;
import android.content.res.TypedArray;
import android.graphics.Canvas;
import android.graphics.Color;
import android.graphics.Paint;
import android.graphics.RectF;
import android.graphics.drawable.Drawable;
import android.util.AttributeSet;
import android.view.View;

import com.github.tifezh.kchartlib.R;

import java.util.ArrayList;

/**
 * @Describe 自动适配的柱状图
 * @Author puyantao
 * @Email 1067899750@qq.com
 * @create 2019/5/20 19:11
 */
public class HistogramView extends View {
    private Context mContext;
    private ArrayList<HistogramModel> mPoints;
    private int mPointCount;

    private Paint mNameTextPaint = new Paint(Paint.ANTI_ALIAS_FLAG); //名字
    private Paint mColumnPaint = new Paint(Paint.ANTI_ALIAS_FLAG); //线
    private Paint mMoneyTextPaint = new Paint(Paint.ANTI_ALIAS_FLAG); //文字

    private int mHeight; //试图高度
    private int mWidth;  //试图宽度
    private int mBasePaddingLeft = 20;
    private int mBasePaddingRight = 0;
    private int mPadding = 30;

    private int mBackgroundColor;
    private float mRightTextWeight = 0;
    private float mMaxManey = 0;
    private float mColumnScaleY = 0;
    private float mColumnLeft;


    private int mBackgroudColor;
    private int mTextColor;
    private int mLeftTextSize;
    private int mRightTextSize;
    private int mColumnWeight; //柱子宽度


    public HistogramView(Context context) {
        this(context, null);
    }

    public HistogramView(Context context, AttributeSet attrs) {
        this(context, attrs, 0);
    }

    public HistogramView(Context context, AttributeSet attrs, int defStyleAttr) {
        super(context, attrs, defStyleAttr);
        initView(context, attrs);
    }

    private void initView(Context context, AttributeSet attrs) {
        TypedArray ta = context.obtainStyledAttributes(attrs, R.styleable.MyViewStyle);
        mBackgroudColor = ta.getColor(R.styleable.MyViewStyle_appBackgBackground, Color.WHITE); //柱子背景颜色
        mTextColor = ta.getColor(R.styleable.MyViewStyle_appTextColor, Color.BLACK); //Text背景颜色
        mColumnWeight = (int) ta.getDimension(R.styleable.MyViewStyle_appColumnWeight, dp2px(5));
        mLeftTextSize = (int) ta.getDimension(R.styleable.MyViewStyle_appLeftTextSize, sp2px(18));
        mRightTextSize = (int) ta.getDimension(R.styleable.MyViewStyle_appRightTextSize, sp2px(10));
        ta.recycle();


        mBackgroundColor = Color.parseColor("#402A2D4F");

        mPoints = new ArrayList<>();

        mNameTextPaint.setTextSize(mLeftTextSize); //文字
        mNameTextPaint.setColor(mTextColor);

        mMoneyTextPaint.setTextSize(mRightTextSize); //文字
        mMoneyTextPaint.setColor(mTextColor);

        mColumnPaint.setStrokeWidth(mColumnWeight); //柱子
        mColumnPaint.setStyle(Paint.Style.FILL_AND_STROKE);
        mColumnPaint.setColor(mBackgroudColor);

    }


    public void initDatas(ArrayList<HistogramModel> datas) {
        mPoints.clear();
        if (datas != null) {
            this.mPoints.addAll(datas);
            mPointCount = mPoints.size();
        }

        for (int i = 0; i < mPointCount; i++) {
            mMaxManey = Math.max(Math.abs(mPoints.get(i).getMoney()), mMaxManey);
        }
        mRightTextWeight = mMoneyTextPaint.measureText(mMaxManey + "元");
        mColumnLeft = mBasePaddingLeft + mNameTextPaint.measureText(mPoints.get(0).getName()) + mPadding;
        requestLayout();
    }


    @Override
    protected void onMeasure(int widthMeasureSpec, int heightMeasureSpec) {
        super.onMeasure(widthMeasureSpec, heightMeasureSpec);
        int widthSpecSize = MeasureSpec.getSize(widthMeasureSpec);
        int heightSpecSize = MeasureSpec.getSize(heightMeasureSpec);
        float nameHeight = getFontHeight(mNameTextPaint);
        heightSpecSize = (int) (nameHeight * mPointCount + (mPadding * (mPointCount - 1)));

        setMeasuredDimension(widthSpecSize, heightSpecSize);
        this.mWidth = getMeasuredWidth();
        this.mHeight = getMeasuredHeight();
        float columnMaxWeight = mWidth - mColumnLeft - mPadding - mRightTextWeight - mBasePaddingRight;
        mColumnScaleY = columnMaxWeight / mMaxManey;
    }

    @Override
    protected void onLayout(boolean changed, int left, int top, int right, int bottom) {
        super.onLayout(changed, left, top, right, bottom);
    }

    @Override
    protected void onSizeChanged(int w, int h, int oldw, int oldh) {
        super.onSizeChanged(w, h, oldw, oldh);
        this.mWidth = getMeasuredWidth();
        this.mHeight = getMeasuredHeight();
        float columnMaxWeight = mWidth - mColumnLeft - mPadding - mRightTextWeight - mBasePaddingRight;
        mColumnScaleY = columnMaxWeight / mMaxManey;
    }

    @Override
    protected void onDraw(Canvas canvas) {
        super.onDraw(canvas);
        //绘制背景颜色
//        canvas.drawColor(mBackgroundColor);

        drawNameText(canvas); //绘制Name
        drawColumn(canvas); //绘制柱子, 价格

    }

    private void drawNameText(Canvas canvas) {
        mNameTextPaint.setTextAlign(Paint.Align.LEFT);
        for (int i = 0; i < mPointCount; i++) {
            canvas.drawText(mPoints.get(i).getName(),
                    mBasePaddingLeft,
                    mPadding * i + getFontBaseLineHeight(mNameTextPaint) * (i + 1),
                    mNameTextPaint);
        }
    }

    //TODO 高度计算存在问题
    private void drawColumn(Canvas canvas) {
        mColumnPaint.setStrokeWidth(getFontHeight(mNameTextPaint) / 2);
        mColumnPaint.setTextAlign(Paint.Align.CENTER);
        mMoneyTextPaint.setTextAlign(Paint.Align.LEFT);
        float textNameHeight = getFontHeight(mNameTextPaint);
        float pading = getFontBaseLineHeight(mNameTextPaint) - textNameHeight;

        for (int i = 0; i < mPointCount; i++) {
//            canvas.drawRect(new RectF(mColumnLeft,
//                            mPadding * i + textNameHeight * i + textNameHeight * (i + 1) / 3,
//                            mColumnLeft + mColumnScaleY * mPoints.get(i).getMoney(),
//                            mPadding * i + textNameHeight * i + textNameHeight * 2 / 3),
//                    mColumnPaint);

            canvas.drawLine(mColumnLeft,
                    mPadding * i + textNameHeight / 2 + textNameHeight * i - 5 * i,
                    mColumnLeft + mColumnScaleY * mPoints.get(i).getMoney(),
                    mPadding * i + textNameHeight / 2 + textNameHeight * i - 5 * i,
                    mColumnPaint);


            canvas.drawText(mPoints.get(i).getMoney() + "元",
                    mColumnLeft + mColumnScaleY * mPoints.get(i).getMoney() + mPadding,
                    mPadding * i + textNameHeight / 2 + textNameHeight * i + getFontHeight(mMoneyTextPaint) / 2 - 5 * (i + 1),
                    mMoneyTextPaint);

        }


    }


    /*************************************************工具***************************************************/
    private int dp2px(float dp) {
        final float scale = getContext().getResources().getDisplayMetrics().density;
        return (int) (dp * scale + 0.5f);
    }

    private int sp2px(float spValue) {
        final float fontScale = getContext().getResources().getDisplayMetrics().scaledDensity;
        return (int) (spValue * fontScale + 0.5f);
    }

    //高度
    private float getFontBaseLineHeight(Paint paint) {
        Paint.FontMetrics fm = paint.getFontMetrics();
        float textHeight = fm.descent - fm.ascent;
        float baseLine = (textHeight - fm.bottom - fm.top) / 2;
        return baseLine;
    }

    //文字的高度
    private float getFontHeight(Paint paint) {
        Paint.FontMetrics fm = paint.getFontMetrics();
        float textHeight = fm.descent - fm.ascent;
        return textHeight;
    }


}





















