package com.github.tifezh.kchartlib.utils;

import android.graphics.Paint;
import android.graphics.Rect;

/**
 * @Describe
 * @Author puyantao
 * @Email 1067899750@qq.com
 * @create 2019/5/22 15:30
 */
public class TextUntils {

    /**
     *  基线在屏幕上的位置
     * @param paint
     * @return
     */
    public static float getFontBaseLineHeight(Paint paint) {
        Paint.FontMetrics fm = paint.getFontMetrics();
        float textHeight = fm.descent - fm.ascent;
        float baseLine = (textHeight - fm.bottom - fm.top) / 2;
        return baseLine;
    }

    /**
     *  文字的高度
     * @param paint
     * @return
     */
    public static float getFontHeight(Paint paint) {
        Paint.FontMetrics fm = paint.getFontMetrics();
        float textHeight = fm.descent - fm.ascent;
        return textHeight;
    }


    /**
     *  文字的高度
     * @param paint
     * @return
     */
    public static float getRectHeight(Paint paint) {
        Paint.FontMetrics fm = paint.getFontMetrics();
        float textHeight = fm.bottom - fm.top;
        return textHeight;
    }



    public static Rect getFobtWeightAndHeight(Paint paint, String text){
        Rect rect = new Rect();
        paint.getTextBounds(text, 0, text.length(), rect);
        int width = rect.width();//文本的宽度
        int height = rect.height();//文本的高度
        return rect;
    }



}















