package com.taobao.android.dxcontainer.vlayout;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import java.util.List;

public abstract class LayoutHelperFinder {
    @Nullable
    public abstract LayoutHelper getLayoutHelper(int i);

    /* access modifiers changed from: protected */
    @NonNull
    public abstract List<LayoutHelper> getLayoutHelpers();

    /* access modifiers changed from: protected */
    public abstract List<LayoutHelper> reverse();

    /* access modifiers changed from: package-private */
    public abstract void setLayouts(@Nullable List<LayoutHelper> list);
}
