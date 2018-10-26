package com.github.tifezh.kchartlib.chart.draw;

import android.graphics.Canvas;
import android.graphics.Paint;
import android.support.annotation.NonNull;
import android.support.annotation.Nullable;

import com.github.tifezh.kchartlib.chart.view.BaseKChartView;
import com.github.tifezh.kchartlib.chart.base.IChartDraw;
import com.github.tifezh.kchartlib.chart.base.IValueFormatter;
import com.github.tifezh.kchartlib.chart.comInterface.IBOLL;
import com.github.tifezh.kchartlib.chart.formatter.ValueFormatter;

/**
 * BOLL实现类
 * Created by tifezh on 2016/6/19.
 */

public class BOLLDraw  implements IChartDraw<IBOLL> {

    private Paint mUpPaint = new Paint(Paint.ANTI_ALIAS_FLAG);
    private Paint mMbPaint = new Paint(Paint.ANTI_ALIAS_FLAG);
    private Paint mDnPaint = new Paint(Paint.ANTI_ALIAS_FLAG);
    private Paint mTargetPaint = new Paint(Paint.ANTI_ALIAS_FLAG);
    private Paint mTargetNamePaint = new Paint(Paint.ANTI_ALIAS_FLAG);
    public BOLLDraw(BaseKChartView view) {

    }

    @Override
    public void drawTranslated(@Nullable IBOLL lastPoint, @NonNull IBOLL curPoint, float lastX, float curX, @NonNull Canvas canvas, @NonNull BaseKChartView view, int position) {
        view.drawChildLine(canvas, mUpPaint, lastX, lastPoint.getUp(), curX, curPoint.getUp());
        view.drawChildLine(canvas, mMbPaint, lastX, lastPoint.getMb(), curX, curPoint.getMb());
        view.drawChildLine(canvas, mDnPaint, lastX, lastPoint.getDn(), curX, curPoint.getDn());
    }

    @Override
    public void drawText(@NonNull Canvas canvas, @NonNull BaseKChartView view, int position, float x, float y) {
        y = y - BaseKChartView.mChildTextPaddingY;
        x =  BaseKChartView.mChildTextPaddingX;

        String text = "";
        IBOLL point = (IBOLL) view.getItem(position);
        text = "T:" + view.formatValue(point.getUp());
        canvas.drawText(text, x, y, mUpPaint);
        x += mUpPaint.measureText(text);
        text = "M:" + view.formatValue(point.getMb());
        canvas.drawText(text, x, y, mMbPaint);
        x += mMbPaint.measureText(text);
        text = "B:" + view.formatValue(point.getDn());
        canvas.drawText(text, x, y, mDnPaint);
    }

    @Override
    public float getMaxValue(IBOLL point) {
        if (Float.isNaN(point.getUp())) {
            return point.getMb();
        }
        return point.getUp();
    }

    @Override
    public void drawMaxAndMin(@NonNull BaseKChartView view, Canvas canvas, float maxX, float minX, IBOLL maxPoint, IBOLL minPoint) {

    }


    @Override
    public float getMinValue(IBOLL point) {
        if (Float.isNaN(point.getDn())) {
            return point.getMb();
        }
        return point.getDn();
    }


    @Override
    public IValueFormatter getValueFormatter() {
        return new ValueFormatter();
    }

    @Override
    public void setTargetColor(int... color) {
        if(color.length>0){
            mTargetPaint.setColor(color[0]);
            mTargetNamePaint.setColor(color[1]);
        }

    }


    /**
     * 设置up颜色
     */
    public void setUpColor(int color) {
        mUpPaint.setColor(color);
    }

    /**
     * 设置mb颜色
     *
     * @param color
     */
    public void setMbColor(int color) {
        mMbPaint.setColor(color);
    }

    /**
     * 设置dn颜色
     */
    public void setDnColor(int color) {
        mDnPaint.setColor(color);
    }

    /**
     * 设置曲线宽度
     */
    public void setLineWidth(float width) {
        mUpPaint.setStrokeWidth(width);
        mMbPaint.setStrokeWidth(width);
        mDnPaint.setStrokeWidth(width);
        mTargetPaint.setStrokeWidth(width);
        mTargetNamePaint.setStrokeWidth(width);
    }

    /**
     * 设置文字大小
     */
    public void setTextSize(float textSize) {
        mUpPaint.setTextSize(textSize);
        mMbPaint.setTextSize(textSize);
        mDnPaint.setTextSize(textSize);
        mTargetPaint.setTextSize(textSize);
        mTargetNamePaint.setTextSize(textSize);
    }
}
