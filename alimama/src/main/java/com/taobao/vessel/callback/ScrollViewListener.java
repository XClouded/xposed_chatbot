package com.taobao.vessel.callback;

import android.view.View;

public interface ScrollViewListener {
    void onScrollChanged(View view, int i, int i2, int i3, int i4);

    boolean onScrollEnabled(View view, boolean z);

    void onScrollRightOrLeftEdge(View view, int i, int i2);

    void onScrollToBottom(View view, int i, int i2);

    void onScrollToTop(View view, int i, int i2);
}
