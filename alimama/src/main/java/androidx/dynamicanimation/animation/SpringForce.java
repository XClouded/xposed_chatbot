package androidx.dynamicanimation.animation;

import androidx.annotation.FloatRange;
import androidx.annotation.RestrictTo;
import androidx.dynamicanimation.animation.DynamicAnimation;

public final class SpringForce implements Force {
    public static final float DAMPING_RATIO_HIGH_BOUNCY = 0.2f;
    public static final float DAMPING_RATIO_LOW_BOUNCY = 0.75f;
    public static final float DAMPING_RATIO_MEDIUM_BOUNCY = 0.5f;
    public static final float DAMPING_RATIO_NO_BOUNCY = 1.0f;
    public static final float STIFFNESS_HIGH = 10000.0f;
    public static final float STIFFNESS_LOW = 200.0f;
    public static final float STIFFNESS_MEDIUM = 1500.0f;
    public static final float STIFFNESS_VERY_LOW = 50.0f;
    private static final double UNSET = Double.MAX_VALUE;
    private static final double VELOCITY_THRESHOLD_MULTIPLIER = 62.5d;
    private double mDampedFreq;
    double mDampingRatio = 0.5d;
    private double mFinalPosition = UNSET;
    private double mGammaMinus;
    private double mGammaPlus;
    private boolean mInitialized = false;
    private final DynamicAnimation.MassState mMassState = new DynamicAnimation.MassState();
    double mNaturalFreq = Math.sqrt(1500.0d);
    private double mValueThreshold;
    private double mVelocityThreshold;

    public SpringForce() {
    }

    public SpringForce(float f) {
        this.mFinalPosition = (double) f;
    }

    public SpringForce setStiffness(@FloatRange(from = 0.0d, fromInclusive = false) float f) {
        if (f > 0.0f) {
            this.mNaturalFreq = Math.sqrt((double) f);
            this.mInitialized = false;
            return this;
        }
        throw new IllegalArgumentException("Spring stiffness constant must be positive.");
    }

    public float getStiffness() {
        return (float) (this.mNaturalFreq * this.mNaturalFreq);
    }

    public SpringForce setDampingRatio(@FloatRange(from = 0.0d) float f) {
        if (f >= 0.0f) {
            this.mDampingRatio = (double) f;
            this.mInitialized = false;
            return this;
        }
        throw new IllegalArgumentException("Damping ratio must be non-negative");
    }

    public float getDampingRatio() {
        return (float) this.mDampingRatio;
    }

    public SpringForce setFinalPosition(float f) {
        this.mFinalPosition = (double) f;
        return this;
    }

    public float getFinalPosition() {
        return (float) this.mFinalPosition;
    }

    @RestrictTo({RestrictTo.Scope.LIBRARY})
    public float getAcceleration(float f, float f2) {
        float finalPosition = f - getFinalPosition();
        double d = this.mNaturalFreq * this.mNaturalFreq;
        double d2 = this.mNaturalFreq * 2.0d * this.mDampingRatio;
        double d3 = (double) finalPosition;
        Double.isNaN(d3);
        double d4 = (double) f2;
        Double.isNaN(d4);
        return (float) (((-d) * d3) - (d2 * d4));
    }

    @RestrictTo({RestrictTo.Scope.LIBRARY})
    public boolean isAtEquilibrium(float f, float f2) {
        return ((double) Math.abs(f2)) < this.mVelocityThreshold && ((double) Math.abs(f - getFinalPosition())) < this.mValueThreshold;
    }

    private void init() {
        if (!this.mInitialized) {
            if (this.mFinalPosition != UNSET) {
                if (this.mDampingRatio > 1.0d) {
                    this.mGammaPlus = ((-this.mDampingRatio) * this.mNaturalFreq) + (this.mNaturalFreq * Math.sqrt((this.mDampingRatio * this.mDampingRatio) - 1.0d));
                    this.mGammaMinus = ((-this.mDampingRatio) * this.mNaturalFreq) - (this.mNaturalFreq * Math.sqrt((this.mDampingRatio * this.mDampingRatio) - 1.0d));
                } else if (this.mDampingRatio >= 0.0d && this.mDampingRatio < 1.0d) {
                    this.mDampedFreq = this.mNaturalFreq * Math.sqrt(1.0d - (this.mDampingRatio * this.mDampingRatio));
                }
                this.mInitialized = true;
                return;
            }
            throw new IllegalStateException("Error: Final position of the spring must be set before the animation starts");
        }
    }

    /* access modifiers changed from: package-private */
    public DynamicAnimation.MassState updateValues(double d, double d2, long j) {
        double d3;
        double d4;
        init();
        double d5 = (double) j;
        Double.isNaN(d5);
        double d6 = d5 / 1000.0d;
        double d7 = d - this.mFinalPosition;
        if (this.mDampingRatio > 1.0d) {
            double d8 = d7 - (((this.mGammaMinus * d7) - d2) / (this.mGammaMinus - this.mGammaPlus));
            double d9 = ((this.mGammaMinus * d7) - d2) / (this.mGammaMinus - this.mGammaPlus);
            d4 = (Math.pow(2.718281828459045d, this.mGammaMinus * d6) * d8) + (Math.pow(2.718281828459045d, this.mGammaPlus * d6) * d9);
            d3 = (d8 * this.mGammaMinus * Math.pow(2.718281828459045d, this.mGammaMinus * d6)) + (d9 * this.mGammaPlus * Math.pow(2.718281828459045d, this.mGammaPlus * d6));
        } else if (this.mDampingRatio == 1.0d) {
            double d10 = d2 + (this.mNaturalFreq * d7);
            double d11 = d7 + (d10 * d6);
            double pow = Math.pow(2.718281828459045d, (-this.mNaturalFreq) * d6) * d11;
            double pow2 = (d10 * Math.pow(2.718281828459045d, (-this.mNaturalFreq) * d6)) + (d11 * Math.pow(2.718281828459045d, (-this.mNaturalFreq) * d6) * (-this.mNaturalFreq));
            d4 = pow;
            d3 = pow2;
        } else {
            double d12 = (1.0d / this.mDampedFreq) * ((this.mDampingRatio * this.mNaturalFreq * d7) + d2);
            d4 = Math.pow(2.718281828459045d, (-this.mDampingRatio) * this.mNaturalFreq * d6) * ((Math.cos(this.mDampedFreq * d6) * d7) + (Math.sin(this.mDampedFreq * d6) * d12));
            d3 = ((-this.mNaturalFreq) * d4 * this.mDampingRatio) + (Math.pow(2.718281828459045d, (-this.mDampingRatio) * this.mNaturalFreq * d6) * (((-this.mDampedFreq) * d7 * Math.sin(this.mDampedFreq * d6)) + (this.mDampedFreq * d12 * Math.cos(this.mDampedFreq * d6))));
        }
        this.mMassState.mValue = (float) (d4 + this.mFinalPosition);
        this.mMassState.mVelocity = (float) d3;
        return this.mMassState;
    }

    /* access modifiers changed from: package-private */
    public void setValueThreshold(double d) {
        this.mValueThreshold = Math.abs(d);
        this.mVelocityThreshold = this.mValueThreshold * VELOCITY_THRESHOLD_MULTIPLIER;
    }
}
