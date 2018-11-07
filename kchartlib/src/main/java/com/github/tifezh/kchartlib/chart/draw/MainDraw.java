package com.github.tifezh.kchartlib.chart.draw;

import android.content.Context;
import android.graphics.Canvas;
import android.graphics.Color;
import android.graphics.Paint;
import android.graphics.RectF;
import android.support.annotation.NonNull;
import android.support.annotation.Nullable;
import android.support.v4.content.ContextCompat;
import android.util.DisplayMetrics;
import android.view.WindowManager;

import com.github.tifezh.kchartlib.R;
import com.github.tifezh.kchartlib.chart.base.IChartDraw;
import com.github.tifezh.kchartlib.chart.base.IValueFormatter;
import com.github.tifezh.kchartlib.chart.comInterface.ICandle;
import com.github.tifezh.kchartlib.chart.comInterface.IKLine;
import com.github.tifezh.kchartlib.chart.formatter.ValueFormatter;
import com.github.tifezh.kchartlib.chart.view.BaseKChartView;
import com.github.tifezh.kchartlib.utils.DensityUtil;

import java.util.ArrayList;
import java.util.List;

/**
 *
 * Description 主图的实现类
 * Author puyantao
 * Email 1067899750@qq.com
 * Date 2018-11-6 11:31
 */


public class MainDraw implements IChartDraw<ICandle> {

    private float mCandleWidth = 0;
    private float mCandleLineWidth = 0;
    private Paint mRedPaint = new Paint(Paint.ANTI_ALIAS_FLAG);
    private Paint mGreenPaint = new Paint(Paint.ANTI_ALIAS_FLAG);
    //MA
    private Paint ma5Paint = new Paint(Paint.ANTI_ALIAS_FLAG);
    private Paint ma10Paint = new Paint(Paint.ANTI_ALIAS_FLAG);
    private Paint ma20Paint = new Paint(Paint.ANTI_ALIAS_FLAG);
    private Paint ma40Paint = new Paint(Paint.ANTI_ALIAS_FLAG);
    private Paint ma60Paint = new Paint(Paint.ANTI_ALIAS_FLAG);
    //Boll
    private Paint mUpPaint = new Paint(Paint.ANTI_ALIAS_FLAG);
    private Paint mMbPaint = new Paint(Paint.ANTI_ALIAS_FLAG);
    private Paint mDnPaint = new Paint(Paint.ANTI_ALIAS_FLAG);

    private Paint mTargetPaint = new Paint(Paint.ANTI_ALIAS_FLAG);
    private Paint mTargetNamePaint = new Paint(Paint.ANTI_ALIAS_FLAG);

    private Paint mSelectorTextPaint = new Paint(Paint.ANTI_ALIAS_FLAG);
    private Paint mSelectorBackgroundPaint = new Paint(Paint.ANTI_ALIAS_FLAG);
    private Context mContext;

    private boolean mCandleSolid = true;
    private boolean mShowMA = true;//显示boll
    private int screenWidth=0;
    private int screenHeight=0;
    private boolean portraitScreen = true;//默认竖屏
    private boolean mLineFeed=false;//ma 换行显示
    private float y3=0;

    public MainDraw(BaseKChartView view) {
        Context context = view.getContext();
        mContext = context;
        mRedPaint.setColor(ContextCompat.getColor(context, R.color.chart_red));
        mGreenPaint.setColor(ContextCompat.getColor(context, R.color.chart_green));

        WindowManager wm = (WindowManager) mContext.getSystemService(Context.WINDOW_SERVICE);
        DisplayMetrics dm = new DisplayMetrics();
        wm.getDefaultDisplay().getMetrics(dm);
        screenWidth = dm.widthPixels;
        screenHeight=dm.heightPixels;
    }

