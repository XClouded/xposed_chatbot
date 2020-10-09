package com.ali.telescope.util;

import android.os.Looper;

public class ThreadUtils {
    public static boolean isUiThread() {
        return Thread.currentThread() == Looper.getMainLooper().getThread();
    }
}
