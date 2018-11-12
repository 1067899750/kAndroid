package com.github.tifezh.kchartlib.chart.groupview;

import android.content.Context;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.graphics.Canvas;
import android.graphics.Color;
import android.graphics.Paint;
import android.graphics.Rect;
import android.os.Build;
import android.support.annotation.ColorRes;
import android.support.annotation.DimenRes;
import android.support.annotation.Nullable;
import android.support.v4.content.ContextCompat;
import android.support.v4.view.GestureDetectorCompat;
import android.support.v4.view.ViewConfigurationCompat;
import android.util.AttributeSet;
import android.util.Log;
import android.view.GestureDetector;
import android.view.MotionEvent;
import android.view.View;
import android.view.ViewConfiguration;

import com.github.tifezh.kchartlib.R;
import com.github.tifezh.kchartlib.chart.base.IGroupDraw;
import com.github.tifezh.kchartlib.chart.comInterface.ILem;
import com.github.tifezh.kchartlib.utils.DisplayUtil;

import static android.view.View.MeasureSpec.AT_MOST;

public abstract class BaseView extends View  implements GestureDetector.OnGestureListener{
    private Context mContext;
    protected final float DEF_WIDTH = 650;
    protected final float DEF_HIGHT = 400;

    //测量的控件宽高，会在onMeasure中进行测量。
    protected int mBaseWidth;//折线图宽度
    protected int mBaseHeight;//折线图高度
    protected int mHeight; //试图高度
    protected int mWidth;  //试图宽度

    protected int mTopPadding = 30; //据顶部
    protected int mBottomPadding = 57;//距底部
    protected int mPadding = 10;

    protected ILem mMaxPoint; //最大值
    protected ILem mMinPoint;//最小值

    protected int mGridRows = 6;//主视图网格数
    protected int mGridColumns = 0;
    protected int mChildGridRows = 4;//子图横向网格数
    protected int mChildGridColumns = 5;

    //左右padding,允许修改
    protected int mBasePaddingLeft = 50;
    protected int mBasePaddingRight = 50;
    protected float mBaseTextPaddingLeft = 15; //字体据左侧的距离
    protected float mBaseTextPaddingRight = 15;//字体据右侧的距离
    protected int mBaseTimePadding = 5; //下方文字

    protected Paint mGridPaint = new Paint(Paint.ANTI_ALIAS_FLAG);

    private Bitmap mBitmapLogo = BitmapFactory.decodeResource(getContext().getResources(), R.drawable.ic_app_logo);

    protected GestureDetectorCompat mDetector;
    protected boolean isLongPress = false; //是否长按事件
    protected boolean isClosePress = true; //关闭长按时间

    private float mXDown;
    private float mYDown;

    private float mXMove;
    private float mYMove;
    //判定为拖动的最小移动像素数
    private int mTouchSlop;


    public BaseView(Context context) {
        super(context);
        initView(context);
    }

    public BaseView(Context context, @Nullable AttributeSet attrs) {
        super(context, attrs);
        initView(context);
    }

    public BaseView(Context context, @Nullable AttributeSet attrs, int defStyleAttr) {
        super(context, attrs, defStyleAttr);
        initView(context);
    }

    private void initView(Context context) {
        this.mContext = context;

        setWillNotDraw(false);
        ;

        if (Build.VERSION.SDK_INT < Build.VERSION_CODES.LOLLIPOP){
            this.setLayerType(View.LAYER_TYPE_SOFTWARE, null);
        }

        mDetector = new GestureDetectorCompat(getContext(), this);

        ViewConfiguration configuration = ViewConfiguration.get(getContext());
        // 获取TouchSlop值
//        mTouchSlop = ViewConfiguration.get(getContext()).getScaledTouchSlop();
        mTouchSlop = ViewConfigurationCompat.getScaledPagingTouchSlop(configuration);

        mTopPadding = DisplayUtil.dip2px(mContext, mTopPadding);
        mBottomPadding = DisplayUtil.dip2px(mContext, mBottomPadding);
        mPadding = DisplayUtil.dip2px(mContext, mPadding);
    }


    @Override
    protected void onMeasure(int widthMeasureSpec, int heightMeasureSpec) {
        super.onMeasure(widthMeasureSpec, heightMeasureSpec);
        int widthSpecMode = MeasureSpec.getMode(widthMeasureSpec);
        int widthSpecSize = MeasureSpec.getSize(widthMeasureSpec);
        int heightSpecMode = MeasureSpec.getMode(heightMeasureSpec);
        int heightSpecSize = MeasureSpec.getSize(heightMeasureSpec);
        if (widthSpecMode == AT_MOST && heightSpecMode == AT_MOST) {
            setMeasuredDimension((int) DEF_WIDTH, (int) DEF_HIGHT);
        } else if (widthSpecMode == AT_MOST) {
            setMeasuredDimension((int) DEF_WIDTH, heightSpecSize);
        } else if (heightSpecMode == AT_MOST) {
            setMeasuredDimension(widthSpecSize, (int) DEF_HIGHT);
        } else {
            setMeasuredDimension(widthSpecSize, heightSpecSize);
        }
        this.mWidth = getMeasuredWidth();
        this.mBaseWidth = mWidth;
        this.mHeight = getMeasuredHeight();
        this.mBaseHeight = mHeight - mTopPadding - mBottomPadding;
        notifyChanged();
    }

