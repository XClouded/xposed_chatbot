package com.alibaba.android.bindingx.core.internal;

import androidx.annotation.NonNull;
import java.util.Map;

class SpringAnimationDriver extends PhysicsAnimationDriver {
    private static final double MAX_DELTA_TIME_SEC = 0.064d;
    private final PhysicsState mCurrentState = new PhysicsState();
    private double mDisplacementFromRestThreshold;
    private double mEndValue;
    private double mInitialVelocity;
    private long mLastTime;
    private boolean mOvershootClampingEnabled;
    private double mRestSpeedThreshold;
    private double mSpringDamping;
    private double mSpringMass;
    private boolean mSpringStarted;
    private double mSpringStiffness;
    private double mStartValue;
    private double mTimeAccumulator;

    SpringAnimationDriver() {
    }

    private static class PhysicsState {
        double position;
        double velocity;

        private PhysicsState() {
        }
    }

    /* access modifiers changed from: package-private */
    public void onAnimationStart(@NonNull Map<String, Object> map) {
        PhysicsState physicsState = this.mCurrentState;
        double doubleValue = Utils.getDoubleValue(map, "initialVelocity", 0.0d);
        physicsState.velocity = doubleValue;
        this.mVelocity = doubleValue;
        this.mSpringStiffness = Utils.getDoubleValue(map, "stiffness", 100.0d);
        this.mSpringDamping = Utils.getDoubleValue(map, "damping", 10.0d);
        this.mSpringMass = Utils.getDoubleValue(map, "mass", 1.0d);
        this.mInitialVelocity = this.mCurrentState.velocity;
        this.mValue = Utils.getDoubleValue(map, "fromValue", 0.0d);
        this.mEndValue = Utils.getDoubleValue(map, "toValue", 1.0d);
        this.mRestSpeedThreshold = Utils.getDoubleValue(map, "restSpeedThreshold", 0.001d);
        this.mDisplacementFromRestThreshold = Utils.getDoubleValue(map, "restDisplacementThreshold", 0.001d);
        this.mOvershootClampingEnabled = Utils.getBooleanValue(map, "overshootClamping", false);
        this.mHasFinished = false;
        this.mTimeAccumulator = 0.0d;
        this.mSpringStarted = false;
    }

    /* access modifiers changed from: package-private */
    public void runAnimationStep(long j) {
        if (!this.mSpringStarted) {
            PhysicsState physicsState = this.mCurrentState;
            double d = this.mValue;
            physicsState.position = d;
            this.mStartValue = d;
            this.mLastTime = j;
            this.mTimeAccumulator = 0.0d;
            this.mSpringStarted = true;
        }
        double d2 = (double) (j - this.mLastTime);
        Double.isNaN(d2);
        advance(d2 / 1000.0d);
        this.mLastTime = j;
        this.mValue = this.mCurrentState.position;
        this.mVelocity = this.mCurrentState.velocity;
        if (isAtRest()) {
            this.mHasFinished = true;
        }
    }

    /* access modifiers changed from: package-private */
    public boolean isAtRest() {
        return Math.abs(this.mCurrentState.velocity) <= this.mRestSpeedThreshold && (getDisplacementDistanceForState(this.mCurrentState) <= this.mDisplacementFromRestThreshold || this.mSpringStiffness == 0.0d);
    }

    private double getDisplacementDistanceForState(PhysicsState physicsState) {
        return Math.abs(this.mEndValue - physicsState.position);
    }

    private boolean isOvershooting() {
        return this.mSpringStiffness > 0.0d && ((this.mStartValue < this.mEndValue && this.mCurrentState.position > this.mEndValue) || (this.mStartValue > this.mEndValue && this.mCurrentState.position < this.mEndValue));
    }

    private void advance(double d) {
        double d2;
        double d3;
        if (!isAtRest()) {
            double d4 = MAX_DELTA_TIME_SEC;
            if (d <= MAX_DELTA_TIME_SEC) {
                d4 = d;
            }
            this.mTimeAccumulator += d4;
            double d5 = this.mSpringDamping;
            double d6 = this.mSpringMass;
            double d7 = this.mSpringStiffness;
            double d8 = -this.mInitialVelocity;
            double sqrt = d5 / (Math.sqrt(d7 * d6) * 2.0d);
            double sqrt2 = Math.sqrt(d7 / d6);
            double sqrt3 = Math.sqrt(1.0d - (sqrt * sqrt)) * sqrt2;
            double d9 = this.mEndValue - this.mStartValue;
            double d10 = this.mTimeAccumulator;
            if (sqrt < 1.0d) {
                double exp = Math.exp((-sqrt) * sqrt2 * d10);
                double d11 = sqrt * sqrt2;
                double d12 = d8 + (d11 * d9);
                double d13 = d10 * sqrt3;
                double sin = this.mEndValue - ((((d12 / sqrt3) * Math.sin(d13)) + (Math.cos(d13) * d9)) * exp);
                d3 = ((d11 * exp) * (((Math.sin(d13) * d12) / sqrt3) + (Math.cos(d13) * d9))) - (((Math.cos(d13) * d12) - ((sqrt3 * d9) * Math.sin(d13))) * exp);
                d2 = sin;
            } else {
                double exp2 = Math.exp((-sqrt2) * d10);
                d2 = this.mEndValue - (((((sqrt2 * d9) + d8) * d10) + d9) * exp2);
                d3 = exp2 * ((d8 * ((d10 * sqrt2) - 1.0d)) + (d10 * d9 * sqrt2 * sqrt2));
            }
            this.mCurrentState.position = d2;
            this.mCurrentState.velocity = d3;
            if (isAtRest() || (this.mOvershootClampingEnabled && isOvershooting())) {
                if (this.mSpringStiffness > 0.0d) {
                    this.mStartValue = this.mEndValue;
                    this.mCurrentState.position = this.mEndValue;
                } else {
                    this.mEndValue = this.mCurrentState.position;
                    this.mStartValue = this.mEndValue;
                }
                this.mCurrentState.velocity = 0.0d;
            }
        }
    }
}
