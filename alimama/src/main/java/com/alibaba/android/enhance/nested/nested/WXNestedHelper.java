package com.alibaba.android.enhance.nested.nested;

import android.view.View;
import android.view.ViewGroup;
import androidx.annotation.Nullable;
import androidx.coordinatorlayout.widget.CoordinatorLayout;
import androidx.recyclerview.widget.RecyclerView;
import androidx.viewpager.widget.PagerAdapter;
import androidx.viewpager.widget.ViewPager;
import com.google.android.material.appbar.AppBarLayout;
import com.taobao.weex.ui.component.WXVContainer;
import com.taobao.weex.ui.view.WXCirclePageAdapter;
import com.taobao.weex.ui.view.listview.WXRecyclerView;
import com.taobao.weex.ui.view.refresh.wrapper.BounceRecyclerView;
import com.taobao.weex.utils.WXUtils;
import java.util.ArrayDeque;
import java.util.List;
import java.util.Map;

public class WXNestedHelper {
    private static final String OPTION_ANIMATED = "animated";
    private static final String OPTION_INCLUDE_HEAD = "includeHead";

    public static void scrollToTop(CoordinatorLayout coordinatorLayout, @Nullable WXVContainer wXVContainer, Map<String, String> map) {
        if (map != null && !map.isEmpty() && coordinatorLayout != null) {
            boolean booleanValue = WXUtils.getBoolean(map.get("animated"), false).booleanValue();
            boolean booleanValue2 = WXUtils.getBoolean(map.get(OPTION_INCLUDE_HEAD), true).booleanValue();
            scrollToTopInternal(coordinatorLayout, wXVContainer, booleanValue);
            if (booleanValue2) {
                expandHeader(coordinatorLayout, booleanValue);
            }
        }
    }

    private static void expandHeader(CoordinatorLayout coordinatorLayout, boolean z) {
        View childAt = coordinatorLayout.getChildAt(0);
        if (childAt instanceof AppBarLayout) {
            ((AppBarLayout) childAt).setExpanded(true, z);
        }
    }

    private static void scrollToTopInternal(CoordinatorLayout coordinatorLayout, @Nullable WXVContainer wXVContainer, boolean z) {
        View childAt;
        List<View> views;
        WXRecyclerView wXRecyclerView;
        if (wXVContainer != null) {
            if (wXVContainer instanceof WXNestedChild) {
                BounceRecyclerView bounceRecyclerView = (BounceRecyclerView) ((WXNestedChild) wXVContainer).getHostView();
                if (bounceRecyclerView != null && (wXRecyclerView = (WXRecyclerView) bounceRecyclerView.getInnerView()) != null) {
                    letsScroll(wXRecyclerView, z);
                    return;
                }
                return;
            }
            RecyclerView findRecyclerViewRecursively = findRecyclerViewRecursively((ViewGroup) wXVContainer.getHostView());
            if (findRecyclerViewRecursively != null) {
                letsScroll(findRecyclerViewRecursively, z);
            }
        } else if (coordinatorLayout.getChildCount() >= 2 && (childAt = coordinatorLayout.getChildAt(1)) != null && (childAt instanceof ViewGroup)) {
            while (true) {
                ViewGroup viewGroup = (ViewGroup) childAt;
                if (viewGroup instanceof ViewPager) {
                    ViewPager viewPager = (ViewPager) viewGroup;
                    int currentItem = viewPager.getCurrentItem();
                    if (currentItem >= 0 && currentItem < viewGroup.getChildCount()) {
                        childAt = null;
                        PagerAdapter adapter = viewPager.getAdapter();
                        if (!(adapter == null || !(adapter instanceof WXCirclePageAdapter) || (views = ((WXCirclePageAdapter) adapter).getViews()) == null)) {
                            childAt = views.get(currentItem);
                        }
                        if (childAt == null) {
                            return;
                        }
                    } else {
                        return;
                    }
                } else if (viewGroup instanceof RecyclerView) {
                    letsScroll((RecyclerView) viewGroup, z);
                    return;
                } else if (viewGroup.getChildCount() > 0) {
                    childAt = viewGroup.getChildAt(0);
                    if (!(childAt instanceof ViewGroup)) {
                        return;
                    }
                } else {
                    return;
                }
            }
        }
    }

    private static RecyclerView findRecyclerViewRecursively(@Nullable ViewGroup viewGroup) {
        if (viewGroup == null) {
            return null;
        }
        ArrayDeque arrayDeque = new ArrayDeque();
        arrayDeque.add(viewGroup);
        while (!arrayDeque.isEmpty()) {
            View view = (View) arrayDeque.removeFirst();
            if (view instanceof RecyclerView) {
                return (RecyclerView) view;
            }
            if (view instanceof ViewGroup) {
                ViewGroup viewGroup2 = (ViewGroup) view;
                int childCount = viewGroup2.getChildCount();
                for (int i = 0; i < childCount; i++) {
                    arrayDeque.add(viewGroup2.getChildAt(i));
                }
            }
        }
        return null;
    }

    private static void letsScroll(RecyclerView recyclerView, boolean z) {
        if (z) {
            recyclerView.smoothScrollToPosition(0);
            return;
        }
        RecyclerView.LayoutManager layoutManager = recyclerView.getLayoutManager();
        if (layoutManager != null) {
            layoutManager.scrollToPosition(0);
        }
    }
}