    @Override
    public void drawTranslated(@Nullable ICandle lastPoint, @NonNull ICandle curPoint, float lastX, float curX,
                               @NonNull Canvas canvas, @NonNull BaseKChartView view, int position) {
        //绘制柱体
        drawCandle(view, canvas, curPoint, curX, curPoint.getHighPrice(), curPoint.getLowPrice(), curPoint.getOpenPrice(), curPoint.getClosePrice());
        if (mShowMA) {
            //画ma5
            if (lastPoint.getMA5Price() != 0) {
                view.drawMainLine(canvas, ma5Paint, lastX, lastPoint.getMA5Price(), curX, curPoint.getMA5Price());
            }
            //画ma10
            if (lastPoint.getMA10Price() != 0) {
                view.drawMainLine(canvas, ma10Paint, lastX, lastPoint.getMA10Price(), curX, curPoint.getMA10Price());
            }
            //画ma20
            if (lastPoint.getMA20Price() != 0) {
                view.drawMainLine(canvas, ma20Paint, lastX, lastPoint.getMA20Price(), curX, curPoint.getMA20Price());
            }
            //画ma40
            if (lastPoint.getMA20Price() != 0) {
                view.drawMainLine(canvas, ma40Paint, lastX, lastPoint.getMA40Price(), curX, curPoint.getMA40Price());
            }
            //画ma60
            if (lastPoint.getMA60Price() != 0) {
                view.drawMainLine(canvas, ma60Paint, lastX, lastPoint.getMA60Price(), curX, curPoint.getMA60Price());
            }
        } else {
            //画boll
            if (lastPoint.getUp() != 0) {
                view.drawMainLine(canvas, mUpPaint, lastX, lastPoint.getUp(), curX, curPoint.getUp());
            }
            if (lastPoint.getMb() != 0) {
                view.drawMainLine(canvas, mMbPaint, lastX, lastPoint.getMb(), curX, curPoint.getMb());
            }
            if (lastPoint.getDn() != 0) {
                view.drawMainLine(canvas, mDnPaint, lastX, lastPoint.getDn(), curX, curPoint.getDn());
            }
        }

    }

    @Override
    public void drawText(@NonNull Canvas canvas, @NonNull BaseKChartView view, int position, float x, float y) {
        float x2 = BaseKChartView.mChildTextPaddingX;
        float y2=0;
        x = BaseKChartView.mChildTextPaddingX;
        ICandle point = (IKLine) view.getItem(position);
        float textX;
        if (mShowMA) {
            y = y - BaseKChartView.mChildTextPaddingY;
            if(portraitScreen){//竖屏
                y3=y;
            }
            //ma
            String text = "MA";
            canvas.drawText(text, x, y3, mTargetPaint);

            x += DensityUtil.dp2px(35);//适配不同机型间距
            text = "MA5:" + view.formatValue(point.getMA5Price());
            canvas.drawText(text, x, y3, ma5Paint);
            x += ma5Paint.measureText(text);

            x += BaseKChartView.mTextPaddingLeft;
            text = "MA10:" + view.formatValue(point.getMA10Price());
            canvas.drawText(text, x, y3, ma10Paint);
            x += ma10Paint.measureText(text);

            x += BaseKChartView.mTextPaddingLeft;
            text = "MA20:" + view.formatValue(point.getMA20Price());
            canvas.drawText(text, x, y3, ma20Paint);

            textX=2*x;
            if(textX>=screenHeight){
                mLineFeed=true;
            }else {
                mLineFeed=false;
            }
            if(portraitScreen){//竖屏
                //第二行
                x2 += DensityUtil.dp2px(35);
                y2 = y + BaseKChartView.mChildTextPaddingY - DensityUtil.dp2px(4);
                text = "MA40:" + view.formatValue(point.getMA40Price());
                canvas.drawText(text, x2, y2, ma40Paint);
                x2 += ma40Paint.measureText(text);

                x2 += BaseKChartView.mTextPaddingLeft;
                text = "MA60:" + view.formatValue(point.getMA60Price());
                canvas.drawText(text, x2, y2, ma60Paint);
            }else {
                if(textX>=screenHeight){//超过屏幕宽度自动换行
                    //第二行
                    x2 += DensityUtil.dp2px(35);
                    y = y + BaseKChartView.mChildTextPaddingY - DensityUtil.dp2px(4);
                    text = "MA40:" + view.formatValue(point.getMA40Price());
                    canvas.drawText(text, x2, y, ma40Paint);
                    x2 += ma40Paint.measureText(text);

                    x2 += BaseKChartView.mTextPaddingLeft;
                    text = "MA60:" + view.formatValue(point.getMA60Price());
                    canvas.drawText(text, x2, y, ma60Paint);
                }else {
                    x += ma20Paint.measureText(text);
                    x += BaseKChartView.mTextPaddingLeft;
                    text = "MA40:" + view.formatValue(point.getMA40Price());
                    canvas.drawText(text, x, y3, ma40Paint);
                    x += ma40Paint.measureText(text);

                    x += BaseKChartView.mTextPaddingLeft;
                    text = "MA60:" + view.formatValue(point.getMA60Price());
                    canvas.drawText(text, x, y3, ma60Paint);
                }
            }

        } else {
            //boll
            String textBoll = "BOLL";
            y = y - DensityUtil.dp2px(4);

            canvas.drawText(textBoll, x, y, mTargetPaint);
            x += mTargetPaint.measureText(textBoll);

            x += BaseKChartView.mTextPaddingLeft;
            textBoll = "BOLL(26,26,2)";
            canvas.drawText(textBoll, x, y, mTargetNamePaint);
            x += mTargetNamePaint.measureText(textBoll);

            x += BaseKChartView.mTextPaddingLeft;
            textBoll = "M:" + view.formatValue(point.getMb());
            canvas.drawText(textBoll, x, y, mMbPaint);
            x += mMbPaint.measureText(textBoll);

            x += BaseKChartView.mTextPaddingLeft;
            textBoll = "T:" + view.formatValue(point.getUp());
            canvas.drawText(textBoll, x, y, mUpPaint);
            x += mUpPaint.measureText(textBoll);

            x += BaseKChartView.mTextPaddingLeft;
            textBoll = "B:" + view.formatValue(point.getDn());
            canvas.drawText(textBoll, x, y, mDnPaint);
        }
        if (view.isLongPress()) {
            drawSelector(view, canvas);
        }
    }
    @Override
    public float getMaxValue(ICandle point) {
        float bollMax = Float.isNaN(point.getUp()) ? point.getMb() : point.getUp();
        float maMax = Math.max(point.getMA60Price(),Math.max(point.getHighPrice(),point.getClosePrice()));
        return Math.max(bollMax, maMax);
    }

