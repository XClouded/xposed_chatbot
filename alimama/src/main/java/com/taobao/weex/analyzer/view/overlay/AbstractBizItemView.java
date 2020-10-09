package com.taobao.weex.analyzer.view.overlay;

import android.content.Context;
import android.util.AttributeSet;
import android.view.LayoutInflater;
import android.widget.FrameLayout;
import androidx.annotation.LayoutRes;

public abstract class AbstractBizItemView<T> extends FrameLayout {
    /* access modifiers changed from: protected */
    @LayoutRes
    public abstract int getLayoutResId();

    public void inflateData(T t) {
    }

    /* access modifiers changed from: protected */
    public abstract void prepareView();

    public AbstractBizItemView(Context context) {
        super(context);
        init();
    }

    public AbstractBizItemView(Context context, AttributeSet attributeSet) {
        super(context, attributeSet);
        init();
    }

    public AbstractBizItemView(Context context, AttributeSet attributeSet, int i) {
        super(context, attributeSet, i);
        init();
    }

    private void init() {
        LayoutInflater.from(getContext()).inflate(getLayoutResId(), this, true);
        prepareView();
    }
}
