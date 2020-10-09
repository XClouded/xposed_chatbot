package com.taobao.uikit.extend.material;

import android.animation.Animator;
import android.animation.ObjectAnimator;
import android.graphics.Canvas;
import android.graphics.ColorFilter;
import android.graphics.Paint;
import android.graphics.Rect;
import android.graphics.RectF;
import android.graphics.drawable.Animatable;
import android.graphics.drawable.Drawable;
import android.util.Property;
import android.view.animation.DecelerateInterpolator;
import android.view.animation.Interpolator;
import android.view.animation.LinearInterpolator;

public class TBCircularProgressDrawable extends Drawable implements Animatable {
    private static final int ANGLE_ANIMATOR_DURATION = 2000;
    private static final Interpolator ANGLE_INTERPOLATOR = new LinearInterpolator();
    private static final int MIN_SWEEP_ANGLE = 30;
    private static final int SWEEP_ANIMATOR_DURATION = 600;
    private static final Interpolator SWEEP_INTERPOLATOR = new DecelerateInterpolator();
    private final RectF fBounds = new RectF();
    private Property<TBCircularProgressDrawable, Float> mAngleProperty = new Property<TBCircularProgressDrawable, Float>(Float.class, "angle") {
        public Float get(TBCircularProgressDrawable tBCircularProgressDrawable) {
            return Float.valueOf(tBCircularProgressDrawable.getCurrentGlobalAngle());
        }

        public void set(TBCircularProgressDrawable tBCircularProgressDrawable, Float f) {
            tBCircularProgressDrawable.setCurrentGlobalAngle(f.floatValue());
        }
    };
    private float mBorderWidth;
    private float mCurrentGlobalAngle;
    private float mCurrentGlobalAngleOffset;
    private float mCurrentSweepAngle;
    private boolean mModeAppearing;
    private ObjectAnimator mObjectAnimatorAngle;
    private ObjectAnimator mObjectAnimatorSweep;
    private Paint mPaint;
    private boolean mRunning;
    private Property<TBCircularProgressDrawable, Float> mSweepProperty = new Property<TBCircularProgressDrawable, Float>(Float.class, "arc") {
        public Float get(TBCircularProgressDrawable tBCircularProgressDrawable) {
            return Float.valueOf(tBCircularProgressDrawable.getCurrentSweepAngle());
        }

        public void set(TBCircularProgressDrawable tBCircularProgressDrawable, Float f) {
            tBCircularProgressDrawable.setCurrentSweepAngle(f.floatValue());
        }
    };

    public int getOpacity() {
        return -2;
    }

    public TBCircularProgressDrawable(int i, float f) {
        this.mBorderWidth = f;
        this.mPaint = new Paint();
        this.mPaint.setAntiAlias(true);
        this.mPaint.setStyle(Paint.Style.STROKE);
        this.mPaint.setStrokeWidth(f);
        this.mPaint.setColor(i);
        setupAnimations();
    }

    public void draw(Canvas canvas) {
        float f;
        float f2 = this.mCurrentGlobalAngle - this.mCurrentGlobalAngleOffset;
        float f3 = this.mCurrentSweepAngle;
        if (!this.mModeAppearing) {
            f2 += f3;
            f = (360.0f - f3) - 30.0f;
        } else {
            f = f3 + 30.0f;
        }
        canvas.drawArc(this.fBounds, f2, f, false, this.mPaint);
    }

    public void setAlpha(int i) {
        this.mPaint.setAlpha(i);
    }

    public void setColorFilter(ColorFilter colorFilter) {
        this.mPaint.setColorFilter(colorFilter);
    }

    /* access modifiers changed from: private */
    public void toggleAppearingMode() {
        this.mModeAppearing = !this.mModeAppearing;
        if (this.mModeAppearing) {
            this.mCurrentGlobalAngleOffset = (this.mCurrentGlobalAngleOffset + 60.0f) % 360.0f;
        }
    }

    /* access modifiers changed from: protected */
    public void onBoundsChange(Rect rect) {
        super.onBoundsChange(rect);
        this.fBounds.left = ((float) rect.left) + (this.mBorderWidth / 2.0f) + 0.5f;
        this.fBounds.right = (((float) rect.right) - (this.mBorderWidth / 2.0f)) - 0.5f;
        this.fBounds.top = ((float) rect.top) + (this.mBorderWidth / 2.0f) + 0.5f;
        this.fBounds.bottom = (((float) rect.bottom) - (this.mBorderWidth / 2.0f)) - 0.5f;
    }

    private void setupAnimations() {
        this.mObjectAnimatorAngle = ObjectAnimator.ofFloat(this, this.mAngleProperty, new float[]{360.0f});
        this.mObjectAnimatorAngle.setInterpolator(ANGLE_INTERPOLATOR);
        this.mObjectAnimatorAngle.setDuration(2000);
        this.mObjectAnimatorAngle.setRepeatMode(1);
        this.mObjectAnimatorAngle.setRepeatCount(-1);
        this.mObjectAnimatorSweep = ObjectAnimator.ofFloat(this, this.mSweepProperty, new float[]{300.0f});
        this.mObjectAnimatorSweep.setInterpolator(SWEEP_INTERPOLATOR);
        this.mObjectAnimatorSweep.setDuration(600);
        this.mObjectAnimatorSweep.setRepeatMode(1);
        this.mObjectAnimatorSweep.setRepeatCount(-1);
        this.mObjectAnimatorSweep.addListener(new Animator.AnimatorListener() {
            public void onAnimationCancel(Animator animator) {
            }

            public void onAnimationEnd(Animator animator) {
            }

            public void onAnimationStart(Animator animator) {
            }

            public void onAnimationRepeat(Animator animator) {
                TBCircularProgressDrawable.this.toggleAppearingMode();
            }
        });
    }

    public void start() {
        if (!isRunning()) {
            this.mRunning = true;
            this.mObjectAnimatorAngle.start();
            this.mObjectAnimatorSweep.start();
            invalidateSelf();
        }
    }

    public void stop() {
        if (isRunning()) {
            this.mRunning = false;
            this.mObjectAnimatorAngle.cancel();
            this.mObjectAnimatorSweep.cancel();
            invalidateSelf();
        }
    }

    public boolean isRunning() {
        return this.mRunning;
    }

    public void setCurrentGlobalAngle(float f) {
        this.mCurrentGlobalAngle = f;
        invalidateSelf();
    }

    public float getCurrentGlobalAngle() {
        return this.mCurrentGlobalAngle;
    }

    public void setCurrentSweepAngle(float f) {
        this.mCurrentSweepAngle = f;
        invalidateSelf();
    }

    public float getCurrentSweepAngle() {
        return this.mCurrentSweepAngle;
    }

    public void setRingColor(int i) {
        this.mPaint.setColor(i);
        invalidateSelf();
    }

    public void setRingWidth(float f) {
        this.mPaint.setStrokeWidth(f);
        invalidateSelf();
    }
}
