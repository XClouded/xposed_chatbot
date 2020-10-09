package com.taobao.uikit.extend.utils;

import android.app.Activity;
import android.os.Build;
import android.util.DisplayMetrics;
import android.view.View;
import android.view.WindowManager;

public class NavigationBarUtils {
    public static int getNavigationBarHeight(Activity activity) {
        int identifier = activity.getResources().getIdentifier("navigation_bar_height", "dimen", "android");
        if (identifier > 0) {
            return activity.getResources().getDimensionPixelSize(identifier);
        }
        return 0;
    }

    public static boolean isNavigationBarShown(Activity activity) {
        int visibility;
        View findViewById = activity.findViewById(16908336);
        if (findViewById == null || (visibility = findViewById.getVisibility()) == 8 || visibility == 4) {
            return false;
        }
        return true;
    }

    public static int getCurrentNavigationBarHeight(Activity activity) {
        if (isNavigationBarShown(activity)) {
            return getNavigationBarHeight(activity);
        }
        return 0;
    }

    public static int getContentHeight(Activity activity) {
        DisplayMetrics displayMetrics;
        WindowManager windowManager = (WindowManager) activity.getSystemService("window");
        if (Build.VERSION.SDK_INT >= 17) {
            displayMetrics = new DisplayMetrics();
            windowManager.getDefaultDisplay().getRealMetrics(displayMetrics);
        } else {
            displayMetrics = null;
        }
        if (displayMetrics != null) {
            return displayMetrics.heightPixels - getCurrentNavigationBarHeight(activity);
        }
        return DisplayUtil.getScreenHeight(activity);
    }
}
