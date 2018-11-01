package com.github.tifezh.kchartlib.chart.lem;

import android.content.Context;
import android.graphics.Canvas;
import android.graphics.DashPathEffect;
import android.graphics.Paint;
import android.graphics.Path;
import android.graphics.PointF;
import android.support.annotation.Nullable;
import android.util.AttributeSet;
import android.util.Log;

import com.github.tifezh.kchartlib.R;
import com.github.tifezh.kchartlib.chart.comInterface.ILem;
import com.github.tifezh.kchartlib.utils.StrUtil;

import java.util.ArrayList;
import java.util.Collection;
import java.util.List;

public class LmeView extends BaseView {
    private Paint mTextPaint = new Paint(Paint.ANTI_ALIAS_FLAG); //文字
    private Paint mDotPaint = new Paint(Paint.ANTI_ALIAS_FLAG); //圆点
    private Paint mLinePaint = new Paint(Paint.ANTI_ALIAS_FLAG); //线
    private Paint mImaginaryLinePaint = new Paint(Paint.ANTI_ALIAS_FLAG); //虚线
    private Paint mColumnPaint = new Paint(Paint.ANTI_ALIAS_FLAG); //柱子

    private float mCurveMin; //Curve最小值
    private float mCurveMax; //Curve最大值

    private float mVolumeMin; //Volume最小值
    private float mVolumeMax; //Volume最大值

    private float mCurveScaleY = 1; //Y轴单位量
    private float mVolumeScaleY = 1; //Y轴单位量
    private float mScaleX = 1; //x轴的单位量

    private final List<ILem> mPoints = new ArrayList<>();

    private float mColumnPadding = 10;
    private float mColumnWidth; //单位宽度

    private int selectedIndex;

    public LmeView(Context context) {
        super(context);
        initView();
    }

    public LmeView(Context context, @Nullable AttributeSet attrs) {
        super(context, attrs);
        initView();
    }

    public LmeView(Context context, @Nullable AttributeSet attrs, int defStyleAttr) {
        super(context, attrs, defStyleAttr);
        initView();
    }

    private void initView() {
        mDotPaint.setStyle(Paint.Style.FILL);   //圆点

        mTextPaint.setTextSize(sp2px(11)); //文字

        mLinePaint.setStrokeWidth(dp2px(1)); //线

        mImaginaryLinePaint.setColor(getColor(R.color.c67D9FF)); //虚线
        mImaginaryLinePaint.setStrokeWidth(dp2px(2));
        mImaginaryLinePaint.setStyle(Paint.Style.STROKE);
        mImaginaryLinePaint.setDither(true);
        DashPathEffect pathEffect = new DashPathEffect(new float[]{15, 15}, 1); //设置虚线
        mImaginaryLinePaint.setPathEffect(pathEffect);

    }

    public void initData(Collection<? extends ILem> datas) {
        if (datas != null) {
            mPoints.clear();
            this.mPoints.addAll(datas);
            mPointCount = mPoints.size();
        }
        notifyChanged();
    }




