package com.taobao.onlinemonitor;

import android.os.Looper;
import android.os.MessageQueue;
import android.util.Log;

public class ColdBootCheck implements MessageQueue.IdleHandler {
    public boolean queueIdle() {
        OnLineMonitorApp.sIsCodeBoot = false;
        if (OnLineMonitor.sIsNormalDebug) {
            Log.e("OnLineMonitor", "非完全冷启动！");
        }
        return false;
    }

    public void startChecker() {
        if (Looper.myLooper() != null) {
            Looper.myQueue().addIdleHandler(this);
        }
    }

    public void stopChecker() {
        if (Looper.myLooper() != null) {
            Looper.myQueue().removeIdleHandler(this);
        }
    }
}
