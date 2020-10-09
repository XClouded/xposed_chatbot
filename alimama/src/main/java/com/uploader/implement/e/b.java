package com.uploader.implement.e;

import android.os.Build;
import android.os.Process;
import java.util.concurrent.Future;
import java.util.concurrent.LinkedBlockingQueue;
import java.util.concurrent.ThreadFactory;
import java.util.concurrent.ThreadPoolExecutor;
import java.util.concurrent.TimeUnit;
import java.util.concurrent.atomic.AtomicInteger;

/* compiled from: ThreadPoolExecutorFactory */
public class b {
    private static int a = 10;
    private static volatile ThreadPoolExecutor b;

    /* compiled from: ThreadPoolExecutorFactory */
    private static class a implements ThreadFactory {
        /* access modifiers changed from: private */
        public int a = 10;
        private final AtomicInteger b = new AtomicInteger();

        public a(int i) {
            this.a = i;
        }

        public Thread newThread(Runnable runnable) {
            return new Thread(runnable, "aus work thread:" + this.b.getAndIncrement()) {
                public void run() {
                    Process.setThreadPriority(a.this.a);
                    super.run();
                }
            };
        }
    }

    public static ThreadPoolExecutor a() {
        if (b == null) {
            synchronized (b.class) {
                if (b == null) {
                    b = a(2, 4, 30, 128, new a(a));
                    if (Build.VERSION.SDK_INT > 8) {
                        b.allowCoreThreadTimeOut(true);
                    }
                }
            }
        }
        return b;
    }

    public static Future<?> a(Runnable runnable) {
        try {
            return a().submit(runnable);
        } catch (Throwable unused) {
            return null;
        }
    }

    public static ThreadPoolExecutor a(int i, int i2, int i3, int i4, ThreadFactory threadFactory) {
        LinkedBlockingQueue linkedBlockingQueue;
        if (i4 > 0) {
            linkedBlockingQueue = new LinkedBlockingQueue(i4);
        } else {
            linkedBlockingQueue = new LinkedBlockingQueue();
        }
        return new ThreadPoolExecutor(i, i2, (long) i3, TimeUnit.SECONDS, linkedBlockingQueue, threadFactory);
    }
}
