package com.uc.webview.export.cyclone;

import android.os.SystemClock;

@Constant
/* compiled from: U4Source */
public class UCElapseTime {
    private long mStart = currentTime();
    private long mStartCpu = currentThreadTime();

    public long getMilis() {
        return currentTime() - this.mStart;
    }

    public long getMilisCpu() {
        return currentThreadTime() - this.mStartCpu;
    }

    public void reset() {
        this.mStart = currentTime();
        this.mStartCpu = currentThreadTime();
    }

    public String toString() {
        return String.format("milis: %-6d, %-6d", new Object[]{Long.valueOf(getMilis()), Long.valueOf(getMilisCpu())});
    }

    public static long currentTime() {
        return System.currentTimeMillis();
    }

    public static long currentThreadTime() {
        return SystemClock.currentThreadTimeMillis();
    }

    public long start() {
        return this.mStart;
    }

    public long startCpu() {
        return this.mStartCpu;
    }
}
