package com.github.tifezh.kchartlib.toutiao;

import java.util.List;

/**
 * @author puyantao
 * @description
 * @date 2020/7/29 16:21
 */
public interface AnimationFrame {

    /**
     * 获取动画类型
     *
     * @return
     */
    int getType();

    /**
     * 是否运行
     *
     * @return
     */
    boolean isRunning();

    /**
     * 获取节点
     *
     * @param interval
     * @return
     */
    List<Element> nextFrame(long interval);

    /**
     * 是否可以复用
     * @return
     */
    boolean onlyOne();

    /**
     * 是否长按
     * @param b
     */
    void setLongClick(boolean b);

    /**
     * 动画结束监听器
     *
     * @param animationEndListener
     */
    void setAnimationEndListener(AnimationEndListener animationEndListener);

    /**
     * 重置动画
     */
    void reset();

    /**
     * 准备数据集合
     *
     * @param width          点击试图宽
     * @param height         点击试图 高
     * @param x              点击试图 x
     * @param y              点击试图 y
     * @param bitmapProvider 数据集合
     */
    void prepare(int width, int height, int x, int y, BitmapProvider.Provider bitmapProvider);

}
