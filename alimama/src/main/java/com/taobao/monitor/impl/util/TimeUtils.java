package com.taobao.monitor.impl.util;

import android.os.SystemClock;

public class TimeUtils {
    public static long currentTimeMillis() {
        return SystemClock.uptimeMillis();
    }
}
