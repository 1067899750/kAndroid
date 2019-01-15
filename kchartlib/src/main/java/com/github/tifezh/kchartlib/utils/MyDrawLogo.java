package com.github.tifezh.kchartlib.utils;

import android.content.Context;
import android.graphics.Canvas;
import android.graphics.Color;
import android.graphics.ColorFilter;
import android.graphics.Paint;
import android.graphics.PixelFormat;
import android.graphics.drawable.BitmapDrawable;
import android.graphics.drawable.Drawable;
import android.support.annotation.IntRange;
import android.support.annotation.NonNull;
import android.support.annotation.Nullable;

/**
 *
 * Description 水印
 * Author puyantao
 * Email 1067899750@qq.com
 * Date 2018-12-13 16:26
 */


public class MyDrawLogo extends BitmapDrawable {
    private Paint mPaint = new Paint();
    private Context mContext;
    private int mDegress;//角度
    private String mTextStr;


    public MyDrawLogo(Context context, int degress) {
        this.mContext = context;
        this.mDegress = degress;
    }

    @Override
    public void draw(@NonNull Canvas canvas) {
        mTextStr = "水印。。。";

        int width = (int) (getBounds().right * 1.1f);
        int height = (int) (getBounds().bottom * 1.5f);

        canvas.drawColor(Color.parseColor("#00000000"));
        mPaint.setColor(Color.parseColor("#ffffff"));

        mPaint.setAntiAlias(true);
        mPaint.setDither(true);
        mPaint.setStyle(Paint.Style.FILL);
        mPaint.setStrokeWidth(30);
        mPaint.setTextSize(sp2px(60));
        canvas.save();
        canvas.rotate(mDegress);
        float textWidth = mPaint.measureText(mTextStr);
        int index = 0;
        for (int positionY = height / 10; positionY <= height; positionY += height / 10 + 20) {
            float fromX = -width + (index++ % 2) * textWidth;
            for (float positionX = fromX; positionX < width; positionX += textWidth * 1.5) {
                canvas.drawText(mTextStr, positionX, positionY, mPaint);
            }
        }
        canvas.restore();
    }

    @Override
    public void setAlpha(@IntRange(from = 0, to = 255) int alpha) {

    }

    @Override
    public void setColorFilter(@Nullable ColorFilter colorFilter) {

    }

    @Override
    public int getOpacity() {
        return PixelFormat.UNKNOWN;
    }


    public int sp2px(float spValue) {
        final float fontScale = mContext.getResources().getDisplayMetrics().scaledDensity;
        return (int) (spValue * fontScale + 0.5f);
    }

    protected int dp2px(float dp) {
        final float scale = mContext.getResources().getDisplayMetrics().density;
        return (int) (dp * scale + 0.5f);
    }

}
