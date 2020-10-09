package com.taobao.android.internal;

import android.content.Context;
import android.os.Looper;

public class RuntimeGlobals {
    private static final Thread sMainThread = Looper.getMainLooper().getThread();
    private static Boolean sUidShared;

    public static boolean isMultiPackageMode(Context context) {
        return false;
    }

    public static boolean isMainThread() {
        return Thread.currentThread() == sMainThread;
    }
}
