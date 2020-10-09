package com.alibaba.android.enhance.nested.overscroll;

import android.content.Context;
import android.view.View;
import android.view.ViewGroup;
import com.alibaba.android.enhance.nested.nested.WXNestedHeader;
import com.alibaba.android.enhance.nested.overscroll.WXNestedOverScrollLayout;
import com.taobao.weex.ui.component.WXComponent;
import com.taobao.weex.ui.component.WXVContainer;
import com.taobao.weex.ui.view.refresh.core.WXSwipeLayout;

public final class WXNestedOverScrollCreator {
    public static <T extends ViewGroup> WXNestedOverScrollLayout createContainerView(Context context, WXVContainer<T> wXVContainer) {
        final WXNestedOverScrollLayout wXNestedOverScrollLayout = new WXNestedOverScrollLayout(context);
        wXNestedOverScrollLayout.addOnLayoutChangeListener(new View.OnLayoutChangeListener() {
            public void onLayoutChange(View view, int i, int i2, int i3, int i4, int i5, int i6, int i7, int i8) {
                WXNestedOverScrollCreator.enableNestedScroll(view);
            }
        });
        int childCount = wXVContainer.getChildCount();
        int i = 0;
        while (true) {
            if (i >= childCount) {
                break;
            }
            WXComponent child = wXVContainer.getChild(i);
            if (child instanceof WXNestedHeader) {
                ((WXNestedHeader) child).registerOnPullDownEventDirectly(new WXNestedHeader.OnPullDownRefresh() {
                    public void addOnRefreshOffsetChangedListener(final WXSwipeLayout.OnRefreshOffsetChangedListener onRefreshOffsetChangedListener) {
                        wXNestedOverScrollLayout.addOnScrollChangeListener(new WXNestedOverScrollLayout.OnScrollChangeListener() {
                            public void onScrollChange(View view, int i, int i2, int i3, int i4) {
                                onRefreshOffsetChangedListener.onOffsetChanged(i2 - i4);
                            }
                        });
                    }
                });
                break;
            }
            i++;
        }
        return wXNestedOverScrollLayout;
    }

    /* access modifiers changed from: private */
    public static void enableNestedScroll(View view) {
        if (view instanceof ViewGroup) {
            ViewGroup viewGroup = (ViewGroup) view;
            int childCount = viewGroup.getChildCount();
            for (int i = 0; i < childCount; i++) {
                View childAt = viewGroup.getChildAt(i);
                if (childAt instanceof WXSwipeLayout) {
                    ((WXSwipeLayout) childAt).setNestedScrollingEnabled(true);
                } else if (childAt instanceof ViewGroup) {
                    enableNestedScroll(childAt);
                }
            }
        }
    }
}
