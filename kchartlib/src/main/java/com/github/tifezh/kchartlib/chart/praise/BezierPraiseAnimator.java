package com.github.tifezh.kchartlib.chart.praise;

import android.animation.Animator;
import android.animation.AnimatorListenerAdapter;
import android.animation.AnimatorSet;
import android.animation.ObjectAnimator;
import android.animation.ValueAnimator;
import android.app.Activity;
import android.content.Context;
import android.graphics.Bitmap;
import android.graphics.PointF;
import android.graphics.drawable.Drawable;
import android.os.CountDownTimer;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.view.animation.AccelerateDecelerateInterpolator;
import android.view.animation.AlphaAnimation;
import android.view.animation.Animation;
import android.view.animation.AnticipateInterpolator;
import android.widget.ImageView;
import android.widget.TextView;

import com.github.tifezh.kchartlib.R;
import com.github.tifezh.kchartlib.toutiao.BitmapProvider;
import com.github.tifezh.kchartlib.toutiao.Element;

import java.util.ArrayList;
import java.util.List;
import java.util.Random;

/**
 * @author puyantao
 * @describe 贝塞尔点赞动画
 * @create 2020/5/25 9:58
 */
public class BezierPraiseAnimator {
    private Context mContext;
    private long totalTime = 60 * 1000;
    private BitmapProvider.Provider provider;
    /**
     * 点击试图
     */
    private View mPraiseView;
    /**
     * 控件树的顶层视图
     */
    private ViewGroup mRootView;
    /**
     * 随机数
     */
    private Random mRandom;
    /**
     * 动画集合
     */
    private ArrayList<Animator> mAnimatorList;
    /**
     * 目标控件在窗口中的X坐标
     */
    private int mTargetX;
    /**
     * 目标控件在窗口中的Y坐标
     */
    private int mTargetY;
    /**
     * 点赞小图标的宽高
     */
    private int mPraiseIconWidth = 70;
    /**
     * 是否长按
     */
    private boolean isLongClick;
    /**
     * 连续点击次数
     */
    private int likeCount;
    private long lastClickTimeMillis;
    private int mPraiseIconHeight = 70;
    private int mAnimatorDuration = 800;
    private int mMarketDuration = 500;
    /**
     * 喷射图片的个数
     */
    private int mRandomBitmapSize = 5;
    private View mNumberViewGroup;
    private ImageView numberOne;
    private ImageView numberTwo;
    private ImageView numberThree;
    private ImageView numberFour;

    private CountDownTimer countDownTimer = new CountDownTimer(totalTime, 200) {
        @Override
        public void onTick(long millisUntilFinished) {
            //执行任务
            startAnimation(mPraiseView);
        }

        @Override
        public void onFinish() {
            if (countDownTimer != null) {
                countDownTimer.start();
            }
        }
    };

    public BezierPraiseAnimator(Context context) {
        mContext = context;
        if (context instanceof Activity) {
            mRootView = (ViewGroup) ((Activity) context).getWindow().getDecorView();
        } else {
            throw new RuntimeException("context is not instanceof for Activity");
        }
        initData();
    }


    public BezierPraiseAnimator(ViewGroup rootView) {
        mContext = rootView.getContext();
        mRootView = rootView;
        initData();
    }

