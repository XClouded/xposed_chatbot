package com.alibaba.android.bindingx.core.internal;

import android.view.animation.AnimationUtils;
import androidx.annotation.NonNull;
import com.alibaba.android.bindingx.core.internal.AnimationFrame;
import java.util.Map;

abstract class PhysicsAnimationDriver implements AnimationFrame.Callback {
    protected OnAnimationEndListener mAnimationEndListener;
    private AnimationFrame mAnimationFrame;
    protected OnAnimationUpdateListener mAnimationUpdateListener;
    protected boolean mHasFinished;
    protected double mValue;
    protected double mVelocity;

    interface OnAnimationEndListener {
        void onAnimationEnd(@NonNull PhysicsAnimationDriver physicsAnimationDriver, double d, double d2);
    }

    interface OnAnimationUpdateListener {
        void onAnimationUpdate(@NonNull PhysicsAnimationDriver physicsAnimationDriver, double d, double d2);
    }

    /* access modifiers changed from: package-private */
    public abstract boolean isAtRest();

    /* access modifiers changed from: package-private */
    public abstract void onAnimationStart(@NonNull Map<String, Object> map);

    /* access modifiers changed from: package-private */
    public abstract void runAnimationStep(long j);

    PhysicsAnimationDriver() {
    }

    /* access modifiers changed from: package-private */
    public void setOnAnimationUpdateListener(OnAnimationUpdateListener onAnimationUpdateListener) {
        this.mAnimationUpdateListener = onAnimationUpdateListener;
    }

    /* access modifiers changed from: package-private */
    public void setOnAnimationEndListener(OnAnimationEndListener onAnimationEndListener) {
        this.mAnimationEndListener = onAnimationEndListener;
    }

    /* access modifiers changed from: package-private */
    public void start(@NonNull Map<String, Object> map) {
        onAnimationStart(map);
        if (this.mAnimationFrame == null) {
            this.mAnimationFrame = AnimationFrame.newInstance();
        }
        this.mAnimationFrame.requestAnimationFrame(this);
    }

    /* access modifiers changed from: package-private */
    public void cancel() {
        if (this.mAnimationFrame != null) {
            this.mAnimationFrame.clear();
        }
        this.mHasFinished = false;
    }

    /* access modifiers changed from: package-private */
    public boolean hasFinished() {
        return this.mHasFinished;
    }

    /* access modifiers changed from: package-private */
    public double getCurrentValue() {
        return this.mValue;
    }

    /* access modifiers changed from: package-private */
    public double getCurrentVelocity() {
        return this.mVelocity;
    }

    public void doFrame() {
        runAnimationStep(AnimationUtils.currentAnimationTimeMillis());
        if (this.mAnimationUpdateListener != null) {
            this.mAnimationUpdateListener.onAnimationUpdate(this, this.mValue, this.mVelocity);
        }
        if (hasFinished()) {
            if (this.mAnimationEndListener != null) {
                this.mAnimationEndListener.onAnimationEnd(this, this.mValue, this.mVelocity);
            }
            if (this.mAnimationFrame != null) {
                this.mAnimationFrame.clear();
            }
        }
    }
}
