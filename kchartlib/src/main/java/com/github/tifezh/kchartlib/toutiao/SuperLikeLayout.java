package com.github.tifezh.kchartlib.toutiao;

import android.content.Context;
import android.content.res.TypedArray;
import android.graphics.Canvas;
import android.graphics.Paint;
import android.os.CountDownTimer;
import android.os.Handler;
import android.os.Message;
import android.support.annotation.NonNull;
import android.support.annotation.Nullable;
import android.util.AttributeSet;
import android.util.Log;
import android.view.View;

import com.github.tifezh.kchartlib.R;

import java.lang.ref.WeakReference;
import java.util.List;

/**
 * @author puyantao
 * @description
 * @date 2020/7/29 16:21
 */

public class SuperLikeLayout extends View implements AnimationEndListener {
    private long totalTime = 60 * 1000;
    private static final String TAG = "SuperLikeLayout";
    /**
     * 移动时间间隔
     */
    private static final long INTERVAL = 40;
    /**
     * 默认默认缓存数组的个数
     */
    private static final int MAX_FRAME_SIZE = 16;
    /**
     * 默认图片个数
     */
    private static final int ERUPTION_ELEMENT_AMOUNT = 4;
    private AnimationFramePool animationFramePool;

    private AnimationHandler animationHandler;
    private BitmapProvider.Provider provider;
    /**
     * 是否显示喷射图标
     */
    private boolean hasEruptionAnimation;
    /**
     * 是否显示文字
     */
    private boolean hasTextAnimation;
    private View mView;
    private boolean isLongClick;

    private CountDownTimer countDownTimer = new CountDownTimer(totalTime, 200) {
        @Override
        public void onTick(long millisUntilFinished) {
            //执行任务
            launch(mView);
        }

        @Override
        public void onFinish() {
            if (countDownTimer != null) {
                countDownTimer.start();
            }
        }
    };

    public SuperLikeLayout(@NonNull Context context) {
        this(context, null);
    }

    public SuperLikeLayout(@NonNull Context context, @Nullable AttributeSet attrs) {
        this(context, attrs, 0);
    }

    public SuperLikeLayout(@NonNull Context context, @Nullable AttributeSet attrs, int defStyleAttr) {
        super(context, attrs, defStyleAttr);
        init(context, attrs, defStyleAttr);
    }

    private void init(@NonNull Context context, @Nullable AttributeSet attrs, int defStyleAttr) {
        animationHandler = new AnimationHandler(this);
        TypedArray a = context.obtainStyledAttributes(attrs, R.styleable.SuperLikeLayout, defStyleAttr, 0);
        //设置图片个数
        int elementAmount = a.getInteger(R.styleable.SuperLikeLayout_eruption_element_amount, ERUPTION_ELEMENT_AMOUNT);
        //默认缓存数组的个数
        int maxFrameSize = a.getInteger(R.styleable.SuperLikeLayout_max_eruption_total, MAX_FRAME_SIZE);
        //是否显示喷射图标
        hasEruptionAnimation = a.getBoolean(R.styleable.SuperLikeLayout_show_emoji, true);
        //是否显示文字
        hasTextAnimation = a.getBoolean(R.styleable.SuperLikeLayout_show_text, true);
        a.recycle();

        animationFramePool = new AnimationFramePool(maxFrameSize, elementAmount);
    }

    @Override
    public void draw(Canvas canvas) {
        super.draw(canvas);
        if (!animationFramePool.hasRunningAnimation()) {
            return;
        }
        //遍历所有AnimationFrame 并绘制Element
        // note: 需要倒序遍历 nextFrame方法可能会改变runningFrameList Size 导致异常
        List<AnimationFrame> runningFrameList = animationFramePool.getRunningFrameList();
        for (int i = runningFrameList.size() - 1; i >= 0; i--) {
            AnimationFrame animationFrame = runningFrameList.get(i);
            List<Element> elementList = animationFrame.nextFrame(INTERVAL);
            for (Element element : elementList) {
                //绘制图片
                if (animationFrame.getType() == 1) {
                    Paint paint = new Paint();
                    //设置透明程度
                    paint.setAlpha(element.getAlpha());
                    canvas.drawBitmap(element.getBitmap(), element.getX(), element.getY(), paint);
                } else {
                    canvas.drawBitmap(element.getBitmap(), element.getX(), element.getY(), null);
                }
            }
        }

    }

