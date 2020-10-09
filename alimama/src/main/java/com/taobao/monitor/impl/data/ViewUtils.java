package com.taobao.monitor.impl.data;

import android.util.Log;
import android.view.Display;
import android.view.View;
import android.view.ViewGroup;
import android.view.ViewParent;
import android.view.WindowManager;
import com.taobao.monitor.impl.common.Global;
import com.taobao.monitor.impl.util.DeviceUtils;
import java.lang.reflect.Field;

public class ViewUtils {
    private static final int ACTION_STATUS_BAR_HEIGHT = (actionBarHeight() + statusBarHeight());
    private static final String TAG = "ViewUtils";
    private static Field mChildrenField;
    public static final int screenHeight;
    public static final int screenWidth;

    static {
        Display defaultDisplay = ((WindowManager) Global.instance().context().getSystemService("window")).getDefaultDisplay();
        screenHeight = defaultDisplay.getHeight();
        screenWidth = defaultDisplay.getWidth();
        try {
            mChildrenField = ViewGroup.class.getDeclaredField("mChildren");
            mChildrenField.setAccessible(true);
        } catch (Exception e) {
            e.printStackTrace();
        }
    }

    public static View[] getChildren(ViewGroup viewGroup) {
        try {
            return (View[]) mChildrenField.get(viewGroup);
        } catch (IllegalAccessException unused) {
            return null;
        }
    }

    public static boolean isInContentArea(View view, View view2) {
        int[] absLocationInWindow = getAbsLocationInWindow(view, view2);
        int i = absLocationInWindow[1];
        int height = absLocationInWindow[1] + view.getHeight();
        int i2 = absLocationInWindow[0];
        int width = absLocationInWindow[0] + view.getWidth();
        if (i >= screenHeight || height <= ACTION_STATUS_BAR_HEIGHT || width <= 0 || i2 >= screenWidth || height - i <= 0) {
            return false;
        }
        return true;
    }

    public static boolean isInVisibleArea(View view, View view2) {
        int[] absLocationInWindow = getAbsLocationInWindow(view, view2);
        int i = absLocationInWindow[1];
        int height = absLocationInWindow[1] + view.getHeight();
        int i2 = absLocationInWindow[0];
        int width = absLocationInWindow[0] + view.getWidth();
        if (i >= screenHeight || height <= 0 || width <= 0 || i2 >= screenWidth || height - i <= 0) {
            return false;
        }
        return true;
    }

    private static int statusBarHeight() {
        try {
            Class<?> cls = Class.forName("com.android.internal.R$dimen");
            return Global.instance().context().getResources().getDimensionPixelSize(Integer.parseInt(cls.getField("status_bar_height").get(cls.newInstance()).toString()));
        } catch (Exception e) {
            Log.d(TAG, "get status bar height fail");
            e.printStackTrace();
            return DeviceUtils.dip2px(24);
        }
    }

    private static int actionBarHeight() {
        return DeviceUtils.dip2px(48);
    }

    public static int[] getAbsLocationInWindow(View view, View view2) {
        int[] iArr = {0, 0};
        while (view != view2) {
            iArr[1] = iArr[1] + view.getTop();
            iArr[0] = iArr[0] + view.getLeft();
            ViewParent parent = view.getParent();
            if (!(parent instanceof View)) {
                break;
            }
            view = (View) parent;
        }
        return iArr;
    }
}
