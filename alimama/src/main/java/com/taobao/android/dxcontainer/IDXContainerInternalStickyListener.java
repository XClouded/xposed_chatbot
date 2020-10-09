package com.taobao.android.dxcontainer;

import android.view.View;
import com.taobao.android.dxcontainer.vlayout.layout.StickyLayoutHelper;

class IDXContainerInternalStickyListener implements StickyLayoutHelper.StickyListener {
    private DXContainerStickyListener listener;
    private DXContainerViewPager viewPager;

    IDXContainerInternalStickyListener() {
    }

    /* access modifiers changed from: package-private */
    public void setViewPager(DXContainerViewPager dXContainerViewPager) {
        this.viewPager = dXContainerViewPager;
    }

    public void setListener(DXContainerStickyListener dXContainerStickyListener) {
        this.listener = dXContainerStickyListener;
    }

    public void onSticky(int i, View view) {
        if (this.listener != null) {
            this.listener.onSticky(i, view);
        }
    }

    public void onUnSticky(int i, View view) {
        if (this.viewPager != null) {
            this.viewPager.resetState();
        }
        if (this.listener != null) {
            this.listener.onUnSticky(i, view);
        }
    }
}
