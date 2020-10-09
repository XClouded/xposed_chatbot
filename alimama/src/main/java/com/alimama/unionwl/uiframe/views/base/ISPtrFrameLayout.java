package com.alimama.unionwl.uiframe.views.base;

import android.content.Context;
import android.util.AttributeSet;
import android.view.View;
import in.srain.cube.ptr.PtrClassicFrameLayout;
import in.srain.cube.ptr.PtrUIHandler;

public class ISPtrFrameLayout extends PtrClassicFrameLayout {
    public ISPtrFrameLayout(Context context) {
        this(context, (AttributeSet) null);
    }

    public ISPtrFrameLayout(Context context, AttributeSet attributeSet) {
        this(context, attributeSet, 0);
    }

    public ISPtrFrameLayout(Context context, AttributeSet attributeSet, int i) {
        super(context, attributeSet, i);
    }

    public void initView(View view, PtrUIHandler ptrUIHandler) {
        if (view == null || ptrUIHandler == null) {
            throw new IllegalArgumentException("HeaderView or PtrUIHandler is null");
        }
        setHeaderView(view);
        addPtrUIHandler(ptrUIHandler);
        setLoadingMinTime(1500);
    }

    /* access modifiers changed from: protected */
    public void onMeasure(int i, int i2) {
        try {
            super.onMeasure(i, i2);
        } catch (Exception unused) {
        }
    }
}
