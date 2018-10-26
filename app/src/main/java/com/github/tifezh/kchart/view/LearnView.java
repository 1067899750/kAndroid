package com.github.tifezh.kchart.view;

import android.app.Activity;
import android.content.Context;
import android.graphics.Canvas;
import android.graphics.Color;
import android.graphics.Paint;
import android.graphics.Path;
import android.support.annotation.Nullable;
import android.util.AttributeSet;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.MotionEvent;
import android.view.ScaleGestureDetector;
import android.view.View;
import android.widget.Toast;

import com.github.tifezh.kchart.R;

import java.util.ArrayList;
import java.util.List;
import java.util.Random;

public class LearnView extends View {
    private int width;
    private int height;
    private Context context;
    private int HLineNum = 6;
    private int VLineNum = 5;
    private int mPaddingLeft = dp2px(getContext(), 30);
    private int mPaddingRight = dp2px(getContext(), 30);
    private int mPaddingBottom = dp2px(getContext(), 30);
    private float lastHHeight = 0.0f;//绘制纵向的高度
    private String[] mHLine = {"08/28", "09/03", "09/15", "09/21", "09/27"};
    private List<String> mHDatalist = new ArrayList();
    private String[] mVLine = {"升15", "升5", "0", "贴5", "贴15"};
    private List<String> mVDatalist = new ArrayList();

    private ScaleGestureDetector mScaleDetector;
    private float mScaleFactor = 1.0f;

    private boolean isLoogPress = false;
    private float loogPressx;
    private float loogPressy;
    private boolean scaleFactor = false;//是否在缩放
    private long presstime;
    private long uptime;
    private boolean isClickArea = false;//滑动时设置为ture不能点击
    private Paint paint;

    public LearnView(Context context) {
        super(context);
        this.context = context;
        mScaleDetector = new ScaleGestureDetector(context, new ScaleListener());
        for (int i = 0; i < mHLine.length; i++) {
            mHDatalist.add(mHLine[i]);
        }
        for (int i = 0; i < mVLine.length; i++) {
            mVDatalist.add(mVLine[i]);
        }

        paint = new Paint();
    }

    public LearnView(Context context, @Nullable AttributeSet attrs) {
        super(context, attrs);
    }

    public LearnView(Context context, @Nullable AttributeSet attrs, int defStyleAttr) {
        super(context, attrs, defStyleAttr);
    }

    @Override
    public boolean onTouchEvent(MotionEvent event) {
        Log.i("3333333333333", mScaleDetector.getScaleFactor() + "");
        mScaleDetector.onTouchEvent(event);
        loogPressx = event.getRawX();
        loogPressy = event.getRawY();

        switch (event.getAction() & MotionEvent.ACTION_MASK) {
            case MotionEvent.ACTION_DOWN:
                presstime = System.currentTimeMillis();
                isClickArea = false;
                isLoogPress = true;
                invalidate();
                break;
            case MotionEvent.ACTION_MOVE:
                isClickArea = true;
                isLoogPress = true;
                invalidate();
                break;
            case MotionEvent.ACTION_UP:
                if (width / 2 >= loogPressx && lastHHeight >= loogPressy && !isClickArea) {
                    uptime = System.currentTimeMillis();
                    long mtime = (uptime - presstime);
                    if (mtime < 500) {
                        Toast.makeText(context, mtime + "--点击", Toast.LENGTH_LONG).show();
                    } else {
                        Toast.makeText(context, mtime + "--长按点击", Toast.LENGTH_LONG).show();
                    }

                }
                isLoogPress = false;
                invalidate();
                break;
            case MotionEvent.ACTION_CANCEL:
                isLoogPress = false;
                invalidate();

                break;

        }

        return true;
//        return super.onTouchEvent(event);
    }


    @Override
    protected void onLayout(boolean changed, int left, int top, int right, int bottom) {
        super.onLayout(changed, left, top, right, bottom);
//        LayoutInflater.from(getContext()).inflate(R.layout.layout_learnview, null, true);
    }

    @Override
    protected void onMeasure(int widthMeasureSpec, int heightMeasureSpec) {
        super.onMeasure(widthMeasureSpec, heightMeasureSpec);
        width = getMeasuredWidth();
        height = getMeasuredHeight();

        Log.i("1111111111111", width + "&&&&&&&&&&&" + height);
//        Log.i("1111111111111", getSuggestedMinimumWidth() + "_________" + getSuggestedMinimumHeight());
    }

    @Override
    protected void onSizeChanged(int w, int h, int oldw, int oldh) {
        super.onSizeChanged(w, h, oldw, oldh);
        width = w;
        height = h;
        Log.i("1111111111111333", w + "____" + h + "_____" + oldw + "_____" + oldh);
    }

