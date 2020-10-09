package com.ali.ha.fulltrace;

import android.os.Handler;
import android.os.HandlerThread;

public class FulltraceGlobal {
    private final Handler dumpHandler;

    private FulltraceGlobal() {
        HandlerThread handlerThread = new HandlerThread("APM-FulltraceDump");
        handlerThread.start();
        this.dumpHandler = new Handler(handlerThread.getLooper());
    }

    public static FulltraceGlobal instance() {
        return Holder.INSTANCE;
    }

    public Handler dumpHandler() {
        return this.dumpHandler;
    }

    public Handler uploadHandler() {
        return this.dumpHandler;
    }

    private static class Holder {
        static final FulltraceGlobal INSTANCE = new FulltraceGlobal();

        private Holder() {
        }
    }
}
