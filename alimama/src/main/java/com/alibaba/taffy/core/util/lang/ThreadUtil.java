package com.alibaba.taffy.core.util.lang;

import android.os.Looper;

public class ThreadUtil {
    public static boolean isMainThread() {
        return Looper.myLooper() != null && Looper.myLooper() == Looper.getMainLooper();
    }
}