    @Override
    protected void notifyChanged() {
        if (mPoints.size() > 0) {
            mCurveMax = mPoints.get(0).getCurve();
            mCurveMin = mPoints.get(0).getCurve();

            mVolumeMax = mPoints.get(0).getVolume();
            mVolumeMin = mPoints.get(0).getVolume();
        }

        for (int i = 0; i < mPoints.size(); i++) {
            ILem point = mPoints.get(i);
            mCurveMax = Math.max(mCurveMax, point.getCurve());
            mCurveMin = Math.min(mCurveMin, point.getCurve());

            mVolumeMax = Math.max(mVolumeMax, point.getVolume());
            mVolumeMin = Math.min(mVolumeMin, point.getVolume());
        }

        //y Curve 轴的缩放值
        if (mCurveMax > -100 && mCurveMax < 0) {
            mCurveMax = 0;
        } else {
            mCurveMax = StrUtil.getMillionMultipleMinimum((long) mCurveMax);
        }

        if (mCurveMin < 100 && mCurveMin > 0) {
            mCurveMin = 0;
        } else {
            mCurveMin = StrUtil.getMillionMultipleMinimum((long) mCurveMin);
        }
        mCurveScaleY = mBaseHeight /  Math.abs(mCurveMax - mCurveMin);

        //y Volume 轴的缩放值
        if (mVolumeMax > -5 && mVolumeMax < 0) {
            mVolumeMax = 0;
        } else {
            mVolumeMax = StrUtil.getZeroMultipleMinimum((long) mVolumeMax);
        }

        if (mVolumeMin < 5 && mVolumeMin > 0) {
            mVolumeMin = 0;
        } else {
            mVolumeMin = StrUtil.getZeroMultipleMinimum((long) mVolumeMin);
        }
        mVolumeScaleY = mBaseHeight /  Math.abs(mVolumeMax - mVolumeMin);

        //x轴的缩放值
        mScaleX = (mBaseWidth - mBasePaddingRight - mBasePaddingLeft - 2 * mColumnPadding) / mPointCount;

        mColumnWidth = mWidth / getMaxPointCount();
        mColumnPaint.setStrokeWidth(mColumnWidth * 0.5f); //柱子
        invalidate();
    }

    @Override
    protected void calculateSelectedX(float x) {
        Log.i("---> ； x ：", x + "");
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

        drawGird(canvas); //绘制网格
        drawDefTopTxtpaint(canvas); //画上面的小圆点
        drawColumn(canvas);  //绘制柱子
        drawLine(canvas); //绘制线
        drawText(canvas); //绘制文字


        drawLongClickLine(canvas); //长按指示线

    }


    //绘制网格线
    private void drawGird(Canvas canvas) {
        canvas.drawLine(0, 0, mWidth, 0, mGridPaint); //顶部线
        canvas.drawLine(0, mHeight, mWidth, mHeight, mGridPaint);//底部线

        //横向的grid
        float rowSpace = mBaseHeight / mGridRows;
        for (int i = 0; i <= mGridRows; i++) {
            canvas.drawLine(0, mTopPadding + rowSpace * i, mBaseWidth,
                    mTopPadding + rowSpace * i, mGridPaint);
        }

        //纵向的grid
        float columnSpace = (mBaseWidth - mBasePaddingLeft - mBasePaddingRight) / GridColumns;
        for (int i = 0; i <= GridColumns; i++) {
            canvas.drawLine(columnSpace * i + mBasePaddingLeft, mTopPadding,
                    columnSpace * i + mBasePaddingLeft, mHeight - mBottomPadding, mGridPaint);
        }

    }


    private void drawDefTopTxtpaint(Canvas canvas) {
        //红色小圆点
        mDotPaint.setColor(getColor(R.color.cF27A68));
        float r = 8;
        canvas.drawCircle(mBasePaddingLeft + r / 2, mTopPadding / 2 + r, r, mDotPaint);


        //先画文字
        mTextPaint.setTextAlign(Paint.Align.LEFT);
        mTextPaint.setColor(getColor(R.color.c9EB2CD));
        String hintTxt = "变动量";
        canvas.drawText(hintTxt, mBasePaddingLeft + r + 10,
                mTopPadding / 2 + getFontBaseLineHeight(mTextPaint) / 2, mTextPaint);

        float mWidth = mTextPaint.measureText(hintTxt) + mBasePaddingLeft + dp2px(20);
        mDotPaint.setColor(getColor(R.color.c67D9FF));
        canvas.drawCircle(mWidth + r / 2, mTopPadding / 2 + r, r, mDotPaint);

        //先画文字
        mTextPaint.setTextAlign(Paint.Align.LEFT);
        mTextPaint.setColor(getColor(R.color.c9EB2CD));
        hintTxt = "报价";
        canvas.drawText(hintTxt, r + mWidth + 10,
                mTopPadding / 2 + getFontBaseLineHeight(mTextPaint) / 2, mTextPaint);

    }

