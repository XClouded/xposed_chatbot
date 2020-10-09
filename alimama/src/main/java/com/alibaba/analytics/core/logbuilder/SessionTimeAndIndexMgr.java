package com.alibaba.analytics.core.logbuilder;

import com.alibaba.analytics.utils.Logger;
import java.util.concurrent.atomic.AtomicInteger;

public class SessionTimeAndIndexMgr {
    private static SessionTimeAndIndexMgr instance = new SessionTimeAndIndexMgr();
    private final AtomicInteger m2202LogIndex = new AtomicInteger(0);
    private final AtomicInteger mLogIndex = new AtomicInteger(0);
    private long mSessionTime = System.currentTimeMillis();

    public static SessionTimeAndIndexMgr getInstance() {
        return instance;
    }

    private SessionTimeAndIndexMgr() {
    }

    public long getSessionTimestamp() {
        return this.mSessionTime;
    }

    public void sessionTimeout() {
        Logger.d("SessionTimeAndIndexMgr", "changeSession");
        this.mSessionTime = System.currentTimeMillis();
        this.mLogIndex.set(0);
        this.m2202LogIndex.set(0);
    }

    public long logIndexIncrementAndGet() {
        return (long) this.mLogIndex.incrementAndGet();
    }

    public long m2202LogIndexIncrementAndGet() {
        return (long) this.m2202LogIndex.incrementAndGet();
    }
}
