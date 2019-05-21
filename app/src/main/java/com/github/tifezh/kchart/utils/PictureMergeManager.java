package com.github.tifezh.kchart.utils;

import android.app.Activity;
import android.content.Context;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.graphics.Canvas;
import android.graphics.Paint;
import android.graphics.Picture;
import android.graphics.Rect;
import android.util.Log;
import android.view.View;
import android.webkit.WebView;

import com.github.tifezh.kchart.R;

/**
 *
 * Description
 * Author puyantao
 * Email 1067899750@qq.com
 * Date 2019/5/21 13:59
 */
public class PictureMergeManager {
    private Context context;
    private static PictureMergeManager pictureMergeManager = null;

    public PictureMergeManager() {
    }

    public static synchronized PictureMergeManager getPictureMergeManager() {
        if (pictureMergeManager == null) {
            pictureMergeManager = new PictureMergeManager();
        }
        return pictureMergeManager;
    }

    //得到合并的图片
    public Bitmap getBitmap(Context mcontext, Bitmap bitmap, boolean isBaseMax) {
        this.context = mcontext;
//        return mergeBitmap_TB(captureWebView1(webView), getPictureBottom(), isBaseMax);
        return mergeBitmap_TB(bitmap, getPictureBottom(), isBaseMax);
    }

    /**
     * 对WebView进行截图
     *
     * @param webView
     * @return
     */
    private Bitmap captureWebView1(WebView webView) {
        Picture snapShot = webView.capturePicture();
        Bitmap bmp = Bitmap.createBitmap(snapShot.getWidth(), snapShot.getHeight(), Bitmap.Config.ARGB_8888);
        Canvas canvas = new Canvas(bmp);
        snapShot.draw(canvas);
        return bmp;
    }

    /**
     * @param view 需要截取图片的view
     * @return 截图
     */
    public Bitmap getScreenBitmap(Activity mcontext, View view) throws Exception {
        View screenView = mcontext.getWindow().getDecorView();
        screenView.setDrawingCacheEnabled(true);
        screenView.buildDrawingCache();
        //获取屏幕整张图片
        Bitmap bitmap = screenView.getDrawingCache();
        if (bitmap != null) {
            //需要截取的长和宽
            int outWidth = view.getWidth();
            int outHeight = view.getHeight();

            //获取需要截图部分的在屏幕上的坐标(view的左上角坐标）
            int[] viewLocationArray = new int[2];
            view.getLocationOnScreen(viewLocationArray);
            //从屏幕整张图片中截取指定区域
            bitmap = Bitmap.createBitmap(bitmap, viewLocationArray[0], viewLocationArray[1], outWidth, outHeight);
        }
        return bitmap;
    }

    public Bitmap getCanvasBitmap(Activity context, String value, int height) {
        int width = context.getWindowManager().getDefaultDisplay().getWidth();
        Bitmap b = Bitmap.createBitmap(width, dp2px(context, height), Bitmap.Config.ARGB_8888);
        Canvas cvs = new Canvas(b); //然后在cvs上的操作也都会在bitmap上进行记录
        cvs.drawColor(context.getResources().getColor(R.color.c2A2D4F));
        //设置画笔
        Paint paint = new Paint();
        paint.setColor(context.getResources().getColor(R.color.cFFFFFF));
        paint.setTextSize(60);//设置字体大小
        paint.setAntiAlias(true);
        paint.setTextAlign(Paint.Align.CENTER);
        //设置字体的高度
        Paint.FontMetrics fontMetrics = paint.getFontMetrics();
        float fontHeight = fontMetrics.bottom - fontMetrics.top;
        float textBaseY = dp2px(context, height) - (dp2px(context, height) - fontHeight) / 2 - fontMetrics.bottom;
        cvs.drawText(value, width / 2, textBaseY, paint);
        return b;
    }

    //添加底部二维码图片
    private Bitmap getPictureBottom() {
        Bitmap bitmap = BitmapFactory.decodeResource(context.getResources(), R.mipmap.erweima);
        return bitmap;
    }

    /**
     * 把两个位图覆盖合成为一个位图，上下拼接
     * isBaseMax 是否以高度大的位图为准，true则小图等比拉伸，false则大图等比压缩
     *
     * @return
     */
    public Bitmap mergeBitmap_TB(Bitmap topBitmap, Bitmap bottomBitmap, boolean isBaseMax) {
        if (topBitmap == null || topBitmap.isRecycled() || bottomBitmap == null || bottomBitmap.isRecycled()) {
            Log.i("错误", "topBitmap=" + topBitmap + ";bottomBitmap=" + bottomBitmap);
            return null;
        }
        int width = 0;
        if (isBaseMax) {
            width = topBitmap.getWidth() > bottomBitmap.getWidth() ? topBitmap.getWidth() : bottomBitmap.getWidth();
        } else {
            width = topBitmap.getWidth() < bottomBitmap.getWidth() ? topBitmap.getWidth() : bottomBitmap.getWidth();
        }
        Bitmap tempBitmapT = topBitmap;
        Bitmap tempBitmapB = bottomBitmap;
        if (topBitmap.getWidth() != width) {
            tempBitmapT = Bitmap.createScaledBitmap(topBitmap, width, (int) (topBitmap.getHeight() * 1f / topBitmap.getWidth() * width), false);
        } else if (bottomBitmap.getWidth() != width) {
            tempBitmapB = Bitmap.createScaledBitmap(bottomBitmap, width, (int) (bottomBitmap.getHeight() * 1f / bottomBitmap.getWidth() * width), false);
        }

        int height = tempBitmapT.getHeight() + tempBitmapB.getHeight();

        Bitmap bitmap = Bitmap.createBitmap(width, height, Bitmap.Config.ARGB_8888);
        Canvas canvas = new Canvas(bitmap);

        Rect topRect = new Rect(0, 0, tempBitmapT.getWidth(), tempBitmapT.getHeight());
        Rect bottomRect = new Rect(0, 0, tempBitmapB.getWidth(), tempBitmapB.getHeight());

        Rect bottomRectT = new Rect(0, tempBitmapT.getHeight(), width, height);

        canvas.drawBitmap(tempBitmapT, topRect, topRect, null);
        canvas.drawBitmap(tempBitmapB, bottomRect, bottomRectT, null);
        return bitmap;
    }

    public int dp2px(Activity context, float dp) {
        final float scale = context.getResources().getDisplayMetrics().density;
        return (int) (dp * scale + 0.5f);
    }

}
