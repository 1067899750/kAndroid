package com.github.tifezh.kchartlib.chart.football;

import android.content.Context;
import android.graphics.Canvas;
import android.graphics.Paint;
import android.graphics.RectF;
import android.support.annotation.Nullable;
import android.util.AttributeSet;

import com.github.tifezh.kchartlib.R;

import java.util.ArrayList;
import java.util.Collection;
import java.util.List;

public class FootBallView extends BaseView {

    private final List<IFootball> mHomePoints = new ArrayList<>();
    private final List<IFootball> mAwayPoints = new ArrayList<>();

    private Paint mGridLinePaint = new Paint(Paint.ANTI_ALIAS_FLAG);
    private Paint mLinePaint = new Paint(Paint.ANTI_ALIAS_FLAG);
    private Paint mLeftLinePaint = new Paint(Paint.ANTI_ALIAS_FLAG);
    private Paint mTextPaint = new Paint(Paint.ANTI_ALIAS_FLAG); //文字

    private Paint mFootLinePaint = new Paint(Paint.ANTI_ALIAS_FLAG);

    private int mCentreLineLengh = dp2px(4);  //中轴
    private int mCentreSeaftLengh = dp2px(2);  //中轴
    private int mLeftPadding = dp2px(20);  //左padding
    private int mReadLengh = dp2px(30);
    private int mGrayLengh = dp2px(20);
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
        mTextPaint.setTextSize(sp2px(16));

    }

    public void initData(Collection<? extends IFootball> homeDatas, Collection<? extends IFootball> AwayDatas) {
        mHomePoints.clear();
        mAwayPoints.clear();
        if (homeDatas != null) {
            this.mHomePoints.addAll(homeDatas);
            mHomePointCount = mHomePoints.size();

            this.mAwayPoints.addAll(homeDatas);
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

        mScaleX = (mWidth - mLeftPadding - mCentreSeaftLengh) / mTotalTimers;

    }

    @Override
    protected void calculateSelectedX(float x) {
        selectedIndex = (int) (x * 1f / getX(mHomePoints.size() - 1) * (mHomePoints.size() - 1) + 0.5f);
        if (selectedIndex < 0) {
            selectedIndex = 0;
        }
        if (selectedIndex > mHomePoints.size() - 1) {
            selectedIndex = mHomePoints.size() - 1;
        }
    }


    @Override
    protected void onDraw(Canvas canvas) {
        super.onDraw(canvas);
        //绘制网格
        drawGird(canvas);
        if (mHomePoints == null || mAwayPoints == null) {
            return;
        }

        drawText(canvas);
        drawView(canvas);


    }


    //绘制网格线
    private void drawGird(Canvas canvas) {
        //上
        canvas.drawLine(0, mTopPadding, mWidth, mTopPadding, mGridLinePaint);

        //中
        canvas.drawRect(new RectF(0, (mBaseHeight - mCentreLineLengh) / 2 + mTopPadding, mWidth,
                (mBaseHeight + mCentreLineLengh) / 2 + mTopPadding), mLeftLinePaint);

        //下
        canvas.drawLine(0, mTopPadding + mBaseHeight, mWidth,
                mTopPadding + mBaseHeight, mGridLinePaint);

        //左边
        canvas.drawRect(new RectF(0, mTopPadding, mLeftPadding,
                mTopPadding + mBaseHeight), mLeftLinePaint);

        //上下半场分界线
        canvas.drawRect(new RectF((mBaseWidth + mLeftPadding - mCentreSeaftLengh) / 2, mTopPadding,
                (mBaseWidth + mLeftPadding + mCentreSeaftLengh) / 2, mTopPadding + mBaseHeight), mLeftLinePaint);

    }


    private void drawText(Canvas canvas) {
        //绘制名字
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
            switch (mHomePoints.get(i).getHomeSequenceStaus()) {
                case 1: {
                    drawLine(getX(i) + mLeftPadding, 1, canvas, mHomePoints.get(i).getHomeEventType());
                    break;
                }
                case 2: {
                    drawLine(getX(i) + mLeftPadding + mCentreSeaftLengh, 1, canvas, mHomePoints.get(i).getHomeEventType());
                    break;
                }
                default:
                    break;
            }
        }


        //绘制主队进攻
        for (int i = 0; i < mAwayPoints.size(); i++) {
            switch (mAwayPoints.get(i).getHomeSequenceStaus()) {
                case 1: {
                    drawLine(getX(i) + mLeftPadding, 2, canvas, mAwayPoints.get(i).getHomeEventType());
                    break;
                }
                case 2: {
                    drawLine(getX(i) + mLeftPadding + mCentreSeaftLengh, 2, canvas, mAwayPoints.get(i).getHomeEventType());
                    break;
                }
                default:
                    break;
            }
        }


    }


    /**
     * 根据索引获取x的值
     */
    private float getX(int position) {
        mCount = 0;
        int dateTime = mHomePoints.get(position).getHomeDate();
        int startTime = mHomePoints.get(0).getHomeDate();
        int endTime = mHomePoints.get(mHomePoints.size() - 1).getHomeDate();
        if (dateTime >= startTime && dateTime <= endTime) {
            mCount = position;
        } else {
            mCount = 0;
        }
        float c = mCount * mScaleX;
        return mCount * mScaleX;
    }


    //绘制柱体
    private void drawLine(float x, float state, Canvas canvas, int eventType) {
        switch (eventType) {
            case 1: //入球
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
                if (state == 1) {
                    canvas.drawLine(x + mScaleX / 2,
                            (mBaseHeight - mCentreLineLengh) / 2 + mTopPadding,
                            x + mScaleX / 2,
                            (mBaseHeight - mCentreLineLengh) / 2 + mTopPadding - mReadLengh,
                            mFootLinePaint);
                } else if (state == 2) {
                    canvas.drawLine(x + mScaleX / 2,
                            (mBaseHeight + mCentreLineLengh) / 2 + mTopPadding,
                            x + mScaleX / 2,
                            (mBaseHeight + mCentreLineLengh) / 2 + mTopPadding + mReadLengh,
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
                if (state == 1) {
                    canvas.drawLine(x + mScaleX / 2,
                            (mBaseHeight - mCentreLineLengh) / 2 + mTopPadding,
                            x + mScaleX / 2,
                            (mBaseHeight - mCentreLineLengh) / 2 + mTopPadding - mGrayLengh,
                            mFootLinePaint);
                } else if (state == 2) {
                    canvas.drawLine(x + mScaleX / 2,
                            (mBaseHeight + mCentreLineLengh) / 2 + mTopPadding,
                            x + mScaleX / 2,
                            (mBaseHeight + mCentreLineLengh) / 2 + mTopPadding+ mGrayLengh,
                            mFootLinePaint);
                }
                break;

            case 11://换人
                break;

            case 12://角球
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


}





















