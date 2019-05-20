package com.github.tifezh.kchartlib.utils;

import android.content.Context;
import android.graphics.Bitmap;
import android.graphics.Canvas;
import android.graphics.Matrix;
import android.graphics.Paint;

import com.github.tifezh.kchartlib.R;


/**
 *
 * Description  Bitmap工具类
 * Author puyantao
 * Email 1067899750@qq.com
 * Date 2018-12-13 14:24
 */

public class BitmapUntils {
    /**
     * 添加logo水印
     *
     * @param src    原图片
     * @param logo   logo
     * @return 水印图片
     */
    public static Bitmap createWaterMaskImage(Bitmap src, Bitmap logo) {
        if (src == null) {
            return null;
        }
        //原图宽高
        int w = src.getWidth();
        int h = src.getHeight();
        //logo宽高
        int ww = logo.getWidth();
        int wh = logo.getHeight();
        //创建一个和原图宽高一样的bitmap
        Bitmap newBitmap = Bitmap.createBitmap(w, h, Bitmap.Config.ARGB_4444);
        //创建
        Canvas canvas = new Canvas(newBitmap);
        //绘制原始图片
        canvas.drawBitmap(src, 0, 0, null);
        //新建矩阵
        Matrix matrix = new Matrix();
        //对矩阵作缩放处理
        matrix.postScale(0.1f, 0.1f);
        //对矩阵作位置偏移，移动到底部中间的位置
        matrix.postTranslate(0.5f * w - 0.05f * ww, h - 0.1f * wh - 3);
        //将logo绘制到画布上并做矩阵变换
        canvas.drawBitmap(logo, matrix, null);
        // 保存状态
        canvas.save();// 保存
        // 恢复状态
        canvas.restore();
        return newBitmap;
    }


    /**
     * 给图片添加水印
     *
     * @param context
     * @param canvas  画布
     * @param width   宽
     * @param height  高
     */
    public static void drawTextToBitmap(Context context, Canvas canvas, int width, int height) {
        //要添加的文字
        String logo = "天下金屬";
        //新建画笔，默认style为实心
        Paint paint = new Paint();
        //设置颜色，颜色可用Color.parseColor("#6b99b9")代替
        paint.setColor(context.getResources().getColor(R.color.c9EB2CD));
        //设置透明度
        paint.setAlpha(80);
        //抗锯齿
        paint.setAntiAlias(true);
        //画笔粗细大小
        paint.setTextSize(DensityUtil.dp2px(30));
        //保存当前画布状态
        canvas.save();
        //画布旋转-30度
        canvas.rotate(-30);
        //获取要添加文字的宽度
        float textWidth = paint.measureText(logo);
        int index = 0;
        //行循环，从高度为0开始，向下每隔80dp开始绘制文字
        for (int positionY = -DensityUtil.dp2px(30); positionY <= height; positionY += DensityUtil.dp2px(80)) {
            //设置每行文字开始绘制的位置,0.58是根据角度算出tan30°,后面的(index++ % 2) * textWidth是为了展示效果交错绘制
            float fromX = -0.58f * height + (index++ % 2) * textWidth;
            //列循环，从每行的开始位置开始，向右每隔2倍宽度的距离开始绘制（文字间距1倍宽度）
            for (float positionX = fromX; positionX < width; positionX += textWidth * 2) {
                //绘制文字
                canvas.drawText(logo, positionX, positionY, paint);
            }
        }
        //恢复画布状态
        canvas.restore();
    }


}
