    @Override
    public void drawMaxAndMin(@NonNull BaseKChartView view, Canvas canvas, float maxX, float minX,
                              ICandle maxPoint, ICandle minPoint) {
        float high = view.getMainY(maxPoint.getHighPrice());
        float low = view.getMainY(minPoint.getLowPrice());

        Paint paint = new Paint(Paint.ANTI_ALIAS_FLAG);
        paint.setColor(Color.GRAY);
        paint.setTextAlign(Paint.Align.CENTER);
        paint.setTextSize(view.getTextSize());
        float aa = view.getSclase();

        if (aa < 1.5) {
            paint.setTextScaleX((2.0f - aa));
        } else {
            paint.setTextScaleX(0.5f);
        }

        Paint.FontMetrics fm = paint.getFontMetrics();
        float textHeight = fm.descent - fm.ascent;
        float baseLine = (textHeight - fm.bottom - fm.top) / 2;
        float paddingTop = DensityUtil.dp2px( 8);//距顶
        float paddingBottom = DensityUtil.dp2px( 1);//距底
        canvas.drawText(maxPoint.getHighPrice() + "", maxX, high - baseLine+paddingTop, paint);
        canvas.drawText(minPoint.getLowPrice() + "", minX, low + baseLine-paddingBottom, paint);
    }

    @Override
    public float getMinValue(ICandle point) {
        float maMin = Math.min(point.getMA5Price(), point.getLowPrice());
        float bollMin = Float.isNaN(point.getDn()) ? point.getMb() : point.getDn();
        return Math.min(maMin, bollMin);
    }

    @Override
    public IValueFormatter getValueFormatter() {
        return new ValueFormatter();
    }

    @Override
    public void setTargetColor(int... color) {
        if (color.length > 0) {
            mTargetPaint.setColor(color[0]);
            mTargetNamePaint.setColor(color[1]);
        }
    }


    /**
     * 画Candle
     *
     * @param canvas
     * @param x      x轴坐标
     * @param high   最高价
     * @param low    最低价
     * @param open   开盘价
     * @param close  收盘价
     */
    private void drawCandle(BaseKChartView view, Canvas canvas, ICandle curPoint, float x, float high, float low, float open, float close) {
        high = view.getMainY(high);
        low = view.getMainY(low);
        open = view.getMainY(open);
        close = view.getMainY(close);
        float r = mCandleWidth / 2;
        float lineR = mCandleLineWidth / 2;
        if (open > close) {
            //实心
            if (mCandleSolid) {
                canvas.drawRect(x - r, close, x + r, open, mRedPaint);
                canvas.drawRect(x - lineR, high, x + lineR, low, mRedPaint);

            } else {
                mRedPaint.setStrokeWidth(mCandleLineWidth);
                canvas.drawLine(x, high, x, close, mRedPaint);
                canvas.drawLine(x, open, x, low, mRedPaint);
                canvas.drawLine(x - r + lineR, open, x - r + lineR, close, mRedPaint);
                canvas.drawLine(x + r - lineR, open, x + r - lineR, close, mRedPaint);
                mRedPaint.setStrokeWidth(mCandleLineWidth * view.getScaleX());
                canvas.drawLine(x - r, open, x + r, open, mRedPaint);
                canvas.drawLine(x - r, close, x + r, close, mRedPaint);
            }

        } else if (open < close) {
            canvas.drawRect(x - r, open, x + r, close, mGreenPaint);
            canvas.drawRect(x - lineR, high, x + lineR, low, mGreenPaint);

        } else {
            canvas.drawRect(x - r, open, x + r, close + 1, mRedPaint);
            canvas.drawRect(x - lineR, high, x + lineR, low, mRedPaint);
        }
    }

