package com.alibaba.ha.adapter.service.crash;

import com.alibaba.motu.crashreporter.IUTCrashCaughtListener;
import java.util.Map;

public class JavaCrashListenerAdapter implements IUTCrashCaughtListener {
    private JavaCrashListener javaCrashListener = null;

    public JavaCrashListenerAdapter(JavaCrashListener javaCrashListener2) {
        this.javaCrashListener = javaCrashListener2;
    }

    public Map<String, Object> onCrashCaught(Thread thread, Throwable th) {
        if (this.javaCrashListener != null) {
            return this.javaCrashListener.onCrashCaught(thread, th);
        }
        return null;
    }
}
