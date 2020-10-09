package com.alimama.unwdinamicxcontainer.diywidget.viewcontainer;

import android.view.View;
import android.view.animation.Animation;
import android.view.animation.LinearInterpolator;
import android.view.animation.Transformation;

public class RotationAnimation extends Animation {
    private static final int ANIMATION_DURATION = 666;
    RotationOrientation mAnimationOrientation;
    View mViewUsingAnimation;

    public enum RotationOrientation {
        CLOCKWISE,
        UNCLOCKWIS
    }

    public RotationAnimation(View view, RotationOrientation rotationOrientation) {
        this.mViewUsingAnimation = view;
        this.mAnimationOrientation = rotationOrientation;
    }

    public void initialize(int i, int i2, int i3, int i4) {
        super.initialize(i, i2, i3, i4);
        setRepeatMode(1);
        setInterpolator(new LinearInterpolator());
        setDuration(666);
    }

    /* access modifiers changed from: protected */
    public void applyTransformation(float f, Transformation transformation) {
        float f2 = f * 360.0f;
        if (this.mAnimationOrientation != RotationOrientation.CLOCKWISE) {
            f2 = 0.0f - f2;
        }
        this.mViewUsingAnimation.setRotation(f2);
    }
}
