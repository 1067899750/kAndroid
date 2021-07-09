package com.github.tifezh.kchart.widget;

import android.content.Context;
import android.graphics.Bitmap;
import android.graphics.Canvas;
import android.graphics.Paint;
import android.graphics.drawable.Drawable;
import android.graphics.drawable.VectorDrawable;
import android.util.AttributeSet;
import android.util.Log;
import android.view.MotionEvent;
import android.view.View;

import com.github.tifezh.kchart.R;

/**
 * @author puyantao
 * @description 自定义评分控件
 * @date 2021/1/5 14:19
 */

public class YStarView extends View {

    private int starCount;  //星星个数
    private int rating;     //亮星星的个数，默认为0
    private int ratingH;    //星星的余数，控制半星
    private boolean change;    //是否可以滑动
    private boolean half;    //是否开启半星
    private int starSize;     //星星高度大小，星星一般正方形，宽度等于高度
    private Bitmap starT; //亮星星
    private Bitmap starF; //暗星星
    private Bitmap starH; //暗星星
    private Drawable fillStar;    //亮星星设置图
    private Drawable halfStar;    //半星星设置图
    private Drawable emptyStar;    //暗星星设置图
    private Paint mPaint;//画笔

    public YStarView(Context context) {
        super(context);
    }

    public YStarView(Context context, AttributeSet attrs) {
        super(context, attrs);
        init();
    }

    public YStarView(Context context, AttributeSet attrs, int defStyleAttr) {
        super(context, attrs, defStyleAttr);
        init();
    }

    @Override
    protected void onMeasure(int widthMeasureSpec, int heightMeasureSpec) {
        super.onMeasure(widthMeasureSpec, heightMeasureSpec);
        //这个是获取宽跟高，给下面计算星星大小用
        calculationSize(MeasureSpec.getSize(widthMeasureSpec), MeasureSpec.getSize(heightMeasureSpec));
    }

    @Override
    protected void onDraw(Canvas canvas) {
        super.onDraw(canvas);

        //画图
        for (int i = 0; i < starCount; i++) {//画多少颗星星
            if (rating > i) canvas.drawBitmap(starT, starSize * i, 0, mPaint);//画亮的星星
            else if (half && ratingH < 40 && ratingH > 5 && rating == i)
                canvas.drawBitmap(starH, starSize * i, 0, mPaint);//画半的星星
            else canvas.drawBitmap(starF, starSize * i, 0, mPaint);//画暗的的星星
        }
    }


    /**
     * 滑动和点击选择星星
     */
    @Override
    public boolean onTouchEvent(MotionEvent event) {
        if (change) {//是否可以点击或者滑动
            int x = (int) event.getX();
            if (x < 0) x = 0;
            if (x > getMeasuredWidth()) x = getMeasuredWidth();
            rating = x / starSize;
            ratingH = x % starSize;
            if (ratingH > 40) rating++;
            invalidate();//重新绘制
            return true;
        } else return false;
    }


    /**
     * 初始化参数
     */
    private void init() {

        mPaint = new Paint();
        mPaint.setAntiAlias(true);        // 打开抗锯齿

        half = false;//默认关闭半星
        change = false;//默认关闭点击滑动
        rating = 0;//默认评分为0
        starCount = 5;//默认五颗星星

        //这两个自行改成自己的图标
        fillStar = this.getResources().getDrawable(R.drawable.ic_full);//亮星星的图标
        emptyStar = this.getResources().getDrawable(R.drawable.ic_empty);//暗星星的图标
        halfStar = this.getResources().getDrawable(R.drawable.ic_half);//半星星的图标
    }


    /**
     * 计算星星大小
     */
    private void calculationSize(int width, int height) {
        //计算星星大小
        if (width > height * starCount) {
            starSize = height;
        } else starSize = width / starCount;

        //初始化图片
        starT = drawableToBitmap(fillStar, starSize);
        starF = drawableToBitmap(emptyStar, starSize);
        starH = drawableToBitmap(halfStar, starSize);
    }

    /**
     * drawable转bitmap
     *
     * @param drawable
     * @return bitmap
     */
    private Bitmap drawableToBitmap(Drawable drawable, int size) {
        if (drawable == null) return null;
        Bitmap bitmap = Bitmap.createBitmap(size, size, Bitmap.Config.ARGB_8888);
        Canvas canvas = new Canvas(bitmap);
        drawable.setBounds(0, 0, size, size);
        drawable.draw(canvas);
        return bitmap;
    }

    /**
     * 设置星星总数
     */
    public void setStarCount(int count) {
        starCount = count;
    }

    /**
     * 设置星星亮的颗数
     */
    public void setRating(int rating) {
        this.rating = rating;
    }

    /**
     * 设置星星是否可以点击和滑动改变
     */
    public void setChange(boolean change) {
        this.change = change;
    }

    /**
     * 设置星星是否可以点击和滑动改变
     */
    public void setHalf(boolean half) {
        this.half = half;
    }

    /**
     * 设置星星的样式
     */
    public void setStar(int fillStar, int emptyStar) {
        this.fillStar = this.getResources().getDrawable(fillStar);
        this.emptyStar = this.getResources().getDrawable(emptyStar);
    }

    /**
     * 获取亮星星的颗数
     */
    public int getRating() {
        return rating;
    }

}