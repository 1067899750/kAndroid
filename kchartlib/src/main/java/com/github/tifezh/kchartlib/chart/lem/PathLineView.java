package com.github.tifezh.kchartlib.chart.lem;

import android.content.Context;
import android.graphics.Canvas;
import android.graphics.Paint;
import android.graphics.Path;
import android.graphics.drawable.Drawable;
import android.support.annotation.Nullable;
import android.util.AttributeSet;
import android.view.View;

import com.github.tifezh.kchartlib.R;
import com.github.tifezh.kchartlib.utils.CommonUtils;
/**
 *
 * Description
 * Author puyantao
 * Email 1067899750@qq.com
 * Date 2018-10-26 17:36
 */

public class PathLineView extends View {

    //mPath用于画线
    private Path mPath = new Path();
    //mFillPath用于封闭后形成一个图形去切割画布
    private Path mFillPath = new Path();
    private Paint mPaint = new Paint();
    private int mViewWidth;
    private int mViewHeight;

    public  PathLineView(Context context) {
        this(context, null);
    }

    public PathLineView(Context context, @Nullable AttributeSet attrs) {
        this(context, attrs, 0);
    }

    public PathLineView(Context context, @Nullable AttributeSet attrs, int defStyleAttr) {
        super(context, attrs, defStyleAttr);
        init();
    }

    private void init() {
        int fillAlpha = 255;
        int fillColor = getResources().getColor(R.color.chart_tab_indicator);
        int color = (fillAlpha << 24) | (fillColor & 0xffffff);
        mPaint.setColor(color);
        mPaint.setStrokeWidth(6);
        mPaint.setStyle(Paint.Style.STROKE);
    }

    @Override
    protected void onMeasure(int widthMeasureSpec, int heightMeasureSpec) {
        super.onMeasure(widthMeasureSpec, heightMeasureSpec);
        mViewWidth = MeasureSpec.getSize(widthMeasureSpec);
        mViewHeight = MeasureSpec.getSize(heightMeasureSpec);
    }

    @Override
    protected void onDraw(Canvas canvas) {
        super.onDraw(canvas);
        //x轴
        canvas.drawLine(0, mViewHeight, mViewWidth, mViewHeight, mPaint);
        //y轴
        canvas.drawLine(0, 0, 0, mViewHeight, mPaint);


        float startX = 150;
        float startY = 200;
        float topY = 150;
        float endX = 500;
        float endY = 150;
        mPath.reset();
        mPath.moveTo(startX,startY);
        mPath.lineTo(300,topY);
        mPath.lineTo(endX,endY);
        canvas.drawPath(mPath,mPaint);
        //绘制封闭Path用于切割
        mFillPath.reset();
        mFillPath.addPath(mPath);
        mFillPath.lineTo(endX,mViewHeight);
        mFillPath.lineTo(startX,mViewHeight);
        mFillPath.close();

        //默认开启硬件加速的情况下 clipPath 需要SDK >18
        //如果关闭硬件加速，则不需要SDK要求
        if(clipPathSupported()) {
            int save = canvas.save();
            //将画布切割成path的形状
            canvas.clipPath(mFillPath);

            Drawable drawable = getFillDrawable();
            drawable.setBounds((int)startX,(int)topY,mViewWidth,mViewHeight);
            drawable.draw(canvas);
            canvas.restoreToCount(save);
        }else{
            canvas.drawPath(mPath, mPaint);
        }

    }


    /**
     * Clip path with hardware acceleration only working properly on API level 18 and above.
     *
     * @return
     */
    private boolean clipPathSupported() {
        return CommonUtils.getSDKInt() >= 18;
    }

    private Drawable getFillDrawable(){
        return getResources().getDrawable(R.drawable.path_fill_red);
    }

}

