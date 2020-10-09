package com.taobao.android.animationkit;

import android.content.Context;
import android.util.AttributeSet;
import com.airbnb.lottie.LottieAnimationView;

public class GuardedLottieAnimationView extends LottieAnimationView {
    public GuardedLottieAnimationView(Context context) {
        super(context);
    }

    public GuardedLottieAnimationView(Context context, AttributeSet attributeSet) {
        super(context, attributeSet);
    }

    public GuardedLottieAnimationView(Context context, AttributeSet attributeSet, int i) {
        super(context, attributeSet, i);
    }

    public void playAnimation() {
        try {
            super.playAnimation();
        } catch (Throwable unused) {
        }
    }

    public void pauseAnimation() {
        try {
            super.pauseAnimation();
        } catch (Throwable unused) {
        }
    }

    public void resumeAnimation() {
        try {
            super.resumeAnimation();
        } catch (Throwable unused) {
        }
    }

    public void cancelAnimation() {
        try {
            super.cancelAnimation();
        } catch (Throwable unused) {
        }
    }
}
