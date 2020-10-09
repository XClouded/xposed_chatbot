package com.ut.mini.crashhandler;

import com.alibaba.motu.crashreporter.IUTCrashCaughtListener;
import java.util.Map;

public class UTCrashHandlerWapper implements IUTCrashCaughtListener {
    private IUTCrashCaughtListener crashCaughtListener;

    public UTCrashHandlerWapper(IUTCrashCaughtListener iUTCrashCaughtListener) {
        this.crashCaughtListener = iUTCrashCaughtListener;
    }

    public Map<String, Object> onCrashCaught(Thread thread, Throwable th) {
        if (this.crashCaughtListener != null) {
            return this.crashCaughtListener.onCrashCaught(thread, th);
        }
        return null;
    }
}
