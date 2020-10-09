package com.alibaba.android.bindingx.core.internal;

import androidx.annotation.Nullable;

class OrientationEvaluator {
    private final Euler EULER = new Euler();
    private final Quaternion Q0 = new Quaternion();
    private final Quaternion Q1 = new Quaternion(-Math.sqrt(0.5d), 0.0d, 0.0d, Math.sqrt(0.5d));
    private final Vector3 ZEE = new Vector3(0.0d, 0.0d, 1.0d);
    private Double constraintAlpha = null;
    private double constraintAlphaOffset = 0.0d;
    private Double constraintBeta = null;
    private double constraintBetaOffset = 0.0d;
    private Double constraintGamma = null;
    private double constraintGammaOffset = 0.0d;
    private Quaternion quaternion = new Quaternion(0.0d, 0.0d, 0.0d, 1.0d);

    OrientationEvaluator(@Nullable Double d, @Nullable Double d2, @Nullable Double d3) {
        this.constraintAlpha = d;
        this.constraintBeta = d2;
        this.constraintGamma = d3;
    }

    /* access modifiers changed from: package-private */
    public Quaternion calculate(double d, double d2, double d3, double d4) {
        setObjectQuaternion(this.quaternion, Math.toRadians(this.constraintAlpha != null ? this.constraintAlpha.doubleValue() : d4 + this.constraintAlphaOffset), Math.toRadians(this.constraintBeta != null ? this.constraintBeta.doubleValue() : this.constraintBetaOffset + d2), Math.toRadians(this.constraintGamma != null ? this.constraintGamma.doubleValue() : d3 + this.constraintGammaOffset), 0.0d);
        return this.quaternion;
    }

    private void setObjectQuaternion(Quaternion quaternion2, double d, double d2, double d3, double d4) {
        Quaternion quaternion3 = quaternion2;
        this.EULER.setValue(d2, d, -d3, "YXZ");
        quaternion2.setFromEuler(this.EULER);
        quaternion2.multiply(this.Q1);
        quaternion2.multiply(this.Q0.setFromAxisAngle(this.ZEE, -d4));
    }
}
