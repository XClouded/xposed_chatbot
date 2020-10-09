package com.airbnb.lottie.animation.keyframe;

import androidx.annotation.FloatRange;
import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import com.airbnb.lottie.L;
import com.airbnb.lottie.value.Keyframe;
import com.airbnb.lottie.value.LottieValueCallback;
import java.util.ArrayList;
import java.util.List;

public abstract class BaseKeyframeAnimation<K, A> {
    private float cachedEndProgress = -1.0f;
    @Nullable
    private A cachedGetValue = null;
    private float cachedStartDelayProgress = -1.0f;
    private boolean isDiscrete = false;
    private final KeyframesWrapper<K> keyframesWrapper;
    final List<AnimationListener> listeners = new ArrayList(1);
    private float progress = 0.0f;
    @Nullable
    protected LottieValueCallback<A> valueCallback;

    public interface AnimationListener {
        void onValueChanged();
    }

    private interface KeyframesWrapper<T> {
        Keyframe<T> getCurrentKeyframe();

        @FloatRange(from = 0.0d, to = 1.0d)
        float getEndProgress();

        @FloatRange(from = 0.0d, to = 1.0d)
        float getStartDelayProgress();

        boolean isCachedValueEnabled(float f);

        boolean isEmpty();

        boolean isValueChanged(float f);
    }

    /* access modifiers changed from: package-private */
    public abstract A getValue(Keyframe<K> keyframe, float f);

    BaseKeyframeAnimation(List<? extends Keyframe<K>> list) {
        this.keyframesWrapper = wrap(list);
    }

    public void setIsDiscrete() {
        this.isDiscrete = true;
    }

    public void addUpdateListener(AnimationListener animationListener) {
        this.listeners.add(animationListener);
    }

    public void setProgress(@FloatRange(from = 0.0d, to = 1.0d) float f) {
        if (!this.keyframesWrapper.isEmpty()) {
            if (f < getStartDelayProgress()) {
                f = getStartDelayProgress();
            } else if (f > getEndProgress()) {
                f = getEndProgress();
            }
            if (f != this.progress) {
                this.progress = f;
                if (this.keyframesWrapper.isValueChanged(f)) {
                    notifyListeners();
                }
            }
        }
    }

    public void notifyListeners() {
        for (int i = 0; i < this.listeners.size(); i++) {
            this.listeners.get(i).onValueChanged();
        }
    }

    /* access modifiers changed from: protected */
    public Keyframe<K> getCurrentKeyframe() {
        L.beginSection("BaseKeyframeAnimation#getCurrentKeyframe");
        Keyframe<K> currentKeyframe = this.keyframesWrapper.getCurrentKeyframe();
        L.endSection("BaseKeyframeAnimation#getCurrentKeyframe");
        return currentKeyframe;
    }

    /* access modifiers changed from: package-private */
    public float getLinearCurrentKeyframeProgress() {
        if (this.isDiscrete) {
            return 0.0f;
        }
        Keyframe currentKeyframe = getCurrentKeyframe();
        if (currentKeyframe.isStatic()) {
            return 0.0f;
        }
        return (this.progress - currentKeyframe.getStartProgress()) / (currentKeyframe.getEndProgress() - currentKeyframe.getStartProgress());
    }

    /* access modifiers changed from: protected */
    public float getInterpolatedCurrentKeyframeProgress() {
        Keyframe currentKeyframe = getCurrentKeyframe();
        if (currentKeyframe.isStatic()) {
            return 0.0f;
        }
        return currentKeyframe.interpolator.getInterpolation(getLinearCurrentKeyframeProgress());
    }

    @FloatRange(from = 0.0d, to = 1.0d)
    private float getStartDelayProgress() {
        if (this.cachedStartDelayProgress == -1.0f) {
            this.cachedStartDelayProgress = this.keyframesWrapper.getStartDelayProgress();
        }
        return this.cachedStartDelayProgress;
    }

    /* access modifiers changed from: package-private */
    @FloatRange(from = 0.0d, to = 1.0d)
    public float getEndProgress() {
        if (this.cachedEndProgress == -1.0f) {
            this.cachedEndProgress = this.keyframesWrapper.getEndProgress();
        }
        return this.cachedEndProgress;
    }

    public A getValue() {
        float interpolatedCurrentKeyframeProgress = getInterpolatedCurrentKeyframeProgress();
        if (this.valueCallback == null && this.keyframesWrapper.isCachedValueEnabled(interpolatedCurrentKeyframeProgress)) {
            return this.cachedGetValue;
        }
        A value = getValue(getCurrentKeyframe(), interpolatedCurrentKeyframeProgress);
        this.cachedGetValue = value;
        return value;
    }

