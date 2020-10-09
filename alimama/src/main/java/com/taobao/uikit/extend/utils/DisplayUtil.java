package com.taobao.uikit.extend.utils;

import android.content.Context;
import android.util.DisplayMetrics;

public class DisplayUtil {
    private static DisplayMetrics mDisplayMetrics;

    public static int getScreenWidth(Context context) {
        mDisplayMetrics = context.getResources().getDisplayMetrics();
        return mDisplayMetrics.widthPixels;
    }

    public static int getScreenHeight(Context context) {
        mDisplayMetrics = context.getResources().getDisplayMetrics();
        return mDisplayMetrics.heightPixels;
    }

    public static float getScreenDensity(Context context) {
        mDisplayMetrics = context.getResources().getDisplayMetrics();
        return mDisplayMetrics.density;
    }

    public static DisplayMetrics getDisplayMetrics(Context context) {
        mDisplayMetrics = context.getResources().getDisplayMetrics();
        return mDisplayMetrics;
    }
}
