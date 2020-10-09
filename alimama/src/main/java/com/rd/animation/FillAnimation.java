package com.rd.animation;

import android.animation.IntEvaluator;
import android.animation.PropertyValuesHolder;
import android.animation.ValueAnimator;
import android.view.animation.DecelerateInterpolator;
import androidx.annotation.NonNull;
import com.rd.animation.ValueAnimation;

public class FillAnimation extends ColorAnimation {
    private static final String ANIMATION_COLOR = "ANIMATION_COLOR";
    private static final String ANIMATION_COLOR_REVERSE = "ANIMATION_COLOR_REVERSE";
    private static final String ANIMATION_RADIUS = "ANIMATION_RADIUS";
    private static final String ANIMATION_RADIUS_REVERSE = "ANIMATION_RADIUS_REVERSE";
    private static final String ANIMATION_STROKE = "ANIMATION_STROKE";
    private static final String ANIMATION_STROKE_REVERSE = "ANIMATION_STROKE_REVERSE";
    public static final int DEFAULT_STROKE_DP = 1;
    private int radiusPx;
    private int strokePx;

    public FillAnimation(@NonNull ValueAnimation.UpdateListener updateListener) {
        super(updateListener);
    }

    @NonNull
    public ValueAnimator createAnimator() {
        ValueAnimator valueAnimator = new ValueAnimator();
        valueAnimator.setDuration(350);
        valueAnimator.setInterpolator(new DecelerateInterpolator());
        valueAnimator.addUpdateListener(new ValueAnimator.AnimatorUpdateListener() {
            public void onAnimationUpdate(ValueAnimator valueAnimator) {
                FillAnimation.this.onAnimateUpdated(valueAnimator);
            }
        });
        return valueAnimator;
    }

    @NonNull
    public FillAnimation with(int i, int i2, int i3, int i4) {
        if (this.animator != null && hasChanges(i, i2, i3, i4)) {
            this.startColor = i;
            this.endColor = i2;
            this.radiusPx = i3;
            this.strokePx = i4;
            ((ValueAnimator) this.animator).setValues(new PropertyValuesHolder[]{createColorPropertyHolder(false), createColorPropertyHolder(true), createRadiusPropertyHolder(false), createRadiusPropertyHolder(true), createStrokePropertyHolder(false), createStrokePropertyHolder(true)});
        }
        return this;
    }

    @NonNull
    private PropertyValuesHolder createRadiusPropertyHolder(boolean z) {
        String str;
        int i;
        int i2;
        if (z) {
            str = ANIMATION_RADIUS_REVERSE;
            i2 = this.radiusPx / 2;
            i = this.radiusPx;
        } else {
            str = ANIMATION_RADIUS;
            i2 = this.radiusPx;
            i = this.radiusPx / 2;
        }
        PropertyValuesHolder ofInt = PropertyValuesHolder.ofInt(str, new int[]{i2, i});
        ofInt.setEvaluator(new IntEvaluator());
        return ofInt;
    }

    @NonNull
    private PropertyValuesHolder createStrokePropertyHolder(boolean z) {
        String str;
        int i;
        int i2;
        if (z) {
            str = ANIMATION_STROKE_REVERSE;
            i2 = this.radiusPx;
            i = 0;
        } else {
            str = ANIMATION_STROKE;
            i = this.radiusPx;
            i2 = 0;
        }
        PropertyValuesHolder ofInt = PropertyValuesHolder.ofInt(str, new int[]{i2, i});
        ofInt.setEvaluator(new IntEvaluator());
        return ofInt;
    }

    /* access modifiers changed from: private */
    public void onAnimateUpdated(@NonNull ValueAnimator valueAnimator) {
        int intValue = ((Integer) valueAnimator.getAnimatedValue(ANIMATION_COLOR)).intValue();
        int intValue2 = ((Integer) valueAnimator.getAnimatedValue(ANIMATION_COLOR_REVERSE)).intValue();
        int intValue3 = ((Integer) valueAnimator.getAnimatedValue(ANIMATION_RADIUS)).intValue();
        int intValue4 = ((Integer) valueAnimator.getAnimatedValue(ANIMATION_RADIUS_REVERSE)).intValue();
        int intValue5 = ((Integer) valueAnimator.getAnimatedValue(ANIMATION_STROKE)).intValue();
        int intValue6 = ((Integer) valueAnimator.getAnimatedValue(ANIMATION_STROKE_REVERSE)).intValue();
        if (this.listener != null) {
            this.listener.onFillAnimationUpdated(intValue, intValue2, intValue3, intValue4, intValue5, intValue6);
        }
    }

    private boolean hasChanges(int i, int i2, int i3, int i4) {
        if (this.startColor == i && this.endColor == i2 && this.radiusPx == i3 && this.strokePx == i4) {
            return false;
        }
        return true;
    }
}
