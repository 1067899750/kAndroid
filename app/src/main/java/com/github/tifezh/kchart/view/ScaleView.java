package com.github.tifezh.kchart.view;

import android.content.Context;
import android.graphics.Canvas;
import android.graphics.Color;
import android.graphics.DashPathEffect;
import android.graphics.Matrix;
import android.graphics.Paint;
import android.graphics.Path;
import android.graphics.Shader;
import android.util.Log;
import android.view.MotionEvent;
import android.view.ScaleGestureDetector;
import android.view.View;
import android.view.ViewGroup;

import java.util.Random;

public class ScaleView extends View {
    private ScaleGestureDetector mScaleDetector;
    private float mScaleFactor = 1.0f;

    public ScaleView(Context mContext) {
        super(mContext);
        mScaleDetector = new ScaleGestureDetector(mContext, new ScaleListener());
    }


    @Override
    public boolean onTouchEvent(MotionEvent ev) {
        mScaleDetector.onTouchEvent(ev);
        return true;
    }

    @Override
    protected void onMeasure(int widthMeasureSpec, int heightMeasureSpec) {
        super.onMeasure(widthMeasureSpec, heightMeasureSpec);

        int wm = MeasureSpec.getMode(widthMeasureSpec);
        int hm = MeasureSpec.getMode(heightMeasureSpec);
        int w = MeasureSpec.getSize(widthMeasureSpec);
        int h = MeasureSpec.getSize(heightMeasureSpec);
        int aa = MeasureSpec.makeMeasureSpec(widthMeasureSpec, MeasureSpec.EXACTLY);
        int bb = MeasureSpec.makeMeasureSpec(heightMeasureSpec, MeasureSpec.EXACTLY);
        Log.i("222222222222222", aa + "-----" + bb + "@@@@@@" + wm + "------" + hm);
    }

    @Override
    public void onDraw(Canvas canvas) {
        super.onDraw(canvas);
        canvas.save();
        Random random = new Random();
        canvas.drawARGB(150, random.nextInt(255), random.nextInt(255), random.nextInt(255));
        canvas.scale(mScaleFactor, mScaleFactor);
//        canvas.drawColor(Color.RED);

        Paint paint = new Paint();
        paint.setTextAlign(Paint.Align.LEFT);
        paint.setTextSize(25);
        paint.setTextSkewX(1);
        canvas.drawText("顶部", 35, 150, paint);
        canvas.drawLine(50, 190, 50, 1000, paint);
        canvas.drawText("右部", 600, 1010, paint);
        canvas.drawLine(50, 1000, 600, 1000, paint);


        Paint paint1 = new Paint();
        paint1.setTextAlign(Paint.Align.RIGHT);
        paint1.setTextSize(25);
        canvas.drawText("0", 45, 1000, paint1);
        canvas.drawText("100", 45, 900, paint1);
        canvas.drawText("200", 45, 800, paint1);
        canvas.drawText("300", 45, 700, paint1);
        canvas.drawText("400", 45, 600, paint1);
        canvas.drawText("500", 45, 500, paint1);
        canvas.drawText("600", 45, 400, paint1);
        canvas.drawText("700", 45, 300, paint1);
        canvas.drawText("800", 45, 200, paint1);

        canvas.drawLine(50, 890, 60, 890, paint);
        canvas.drawLine(50, 790, 60, 790, paint);
        canvas.drawLine(50, 690, 60, 690, paint);
        canvas.drawLine(50, 590, 60, 590, paint);
        canvas.drawLine(50, 490, 60, 490, paint);
        canvas.drawLine(50, 390, 60, 390, paint);
        canvas.drawLine(50, 290, 60, 290, paint);
        canvas.drawLine(50, 190, 60, 190, paint);

        Paint paint2 = new Paint();
        paint2.setTextAlign(Paint.Align.CENTER);
        paint2.setTextSize(25);


        canvas.drawText("9/1", 50, 1030, paint2);
        canvas.drawText("9/2", 150, 1030, paint2);
        canvas.drawText("9/3", 250, 1030, paint2);
        canvas.drawText("9/4", 350, 1030, paint2);
        canvas.drawText("9/5", 450, 1030, paint2);
        canvas.drawText("9/6", 550, 1030, paint2);

        Paint paint3 = new Paint();
        paint3.setTextAlign(Paint.Align.CENTER);
        paint3.setTextSize(25);
        paint3.setColor(Color.BLUE);
        paint3.setStrokeWidth(mScaleFactor * 5);
        for (int i = 1; i < 55; i++) {
            canvas.drawLine(50 + (i * 10), 1000, 50 + (i * 10), new Random().nextInt(1000), paint3);
        }

        canvas.drawLine(150, 1000, 150, 990, paint2);
        canvas.drawLine(250, 1000, 250, 990, paint2);
        canvas.drawLine(350, 1000, 350, 990, paint2);
        canvas.drawLine(450, 1000, 450, 990, paint2);
        canvas.drawLine(550, 1000, 550, 990, paint2);

        paint.setAntiAlias(true);
        paint.reset();
        paint.setStyle(Paint.Style.STROKE);
        paint.setPathEffect(new DashPathEffect(new float[]{50, 10}, 1));
        paint.setColor(Color.GREEN);
        paint.setStrokeWidth(5);
        Path path = new Path();
        path.addCircle(100, 900, 5, Path.Direction.CCW);
        path.addCircle(200, 350, 5, Path.Direction.CW);
        path.addCircle(300, 650, 5, Path.Direction.CW);
        path.addCircle(400, 250, 5, Path.Direction.CW);
        path.addCircle(500, 450, 5, Path.Direction.CCW);
        path.moveTo(50, 1000);
        path.lineTo(100, 900);
        path.lineTo(200, 350);
        path.lineTo(300, 650);
        path.lineTo(400, 250);
        path.lineTo(500, 450);
        canvas.drawPath(path, paint);
        paint.reset();
        paint.setStyle(Paint.Style.FILL);
        paint.setColor(Color.RED);
        paint.setStrokeCap(Paint.Cap.ROUND);
        paint.setStrokeWidth(25);
        paint.setDither(true);

        for (int i = 0; i < 10; i++) {
            canvas.drawPoint(100 + (i * 8), 900 - (i * 50), paint);
        }
        for (int i = 0; i < 10; i++) {
            canvas.drawPoint(200 + (i * 30), 400 + (i * 60), paint);
        }
//        canvas.drawPoint(100, 900, paint);
//        canvas.drawPoint(200, 350, paint);
//        canvas.drawPoint(300, 650, paint);
//        canvas.drawPoint(400, 250, paint);
//        canvas.drawPoint(500, 450, paint);

//        canvas.drawLine(50, 1000, 100, 900, paint);
//        canvas.drawLine(100, 900, 200, 350, paint);
//        canvas.drawLine(200, 350, 300, 650, paint);
//        canvas.drawLine(300, 650, 400, 250, paint);
//        canvas.drawLine(400, 250, 500, 450, paint);
        canvas.restore();
    }

    private class ScaleListener extends ScaleGestureDetector.SimpleOnScaleGestureListener {
        @Override
        public boolean onScale(ScaleGestureDetector detector) {
            mScaleFactor *= detector.getScaleFactor();
            // Don't let the object get too small or too large.
            mScaleFactor = Math.max(1.0f, Math.min(mScaleFactor, 1.5f));
            invalidate();
            return true;
        }
    }
}
