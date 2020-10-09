package com.huawei.hianalytics.i;

import java.util.concurrent.LinkedBlockingQueue;
import java.util.concurrent.RejectedExecutionException;
import java.util.concurrent.ThreadPoolExecutor;
import java.util.concurrent.TimeUnit;

public class b {
    private static b b = new b();
    private static b c = new b();
    private static b d = new b();
    private static b e = new b();
    private ThreadPoolExecutor a = new ThreadPoolExecutor(0, 1, 60000, TimeUnit.MILLISECONDS, new LinkedBlockingQueue(5000));

    private static class a implements Runnable {
        private Runnable a;

        public a(Runnable runnable) {
            this.a = runnable;
        }

        public void run() {
            if (this.a != null) {
                try {
                    this.a.run();
                } catch (Exception unused) {
                    com.huawei.hianalytics.g.b.c("TaskThread", "InnerTask : Exception has happened,From internal operations!");
                }
            }
        }
    }

    private b() {
    }

    public static b a() {
        return b;
    }

    public static b b() {
        return c;
    }

    public static b c() {
        return d;
    }

    public static b d() {
        return e;
    }

    public void a(a aVar) {
        try {
            this.a.execute(new a(aVar));
        } catch (RejectedExecutionException unused) {
            com.huawei.hianalytics.g.b.c("TaskThread", "addToQueue() Exception has happened!Form rejected execution");
        }
    }
}
