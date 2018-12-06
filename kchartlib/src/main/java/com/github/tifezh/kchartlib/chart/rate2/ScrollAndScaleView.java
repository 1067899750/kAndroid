package com.github.tifezh.kchartlib.chart.rate2;

import android.content.Context;
import android.support.v4.view.GestureDetectorCompat;
import android.util.AttributeSet;
import android.util.Log;
import android.view.GestureDetector;
import android.view.MotionEvent;
import android.view.ScaleGestureDetector;
import android.widget.OverScroller;
import android.widget.RelativeLayout;

/**
 *
 * Description 可以滑动和放大的view
 * Author puyantao
 * Email 1067899750@qq.com
 * Date 2018-12-4 10:43
 */

public abstract class ScrollAndScaleView extends RelativeLayout implements
        GestureDetector.OnGestureListener,
        ScaleGestureDetector.OnScaleGestureListener {
    protected int mScrollX = 0;
    protected GestureDetectorCompat mDetector;
    protected ScaleGestureDetector mScaleDetector;

    protected boolean isLongPress = false;
    protected boolean isClosePress = true; //关闭长按时间

    private OverScroller mScroller;
    protected boolean touch = false;

    protected float mScaleX = 1; //默认缩放分量
    protected float mScaleXMax = 2f;//最大缩放分量
    protected float mScaleXMin = 0.5f;//最小缩放分量

    private boolean mMultipleTouch = false;
    private boolean mScrollEnable = true;
    private boolean mScaleEnable = true;
    private boolean isScale = false;

    public ScrollAndScaleView(Context context) {
        super(context);
        initView();
    }

    public ScrollAndScaleView(Context context, AttributeSet attrs) {
        super(context, attrs);
        initView();
    }

    public ScrollAndScaleView(Context context, AttributeSet attrs, int defStyleAttr) {
        super(context, attrs, defStyleAttr);
        initView();
    }

    private void initView() {
        setWillNotDraw(false);
        mDetector = new GestureDetectorCompat(getContext(), this);
        mScaleDetector = new ScaleGestureDetector(getContext(), this);
        mScroller = new OverScroller(getContext());
    }

    @Override
    public boolean onTouchEvent(MotionEvent event) {
        switch (event.getAction() & MotionEvent.ACTION_MASK) {
            case MotionEvent.ACTION_DOWN:
                touch = true;
                break;
            case MotionEvent.ACTION_MOVE:
                if (event.getPointerCount() == 1) {
                    //长按之后移动
                    if (isLongPress || !isClosePress) {
                        onLongPress(event);
                    }
                }
                break;
            case MotionEvent.ACTION_POINTER_UP:
                invalidate();
                break;
            case MotionEvent.ACTION_UP:
                if (!isClosePress) {
                    isLongPress = false;
                }
                touch = false;
                invalidate();
                break;
            case MotionEvent.ACTION_CANCEL:
                if (!isClosePress) {
                    isLongPress = false;
                }
                touch = false;
                invalidate();
                break;
        }
        mMultipleTouch = event.getPointerCount() > 1;
        this.mDetector.onTouchEvent(event);
        this.mScaleDetector.onTouchEvent(event);
        return true;
    }



    @Override
    public void computeScroll() {
        if (isClosePress) {
            if (mScroller.computeScrollOffset()) {
                if (!isTouch()) {
                    //  scrollTo(mScroller.getCurrX(), mScroller.getCurrY());
                    scrollTo(mScroller.getCurrX(), 0);
                } else {
                    mScroller.forceFinished(true);
                }
            }
        }
    }

    @Override
    public void scrollBy(int x, int y) {
        if (isClosePress) {
            scrollTo(mScrollX - Math.round(x / mScaleX), 0);
        }

    }

    //设置滑动位置
    @Override
    public void scrollTo(int x, int y) {
        if (isClosePress) {
            if (!isScrollEnable()) {
                mScroller.forceFinished(true);
                return;
            }
            int oldX = mScrollX;
            mScrollX = x;
            if (mScrollX < getMinScrollX()) { //滑动最右边
                mScrollX = getMinScrollX();
                onRightSide();
                mScroller.forceFinished(true);

            } else if (mScrollX > getMaxScrollX()) {//滑动最左边
                mScrollX = getMaxScrollX();
                onLeftSide();
                mScroller.forceFinished(true);
            }
            onScrollChanged(mScrollX, 0, oldX, 0);
            invalidate();
        }
    }


    // 获取位移的最小值
    public abstract int getMinScrollX();

    //获取位移的最大值
    public abstract int getMaxScrollX();

    //滑到了最左边
    abstract public void onLeftSide();

    // 滑到了最右边
    abstract public void onRightSide();


    /******************************长按，点击手势*****************************************/

    // 单击, 触摸屏按下时立刻触发
    @Override
    public boolean onDown(MotionEvent e) {
        return false;
    }

    //短按, 触摸屏按下后片刻后抬起，会触发这个手势，如果迅速抬起则不会
    @Override
    public void onShowPress(MotionEvent e) {

    }

    //抬起, 手指离开触摸屏时触发(长按、滚动、滑动时，不会触发这个手势)
    @Override
    public boolean onSingleTapUp(MotionEvent e) {
        return false;
    }

    //滚动, 触摸屏按下后移动
    @Override
    public boolean onScroll(MotionEvent e1, MotionEvent e2, float distanceX, float distanceY) {
        if (!isScale && !isLongPress && !isMultipleTouch()) {
            scrollBy(Math.round(distanceX), 0);
            return true;
        }
        return false;
    }

    //长按, 触摸屏按下后既不抬起也不移动，过一段时间后触发
    @Override
    public void onLongPress(MotionEvent e) {
        isLongPress = true;
        isClosePress = false;
    }


    //滑动, 触摸屏按下后快速移动并抬起，会先触发滚动手势，跟着触发一个滑动手势
    @Override
    public boolean onFling(MotionEvent e1, MotionEvent e2, float velocityX, float velocityY) {
        if (isClosePress) {
            if (!isScale && !isTouch() && isScrollEnable()) {
                mScroller.fling(mScrollX, 0
                        , Math.round(velocityX / mScaleX), 0,
                        Integer.MIN_VALUE, Integer.MAX_VALUE,
                        0, 0);
            }
        }
        return true;
    }


    /******************************缩放手势*****************************************/
    @Override
    public boolean onScale(ScaleGestureDetector detector) {
        if (isClosePress) {
            if (!isScaleEnable()) {
                return false;
            }
            float oldScale = mScaleX;
            mScaleX *= detector.getScaleFactor();
            if (mScaleX < mScaleXMin) {
                mScaleX = mScaleXMin;

            } else if (mScaleX > mScaleXMax) {
                mScaleX = mScaleXMax;

            } else {
                onScaleChanged(mScaleX, oldScale);
            }

            Log.i("22222222222222", mScaleX + "");
            if (mScaleX >= 2.0f || mScaleX <= 0.5f) {
                isScale = true;
            }
        }
        return true;
    }

    @Override
    public boolean onScaleBegin(ScaleGestureDetector detector) {
        isScale = true;
        return true;
    }

    @Override
    public void onScaleEnd(ScaleGestureDetector detector) {
        isScale = false;
    }

    protected void onScaleChanged(float scale, float oldScale) {
        isScale = true;
        invalidate();
    }



    //是否在触摸中
    public boolean isTouch() {
        return touch;
    }


    //设置ScrollX
    public void setScrollX(int scrollX) {
        this.mScrollX = scrollX;
        scrollTo(scrollX, 0);
    }

    //是否是多指触控
    public boolean isMultipleTouch() {
        return mMultipleTouch;
    }


    //判断是否滑动到终点，滑动到终点结束滑动
    protected void checkAndFixScrollX() {
        if (mScrollX < getMinScrollX()) {
            mScrollX = getMinScrollX();
            mScroller.forceFinished(true);
        } else if (mScrollX > getMaxScrollX()) {
            mScrollX = getMaxScrollX();
            mScroller.forceFinished(true);
        }
    }

    public float getScaleXMax() {
        return mScaleXMax;
    }
    public float getScaleXMin() {
        return mScaleXMin;
    }
    public boolean isScrollEnable() {
        return mScrollEnable;
    }
    public boolean isScaleEnable() {
        return mScaleEnable;
    }

    //设置是否可以滑动
    public void setScrollEnable(boolean scrollEnable) {
        mScrollEnable = scrollEnable;
    }

    //设置是否可以缩放
    public void setScaleEnable(boolean scaleEnable) {
        mScaleEnable = scaleEnable;
    }

    @Override
    public float getScaleX() {
        return mScaleX;
    }

    public void setLongPress(boolean longPress) {
        isLongPress = longPress;
    }

    public void setClosePress(boolean closePress) {
        isClosePress = closePress;
    }
}
