package com.github.tifezh.kchart.view;

import android.content.Context;
import android.graphics.drawable.Drawable;
import android.os.CountDownTimer;
import android.os.Handler;
import android.os.Looper;
import android.os.Message;
import android.util.AttributeSet;
import android.view.LayoutInflater;
import android.widget.LinearLayout;
import android.widget.TextView;

import com.github.tifezh.kchart.R;
import com.github.tifezh.kchartlib.utils.DensityUtil;

import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.Date;
import java.util.Locale;

/**
 * @author puyantao
 * @describe 剩余倒计时器
 * @create 2020/7/9 16:38
 */
public class TimerTextView extends LinearLayout {
    private long totalTime = 24 * 60 * 60 * 60;
    /**
     * 时间变量 ms
     */
    private long second;
    private TextView mTimerTv;
    private RefreshCallBack refreshCallBack;
    /**
     * 是否启动
     */
    private boolean isRun = true;
    private long mDayCount;
    private long mHourCount;
    private long mMinuteCount;
    private long mSecondCount;

    CountDownTimer countDownTimer = new CountDownTimer(totalTime, 1000) {
        @Override
        public void onTick(long millisUntilFinished) {
            //执行任务
            computerTimer(second - 1000);
        }

        @Override
        public void onFinish() {
            if (countDownTimer != null) {
                countDownTimer.start();
            }
        }
    };


    public TimerTextView(Context context) {
        super(context);
        initView(context);
    }


    public TimerTextView(Context context, AttributeSet attrs) {
        super(context, attrs);
        initView(context);
    }

    public TimerTextView(Context context, AttributeSet attrs, int defStyleAttr) {
        super(context, attrs, defStyleAttr);
        initView(context);
    }


    private void initView(Context context) {
        // 加载布局
        LayoutInflater.from(context).inflate(R.layout.timer_text_view, this);
        mTimerTv = findViewById(R.id.current_time_tv);
    }

    /**
     * 是否运行
     * @return
     */
    public boolean isRun() {
        return isRun;
    }

    /**
     * 开始计时
     */
    public void start() {
        //主线程中调用：
        countDownTimer.start();
        isRun = true;
    }

    /**
     * 结束计时
     */
    public void stop() {
        countDownTimer.cancel();
        isRun = false;
    }

    /**
     * 设置字体大小
     * @param size
     */
    public void setTimerSize(int size){
        mTimerTv.setTextSize(size);
    }

    /**
     * 设置字体颜色
     * @param color
     */
    public void setTimerColor(int color){
        mTimerTv.setTextColor(color);
    }

    /**
     * 设置背景
     * @param color
     */
    public void setTimerBackgroundColor(int color){
        mTimerTv.setBackgroundColor(color);
    }

    /**
     * 设置背景
     * @param drawable
     */
    public void setTimerBackground(Drawable drawable){
        mTimerTv.setBackground(drawable);
    }

    /**
     * 设置数据
     * @param startTime
     * @param endTime
     */
    public void diffTime(String startTime, String endTime) {
        SimpleDateFormat sd = new SimpleDateFormat( "yyyy-MM-dd hh:mm:ss");
        long diff = 0;
        try {
            diff = sd.parse(endTime).getTime() - sd.parse(startTime).getTime();
        } catch (ParseException e) {
            e.printStackTrace();
        }
        if (diff < 0) {
            if (null != refreshCallBack) {
                refreshCallBack.showCallBack(false);
            }
            return;
        } else {
            if (null != refreshCallBack) {
                refreshCallBack.showCallBack(true);
            }
            computerTimer(diff);
        }
    }

    /**
     * 计算时间
     *
     * @param timer
     */
    public void computerTimer(long timer) {
        second = timer;
        //一天的毫秒数
        long nd = 1000 * 24 * 60 * 60;
        //一小时的毫秒数
        long nh = 1000 * 60 * 60;
        //一分钟的毫秒数
        long nm = 1000 * 60;
        //一秒钟的毫秒数long diff
        long ns = 1000;
        //获得两个时间的毫秒时间差异
        mDayCount = second / nd;
        long hour = second % nd;
        mHourCount = hour / nh;
        long minute = hour % nh;
        mMinuteCount = minute / nm;
        long second = minute % nm;
        mSecondCount = second / ns;
        mTimerTv.setText(mDayCount + "天" + mHourCount + "小时" + mMinuteCount + "分" + mSecondCount + "秒");
        if (mDayCount == 0 && mHourCount == 0 && mMinuteCount == 0 && mSecondCount == 0) {
            if (null != refreshCallBack) {
                refreshCallBack.showCallBack(false);
            }
        }
    }

    public void setRefreshCallBack(RefreshCallBack refreshCallBack) {
        this.refreshCallBack = refreshCallBack;
    }

    public interface RefreshCallBack {
        /**
         * 结束回调
         *
         * @param flag
         */
        public void showCallBack(boolean flag);
    }


}
