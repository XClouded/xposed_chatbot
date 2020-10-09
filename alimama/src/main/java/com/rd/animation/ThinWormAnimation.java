package com.rd.animation;

import android.animation.Animator;
import android.animation.AnimatorSet;
import android.animation.ValueAnimator;
import android.view.animation.AccelerateDecelerateInterpolator;
import androidx.annotation.NonNull;
import com.rd.animation.ValueAnimation;
import com.rd.animation.WormAnimation;

public class ThinWormAnimation extends WormAnimation {
    private static final float PERCENTAGE_HEIGHT_DURATION = 0.25f;
    private static final float PERCENTAGE_REVERSE_HEIGHT_DELAY = 0.65f;
    private static final float PERCENTAGE_SIZE_DURATION_DELAY = 0.7f;
    /* access modifiers changed from: private */
    public int height;

    public ThinWormAnimation(@NonNull ValueAnimation.UpdateListener updateListener) {
        super(updateListener);
    }

    public ThinWormAnimation duration(long j) {
        super.duration(j);
        return this;
    }

    public WormAnimation with(int i, int i2, int i3, boolean z) {
        int i4 = i;
        int i5 = i3;
        boolean z2 = z;
        if (hasChanges(i, i2, i3, z)) {
            this.animator = createAnimator();
            this.fromValue = i4;
            this.toValue = i2;
            this.radius = i5;
            this.height = i5 * 2;
            this.isRightSide = z2;
            this.rectLeftX = i4 - i5;
            this.rectRightX = i4 + i5;
            long j = (long) (((float) this.animationDuration) * 0.7f);
            long j2 = this.animationDuration;
            long j3 = (long) (((float) this.animationDuration) * PERCENTAGE_REVERSE_HEIGHT_DELAY);
            WormAnimation.AnimationValues createAnimationValues = createAnimationValues(z2);
            ValueAnimator createWormAnimator = createWormAnimator(createAnimationValues.fromX, createAnimationValues.toX, j, false);
            ValueAnimator createHeightAnimator = createHeightAnimator(this.height, this.height / 2, 0);
            ((AnimatorSet) this.animator).playTogether(new Animator[]{createWormAnimator, createWormAnimator(createAnimationValues.reverseFromX, createAnimationValues.reverseToX, j2, true), createHeightAnimator, createHeightAnimator(this.height / 2, this.height, j3)});
        }
        return this;
    }

    private ValueAnimator createHeightAnimator(int i, int i2, long j) {
        ValueAnimator ofInt = ValueAnimator.ofInt(new int[]{i, i2});
        ofInt.setInterpolator(new AccelerateDecelerateInterpolator());
        ofInt.setDuration((long) (((float) this.animationDuration) * PERCENTAGE_HEIGHT_DURATION));
        ofInt.setStartDelay(j);
        ofInt.addUpdateListener(new ValueAnimator.AnimatorUpdateListener() {
            public void onAnimationUpdate(ValueAnimator valueAnimator) {
                int unused = ThinWormAnimation.this.height = ((Integer) valueAnimator.getAnimatedValue()).intValue();
                ThinWormAnimation.this.listener.onThinWormAnimationUpdated(ThinWormAnimation.this.rectLeftX, ThinWormAnimation.this.rectRightX, ThinWormAnimation.this.height);
            }
        });
        return ofInt;
    }

    public ThinWormAnimation progress(float f) {
        if (this.animator != null) {
            long j = (long) (f * ((float) this.animationDuration));
            int size = ((AnimatorSet) this.animator).getChildAnimations().size();
            long j2 = (long) (((float) this.animationDuration) * PERCENTAGE_REVERSE_HEIGHT_DELAY);
            for (int i = 0; i < size; i++) {
                ValueAnimator valueAnimator = (ValueAnimator) ((AnimatorSet) this.animator).getChildAnimations().get(i);
                if (i == 3) {
                    if (j < j2) {
                        break;
                    }
                    j -= j2;
                }
                long duration = j >= valueAnimator.getDuration() ? valueAnimator.getDuration() : j;
                if (valueAnimator.getValues() != null && valueAnimator.getValues().length > 0) {
                    valueAnimator.setCurrentPlayTime(duration);
                }
            }
        }
        return this;
    }
}
