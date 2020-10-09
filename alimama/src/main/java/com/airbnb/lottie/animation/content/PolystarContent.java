package com.airbnb.lottie.animation.content;

import android.graphics.Path;
import android.graphics.PointF;
import androidx.annotation.Nullable;
import com.airbnb.lottie.LottieDrawable;
import com.airbnb.lottie.LottieProperty;
import com.airbnb.lottie.animation.keyframe.BaseKeyframeAnimation;
import com.airbnb.lottie.model.KeyPath;
import com.airbnb.lottie.model.content.PolystarShape;
import com.airbnb.lottie.model.content.ShapeTrimPath;
import com.airbnb.lottie.model.layer.BaseLayer;
import com.airbnb.lottie.utils.MiscUtils;
import com.airbnb.lottie.value.LottieValueCallback;
import java.util.List;

public class PolystarContent implements PathContent, BaseKeyframeAnimation.AnimationListener, KeyPathElementContent {
    private static final float POLYGON_MAGIC_NUMBER = 0.25f;
    private static final float POLYSTAR_MAGIC_NUMBER = 0.47829f;
    private final boolean hidden;
    @Nullable
    private final BaseKeyframeAnimation<?, Float> innerRadiusAnimation;
    @Nullable
    private final BaseKeyframeAnimation<?, Float> innerRoundednessAnimation;
    private boolean isPathValid;
    private final LottieDrawable lottieDrawable;
    private final String name;
    private final BaseKeyframeAnimation<?, Float> outerRadiusAnimation;
    private final BaseKeyframeAnimation<?, Float> outerRoundednessAnimation;
    private final Path path = new Path();
    private final BaseKeyframeAnimation<?, Float> pointsAnimation;
    private final BaseKeyframeAnimation<?, PointF> positionAnimation;
    private final BaseKeyframeAnimation<?, Float> rotationAnimation;
    private CompoundTrimPathContent trimPaths = new CompoundTrimPathContent();
    private final PolystarShape.Type type;

    public PolystarContent(LottieDrawable lottieDrawable2, BaseLayer baseLayer, PolystarShape polystarShape) {
        this.lottieDrawable = lottieDrawable2;
        this.name = polystarShape.getName();
        this.type = polystarShape.getType();
        this.hidden = polystarShape.isHidden();
        this.pointsAnimation = polystarShape.getPoints().createAnimation();
        this.positionAnimation = polystarShape.getPosition().createAnimation();
        this.rotationAnimation = polystarShape.getRotation().createAnimation();
        this.outerRadiusAnimation = polystarShape.getOuterRadius().createAnimation();
        this.outerRoundednessAnimation = polystarShape.getOuterRoundedness().createAnimation();
        if (this.type == PolystarShape.Type.STAR) {
            this.innerRadiusAnimation = polystarShape.getInnerRadius().createAnimation();
            this.innerRoundednessAnimation = polystarShape.getInnerRoundedness().createAnimation();
        } else {
            this.innerRadiusAnimation = null;
            this.innerRoundednessAnimation = null;
        }
        baseLayer.addAnimation(this.pointsAnimation);
        baseLayer.addAnimation(this.positionAnimation);
        baseLayer.addAnimation(this.rotationAnimation);
        baseLayer.addAnimation(this.outerRadiusAnimation);
        baseLayer.addAnimation(this.outerRoundednessAnimation);
        if (this.type == PolystarShape.Type.STAR) {
            baseLayer.addAnimation(this.innerRadiusAnimation);
            baseLayer.addAnimation(this.innerRoundednessAnimation);
        }
        this.pointsAnimation.addUpdateListener(this);
        this.positionAnimation.addUpdateListener(this);
        this.rotationAnimation.addUpdateListener(this);
        this.outerRadiusAnimation.addUpdateListener(this);
        this.outerRoundednessAnimation.addUpdateListener(this);
        if (this.type == PolystarShape.Type.STAR) {
            this.innerRadiusAnimation.addUpdateListener(this);
            this.innerRoundednessAnimation.addUpdateListener(this);
        }
    }

    public void onValueChanged() {
        invalidate();
    }

    private void invalidate() {
        this.isPathValid = false;
        this.lottieDrawable.invalidateSelf();
    }

    public void setContents(List<Content> list, List<Content> list2) {
        for (int i = 0; i < list.size(); i++) {
            Content content = list.get(i);
            if (content instanceof TrimPathContent) {
                TrimPathContent trimPathContent = (TrimPathContent) content;
                if (trimPathContent.getType() == ShapeTrimPath.Type.SIMULTANEOUSLY) {
                    this.trimPaths.addTrimPath(trimPathContent);
                    trimPathContent.addListener(this);
                }
            }
        }
    }

    public Path getPath() {
        if (this.isPathValid) {
            return this.path;
        }
        this.path.reset();
        if (this.hidden) {
            this.isPathValid = true;
            return this.path;
        }
        switch (this.type) {
            case STAR:
                createStarPath();
                break;
            case POLYGON:
                createPolygonPath();
                break;
        }
        this.path.close();
        this.trimPaths.apply(this.path);
        this.isPathValid = true;
        return this.path;
    }

