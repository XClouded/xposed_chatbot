package com.taobao.android.dinamicx.render.diff;

import android.view.View;
import android.view.ViewGroup;
import com.taobao.android.dinamicx.widget.DXWidgetNode;
import java.lang.ref.WeakReference;

public abstract class DXAbsDiff {
    public abstract void diff(DXWidgetNode dXWidgetNode, DXWidgetNode dXWidgetNode2);

    /* access modifiers changed from: protected */
    public void removeFromSuperView(WeakReference<View> weakReference) {
        View view;
        if (weakReference != null && (view = (View) weakReference.get()) != null && view.getParent() != null) {
            ((ViewGroup) view.getParent()).removeView(view);
        }
    }

    /* access modifiers changed from: protected */
    public void removeAllChildView(WeakReference<View> weakReference) {
        if (weakReference != null) {
            View view = (View) weakReference.get();
            if (view instanceof ViewGroup) {
                ((ViewGroup) view).removeAllViews();
            }
        }
    }
}
