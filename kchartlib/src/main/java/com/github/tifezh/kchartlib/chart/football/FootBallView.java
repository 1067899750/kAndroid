package com.github.tifezh.kchartlib.chart.football;

import android.content.Context;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.graphics.Canvas;
import android.graphics.Paint;
import android.graphics.RectF;
import android.support.annotation.Nullable;
import android.util.AttributeSet;

import com.github.tifezh.kchartlib.R;

import java.util.ArrayList;
import java.util.Collection;
import java.util.List;

/**
 * Description
 * Author puyantao
 * Email 1067899750@qq.com
 * Date 2019-1-17 14:14
 */

public class FootBallView extends BaseView {
    private Bitmap mFootBallBitmap = BitmapFactory.decodeResource(getContext().getResources(), R.drawable.football);
    private Bitmap mStandardBitmap = BitmapFactory.decodeResource(getContext().getResources(), R.drawable.standard);

    private final List<IFootball> mHomePoints = new ArrayList<>();
    private final List<IFootball> mAwayPoints = new ArrayList<>();

    private Paint mGridLinePaint = new Paint(Paint.ANTI_ALIAS_FLAG);
    private Paint mLinePaint = new Paint(Paint.ANTI_ALIAS_FLAG);
    private Paint mLeftLinePaint = new Paint(Paint.ANTI_ALIAS_FLAG);
    private Paint mTextPaint = new Paint(Paint.ANTI_ALIAS_FLAG);
    private Paint mBottomLinePaint = new Paint(Paint.ANTI_ALIAS_FLAG);
    private Paint mFootLinePaint = new Paint(Paint.ANTI_ALIAS_FLAG);

    private int mCentreLineLength = dp2px(4);  //中轴
    private int mCentreSeaftLength = dp2px(2);  //中轴
    private int mLeftPadding = dp2px(20);  //左padding
    private int mReadLength = dp2px(30);
    private int mGrayLength = dp2px(20);

    private int mFootBallLength = dp2px(40);
    private int mStandardLength = dp2px(50);

    private int mBottomLeftPadding = dp2px(30);
    private String mFootBallName = "主客";

    private int mCount = 0;

    public FootBallView(Context context) {
        super(context);
        initView(context);
    }

    public FootBallView(Context context, @Nullable AttributeSet attrs) {
        super(context, attrs);
        initView(context);

    }

    public FootBallView(Context context, @Nullable AttributeSet attrs, int defStyleAttr) {
        super(context, attrs, defStyleAttr);
        initView(context);
    }

    private void initView(Context context) {
        mLinePaint.setColor(getColor(R.color.c3EB86A));

        mFootLinePaint.setColor(getColor(R.color.cF27A68));
        mFootLinePaint.setTextAlign(Paint.Align.CENTER);
        mFootLinePaint.setStrokeWidth(dp2px(3));

        mGridLinePaint.setColor(getColor(R.color.c9EB2CD));
        mGridLinePaint.setStrokeWidth(dp2px(1));

        mLeftLinePaint.setColor(getColor(R.color.c3EB86A));
        mLeftLinePaint.setStyle(Paint.Style.FILL_AND_STROKE);

        mTextPaint.setColor(getColor(R.color.cffffff));

        mBottomLinePaint.setColor(getColor(R.color.c3EB86A));
        mBottomLinePaint.setStrokeWidth(dp2px(3));
    }

    public void initData(Collection<? extends IFootball> homeDatas, Collection<? extends IFootball> AwayDatas) {
        mHomePoints.clear();
        mAwayPoints.clear();
        if (homeDatas != null) {
            this.mHomePoints.addAll(homeDatas);
            mHomePointCount = mHomePoints.size();

            this.mAwayPoints.addAll(AwayDatas);
            mAwayPointCount = mAwayPoints.size();
        }

        mTotalTimers = 90; //一场总时间

        notifyChanged();
    }


    @Override
    protected void notifyChanged() {
        if (mHomePoints.size() <= 0) {
            invalidate();
            return;
        }

        mScaleX = (mWidth - mLeftPadding - mCentreSeaftLength) / mTotalTimers;

    }

    @Override
    protected void calculateSelectedX(float x) {

    }


    @Override
    protected void onDraw(Canvas canvas) {
        super.onDraw(canvas);
        //绘制网格
        drawGird(canvas);
        if (mHomePoints == null || mAwayPoints == null) {
            return;
        }

        drawBottomView(canvas);
        drawText(canvas);
        drawView(canvas);


    }


