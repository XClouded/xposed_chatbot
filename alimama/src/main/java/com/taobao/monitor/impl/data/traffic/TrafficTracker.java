package com.taobao.monitor.impl.data.traffic;

import android.net.TrafficStats;
import android.os.Process;

public class TrafficTracker {
    private static long[] lastBytes = new long[2];
    private static boolean sSupported = false;
    private static int sUid = Process.myUid();

    static {
        boolean z = false;
        lastBytes[0] = TrafficStats.getUidRxBytes(sUid);
        lastBytes[1] = TrafficStats.getUidTxBytes(sUid);
        if (lastBytes[0] >= 0 && lastBytes[1] >= 0) {
            z = true;
        }
        sSupported = z;
    }

    private TrafficTracker() {
    }

    public static long[] getFlowBean() {
        if (!sSupported || sUid <= 0) {
            return lastBytes;
        }
        lastBytes[0] = TrafficStats.getUidRxBytes(sUid);
        lastBytes[1] = TrafficStats.getUidTxBytes(sUid);
        return lastBytes;
    }
}
