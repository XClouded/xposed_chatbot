package anet.channel.thread;

import anet.channel.util.ALog;
import java.util.concurrent.Future;
import java.util.concurrent.LinkedBlockingDeque;
import java.util.concurrent.PriorityBlockingQueue;
import java.util.concurrent.ScheduledThreadPoolExecutor;
import java.util.concurrent.ThreadFactory;
import java.util.concurrent.ThreadPoolExecutor;
import java.util.concurrent.TimeUnit;
import java.util.concurrent.atomic.AtomicInteger;

public class ThreadPoolExecutorFactory {
    private static final String TAG = "awcn.ThreadPoolExecutorFactory";
    private static ThreadPoolExecutor backupExecutor = new ThreadPoolExecutor(32, 32, 60, TimeUnit.SECONDS, new LinkedBlockingDeque(), new Factory("AWCN Worker(Backup)"));
    private static ThreadPoolExecutor detectExecutor = new ThreadPoolExecutor(1, 1, 30, TimeUnit.SECONDS, new LinkedBlockingDeque(), new Factory("AWCN Detector"));
    private static ThreadPoolExecutor highExecutor = new ThreadPoolExecutor(2, 2, 60, TimeUnit.SECONDS, new LinkedBlockingDeque(), new Factory("AWCN Worker(H)"));
    private static ThreadPoolExecutor lowExecutor = new ThreadPoolExecutor(2, 2, 60, TimeUnit.SECONDS, new LinkedBlockingDeque(), new Factory("AWCN Worker(L)"));
    private static ThreadPoolExecutor midExecutor = new PriorityExecutor(16, 16, 60, TimeUnit.SECONDS, new PriorityBlockingQueue(), new Factory("AWCN Worker(M)"));
    private static ScheduledThreadPoolExecutor scheduledExecutor = new ScheduledThreadPoolExecutor(1, new Factory("AWCN Scheduler"));

    public static class Priority {
        public static int HIGH = 0;
        public static int LOW = 9;
        public static int NORMAL = 1;
    }

    static {
        highExecutor.allowCoreThreadTimeOut(true);
        midExecutor.allowCoreThreadTimeOut(true);
        lowExecutor.allowCoreThreadTimeOut(true);
        backupExecutor.allowCoreThreadTimeOut(true);
        detectExecutor.allowCoreThreadTimeOut(true);
    }

    private static class Factory implements ThreadFactory {
        String name;
        AtomicInteger seq = new AtomicInteger(0);

        Factory(String str) {
            this.name = str;
        }

        public Thread newThread(Runnable runnable) {
            Thread thread = new Thread(runnable, this.name + this.seq.incrementAndGet());
            ALog.i(ThreadPoolExecutorFactory.TAG, "thread created!", (String) null, "name", thread.getName());
            thread.setPriority(5);
            return thread;
        }
    }

    public static Future<?> submitScheduledTask(Runnable runnable) {
        return scheduledExecutor.submit(runnable);
    }

    public static Future<?> submitScheduledTask(Runnable runnable, long j, TimeUnit timeUnit) {
        return scheduledExecutor.schedule(runnable, j, timeUnit);
    }

    public static void removeScheduleTask(Runnable runnable) {
        scheduledExecutor.remove(runnable);
    }

    public static Future<?> submitPriorityTask(Runnable runnable, int i) {
        if (ALog.isPrintLog(1)) {
            ALog.d(TAG, "submit priority task", (String) null, "priority", Integer.valueOf(i));
        }
        if (i < Priority.HIGH || i > Priority.LOW) {
            i = Priority.LOW;
        }
        if (i == Priority.HIGH) {
            return highExecutor.submit(runnable);
        }
        if (i == Priority.LOW) {
            return lowExecutor.submit(runnable);
        }
        return midExecutor.submit(new ComparableTask(runnable, i));
    }

    public static Future<?> submitBackupTask(Runnable runnable) {
        return backupExecutor.submit(runnable);
    }

    public static Future<?> submitDetectTask(Runnable runnable) {
        return detectExecutor.submit(runnable);
    }

    static class ComparableTask implements Runnable, Comparable<ComparableTask> {
        long createTime = System.currentTimeMillis();
        int priority = 0;
        Runnable rawTask = null;

        public ComparableTask(Runnable runnable, int i) {
            this.rawTask = runnable;
            this.priority = i;
            this.createTime = System.currentTimeMillis();
        }

        public int compareTo(ComparableTask comparableTask) {
            if (this.priority != comparableTask.priority) {
                return this.priority - comparableTask.priority;
            }
            return (int) (comparableTask.createTime - this.createTime);
        }

        public void run() {
            this.rawTask.run();
        }
    }

    public static synchronized void setNormalExecutorPoolSize(int i) {
        synchronized (ThreadPoolExecutorFactory.class) {
            if (i < 6) {
                i = 6;
            }
            midExecutor.setCorePoolSize(i);
            midExecutor.setMaximumPoolSize(i);
        }
    }
}
