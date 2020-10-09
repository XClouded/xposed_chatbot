package com.airbnb.lottie.animation.keyframe;

import android.graphics.Matrix;
import android.graphics.PointF;
import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import com.airbnb.lottie.LottieProperty;
import com.airbnb.lottie.animation.keyframe.BaseKeyframeAnimation;
import com.airbnb.lottie.model.animatable.AnimatableTransform;
import com.airbnb.lottie.model.layer.BaseLayer;
import com.airbnb.lottie.value.Keyframe;
import com.airbnb.lottie.value.LottieValueCallback;
import com.airbnb.lottie.value.ScaleXY;
import java.util.Collections;

public class TransformKeyframeAnimation {
    @NonNull
    private BaseKeyframeAnimation<PointF, PointF> anchorPoint;
    @Nullable
    private BaseKeyframeAnimation<?, Float> endOpacity;
    private final Matrix matrix = new Matrix();
    @NonNull
    private BaseKeyframeAnimation<Integer, Integer> opacity;
    @NonNull
    private BaseKeyframeAnimation<?, PointF> position;
    @NonNull
    private BaseKeyframeAnimation<Float, Float> rotation;
    @NonNull
    private BaseKeyframeAnimation<ScaleXY, ScaleXY> scale;
    @Nullable
    private FloatKeyframeAnimation skew;
    @Nullable
    private FloatKeyframeAnimation skewAngle;
    private final Matrix skewMatrix1;
    private final Matrix skewMatrix2;
    private final Matrix skewMatrix3;
    private final float[] skewValues;
    @Nullable
    private BaseKeyframeAnimation<?, Float> startOpacity;

    public TransformKeyframeAnimation(AnimatableTransform animatableTransform) {
        this.anchorPoint = animatableTransform.getAnchorPoint() == null ? null : animatableTransform.getAnchorPoint().createAnimation();
        this.position = animatableTransform.getPosition() == null ? null : animatableTransform.getPosition().createAnimation();
        this.scale = animatableTransform.getScale() == null ? null : animatableTransform.getScale().createAnimation();
        this.rotation = animatableTransform.getRotation() == null ? null : animatableTransform.getRotation().createAnimation();
        this.skew = animatableTransform.getSkew() == null ? null : (FloatKeyframeAnimation) animatableTransform.getSkew().createAnimation();
        if (this.skew != null) {
            this.skewMatrix1 = new Matrix();
            this.skewMatrix2 = new Matrix();
            this.skewMatrix3 = new Matrix();
            this.skewValues = new float[9];
        } else {
            this.skewMatrix1 = null;
            this.skewMatrix2 = null;
            this.skewMatrix3 = null;
            this.skewValues = null;
        }
        this.skewAngle = animatableTransform.getSkewAngle() == null ? null : (FloatKeyframeAnimation) animatableTransform.getSkewAngle().createAnimation();
        if (animatableTransform.getOpacity() != null) {
            this.opacity = animatableTransform.getOpacity().createAnimation();
        }
        if (animatableTransform.getStartOpacity() != null) {
            this.startOpacity = animatableTransform.getStartOpacity().createAnimation();
        } else {
            this.startOpacity = null;
        }
        if (animatableTransform.getEndOpacity() != null) {
            this.endOpacity = animatableTransform.getEndOpacity().createAnimation();
        } else {
            this.endOpacity = null;
        }
    }

    public void addAnimationsToLayer(BaseLayer baseLayer) {
        baseLayer.addAnimation(this.opacity);
        baseLayer.addAnimation(this.startOpacity);
        baseLayer.addAnimation(this.endOpacity);
        baseLayer.addAnimation(this.anchorPoint);
        baseLayer.addAnimation(this.position);
        baseLayer.addAnimation(this.scale);
        baseLayer.addAnimation(this.rotation);
        baseLayer.addAnimation(this.skew);
        baseLayer.addAnimation(this.skewAngle);
    }