    @Override
    protected void onDraw(Canvas canvas) {
        super.onDraw(canvas);
        canvas.drawColor(getResources().getColor(R.color.c2A2D4F));

        paint.setColor(getResources().getColor(R.color.cffffff));
        paint.setStrokeWidth(dp2px(context, 0.2f));
        paint.setAntiAlias(true);
        canvas.save();
        if (height == 0 || width == 0) {
            return;
        }
        //画横线
        lastHHeight = (height - mPaddingBottom);
        float hline = lastHHeight / HLineNum;
        for (int i = 0; i <= HLineNum; i++) {
            canvas.drawLine(0, (hline * i) + 1, width, (hline * i) + 1, paint);
        }
        //画纵线
        float vline = (width - mPaddingLeft - mPaddingRight) / VLineNum;
        for (int i = 0; i <= VLineNum; i++) {
            canvas.drawLine((vline * i) + mPaddingLeft, 0, (vline * i) + mPaddingLeft, lastHHeight, paint);
        }
        paint.reset();
        //画文字
        paint.setAntiAlias(true);
        paint.setTextSize(dp2px(getContext(), 12));
        paint.setTextAlign(Paint.Align.LEFT);
        paint.setColor(getResources().getColor(R.color.c6A798E));
        Paint.FontMetrics fm = paint.getFontMetrics();
        float textHeight = fm.descent - fm.ascent;
        float baseLine = (textHeight - fm.bottom - fm.top) / 2;


        float xx = 0.0f;
        for (int i = 0; i < mHDatalist.size(); i++) {
            if (i == 0) {
                xx = mPaddingLeft / 3;
            } else if (i == 1) {
                xx = vline + mPaddingLeft;
            } else if (i == 2) {
                xx = width / 2 - mPaddingLeft / 2;
            } else if (i == 3) {
                xx = vline * 4;
            } else if (i == 4) {
                xx = vline * 5 + mPaddingLeft / 2;
            }
            canvas.drawText(mHDatalist.get(i), xx, lastHHeight + baseLine, paint);
        }
        int mSetXText = dp2px(getContext(), 20);
        float yy = 0.0f;
        for (int i = 0; i < mVDatalist.size(); i++) {
            if (i == 0) {
                yy = baseLine;
            } else if (i == 1) {
                yy = lastHHeight / 4;
            } else if (i == 2) {
                yy = lastHHeight / 2 + (baseLine / 3);
            } else if (i == 3) {
                yy = lastHHeight / 2 + lastHHeight / 4;
            } else if (i == 4) {
                yy = lastHHeight - (baseLine / 2);
            }
            canvas.drawText(mVDatalist.get(i), mSetXText, yy, paint);//height / HLineNum * i*1.5f + baseLine
        }

        setLoogLine(canvas, paint);

        paint.reset();
        canvas.save();
        canvas.scale(mScaleFactor, 1.0f);
        paint.setStrokeWidth(2);
        paint.setColor(getResources().getColor(R.color.c6774FF));
        paint.setStyle(Paint.Style.STROKE);
        Path path = new Path();
        path.moveTo(mPaddingLeft, lastHHeight);
        path.quadTo(mPaddingLeft, lastHHeight / 2, 100, 300);
        path.quadTo(120, 150, 200, 350);
        path.quadTo(230, 430, 250, 500);
        path.quadTo(330, 700, 350, 200);
        path.quadTo(380, 100, 450, 700);
        path.quadTo(520, 1000, 600, 300);
        path.quadTo(700, 100, 800, 1500);
//        path.lineTo(width / 20, lastHHeight / 2);
//        path.lineTo(width / 19, lastHHeight / 2);
//        path.lineTo(width / 18, lastHHeight / 4);
//        path.lineTo(width / 17, lastHHeight / 5);
//        path.lineTo(width / 16, lastHHeight / 3);
//        path.lineTo(width / 15, lastHHeight / 6);
//        path.lineTo(width / 14, lastHHeight / 2);
//        path.lineTo(width / 13, lastHHeight / 4);
//        path.lineTo(width / 12, lastHHeight / 3);
//        path.lineTo(width / 11, lastHHeight / 7);
//        path.lineTo(width / 10, lastHHeight / 7);
//        path.lineTo(width / 9, lastHHeight / 2);
//        path.lineTo(width / 8, lastHHeight / 4);
//        path.lineTo(width / 7, lastHHeight / 7);
//        path.lineTo(width / 6, lastHHeight / 3);
//        path.lineTo(width / 5, lastHHeight / 6);
//        path.lineTo(width / 4, lastHHeight / 2);
//        path.lineTo(width / 3, lastHHeight / 8);
//        path.lineTo(width / 2, lastHHeight / 3);
//        path.lineTo(width / 1, lastHHeight / 7);
        canvas.drawPath(path, paint);
        canvas.restore();
    }

    private void setLoogLine(Canvas canvas, Paint paint) {
        if (isLoogPress) {
            paint.reset();
            //画实现
            paint.setAntiAlias(true);
            paint.setStrokeWidth(1);
            paint.setColor(getResources().getColor(R.color.cffffff));
            canvas.drawLine(0, Math.min(loogPressy, lastHHeight), width, Math.min(loogPressy, lastHHeight), paint);
            canvas.drawLine(loogPressx, 0, loogPressx, lastHHeight, paint);
        }
    }

    //画多少横线
    public void setHLineNum(int HLineNum) {
        this.HLineNum = HLineNum;
    }

    //画多少纵线
    public void setVLineNum(int VLineNum) {
        this.VLineNum = VLineNum;
    }

    //dp转px
    public int dp2px(Context context, float dp) {
        final float scale = context.getResources().getDisplayMetrics().density;
        return (int) (dp * scale + 0.5f);
    }

    private class ScaleListener extends ScaleGestureDetector.SimpleOnScaleGestureListener {
        @Override
        public boolean onScaleBegin(ScaleGestureDetector detector) {
            scaleFactor = true;
            return super.onScaleBegin(detector);
        }

        @Override
        public boolean onScale(ScaleGestureDetector detector) {
            mScaleFactor *= detector.getScaleFactor();
            // Don't let the object get too small or too large.
            mScaleFactor = Math.max(1.0f, Math.min(mScaleFactor, 2f));
            invalidate();
            return true;
        }

        @Override
        public void onScaleEnd(ScaleGestureDetector detector) {
            scaleFactor = false;
            super.onScaleEnd(detector);
        }
    }
}
