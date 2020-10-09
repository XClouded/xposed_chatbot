package com.rd.animation;

import android.animation.Animator;
import android.animation.AnimatorSet;
import android.animation.ValueAnimator;
import android.view.animation.AccelerateDecelerateInterpolator;
import androidx.annotation.NonNull;
import com.rd.animation.ValueAnimation;
import java.util.Iterator;

public class DropAnimation extends AbsAnimation<AnimatorSet> {
    private int center;
    private int frameRadius;
    private int frameX;
    private int frameY;
    private int radius;
    private int xFromValue;
    private int xToValue;

    public enum AnimationType {
        Width,
        Height,
        Radius
    }

    public DropAnimation(@NonNull ValueAnimation.UpdateListener updateListener) {
        super(updateListener);
    }

    @NonNull
    public AnimatorSet createAnimator() {
        AnimatorSet animatorSet = new AnimatorSet();
        animatorSet.setInterpolator(new AccelerateDecelerateInterpolator());
        return animatorSet;
    }

    public DropAnimation progress(float f) {
        if (this.animator != null) {
            long j = (long) (f * ((float) this.animationDuration));
            boolean z = false;
            Iterator<Animator> it = ((AnimatorSet) this.animator).getChildAnimations().iterator();
            while (it.hasNext()) {
                ValueAnimator valueAnimator = (ValueAnimator) it.next();
                long duration = valueAnimator.getDuration();
                long j2 = z ? j - duration : j;
                if (j2 >= 0) {
                    if (j2 >= duration) {
                        j2 = duration;
                    }
                    if (valueAnimator.getValues() != null && valueAnimator.getValues().length > 0) {
                        valueAnimator.setCurrentPlayTime(j2);
                    }
                    if (!z && duration >= this.animationDuration) {
                        z = true;
                    }
                }
            }
        }
        return this;
    }

    public DropAnimation duration(long j) {
        super.duration(j);
        return this;
    }

    public DropAnimation with(int i, int i2, int i3, int i4) {
        int i5 = i;
        int i6 = i3;
        int i7 = i4;
        if (hasChanges(i, i2, i3, i4)) {
            this.animator = createAnimator();
            this.xFromValue = i5;
            int i8 = i2;
            this.xToValue = i8;
            this.center = i6;
            this.radius = i7;
            int i9 = i6 / 3;
            double d = (double) i7;
            Double.isNaN(d);
            int i10 = (int) (d / 1.5d);
            long j = this.animationDuration / 2;
            this.frameX = i5;
            this.frameY = i6;
            this.frameRadius = i7;
            ValueAnimator createValueAnimation = createValueAnimation(i, i8, this.animationDuration, AnimationType.Width);
            long j2 = j;
            ValueAnimator createValueAnimation2 = createValueAnimation(i3, i9, j2, AnimationType.Height);
            ValueAnimator createValueAnimation3 = createValueAnimation(i9, i3, j2, AnimationType.Height);
            ValueAnimator createValueAnimation4 = createValueAnimation(i4, i10, j2, AnimationType.Radius);
            ((AnimatorSet) this.animator).play(createValueAnimation2).with(createValueAnimation4).with(createValueAnimation).before(createValueAnimation3).before(createValueAnimation(i10, i4, j2, AnimationType.Radius));
        }
        return this;
    }

    private ValueAnimator createValueAnimation(int i, int i2, long j, final AnimationType animationType) {
        ValueAnimator ofInt = ValueAnimator.ofInt(new int[]{i, i2});
        ofInt.setInterpolator(new AccelerateDecelerateInterpolator());
        ofInt.setDuration(j);
        ofInt.addUpdateListener(new ValueAnimator.AnimatorUpdateListener() {
            public void onAnimationUpdate(ValueAnimator valueAnimator) {
                DropAnimation.this.onAnimatorUpdate(((Integer) valueAnimator.getAnimatedValue()).intValue(), animationType);
            }
        });
        return ofInt;
    }

    /* access modifiers changed from: private */
    public void onAnimatorUpdate(int i, AnimationType animationType) {
        switch (animationType) {
            case Width:
                this.frameX = i;
                break;
            case Height:
                this.frameY = i;
                break;
            case Radius:
                this.frameRadius = i;
                break;
        }
        if (this.listener != null) {
            this.listener.onDropAnimationUpdated(this.frameX, this.frameY, this.frameRadius);
        }
    }

    private boolean hasChanges(int i, int i2, int i3, int i4) {
        if (this.xFromValue == i && this.xToValue == i2 && this.center == i3 && this.radius == i4) {
            return false;
        }
        return true;
    }
}