    public String getName() {
        return this.name;
    }

    private void createStarPath() {
        float f;
        double d;
        float f2;
        float f3;
        float f4;
        double d2;
        float f5;
        float f6;
        float f7;
        float f8;
        float f9;
        float floatValue = this.pointsAnimation.getValue().floatValue();
        double radians = Math.toRadians((this.rotationAnimation == null ? 0.0d : (double) this.rotationAnimation.getValue().floatValue()) - 90.0d);
        double d3 = (double) floatValue;
        Double.isNaN(d3);
        float f10 = (float) (6.283185307179586d / d3);
        float f11 = f10 / 2.0f;
        float f12 = floatValue - ((float) ((int) floatValue));
        if (f12 != 0.0f) {
            double d4 = (double) ((1.0f - f12) * f11);
            Double.isNaN(d4);
            radians += d4;
        }
        float floatValue2 = this.outerRadiusAnimation.getValue().floatValue();
        float floatValue3 = this.innerRadiusAnimation.getValue().floatValue();
        float floatValue4 = this.innerRoundednessAnimation != null ? this.innerRoundednessAnimation.getValue().floatValue() / 100.0f : 0.0f;
        float floatValue5 = this.outerRoundednessAnimation != null ? this.outerRoundednessAnimation.getValue().floatValue() / 100.0f : 0.0f;
        if (f12 != 0.0f) {
            f2 = ((floatValue2 - floatValue3) * f12) + floatValue3;
            double d5 = (double) f2;
            double cos = Math.cos(radians);
            Double.isNaN(d5);
            d = d3;
            f4 = (float) (d5 * cos);
            double sin = Math.sin(radians);
            Double.isNaN(d5);
            f3 = (float) (d5 * sin);
            this.path.moveTo(f4, f3);
            double d6 = (double) ((f10 * f12) / 2.0f);
            Double.isNaN(d6);
            d2 = radians + d6;
            f = floatValue2;
        } else {
            d = d3;
            float f13 = floatValue2;
            double d7 = (double) f13;
            double cos2 = Math.cos(radians);
            Double.isNaN(d7);
            float f14 = (float) (cos2 * d7);
            double sin2 = Math.sin(radians);
            Double.isNaN(d7);
            float f15 = (float) (d7 * sin2);
            this.path.moveTo(f14, f15);
            f = f13;
            double d8 = (double) f11;
            Double.isNaN(d8);
            d2 = radians + d8;
            f4 = f14;
            f3 = f15;
            f2 = 0.0f;
        }
        double ceil = Math.ceil(d) * 2.0d;
        int i = 0;
        double d9 = d2;
        float f16 = f4;
        float f17 = f3;
        boolean z = false;
        while (true) {
            double d10 = (double) i;
            if (d10 < ceil) {
                float f18 = z ? f : floatValue3;
                float f19 = (f2 == 0.0f || d10 != ceil - 2.0d) ? f11 : (f10 * f12) / 2.0f;
                if (f2 == 0.0f || d10 != ceil - 1.0d) {
                    f5 = f10;
                } else {
                    f5 = f10;
                    f18 = f2;
                }
                double d11 = (double) f18;
                double cos3 = Math.cos(d9);
                Double.isNaN(d11);
                float f20 = f2;
                float f21 = f19;
                float f22 = (float) (d11 * cos3);
                double sin3 = Math.sin(d9);
                Double.isNaN(d11);
                float f23 = (float) (d11 * sin3);
                if (floatValue4 == 0.0f && floatValue5 == 0.0f) {
                    this.path.lineTo(f22, f23);
                    f8 = floatValue3;
                    f7 = floatValue4;
                    f6 = floatValue5;
                    f9 = f21;
                } else {
                    double d12 = d10;
                    float f24 = f17;
                    f8 = floatValue3;
                    f7 = floatValue4;
                    float f25 = f16;
                    double atan2 = (double) ((float) (Math.atan2((double) f24, (double) f25) - 1.5707963267948966d));
                    float cos4 = (float) Math.cos(atan2);
                    float sin4 = (float) Math.sin(atan2);
                    f6 = floatValue5;
                    float f26 = f25;
                    double atan22 = (double) ((float) (Math.atan2((double) f23, (double) f22) - 1.5707963267948966d));
                    float cos5 = (float) Math.cos(atan22);
                    float sin5 = (float) Math.sin(atan22);
                    float f27 = z ? f7 : f6;
                    float f28 = z ? f6 : f7;
                    float f29 = z ? f8 : f;
                    float f30 = z ? f : f8;
                    float f31 = f29 * f27 * POLYSTAR_MAGIC_NUMBER;
                    float f32 = cos4 * f31;
                    float f33 = f31 * sin4;
                    float f34 = f30 * f28 * POLYSTAR_MAGIC_NUMBER;
                    float f35 = cos5 * f34;
                    float f36 = f34 * sin5;
                    if (f12 != 0.0f) {
                        if (i == 0) {
                            f32 *= f12;
                            f33 *= f12;
                        } else if (d12 == ceil - 1.0d) {
                            f35 *= f12;
                            f36 *= f12;
                        }
                    }
                    this.path.cubicTo(f26 - f32, f24 - f33, f22 + f35, f23 + f36, f22, f23);
                    f9 = f21;
                }
                double d13 = (double) f9;
                Double.isNaN(d13);
                d9 += d13;
                z = !z;
                i++;
                f17 = f23;
                f16 = f22;
                f10 = f5;
                f2 = f20;
                floatValue3 = f8;
                floatValue4 = f7;
                floatValue5 = f6;
            } else {
                PointF value = this.positionAnimation.getValue();
                this.path.offset(value.x, value.y);
                this.path.close();
                return;
            }
        }
    }

