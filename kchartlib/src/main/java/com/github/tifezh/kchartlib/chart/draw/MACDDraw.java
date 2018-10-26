package com.github.tifezh.kchartlib.chart.draw;

import android.content.Context;
import android.graphics.Canvas;
import android.graphics.Paint;
import android.support.annotation.NonNull;
import android.support.annotation.Nullable;
import android.support.v4.content.ContextCompat;

import com.github.tifezh.kchartlib.R;
import com.github.tifezh.kchartlib.chart.base.IChartDraw;
import com.github.tifezh.kchartlib.chart.base.IValueFormatter;
import com.github.tifezh.kchartlib.chart.comInterface.IMACD;
import com.github.tifezh.kchartlib.chart.formatter.ValueFormatter;
import com.github.tifezh.kchartlib.chart.view.BaseKChartView;

/**
 *
 * Description macd实现类
 * Author puyantao
 * Email 1067899750@qq.com
 * Date 2018-10-26 17:39
 */


public class MACDDraw implements IChartDraw<IMACD> {

    private Paint mRedPaint = new Paint(Paint.ANTI_ALIAS_FLAG);
    private Paint mGreenPaint = new Paint(Paint.ANTI_ALIAS_FLAG);
    private Paint mDIFPaint = new Paint(Paint.ANTI_ALIAS_FLAG);
    private Paint mDEAPaint = new Paint(Paint.ANTI_ALIAS_FLAG);
    private Paint mMACDPaint = new Paint(Paint.ANTI_ALIAS_FLAG);
    private Paint mTargetPaint = new Paint(Paint.ANTI_ALIAS_FLAG);
    private Paint mTargetNamePaint = new Paint(Paint.ANTI_ALIAS_FLAG);
    /**
     * macd 中柱子的宽度
     */
    private float mMACDWidth = 0;

    public MACDDraw(BaseKChartView view) {
        Context context = view.getContext();
        mRedPaint.setColor(ContextCompat.getColor(context, R.color.chart_red));
        mGreenPaint.setColor(ContextCompat.getColor(context, R.color.chart_green));
    }

    @Override
    public void drawTranslated(@Nullable IMACD lastPoint, @NonNull IMACD curPoint, float lastX, float curX, @NonNull Canvas canvas, @NonNull BaseKChartView view, int position) {
        drawMACD(canvas, view, curX, curPoint.getMacd());
        view.drawChildLine(canvas, mDEAPaint, lastX, lastPoint.getDea(), curX, curPoint.getDea());
        view.drawChildLine(canvas, mDIFPaint, lastX, lastPoint.getDif(), curX, curPoint.getDif());
    }

    @Override
    public void drawText(@NonNull Canvas canvas, @NonNull BaseKChartView view, int position, float x, float y) {
        y = y - BaseKChartView.mChildTextPaddingY;
        x = BaseKChartView.mChildTextPaddingX;

        String text ="MACD";
        IMACD point = (IMACD) view.getItem(position);

        canvas.drawText(text, x, y, mTargetPaint);
        x += mTargetPaint.measureText(text);
        x += BaseKChartView.mTextPaddingLeft;

        text = "MACD (12,26,9)";
        canvas.drawText(text, x, y, mTargetNamePaint);
        x += mTargetNamePaint.measureText(text);
        x += BaseKChartView.mTextPaddingLeft;

        text = "DIF:" + view.formatValue(point.getDif());
        canvas.drawText(text, x, y, mDIFPaint);
        x += mDIFPaint.measureText(text);
        x += BaseKChartView.mTextPaddingLeft;

        text = "DEA:" + view.formatValue(point.getDea());
        canvas.drawText(text, x, y, mDEAPaint);
        x += mDEAPaint.measureText(text);

        x += BaseKChartView.mTextPaddingLeft;
        text = "STICK:" + view.formatValue(point.getMacd());
        canvas.drawText(text, x, y, mMACDPaint);

    }

    @Override
    public float getMaxValue(IMACD point) {
        return Math.max(Math.abs(point.getMacd()), Math.max(Math.abs(point.getDea()), Math.abs(point.getDif())));
    }

    @Override
    public void drawMaxAndMin(@NonNull BaseKChartView view, Canvas canvas, float maxX, float minX, IMACD maxPoint, IMACD minPoint) {

    }


    @Override
    public float getMinValue(IMACD point) {
        return -Math.max(Math.abs(point.getMacd()), Math.max(Math.abs(point.getDea()), Math.abs(point.getDif())));
    }


    @Override
    public IValueFormatter getValueFormatter() {
        return new ValueFormatter();
    }

    @Override
    public void setTargetColor(int... color) {
        if (color.length > 0) {
            mTargetPaint.setColor(color[0]);
            mTargetNamePaint.setColor(color[1]);
        }
    }

    /**
     * 画macd
     *
     * @param canvas
     * @param x
     * @param macd
     */
    private void drawMACD(Canvas canvas, BaseKChartView view, float x, float macd) {
        float macdy = view.getChildY(macd);
        float r = mMACDWidth / 2;
        float zeroy = view.getChildY(0);
        if (macd > 0) {
            //               left   top   right  bottom
            canvas.drawRect(x - r, macdy, x + r, zeroy, mRedPaint);
        } else {
            canvas.drawRect(x - r, zeroy, x + r, macdy, mGreenPaint);
        }
    }

    /**
     * 设置DIF颜色
     */
    public void setDIFColor(int color) {
        this.mDIFPaint.setColor(color);
    }

    /**
     * 设置DEA颜色
     */
    public void setDEAColor(int color) {
        this.mDEAPaint.setColor(color);
    }

    /**
     * 设置MACD颜色
     */
    public void setMACDColor(int color) {
        this.mMACDPaint.setColor(color);
    }

    /**
     * 设置MACD的宽度
     *
     * @param MACDWidth
     */
    public void setMACDWidth(float MACDWidth) {
        mMACDWidth = MACDWidth;
    }

    /**
     * 设置曲线宽度
     */
    public void setLineWidth(float width) {
        mDEAPaint.setStrokeWidth(width);
        mDIFPaint.setStrokeWidth(width);
        mMACDPaint.setStrokeWidth(width);
        mTargetPaint.setStrokeWidth(width);
        mTargetNamePaint.setStrokeWidth(width);
    }

    /**
     * 设置文字大小
     */
    public void setTextSize(float textSize) {
        mDEAPaint.setTextSize(textSize);
        mDIFPaint.setTextSize(textSize);
        mMACDPaint.setTextSize(textSize);
        mTargetPaint.setTextSize(textSize);
        mTargetNamePaint.setTextSize(textSize);
    }
}