    public void addListener(BaseKeyframeAnimation.AnimationListener animationListener) {
        if (this.opacity != null) {
            this.opacity.addUpdateListener(animationListener);
        }
        if (this.startOpacity != null) {
            this.startOpacity.addUpdateListener(animationListener);
        }
        if (this.endOpacity != null) {
            this.endOpacity.addUpdateListener(animationListener);
        }
        if (this.anchorPoint != null) {
            this.anchorPoint.addUpdateListener(animationListener);
        }
        if (this.position != null) {
            this.position.addUpdateListener(animationListener);
        }
        if (this.scale != null) {
            this.scale.addUpdateListener(animationListener);
        }
        if (this.rotation != null) {
            this.rotation.addUpdateListener(animationListener);
        }
        if (this.skew != null) {
            this.skew.addUpdateListener(animationListener);
        }
        if (this.skewAngle != null) {
            this.skewAngle.addUpdateListener(animationListener);
        }
    }

    public void setProgress(float f) {
        if (this.opacity != null) {
            this.opacity.setProgress(f);
        }
        if (this.startOpacity != null) {
            this.startOpacity.setProgress(f);
        }
        if (this.endOpacity != null) {
            this.endOpacity.setProgress(f);
        }
        if (this.anchorPoint != null) {
            this.anchorPoint.setProgress(f);
        }
        if (this.position != null) {
            this.position.setProgress(f);
        }
        if (this.scale != null) {
            this.scale.setProgress(f);
        }
        if (this.rotation != null) {
            this.rotation.setProgress(f);
        }
        if (this.skew != null) {
            this.skew.setProgress(f);
        }
        if (this.skewAngle != null) {
            this.skewAngle.setProgress(f);
        }
    }

    @Nullable
    public BaseKeyframeAnimation<?, Integer> getOpacity() {
        return this.opacity;
    }

    @Nullable
    public BaseKeyframeAnimation<?, Float> getStartOpacity() {
        return this.startOpacity;
    }

    @Nullable
    public BaseKeyframeAnimation<?, Float> getEndOpacity() {
        return this.endOpacity;
    }

    public Matrix getMatrix() {
        float f;
        this.matrix.reset();
        if (this.position != null) {
            PointF value = this.position.getValue();
            if (!(value.x == 0.0f && value.y == 0.0f)) {
                this.matrix.preTranslate(value.x, value.y);
            }
        }
        if (this.rotation != null) {
            if (this.rotation instanceof ValueCallbackKeyframeAnimation) {
                f = this.rotation.getValue().floatValue();
            } else {
                f = ((FloatKeyframeAnimation) this.rotation).getFloatValue();
            }
            if (f != 0.0f) {
                this.matrix.preRotate(f);
            }
        }
        if (this.skew != null) {
            float cos = this.skewAngle == null ? 0.0f : (float) Math.cos(Math.toRadians((double) ((-this.skewAngle.getFloatValue()) + 90.0f)));
            float sin = this.skewAngle == null ? 1.0f : (float) Math.sin(Math.toRadians((double) ((-this.skewAngle.getFloatValue()) + 90.0f)));
            clearSkewValues();
            this.skewValues[0] = cos;
            this.skewValues[1] = sin;
            float f2 = -sin;
            this.skewValues[3] = f2;
            this.skewValues[4] = cos;
            this.skewValues[8] = 1.0f;
            this.skewMatrix1.setValues(this.skewValues);
            clearSkewValues();
            this.skewValues[0] = 1.0f;
            this.skewValues[3] = (float) Math.tan(Math.toRadians((double) this.skew.getFloatValue()));
            this.skewValues[4] = 1.0f;
            this.skewValues[8] = 1.0f;
            this.skewMatrix2.setValues(this.skewValues);
            clearSkewValues();
            this.skewValues[0] = cos;
            this.skewValues[1] = f2;
            this.skewValues[3] = sin;
            this.skewValues[4] = cos;
            this.skewValues[8] = 1.0f;
            this.skewMatrix3.setValues(this.skewValues);
            this.skewMatrix2.preConcat(this.skewMatrix1);
            this.skewMatrix3.preConcat(this.skewMatrix2);
            this.matrix.preConcat(this.skewMatrix3);
        }
        if (this.scale != null) {
            ScaleXY value2 = this.scale.getValue();
            if (!(value2.getScaleX() == 1.0f && value2.getScaleY() == 1.0f)) {
                this.matrix.preScale(value2.getScaleX(), value2.getScaleY());
            }
        }
        if (this.anchorPoint != null) {
            PointF value3 = this.anchorPoint.getValue();
            if (!(value3.x == 0.0f && value3.y == 0.0f)) {
                this.matrix.preTranslate(-value3.x, -value3.y);
            }
        }
        return this.matrix;
    }

