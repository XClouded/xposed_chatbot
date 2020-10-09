package com.ali.alihadeviceevaluator.util;

import android.app.Application;
import android.os.Handler;

public class Global {
    public static final String TAG = "DeviceEvaluator";
    public static Application context;
    public static Handler handler;

    public static long hour2Ms(long j) {
        return j * 60 * 60 * 1000;
    }
}
