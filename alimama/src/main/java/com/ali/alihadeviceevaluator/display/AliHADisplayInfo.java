package com.ali.alihadeviceevaluator.display;

import android.content.Context;
import android.util.DisplayMetrics;

public class AliHADisplayInfo {
    private static AliHADisplayInfo mDisplayInfo;
    public float mDensity;
    public int mHeightPixels;
    public int mWidthPixels;

    public static AliHADisplayInfo getDisplayInfo(Context context) {
        if (context == null) {
            return null;
        }
        if (mDisplayInfo != null) {
            return mDisplayInfo;
        }
        DisplayMetrics displayMetrics = context.getResources().getDisplayMetrics();
        mDisplayInfo = new AliHADisplayInfo();
        mDisplayInfo.mDensity = displayMetrics.density;
        mDisplayInfo.mHeightPixels = displayMetrics.heightPixels;
        mDisplayInfo.mWidthPixels = displayMetrics.widthPixels;
        return mDisplayInfo;
    }
}
