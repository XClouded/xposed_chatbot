package com.taobao.android.task;

import android.os.AsyncTask;
import android.os.Build;
import android.util.Log;
import java.io.File;
import java.io.FileFilter;
import java.lang.Thread;
import java.lang.reflect.Field;
import java.lang.reflect.Method;
import java.util.HashSet;
import java.util.Iterator;
import java.util.concurrent.Executor;
import java.util.concurrent.LinkedBlockingQueue;
import java.util.concurrent.ThreadFactory;
import java.util.concurrent.ThreadPoolExecutor;
import java.util.concurrent.TimeUnit;
import java.util.concurrent.atomic.AtomicInteger;
import java.util.concurrent.locks.LockSupport;
import java.util.regex.Pattern;

public class SaturativeExecutor extends ThreadPoolExecutor {
    private static final boolean DEBUG = false;
    private static final int KEEP_ALIVE = 1;
    private static final int MAX_POOL_SIZE = 128;
    private static final int MIN_THREADS_BEFORE_SATURATION = 3;
    /* access modifiers changed from: private */
    public static final Pattern PATTERN_CPU_ENTRIES = Pattern.compile("cpu[0-9]+");
    private static final int QUEUE_CAPACITY = 1024;
    static final String TAG = "SatuExec";
    private static SaturationAwareBlockingQueue<Runnable> mQueue;
    private static final HashSet<Thread> mThreads = new HashSet<>();
    private static final ThreadFactory sThreadFactory = new ThreadFactory() {
        private final AtomicInteger mCount = new AtomicInteger(1);

        public Thread newThread(Runnable runnable) {
            Thread thread = new Thread(runnable, "SaturativeThread #" + this.mCount.getAndIncrement());
            SaturativeExecutor.collectThread(thread);
            return thread;
        }
    };

    public void execute(Runnable runnable) {
        super.execute(new CountedTask(runnable));
    }

    public static final boolean installAsDefaultAsyncTaskExecutor(ThreadPoolExecutor threadPoolExecutor) {
        if (Build.VERSION.SDK_INT >= 11) {
            try {
                Field declaredField = AsyncTask.class.getDeclaredField("THREAD_POOL_EXECUTOR");
                declaredField.setAccessible(true);
                declaredField.set((Object) null, threadPoolExecutor);
            } catch (Exception unused) {
                Log.d(TAG, "Failed to install THREAD_POOL_EXECUTOR as default executor of AsyncTask.");
            }
        }
        try {
            Method method = AsyncTask.class.getMethod("setDefaultExecutor", new Class[]{Executor.class});
            method.setAccessible(true);
            method.invoke((Object) null, new Object[]{threadPoolExecutor});
            return true;
        } catch (Exception unused2) {
            try {
                Field declaredField2 = AsyncTask.class.getDeclaredField("sDefaultExecutor");
                declaredField2.setAccessible(true);
                declaredField2.set((Object) null, threadPoolExecutor);
                return true;
            } catch (Exception unused3) {
                try {
                    Field declaredField3 = AsyncTask.class.getDeclaredField("sExecutor");
                    declaredField3.setAccessible(true);
                    declaredField3.set((Object) null, threadPoolExecutor);
                    return true;
                } catch (Exception unused4) {
                    Log.d(TAG, "Failed to install as default executor of AsyncTask.");
                    return false;
                }
            }
        }
    }

    public SaturativeExecutor() {
        this(determineBestMinPoolSize());
    }

