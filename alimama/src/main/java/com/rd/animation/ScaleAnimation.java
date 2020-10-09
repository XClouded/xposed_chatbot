package com.rd.animation;

import android.animation.IntEvaluator;
import android.animation.PropertyValuesHolder;
import android.animation.ValueAnimator;
import android.view.animation.DecelerateInterpolator;
import androidx.annotation.NonNull;
import com.rd.animation.ValueAnimation;

public class ScaleAnimation extends ColorAnimation {
    private static final String ANIMATION_COLOR = "ANIMATION_COLOR";
    private static final String ANIMATION_COLOR_REVERSE = "ANIMATION_COLOR_REVERSE";
    private static final String ANIMATION_SCALE = "ANIMATION_SCALE";
    private static final String ANIMATION_SCALE_REVERSE = "ANIMATION_SCALE_REVERSE";
    public static final float DEFAULT_SCALE_FACTOR = 0.7f;
    public static final float MAX_SCALE_FACTOR = 1.0f;
    public static final float MIN_SCALE_FACTOR = 0.3f;
    private int radiusPx;
    private float scaleFactor;

    public ScaleAnimation(@NonNull ValueAnimation.UpdateListener updateListener) {
        super(updateListener);
    }

    @NonNull
    public ValueAnimator createAnimator() {
        ValueAnimator valueAnimator = new ValueAnimator();
        valueAnimator.setDuration(350);
        valueAnimator.setInterpolator(new DecelerateInterpolator());
        valueAnimator.addUpdateListener(new ValueAnimator.AnimatorUpdateListener() {
            public void onAnimationUpdate(ValueAnimator valueAnimator) {
                ScaleAnimation.this.onAnimateUpdated(valueAnimator);
            }
        });
        return valueAnimator;
    }

    @NonNull
    public ScaleAnimation with(int i, int i2, int i3, float f) {
        if (this.animator != null && hasChanges(i, i2, i3, f)) {
            this.startColor = i;
            this.endColor = i2;
            this.radiusPx = i3;
            this.scaleFactor = f;
            ((ValueAnimator) this.animator).setValues(new PropertyValuesHolder[]{createColorPropertyHolder(false), createColorPropertyHolder(true), createScalePropertyHolder(false), createScalePropertyHolder(true)});
        }
        return this;
    }

    /* access modifiers changed from: private */
    public void onAnimateUpdated(@NonNull ValueAnimator valueAnimator) {
        int intValue = ((Integer) valueAnimator.getAnimatedValue(ANIMATION_COLOR)).intValue();
        int intValue2 = ((Integer) valueAnimator.getAnimatedValue(ANIMATION_COLOR_REVERSE)).intValue();
        int intValue3 = ((Integer) valueAnimator.getAnimatedValue(ANIMATION_SCALE)).intValue();
        int intValue4 = ((Integer) valueAnimator.getAnimatedValue(ANIMATION_SCALE_REVERSE)).intValue();
        if (this.listener != null) {
            this.listener.onScaleAnimationUpdated(intValue, intValue2, intValue3, intValue4);
        }
    }

    @NonNull
    private PropertyValuesHolder createScalePropertyHolder(boolean z) {
        String str;
        int i;
        int i2;
        if (z) {
            str = ANIMATION_SCALE_REVERSE;
            i2 = this.radiusPx;
            i = (int) (((float) this.radiusPx) * this.scaleFactor);
        } else {
            str = ANIMATION_SCALE;
            i2 = (int) (((float) this.radiusPx) * this.scaleFactor);
            i = this.radiusPx;
        }
        PropertyValuesHolder ofInt = PropertyValuesHolder.ofInt(str, new int[]{i2, i});
        ofInt.setEvaluator(new IntEvaluator());
        return ofInt;
    }

    private boolean hasChanges(int i, int i2, int i3, float f) {
        if (this.startColor == i && this.endColor == i2 && this.radiusPx == i3 && this.scaleFactor == f) {
            return false;
        }
        return true;
    }
}
