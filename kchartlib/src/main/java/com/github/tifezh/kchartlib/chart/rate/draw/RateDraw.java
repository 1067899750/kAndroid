package com.github.tifezh.kchartlib.chart.rate.draw;

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
import com.github.tifezh.kchartlib.chart.base.IValueFormatter;
import com.github.tifezh.kchartlib.chart.comInterface.ICandle;
import com.github.tifezh.kchartlib.chart.rate.BaseRateView;
import com.github.tifezh.kchartlib.chart.rate.base.IRate;
import com.github.tifezh.kchartlib.chart.rate.base.IRateDraw;
import com.github.tifezh.kchartlib.utils.DateUtil;
import com.github.tifezh.kchartlib.utils.DensityUtil;
import com.github.tifezh.kchartlib.utils.DisplayUtil;
import com.github.tifezh.kchartlib.utils.StrUtil;

import java.util.ArrayList;
import java.util.List;

import static com.github.tifezh.kchartlib.utils.DensityUtil.dp2px;

public class RateDraw implements IRateDraw<IRate> {
    private Context mContext;

    private Paint mLinePaint = new Paint(Paint.ANTI_ALIAS_FLAG);
    private Paint mTextPaint = new Paint(Paint.ANTI_ALIAS_FLAG);
    private Paint mTextRactPaint = new Paint(Paint.ANTI_ALIAS_FLAG);
    private Paint mSelectorBackgroundPaint = new Paint(Paint.ANTI_ALIAS_FLAG);
    private Paint mSelectorTextPaint = new Paint(Paint.ANTI_ALIAS_FLAG);

    private int screenWidth=0;
    private int screenHeight=0;

    private float mTextSize = 10;

    public RateDraw(BaseRateView view) {
        mContext= view.getContext();

        WindowManager wm = (WindowManager) mContext.getSystemService(Context.WINDOW_SERVICE);
        DisplayMetrics dm = new DisplayMetrics();
        wm.getDefaultDisplay().getMetrics(dm);
        screenWidth = dm.widthPixels;
        screenHeight=dm.heightPixels;

        mLinePaint.setColor(mContext.getResources().getColor(R.color.cffffff));

        mTextPaint.setColor(mContext.getResources().getColor(R.color.cffffff));
        mTextPaint.setTextSize(DisplayUtil.sp2px(mContext, 11));


        mTextRactPaint.setColor(Color.parseColor("#6A798E"));
        mTextRactPaint.setTextSize(sp2px(mTextSize));
        mTextRactPaint.setStrokeWidth(dp2px(0.5f));

        mSelectorTextPaint.setColor(Color.parseColor("#E7EDF5"));
        mSelectorTextPaint.setTextSize(sp2px(13));

        mSelectorBackgroundPaint.setColor(mContext.getResources().getColor(R.color.c4F5490));
    }




    @Override
    public void drawTranslated(@Nullable IRate lastPoint, @NonNull IRate curPoint, float lastX,
                               float curX, @NonNull Canvas canvas, @NonNull BaseRateView view, int position) {
        view.drawMainLine(canvas, mLinePaint, lastX, lastPoint.getValue(), curX, curPoint.getValue());

    }

    @Override
    public void drawText(@NonNull Canvas canvas, @NonNull BaseRateView view, int position, float x, float y) {
        Paint.FontMetrics metrics = mTextRactPaint.getFontMetrics();
        float textHeight = metrics.descent - metrics.ascent;

        IRate point = view.getItem(position);

        float padding = dp2px(5);
        float margin = dp2px(5);
        float width = 0;
        float left = 5;
        float top = dp2px(10);
        float bottom = 10;

        List<String> strings = new ArrayList<>();
        strings.add(DateUtil.getStringDateByLong(point.getDate().getTime(), 2));
        strings.add("数值");
        strings.add(point.getValue() + "");

        strings.add("涨跌");
        strings.add(point.getChange());
        strings.add(point.getValue()+ "");

        width = sp2px(78);

        float x1 = view.translateXtoX(view.getX(position));
        if (x1 > view.getChartWidth() / 2) {
            left = margin;
        } else {
            left = view.getChartWidth() - width - margin;
        }
        float height = top + padding * 2 + (textHeight - metrics.bottom - metrics.top) / 2 +
                (textHeight + padding) * (strings.size() - 1);
        RectF r = new RectF(left, top, left + width, top + height + bottom);
        canvas.drawRoundRect(r, padding, padding, mSelectorBackgroundPaint);

        float h = top + padding * 2 + (textHeight - metrics.bottom - metrics.top) / 2;
        for (String s : strings) {
            if (StrUtil.isTimeText(s)) {
                mSelectorTextPaint.setTextSize(sp2px(12));
                mSelectorTextPaint.setColor(mContext.getResources().getColor(R.color.color_text_positive_paint));
                canvas.drawText(s, left + padding, h, mSelectorTextPaint);

            } else if (StrUtil.isChinaText(s)) {
                mTextRactPaint.setTextSize(sp2px(10));
                canvas.drawText(s, left + padding, h, mTextRactPaint);

            } else {
                mSelectorTextPaint.setTextSize(sp2px(13));
                if (StrUtil.isPositiveOrNagativeNumberText(s)) {
                    mSelectorTextPaint.setColor(mContext.getResources().getColor(R.color.color_negative_value));
                    canvas.drawText(s, left + padding, h, mSelectorTextPaint);
                } else {
                    mSelectorTextPaint.setColor(mContext.getResources().getColor(R.color.color_text_positive_paint));
                    canvas.drawText(s, left + padding, h, mSelectorTextPaint);
                }
            }

            h += textHeight + padding;
        }

    }

    @Override
    public float getValue(IRate point) {
        return point.getValue();
    }


    @Override
    public IValueFormatter getValueFormatter() {
        return null;
    }

    @Override
    public void setTargetColor(int... color) {

    }

    protected int dp2px(float dp) {
        final float scale = mContext.getResources().getDisplayMetrics().density;
        return (int) (dp * scale + 0.5f);
    }

    protected int sp2px(float spValue) {
        final float fontScale = mContext.getResources().getDisplayMetrics().scaledDensity;
        return (int) (spValue * fontScale + 0.5f);
    }


}
