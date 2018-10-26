package com.github.tifezh.kchartlib.chart.draw;

import android.graphics.Canvas;
import android.graphics.Paint;
import android.support.annotation.NonNull;
import android.support.annotation.Nullable;

import com.github.tifezh.kchartlib.chart.view.BaseKChartView;
import com.github.tifezh.kchartlib.chart.base.IChartDraw;
import com.github.tifezh.kchartlib.chart.base.IValueFormatter;
import com.github.tifezh.kchartlib.chart.comInterface.IKDJ;
import com.github.tifezh.kchartlib.chart.formatter.ValueFormatter;

/**
 *
 * Description KDJ实现类
 * Author puyantao
 * Email 1067899750@qq.com
 * Date 2018-10-26 17:39
 */

public class KDJDraw implements IChartDraw<IKDJ>{

    private Paint mKPaint = new Paint(Paint.ANTI_ALIAS_FLAG);
    private Paint mDPaint = new Paint(Paint.ANTI_ALIAS_FLAG);
    private Paint mJPaint = new Paint(Paint.ANTI_ALIAS_FLAG);
    private Paint mTargetPaint = new Paint(Paint.ANTI_ALIAS_FLAG);
    private Paint mTargetNamePaint = new Paint(Paint.ANTI_ALIAS_FLAG);

    public KDJDraw(BaseKChartView view) {
    }

    @Override
    public void drawTranslated(@Nullable IKDJ lastPoint, @NonNull IKDJ curPoint, float lastX, float curX, @NonNull Canvas canvas, @NonNull BaseKChartView view, int position) {
        view.drawChildLine(canvas, mKPaint, lastX, lastPoint.getK(), curX, curPoint.getK());
        view.drawChildLine(canvas, mDPaint, lastX, lastPoint.getD(), curX, curPoint.getD());
        view.drawChildLine(canvas, mJPaint, lastX, lastPoint.getJ(), curX, curPoint.getJ());
    }

    @Override
    public void drawText(@NonNull Canvas canvas, @NonNull BaseKChartView view, int position, float x, float y) {
        y = y - BaseKChartView.mChildTextPaddingY;
        x = BaseKChartView.mChildTextPaddingX;

        String text = "";
        IKDJ point = (IKDJ) view.getItem(position);

        text = "KDJ";
        canvas.drawText(text, x, y, mTargetPaint);
        x += mTargetPaint.measureText(text);


        x += BaseKChartView.mTextPaddingLeft;
        text = "KDJ(9,3,3)";
        canvas.drawText(text, x, y, mTargetNamePaint);
        x += mTargetNamePaint.measureText(text);


        x += BaseKChartView.mTextPaddingLeft;
        text = "K:" + view.formatValue(point.getK());
        canvas.drawText(text, x, y, mKPaint);
        x += mKPaint.measureText(text);

        x += BaseKChartView.mTextPaddingLeft;
        text = "D:" + view.formatValue(point.getD());
        canvas.drawText(text, x, y, mDPaint);
        x += mDPaint.measureText(text);

        x += BaseKChartView.mTextPaddingLeft;
        text = "J:" + view.formatValue(point.getJ());
        canvas.drawText(text, x, y, mJPaint);
    }

    @Override
    public float getMaxValue(IKDJ point) {
        return Math.max(point.getK(), Math.max(point.getD(), point.getJ()));
    }

    @Override
    public void drawMaxAndMin(@NonNull BaseKChartView view, Canvas canvas, float maxX, float minX, IKDJ maxPoint, IKDJ minPoint) {

    }


    @Override
    public float getMinValue(IKDJ point) {
        return Math.min(point.getK(), Math.min(point.getD(), point.getJ()));
    }

    @Override
    public IValueFormatter getValueFormatter() {
        return new ValueFormatter();
    }

    @Override
    public void setTargetColor(int... color) {
        if (color.length > 0) {
            this.mTargetPaint.setColor(color[0]);
            this.mTargetNamePaint.setColor(color[1]);
        }
    }

    /**
     * 设置K颜色
     */
    public void setKColor(int color) {
        mKPaint.setColor(color);
    }

    /**
     * 设置D颜色
     */
    public void setDColor(int color) {
        mDPaint.setColor(color);
    }

    /**
     * 设置J颜色
     */
    public void setJColor(int color) {
        mJPaint.setColor(color);
    }

    /**
     * 设置曲线宽度
     */
    public void setLineWidth(float width) {
        mKPaint.setStrokeWidth(width);
        mDPaint.setStrokeWidth(width);
        mJPaint.setStrokeWidth(width);
        mTargetPaint.setStrokeWidth(width);
        mTargetNamePaint.setStrokeWidth(width);
    }

    /**
     * 设置文字大小
     */
    public void setTextSize(float textSize) {
        mKPaint.setTextSize(textSize);
        mDPaint.setTextSize(textSize);
        mJPaint.setTextSize(textSize);
        mTargetPaint.setTextSize(textSize);
        mTargetNamePaint.setTextSize(textSize);
    }
}
