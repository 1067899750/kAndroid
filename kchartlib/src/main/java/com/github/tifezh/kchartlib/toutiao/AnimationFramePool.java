package com.github.tifezh.kchartlib.toutiao;

import java.util.ArrayList;
import java.util.List;

/**
 * @author puyantao
 * @description
 * @date 2020/7/29 16:21
 */
public class AnimationFramePool {
    /**
     * 延时时间
     */
    private static final int DURATION = 800;
    /**
     * 连点次数缓存数组
     */
    private List<AnimationFrame> runningFrameList;
    /**
     * 释放对象数组，方便再利用
     */
    private List<AnimationFrame> idleFrameList;
    /**
     * 数组的大小
     */
    private int maxFrameSize;
    /**
     * 射出表情图表个数
     */
    private int elementAmount;
    /**
     * 记录加入对象个数，判断是否需要新建
     */
    private int frameCount;

    AnimationFramePool(int maxFrameSize, int elementAmount) {
        this.maxFrameSize = maxFrameSize;
        this.elementAmount = elementAmount;
        runningFrameList = new ArrayList<>(maxFrameSize);
        idleFrameList = new ArrayList<>(maxFrameSize);
    }

    boolean hasRunningAnimation() {
        return runningFrameList.size() > 0;
    }

    /**
     * 获取执行对象
     * @param type
     * @return
     */
    AnimationFrame obtain(int type) {
        // RunningAnimationFrame 存在onlyOne直接复用
        AnimationFrame animationFrame = getRunningFrameListByOnlyOneAndType(type);
        if (animationFrame != null) {
            return animationFrame;
        }

        // 有空闲AnimationFrame直接使用, 加入runningFrame队列中
        animationFrame = removeIdleFrameListDownByType(type);
        if (animationFrame == null && frameCount < maxFrameSize) {
            frameCount++;
            if (type == EruptionAnimationFrame.TYPE) {
                animationFrame = new EruptionAnimationFrame(elementAmount, DURATION);
            } else {
                animationFrame = new TextAnimationFrame(DURATION);
            }
        }
        if (animationFrame != null) {
            runningFrameList.add(animationFrame);
        }
        return animationFrame;
    }

    /**
     * 从运行列表中获取对象，有的话直接复用
     * @param type
     * @return
     */
    private AnimationFrame getRunningFrameListByOnlyOneAndType(int type) {
        for (int i = runningFrameList.size() - 1; i >= 0; i--) {
            AnimationFrame animationFrame = runningFrameList.get(i);
            if (type == animationFrame.getType() && animationFrame.onlyOne()) {
                return animationFrame;
            }
        }
        return null;
    }

    /**
     * 释放所有对象
     */
    void recycleAll() {
        for (int i = runningFrameList.size() - 1; i >= 0; i--) {
            AnimationFrame animationFrame = runningFrameList.get(i);
            recycle(animationFrame);
            animationFrame.reset();
        }
    }

    /**
     * 释放对象
     * @param animationFrame
     */
    void recycle(AnimationFrame animationFrame) {
        runningFrameList.remove(animationFrame);
        idleFrameList.add(animationFrame);
    }

    /**
     * 获取运行列表对象
     * @return
     */
    List<AnimationFrame> getRunningFrameList() {
        return runningFrameList;
    }

    /**
     * 有空闲AnimationFrame直接使用, 加入runningFrame队列中
     * @param type
     * @return
     */
    private AnimationFrame removeIdleFrameListDownByType(int type) {
        for (int i = idleFrameList.size() - 1; i >= 0; i--) {
            AnimationFrame animationFrame = idleFrameList.get(i);
            if (type == animationFrame.getType()) {
                return idleFrameList.remove(i);
            }
        }
        return null;
    }
}
