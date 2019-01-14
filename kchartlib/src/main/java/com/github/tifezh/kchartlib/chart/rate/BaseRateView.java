package com.github.tifezh.kchartlib.chart.rate;

import android.animation.ValueAnimator;
import android.content.Context;
import android.content.res.Configuration;
import android.database.DataSetObserver;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.graphics.Canvas;
import android.graphics.Color;
import android.graphics.Paint;
import android.graphics.Rect;
import android.support.annotation.ColorRes;
import android.support.annotation.DimenRes;
import android.support.v4.content.ContextCompat;
import android.support.v4.view.GestureDetectorCompat;
import android.util.AttributeSet;
import android.util.Log;
import android.view.MotionEvent;
import android.view.ScaleGestureDetector;

import com.github.tifezh.kchartlib.R;
import com.github.tifezh.kchartlib.chart.base.IAdapter;
import com.github.tifezh.kchartlib.chart.formatter.DateFormatter;
import com.github.tifezh.kchartlib.chart.rate.base.IRate;
import com.github.tifezh.kchartlib.chart.rate.base.IRateDraw;
import com.github.tifezh.kchartlib.utils.DateUtil;
import com.github.tifezh.kchartlib.utils.DensityUtil;
import com.github.tifezh.kchartlib.utils.StrUtil;

import java.util.Date;

/**
 * Description k线图
 * Author puyantao
 * Email 1067899750@qq.com
 * Date 2018-12-4 10:43
 */

public abstract class BaseRateView extends ScrollAndScaleView {
    protected float mTranslateX = Float.MIN_VALUE;
    protected float mMainScaleY = 1;

    protected float mDataLen = 0;  //数据的长度

    protected float downX;
    protected float downY;
    protected float mMainMaxValue; //最大值
    protected float mMainMinValue;//最小值

    protected float mPointWidth = 18; //点的宽度

    protected float mOverScrollRange = 0;   //设置超出右方后可滑动的范围

    protected int mWidth; //试图宽
    protected int mHeight;//试图高

    protected int mMaxPoint = 240; //最多点的个数
    protected int mPoint = 60; //默认点的个数
    protected int mMinPoint = 20;//最少点的个数

    //点的索引
    protected int mStartIndex = 0;
    protected int mStopIndex = 0;
    protected int mGridRows = 7;
    protected int mGridColumns = 5;

    protected int mSelectedIndex;

    protected int mMainHeight;//主视图高
    protected int mItemCount;//当前点的个数

    protected Paint mGridPaint = new Paint(Paint.ANTI_ALIAS_FLAG); //网格线画笔
    protected Paint mBackgroundPaint = new Paint(Paint.ANTI_ALIAS_FLAG); //背景

    protected IRateDraw mMainDraw;
    protected IAdapter mAdapter;
    protected CallOnClick mCallOnClick;//添加视图点击事件

    protected ValueAnimator mAnimator;

    protected long mAnimationDuration = 500;
    protected long mClickTime = 0; //点击时间
    protected OnSelectedChangedListener mOnSelectedChangedListener = null;
    protected Rect mMainRect;

    protected int mBackgroundColor;
    protected Bitmap mBitmapLogo = BitmapFactory.decodeResource(getContext().getResources(), R.drawable.ic_app_logo);
    protected int mBasePaddingLeft = DensityUtil.dp2px(25);  //左padding
    protected int mBasePaddingRight = DensityUtil.dp2px(25);//右padding
    protected int mTopPadding = DensityUtil.dp2px(5);//距顶部距离;
    protected int mBottomPadding = DensityUtil.dp2px(20);//距底部距离

    private Paint mDotPaint = new Paint(Paint.ANTI_ALIAS_FLAG); //圆点


    public BaseRateView(Context context) {
        super(context);
        initView();
    }

    public BaseRateView(Context context, AttributeSet attrs) {
        super(context, attrs);
        initView();
    }

    public BaseRateView(Context context, AttributeSet attrs, int defStyleAttr) {
        super(context, attrs, defStyleAttr);
        initView();
    }

    public void setOnViewClickListener(CallOnClick callOnClick) {
        this.mCallOnClick = callOnClick;
    }