    public float getProgress() {
        return this.progress;
    }

    public void setValueCallback(@Nullable LottieValueCallback<A> lottieValueCallback) {
        if (this.valueCallback != null) {
            this.valueCallback.setAnimation((BaseKeyframeAnimation<?, ?>) null);
        }
        this.valueCallback = lottieValueCallback;
        if (lottieValueCallback != null) {
            lottieValueCallback.setAnimation(this);
        }
    }

    private static <T> KeyframesWrapper<T> wrap(List<? extends Keyframe<T>> list) {
        if (list.isEmpty()) {
            return new EmptyKeyframeWrapper();
        }
        if (list.size() == 1) {
            return new SingleKeyframeWrapper(list);
        }
        return new KeyframesWrapperImpl(list);
    }

    private static final class EmptyKeyframeWrapper<T> implements KeyframesWrapper<T> {
        public float getEndProgress() {
            return 1.0f;
        }

        public float getStartDelayProgress() {
            return 0.0f;
        }

        public boolean isEmpty() {
            return true;
        }

        public boolean isValueChanged(float f) {
            return false;
        }

        private EmptyKeyframeWrapper() {
        }

        public Keyframe<T> getCurrentKeyframe() {
            throw new IllegalStateException("not implemented");
        }

        public boolean isCachedValueEnabled(float f) {
            throw new IllegalStateException("not implemented");
        }
    }

    private static final class SingleKeyframeWrapper<T> implements KeyframesWrapper<T> {
        private float cachedInterpolatedProgress = -1.0f;
        @NonNull
        private final Keyframe<T> keyframe;

        public boolean isEmpty() {
            return false;
        }

        SingleKeyframeWrapper(List<? extends Keyframe<T>> list) {
            this.keyframe = (Keyframe) list.get(0);
        }

        public boolean isValueChanged(float f) {
            return !this.keyframe.isStatic();
        }

        public Keyframe<T> getCurrentKeyframe() {
            return this.keyframe;
        }

        public float getStartDelayProgress() {
            return this.keyframe.getStartProgress();
        }

        public float getEndProgress() {
            return this.keyframe.getEndProgress();
        }

        public boolean isCachedValueEnabled(float f) {
            if (this.cachedInterpolatedProgress == f) {
                return true;
            }
            this.cachedInterpolatedProgress = f;
            return false;
        }
    }

    private static final class KeyframesWrapperImpl<T> implements KeyframesWrapper<T> {
        private Keyframe<T> cachedCurrentKeyframe = null;
        private float cachedInterpolatedProgress = -1.0f;
        @NonNull
        private Keyframe<T> currentKeyframe;
        private final List<? extends Keyframe<T>> keyframes;

        public boolean isEmpty() {
            return false;
        }

        KeyframesWrapperImpl(List<? extends Keyframe<T>> list) {
            this.keyframes = list;
            this.currentKeyframe = findKeyframe(0.0f);
        }

        public boolean isValueChanged(float f) {
            if (this.currentKeyframe.containsProgress(f)) {
                return !this.currentKeyframe.isStatic();
            }
            this.currentKeyframe = findKeyframe(f);
            return true;
        }

        private Keyframe<T> findKeyframe(float f) {
            Keyframe<T> keyframe = (Keyframe) this.keyframes.get(this.keyframes.size() - 1);
            if (f >= keyframe.getStartProgress()) {
                return keyframe;
            }
            for (int size = this.keyframes.size() - 2; size >= 1; size--) {
                Keyframe<T> keyframe2 = (Keyframe) this.keyframes.get(size);
                if (this.currentKeyframe != keyframe2 && keyframe2.containsProgress(f)) {
                    return keyframe2;
                }
            }
            return (Keyframe) this.keyframes.get(0);
        }

        @NonNull
        public Keyframe<T> getCurrentKeyframe() {
            return this.currentKeyframe;
        }

        public float getStartDelayProgress() {
            return ((Keyframe) this.keyframes.get(0)).getStartProgress();
        }

        public float getEndProgress() {
            return ((Keyframe) this.keyframes.get(this.keyframes.size() - 1)).getEndProgress();
        }

        public boolean isCachedValueEnabled(float f) {
            if (this.cachedCurrentKeyframe == this.currentKeyframe && this.cachedInterpolatedProgress == f) {
                return true;
            }
            this.cachedCurrentKeyframe = this.currentKeyframe;
            this.cachedInterpolatedProgress = f;
            return false;
        }
    }
}