    private void initData() {
        mRandom = new Random();
        mAnimatorList = new ArrayList<>();
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
            provider = new BitmapProvider.Builder(mContext)
                    .build();
        }
        return provider;
    }


    /**
     * 获取当前targetView在屏幕中的位置
     *
     * @param targetView
     */
    public void startAnimation(View targetView) {
        // 获取targetView在屏幕中的位置
        int loc[] = new int[2];
        targetView.getLocationInWindow(loc);
        int viewHeight = targetView.getHeight();
        int viewWidth = targetView.getWidth();
        // 设置起始坐标，中点坐标
        mTargetX = loc[0] + viewWidth / 2 - mPraiseIconWidth / 2;
        mTargetY = loc[1] + viewHeight / 2 - mPraiseIconHeight / 2;
        // 播放点赞动画
        startPraiseAnimation();

        if (mNumberViewGroup == null) {
            mNumberViewGroup = LayoutInflater.from(mContext).inflate(R.layout.number_text_layout, null);
            mNumberViewGroup.setLayoutParams(new ViewGroup.LayoutParams(ViewGroup.LayoutParams.WRAP_CONTENT,
                    ViewGroup.LayoutParams.WRAP_CONTENT));
            numberOne = mNumberViewGroup.findViewById(R.id.number_one);
            numberTwo = mNumberViewGroup.findViewById(R.id.number_two);
            numberThree = mNumberViewGroup.findViewById(R.id.number_three);
            numberFour = mNumberViewGroup.findViewById(R.id.number_four);
            if (mTargetX < mRootView.getWidth() / 2) {
                mNumberViewGroup.setX(loc[0] + viewWidth);
                mNumberViewGroup.setY(loc[1] - viewHeight);
            } else {
                mNumberViewGroup.setX(loc[0] - viewWidth);
                mNumberViewGroup.setY(loc[1] - viewHeight);
            }
            mRootView.addView(mNumberViewGroup);
        }
        mNumberViewGroup.setVisibility(View.VISIBLE);
        //记录点击次数
        calculateCombo();
        addTextView();
    }


    /**
     * 判断数字是重置还是加 1
     */
    private void calculateCombo() {
        //连续点击事件小于 1s 加 1, 否着重置
        if (isLongClick) {
            likeCount++;
        } else {
            if (System.currentTimeMillis() - lastClickTimeMillis < 800) {
                likeCount++;
            } else {
                likeCount = 1;
            }
        }
        lastClickTimeMillis = System.currentTimeMillis();
    }

    /**
     * 添加文字
     */
    private void addTextView() {
        if (likeCount >= 100) {
            numberTwo.setVisibility(View.VISIBLE);
            numberThree.setVisibility(View.VISIBLE);
            numberOne.setImageBitmap(getProvider().getNumberBitmap(likeCount / 100));
            numberTwo.setImageBitmap(getProvider().getNumberBitmap((likeCount % 100) / 10));
            numberThree.setImageBitmap(getProvider().getNumberBitmap((likeCount % 100) % 10));
        } else if (likeCount >= 10) {
            numberTwo.setVisibility(View.VISIBLE);
            numberThree.setVisibility(View.GONE);
            numberOne.setImageBitmap(getProvider().getNumberBitmap(likeCount / 10));
            numberTwo.setImageBitmap(getProvider().getNumberBitmap(likeCount % 10));
        } else {
            numberTwo.setVisibility(View.GONE);
            numberThree.setVisibility(View.GONE);
            numberOne.setImageBitmap(getProvider().getNumberBitmap(likeCount));
        }

        //判断是 鼓励、加油、太棒了
        int level = 0;
        if (isLongClick) {
            //长按情况
            if (likeCount < 21) {
                level = 0;
            } else if (likeCount < 46) {
                level = 1;
            } else {
                level = 2;
            }
        } else {
            //单点情况
            level = likeCount / 10;
            if (level > 2) {
                level = 2;
            }
        }
        numberFour.setImageBitmap(getProvider().getLevelBitmap(level));
    }


    /**
     * 屏幕坐标
     *
     * @param screenX
     * @param screenY
     */
    public void startAnimation(int screenX, int screenY) {
        mTargetX = screenX;
        mTargetY = screenY;
        startPraiseAnimation();
    }

    /**
     * 取消动画
     */
    public void cancelAnimation() {
        if (mAnimatorList == null || mAnimatorList.isEmpty()) {
            return;
        }
        for (Animator animator : mAnimatorList) {
            if (animator.isRunning()) {
                animator.cancel();
            }
        }
    }

    /**
     * 播放点赞动画
     */
    private void startPraiseAnimation() {
        mAnimatorList.clear();
        for (int i = 0; i < mRandomBitmapSize; i++) {
            // 动态添加点赞小图标
            final ImageView praiseIv = new ImageView(mContext);
            praiseIv.setImageBitmap(getProvider().getRandomBitmap());
            praiseIv.setLayoutParams(new ViewGroup.LayoutParams(mPraiseIconWidth, mPraiseIconHeight));
            mRootView.addView(praiseIv);
            // 设置点赞小图标的动画并播放
            Animator praiseAnimator = getPraiseAnimator(praiseIv);
            praiseAnimator.addListener(new AnimatorListenerAdapter() {
                @Override
                public void onAnimationEnd(Animator animation) {
                    super.onAnimationEnd(animation);
                    removePraiseView(praiseIv);
                }


                @Override
                public void onAnimationCancel(Animator animation) {
                    super.onAnimationCancel(animation);
                    removePraiseView(praiseIv);
                }
            });
            praiseAnimator.start();
            mAnimatorList.add(praiseAnimator);
        }
    }

    private void removePraiseView(View praiseView) {
        try {
            mRootView.removeView(praiseView);
        } catch (Exception e) {
            e.printStackTrace();
        }
    }

    /**
     * 获取到点赞小图标动画
     *
     * @param target
     * @return
     */
    private Animator getPraiseAnimator(View target) {
        // 获取贝塞尔曲线动画
        ValueAnimator bezierPraiseAnimator = getBezierPraiseAnimator(target);
        // 组合动画（旋转动画+贝塞尔曲线动画）旋转角度（200~720）
        int rotationAngle = mRandom.nextInt(520) + 200;
        ObjectAnimator rotationAnimator = ObjectAnimator.ofFloat(target, "rotation", 0,
                rotationAngle % 2 == 0 ? rotationAngle : -rotationAngle);
        rotationAnimator.setInterpolator(new AccelerateDecelerateInterpolator());
        rotationAnimator.setTarget(target);

        // 组合动画
        AnimatorSet composeAnimator = new AnimatorSet();
        composeAnimator.play(bezierPraiseAnimator).with(rotationAnimator);
        composeAnimator.setInterpolator(new AccelerateDecelerateInterpolator());
        composeAnimator.setDuration(mAnimatorDuration);
        composeAnimator.setTarget(target);
        return composeAnimator;

    }

    private ValueAnimator getBezierPraiseAnimator(final View target) {
        // 构建贝塞尔曲线的起点，控制点，终点坐标
        float startX = mTargetX;
        float startY = mTargetY;
        int random = mRandom.nextInt(mPraiseIconWidth);
        float endX;
        float endY;
        float controlX;
        final float controlY;

        controlY = startY - mRandom.nextInt(500) - 300;
        // 左右两边
        if (random % 2 == 0) {
            endX = mTargetX - random * 16;
            controlX = mTargetX - random * 8;
        } else {
            endX = mTargetX + random * 16;
            controlX = mTargetX + random * 8;
        }
        endY = mTargetY + random + 400;

        // 构造自定义的贝塞尔估值器
        PraiseEvaluator evaluator = new PraiseEvaluator(new PointF(controlX, controlY));

        ValueAnimator animator = ValueAnimator.ofObject(evaluator, new PointF(startX, startY), new PointF(endX, endY));
        animator.setInterpolator(new AnticipateInterpolator());
        animator.addUpdateListener(new ValueAnimator.AnimatorUpdateListener() {
            @Override
            public void onAnimationUpdate(ValueAnimator animation) {
                PointF currentPoint = (PointF) animation.getAnimatedValue();
                target.setX(currentPoint.x);
                target.setY(currentPoint.y);
                // 设置透明度 [1~0]
                target.setAlpha(1.0f - animation.getAnimatedFraction());
            }
        });
        animator.setTarget(target);
        return animator;
    }

    /**
     * 长按试图
     *
     * @param view
     */
    public void longClickView(View view) {
        this.mPraiseView = view;
        countDownTimer.cancel();
        isLongClick = true;
        //主线程中调用
        countDownTimer.start();
    }

    /**
     * 单点试图
     *
     * @param view
     */
    public void clickView(View view) {
        this.mPraiseView = view;
        isLongClick = false;
        startAnimation(view);
    }


    /**
     * 结束计时
     */
    public void stop() {
        countDownTimer.cancel();
        stopNumAnimation();
    }


    /**
     * 隐藏数字动画
     */
    public void stopNumAnimation() {
        AlphaAnimation alphaAnimation = new AlphaAnimation(1, 0);
        alphaAnimation.setDuration(mMarketDuration);
        alphaAnimation.setAnimationListener(new Animation.AnimationListener() {
            @Override
            public void onAnimationStart(Animation animation) {

            }

            @Override
            public void onAnimationEnd(Animation animation) {
                mNumberViewGroup.setVisibility(View.GONE);
            }

            @Override
            public void onAnimationRepeat(Animation animation) {

            }
        });
        mNumberViewGroup.setAnimation(alphaAnimation);
    }
}
















