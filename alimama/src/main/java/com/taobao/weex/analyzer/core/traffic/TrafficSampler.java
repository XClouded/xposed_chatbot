package com.taobao.weex.analyzer.core.traffic;

import android.net.TrafficStats;

public class TrafficSampler {
    public static double getUidRxBytes(int i) {
        if (TrafficStats.getUidRxBytes(i) == -1) {
            return 0.0d;
        }
        return (double) TrafficStats.getUidRxBytes(i);
    }

    public static double getUidTxBytes(int i) {
        if (TrafficStats.getUidTxBytes(i) == -1) {
            return 0.0d;
        }
        return (double) TrafficStats.getUidTxBytes(i);
    }
}
