package com.huawei.android.hms.agent.common;

import android.os.Handler;
import android.os.Looper;
import java.util.concurrent.ExecutorService;
import java.util.concurrent.Executors;

public final class ThreadUtil {
    public static final ThreadUtil INST = new ThreadUtil();
    private ExecutorService executors;

    private ThreadUtil() {
    }

    public void excute(Runnable runnable) {
        ExecutorService executorService = getExecutorService();
        if (executorService != null) {
            executorService.execute(runnable);
        } else {
            new Thread(runnable).start();
        }
    }

    public void excuteInMainThread(Runnable runnable) {
        new Handler(Looper.getMainLooper()).post(runnable);
    }

    private ExecutorService getExecutorService() {
        if (this.executors == null) {
            try {
                this.executors = Executors.newCachedThreadPool();
            } catch (Exception e) {
                HMSAgentLog.e("create thread service error:" + e.getMessage());
            }
        }
        return this.executors;
    }
}
