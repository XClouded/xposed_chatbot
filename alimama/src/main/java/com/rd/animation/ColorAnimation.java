package com.rd.animation;

import android.animation.ArgbEvaluator;
import android.animation.PropertyValuesHolder;
import android.animation.ValueAnimator;
import android.view.animation.DecelerateInterpolator;
import androidx.annotation.NonNull;
import com.rd.animation.ValueAnimation;

public class ColorAnimation extends AbsAnimation<ValueAnimator> {
    private static final String ANIMATION_COLOR = "ANIMATION_COLOR";
    private static final String ANIMATION_COLOR_REVERSE = "ANIMATION_COLOR_REVERSE";
    public static final String DEFAULT_SELECTED_COLOR = "#ffffff";
    public static final String DEFAULT_UNSELECTED_COLOR = "#33ffffff";
    protected int endColor;
    protected int startColor;

    public ColorAnimation(@NonNull ValueAnimation.UpdateListener updateListener) {
        super(updateListener);
    }

    @NonNull
    public ValueAnimator createAnimator() {
        ValueAnimator valueAnimator = new ValueAnimator();
        valueAnimator.setDuration(350);
        valueAnimator.setInterpolator(new DecelerateInterpolator());
        valueAnimator.addUpdateListener(new ValueAnimator.AnimatorUpdateListener() {
            public void onAnimationUpdate(ValueAnimator valueAnimator) {
                ColorAnimation.this.onAnimateUpdated(valueAnimator);
            }
        });
        return valueAnimator;
    }

    public ColorAnimation progress(float f) {
        if (this.animator != null) {
            long j = (long) (f * ((float) this.animationDuration));
            if (((ValueAnimator) this.animator).getValues() != null && ((ValueAnimator) this.animator).getValues().length > 0) {
                ((ValueAnimator) this.animator).setCurrentPlayTime(j);
            }
        }
        return this;
    }

    @NonNull
    public ColorAnimation with(int i, int i2) {
        if (this.animator != null && hasChanges(i, i2)) {
            this.startColor = i;
            this.endColor = i2;
            ((ValueAnimator) this.animator).setValues(new PropertyValuesHolder[]{createColorPropertyHolder(false), createColorPropertyHolder(true)});
        }
        return this;
    }

    /* access modifiers changed from: protected */
    public PropertyValuesHolder createColorPropertyHolder(boolean z) {
        String str;
        int i;
        int i2;
        if (z) {
            str = ANIMATION_COLOR_REVERSE;
            i2 = this.endColor;
            i = this.startColor;
        } else {
            str = ANIMATION_COLOR;
            i2 = this.startColor;
            i = this.endColor;
        }
        PropertyValuesHolder ofInt = PropertyValuesHolder.ofInt(str, new int[]{i2, i});
        ofInt.setEvaluator(new ArgbEvaluator());
        return ofInt;
    }

    private boolean hasChanges(int i, int i2) {
        if (this.startColor == i && this.endColor == i2) {
            return false;
        }
        return true;
    }

    /* access modifiers changed from: private */
    public void onAnimateUpdated(@NonNull ValueAnimator valueAnimator) {
        int intValue = ((Integer) valueAnimator.getAnimatedValue(ANIMATION_COLOR)).intValue();
        int intValue2 = ((Integer) valueAnimator.getAnimatedValue(ANIMATION_COLOR_REVERSE)).intValue();
        if (this.listener != null) {
            this.listener.onColorAnimationUpdated(intValue, intValue2);
        }
    }
}