    //绘制网格线
    private void drawGird(Canvas canvas) {
        //上
        canvas.drawLine(0, mTopPadding, mWidth, mTopPadding, mGridLinePaint);

        //中
        canvas.drawRect(new RectF(0, (mBaseHeight - mCentreLineLength) / 2 + mTopPadding, mWidth,
                (mBaseHeight + mCentreLineLength) / 2 + mTopPadding), mLeftLinePaint);

        //下
        canvas.drawLine(0, mTopPadding + mBaseHeight, mWidth,
                mTopPadding + mBaseHeight, mGridLinePaint);

        //左边
        canvas.drawRect(new RectF(0, mTopPadding, mLeftPadding,
                mTopPadding + mBaseHeight), mLeftLinePaint);

        //上下半场分界线
        canvas.drawRect(new RectF((mBaseWidth + mLeftPadding - mCentreSeaftLength) / 2, mTopPadding,
                (mBaseWidth + mLeftPadding + mCentreSeaftLength) / 2, mTopPadding + mBaseHeight), mLeftLinePaint);

    }

    //绘制下面图标
    private void drawBottomView(Canvas canvas) {
        float textLength = mTextPaint.measureText("进球");
        float textHeight = getFontBaseLineHeight(mTextPaint);

        mTextPaint.setTextAlign(Paint.Align.LEFT);
        mTextPaint.setTextSize(sp2px(12));
        mTextPaint.setColor(getColor(R.color.c6A798E));
        int h = dp2px(7);
        //进球
        canvas.drawBitmap(mFootBallBitmap,
                mBottomLeftPadding,
                mTopPadding + mBaseHeight + mBottomPadding / 2 - mFootBallBitmap.getHeight() / 2, null);

        canvas.drawText("进球",
                mBottomLeftPadding * 1 + mFootBallBitmap.getWidth(),
                mTopPadding + mBaseHeight + mBottomPadding / 2 + textHeight,
                mTextPaint);


        //角球
        canvas.drawBitmap(mStandardBitmap,
                mBottomLeftPadding * 2 + textLength * 1 + mFootBallBitmap.getWidth(),
                mTopPadding + mBaseHeight + mBottomPadding / 2 - mStandardBitmap.getHeight() / 2, null);

        canvas.drawText("角球",
                mBottomLeftPadding * 2 + textLength * 1 + mStandardBitmap.getWidth() + mFootBallBitmap.getWidth(),
                mTopPadding + mBaseHeight + mBottomPadding / 2 + textHeight,
                mTextPaint);


        //射正
        mBottomLinePaint.setTextAlign(Paint.Align.CENTER);
        mBottomLinePaint.setColor(getColor(R.color.cF27A68));
        canvas.drawLine(mBottomLeftPadding * 3 + textLength * 2 + mStandardBitmap.getWidth() + mFootBallBitmap.getWidth(),
                mTopPadding + mBaseHeight + h,
                mBottomLeftPadding * 3 + textLength * 2 + mStandardBitmap.getWidth() + mFootBallBitmap.getWidth(),
                mTopPadding + mBaseHeight + mBottomPadding - h,
                mBottomLinePaint);


        canvas.drawText("射正",
                mBottomLeftPadding * 3 + textLength * 2 + dp2px(5) + mStandardBitmap.getWidth() + mFootBallBitmap.getWidth(),
                mTopPadding + mBaseHeight + mBottomPadding / 2 + textHeight,
                mTextPaint);


        //射偏
        mBottomLinePaint.setColor(getColor(R.color.c3EB86A));
        canvas.drawLine(mBottomLeftPadding * 4 + textLength * 3 + dp2px(5) + mStandardBitmap.getWidth() + mFootBallBitmap.getWidth(),
                mTopPadding + mBaseHeight + h,
                mBottomLeftPadding * 4 + textLength * 3 + dp2px(5) + mStandardBitmap.getWidth() + mFootBallBitmap.getWidth(),
                mTopPadding + mBaseHeight + mBottomPadding - h,
                mBottomLinePaint);

        canvas.drawText("射偏",
                mBottomLeftPadding * 4 + textLength * 3 + dp2px(5) * 2 + mStandardBitmap.getWidth() + mFootBallBitmap.getWidth(),
                mTopPadding + mBaseHeight + mBottomPadding / 2 + textHeight,
                mTextPaint);


    }

    private void drawText(Canvas canvas) {
        //绘制名字
        mTextPaint.setColor(getColor(R.color.cffffff));
        mTextPaint.setTextAlign(Paint.Align.CENTER);
        mTextPaint.setTextSize(sp2px(14));
        for (int i = 0; i < mFootBallName.length(); i++) {
            String subStr = mFootBallName.substring(i, i + 1);
            float strLong = mTextPaint.measureText(subStr);
            if (i == 0) {
                canvas.drawText(subStr, mLeftPadding / 2, mTopPadding + mBaseHeight / 5 + strLong / 2, mTextPaint);
            } else {
                canvas.drawText(subStr, mLeftPadding / 2, mTopPadding + mBaseHeight * 4 / 5, mTextPaint);
            }

        }
    }


