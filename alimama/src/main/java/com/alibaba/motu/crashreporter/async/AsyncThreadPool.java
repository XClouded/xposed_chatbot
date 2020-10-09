package com.alibaba.motu.crashreporter.async;

import java.util.concurrent.Executors;
import java.util.concurrent.ScheduledExecutorService;
import java.util.concurrent.ThreadFactory;
import java.util.concurrent.atomic.AtomicInteger;

public class AsyncThreadPool {
    public static final AtomicInteger INTEGER = new AtomicInteger();
    public static Integer corePoolSize = 2;
    public static int prop = 10;
    public static ScheduledExecutorService threadPoolExecutor = Executors.newScheduledThreadPool(corePoolSize.intValue(), new CrashThreadFactory(prop));

    static class CrashThreadFactory implements ThreadFactory {
        private int priority;

        public CrashThreadFactory(int i) {
            this.priority = i;
        }

        public Thread newThread(Runnable runnable) {
            int andIncrement = AsyncThreadPool.INTEGER.getAndIncrement();
            Thread thread = new Thread(runnable, "CrashReporter:" + andIncrement);
            thread.setPriority(this.priority);
            return thread;
        }
    }

    public void start(Runnable runnable) {
        try {
            threadPoolExecutor.submit(runnable);
        } catch (Throwable th) {
            th.printStackTrace();
        }
    }
}
