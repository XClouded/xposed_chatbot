package com.taobao.android.dinamicx.view;

import android.content.Context;
import androidx.recyclerview.widget.LinearLayoutManager;

public class DXLinearLayoutManager extends LinearLayoutManager {
    private boolean isScrollEnabled = true;

    public DXLinearLayoutManager(Context context) {
        super(context);
    }

    public DXLinearLayoutManager(Context context, int i, boolean z) {
        super(context, i, z);
    }

    public void setScrollEnabled(boolean z) {
        this.isScrollEnabled = z;
    }

    public boolean canScrollVertically() {
        return this.isScrollEnabled && super.canScrollVertically();
    }

    public boolean canScrollHorizontally() {
        return this.isScrollEnabled && super.canScrollHorizontally();
    }
}