    /**
     * draw选择器
     *
     * @param view
     * @param canvas
     */
    private void drawSelector(BaseKChartView view, Canvas canvas) {
        Paint.FontMetrics metrics = mSelectorTextPaint.getFontMetrics();
        float textHeight = metrics.descent - metrics.ascent;
        int index = view.getSelectedIndex();
        float bgCircle= DensityUtil.dp2px(4);//背景圆角
        float paddingText = DensityUtil.dp2px( 1);//文字间距
        float margin = DensityUtil.dp2px(8);
        float width = 0;
        float left =margin;
        float top = margin;

        float bgPaddingTop =DensityUtil.dp2px(4);//背景内顶部
        float bgPaddingLeft = DensityUtil.dp2px( 8);//背景左边

        ICandle point = (ICandle) view.getItem(index);
        List<String> strings = new ArrayList<>();
        strings.add(view.formatShortDate(view.getAdapter().getDate(index)));
        strings.add("开盘价");
        strings.add(String.valueOf(point.getOpenPrice()));
        strings.add("最高");
        strings.add(String.valueOf(point.getHighPrice()));
        strings.add("最低");
        strings.add(String.valueOf(point.getLowPrice()));

        strings.add("收盘价");
        strings.add(String.valueOf(point.getClosePrice()));
        strings.add(String.valueOf(point.getUpDown()));
        strings.add(String.valueOf(point.getPercent()));

        strings.add("持仓量");
        strings.add(String.valueOf(point.getInterest()));
        strings.add(String.valueOf(point.getChgInterest()));

        strings.add("成交量");
        strings.add(String.valueOf(point.getVolume()));
        strings.add(String.valueOf(point.getChgVolume()));

        strings.add("结算价");
        strings.add(String.valueOf(point.getPreClose()));

        float height = paddingText * strings.size() + textHeight * (strings.size());
        for (String s : strings) {
            width = Math.max(width, mSelectorTextPaint.measureText(s));
        }
        width=width+(2*left);//文字宽度
        float x = view.translateXtoX(view.getX(index));
        if (x > view.getChartWidth() / 2) {
            left = margin;
        } else {
            left = view.getChartWidth() - width - margin;
        }
        RectF r = new RectF(left, top, left + width, height+(2*top));
        canvas.drawRoundRect(r, bgCircle, bgCircle, mSelectorBackgroundPaint);//画背景

//        if (x > view.getChartWidth() / 2) {
//            left = margin + bgPaddingLeft;
//            mSelectorTextPaint.setTextAlign(Paint.Align.LEFT);
//        } else {
//            left = view.getChartWidth() - margin - bgPaddingLeft;
//            mSelectorTextPaint.setTextAlign(Paint.Align.RIGHT);
//        }

        float y = top + (textHeight- metrics.bottom - metrics.top) / 2;
        for (int i = 0; i < strings.size(); i++) {
            if (i == 1 || i == 3 || i == 5 || i == 7 || i == 11 || i == 14 || i == 17) {
                mSelectorTextPaint.setColor(ContextCompat.getColor(mContext, R.color.color_text_hint_1));
            } else if (i == 8 || i == 9 || i == 10 || i == 13 || i == 16) {//收盘价颜色判断
                if (point.getOpenPrice() < point.getClosePrice()) {
                    mSelectorTextPaint.setColor(ContextCompat.getColor(mContext, R.color.color_positive_value));
                } else {
                    mSelectorTextPaint.setColor(ContextCompat.getColor(mContext, R.color.chart_green));
                }
            } else {
                mSelectorTextPaint.setColor(ContextCompat.getColor(mContext, R.color.chart_white));
            }
            if(i==0){//第一条跟顶部间距
                y=y+bgPaddingTop;
            }
            canvas.drawText(strings.get(i), left+ bgPaddingLeft, y, mSelectorTextPaint);
            y += textHeight + paddingText;
        }
    }


