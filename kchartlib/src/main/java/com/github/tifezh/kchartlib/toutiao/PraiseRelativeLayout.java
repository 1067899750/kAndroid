package com.github.tifezh.kchartlib.toutiao;

import android.content.Context;
import android.os.Handler;
import android.os.Message;
import android.util.AttributeSet;
import android.view.MotionEvent;
import android.view.View;
import android.widget.RelativeLayout;

import java.lang.ref.WeakReference;

/**
 * @author puyantao
 * @describe 点赞布局试图
 * @create 2020/7/30 10:30
 */
public class PraiseRelativeLayout extends RelativeLayout {
    /**
     * 延时 1s,用来判断是否为长按
     */
    private long duration = 1000;
    private long lastClickTime;
    private OnPraiseClickListener mOnPraiseClickListener;
    /**
     * 手指落下是的坐标
     */
    private float mLastMotionX;
    private float mLastMotionY;
    private ClickHandler mClickHandler;
    public static final int LONG_START_CLICK = 1001;
    /**
     * 手指移动最大距离
     */
    public static final int MOVE_MAX_SIZE = 50;

    public PraiseRelativeLayout(Context context) {
        this(context, null);
    }

    public PraiseRelativeLayout(Context context, AttributeSet attrs) {
        this(context, attrs, 0);
    }

    public PraiseRelativeLayout(Context context, AttributeSet attrs, int defStyleAttr) {
        super(context, attrs, defStyleAttr);
        mClickHandler = new ClickHandler(this);
    }


    @Override
    public boolean onInterceptTouchEvent(MotionEvent ev) {
        return true;
    }


    @Override
    public boolean onTouchEvent(MotionEvent event) {
        switch (event.getAction()) {
            case MotionEvent.ACTION_DOWN:
                lastClickTime = System.currentTimeMillis();
                mLastMotionX = event.getRawX();
                mLastMotionY = event.getRawY();
                mClickHandler.sendEmptyMessageDelayed(PraiseRelativeLayout.LONG_START_CLICK, duration);
                break;
            case MotionEvent.ACTION_MOVE:
                //划出距离是取消动画
                if (Math.abs(mLastMotionX - event.getRawX()) > MOVE_MAX_SIZE
                        || Math.abs(mLastMotionY - event.getRawY()) > MOVE_MAX_SIZE) {
                    mClickHandler.removeMessages(PraiseRelativeLayout.LONG_START_CLICK);
                    if (mOnPraiseClickListener != null) {
                        mOnPraiseClickListener.onCancelClick(this);
                    }
                }
                break;
            case MotionEvent.ACTION_CANCEL:
            case MotionEvent.ACTION_UP:
                mClickHandler.removeMessages(PraiseRelativeLayout.LONG_START_CLICK);
                if (System.currentTimeMillis() - lastClickTime < duration) {
                    if (mOnPraiseClickListener != null) {
                        mOnPraiseClickListener.onClick(this);
                    }
                } else {
                    if (mOnPraiseClickListener != null) {
                        mOnPraiseClickListener.onCancelClick(this);
                    }
                }
                break;
        }
        return true;
    }


    public void setOnPraiseClickListener(OnPraiseClickListener listener) {
        this.mOnPraiseClickListener = listener;
    }


    /**
     * 点击监听器
     */
    public interface OnPraiseClickListener {
        /**
         * 点击
         */
        void onClick(View v);

        /**
         * 长按
         */
        void onLongClick(View v);

        /**
         * 取消点击
         */
        void onCancelClick(View v);
    }


    private static final class ClickHandler extends Handler {
        private WeakReference<PraiseRelativeLayout> weakReference;

        public ClickHandler(PraiseRelativeLayout praiseRelativeLayout) {
            weakReference = new WeakReference<>(praiseRelativeLayout);
        }

        @Override
        public void handleMessage(Message msg) {
            super.handleMessage(msg);
            if (msg.what == PraiseRelativeLayout.LONG_START_CLICK) {
                if (weakReference.get().mOnPraiseClickListener != null) {
                    weakReference.get().mOnPraiseClickListener.onLongClick(weakReference.get());
                }
            }
        }
    }


}









