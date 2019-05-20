package com.github.tifezh.kchartlib.chart.detail;

import android.content.Context;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.graphics.Canvas;
import android.graphics.Color;
import android.graphics.Paint;
import android.support.v4.view.GestureDetectorCompat;
import android.util.AttributeSet;
import android.util.Log;
import android.view.GestureDetector;
import android.view.MotionEvent;
import android.view.ScaleGestureDetector;
import android.view.View;

import com.github.tifezh.kchartlib.R;
import com.github.tifezh.kchartlib.chart.comInterface.ILem;

import java.util.ArrayList;
import java.util.Collection;
import java.util.List;

import static android.view.View.MeasureSpec.AT_MOST;

/**
 * @Describe 明细试图基类
 * @Author puyantao
 * @Email 1067899750@qq.com
 * @create 2019/5/20 10:58
 */
public abstract class BaseDetailView extends View implements GestureDetector.OnGestureListener,
        ScaleGestureDetector.OnScaleGestureListener {
    private Context mContext;


    protected Paint mGridPaint = new Paint(Paint.ANTI_ALIAS_FLAG);
    protected Paint mBackgroundPaint = new Paint(Paint.ANTI_ALIAS_FLAG);
    protected int mBackgroundColor;

    protected GestureDetectorCompat mDetector; //点击手势
    protected ScaleGestureDetector mScaleDetector;//缩放手势

    protected List<IDetailLine> mPoints = new ArrayList<>();
    protected long mPointCount = 0; //点的个数

    protected boolean isLongPress = false; //是否长按事件
    protected boolean isClosePress = true; //关闭长按时间
    private Bitmap mBitmapLogo = BitmapFactory.decodeResource(getContext().getResources(), R.drawable.logo);

    protected int mTopPadding = 1; //据顶部
    protected int mBottomPadding = 50;//距底部
    protected int mBaseWidth;//折线图宽度
    protected int mBaseHeight;//折线图高度
    protected int mHeight; //试图高度
    protected int mWidth;  //试图宽度

    //左右padding,允许修改
    protected int mBasePaddingLeft = 80;
    protected int mBasePaddingRight = 0;

    protected int GridColumns = 1; //列数
    protected int mGridRows = 1; //行数


    private float mXDown;
    private float mYDown;
    private float mXMove;
    private float mYMove;

    public BaseDetailView(Context context) {
        super(context);
        initView(context);
    }

    public BaseDetailView(Context context, AttributeSet attrs) {
        super(context, attrs);
        initView(context);
    }

    public BaseDetailView(Context context, AttributeSet attrs, int defStyleAttr) {
        super(context, attrs, defStyleAttr);
        initView(context);
    }

    private void initView(Context context) {
        this.mContext = context;

        mDetector = new GestureDetectorCompat(getContext(), this);
        mScaleDetector = new ScaleGestureDetector(getContext(), this);


        mBackgroundColor = Color.parseColor("#402A2D4F");
        mBackgroundPaint.setColor(mBackgroundColor);

        mGridPaint.setColor(Color.parseColor("#60FFFFFF")); //网格线颜色
        mGridPaint.setStrokeWidth(dp2px(1));

    }


    public void initData(Collection<? extends IDetailLine> datas) {
        mPoints.clear();
        if (datas != null) {
            this.mPoints.addAll(datas);
            mPointCount = mPoints.size();
        }
        notifyChanged();
    }

    @Override
    protected void onMeasure(int widthMeasureSpec, int heightMeasureSpec) {
        super.onMeasure(widthMeasureSpec, heightMeasureSpec);
        int widthSpecSize = MeasureSpec.getSize(widthMeasureSpec);
        int heightSpecSize = MeasureSpec.getSize(heightMeasureSpec);
        setMeasuredDimension(widthSpecSize, heightSpecSize);

        mWidth = getMeasuredWidth();
        mBaseWidth = mWidth - mBasePaddingLeft - mBasePaddingRight;
        mHeight = getMeasuredHeight();
        mBaseHeight = mHeight - mTopPadding - mBottomPadding;
        notifyChanged();
    }

    @Override
    protected void onSizeChanged(int w, int h, int oldw, int oldh) {
        super.onSizeChanged(w, h, oldw, oldh);
        this.mHeight = h;
        this.mBaseHeight = mHeight - mTopPadding - mBottomPadding;

        this.mWidth = w;
        this.mBaseWidth = mWidth - mBasePaddingRight - mBasePaddingLeft;
        notifyChanged();
    }

    @Override
    protected void onLayout(boolean changed, int left, int top, int right, int bottom) {
        super.onLayout(changed, left, top, right, bottom);

    }

    @Override
    protected void onDraw(Canvas canvas) {
        super.onDraw(canvas);

        //绘制背景颜色
        canvas.drawColor(mBackgroundColor);
        if (mWidth == 0 || mHeight == 0) {
            return;
        }

        //绘制水印
//        drawMainViewLogo(canvas);

        drawGird(canvas); //绘制网格
        if (mPoints == null || mPoints.size() == 0) {
            return;
        }

    }

    public void drawGird(Canvas canvas) {
        //先画出坐标轴
        canvas.translate(0, mTopPadding);
        canvas.scale(1, 1);
        //横向的grid
        if (mGridRows == 1) {
//            canvas.drawLine(0, mTopPadding, mBaseWidth, mTopPadding, mGridPaint);
            canvas.drawLine(mBasePaddingLeft, mTopPadding + mBaseHeight, mWidth - mBasePaddingRight,
                    mTopPadding + mBaseHeight, mGridPaint);
        } else {
            float rowSpace = mBaseHeight / mGridRows;
            for (int i = 0; i <= mGridRows; i++) {
                canvas.drawLine(mBasePaddingLeft, mTopPadding + rowSpace * i, mWidth - mBasePaddingRight,
                        mTopPadding + rowSpace * i, mGridPaint);
            }
        }

        //纵向的grid
        if (GridColumns == 1) {
            canvas.drawLine(mBasePaddingLeft, mTopPadding, mBasePaddingLeft, mHeight - mBottomPadding, mGridPaint);
        } else {
            float columnSpace = (mBaseWidth - mBasePaddingLeft - mBasePaddingRight) / GridColumns;
            for (int i = 0; i <= GridColumns; i++) {
                canvas.drawLine(columnSpace * i + mBasePaddingLeft, mTopPadding,
                        columnSpace * i + mBasePaddingLeft, mHeight - mBottomPadding, mGridPaint);
            }
        }
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
    public boolean onTouchEvent(MotionEvent event) {
        switch (event.getAction() & MotionEvent.ACTION_MASK) {
            case MotionEvent.ACTION_DOWN:
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

    protected int dp2px(float dp) {
        final float scale = getContext().getResources().getDisplayMetrics().density;
        return (int) (dp * scale + 0.5f);
    }

    protected int sp2px(float spValue) {
        final float fontScale = getContext().getResources().getDisplayMetrics().scaledDensity;
        return (int) (spValue * fontScale + 0.5f);
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

    /*********************************************抽象方法用于实现继承类**************************************/

    protected abstract void notifyChanged();

    protected abstract void calculateSelectedX(float x);


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
        switch (e.getAction()) {
            case MotionEvent.ACTION_DOWN:
                Log.i("--->", "ACTION_DOWN");
                break;
            case MotionEvent.ACTION_UP: //双指点击时不会触发
                Log.i("--->", "ACTION_UP");
                if (isClosePress) {
                    float downX = e.getX();
                    float downY = e.getY();
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
        return false;
    }

    @Override
    public boolean onScaleBegin(ScaleGestureDetector detector) {
        return false;
    }

    @Override
    public void onScaleEnd(ScaleGestureDetector detector) {

    }
}

















