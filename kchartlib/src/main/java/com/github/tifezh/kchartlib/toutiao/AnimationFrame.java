package com.github.tifezh.kchartlib.toutiao;

import java.util.List;

/**
 * @author puyantao
 * @description
 * @date 2020/7/29 16:21
 */
public interface AnimationFrame {

    int getType();

    boolean isRunning();

    List<Element> nextFrame(long interval);

    boolean onlyOne();

    void setAnimationEndListener(AnimationEndListener animationEndListener);

    void reset();

    void prepare(int x, int y, BitmapProvider.Provider bitmapProvider);

}