    private void initView() {
        setWillNotDraw(false);

        mBackgroundColor = Color.parseColor("#2A2D4F");
        mBackgroundPaint.setColor(mBackgroundColor);

        mGridPaint.setColor(Color.parseColor("#15FFFFFF")); //网格线颜色
        mGridPaint.setStrokeWidth(dp2px(1));


        mDotPaint.setStyle(Paint.Style.FILL);   //圆点
        mDotPaint.setColor(getColor(R.color.chart_FF6600));

        mDetector = new GestureDetectorCompat(getContext(), this);
        mScaleDetector = new ScaleGestureDetector(getContext(), this);
        mAnimator = ValueAnimator.ofFloat(0f, 1f);
        mAnimator.setDuration(mAnimationDuration);
        mAnimator.addUpdateListener(new ValueAnimator.AnimatorUpdateListener() {
            @Override
            public void onAnimationUpdate(ValueAnimator animation) {
                invalidate();
            }
        });
    }


    //点击， 处理长安时间
    @Override
    public boolean onTouchEvent(MotionEvent event) {
        switch (event.getAction() & MotionEvent.ACTION_MASK) {
            case MotionEvent.ACTION_DOWN:
                mClickTime = System.currentTimeMillis();
                break;
            case MotionEvent.ACTION_MOVE:
                //一个点的时候滑动
                if (event.getPointerCount() == 1) {
                    //长按之后移动
                    if (isLongPress || !isClosePress) {
                        calculateSelectedX(event.getX());
                        invalidate();
                    }
                }
                break;
            case MotionEvent.ACTION_UP:
                if (!isClosePress) {
                    isLongPress = false;
                }
                invalidate();
                break;
            case MotionEvent.ACTION_CANCEL:
                if (!isClosePress) {
                    isLongPress = false;
                }
                invalidate();
                break;
        }
        this.mDetector.onTouchEvent(event);
        this.mScaleDetector.onTouchEvent(event);
        return true;
    }


    //抬起, 手指离开触摸屏时触发(长按、滚动、滑动时，不会触发这个手势)
    @Override
    public boolean onSingleTapUp(MotionEvent e) {
        switch (e.getAction()) {
            case MotionEvent.ACTION_DOWN:
                break;
            case MotionEvent.ACTION_UP: //双指点击时不会触发
                if (isClosePress) {
                    if (System.currentTimeMillis() - mClickTime < 500) {
                        downX = e.getX();
                        downY = e.getY();
                        if (downX > 0 && downX < mWidth) {
                            if (mCallOnClick != null) {
                                if (downY <= mMainHeight) {
                                    mCallOnClick.onMainViewClick();
                                }
                            }

                        }
                    }
                } else {
                    isClosePress = true;
                }
                invalidate();
                break;
            case MotionEvent.ACTION_MOVE:
                break;
        }
        return false;
    }

    @Override
    public void onConfigurationChanged(Configuration newConfig) {//横竖屏切换
        super.onConfigurationChanged(newConfig);
        mMainHeight = mHeight - mTopPadding - mBottomPadding;

        invalidate();
    }


    @Override
    protected void onSizeChanged(int w, int h, int oldw, int oldh) {
        super.onSizeChanged(w, h, oldw, oldh);
        this.mWidth = w;
        this.mHeight = h;
        mMainHeight = h - mTopPadding - mBottomPadding;
        mMainRect = new Rect(0, mTopPadding, mWidth, mTopPadding + mMainHeight);

        setScaleValue();  //计算缩放率
        setTranslateXFromScrollX(mScrollX);

    }


    @Override
    protected void onDraw(Canvas canvas) {
        super.onDraw(canvas);
        canvas.drawColor(mBackgroundColor); //绘制背景
//        drawMainViewLogo(canvas); //绘制logo
        drawGird(canvas); //绘制网格线

    }

    //添加主视图水印
    public void drawMainViewLogo(Canvas canvas) {
        if (mBitmapLogo != null) {
            int mLeft = getWidth() / 2 - mBitmapLogo.getWidth() / 2;
            int mTop = (mTopPadding + mMainHeight) / 2 - mBitmapLogo.getHeight() / 2;
            canvas.drawBitmap(mBitmapLogo, mLeft, mTop, null);

        }

    }

    //画表格
    private void drawGird(Canvas canvas) {
        //横向的grid
        float rowSpace = mMainRect.height() / mGridRows;
        for (int i = 0; i <= mGridRows; i++) {
            canvas.drawLine(0, rowSpace * i + mMainRect.top, mWidth,
                    rowSpace * i + mMainRect.top, mGridPaint);
        }

        //纵向的grid
        float columnSpace = (mWidth - mBasePaddingLeft - mBasePaddingRight) / mGridColumns;
        for (int i = 0; i <= mGridColumns; i++) {
            canvas.drawLine(columnSpace * i + mBasePaddingLeft, mMainRect.top,
                    columnSpace * i + mBasePaddingRight, mMainRect.bottom, mGridPaint);

        }
    }


