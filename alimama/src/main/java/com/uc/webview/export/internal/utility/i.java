package com.uc.webview.export.internal.utility;

import com.uc.webview.export.internal.setup.ae;
import java.util.concurrent.BlockingQueue;
import java.util.concurrent.Callable;
import java.util.concurrent.Future;
import java.util.concurrent.LinkedBlockingQueue;
import java.util.concurrent.ThreadFactory;
import java.util.concurrent.ThreadPoolExecutor;
import java.util.concurrent.TimeUnit;

/* compiled from: U4Source */
public class i {
    /* access modifiers changed from: private */
    public static final String a = "i";
    private static ThreadPoolExecutor b;
    private static LinkedBlockingQueue<Runnable> c;

    /* compiled from: U4Source */
    static class b implements ThreadFactory {
        /* access modifiers changed from: private */
        public static volatile int a;

        private b() {
        }

        /* synthetic */ b(byte b) {
            this();
        }

        public final Thread newThread(Runnable runnable) {
            int i = a + 1;
            a = i;
            String format = String.format("%s-%d", new Object[]{"UCCoreThread", Integer.valueOf(i)});
            Thread thread = new Thread(runnable, format);
            thread.setUncaughtExceptionHandler(new j(this, format));
            if (a > 1) {
                String c = i.a;
                Log.d(c, "threadName " + format);
            }
            return thread;
        }
    }

    /* compiled from: U4Source */
    public static class a extends ThreadPoolExecutor {
        public a(TimeUnit timeUnit, BlockingQueue<Runnable> blockingQueue, ThreadFactory threadFactory) {
            super(2, 5, 30, timeUnit, blockingQueue, threadFactory);
        }

        /* access modifiers changed from: protected */
        public final void beforeExecute(Thread thread, Runnable runnable) {
            super.beforeExecute(thread, runnable);
            ae.a().a(runnable);
        }

        /* access modifiers changed from: protected */
        public final void afterExecute(Runnable runnable, Throwable th) {
            super.afterExecute(runnable, th);
            ae.a().b(runnable);
        }
    }

    private static ThreadPoolExecutor d() {
        synchronized (i.class) {
            if (b != null) {
                ThreadPoolExecutor threadPoolExecutor = b;
                return threadPoolExecutor;
            }
            com.uc.webview.export.internal.uc.startup.b.a(504);
            c = new LinkedBlockingQueue<>(32);
            a aVar = new a(TimeUnit.SECONDS, c, new b((byte) 0));
            b = aVar;
            aVar.allowCoreThreadTimeOut(true);
            com.uc.webview.export.internal.uc.startup.b.a(505);
            return b;
        }
    }

    public static void a() {
        if (2 != d().getCorePoolSize()) {
            d().setCorePoolSize(2);
        }
    }

    public static int b() {
        return b.a;
    }

    public static final void a(Runnable runnable) {
        if (runnable != null) {
            d().execute(runnable);
        }
    }

    public static final void b(Runnable runnable) {
        new Thread(runnable, String.format("%s-%d", new Object[]{"UCCoreThread", Integer.valueOf(runnable.hashCode())})).start();
    }

    public static final <T> Future<T> a(Callable<T> callable) {
        return d().submit(callable);
    }
}
