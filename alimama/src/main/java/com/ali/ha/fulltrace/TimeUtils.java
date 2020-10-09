package com.ali.ha.fulltrace;

import android.os.SystemClock;

public class TimeUtils {
    static long dTime = (System.currentTimeMillis() - SystemClock.elapsedRealtime());

    public static long currentTimeMillis() {
        return SystemClock.elapsedRealtime() + dTime;
    }
}
