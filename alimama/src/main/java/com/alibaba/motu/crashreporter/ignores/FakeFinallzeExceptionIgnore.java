package com.alibaba.motu.crashreporter.ignores;

public class FakeFinallzeExceptionIgnore implements UncaughtExceptionIgnore {
    public String getName() {
        return "FakeFinallzeExceptionIgnore";
    }

    public boolean uncaughtExceptionIgnore(Thread thread, Throwable th) {
        String name = thread.getName();
        if (("FinalizerDaemon".equals(name) || "FakeFinalizerDaemon".equals(name) || "FinalizerWatchdogDaemon".equals(name) || "FakeFinalizerWatchdogDaemon".equals(name)) && (th instanceof IllegalStateException)) {
            return "not running".equals(th.getMessage()) || "already running".equals(th.getMessage());
        }
        return false;
    }
}
