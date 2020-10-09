package com.ali.user.mobile.utils;

import android.os.Handler;
import android.os.Looper;
import com.ali.user.mobile.log.TLogAdapter;

public class MainThreadExecutor {
    private static Handler mHandler = new Handler(Looper.getMainLooper());

    public static void execute(Runnable runnable) {
        try {
            mHandler.post(runnable);
        } catch (Exception e) {
            TLogAdapter.e("login.MainThreadExecutor", "MainThreadExecutor.excute failed.", e);
            e.printStackTrace();
        }
    }
}
