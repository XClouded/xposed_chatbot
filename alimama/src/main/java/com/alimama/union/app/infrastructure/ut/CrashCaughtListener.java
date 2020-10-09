package com.alimama.union.app.infrastructure.ut;

import com.ut.mini.crashhandler.IUTCrashCaughtListner;
import java.util.HashMap;
import java.util.Map;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

public class CrashCaughtListener implements IUTCrashCaughtListner {
    private Logger logger = LoggerFactory.getLogger((Class<?>) CrashCaughtListener.class);

    public Map<String, String> onCrashCaught(Thread thread, Throwable th) {
        this.logger.error("on crash caught thread: {}, throwable: {}", (Object) Long.valueOf(thread.getId()), (Object) th.getMessage());
        return new HashMap();
    }
}