    private void createPolygonPath() {
        double d;
        double d2;
        int i;
        double d3;
        int floor = (int) Math.floor((double) this.pointsAnimation.getValue().floatValue());
        double radians = Math.toRadians((this.rotationAnimation == null ? 0.0d : (double) this.rotationAnimation.getValue().floatValue()) - 90.0d);
        double d4 = (double) floor;
        Double.isNaN(d4);
        float floatValue = this.outerRoundednessAnimation.getValue().floatValue() / 100.0f;
        float floatValue2 = this.outerRadiusAnimation.getValue().floatValue();
        double d5 = (double) floatValue2;
        double cos = Math.cos(radians);
        Double.isNaN(d5);
        float f = (float) (cos * d5);
        double sin = Math.sin(radians);
        Double.isNaN(d5);
        float f2 = (float) (sin * d5);
        this.path.moveTo(f, f2);
        double d6 = (double) ((float) (6.283185307179586d / d4));
        Double.isNaN(d6);
        double d7 = radians + d6;
        double ceil = Math.ceil(d4);
        int i2 = 0;
        while (((double) i2) < ceil) {
            double cos2 = Math.cos(d7);
            Double.isNaN(d5);
            float f3 = (float) (cos2 * d5);
            double sin2 = Math.sin(d7);
            Double.isNaN(d5);
            double d8 = ceil;
            float f4 = (float) (d5 * sin2);
            if (floatValue != 0.0f) {
                d3 = d5;
                i = i2;
                d2 = d7;
                double atan2 = (double) ((float) (Math.atan2((double) f2, (double) f) - 1.5707963267948966d));
                float cos3 = (float) Math.cos(atan2);
                d = d6;
                double atan22 = (double) ((float) (Math.atan2((double) f4, (double) f3) - 1.5707963267948966d));
                float f5 = floatValue2 * floatValue * POLYGON_MAGIC_NUMBER;
                this.path.cubicTo(f - (cos3 * f5), f2 - (((float) Math.sin(atan2)) * f5), f3 + (((float) Math.cos(atan22)) * f5), f4 + (f5 * ((float) Math.sin(atan22))), f3, f4);
            } else {
                i = i2;
                d2 = d7;
                d3 = d5;
                d = d6;
                this.path.lineTo(f3, f4);
            }
            Double.isNaN(d);
            d7 = d2 + d;
            i2 = i + 1;
            f2 = f4;
            f = f3;
            ceil = d8;
            d5 = d3;
            d6 = d;
        }
        PointF value = this.positionAnimation.getValue();
        this.path.offset(value.x, value.y);
        this.path.close();
    }

    public void resolveKeyPath(KeyPath keyPath, int i, List<KeyPath> list, KeyPath keyPath2) {
        MiscUtils.resolveKeyPath(keyPath, i, list, keyPath2, this);
    }

    public <T> void addValueCallback(T t, @Nullable LottieValueCallback<T> lottieValueCallback) {
        if (t == LottieProperty.POLYSTAR_POINTS) {
            this.pointsAnimation.setValueCallback(lottieValueCallback);
        } else if (t == LottieProperty.POLYSTAR_ROTATION) {
            this.rotationAnimation.setValueCallback(lottieValueCallback);
        } else if (t == LottieProperty.POSITION) {
            this.positionAnimation.setValueCallback(lottieValueCallback);
        } else if (t == LottieProperty.POLYSTAR_INNER_RADIUS && this.innerRadiusAnimation != null) {
            this.innerRadiusAnimation.setValueCallback(lottieValueCallback);
        } else if (t == LottieProperty.POLYSTAR_OUTER_RADIUS) {
            this.outerRadiusAnimation.setValueCallback(lottieValueCallback);
        } else if (t == LottieProperty.POLYSTAR_INNER_ROUNDEDNESS && this.innerRoundednessAnimation != null) {
            this.innerRoundednessAnimation.setValueCallback(lottieValueCallback);
        } else if (t == LottieProperty.POLYSTAR_OUTER_ROUNDEDNESS) {
            this.outerRoundednessAnimation.setValueCallback(lottieValueCallback);
        }
    }
}
