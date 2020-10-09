package com.taobao.weex.layout;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import com.taobao.weex.base.CalledByNative;
import com.taobao.weex.common.Destroyable;
import com.taobao.weex.ui.component.WXComponent;
import java.io.Serializable;

public abstract class ContentBoxMeasurement implements Serializable, Destroyable {
    @Nullable
    protected WXComponent mComponent;
    protected float mMeasureHeight;
    protected float mMeasureWidth;

    @CalledByNative
    public abstract void layoutAfter(float f, float f2);

    @CalledByNative
    public abstract void layoutBefore();

    public abstract void measureInternal(float f, float f2, int i, int i2);

    public ContentBoxMeasurement() {
        this.mComponent = null;
    }

    public ContentBoxMeasurement(@NonNull WXComponent wXComponent) {
        this.mComponent = wXComponent;
    }

    @CalledByNative
    public final void measure(float f, float f2, int i, int i2) {
        measureInternal(f, f2, i, i2);
    }

    @CalledByNative
    public float getWidth() {
        return this.mMeasureWidth;
    }

    @CalledByNative
    public float getHeight() {
        return this.mMeasureHeight;
    }

    public void destroy() {
        this.mComponent = null;
    }
}