    /**
     * ma 是否需换行
     * @return
     */
    public boolean getLineFeed(){
        return mLineFeed;
    }


    /**
     * 是否是竖屏
     * @param status
     */
    public void setScreenStatus(boolean status){
        this.portraitScreen=status;
    }

    /**
     * 设置蜡烛宽度
     *
     * @param candleWidth
     */
    public void setCandleWidth(float candleWidth) {
        mCandleWidth = candleWidth;
    }

    /**
     * 设置蜡烛线宽度
     *
     * @param candleLineWidth
     */
    public void setCandleLineWidth(float candleLineWidth) {
        mCandleLineWidth = candleLineWidth;
    }

    /**
     * 设置ma5颜色
     *
     * @param color
     */
    public void setMa5Color(int color) {
        this.ma5Paint.setColor(color);
    }

    /**
     * 设置ma10颜色
     *
     * @param color
     */
    public void setMa10Color(int color) {
        this.ma10Paint.setColor(color);
    }

    /**
     * 设置ma20颜色
     *
     * @param color
     */
    public void setMa20Color(int color) {
        this.ma20Paint.setColor(color);
    }

    /**
     * 设置ma40颜色
     *
     * @param color
     */
    public void setMa40Color(int color) {
        this.ma40Paint.setColor(color);
    }


    /**
     * 设置ma60颜色
     *
     * @param color
     */
    public void setMa60Color(int color) {
        this.ma60Paint.setColor(color);
    }


    /**
     * 设置选择器文字颜色
     *
     * @param color
     */
    public void setSelectorTextColor(int color) {
        mSelectorTextPaint.setColor(color);
    }

    /**
     * 设置选择器文字大小
     *
     * @param textSize
     */
    public void setSelectorTextSize(float textSize) {
        mSelectorTextPaint.setTextSize(textSize);
    }

    /**
     * 设置选择器背景
     *
     * @param color
     */
    public void setSelectorBackgroundColor(int color) {
        mSelectorBackgroundPaint.setColor(color);
    }

    /**
     * 设置曲线宽度
     */
    public void setLineWidth(float width) {
        ma60Paint.setStrokeWidth(width);
        ma40Paint.setStrokeWidth(width);
        ma20Paint.setStrokeWidth(width);
        ma10Paint.setStrokeWidth(width);
        ma5Paint.setStrokeWidth(width);
    }

    /**
     * 设置文字大小
     */
    public void setTextSize(float textSize) {
        ma60Paint.setTextSize(textSize);
        ma40Paint.setTextSize(textSize);
        ma20Paint.setTextSize(textSize);
        ma10Paint.setTextSize(textSize);
        ma5Paint.setTextSize(textSize);
        mTargetPaint.setTextSize(textSize);
        mTargetNamePaint.setTextSize(textSize);
    }

    /**
     * 蜡烛是否实心
     */
    public void setCandleSolid(boolean candleSolid) {
        mCandleSolid = candleSolid;

    }

    public boolean getShowMA() {
        return mShowMA;
    }

    /**
     * MA 和Boll 视图切换
     */
    public void setShowMA(boolean showMA) {
        mShowMA = showMA;
    }


    //boll 样式设置

    /**
     * 设置up颜色
     */
    public void setBollUpColor(int color) {
        mUpPaint.setColor(color);
    }

    /**
     * 设置mb颜色
     *
     * @param color
     */
    public void setBollMbColor(int color) {
        mMbPaint.setColor(color);
    }

    /**
     * 设置dn颜色
     */
    public void setBollDnColor(int color) {
        mDnPaint.setColor(color);
    }

    /**
     * 设置曲线宽度
     */
    public void setBollLineWidth(float width) {
        mUpPaint.setStrokeWidth(width);
        mMbPaint.setStrokeWidth(width);
        mDnPaint.setStrokeWidth(width);
    }

    /**
     * 设置文字大小
     */
    public void setBollTextSize(float textSize) {
        mUpPaint.setTextSize(textSize);
        mMbPaint.setTextSize(textSize);
        mDnPaint.setTextSize(textSize);
    }

}
