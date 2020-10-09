package com.taobao.uikit.feature.features.cellanimator;

import android.animation.Animator;
import android.animation.AnimatorSet;
import android.annotation.SuppressLint;
import android.os.Bundle;
import android.os.Parcelable;
import android.os.SystemClock;
import android.view.View;
import android.widget.ListView;
import java.util.WeakHashMap;

public class CellAnimatorController {
    private static final int DEFAULT_ANIMATION_DELAY_MILLIS = 100;
    private static final int DEFAULT_ANIMATION_DURATION_MILLIS = 300;
    private static final int INITIAL_DELAY_MILLIS = 150;
    private static final String SAVEDINSTANCESTATE_FIRSTANIMATEDPOSITION = "savedinstancestate_firstanimatedposition";
    private static final String SAVEDINSTANCESTATE_LASTANIMATEDPOSITION = "savedinstancestate_lastanimatedposition";
    private static final String SAVEDINSTANCESTATE_SHOULDANIMATE = "savedinstancestate_shouldanimate";
    private int mAnimationDelayMillis = 100;
    private int mAnimationDurationMillis = 300;
    private long mAnimationStartMillis;
    private final WeakHashMap<View, Animator> mAnimators = new WeakHashMap<>();
    private int mFirstAnimatedPosition;
    private int mInitialDelayMillis = 150;
    private int mLastAnimatedPosition;
    private final ListView mListView;
    private boolean mShouldAnimate = true;

    public CellAnimatorController(ListView listView) {
        this.mListView = listView;
        this.mAnimationStartMillis = -1;
        this.mFirstAnimatedPosition = -1;
        this.mLastAnimatedPosition = -1;
    }

    public void reset() {
        clearAnimators();
        this.mFirstAnimatedPosition = -1;
        this.mLastAnimatedPosition = -1;
        this.mAnimationStartMillis = -1;
    }

    public void setShouldAnimateFromPosition(int i) {
        setAnimatorEnable(true);
        int i2 = i - 1;
        this.mFirstAnimatedPosition = i2;
        this.mLastAnimatedPosition = i2;
    }

    public void setShouldAnimateNotVisible() {
        setAnimatorEnable(true);
        this.mFirstAnimatedPosition = this.mListView.getLastVisiblePosition();
        this.mLastAnimatedPosition = this.mListView.getLastVisiblePosition();
    }

    public void setLastAnimatedPosition(int i) {
        this.mLastAnimatedPosition = i;
    }

    public void setInitialDelayMillis(int i) {
        this.mInitialDelayMillis = i;
    }

    public void setAnimationDelayMillis(int i) {
        this.mAnimationDelayMillis = i;
    }

    public void setAnimationDurationMillis(int i) {
        this.mAnimationDurationMillis = i;
    }

    public void setAnimatorEnable(boolean z) {
        this.mShouldAnimate = z;
        if (!z) {
            clearAnimators();
        }
    }

    public void clearAnimators() {
        for (Animator end : this.mAnimators.values()) {
            end.end();
        }
        this.mAnimators.clear();
    }

    public void animateViewIfNecessary(int i, View view, Animator[] animatorArr) {
        if (this.mShouldAnimate && i > this.mLastAnimatedPosition) {
            if (this.mFirstAnimatedPosition == -1) {
                this.mFirstAnimatedPosition = i;
            }
            animateView(i, view, animatorArr);
            this.mLastAnimatedPosition = i;
        }
    }

    private void animateView(int i, View view, Animator[] animatorArr) {
        Animator animator = this.mAnimators.get(view);
        if (animator != null) {
            animator.cancel();
        }
        if (this.mAnimationStartMillis == -1) {
            this.mAnimationStartMillis = SystemClock.uptimeMillis();
        }
        view.setAlpha(0.0f);
        AnimatorSet animatorSet = new AnimatorSet();
        animatorSet.playTogether(animatorArr);
        animatorSet.setStartDelay((long) calculateAnimationDelay(i));
        animatorSet.setDuration((long) this.mAnimationDurationMillis);
        animatorSet.start();
        this.mAnimators.put(view, animatorSet);
    }

    @SuppressLint({"NewApi"})
    private int calculateAnimationDelay(int i) {
        if ((this.mListView.getLastVisiblePosition() - this.mListView.getFirstVisiblePosition()) + 1 < (i - 1) - this.mFirstAnimatedPosition) {
            return this.mAnimationDelayMillis;
        }
        return Math.max(0, (int) ((-SystemClock.uptimeMillis()) + this.mAnimationStartMillis + ((long) this.mInitialDelayMillis) + ((long) ((i - this.mFirstAnimatedPosition) * this.mAnimationDelayMillis))));
    }

    public Parcelable onSaveInstanceState() {
        Bundle bundle = new Bundle();
        bundle.putInt(SAVEDINSTANCESTATE_FIRSTANIMATEDPOSITION, this.mFirstAnimatedPosition);
        bundle.putInt(SAVEDINSTANCESTATE_LASTANIMATEDPOSITION, this.mLastAnimatedPosition);
        bundle.putBoolean(SAVEDINSTANCESTATE_SHOULDANIMATE, this.mShouldAnimate);
        return bundle;
    }

    public void onRestoreInstanceState(Parcelable parcelable) {
        if (parcelable instanceof Bundle) {
            Bundle bundle = (Bundle) parcelable;
            this.mFirstAnimatedPosition = bundle.getInt(SAVEDINSTANCESTATE_FIRSTANIMATEDPOSITION);
            this.mLastAnimatedPosition = bundle.getInt(SAVEDINSTANCESTATE_LASTANIMATEDPOSITION);
            this.mShouldAnimate = bundle.getBoolean(SAVEDINSTANCESTATE_SHOULDANIMATE);
        }
    }
}
