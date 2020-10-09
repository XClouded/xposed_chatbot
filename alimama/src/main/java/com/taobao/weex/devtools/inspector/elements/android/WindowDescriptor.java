package com.taobao.weex.devtools.inspector.elements.android;

import android.view.View;
import android.view.Window;
import com.taobao.weex.devtools.common.Accumulator;
import com.taobao.weex.devtools.inspector.elements.AbstractChainedDescriptor;
import javax.annotation.Nullable;

final class WindowDescriptor extends AbstractChainedDescriptor<Window> implements HighlightableDescriptor {
    WindowDescriptor() {
    }

    /* access modifiers changed from: protected */
    public void onGetChildren(Window window, Accumulator<Object> accumulator) {
        View peekDecorView = window.peekDecorView();
        if (peekDecorView != null) {
            accumulator.store(peekDecorView);
        }
    }

    @Nullable
    public View getViewForHighlighting(Object obj) {
        return ((Window) obj).peekDecorView();
    }
}
