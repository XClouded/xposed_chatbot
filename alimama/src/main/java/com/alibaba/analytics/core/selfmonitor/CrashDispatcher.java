package com.alibaba.analytics.core.selfmonitor;

import java.lang.Thread;
import java.util.ArrayList;
import java.util.Collections;
import java.util.List;

public class CrashDispatcher implements Thread.UncaughtExceptionHandler {
    private static CrashDispatcher instance = new CrashDispatcher();
    private Thread.UncaughtExceptionHandler handler;
    private List<CrashListener> mlisteners = Collections.synchronizedList(new ArrayList());

    public static CrashDispatcher getInstance() {
        return instance;
    }

    public void init() {
        this.handler = Thread.getDefaultUncaughtExceptionHandler();
        Thread.setDefaultUncaughtExceptionHandler(this);
    }

    public void uncaughtException(Thread thread, Throwable th) {
        int i = 0;
        while (i < this.mlisteners.size()) {
            try {
                this.mlisteners.get(i).onCrash(thread, th);
                i++;
            } catch (Throwable th2) {
                if (this.handler != null) {
                    this.handler.uncaughtException(thread, th);
                }
                throw th2;
            }
        }
        if (this.handler == null) {
            return;
        }
        this.handler.uncaughtException(thread, th);
    }

    public void addCrashListener(CrashListener crashListener) {
        this.mlisteners.add(crashListener);
    }
}
