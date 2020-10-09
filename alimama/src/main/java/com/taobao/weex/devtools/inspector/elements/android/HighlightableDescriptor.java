package com.taobao.weex.devtools.inspector.elements.android;

import android.view.View;
import javax.annotation.Nullable;

public interface HighlightableDescriptor {
    @Nullable
    View getViewForHighlighting(Object obj);
}
