package com.taobao.aipc.utils;

import java.util.concurrent.ExecutorService;
import java.util.concurrent.SynchronousQueue;
import java.util.concurrent.ThreadFactory;
import java.util.concurrent.ThreadPoolExecutor;
import java.util.concurrent.TimeUnit;
import java.util.concurrent.atomic.AtomicInteger;

public class IPCThreadPool {
    private static volatile ExecutorService ipcExecutorService;

    private static class IPCThreadFactory implements ThreadFactory {
        private final AtomicInteger mCount;

        private IPCThreadFactory() {
            this.mCount = new AtomicInteger();
        }

        public Thread newThread(Runnable runnable) {
            return new Thread(runnable, "AIPC " + "Thread:" + this.mCount.getAndIncrement()) {
                public void run() {
                    super.run();
                }
            };
        }
    }

    public static ExecutorService getIPCExecutor() {
        if (ipcExecutorService == null) {
            synchronized (IPCThreadPool.class) {
                if (ipcExecutorService == null) {
                    ipcExecutorService = new ThreadPoolExecutor(0, Integer.MAX_VALUE, 60, TimeUnit.SECONDS, new SynchronousQueue(), new IPCThreadFactory());
                }
            }
        }
        return ipcExecutorService;
    }
}
