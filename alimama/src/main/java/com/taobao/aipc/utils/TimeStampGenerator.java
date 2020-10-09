package com.taobao.aipc.utils;

import java.util.concurrent.atomic.AtomicLong;

public final class TimeStampGenerator {
    private static AtomicLong sTimeStamp = new AtomicLong();

    private TimeStampGenerator() {
        throw new UnsupportedOperationException("u can't instantiate me...");
    }

    public static String getTimeStamp() {
        return System.currentTimeMillis() + "." + sTimeStamp.incrementAndGet();
    }
}
