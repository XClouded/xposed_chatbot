package com.taobao.android.dinamicx.widget;

import android.content.Context;
import android.view.View;
import androidx.annotation.Nullable;
import com.taobao.android.dinamicx.view.DXNativeListLayout;

public class DXListLayout extends DXLinearLayoutWidgetNode {
    public DXListLayout() {
        this.propertyInitFlag |= 2;
    }

    public static class Builder implements IDXBuilderWidgetNode {
        public DXWidgetNode build(@Nullable Object obj) {
            return new DXListLayout();
        }
    }

    /* access modifiers changed from: protected */
    public View onCreateView(Context context) {
        return new DXNativeListLayout(context);
    }

    public DXWidgetNode build(@Nullable Object obj) {
        return new DXListLayout();
    }
}
