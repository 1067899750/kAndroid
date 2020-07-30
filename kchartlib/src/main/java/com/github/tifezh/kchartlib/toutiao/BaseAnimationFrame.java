package com.github.tifezh.kchartlib.toutiao;

import java.util.List;

/**
 * @author puyantao
 * @description
 * @date 2020/7/29 16:21
 */
public abstract class BaseAnimationFrame implements AnimationFrame {
    /**
     * 起始点
     */
    private int x;
    private int y;
    /**
     * 点击试图的宽、高
     */
    private int width;
    private int height;
    private double currentTime;
    private boolean isRunnable;
    List<Element> elements;
    private AnimationEndListener animationEndListener;
    long duration;

    public BaseAnimationFrame(long duration) {
        this.duration = duration;
    }

    @Override
    public boolean isRunning() {
        return isRunnable;
    }

    @Override
    public List<Element> nextFrame(long interval) {
        currentTime += interval;
        if (currentTime >= duration) {
            isRunnable = false;
            if (animationEndListener != null) {
                animationEndListener.onAnimationEnd(this);
            }
        } else {
            for (Element element : elements) {
                element.evaluate(width, height, x, y, currentTime);
            }
        }
        return elements;
    }

    @Override
    public void setAnimationEndListener(AnimationEndListener animationEndListener) {
        this.animationEndListener = animationEndListener;
    }

    @Override
    public void reset() {
        currentTime = 0;
        if (elements != null) {
            elements.clear();
        }
    }

    /**
     * 生成图标对象
     *
     * @param width
     * @param height
     * @param x
     * @param y
     * @param bitmapProvider
     * @return
     */
    protected abstract List<Element> generatedElements(int width, int height, int x, int y, BitmapProvider.Provider bitmapProvider);

    /**
     * 设置起始点
     *
     * @param x
     * @param y
     */
    protected void setStartPoint(int width, int height, int x, int y) {
        this.width = width;
        this.height = height;
        this.x = x;
        this.y = y;
    }

    @Override
    public boolean onlyOne() {
        return false;
    }

    @Override
    public void setLongClick(boolean b) {
    }


}