    private void clearSkewValues() {
        for (int i = 0; i < 9; i++) {
            this.skewValues[i] = 0.0f;
        }
    }

    public Matrix getMatrixForRepeater(float f) {
        PointF pointF = null;
        PointF value = this.position == null ? null : this.position.getValue();
        ScaleXY value2 = this.scale == null ? null : this.scale.getValue();
        this.matrix.reset();
        if (value != null) {
            this.matrix.preTranslate(value.x * f, value.y * f);
        }
        if (value2 != null) {
            double d = (double) f;
            this.matrix.preScale((float) Math.pow((double) value2.getScaleX(), d), (float) Math.pow((double) value2.getScaleY(), d));
        }
        if (this.rotation != null) {
            float floatValue = this.rotation.getValue().floatValue();
            if (this.anchorPoint != null) {
                pointF = this.anchorPoint.getValue();
            }
            Matrix matrix2 = this.matrix;
            float f2 = floatValue * f;
            float f3 = 0.0f;
            float f4 = pointF == null ? 0.0f : pointF.x;
            if (pointF != null) {
                f3 = pointF.y;
            }
            matrix2.preRotate(f2, f4, f3);
        }
        return this.matrix;
    }

    public <T> boolean applyValueCallback(T t, @Nullable LottieValueCallback<T> lottieValueCallback) {
        if (t == LottieProperty.TRANSFORM_ANCHOR_POINT) {
            if (this.anchorPoint == null) {
                this.anchorPoint = new ValueCallbackKeyframeAnimation(lottieValueCallback, new PointF());
                return true;
            }
            this.anchorPoint.setValueCallback(lottieValueCallback);
            return true;
        } else if (t == LottieProperty.TRANSFORM_POSITION) {
            if (this.position == null) {
                this.position = new ValueCallbackKeyframeAnimation(lottieValueCallback, new PointF());
                return true;
            }
            this.position.setValueCallback(lottieValueCallback);
            return true;
        } else if (t == LottieProperty.TRANSFORM_SCALE) {
            if (this.scale == null) {
                this.scale = new ValueCallbackKeyframeAnimation(lottieValueCallback, new ScaleXY());
                return true;
            }
            this.scale.setValueCallback(lottieValueCallback);
            return true;
        } else if (t == LottieProperty.TRANSFORM_ROTATION) {
            if (this.rotation == null) {
                this.rotation = new ValueCallbackKeyframeAnimation(lottieValueCallback, Float.valueOf(0.0f));
                return true;
            }
            this.rotation.setValueCallback(lottieValueCallback);
            return true;
        } else if (t == LottieProperty.TRANSFORM_OPACITY) {
            if (this.opacity == null) {
                this.opacity = new ValueCallbackKeyframeAnimation(lottieValueCallback, 100);
                return true;
            }
            this.opacity.setValueCallback(lottieValueCallback);
            return true;
        } else if (t != LottieProperty.TRANSFORM_START_OPACITY || this.startOpacity == null) {
            if (t != LottieProperty.TRANSFORM_END_OPACITY || this.endOpacity == null) {
                if (t == LottieProperty.TRANSFORM_SKEW && this.skew != null) {
                    if (this.skew == null) {
                        this.skew = new FloatKeyframeAnimation(Collections.singletonList(new Keyframe(Float.valueOf(0.0f))));
                    }
                    this.skew.setValueCallback(lottieValueCallback);
                    return true;
                } else if (t != LottieProperty.TRANSFORM_SKEW_ANGLE || this.skewAngle == null) {
                    return false;
                } else {
                    if (this.skewAngle == null) {
                        this.skewAngle = new FloatKeyframeAnimation(Collections.singletonList(new Keyframe(Float.valueOf(0.0f))));
                    }
                    this.skewAngle.setValueCallback(lottieValueCallback);
                    return true;
                }
            } else if (this.endOpacity == null) {
                this.endOpacity = new ValueCallbackKeyframeAnimation(lottieValueCallback, 100);
                return true;
            } else {
                this.endOpacity.setValueCallback(lottieValueCallback);
                return true;
            }
        } else if (this.startOpacity == null) {
            this.startOpacity = new ValueCallbackKeyframeAnimation(lottieValueCallback, 100);
            return true;
        } else {
            this.startOpacity.setValueCallback(lottieValueCallback);
            return true;
        }
    }
}
