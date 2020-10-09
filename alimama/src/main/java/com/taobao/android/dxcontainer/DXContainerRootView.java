package com.taobao.android.dxcontainer;

import android.content.Context;
import android.util.AttributeSet;
import android.widget.FrameLayout;
import androidx.annotation.NonNull;
import androidx.annotation.Nullable;

public class DXContainerRootView extends FrameLayout {
    public DXContainerRootView(@NonNull Context context) {
        super(context);
    }

    public DXContainerRootView(@NonNull Context context, @Nullable AttributeSet attributeSet) {
        super(context, attributeSet);
    }

    public DXContainerRootView(@NonNull Context context, @Nullable AttributeSet attributeSet, int i) {
        super(context, attributeSet, i);
    }
}
