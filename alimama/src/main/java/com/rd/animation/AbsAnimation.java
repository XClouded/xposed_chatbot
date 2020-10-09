package com.rd.animation;

import android.animation.Animator;
import android.animation.ValueAnimator;
import androidx.annotation.NonNull;
import com.rd.animation.ValueAnimation;

public abstract class AbsAnimation<T extends Animator> {
    public static final int DEFAULT_ANIMATION_TIME = 350;
    protected long animationDuration = 350;
    protected T animator;
    protected ValueAnimation.UpdateListener listener;

    @NonNull
    public abstract T createAnimator();

    public abstract AbsAnimation progress(float f);

    public AbsAnimation(@NonNull ValueAnimation.UpdateListener updateListener) {
        this.listener = updateListener;
        this.animator = createAnimator();
    }

    public AbsAnimation duration(long j) {
        this.animationDuration = j;
        if (this.animator instanceof ValueAnimator) {
            this.animator.setDuration(this.animationDuration);
        }
        return this;
    }

    public void start() {
        if (this.animator != null && !this.animator.isRunning()) {
            this.animator.start();
        }
    }

    public void end() {
        if (this.animator != null && this.animator.isStarted()) {
            this.animator.end();
        }
    }
}