    /**
     * 启动动画
     *
     * @param v 单次点击试图
     */
    private void launch(View v) {
        int width = v.getWidth();
        int height = v.getHeight();
        int x = (int) (v.getX() + width / 2);
        int y = (int) (v.getY() + height / 2);

        if (!hasEruptionAnimation && !hasTextAnimation) {
            return;
        }
        // 喷射动画
        if (hasEruptionAnimation) {
            AnimationFrame eruptionAnimationFrame = animationFramePool.obtain(EruptionAnimationFrame.TYPE);
            if (eruptionAnimationFrame != null && !eruptionAnimationFrame.isRunning()) {
                eruptionAnimationFrame.setAnimationEndListener(this);
                eruptionAnimationFrame.prepare(width, height, x, y, getProvider());
            }
        }
        // combo动画
        if (hasTextAnimation) {
            AnimationFrame textAnimationFrame = animationFramePool.obtain(TextAnimationFrame.TYPE);
            if (textAnimationFrame != null) {
                textAnimationFrame.setLongClick(isLongClick);
                textAnimationFrame.setAnimationEndListener(this);
                textAnimationFrame.prepare(width, height, x, y, getProvider());
            }
        }
        animationHandler.removeMessages(AnimationHandler.MESSAGE_CODE_REFRESH_ANIMATION);
        animationHandler.sendEmptyMessageDelayed(AnimationHandler.MESSAGE_CODE_REFRESH_ANIMATION, INTERVAL);

    }

    /**
     * 长按试图
     *
     * @param view
     */
    public void longClickView(View view) {
        this.mView = view;
        stop();
        isLongClick = true;
        //主线程中调用：
        countDownTimer.start();
    }

    /**
     * 单点试图
     *
     * @param view
     */
    public void clickView(View view) {
        this.mView = view;
        isLongClick = false;
        launch(view);
    }


    /**
     * 结束计时
     */
    public void stop() {
        countDownTimer.cancel();
    }

    public boolean hasAnimation() {
        return animationFramePool.hasRunningAnimation();
    }

    /**
     * 设置数据对象
     *
     * @param provider
     */
    public void setProvider(BitmapProvider.Provider provider) {
        this.provider = provider;
    }

    public BitmapProvider.Provider getProvider() {
        if (provider == null) {
            provider = new BitmapProvider.Builder(getContext())
                    .build();
        }
        return provider;
    }


    /**
     * 回收SurpriseView  添加至空闲队列方便下次使用
     */
    private void onRecycle(AnimationFrame animationFrame) {
        Log.v(TAG, "=== AnimationFrame recycle ===");
        animationFrame.reset();
        animationFramePool.recycle(animationFrame);
    }

    @Override
    protected void onDetachedFromWindow() {
        super.onDetachedFromWindow();
        if (!hasAnimation()) {
            return;
        }
        // 回收所有动画 并暂停动画
        animationFramePool.recycleAll();

        animationHandler.removeMessages(AnimationHandler.MESSAGE_CODE_REFRESH_ANIMATION);
    }

    /**
     * 动画结束
     *
     * @param animationFrame
     */
    @Override
    public void onAnimationEnd(AnimationFrame animationFrame) {
        onRecycle(animationFrame);
    }


    private static final class AnimationHandler extends Handler {
        public static final int MESSAGE_CODE_REFRESH_ANIMATION = 1001;
        private WeakReference<SuperLikeLayout> weakReference;

        public AnimationHandler(SuperLikeLayout superLikeLayout) {
            weakReference = new WeakReference<>(superLikeLayout);
        }

        @Override
        public void handleMessage(Message msg) {
            super.handleMessage(msg);
            if (msg.what == MESSAGE_CODE_REFRESH_ANIMATION && weakReference != null && weakReference.get() != null) {
                weakReference.get().invalidate();
                // 动画还未结束继续刷新
                if (weakReference.get().hasAnimation()) {
                    sendEmptyMessageDelayed(MESSAGE_CODE_REFRESH_ANIMATION, INTERVAL);
                }
            }

        }
    }

}
