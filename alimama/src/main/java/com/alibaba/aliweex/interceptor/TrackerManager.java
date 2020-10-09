package com.alibaba.aliweex.interceptor;

import java.util.concurrent.atomic.AtomicInteger;

public class TrackerManager {
    private static final String TAG = "TrackManager";
    private static final AtomicInteger sSequenceNumberGenerator = new AtomicInteger(0);

    public static int nextRequestId() {
        return sSequenceNumberGenerator.getAndIncrement();
    }
}
