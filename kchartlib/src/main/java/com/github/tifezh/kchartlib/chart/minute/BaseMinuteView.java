package com.github.tifezh.kchartlib.chart.minute;

import android.content.Context;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.graphics.Canvas;
import android.graphics.Color;
import android.graphics.Paint;
import android.support.annotation.Nullable;
import android.support.v4.view.GestureDetectorCompat;
import android.util.AttributeSet;
import android.util.Log;
import android.view.GestureDetector;
import android.view.MotionEvent;
import android.view.ScaleGestureDetector;
import android.view.View;

import com.github.tifezh.kchartlib.R;
import com.github.tifezh.kchartlib.chart.base.IValueFormatter;
import com.github.tifezh.kchartlib.chart.comInterface.IMinuteLine;
import com.github.tifezh.kchartlib.chart.comInterface.IMinuteTime;
import com.github.tifezh.kchartlib.chart.formatter.BigValueFormatter;

import java.util.ArrayList;
import java.util.Collection;
import java.util.Date;
import java.util.List;

/**
 * Description 分时图基类
 * Author puyantao
 * Email 1067899750@qq.com
 * Date 2018-10-17 9:52
 */

public abstract class BaseMinuteView extends View implements GestureDetector.OnGestureListener,
        ScaleGestureDetector.OnScaleGestureListener {
    protected int ONE_MINUTE = 60000;

    protected int mMainHeight = 0; //主视图
    protected int mWidth = 0; //试图宽度
    //可修改
    protected int mVolumeHeight = 100; //子试图高度
    protected int mVolumeTextHeight = 20; //CJL高度

    protected int mTopPadding = 1; //据顶部
    protected int mBottomPadding = 15;//距底部
    //左右padding,允许修改
    protected int mBasePaddingLeft = 50;
    protected int mBasePaddingRight = 50;
    protected float mBaseTextPaddingLeft = 15; //字体据左侧的距离
    protected float mBaseTextPaddingRight = 15;//字体据右侧的距离
    protected int mBaseTimePadding = 5; //下方时间文字

    protected int GridColumns = 5; //列数
    protected int mGridRows = 6; //主试图的行数
    protected int mGridChildRows = 4; //子试图的行数
    protected float mPointWidth; //柱子单位量

    protected boolean isLongPress = false; //是否长按事件
    protected boolean isClosePress = true; //关闭长按时间

    protected boolean mScaleEnable = false; //是否可以缩放
    protected GestureDetectorCompat mDetector;
    protected ScaleGestureDetector mScaleDetector;

    protected boolean isDrawChildView = true; //是否子试图点击事件


    protected long mClickTime = 0; //点击时间

    protected float mScaleY = 1; //Y轴单位量
    protected float mScaleX = 1; //x轴的单位量
    protected float mOldScale = 1.0f; //用来判断当前是否缩放
    protected OnScaleGestureListener mOnScaleGestureListener;

    protected long mTotalTime; //总时间

    protected Paint mGridPaint = new Paint(Paint.ANTI_ALIAS_FLAG);
    protected Paint mBackgroundPaint = new Paint(Paint.ANTI_ALIAS_FLAG);
    protected int mBackgroundColor;

    protected long mPointCount = 0; //点的个数
    protected float mValueStart;//成交量起始值/昨收价
    protected final List<IMinuteLine> mPoints = new ArrayList<>();
    protected final List<IMinuteTime> mTimes = new ArrayList<>();
    protected Date mMainStartTime;
    protected Date mMainEndTime;
    protected Date mStartTime;
    protected Date mEndTime;

    protected int mMACDClickPoint; //MACD选择点
    private Bitmap mBitmapLogo = BitmapFactory.decodeResource(getContext().getResources(), R.drawable.app_bg_logo);

    private IValueFormatter mVolumeFormatter;

    public BaseMinuteView(Context context) {
        super(context);
        init();
    }

    public BaseMinuteView(Context context, @Nullable AttributeSet attrs) {
        super(context, attrs);
        init();
    }

    public BaseMinuteView(Context context, @Nullable AttributeSet attrs, int defStyleAttr) {
        super(context, attrs, defStyleAttr);
        init();
    }


    protected void init() {
        mDetector = new GestureDetectorCompat(getContext(), this);
        mScaleDetector = new ScaleGestureDetector(getContext(), this);

        mBackgroundColor = Color.parseColor("#2A2D4F");
        mBackgroundPaint.setColor(mBackgroundColor);

        mGridPaint.setColor(Color.parseColor("#10FFFFFF")); //网格线颜色
        mGridPaint.setStrokeWidth(dp2px(1));

        mVolumeFormatter = new BigValueFormatter();

    }


    /**
     * @param data          数据源
     * @param startTime     显示的开始时间
     * @param endTime       显示的结束时间
     * @param times         休息开始时间
     * @param yesClosePrice 昨收价
     */
    public void initData(Collection<? extends IMinuteLine> data, Date startTime, Date endTime,
                         Collection<? extends IMinuteTime> times, float yesClosePrice) {
        if ((data == null && data.isEmpty()) || (times == null && times.isEmpty()) ) {
            return;
        }
        if (times != null) {
            mTimes.clear();
            mTimes.addAll(times);
            mTotalTime = 0;
        }

        this.mMainStartTime = startTime;
        this.mMainEndTime = endTime;
        if (mMainStartTime.getTime() >= mMainEndTime.getTime())
            throw new IllegalStateException("开始时间不能大于结束时间");

        if (mTimes.size() != 0) {
            for (int i = 0; i < mTimes.size(); i++) {
                mStartTime = mTimes.get(i).getStartDate();
                mEndTime = mTimes.get(i).getEndDate();
                mTotalTime += mEndTime.getTime() - mStartTime.getTime();

            }

        } else {
            mTotalTime = mMainEndTime.getTime() - mMainStartTime.getTime();
        }

        setValueStart(yesClosePrice); //设置开始的值(昨日成交量)

        if (data != null) {
            mPoints.clear();
            this.mPoints.addAll(data);
            mPointCount = mPoints.size();
            mMACDClickPoint = mPoints.size() - 1;
        }
        notifyChanged();
    }


    /**
     * 点击， 处理长安时间
     *
     * @param event
     * @return
     */
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
                mMACDClickPoint = mPoints.size() - 1;

//                if (isDrawChildView && isClickPress) {
//                    //当点击时间小于2000ms时，为交互时间
//                    if (System.currentTimeMillis() - mClickTime < 500) {
//                        float downX = event.getX();
//                        float downY = event.getY();
//                        jumpToCJLAndMACL(downX, downY);
//                    }
//                }

                invalidate();
                break;
            case MotionEvent.ACTION_CANCEL:
                if (!isClosePress) {
                    isLongPress = false;
                }
                mMACDClickPoint = mPoints.size() - 1;
                invalidate();
                break;
        }
        this.mDetector.onTouchEvent(event);
        this.mScaleDetector.onTouchEvent(event);
        return true;
    }


    @Override
    protected void onSizeChanged(int w, int h, int oldw, int oldh) {
        super.onSizeChanged(w, h, oldw, oldh);
        int height = h - mTopPadding - mBottomPadding;
        this.mMainHeight = height - mVolumeHeight - mVolumeTextHeight;
        this.mWidth = w;
        notifyChanged();
    }

    @Override
    protected void onDraw(Canvas canvas) {
        //绘制背景颜色
        canvas.drawColor(mBackgroundColor);
        drawMainViewLogo(canvas);
        if (isDrawChildView) {
            drawChildViewLogo(canvas);
        }
        drawGird(canvas); //绘制网格
        if (mWidth == 0 || mMainHeight == 0 || mPoints == null || mPoints.size() == 0) {
            return;
        }
        super.onDraw(canvas);
    }

    /**
     * 添加主视图水印
     *
     * @param canvas
     */
    public void drawMainViewLogo(Canvas canvas) {
        if (mBitmapLogo != null) {
            int mLeft = getWidth() / 2 - mBitmapLogo.getWidth() / 2;
            int mTop = mMainHeight / 2 - mBitmapLogo.getHeight() / 2 + mTopPadding;
            canvas.drawBitmap(mBitmapLogo, mLeft, mTop, null);
        }
    }

    /**
     * 添加子视图水印
     *
     * @param canvas
     */
    public void drawChildViewLogo(Canvas canvas) {
        if (mBitmapLogo != null) {
            int mLeft = getWidth() / 2 - mBitmapLogo.getWidth() / 2;
            int mTop = mMainHeight + mVolumeTextHeight + (mVolumeHeight / 2) - mBitmapLogo.getHeight() / 2 + mTopPadding;
            canvas.drawBitmap(mBitmapLogo, mLeft, mTop, null);
        }
    }


    //绘制网格线
    private void drawGird(Canvas canvas) {
        //先画出坐标轴
        canvas.translate(0, mTopPadding);
        canvas.scale(1, 1);
        //主视图横向的grid
        float rowSpace = mMainHeight / mGridRows;
        for (int i = 0; i <= mGridRows; i++) {
            canvas.drawLine(0, rowSpace * i, mWidth, rowSpace * i, mGridPaint);
        }

        //主试图纵向的grid
        float columnSpace = (mWidth - mBasePaddingLeft - mBasePaddingRight) / GridColumns;
        for (int i = 0; i <= GridColumns; i++) {
            canvas.drawLine(columnSpace * i + mBasePaddingLeft, 0,
                    columnSpace * i + mBasePaddingLeft, mMainHeight, mGridPaint);
        }

        //中间轴线
//        canvas.drawLine(0, rowSpace * mGridRows / 2, mWidth, rowSpace * mGridRows / 2, mGridPaint);

        if (isDrawChildView) {
            //子视图横向的grid
            float rowChildSpace = mVolumeHeight / mGridChildRows;
            for (int i = 0; i <= mGridChildRows; i++) {
                canvas.drawLine(0, mMainHeight + rowChildSpace * i + mVolumeTextHeight, mWidth,
                        mMainHeight + rowChildSpace * i + mVolumeTextHeight, mGridPaint);
            }

            //子试图纵向的grid
            float columnChildSpace = (mWidth - mBasePaddingLeft - mBasePaddingRight) / GridColumns;
            for (int i = 0; i <= GridColumns; i++) {
                canvas.drawLine(columnChildSpace * i + mBasePaddingLeft, mMainHeight + mVolumeTextHeight, columnChildSpace * i + mBasePaddingLeft,
                        mMainHeight + mVolumeHeight + mVolumeTextHeight, mGridPaint);
            }
        }

    }


    /*********************************************抽象方法用于实现继承类**************************************/

    protected abstract void notifyChanged();

    protected abstract void calculateSelectedX(float x);

    protected abstract void jumpToCJLAndMACL(float downX, float downY);


    protected int dp2px(float dp) {
        final float scale = getContext().getResources().getDisplayMetrics().density;
        return (int) (dp * scale + 0.5f);
    }

    protected int sp2px(float spValue) {
        final float fontScale = getContext().getResources().getDisplayMetrics().scaledDensity;
        return (int) (spValue * fontScale + 0.5f);
    }

    /**
     * 设置开始的值 对称轴线
     */
    protected void setValueStart(float valueStart) {
        this.mValueStart = valueStart;
    }

    /*********************************************可设置参数**************************************/

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
     * 设置子试图表格行数
     */
    public void setGridChildRows(int gridRows) {
        if (gridRows < 1) {
            gridRows = 1;
        }
        mGridChildRows = gridRows;
    }

    /**
     * 设置表格列数
     */
    public void setGridColumns(int gridColumns) {
        if (gridColumns < 1) {
            gridColumns = 1;
        }
        GridColumns = gridColumns;
    }


    //是否绘制子试图
    public void setDrawChildView(boolean b) {
        isDrawChildView = b;
        if (!isDrawChildView) {
            mVolumeHeight = 0; //子试图高度
            mVolumeTextHeight = 0; //CJL高度
        }
    }

    //设置子试图高度
    public void setVolumeHeight(int height) {
        mVolumeHeight = dp2px(height);
    }

    //设置CJL高度
    public void setVolumeTextHeight(int height) {
        mVolumeTextHeight = dp2px(height);
    }

    //设置主试图高度
    public void setMainHeight(int height) {
        mMainHeight =  dp2px(height);
    }

    /**
     * 获取最大能有多少个点
     */
    public long getMaxPointCount(int count) {
        return mTotalTime / ONE_MINUTE;
    }

    /**
     * 添加一个点
     */
    public void addPoint(IMinuteLine point) {
        mPoints.add(point);
        notifyChanged();
    }

    /**
     * 修改某个点的值
     *
     * @param position 索引值
     */
    public void changePoint(int position, IMinuteLine point) {
        mPoints.set(position, point);
        notifyChanged();
    }


    /**
     * 根据索引获取点
     */
    public IMinuteLine getItem(int position) {
        return mPoints.get(position);
    }

    /**
     * 获取点的个数
     */
    private int getItemSize() {
        return mPoints.size();
    }

    /**
     * 刷新最后一个点
     */
    public void refreshLastPoint(IMinuteLine point) {
        changePoint(getItemSize() - 1, point);
    }

    //是否可以缩放
    public void setScaleEnable(boolean scaleEnable) {
        this.mScaleEnable = scaleEnable;
    }


    /**
     * 设置成交量格式化器
     *
     * @param volumeFormatter {@link IValueFormatter} 成交量格式化器
     */
    public void setVolumeFormatter(IValueFormatter volumeFormatter) {
        mVolumeFormatter = volumeFormatter;
    }


    //设置时间单位； 1代表一分钟， 2代表两分钟
    public void setOpenMinute(int number) {
        ONE_MINUTE = number * 60000;
    }

    public void setLongPress(boolean longPress) {
        isLongPress = longPress;
    }

    public void setClosePress(boolean closePress){
        isClosePress = closePress;
    }

    /******************************长按，点击手势*****************************************/

    // 单击, 触摸屏按下时立刻触发
    @Override
    public boolean onDown(MotionEvent e) {
        Log.i("--->", "onDown");
        return false;
    }

    //短按, 触摸屏按下后片刻后抬起，会触发这个手势，如果迅速抬起则不会
    @Override
    public void onShowPress(MotionEvent e) {
    }

    //抬起, 手指离开触摸屏时触发(长按、滚动、滑动时，不会触发这个手势)
    @Override
    public boolean onSingleTapUp(MotionEvent e) {
        switch (e.getAction()){
            case MotionEvent.ACTION_DOWN:
                Log.i("--->", "ACTION_DOWN");
                break;
            case MotionEvent.ACTION_UP: //双指点击时不会触发
                Log.i("--->", "ACTION_UP");
                if (isClosePress) {
                    if (isDrawChildView) {
                        //当点击时间小于2000ms时，为交互时间
                        if (System.currentTimeMillis() - mClickTime < 500) {
                            float downX = e.getX();
                            float downY = e.getY();
                            jumpToCJLAndMACL(downX, downY);
                        }
                    }
                } else {
                    isClosePress = true;
                }

                break;
            case MotionEvent.ACTION_MOVE:
                Log.i("--->", "ACTION_MOVE");
                break;
        }
        return false;
    }

    //滚动, 触摸屏按下后移动
    @Override
    public boolean onScroll(MotionEvent e1, MotionEvent e2, float distanceX, float distanceY) {
        return false;
    }

    //长按, 触摸屏按下后既不抬起也不移动，过一段时间后触发
    @Override
    public void onLongPress(MotionEvent e) {
        isLongPress = true;
        isClosePress = false;
        calculateSelectedX(e.getX());
        invalidate();
    }

    //滑动, 触摸屏按下后快速移动并抬起，会先触发滚动手势，跟着触发一个滑动手势
    @Override
    public boolean onFling(MotionEvent e1, MotionEvent e2, float velocityX, float velocityY) {
        return false;
    }


    /******************************缩放手势*****************************************/
    @Override
    public boolean onScale(ScaleGestureDetector detector) {
        if (!isScaleEnable()) {
            return false;
        }
        mOldScale = 1.0f;
        float f = detector.getScaleFactor();


        mOldScale *= f;


//        if (mScaleX < mScaleXMin) {
//            mScaleX = mScaleXMin;
//        } else if (mScaleX > mScaleXMax) {
//            mScaleX = mScaleXMax;
//        } else {
//            onScaleChanged(mScaleX, oldScale);
//        }
        return true;
    }

    @Override
    public boolean onScaleBegin(ScaleGestureDetector detector) {
        return true;
    }

    @Override
    public void onScaleEnd(ScaleGestureDetector detector) {
        if (isClosePress) {
            if (mOldScale > 1.0f) { //放大
                mOnScaleGestureListener.setLoseNumber();

            } else { //缩小
                mOnScaleGestureListener.setAddNumber();

            }
        }

    }

    protected void onScaleChanged(float scale, float oldScale) {
        invalidate();
    }


    protected boolean isScaleEnable() {
        return mScaleEnable;
    }


    public void setViewScaleGestureListener(OnScaleGestureListener listener) {
        if (listener != null) {
            this.mOnScaleGestureListener = listener;
        }
    }

    public interface OnScaleGestureListener {
        void setAddNumber();

        void setLoseNumber();

    }


}