    private void calculateSelectedX(float x) {
        mSelectedIndex = indexOfTranslateX(xToTranslateX(x));
        Log.e("move mSelectedIndex:", "" + mSelectedIndex + "/mStartIndex:" + mStartIndex);
        if (mSelectedIndex < mStartIndex) {
            mSelectedIndex = mStartIndex;
        }
        if (mSelectedIndex > mStopIndex) {
            mSelectedIndex = mStopIndex;
        }

    }

    @Override
    public void onLongPress(MotionEvent e) {
        super.onLongPress(e);
        int lastIndex = mSelectedIndex;
        calculateSelectedX(e.getX());
        if (lastIndex != mSelectedIndex) {
            onSelectedChanged(this, getItem(mSelectedIndex), mSelectedIndex);
        }
        invalidate();
    }


    @Override
    protected void onScrollChanged(int l, int t, int oldl, int oldt) {
        super.onScrollChanged(l, t, oldl, oldt);
        setTranslateXFromScrollX(mScrollX);
        Log.e("scroll-change", "" + mScrollX);
    }

    @Override
    protected void onScaleChanged(float scale, float oldScale) {
        super.onScaleChanged(scale, oldScale);
        checkAndFixScrollX();
        setTranslateXFromScrollX(mScrollX);
    }


    @Override
    public int getMinScrollX() {
        return (int) -(mOverScrollRange / mScaleX);
    }

    @Override
    public int getMaxScrollX() {
        return Math.round(getMaxTranslateX() - getMinTranslateX());
    }


    //获取平移的最小值
    private float getMinTranslateX() {
        return -mDataLen + mWidth / mScaleX - mPointWidth / 2;
    }

    //获取平移的最大值
    private float getMaxTranslateX() {
        if (!isFullScreen()) { //数据不够时
            return getMinTranslateX();
        }
        return mPointWidth / 2;
    }


    //数据是否充满屏幕
    public boolean isFullScreen() {
        Log.i("mDataLen : ", mDataLen + "-->1");
        Log.i("mDataLen : ", mWidth + "-->mWidth");
        Log.i("mDataLen : ", mWidth / mScaleX + "-->mWidth / mScaleX");
        Log.i("mDataLen : ", mScaleX + "-->mScaleX");
        return mDataLen >= mWidth / mScaleX;
    }

    public float getXScale() {
        return mScaleX;
    }


    //在试图区域画线
    public void drawMainLine(Canvas canvas, Paint paint, float startX, float startValue, float stopX, float stopValue) {
        paint.setStrokeWidth(paint.getStrokeWidth() / mScaleX);

        canvas.drawCircle(stopX, getMainY(stopValue), 5, mDotPaint);

        canvas.drawLine(startX, getMainY(startValue), stopX, getMainY(stopValue), paint);
    }


    //计算Y轴位置
    public float getMainY(float value) {
        return (mMainMaxValue - value) * mMainScaleY + mMainRect.top;
    }


    //获取适配器
    public IAdapter getAdapter() {
        return mAdapter;
    }


    //设置数据适配器
    public void setAdapter(IAdapter adapter) {
        if (mAdapter != null && mDataSetObserver != null) {
            mAdapter.unregisterDataSetObserver(mDataSetObserver);
        }
        mAdapter = adapter;
        mItemCount = mAdapter.getCount();
        if (mAdapter != null) {
            mAdapter.registerDataSetObserver(mDataSetObserver);
            mItemCount = mAdapter.getCount();
        } else {
            mItemCount = 0;
        }

        notifyChanged();
    }


    //重新计算并刷新线条
    public void notifyChanged() {
        if (mItemCount != 0) {
            mDataLen = (mItemCount - 1) * mPointWidth;
            checkAndFixScrollX();
            setTranslateXFromScrollX(mScrollX);

        } else {
            setScrollX(0);
        }
        invalidate();
    }

    //设置缩放率(并还原 当初设置)
    public void setScaleValue() {
        mScaleXMax = mWidth / (mMinPoint * mPointWidth);
//        mScrollX = 0;
        mScaleX = 1;

        if (mAdapter.getCount() < mMaxPoint) {
            mScaleXMin = mWidth / mDataLen;
        } else {
            mScaleXMin = mWidth / (mMaxPoint * mPointWidth);
        }
    }

    //根据索引获取实体
    public IRate getItem(int position) {
        if (mAdapter != null) {
            return (IRate) mAdapter.getItem(position);
        } else {
            return null;
        }
    }

