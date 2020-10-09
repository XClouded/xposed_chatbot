package com.rd.animation;

import android.animation.Animator;
import android.animation.AnimatorSet;
import android.animation.ValueAnimator;
import android.view.animation.AccelerateDecelerateInterpolator;
import androidx.annotation.NonNull;
import com.rd.animation.ValueAnimation;
import java.util.Iterator;

public class WormAnimation extends AbsAnimation<AnimatorSet> {
    int fromValue;
    boolean isRightSide;
    int radius;
    int rectLeftX;
    int rectRightX;
    int toValue;

    public WormAnimation(@NonNull ValueAnimation.UpdateListener updateListener) {
        super(updateListener);
    }

    @NonNull
    public AnimatorSet createAnimator() {
        AnimatorSet animatorSet = new AnimatorSet();
        animatorSet.setInterpolator(new AccelerateDecelerateInterpolator());
        return animatorSet;
    }

    public WormAnimation duration(long j) {
        super.duration(j);
        return this;
    }

    public WormAnimation with(int i, int i2, int i3, boolean z) {
        if (hasChanges(i, i2, i3, z)) {
            this.animator = createAnimator();
            this.fromValue = i;
            this.toValue = i2;
            this.radius = i3;
            this.isRightSide = z;
            this.rectLeftX = i - i3;
            this.rectRightX = i + i3;
            AnimationValues createAnimationValues = createAnimationValues(z);
            long j = this.animationDuration / 2;
            ((AnimatorSet) this.animator).playSequentially(new Animator[]{createWormAnimator(createAnimationValues.fromX, createAnimationValues.toX, j, false), createWormAnimator(createAnimationValues.reverseFromX, createAnimationValues.reverseToX, j, true)});
        }
        return this;
    }

    public WormAnimation progress(float f) {
        if (this.animator != null) {
            long j = (long) (f * ((float) this.animationDuration));
            Iterator<Animator> it = ((AnimatorSet) this.animator).getChildAnimations().iterator();
            while (it.hasNext()) {
                ValueAnimator valueAnimator = (ValueAnimator) it.next();
                long duration = valueAnimator.getDuration();
                if (j < 0) {
                    j = 0;
                }
                if (j < duration) {
                    duration = j;
                }
                if (valueAnimator.getValues() != null && valueAnimator.getValues().length > 0) {
                    valueAnimator.setCurrentPlayTime(duration);
                }
                j -= duration;
            }
        }
        return this;
    }

    /* access modifiers changed from: package-private */
    public ValueAnimator createWormAnimator(int i, int i2, long j, final boolean z) {
        ValueAnimator ofInt = ValueAnimator.ofInt(new int[]{i, i2});
        ofInt.setInterpolator(new AccelerateDecelerateInterpolator());
        ofInt.setDuration(j);
        ofInt.addUpdateListener(new ValueAnimator.AnimatorUpdateListener() {
            public void onAnimationUpdate(ValueAnimator valueAnimator) {
                int intValue = ((Integer) valueAnimator.getAnimatedValue()).intValue();
                if (!z) {
                    if (WormAnimation.this.isRightSide) {
                        WormAnimation.this.rectRightX = intValue;
                    } else {
                        WormAnimation.this.rectLeftX = intValue;
                    }
                } else if (WormAnimation.this.isRightSide) {
                    WormAnimation.this.rectLeftX = intValue;
                } else {
                    WormAnimation.this.rectRightX = intValue;
                }
                WormAnimation.this.listener.onWormAnimationUpdated(WormAnimation.this.rectLeftX, WormAnimation.this.rectRightX);
            }
        });
        return ofInt;
    }

    /* access modifiers changed from: package-private */
    public boolean hasChanges(int i, int i2, int i3, boolean z) {
        if (this.fromValue == i && this.toValue == i2 && this.radius == i3 && this.isRightSide == z) {
            return false;
        }
        return true;
    }

    /* access modifiers changed from: package-private */
    @NonNull
    public AnimationValues createAnimationValues(boolean z) {
        int i;
        int i2;
        int i3;
        int i4;
        if (z) {
            i = this.fromValue + this.radius;
            i2 = this.toValue + this.radius;
            i3 = this.fromValue - this.radius;
            i4 = this.toValue - this.radius;
        } else {
            i = this.fromValue - this.radius;
            i2 = this.toValue - this.radius;
            i3 = this.fromValue + this.radius;
            i4 = this.toValue + this.radius;
        }
        return new AnimationValues(i, i2, i3, i4);
    }

    class AnimationValues {
        final int fromX;
        final int reverseFromX;
        final int reverseToX;
        final int toX;

        AnimationValues(int i, int i2, int i3, int i4) {
            this.fromX = i;
            this.toX = i2;
            this.reverseFromX = i3;
            this.reverseToX = i4;
        }
    }
}