    /* JADX WARNING: Illegal instructions before constructor call */
    /* Code decompiled incorrectly, please refer to instructions dump. */
    public SaturativeExecutor(int r10) {
        /*
            r9 = this;
            java.util.concurrent.TimeUnit r5 = java.util.concurrent.TimeUnit.SECONDS
            com.taobao.android.task.SaturativeExecutor$SaturationAwareBlockingQueue r6 = new com.taobao.android.task.SaturativeExecutor$SaturationAwareBlockingQueue
            r0 = 1024(0x400, float:1.435E-42)
            r6.<init>(r0)
            mQueue = r6
            java.util.concurrent.ThreadFactory r7 = sThreadFactory
            java.util.concurrent.ThreadPoolExecutor$CallerRunsPolicy r8 = new java.util.concurrent.ThreadPoolExecutor$CallerRunsPolicy
            r8.<init>()
            r2 = 128(0x80, float:1.794E-43)
            r3 = 1
            r0 = r9
            r1 = r10
            r0.<init>(r1, r2, r3, r5, r6, r7, r8)
            java.util.concurrent.BlockingQueue r10 = r9.getQueue()
            com.taobao.android.task.SaturativeExecutor$SaturationAwareBlockingQueue r10 = (com.taobao.android.task.SaturativeExecutor.SaturationAwareBlockingQueue) r10
            r10.setExecutor(r9)
            return
        */
        throw new UnsupportedOperationException("Method not decompiled: com.taobao.android.task.SaturativeExecutor.<init>(int):void");
    }

    /* access modifiers changed from: protected */
    public boolean isReallyUnsaturated() {
        if (isSaturated()) {
            return false;
        }
        LockSupport.parkNanos(10);
        if (isSaturated()) {
            return false;
        }
        return true;
    }

    /* access modifiers changed from: protected */
    public boolean isSaturated() {
        int i;
        if (getPoolSize() <= 3) {
            return false;
        }
        int corePoolSize = getCorePoolSize();
        int i2 = CountedTask.mNumRunning.get();
        int size = mThreads.size();
        if (i2 < corePoolSize || i2 < size) {
            return true;
        }
        synchronized (mThreads) {
            Iterator<Thread> it = mThreads.iterator();
            i = 0;
            while (it.hasNext()) {
                Thread.State state = it.next().getState();
                if (state != Thread.State.RUNNABLE) {
                    if (state != Thread.State.NEW) {
                        if (state == Thread.State.TERMINATED) {
                            it.remove();
                        }
                    }
                }
                i++;
            }
        }
        if (i >= corePoolSize) {
            return true;
        }
        return false;
    }

    protected static void collectThread(Thread thread) {
        synchronized (mThreads) {
            mThreads.add(thread);
        }
    }

    private static int determineBestMinPoolSize() {
        int countCpuCores = countCpuCores();
        return countCpuCores > 0 ? countCpuCores : Runtime.getRuntime().availableProcessors() * 2;
    }

    private static int countCpuCores() {
        try {
            return new File("/sys/devices/system/cpu/").listFiles(new FileFilter() {
                public boolean accept(File file) {
                    return SaturativeExecutor.PATTERN_CPU_ENTRIES.matcher(file.getName()).matches();
                }
            }).length;
        } catch (Exception unused) {
            return 0;
        }
    }

    protected static class SaturationAwareBlockingQueue<T> extends LinkedBlockingQueue<T> {
        private static final long serialVersionUID = 1;
        private SaturativeExecutor mExecutor;

        public SaturationAwareBlockingQueue(int i) {
            super(i);
        }

        /* access modifiers changed from: package-private */
        public void setExecutor(SaturativeExecutor saturativeExecutor) {
            this.mExecutor = saturativeExecutor;
        }

        public boolean add(T t) {
            if (!this.mExecutor.isReallyUnsaturated()) {
                return super.add(t);
            }
            throw new IllegalStateException("Unsaturated");
        }

        public boolean offer(T t) {
            if (this.mExecutor.isReallyUnsaturated()) {
                return false;
            }
            return super.offer(t);
        }

        public void put(T t) {
            throw new UnsupportedOperationException();
        }

        public boolean offer(T t, long j, TimeUnit timeUnit) {
            throw new UnsupportedOperationException();
        }
    }

    protected static class CountedTask implements Runnable {
        static final AtomicInteger mNumRunning = new AtomicInteger();
        Runnable mRunnable;

        public CountedTask(Runnable runnable) {
            this.mRunnable = runnable;
        }

        public void run() {
            mNumRunning.incrementAndGet();
            try {
                this.mRunnable.run();
            } finally {
                mNumRunning.decrementAndGet();
            }
        }
    }
}
