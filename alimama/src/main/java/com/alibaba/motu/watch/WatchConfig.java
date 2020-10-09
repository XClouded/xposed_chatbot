package com.alibaba.motu.watch;

public class WatchConfig {
    public static String TAG = "MotuWatch";
    public boolean closeMainLooperMonitor = false;
    public Long enabeMainLooperTimeoutInterval = 5000L;
    public boolean enableWatchMainThreadOnly = false;
}