    //根据索引索取x坐标
    public float getX(int position) {
        return position * mPointWidth;
    }



    //scrollX 转换为 TranslateX
    private void setTranslateXFromScrollX(int scrollX) {
        mTranslateX = scrollX + getMinTranslateX();
    }


    // view中的x转化为TranslateX
    public float xToTranslateX(float x) {
        return -mTranslateX + x / mScaleX;
    }

    //translateX转化为view中的x
    public float translateXtoX(float translateX) {
        return (translateX + mTranslateX) * mScaleX;
    }



    //转化成数组的索引
    public int indexOfTranslateX(float translateX) {
        return indexOfTranslateX(translateX, 0, mItemCount - 1);
    }

    /**
     * 二分查找当前值的index
     *
     * @return 返回当前位置
     */
    public int indexOfTranslateX(float translateX, int start, int end) {
        if (end == start) {
            return start;
        }
        if (end - start == 1) {
            float startValue = getX(start);
            float endValue = getX(end);
            return Math.abs(translateX - startValue) < Math.abs(translateX - endValue) ? start : end;
        }
        int mid = start + (end - start) / 2;
        float midValue = getX(mid);
        if (translateX < midValue) {
            return indexOfTranslateX(translateX, start, mid);
        } else if (translateX > midValue) {
            return indexOfTranslateX(translateX, mid, end);
        } else {
            return mid;
        }
    }

    //获取主区域的 IRateDr
    public IRateDraw getMainDraw() {
        return mMainDraw;
    }

    // 设置主区域的 IRateDraw
    public void setMainDraw(IRateDraw mainDraw) {
        mMainDraw = mainDraw;
    }


    //开始动画
    public void startAnimation() {
        if (mAnimator != null) {
            mAnimator.start();
        }
    }

    //设置动画时间
    public void setAnimationDuration(long duration) {
        if (mAnimator != null) {
            mAnimator.setDuration(duration);
        }
    }

    //设置表格行数
    public void setGridRows(int gridRows) {
        if (gridRows < 1) {
            gridRows = 1;
        }
        mGridRows = gridRows;
    }

    //设置表格列数
    public void setGridColumns(int gridColumns) {
        if (gridColumns < 1) {
            gridColumns = 1;
        }
        mGridColumns = gridColumns;
    }


    //获取图的宽度
    public int getChartWidth() {
        return mWidth;
    }

    //是否长按
    public boolean isLongPress() {
        return (isLongPress || !isClosePress);
    }

    //获取选择索引
    public int getSelectedIndex() {
        return mSelectedIndex;
    }

    //设置选择监听
    public void setOnSelectedChangedListener(OnSelectedChangedListener l) {
        this.mOnSelectedChangedListener = l;
    }

    public void onSelectedChanged(BaseRateView view, Object point, int index) {
        if (this.mOnSelectedChangedListener != null) {
            mOnSelectedChangedListener.onSelectedChanged(view, point, index);
        }
    }


    //设置超出右方后可滑动的范围
    public void setOverScrollRange(float overScrollRange) {
        if (overScrollRange < 0) {
            overScrollRange = 0;
        }
        mOverScrollRange = overScrollRange;
    }


    //设置每个点的宽度
    public void setPointWidth(float pointWidth) {
        mPointWidth = pointWidth;
    }


    //监听视图点击区域
    public interface CallOnClick {
        void onMainViewClick();
    }

    private DataSetObserver mDataSetObserver = new DataSetObserver() {
        @Override
        public void onChanged() {
            mItemCount = getAdapter().getCount();
            notifyChanged();
        }

        @Override
        public void onInvalidated() {
            mItemCount = getAdapter().getCount();
            notifyChanged();
        }
    };

    //选中点变化时的监听
    public interface OnSelectedChangedListener {
        /**
         * 当选点中变化时
         *
         * @param view  当前view
         * @param point 选中的点
         * @param index 选中点的索引
         */
        void onSelectedChanged(BaseRateView view, Object point, int index);
    }

    public float getDimension(@DimenRes int resId) {
        return getResources().getDimension(resId);
    }

    public int getColor(@ColorRes int resId) {
        return ContextCompat.getColor(getContext(), resId);
    }


    public int dp2px(float dp) {
        final float scale = getContext().getResources().getDisplayMetrics().density;
        return (int) (dp * scale + 0.5f);
    }

    public int sp2px(float spValue) {
        final float fontScale = getContext().getResources().getDisplayMetrics().scaledDensity;
        return (int) (spValue * fontScale + 0.5f);
    }


}
