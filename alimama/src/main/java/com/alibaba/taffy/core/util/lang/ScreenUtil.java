package com.alibaba.taffy.core.util.lang;

import android.app.Activity;
import android.content.Context;
import android.graphics.Rect;

public class ScreenUtil {
    public static int getDeviceWidthPixels(Context context) {
        return context.getResources().getDisplayMetrics().widthPixels;
    }

    public static int getDeviceHeightPixels(Context context) {
        return context.getResources().getDisplayMetrics().heightPixels;
    }

    public static float getDeviceDensity(Context context) {
        return context.getResources().getDisplayMetrics().density;
    }

    public static int getStatusBarHeightPixels(Context context) {
        Rect rect = new Rect();
        ((Activity) context).getWindow().getDecorView().getWindowVisibleDisplayFrame(rect);
        return rect.top;
    }

    public static int dp2px(Context context, int i) {
        return (int) ((((float) i) * context.getResources().getDisplayMetrics().density) + 0.5f);
    }

    public static int px2dp(Context context, int i) {
        return (int) ((((float) i) / context.getResources().getDisplayMetrics().density) + 0.5f);
    }

    public static boolean isScreenLand(Context context) {
        return getDeviceWidthPixels(context) > getDeviceHeightPixels(context);
    }
}
