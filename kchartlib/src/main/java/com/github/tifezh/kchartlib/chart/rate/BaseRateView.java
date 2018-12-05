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
    private float mTranslateX = Float.MIN_VALUE;
    private float mMainScaleY = 1;
    private float mDataLen = 0;
    private float downX;
    private float downY;
    private float mMainMaxValue; //最大值
    private float mMainMinValue;//最小值

    private int mLeftCount = 4;
    private float mPointWidth = 6;
    private float mMaxValue;
    private float mMinValue;
    private float mOverScrollRange = 0;

    private int mWidth; //试图宽
    private int mHeight;//试图高

    //点的索引
    private int mStartIndex = 0;
    private int mStopIndex = 0;
    private int mGridRows = 7;
    private int mGridColumns = 5;

    private int mSelectedIndex;

    private int mMainHeight;//主视图高
    private int mItemCount;//当前点的个数

    private Paint mGridPaint = new Paint(Paint.ANTI_ALIAS_FLAG); //网格线画笔
    private Paint mTextPaint = new Paint(Paint.ANTI_ALIAS_FLAG);
    private Paint mBackgroundPaint = new Paint(Paint.ANTI_ALIAS_FLAG);
    private Paint mSelectedLinePaint = new Paint(Paint.ANTI_ALIAS_FLAG);

    private IRateDraw mMainDraw;
    private IAdapter mAdapter;
    private CallOnClick mCallOnClick;//添加视图点击事件

    private ValueAnimator mAnimator;

    private long mAnimationDuration = 500;
    protected long mClickTime = 0; //点击时间
    private OnSelectedChangedListener mOnSelectedChangedListener = null;
    private Rect mMainRect;

    private boolean isFirstInto = true;
    private int mFirNum = 60;

    protected int mBackgroundColor;
    private Bitmap mBitmapLogo = BitmapFactory.decodeResource(getContext().getResources(), R.drawable.ic_app_logo);
    private int mBasePaddingLeft = DensityUtil.dp2px(25);  //左padding
    private int mBasePaddingRight = DensityUtil.dp2px(25);//右padding
    private int mTopPadding = DensityUtil.dp2px(5);//距顶部距离;
    public static int mTextPaddingLeft = DensityUtil.dp2px(10);//指标文字间距
    public static int mBottomPadding = DensityUtil.dp2px(20);//距底部距离
    public static int mTimeLeftPadding = DensityUtil.dp2px(5);//时间距左边距离

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

        mSelectedLinePaint.setStrokeWidth(1);
        mSelectedLinePaint.setColor(getColor(R.color.chart_press_xian));

        mTextPaint.setColor(getColor(R.color.c6A798E));
        mTextPaint.setTextSize(sp2px(11));


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
        setTranslateXFromScrollX(mScrollX);
    }


    @Override
    protected void onDraw(Canvas canvas) {
        super.onDraw(canvas);
        canvas.drawColor(mBackgroundColor); //绘制背景
        drawMainViewLogo(canvas); //绘制logo
        drawGird(canvas);

        if (mWidth == 0 || mMainRect.height() == 0 || mItemCount == 0) {
            return;
        }

        calculateValue();
        canvas.save();
        canvas.scale(1, 1);

        drawLine(canvas);
        drawText(canvas);

        drawValue(canvas, (isLongPress || !isClosePress) ? mSelectedIndex : mStopIndex);
        canvas.restore();

    }

    /**
     * 计算当前的显示区域
     */
    private void calculateValue() {
        if (!isLongPress()) {
            mSelectedIndex = -1;
        }

        mMainMaxValue = getItem(0).getValue();
        mMainMinValue = getItem(0).getValue();


        mStartIndex = indexOfTranslateX(xToTranslateX(0));
        mStopIndex = indexOfTranslateX(xToTranslateX(mWidth));


        for (int i = mStartIndex; i <= mStopIndex; i++) {
            IRate point = getItem(i);
            if (mMainDraw != null) {
                mMainMaxValue = Math.max(mMainMaxValue, mMainDraw.getValue(point));
                mMainMinValue = Math.min(mMainMinValue, mMainDraw.getValue(point));
            }

        }
        if (mMainMaxValue != mMainMinValue) {
            float padding = (mMainMaxValue - mMainMinValue) * 0.05f;
            mMainMaxValue += padding;
            mMainMinValue -= padding;
        } else {
            //当最大值和最小值都相等的时候 分别增大最大值和 减小最小值
            mMainMaxValue += Math.abs(mMainMaxValue * 0.05f);
            mMainMinValue -= Math.abs(mMainMinValue * 0.05f);
            if (mMainMaxValue == 0) {
                mMainMaxValue = 1;
            }
        }
        mMainScaleY = mMainRect.height() * 1f / (mMainMaxValue - mMainMinValue);


        if (mAnimator.isRunning()) {
            float value = (float) mAnimator.getAnimatedValue();
            mStopIndex = mStartIndex + Math.round(value * (mStopIndex - mStartIndex));
        }
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


    //画走势图
    private void drawLine(Canvas canvas) {
        //保存之前的平移，缩放
        canvas.save();
        canvas.translate(mTranslateX * mScaleX, 0); //平移
        canvas.scale(mScaleX, 1); //缩放

        mMaxValue = getItem(mStartIndex).getValue();
        mMinValue = getItem(mStartIndex).getValue();

        for (int i = mStartIndex; i <= mStopIndex; i++) {
            IRate currentPoint = getItem(i);
            float currentPointX = getX(i);
            IRate lastPoint = i == 0 ? currentPoint : getItem(i - 1);
            float lastX = i == 0 ? currentPointX : getX(i - 1);
            if (mMainDraw != null) {
                if (mMaxValue < getItem(i).getValue()) {
                    mMaxValue = getItem(i).getValue();
                } else if (mMinValue > getItem(i).getValue()) {
                    mMinValue = getItem(i).getValue();
                }
                mMainDraw.drawTranslated(lastPoint, currentPoint, lastX, currentPointX, canvas, this, i);
            }
        }


        //画选择线
        if (isLongPress || !isClosePress) {
            IRate point = getItem(mSelectedIndex);
            if (point == null) {
                return;
            }
            float x = getX(mSelectedIndex);
            float y = getMainY(point.getValue());

            canvas.drawLine(x, mMainRect.top, x, mMainRect.bottom, mSelectedLinePaint);
//            canvas.drawLine(-mTranslateX, y, -mTranslateX + mWidth / mScaleX, y, mSelectedLinePaint);//隐藏横线
        }
        //还原 平移缩放
        canvas.restore();
    }


    //画文字
    private void drawText(Canvas canvas) {
        Paint.FontMetrics fm = mTextPaint.getFontMetrics();
        float textHeight = fm.descent - fm.ascent;
        float baseLine = (textHeight - fm.bottom - fm.top) / 2;
        //--------------左边文字-------------
        if (mMainDraw != null) {
            canvas.drawText(StrUtil.floatToString(mMainMaxValue, 4), mTextPaddingLeft, baseLine + mMainRect.top, mTextPaint);
            canvas.drawText(StrUtil.floatToString(mMainMinValue, 4), mTextPaddingLeft,
                    mMainRect.bottom - textHeight + baseLine, mTextPaint);
            float rowValue = (mMainMaxValue - mMainMinValue) / mLeftCount;
            float rowSpace = mMainRect.height() / mLeftCount;

            for (int i = 1; i < mLeftCount; i++) {
                String text = StrUtil.floatToString(rowValue * (mLeftCount - i) + mMainMinValue, 4);
                canvas.drawText(text, mTextPaddingLeft, fixTextY(rowSpace * i + mMainRect.top), mTextPaint);
            }
        }

        //--------------画时间 共五个点---------------------
        float columnSpace = (mWidth) / mLeftCount;
        float y = mMainRect.bottom + baseLine;
        float startX = getX(mStartIndex) - mPointWidth / 2;
        float stopX = getX(mStopIndex) + mPointWidth / 2;

        float translateX = xToTranslateX(0);
        if (translateX >= startX && translateX <= stopX) {
            canvas.drawText(DateUtil.getStringDateByLong(mAdapter.getDate(mStartIndex).getTime(), 9),
                    mTimeLeftPadding,
                    y + mTimeLeftPadding, mTextPaint);
        }
        translateX = xToTranslateX(mWidth);
        if (translateX >= startX && translateX <= stopX) {
            String text = DateUtil.getStringDateByLong(mAdapter.getDate(mStopIndex).getTime(), 9);
            canvas.drawText(text,
                    mWidth - mTextPaint.measureText(text) - mTimeLeftPadding,
                    y + mTimeLeftPadding, mTextPaint);
        }


        for (int i = 1; i < mLeftCount; i++) {
            translateX = xToTranslateX(columnSpace * i);
            if (translateX >= startX && translateX <= stopX) {
                int index = indexOfTranslateX(translateX);
                String text = DateUtil.getStringDateByLong(mAdapter.getDate(index).getTime(), 9);
                canvas.drawText(text,
                        columnSpace * i - mTextPaint.measureText(text) / 2,
                        y + mTimeLeftPadding, mTextPaint);
            }
        }

    }


    public float getMainY(float value) {
        return (mMainMaxValue - value) * mMainScaleY + mMainRect.top;
    }


    /**
     * 解决text居中的问题
     */
    public float fixTextY(float y) {
        Paint.FontMetrics fontMetrics = mTextPaint.getFontMetrics();
        return (y + (fontMetrics.descent - fontMetrics.ascent) / 2 - fontMetrics.descent);
    }


    //画值
    private void drawValue(Canvas canvas, int position) {
        Paint.FontMetrics fm = mTextPaint.getFontMetrics();
        float textHeight = fm.descent - fm.ascent;
        float baseLine = (textHeight - fm.bottom - fm.top) / 2;
        if (position >= 0 && position < mItemCount) {
            if (mMainDraw != null) {
                float y = mMainRect.top + baseLine - textHeight;
                float x = 0;
                if (isLongPress || !isClosePress) {
                    mMainDraw.drawText(canvas, this, position, x, y);
                }
            }
        }
    }


    /**
     * 重新计算并刷新线条
     */
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
        checkAndFixScrollX();
        setTranslateXFromScrollX(mScrollX);
        super.onScaleChanged(scale, oldScale);
    }


    /**
     * 获取平移的最小值
     *
     * @return
     */
    private float getMinTranslateX() {
        return -mDataLen + mWidth / mScaleX - mPointWidth / 2;
    }

    /**
     * 获取平移的最大值
     *
     * @return
     */
    private float getMaxTranslateX() {
        if (!isFullScreen()) {
            return getMinTranslateX();
        }
        return mPointWidth / 2;
    }

    @Override
    public int getMinScrollX() {
        return (int) -(mOverScrollRange / mScaleX);
    }

    public int getMaxScrollX() {
        return Math.round(getMaxTranslateX() - getMinTranslateX());
    }

    public float getXScale() {
        return mScaleX;
    }


    //转化成数组的索引
    public int indexOfTranslateX(float translateX) {
        return indexOfTranslateX(translateX, 0, mItemCount - 1);
    }

    //在试图区域画线
    public void drawMainLine(Canvas canvas, Paint paint, float startX, float startValue, float stopX, float stopValue) {
        canvas.drawLine(startX, getMainY(startValue), stopX, getMainY(stopValue), paint);
    }


    /**
     * 根据索引获取实体
     *
     * @param position 索引值
     * @return
     */
    public IRate getItem(int position) {
        if (mAdapter != null) {
            return (IRate) mAdapter.getItem(position);
        } else {
            return null;
        }
    }

    /**
     * 根据索引索取x坐标
     *
     * @param position 索引值
     * @return
     */
    public float getX(int position) {
        return position * mPointWidth;
    }

    /**
     * 获取适配器
     *
     * @return
     */
    public IAdapter getAdapter() {
        return mAdapter;
    }


    /**
     * scrollX 转换为 TranslateX
     *
     * @param scrollX
     */
    private void setTranslateXFromScrollX(int scrollX) {
        mTranslateX = scrollX + getMinTranslateX();
    }


    /**
     * 获取主区域的 IRateDraw
     *
     * @return IRateDraw
     */
    public IRateDraw getMainDraw() {
        return mMainDraw;
    }

    /**
     * 设置主区域的 IRateDraw
     *
     * @param mainDraw IRateDraw
     */
    public void setMainDraw(IRateDraw mainDraw) {
        mMainDraw = mainDraw;
    }

    /**
     * 二分查找当前值的index
     *
     * @return
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

    /**
     * 设置数据适配器
     */
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

    /**
     * 开始动画
     */
    public void startAnimation() {
        if (mAnimator != null) {
            mAnimator.start();
        }
    }

    /**
     * 设置动画时间
     */
    public void setAnimationDuration(long duration) {
        if (mAnimator != null) {
            mAnimator.setDuration(duration);
        }
    }

    /**
     * 设置表格行数
     */
    public void setGridRows(int gridRows) {
        if (gridRows < 1) {
            gridRows = 1;
        }
        mGridRows = gridRows;
    }

    /**
     * 设置表格列数
     */
    public void setGridColumns(int gridColumns) {
        if (gridColumns < 1) {
            gridColumns = 1;
        }
        mGridColumns = gridColumns;
    }


    /**
     * view中的x转化为TranslateX
     *
     * @param x
     * @return
     */
    public float xToTranslateX(float x) {
        return -mTranslateX + x / mScaleX;
    }

    /**
     * translateX转化为view中的x
     *
     * @param translateX
     * @return
     */
    public float translateXtoX(float translateX) {
        return (translateX + mTranslateX) * mScaleX;
    }

    /**
     * 获取图的宽度
     *
     * @return
     */
    public int getChartWidth() {
        return mWidth;
    }

    /**
     * 是否长按
     */
    public boolean isLongPress() {
        return (isLongPress || !isClosePress);
    }

    /**
     * 获取选择索引
     */
    public int getSelectedIndex() {
        return mSelectedIndex;
    }

    /**
     * 设置选择监听
     */
    public void setOnSelectedChangedListener(OnSelectedChangedListener l) {
        this.mOnSelectedChangedListener = l;
    }

    public void onSelectedChanged(BaseRateView view, Object point, int index) {
        if (this.mOnSelectedChangedListener != null) {
            mOnSelectedChangedListener.onSelectedChanged(view, point, index);
        }
    }

    /**
     * 数据是否充满屏幕
     *
     * @return
     */
    public boolean isFullScreen() {
        return mDataLen >= mWidth / mScaleX;
    }

    /**
     * 设置超出右方后可滑动的范围
     */
    public void setOverScrollRange(float overScrollRange) {
        if (overScrollRange < 0) {
            overScrollRange = 0;
        }
        mOverScrollRange = overScrollRange;
    }


    /**
     * 设置背景颜色
     */
    public void setBackgroundColor(int color) {
        mBackgroundPaint.setColor(color);
    }


    /**
     * 设置每个点的宽度
     */
    public void setPointWidth(float pointWidth) {
        mPointWidth = pointWidth;
    }


    /**
     * 监听视图点击区域
     */
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

    /**
     * 选中点变化时的监听
     */
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
