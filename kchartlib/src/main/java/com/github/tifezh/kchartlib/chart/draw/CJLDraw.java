package com.github.tifezh.kchartlib.chart.draw;

import android.content.Context;
import android.graphics.Canvas;
import android.graphics.Paint;
import android.support.annotation.NonNull;
import android.support.annotation.Nullable;
import android.support.v4.content.ContextCompat;

import com.github.tifezh.kchartlib.R;
import com.github.tifezh.kchartlib.chart.view.BaseKChartView;
import com.github.tifezh.kchartlib.chart.base.IChartDraw;
import com.github.tifezh.kchartlib.chart.base.IValueFormatter;
import com.github.tifezh.kchartlib.chart.comInterface.IVolume;
import com.github.tifezh.kchartlib.chart.formatter.BigValueFormatter;
import com.github.tifezh.kchartlib.utils.DensityUtil;

/**
 * Description：
 * Author: star
 * Email: guimingxing@163.com
 * Date: 2018-10-17 15:50
 */

public class CJLDraw implements IChartDraw<IVolume> {

    private Paint mRedPaint = new Paint(Paint.ANTI_ALIAS_FLAG);
    private Paint mGreenPaint = new Paint(Paint.ANTI_ALIAS_FLAG);
    private Paint ma5Paint = new Paint(Paint.ANTI_ALIAS_FLAG);
    private Paint ma10Paint = new Paint(Paint.ANTI_ALIAS_FLAG);
    private Paint mTargetPaint = new Paint(Paint.ANTI_ALIAS_FLAG);
    private Paint interestPaint = new Paint(Paint.ANTI_ALIAS_FLAG);
    private Paint mVolPaint = new Paint(Paint.ANTI_ALIAS_FLAG);//成交量
    private int pillarWidth = 0;

    public CJLDraw(BaseKChartView view) {
        Context context = view.getContext();
        mRedPaint.setColor(ContextCompat.getColor(context, R.color.chart_red));
        mGreenPaint.setColor(ContextCompat.getColor(context, R.color.chart_green));
        pillarWidth = DensityUtil.dp2px(4);
    }

    @Override
    public void drawTranslated(
            @Nullable IVolume lastPoint, @NonNull IVolume curPoint, float lastX, float curX,
            @NonNull Canvas canvas, @NonNull BaseKChartView view, int position) {

        drawHistogram(canvas, curPoint, lastPoint, curX, view, position);
//        view.drawChildLine(canvas, ma5Paint, lastX, lastPoint.getMA5Volume(), curX, curPoint.getMA5Volume());
        // view.drawChildLine(canvas, ma10Paint, lastX, lastPoint.getMA10Volume(), curX, curPoint.getMA10Volume());

        view.drawChildLine(canvas, interestPaint, lastX, lastPoint.getInterest(), curX, curPoint.getInterest());
    }

    private void drawHistogram(
            Canvas canvas, IVolume curPoint, IVolume lastPoint, float curX,
            BaseKChartView view, int position) {

        float r = pillarWidth / 2;
        float top = view.getChildY(curPoint.getVolume());
        int bottom = view.getChildRect().bottom;
        if (curPoint.getClosePrice() >= curPoint.getOpenPrice()) {//涨
            canvas.drawRect(curX - r, top, curX + r, bottom, mRedPaint);
        } else {
            canvas.drawRect(curX - r, top, curX + r, bottom, mGreenPaint);
        }

    }

    @Override
    public void drawText(@NonNull Canvas canvas, @NonNull BaseKChartView view, int position, float x, float y) {
        y = y - BaseKChartView.mChildTextPaddingY;
        x = BaseKChartView.mChildTextPaddingX;
        String text = "";
        IVolume point = (IVolume) view.getItem(position);
        text = "CJL";

        canvas.drawText(text, x, y, mTargetPaint);
        x += mTargetPaint.measureText(text);

        x += BaseKChartView.mTextPaddingLeft;
        text = "成交量:" + getValueFormatter().format(point.getVolume()) + " ";
        canvas.drawText(text, x, y, mVolPaint);
        x += mVolPaint.measureText(text);

//        x+=BaseKChartView.mTextPaddingLeft;
//        text = "MA5:" + getValueFormatter().format(point.getMA5Volume()) + " ";
//        canvas.drawText(text, x, y, ma5Paint);
//        x += ma5Paint.measureText(text);

        x += BaseKChartView.mTextPaddingLeft;
        text = "持仓量:" + getValueFormatter().format(point.getInterest()) + " ";
        canvas.drawText(text, x, y, interestPaint);
    }

    @Override
    public float getMaxValue(IVolume point) {
//        return Math.max(point.getVolume(), Math.max(point.getMA5Volume(), point.getMA10Volume()));
        return Math.max(point.getVolume(), point.getInterest());
    }

    @Override
    public void drawMaxAndMin(@NonNull BaseKChartView view, Canvas canvas, float maxX, float minX, IVolume maxPoint, IVolume minPoint) {

    }


    @Override
    public float getMinValue(IVolume point) {
       // return Math.min(point.getVolume(), Math.min(point.getMA5Volume(), point.getMA10Volume()));
        return Math.min(point.getVolume(), point.getInterest());
    }


    @Override
    public IValueFormatter getValueFormatter() {
        return new BigValueFormatter();
    }

    @Override
    public void setTargetColor(int... color) {
        if(color.length>0){
            this.mTargetPaint.setColor(color[0]);
            this.mVolPaint.setColor(color[1]);
        }

    }

    /**
     * 设置持仓量的颜色
     *
     * @param color
     */
    public void setInterestPaintColor(int color) {
        this.interestPaint.setColor(color);
    }

    /**
     * 设置 MA5 线的颜色
     *
     * @param color
     */
    public void setMa5Color(int color) {
        this.ma5Paint.setColor(color);
    }

    /**
     * 设置 MA10 线的颜色
     *
     * @param color
     */
    public void setMa10Color(int color) {
        this.ma10Paint.setColor(color);
    }

    public void setLineWidth(float width) {
        this.ma5Paint.setStrokeWidth(width);
        this.ma10Paint.setStrokeWidth(width);
        this.interestPaint.setStrokeWidth(width);
        this.mTargetPaint.setStrokeWidth(width);
        this.mVolPaint.setStrokeWidth(width);
    }

    /**
     * 设置文字大小
     *
     * @param textSize
     */
    public void setTextSize(float textSize) {
        this.ma5Paint.setTextSize(textSize);
        this.ma10Paint.setTextSize(textSize);
        this.mTargetPaint.setTextSize(textSize);
        this.interestPaint.setTextSize(textSize);
        this.mVolPaint.setTextSize(textSize);
    }

}
