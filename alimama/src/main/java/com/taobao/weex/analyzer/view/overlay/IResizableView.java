package com.taobao.weex.analyzer.view.overlay;

import android.view.View;

public interface IResizableView {

    public interface OnSizeChangedListener {
        void onSizeChanged(int i);
    }

    void setViewSize(int i, View view, boolean z);
}