    private void drawView(Canvas canvas) {
        //绘制主队进攻
        for (int i = 0; i < mHomePoints.size(); i++) {
            if (mHomePoints.get(i).getHomeSequenceStaus() == 1) {
                drawLine(getX(i, 0) + mLeftPadding,
                        0, canvas, mHomePoints.get(i).getHomeEventType());

            } else if (mHomePoints.get(i).getHomeSequenceStaus() == 2) {
                drawLine(getX(i, 0) + mLeftPadding + mCentreSeaftLength,
                        0, canvas, mHomePoints.get(i).getHomeEventType());

            }

        }

        //绘制客队进攻
        for (int i = 0; i < mAwayPoints.size(); i++) {
            if (mAwayPoints.get(i).getAwaySequenceStaus() == 1) {
                drawLine(getX(i, 1) + mLeftPadding,
                        1, canvas, mAwayPoints.get(i).getAwayEventType());

            } else if (mAwayPoints.get(i).getAwaySequenceStaus() == 2) {
                drawLine(getX(i, 1) + mLeftPadding + mCentreSeaftLength,
                        1, canvas, mAwayPoints.get(i).getAwayEventType());

            }
        }


    }


    /**
     * 根据索引获取x的值
     */
    private float getX(int position, int treamType) {

        if (treamType == 0) { //主队
            float c = mHomePoints.get(position).getHomeDate();
            return mHomePoints.get(position).getHomeDate() * mScaleX;
        } else if (treamType == 1) { //客队
            float c = mAwayPoints.get(position).getAwayDate();
            return mAwayPoints.get(position).getAwayDate() * mScaleX;

        } else {
            return 0;
        }
    }


    /**
     * 绘制柱体
     *
     * @param x         x轴距离
     * @param state     0 主队， 1客队
     * @param canvas
     * @param eventType 事件类型
     */
    private void drawLine(float x, float state, Canvas canvas, int eventType) {
        switch (eventType) {
            case 1: //入球
                if (state == 0) {
                    canvas.drawBitmap(mFootBallBitmap,
                            x + mScaleX / 2,
                            (mBaseHeight - mCentreLineLength) / 2 + mTopPadding - mFootBallLength - mFootBallBitmap.getHeight() / 2, null);
                } else if (state == 1) {
                    canvas.drawBitmap(mFootBallBitmap,
                            x + mScaleX / 2,
                            (mBaseHeight - mCentreLineLength) / 2 + mTopPadding + mFootBallLength, null);
                }
                break;

            case 2: //红牌
                break;

            case 3://黄牌
                break;

            case 4://进攻时间
                break;

            case 5://危险进攻时间
                break;

            case 6://射正球门时间
                mFootLinePaint.setColor(getColor(R.color.cF27A68));
                if (state == 0) {
                    canvas.drawLine(x + mScaleX / 2,
                            (mBaseHeight - mCentreLineLength) / 2 + mTopPadding,
                            x + mScaleX / 2,
                            (mBaseHeight - mCentreLineLength) / 2 + mTopPadding - mReadLength,
                            mFootLinePaint);
                } else if (state == 1) {
                    canvas.drawLine(x + mScaleX / 2,
                            (mBaseHeight + mCentreLineLength) / 2 + mTopPadding,
                            x + mScaleX / 2,
                            (mBaseHeight + mCentreLineLength) / 2 + mTopPadding + mReadLength,
                            mFootLinePaint);
                }
                break;

            case 7://点球
                break;

            case 8://乌龙
                break;

            case 9://两黄变红
                break;

            case 10://射偏球门时间
                mFootLinePaint.setColor(getColor(R.color.c9EB2CD));
                if (state == 0) {
                    canvas.drawLine(x + mScaleX / 2,
                            (mBaseHeight - mCentreLineLength) / 2 + mTopPadding,
                            x + mScaleX / 2,
                            (mBaseHeight - mCentreLineLength) / 2 + mTopPadding - mGrayLength,
                            mFootLinePaint);
                } else if (state == 1) {
                    canvas.drawLine(x + mScaleX / 2,
                            (mBaseHeight + mCentreLineLength) / 2 + mTopPadding,
                            x + mScaleX / 2,
                            (mBaseHeight + mCentreLineLength) / 2 + mTopPadding + mGrayLength,
                            mFootLinePaint);
                }
                break;

            case 11://换人
                break;

            case 12://角球
                if (state == 0) {
                    canvas.drawBitmap(mStandardBitmap,
                            x + mScaleX / 2,
                            (mBaseHeight - mCentreLineLength) / 2 + mTopPadding - mStandardLength - mStandardBitmap.getHeight() / 2, null);
                } else if (state == 1) {
                    canvas.drawBitmap(mStandardBitmap,
                            x + mScaleX / 2,
                            (mBaseHeight - mCentreLineLength) / 2 + mTopPadding + mStandardLength, null);
                }
                break;

            case 13:
                break;

            case 14:
                break;

            case 15://中场时间事件
                break;
        }
    }

    public void setFootBallName(String footBallName) {
        this.mFootBallName = footBallName;
    }


    public void clearMemory() {
        if (mFootBallBitmap != null) {
            mFootBallBitmap.recycle();
            mFootBallBitmap = null;
        }

        if (mStandardBitmap != null) {
            mStandardBitmap.recycle();
            mStandardBitmap = null;
        }
    }

}





