    private void drawText(Canvas canvas) {
        float baseLine = getFontBaseLineHeight(mTextPaint);
        float textHeight = getFontHeight(mTextPaint);

        float rowValue = (mCurveMax - mCurveMin) / (mGridRows - 1);
        float rowSpace = mBaseHeight / mGridRows;

        //画左边的值
        mTextPaint.setTextAlign(Paint.Align.LEFT);
        mTextPaint.setColor(getResources().getColor(R.color.c9EB2CD));
//        canvas.drawText(mCurveMax + "", mBaseTextPaddingLeft, baseLine + mTopPadding, mTextPaint); //绘制最大值
//
//        canvas.drawText(mCurveMin + "", mBaseTextPaddingLeft,
//                mHeight - mBottomPadding - textHeight + baseLine, mTextPaint); //绘制最小值

        for (int i = 0; i < mGridRows; i++) {
            String text = StrUtil.getPositiveNumber(mCurveMax - rowValue * i);
            if (i < mGridRows / 2) {
                canvas.drawText(text, mBaseTextPaddingLeft,
                        rowSpace * i + baseLine + mTopPadding, mTextPaint);

            } else if (i == mGridRows / 2) {
                canvas.drawText(text, mBaseTextPaddingLeft,
                        rowSpace * i + rowSpace / 2 + baseLine / 2 + mTopPadding, mTextPaint);

            } else if (i > mGridRows / 2) {
                canvas.drawText(text, mBaseTextPaddingLeft,
                        mHeight - mBottomPadding - textHeight + baseLine - rowSpace * (mGridRows - i - 1), mTextPaint);

            }


        }


        //画右边的值
        float rowVolume = (mVolumeMax - mVolumeMin) / (mGridRows - 1);
        mTextPaint.setTextAlign(Paint.Align.RIGHT);
        mTextPaint.setColor(getResources().getColor(R.color.c9EB2CD));
//        canvas.drawText(mVolumeMax + "", mBaseWidth - mBaseTextPaddingRight, baseLine + mTopPadding, mTextPaint); //绘制最大值
//
//        canvas.drawText(mVolumeMin + "", mBaseWidth - mBaseTextPaddingRight,
//                mHeight - mBottomPadding - textHeight + baseLine, mTextPaint); //绘制最小值

        for (int i = 0; i < mGridRows; i++) {
            String text = StrUtil.getPositiveNumber(mVolumeMax - rowVolume * i);
            if (i < mGridRows / 2) {
                canvas.drawText(text, mBaseWidth - mBaseTextPaddingRight,
                        rowSpace * i + baseLine + mTopPadding, mTextPaint);

            } else if (i == mGridRows / 2) {
                canvas.drawText(text, mBaseWidth - mBaseTextPaddingRight,
                        rowSpace * i + rowSpace / 2 + baseLine / 2 + mTopPadding, mTextPaint);

            } else if (i > mGridRows / 2) {
                canvas.drawText(text, mBaseWidth - mBaseTextPaddingRight,
                        mHeight - mBottomPadding - textHeight + baseLine - rowSpace * (mGridRows - i - 1), mTextPaint);

            }
        }

        //画时间
        mTextPaint.setTextAlign(Paint.Align.RIGHT);
        mTextPaint.setColor(getResources().getColor(R.color.c6A798E));
        float y = mBaseHeight + mTopPadding + mTextPaint.measureText(StrUtil.getLEMDataStr(mPoints.get(0).getDate().getTime()));
        mTextPaint.setTextAlign(Paint.Align.LEFT);
        for (int i = 0; i < mPoints.size(); i++) {
            canvas.save();
            canvas.rotate(270, getX(i) + baseLine / 2, y); //默认以原点旋转，所以的设置旋转点
            canvas.drawText(StrUtil.getLEMDataStr(mPoints.get(i).getDate().getTime()),
                    getX(i), y - 5, mTextPaint); //时间 ?? 存在和柱子对齐的问题
            canvas.restore();
        }


    }

