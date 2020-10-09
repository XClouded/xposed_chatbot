package com.alibaba.taffy.core.util.view;

import android.view.View;
import android.view.ViewGroup;
import android.view.inputmethod.InputMethodManager;
import android.widget.AbsListView;
import android.widget.ListAdapter;
import android.widget.ListView;
import android.widget.RelativeLayout;
import com.alibaba.taffy.core.collection.Predicate;
import java.util.ArrayList;
import java.util.List;

public class ViewUtil {
    private static final String CLASS_NAME_GRID_VIEW = "android.widget.GridView";
    private static final String FIELD_NAME_VERTICAL_SPACING = "mVerticalSpacing";

    /* JADX WARNING: Code restructure failed: missing block: B:4:0x000c, code lost:
        r1 = (r1 = r2.getAdapter()).getCount();
     */
    /* Code decompiled incorrectly, please refer to instructions dump. */
    public static int getListViewHeightBasedOnChildren(android.widget.ListView r2) {
        /*
            int r0 = getAbsListViewHeightBasedOnChildren(r2)
            if (r2 == 0) goto L_0x001b
            android.widget.ListAdapter r1 = r2.getAdapter()
            if (r1 == 0) goto L_0x001b
            int r1 = r1.getCount()
            if (r1 <= 0) goto L_0x001b
            int r2 = r2.getDividerHeight()
            int r1 = r1 + -1
            int r2 = r2 * r1
            int r0 = r0 + r2
        L_0x001b:
            return r0
        */
        throw new UnsupportedOperationException("Method not decompiled: com.alibaba.taffy.core.util.view.ViewUtil.getListViewHeightBasedOnChildren(android.widget.ListView):int");
    }

    public static int getAbsListViewHeightBasedOnChildren(AbsListView absListView) {
        ListAdapter listAdapter;
        if (absListView == null || (listAdapter = (ListAdapter) absListView.getAdapter()) == null) {
            return 0;
        }
        int i = 0;
        for (int i2 = 0; i2 < listAdapter.getCount(); i2++) {
            View view = listAdapter.getView(i2, (View) null, absListView);
            if (view instanceof ViewGroup) {
                view.setLayoutParams(new RelativeLayout.LayoutParams(-2, -2));
            }
            view.measure(0, 0);
            i += view.getMeasuredHeight();
        }
        return i + absListView.getPaddingTop() + absListView.getPaddingBottom();
    }

    public static void setViewHeight(View view, int i) {
        if (view != null) {
            view.getLayoutParams().height = i;
        }
    }

    public static void setListViewHeightBasedOnChildren(ListView listView) {
        setViewHeight(listView, getListViewHeightBasedOnChildren(listView));
    }

    public static void setAbsListViewHeightBasedOnChildren(AbsListView absListView) {
        setViewHeight(absListView, getAbsListViewHeightBasedOnChildren(absListView));
    }

    public static List<View> getDescendants(ViewGroup viewGroup, Predicate<View> predicate) {
        ArrayList arrayList = new ArrayList();
        int childCount = viewGroup.getChildCount();
        for (int i = 0; i < childCount; i++) {
            View childAt = viewGroup.getChildAt(i);
            if (predicate.evaluate(childAt)) {
                arrayList.add(childAt);
            }
            if (childAt instanceof ViewGroup) {
                arrayList.addAll(getDescendants((ViewGroup) childAt, predicate));
            }
        }
        return arrayList;
    }

    public static void hideKeyboard(View view) {
        if (view != null) {
            InputMethodManager inputMethodManager = (InputMethodManager) view.getContext().getSystemService("input_method");
            if (inputMethodManager.isActive()) {
                inputMethodManager.hideSoftInputFromWindow(view.getApplicationWindowToken(), 0);
            }
        }
    }

    public static void showKeyboard(View view) {
        if (view != null) {
            InputMethodManager inputMethodManager = (InputMethodManager) view.getContext().getSystemService("input_method");
            if (!inputMethodManager.isActive()) {
                inputMethodManager.showSoftInputFromInputMethod(view.getApplicationWindowToken(), 0);
            }
        }
    }
}