    @Override
    protected void onSizeChanged(int w, int h, int oldw, int oldh) {
        super.onSizeChanged(w, h, oldw, oldh);
        this.mWidth = getMeasuredWidth();
        this.mBaseWidth = mWidth;
        this.mHeight = getMeasuredHeight();
        this.mBaseHeight = mHeight - mTopPadding - mBottomPadding;
        notifyChanged();
    }


    @Override
    protected void onDraw(Canvas canvas) {
        super.onDraw(canvas);

        //绘制水印
        drawMainViewLogo(canvas);

    }


    /**
     * 添加视图水印
     *
     * @param canvas
     */
    public void drawMainViewLogo(Canvas canvas) {
        if (mBitmapLogo != null) {
            int mLeft = getWidth() / 2 - mBitmapLogo.getWidth() / 2;
            int mTop = mBaseHeight / 2 - mBitmapLogo.getHeight() / 2 + mTopPadding;
            canvas.drawBitmap(mBitmapLogo, mLeft, mTop, null);
        }
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
                mXDown = event.getX();
                mYDown = event.getY();
                break;

            case MotionEvent.ACTION_MOVE:
                final float curX = event.getX();
                final float curY = event.getY();
                mXMove += Math.abs(curX - mXDown);
                mYMove += Math.abs(curY - mYDown);

                mXDown = curX;
                mYDown = curY;

                if (mYMove > mXMove){
                    return false;
                }

                //一个点的时候滑动
                if (event.getPointerCount() == 1) {
                    //长按之后移动
//                    if (isLongPress || !isClosePress) {
                    Log.i("---> : ", "onTouchEvent");
                    calculateSelectedX(event.getX());
//                    invalidate();
//                    }
                }
                break;
            case MotionEvent.ACTION_UP:
                if (!isClosePress) {
                    isLongPress = false;
                }
//                invalidate();
                break;
            case MotionEvent.ACTION_CANCEL:
                if (!isClosePress) {
                    isLongPress = false;
                }
//                invalidate();
                break;
        }
        this.mDetector.onTouchEvent(event);
        return true;
    }


    protected abstract void notifyChanged();
    protected abstract void calculateSelectedX(float x);

    //高度
    protected float getFontBaseLineHeight(Paint paint) {
        Paint.FontMetrics fm = paint.getFontMetrics();
        float textHeight = fm.descent - fm.ascent;
        float baseLine = (textHeight - fm.bottom - fm.top) / 2;
        return baseLine;
    }

    //文字的高度
    protected float getFontHeight(Paint paint) {
        Paint.FontMetrics fm = paint.getFontMetrics();
        float textHeight = fm.descent - fm.ascent;
        return textHeight;
    }

    //设置表格线宽度
    public void setGridLineWidth(float width) {
        mGridPaint.setStrokeWidth(width);
    }

    //设置表格线颜色
    public void setGridLineColor(int color) {
        mGridPaint.setColor(color);
    }

    public float getDimension(@DimenRes int resId) {
        return getResources().getDimension(resId);
    }

    public int getColor(@ColorRes int resId) {
        return ContextCompat.getColor(getContext(), resId);
    }

    protected int dp2px(float dp) {
        final float scale = getContext().getResources().getDisplayMetrics().density;
        return (int) (dp * scale + 0.5f);
    }

    protected int sp2px(float spValue) {
        final float fontScale = getContext().getResources().getDisplayMetrics().scaledDensity;
        return (int) (spValue * fontScale + 0.5f);
    }

    /******************************长按事件*****************************************/

    @Override
    public boolean onDown(MotionEvent e) {
        return false;
    }

    @Override
    public void onShowPress(MotionEvent e) {
    }

    @Override
    public boolean onSingleTapUp(MotionEvent e) {
        switch (e.getAction()){
            case MotionEvent.ACTION_DOWN:
                Log.i("--->", "ACTION_DOWN");
                break;
            case MotionEvent.ACTION_UP: //双指点击时不会触发
                Log.i("--->", "ACTION_UP");
                if (isClosePress) {
                    //代处理其它事件
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

    @Override
    public boolean onScroll(MotionEvent e1, MotionEvent e2, float distanceX, float distanceY) {
        return false;
    }

    @Override
    public void onLongPress(MotionEvent e) {
        isLongPress = true;
        isClosePress = false;
        calculateSelectedX(e.getX());
//        invalidate();
    }


    @Override
    public boolean onFling(MotionEvent e1, MotionEvent e2, float velocityX, float velocityY) {
        return false;
    }



}
