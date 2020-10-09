package com.taobao.monitor.impl.common;

import android.content.Context;
import android.os.Handler;
import android.os.HandlerThread;

public class Global {
    private Context context;
    private Handler handler;
    private String namespace;

    private Global() {
    }

    public static Global instance() {
        return Holder.INSTANCE;
    }

    public Context context() {
        return this.context;
    }

    public Global setContext(Context context2) {
        this.context = context2;
        return this;
    }

    public Global setNamespace(String str) {
        this.namespace = str;
        return this;
    }

    public String getNamespace() {
        return this.namespace;
    }

    public Handler handler() {
        if (this.handler == null) {
            HandlerThread handlerThread = new HandlerThread("APM-Monitor-Biz");
            handlerThread.start();
            this.handler = new Handler(handlerThread.getLooper());
        }
        return this.handler;
    }

    public Handler getAsyncUiHandler() {
        return this.handler;
    }

    public void setHandler(Handler handler2) {
        this.handler = handler2;
    }

    private static class Holder {
        static final Global INSTANCE = new Global();

        private Holder() {
        }
    }
}
