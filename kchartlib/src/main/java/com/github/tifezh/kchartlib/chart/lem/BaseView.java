package com.github.tifezh.kchartlib.chart.lem;

import android.content.Context;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.graphics.Canvas;
import android.graphics.Color;
import android.graphics.Paint;
import android.support.annotation.ColorRes;
import android.support.annotation.Nullable;
import android.support.annotation.StringRes;
import android.support.v4.view.GestureDetectorCompat;
import android.support.v4.view.ViewConfigurationCompat;
import android.util.AttributeSet;
import android.util.Log;
import android.view.GestureDetector;
import android.view.MotionEvent;
import android.view.View;
import android.view.ViewConfiguration;

import com.github.tifezh.kchartlib.R;

import static android.view.View.MeasureSpec.AT_MOST;

/**
 * Description 自定义view最基础的类
 * Author puyantao
 * Email 1067899750@qq.com
 * Date 2018-10-19 11:22
 */

public abstract class BaseView extends View  implements GestureDetector.OnGestureListener{

    protected String TAG;

    protected Context mContext;

    protected final float DEF_WIDTH = 650;
    protected final float DEF_HIGHT = 400;

    //测量的控件宽高，会在onMeasure中进行测量。
    protected int mBaseWidth;//折线图宽度
    protected int mBaseHeight;//折线图高度
    protected int mHeight; //试图高度
    protected int mWidth;  //试图宽度

    protected Paint mGridPaint = new Paint(Paint.ANTI_ALIAS_FLAG);
    protected Paint mBackgroundPaint = new Paint(Paint.ANTI_ALIAS_FLAG);
    protected int mBackgroundColor;
    protected int GridColumns = 5; //列数
    protected int mGridRows = 5; //行数

    protected int mTopPadding = 100; //据顶部
    protected int mBottomPadding = 150;//距底部

    protected float mScaleY = 1; //Y轴单位量
    protected float mScaleX = 1; //x轴的单位量
    protected long mTotalTime; //总时间
    protected long mPointCount = 0; //点的个数

    //左右padding,允许修改
    protected int mBasePaddingLeft = 50;
    protected int mBasePaddingRight = 50;
    protected float mBaseTextPaddingLeft = 15; //字体据左侧的距离
    protected float mBaseTextPaddingRight = 15;//字体据右侧的距离
    protected int mBaseTimePadding = 5; //下方文字

    protected GestureDetectorCompat mDetector;
    protected boolean isLongPress = false; //是否长按事件
    protected boolean isClosePress = true; //关闭长按时间
    private Bitmap mBitmapLogo = BitmapFactory.decodeResource(getContext().getResources(), R.drawable.app_bg_logo);


    private float mXDown;
    private float mYDown;

    private float mXMove;
    private float mYMove;

    /**
     * 判定为拖动的最小移动像素数
     */
    private int mTouchSlop;

    public BaseView(Context context) {
        this(context, null);
        init();
    }

    public BaseView(Context context, @Nullable AttributeSet attrs) {
        this(context, attrs, 0);
        init();
    }

    public BaseView(Context context, @Nullable AttributeSet attrs, int defStyleAttr) {
        super(context, attrs, defStyleAttr);
        init();
        TAG = this.getClass().getSimpleName();
        mContext = context;
    }

    protected void init() {
        mDetector = new GestureDetectorCompat(getContext(), this);

        ViewConfiguration configuration = ViewConfiguration.get(getContext());
        // 获取TouchSlop值
        mTouchSlop = ViewConfigurationCompat.getScaledPagingTouchSlop(configuration);

        mBackgroundColor = Color.parseColor("#101114");
        mBackgroundPaint.setColor(mBackgroundColor);

        mGridPaint.setColor(Color.parseColor("#20FFFFFF")); //网格线颜色
        mGridPaint.setStrokeWidth(dp2px(1));

    }

    @Override
    protected void onDraw(Canvas canvas) {
        super.onDraw(canvas);
        //绘制背景颜色
        canvas.drawColor(mBackgroundColor);
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
        mBaseWidth = mWidth = getMeasuredWidth();
        mHeight = getMeasuredHeight();
        mBaseHeight = mHeight - mTopPadding - mBottomPadding;
        notifyChanged();
    }

    @Override
    protected void onSizeChanged(int w, int h, int oldw, int oldh) {
        super.onSizeChanged(w, h, oldw, oldh);
        this.mHeight = h;
        this.mBaseWidth = mHeight - mTopPadding - mBottomPadding;
        this.mWidth = this.mBaseWidth = w;
        notifyChanged();
    }

    @Override
    protected void onLayout(boolean changed, int left, int top, int right, int bottom) {
        super.onLayout(changed, left, top, right, bottom);
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
                        calculateSelectedX(event.getX());
                        invalidate();
//                    }
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
        return true;
    }


    protected abstract void notifyChanged();
    protected abstract void calculateSelectedX(float x);


    protected String getString(@StringRes int stringId) {
        return getResources().getString(stringId);
    }


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

    /**
     * 根据颜色id获取颜色
     *
     * @param colorId
     * @return
     */
    protected int getColor(@ColorRes int colorId) {
        return getResources().getColor(colorId);
    }


    protected int dp2px(float dp) {
        final float scale = getContext().getResources().getDisplayMetrics().density;
        return (int) (dp * scale + 0.5f);
    }

    protected int sp2px(float spValue) {
        final float fontScale = getContext().getResources().getDisplayMetrics().scaledDensity;
        return (int) (spValue * fontScale + 0.5f);
    }


    //----------------------对用户暴露可以修改的参数------------------


    public float getDEF_WIDTH() {
        return DEF_WIDTH;
    }

    public float getDEF_HIGHT() {
        return DEF_HIGHT;
    }

    public void getBaseHeight(int height) {
        mHeight = height;
    }

    public void setBaseWidth(int baseWidth) {
        mBaseWidth = baseWidth;
    }

    public void getBaseTopPadding(int top) {
        mTopPadding = top;
    }

    public void setBaseBottomPadding(int bottom) {
        mBottomPadding = bottom;
    }

    public void setLongPress(boolean longPress) {
        isLongPress = longPress;
    }

    public void setClosePress(boolean closePress){
        isClosePress = closePress;
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
        GridColumns = gridColumns;
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
        invalidate();
    }


    @Override
    public boolean onFling(MotionEvent e1, MotionEvent e2, float velocityX, float velocityY) {
        return false;
    }

}
