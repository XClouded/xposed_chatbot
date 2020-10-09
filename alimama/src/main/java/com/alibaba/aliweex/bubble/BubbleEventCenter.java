package com.alibaba.aliweex.bubble;

import java.util.ArrayList;
import java.util.Iterator;

public class BubbleEventCenter {
    private static volatile BubbleEventCenter _instance;
    private ArrayList<IBubbleAnimationListener> mAnimListeners = new ArrayList<>();

    public enum AnimationType {
        MoveLeft,
        MoveRight,
        ScrollLeft,
        ScrollRight,
        EdgeBounceLeft,
        EdgeBounceRight,
        ReplaceScale
    }

    public interface IBubbleAnimationListener {
        void onCancel(AnimationType animationType, BubbleAnimateWrapper bubbleAnimateWrapper);

        void onEnd(AnimationType animationType, BubbleAnimateWrapper bubbleAnimateWrapper);

        void onStart(AnimationType animationType, BubbleAnimateWrapper bubbleAnimateWrapper);
    }

    private BubbleEventCenter() {
    }

    public static BubbleEventCenter getEventCenter() {
        if (_instance == null) {
            synchronized (BubbleEventCenter.class) {
                if (_instance == null) {
                    _instance = new BubbleEventCenter();
                }
            }
        }
        return _instance;
    }

    public boolean addBubbleAnimListener(IBubbleAnimationListener iBubbleAnimationListener) {
        if (iBubbleAnimationListener == null || this.mAnimListeners.contains(iBubbleAnimationListener)) {
            return false;
        }
        return this.mAnimListeners.add(iBubbleAnimationListener);
    }

    public boolean removeBubbleAnimListener(IBubbleAnimationListener iBubbleAnimationListener) {
        if (iBubbleAnimationListener != null) {
            return this.mAnimListeners.remove(iBubbleAnimationListener);
        }
        return false;
    }

    public void fireAnimationStart(AnimationType animationType, BubbleAnimateWrapper bubbleAnimateWrapper) {
        Iterator<IBubbleAnimationListener> it = this.mAnimListeners.iterator();
        while (it.hasNext()) {
            it.next().onStart(animationType, bubbleAnimateWrapper);
        }
    }

    public void fireAnimationEnd(AnimationType animationType, BubbleAnimateWrapper bubbleAnimateWrapper) {
        Iterator<IBubbleAnimationListener> it = this.mAnimListeners.iterator();
        while (it.hasNext()) {
            it.next().onEnd(animationType, bubbleAnimateWrapper);
        }
    }
}
