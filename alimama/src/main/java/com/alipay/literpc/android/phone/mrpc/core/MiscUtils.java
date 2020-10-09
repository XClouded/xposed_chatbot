package com.alipay.literpc.android.phone.mrpc.core;

import android.content.Context;
import android.util.Log;

public final class MiscUtils {
    private static Boolean DEBUG = null;
    public static final String RC_PACKAGE_NAME = "com.eg.android.AlipayGphoneRC";

    public static final boolean isDebugger(Context context) {
        if (DEBUG != null) {
            return DEBUG.booleanValue();
        }
        try {
            DEBUG = Boolean.valueOf((context.getPackageManager().getApplicationInfo(context.getPackageName(), 0).flags & 2) != 0);
            return DEBUG.booleanValue();
        } catch (Exception e) {
            Log.e("MiscUtils", "", e);
            return false;
        }
    }
}
