package com.taobao.uikit.feature.features;

import android.content.Context;
import android.util.AttributeSet;
import android.view.View;

public abstract class AbsFeature<T extends View> {
    protected T mHost;

    public abstract void constructor(Context context, AttributeSet attributeSet, int i);

    public AbsFeature() {
        constructor((Context) null, (AttributeSet) null, 0);
    }

    public void setHost(T t) {
        this.mHost = t;
    }

    public T getHost() {
        return this.mHost;
    }
}
