package com.taobao.weex.analyzer.core;

import android.os.Handler;
import android.os.HandlerThread;
import androidx.annotation.NonNull;
import androidx.annotation.Nullable;

public class HandlerThreadWrapper {
    private Handler mHandler;
    private HandlerThread mHandlerThread;

    public HandlerThreadWrapper(@NonNull String str) {
        this(str, (Handler.Callback) null);
    }

    public HandlerThreadWrapper(@NonNull String str, @Nullable Handler.Callback callback) {
        this.mHandlerThread = new HandlerThread(str);
        this.mHandlerThread.start();
        this.mHandler = new Handler(this.mHandlerThread.getLooper(), callback);
    }

    @NonNull
    public Handler getHandler() {
        return this.mHandler;
    }

    public boolean isAlive() {
        if (this.mHandlerThread == null) {
            return false;
        }
        return this.mHandlerThread.isAlive();
    }

    public void quit() {
        if (this.mHandlerThread != null) {
            if (this.mHandler != null) {
                this.mHandler.removeCallbacksAndMessages((Object) null);
            }
            this.mHandlerThread.quit();
        }
    }
}