    private void drawColumn(Canvas canvas) {
        //画柱子
        for (int i = 0; i < mPoints.size(); i++) {
            if (mPoints.get(i).getVolume() > 0) {
                mColumnPaint.setColor(getColor(R.color.cF27A68));
            } else if (mPoints.get(i).getVolume() < 0) {
                mColumnPaint.setColor(getColor(R.color.c3EB86A));
            }
            canvas.drawLine(getX(i),
                    getVoluemY(0),
                    getX(i),
                    getVoluemY(mPoints.get(i).getVolume()),
                    mColumnPaint);

        }

    }

    private void drawLine(Canvas canvas) {
        //小圆点线
        mDotPaint.setColor(getColor(R.color.c67D9FF));
        float r = mColumnWidth * 0.5f / 2;
        for (int i = 0; i < mPoints.size(); i++) {
            canvas.drawCircle(getX(i),
                    getCurveY(mPoints.get(i).getCurve()),
                    r, mDotPaint);
        }


        //虚线
        Path path = new Path();
        path.reset();
        path.moveTo(getX(0), getCurveY(mPoints.get(0).getCurve()));
//        for (int i = 1; i < mPoints.size(); i++) {
//            path.lineTo(getX(i), getCurveY(mPoints.get(i).getValue()));
//            canvas.drawPath(path, mImaginaryLinePaint);
//        }
        drawScrollLine(canvas, path);

    }


    //三级贝塞尔曲线
    private void drawScrollLine(Canvas canvas, Path path) {
        for (int i = 0; i < mPoints.size() - 1; i++) {
            float wt = (getX(i) + getX(i + 1)) / 2;
            PointF p3 = new PointF();
            PointF p4 = new PointF();
            p3.y = getCurveY(mPoints.get(i).getCurve());
            p3.x = wt;

            p4.y = getCurveY(mPoints.get(i + 1).getCurve());
            p4.x = wt;

            path.cubicTo(p3.x, p3.y, p4.x, p4.y, getX(i + 1), getCurveY(mPoints.get(i + 1).getCurve()));
            canvas.drawPath(path, mImaginaryLinePaint);
        }
    }

    private void drawLongClickLine(Canvas canvas) {
        //画指示线
//        if (isLongPress || !isClosePress) {
        mLinePaint.setColor(getColor(R.color.c6774FF));
        mLinePaint.setStrokeWidth(dp2px(1));
        ILem point = mPoints.get(selectedIndex);
        Log.i("---> ； selectedIndex ：", selectedIndex + "");
        float x = getX(selectedIndex);
        //轴线
        canvas.drawLine(x, mTopPadding, x, mHeight - mBottomPadding, mLinePaint);//Y

        drawSelector(selectedIndex, point, canvas);
//        }
    }

    /**
     * draw选择器
     *
     * @param canvas
     */
    private void drawSelector(int selectedIndex, ILem point, Canvas canvas) {

    }

    private float getColumeHeight(float value) {
        return (value - mCurveMin) * mCurveScaleY;
    }

    private float getX(int i) {
        return mBasePaddingLeft + mColumnPadding + mScaleX * i + mColumnWidth / 2;
    }

    /**
     * 修正y值
     */
    private float getCurveY(float value) {
        return mTopPadding + (mCurveMax - value) * mCurveScaleY;
    }

    private float getVoluemY(float value) {
        return mTopPadding + (mVolumeMax - value) * mVolumeScaleY;
    }


    /**
     * 获取最大能有多少个点
     */
    public long getMaxPointCount() {
        return mPoints.size();
    }


}









