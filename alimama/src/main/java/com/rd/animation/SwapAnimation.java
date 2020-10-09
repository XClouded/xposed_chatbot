package com.rd.animation;

import android.animation.IntEvaluator;
import android.animation.PropertyValuesHolder;
import android.animation.ValueAnimator;
import android.view.animation.DecelerateInterpolator;
import androidx.annotation.NonNull;
import com.rd.animation.ValueAnimation;

public class SwapAnimation extends AbsAnimation<ValueAnimator> {
    private static final String ANIMATION_X_COORDINATE = "ANIMATION_X_COORDINATE";
    private static final int COORDINATE_NONE = -1;
    private int xEndCoordinate = -1;
    private int xStartCoordinate = -1;

    public SwapAnimation(@NonNull ValueAnimation.UpdateListener updateListener) {
        super(updateListener);
    }

    @NonNull
    public ValueAnimator createAnimator() {
        ValueAnimator valueAnimator = new ValueAnimator();
        valueAnimator.setDuration(350);
        valueAnimator.setInterpolator(new DecelerateInterpolator());
        valueAnimator.addUpdateListener(new ValueAnimator.AnimatorUpdateListener() {
            public void onAnimationUpdate(ValueAnimator valueAnimator) {
                SwapAnimation.this.onAnimateUpdated(valueAnimator);
            }
        });
        return valueAnimator;
    }

    public SwapAnimation progress(float f) {
        if (this.animator != null) {
            long j = (long) (f * ((float) this.animationDuration));
            if (((ValueAnimator) this.animator).getValues() != null && ((ValueAnimator) this.animator).getValues().length > 0) {
                ((ValueAnimator) this.animator).setCurrentPlayTime(j);
            }
        }
        return this;
    }

    @NonNull
    public SwapAnimation with(int i, int i2) {
        if (this.animator != null && hasChanges(i, i2)) {
            this.xStartCoordinate = i;
            this.xEndCoordinate = i2;
            ((ValueAnimator) this.animator).setValues(new PropertyValuesHolder[]{createColorPropertyHolder()});
        }
        return this;
    }

    private PropertyValuesHolder createColorPropertyHolder() {
        PropertyValuesHolder ofInt = PropertyValuesHolder.ofInt(ANIMATION_X_COORDINATE, new int[]{this.xStartCoordinate, this.xEndCoordinate});
        ofInt.setEvaluator(new IntEvaluator());
        return ofInt;
    }

    /* access modifiers changed from: private */
    public void onAnimateUpdated(@NonNull ValueAnimator valueAnimator) {
        int intValue = ((Integer) valueAnimator.getAnimatedValue(ANIMATION_X_COORDINATE)).intValue();
        if (this.listener != null) {
            this.listener.onSwapAnimationUpdated(intValue);
        }
    }

    private boolean hasChanges(int i, int i2) {
        if (this.xStartCoordinate == i && this.xEndCoordinate == i2) {
            return false;
        }
        return true;
    }
}
