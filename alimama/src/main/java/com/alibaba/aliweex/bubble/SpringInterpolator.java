package com.alibaba.aliweex.bubble;

import android.view.animation.Interpolator;

public class SpringInterpolator implements Interpolator {
    public static final float FIRST_BOUNCE_TIME = 0.26666f;
    public static final float ROTATION_TIME = 0.46667f;
    public static final float SECOND_BOUNCE_TIME = 0.26667f;

    private float firstBounce(float f) {
        return (((2.5f * f) * f) - (f * 3.0f)) + 1.85556f;
    }

    private float rotation(float f) {
        return 4.592f * f * f;
    }

    private float secondBounce(float f) {
        return (((0.625f * f) * f) - (f * 1.08f)) + 1.458f;
    }

    public float getInterpolation(float f) {
        if (f < 0.46667f) {
            return rotation(f);
        }
        if (f < 0.73333f) {
            return firstBounce(f);
        }
        return secondBounce(f);
    }
}
