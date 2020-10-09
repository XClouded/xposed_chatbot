package com.taobao.android.dxcontainer.adapter;

import android.content.Context;
import androidx.annotation.NonNull;
import com.taobao.android.dxcontainer.vlayout.VirtualLayoutManager;

public class DXContainerBaseLayoutManager extends VirtualLayoutManager {
    public DXContainerBaseLayoutManager(@NonNull Context context) {
        super(context);
    }

    public DXContainerBaseLayoutManager(@NonNull Context context, int i) {
        super(context, i);
    }

    public DXContainerBaseLayoutManager(@NonNull Context context, int i, boolean z) {
        super(context, i, z);
    }
}
