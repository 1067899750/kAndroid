package com.github.tifezh.kchartlib.chart.groupview;

import android.content.Context;
import android.graphics.Canvas;
import android.graphics.Color;
import android.graphics.Paint;
import android.support.annotation.ColorRes;
import android.support.annotation.DimenRes;
import android.support.annotation.NonNull;
import android.support.annotation.Nullable;
import android.support.v4.content.ContextCompat;
import android.util.AttributeSet;
import android.util.DisplayMetrics;
import android.util.Log;
import android.view.View;
import android.view.WindowManager;

import com.github.tifezh.kchartlib.R;
import com.github.tifezh.kchartlib.chart.base.IGroupDraw;
import com.github.tifezh.kchartlib.chart.base.IValueFormatter;
import com.github.tifezh.kchartlib.chart.comInterface.ILem;

import static com.github.tifezh.kchartlib.utils.DensityUtil.dp2px;

public class ChildView extends View implements IGroupDraw<ILem> {
    private Context mContext;


    private int mBasePaddingLeft = 50;

    private int mTopPadding = dp2px(30); //据顶部
    protected int mBottomPadding = dp2px(57);//距底部
    private float mColumnPadding = 10;

    private int mBackgroundColor;
    private Paint mBackgroundPaint = new Paint(Paint.ANTI_ALIAS_FLAG);
    private Paint mGridPaint = new Paint(Paint.ANTI_ALIAS_FLAG);
    private Paint mDotRingPaint = new Paint(Paint.ANTI_ALIAS_FLAG); //圆点
    private Paint mLinePaint = new Paint(Paint.ANTI_ALIAS_FLAG); //线

    private ILem mILem;
    private int selectedIndex = 0;
    private float mColumnWidth;
    private float mCurveMax; //Curve最大值
    private float mCurveScaleY = 1; //Y轴单位量
    private float mScaleX = 1; //x轴的单位量


    public ChildView(Context context) {
        super(context);
        initView(context);
    }

    public ChildView(Context context, @Nullable AttributeSet attrs) {
        super(context, attrs);
        initView(context);
    }

    public ChildView(Context context, @Nullable AttributeSet attrs, int defStyleAttr) {
        super(context, attrs, defStyleAttr);
        initView(context);
    }

    private void initView(Context context) {
        this.mContext = context;
        mBackgroundColor = Color.parseColor("#00000000");
        mBackgroundPaint.setColor(mBackgroundColor);

        mGridPaint.setStrokeWidth(getDimension(R.dimen.chart_grid_line_width));
        mGridPaint.setColor(getColor(R.color.chart_green));


        mDotRingPaint.setStrokeWidth(dp2px(2)); //圆弧
        mDotRingPaint.setStyle(Paint.Style.STROKE);
        mDotRingPaint.setColor(getColor(R.color.c6774FF));

    }

    public void initData(int selectedIndex, float columnWidth, float curveMax, float scaleX,
                         float curveScaleY, ILem lem) {
        this.selectedIndex = selectedIndex;
        this.mColumnWidth = columnWidth;
        this.mCurveMax = curveMax;
        this.mILem = lem;
        this.mCurveScaleY = curveScaleY;
        this.mScaleX = scaleX;
        invalidate();
    }

    @Override
    protected void onDraw(Canvas canvas) {
        super.onDraw(canvas);
        //绘制背景颜色
        canvas.drawColor(mBackgroundColor);

        if (mILem == null) {
            return;
        }

        drawLongClickLine(canvas); //长按指示线


    }


    private void drawLongClickLine(Canvas canvas) {

        float r = mColumnWidth * 0.5f / 2;
        canvas.drawCircle(getX(selectedIndex), getCurveY(mILem.getCurve()),
                r, mDotRingPaint);

        //画指示线
        if (selectedIndex < 0) return;
        mLinePaint.setColor(getColor(R.color.c6774FF));
        mLinePaint.setStrokeWidth(dp2px(1));
        Log.i("---> ； selectedIndex ：", selectedIndex + "");
        float x = getX(selectedIndex);
        //轴线

//        canvas.drawLine(x, mTopPadding, x, mHeight - mBottomPadding, mLinePaint);//Y
        if (mTopPadding < (getCurveY(mILem.getCurve()) - mColumnWidth * 0.25f)) {
            canvas.drawLine(x, mTopPadding, x,
                    getCurveY(mILem.getCurve()) - mColumnWidth * 0.25f,
                    mLinePaint);
        }
        if ((getCurveY(mILem.getCurve()) + mColumnWidth * 0.25f) < (this.getHeight() - mBottomPadding)) {
            canvas.drawLine(x, getCurveY(mILem.getCurve()) + mColumnWidth * 0.25f,
                    x, this.getHeight() - mBottomPadding, mLinePaint);
        }

    }


    /**
     * 修正y值
     */
    private float getCurveY(float value) {
        return mTopPadding + (mCurveMax - value) * mCurveScaleY;
    }

    private float getX(int i) {
        return mBasePaddingLeft + mColumnPadding + mScaleX * i + mColumnWidth / 2;
    }


    public float getDimension(@DimenRes int resId) {
        return getResources().getDimension(resId);
    }

    public int getColor(@ColorRes int resId) {
        return ContextCompat.getColor(getContext(), resId);
    }


    @Override
    public void drawTranslated(@Nullable ILem lastPoint, @NonNull ILem curPoint, float lastX, float curX, @NonNull Canvas canvas, @NonNull BaseGroupView view, int position) {

    }

    @Override
    public void drawText(@NonNull Canvas canvas, @NonNull BaseGroupView view, int position, float x, float y) {

    }

    @Override
    public float getMaxValue(ILem point) {
        return 0;
    }

    @Override
    public float getMinValue(ILem point) {
        return 0;
    }

    @Override
    public IValueFormatter getValueFormatter() {
        return null;
    }

    @Override
    public void setTargetColor(int... color) {

    }


}
