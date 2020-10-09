package com.ali.telescope.internal.plugins.startPref;

import android.os.Looper;
import android.os.MessageQueue;
import com.ali.telescope.util.TelescopeLog;

public class ColdBootCheck implements MessageQueue.IdleHandler {
    public boolean queueIdle() {
        StartPrefPlugin.sIsCodeBoot = false;
        TelescopeLog.d("StartPrefPlugin", "非完全冷启动！");
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
