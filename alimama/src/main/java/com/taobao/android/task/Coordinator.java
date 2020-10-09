package com.taobao.android.task;

import android.annotation.TargetApi;
import android.net.TrafficStats;
import android.os.AsyncTask;
import android.os.Build;
import android.os.Debug;
import android.os.Handler;
import android.os.Looper;
import android.os.Message;
import android.os.MessageQueue;
import android.os.Process;
import android.taobao.windvane.cache.WVFileInfo;
import android.util.Log;
import com.taobao.weex.el.parse.Operators;
import java.lang.reflect.Field;
import java.util.Comparator;
import java.util.LinkedList;
import java.util.Queue;
import java.util.concurrent.BlockingQueue;
import java.util.concurrent.Executor;
import java.util.concurrent.PriorityBlockingQueue;
import java.util.concurrent.RejectedExecutionException;
import java.util.concurrent.RejectedExecutionHandler;
import java.util.concurrent.ThreadFactory;
import java.util.concurrent.ThreadPoolExecutor;
import java.util.concurrent.TimeUnit;
import java.util.concurrent.atomic.AtomicInteger;
import kotlin.text.Typography;

public class Coordinator {
    public static final int QUEUE_PRIORITY_DECODE_IMAGE = 27;
    public static final int QUEUE_PRIORITY_EMERGENCY = 10;
    public static final int QUEUE_PRIORITY_IMPORTANT = 20;
    public static final int QUEUE_PRIORITY_LOWER = 50;
    public static final int QUEUE_PRIORITY_NORMAL = 30;
    public static final int QUEUE_PRIORITY_NORMAL_DOWNLOAD = 35;
    public static final int QUEUE_PRIORITY_ON_IDLE = 100;
    public static final int QUEUE_PRIORITY_PATCH_DOWNLOAD = 21;
    public static final int QUEUE_PRIORITY_REQUEST_DATA = 23;
    public static final int QUEUE_PRIORITY_REQUEST_IMAGE = 28;
    public static final int QUEUE_PRIORITY_UNIMPORTANT = 90;
    protected static final String TAG = "Coord";
    protected static final Queue<TaggedRunnable> mIdleTasks = new LinkedList();
    protected static final BlockingQueue<Runnable> mPoolWorkQueue = new PriorityBlockingQueue(100, new PriorityComparator());
    protected static Handler sHandler;
    protected static Field sOuterField;
    static ThreadInfoListener sThreadInfoListener;
    static CoordThreadPoolExecutor sThreadPoolExecutor = new CoordThreadPoolExecutor(8, 16, 1, TimeUnit.MINUTES, mPoolWorkQueue, new ThreadFactory() {
        private final AtomicInteger mCount = new AtomicInteger(1);

        public Thread newThread(Runnable runnable) {
            if (runnable instanceof TaggedRunnable) {
                "Coord#" + runnable.toString();
            } else {
                "Coord#" + runnable.getClass().getName();
            }
            return new Thread(runnable, "#" + runnable.getClass().getName());
        }
    }, new CoordinatorRejectHandler());

    public interface PriorityQueue {
        int getQueuePriority();
    }

    public interface ThreadInfoListener {
        void threadInfo(int i, String str, long j, long j2, long j3, String str2, int i2, ThreadPoolExecutor threadPoolExecutor);
    }

    static {
        sThreadPoolExecutor.allowCoreThreadTimeOut(true);
        SaturativeExecutor.installAsDefaultAsyncTaskExecutor(sThreadPoolExecutor);
    }

    public static abstract class TaggedRunnable implements Runnable, PriorityQueue {
        final String mName;
        int mQueuePriority = 30;
        int mThreadPriority = 10;
        int mTraffictag = 0;

        public TaggedRunnable(String str) {
            this.mName = str;
        }

        public TaggedRunnable(String str, int i) {
            this.mName = str;
            int i2 = 100;
            if (i < 0) {
                i2 = 0;
            } else if (i <= 100) {
                i2 = i;
            }
            this.mQueuePriority = i2;
        }

        public int getQueuePriority() {
            return this.mQueuePriority;
        }

        public void setTrafficTag(int i) {
            this.mTraffictag = i;
        }

        public void setThreadPriority(int i) {
            if (i < 1) {
                i = 1;
            }
            this.mThreadPriority = i;
        }

        public String toString() {
            if (this.mName != null) {
                return this.mName;
            }
            return getClass().getName() + '@' + this.mName;
        }
    }

    public static void setThreadInfoListener(ThreadInfoListener threadInfoListener) {
        sThreadInfoListener = threadInfoListener;
    }

    static class PriorityComparator<Runnable> implements Comparator<Runnable> {
        public int compare(Runnable runnable, Runnable runnable2) {
            if (!(runnable instanceof StandaloneTask) || !(runnable2 instanceof StandaloneTask)) {
                return 0;
            }
            StandaloneTask standaloneTask = (StandaloneTask) runnable;
            StandaloneTask standaloneTask2 = (StandaloneTask) runnable2;
            if (standaloneTask.getQueuePriority() > standaloneTask2.getQueuePriority()) {
                return 1;
            }
            return standaloneTask.getQueuePriority() < standaloneTask2.getQueuePriority() ? -1 : 0;
        }
    }

    @Deprecated
    public static void postTask(TaggedRunnable taggedRunnable) {
        postTask(taggedRunnable, Priority.DEFAULT);
    }

    @Deprecated
    public static void postTask(TaggedRunnable taggedRunnable, Priority priority) {
        sThreadPoolExecutor.execute(new StandaloneTask(taggedRunnable));
    }

    @Deprecated
    public static void postTask(TaggedRunnable taggedRunnable, int i) {
        execute(taggedRunnable, 10, i);
    }

    public static void execute(Runnable runnable) {
        sThreadPoolExecutor.execute(runnable, 30);
    }

    public static void execute(Runnable runnable, int i) {
        sThreadPoolExecutor.execute(runnable, i);
    }

    @Deprecated
    public static void execute(TaggedRunnable taggedRunnable, int i, int i2) {
        StandaloneTask standaloneTask = new StandaloneTask(taggedRunnable);
        if (i < 1) {
            i = 1;
        }
        taggedRunnable.mQueuePriority = i;
        if (i2 > 0) {
            Message obtain = Message.obtain();
            obtain.what = taggedRunnable.hashCode();
            obtain.obj = standaloneTask;
            if (sHandler == null) {
                sHandler = new Handler(Looper.getMainLooper()) {
                    public void handleMessage(Message message) {
                        Coordinator.sThreadPoolExecutor.execute((StandaloneTask) message.obj);
                    }
                };
            }
            sHandler.sendMessageDelayed(obtain, (long) i2);
            return;
        }
        sThreadPoolExecutor.execute(standaloneTask);
    }

    @Deprecated
    public static void postTask(TaggedRunnable taggedRunnable, Priority priority, int i) {
        execute(taggedRunnable, 10, i);
    }

    @Deprecated
    public static void removeDelayTask(TaggedRunnable taggedRunnable) {
        if (sHandler != null) {
            sHandler.removeMessages(taggedRunnable.hashCode());
        }
    }

    @Deprecated
    public static void postIdleTask(TaggedRunnable taggedRunnable) {
        mIdleTasks.add(taggedRunnable);
    }

    @Deprecated
    public static void runTask(TaggedRunnable taggedRunnable) {
        runWithTiming(taggedRunnable);
    }

    @Deprecated
    public static void scheduleIdleTasks() {
        Looper.myQueue().addIdleHandler(new MessageQueue.IdleHandler() {
            public boolean queueIdle() {
                TaggedRunnable poll = Coordinator.mIdleTasks.poll();
                if (poll == null) {
                    return false;
                }
                Coordinator.postTask(poll);
                return !Coordinator.mIdleTasks.isEmpty();
            }
        });
    }

    protected static void runWithTiming(Runnable runnable) {
        long j;
        String str;
        String str2;
        String name;
        String name2;
        Runnable runnable2 = runnable;
        long j2 = 0;
        if (sThreadInfoListener != null) {
            j = System.currentTimeMillis();
            j2 = Debug.threadCpuTimeNanos();
        } else {
            j = 0;
        }
        int myTid = Process.myTid();
        if (Looper.getMainLooper() != Looper.myLooper()) {
            int i = 10;
            if (runnable2 instanceof TaggedRunnable) {
                i = ((TaggedRunnable) runnable2).mThreadPriority;
            }
            Process.setThreadPriority(i);
        }
        try {
            runnable.run();
            if (sThreadInfoListener != null) {
                long threadCpuTimeNanos = (Debug.threadCpuTimeNanos() - j2) / 1000000;
                long currentTimeMillis = System.currentTimeMillis() - j;
                if (sThreadInfoListener != null) {
                    int queuePriority = runnable2 instanceof PriorityQueue ? ((PriorityQueue) runnable2).getQueuePriority() : 30;
                    if (runnable2 instanceof StandaloneTask) {
                        StandaloneTask standaloneTask = (StandaloneTask) runnable2;
                        if (standaloneTask.mRunnable instanceof TaggedRunnable) {
                            name = ((TaggedRunnable) standaloneTask.mRunnable).toString();
                        } else {
                            name2 = standaloneTask.mRunnable.getClass().getName();
                            if (name2 != null) {
                                try {
                                    if (name2.contains("AsyncTask$")) {
                                        if (sOuterField == null) {
                                            sOuterField = standaloneTask.mRunnable.getClass().getDeclaredField("this$0");
                                            sOuterField.setAccessible(true);
                                        }
                                        name = sOuterField.get(standaloneTask.mRunnable).getClass().getName();
                                    }
                                } catch (Exception unused) {
                                }
                            }
                        }
                        sThreadInfoListener.threadInfo(myTid, name, j, currentTimeMillis, threadCpuTimeNanos, runnable.getClass().getName(), queuePriority, sThreadPoolExecutor);
                    }
                    name = runnable.getClass().getName();
                    if (name != null) {
                        try {
                            if (name.contains("AsyncTask$")) {
                                if (sOuterField == null) {
                                    sOuterField = runnable.getClass().getDeclaredField("this$0");
                                    sOuterField.setAccessible(true);
                                }
                                name2 = sOuterField.get(runnable2).getClass().getName();
                            }
                        } catch (Exception unused2) {
                        }
                    }
                    sThreadInfoListener.threadInfo(myTid, name, j, currentTimeMillis, threadCpuTimeNanos, runnable.getClass().getName(), queuePriority, sThreadPoolExecutor);
                    name = name2;
                    sThreadInfoListener.threadInfo(myTid, name, j, currentTimeMillis, threadCpuTimeNanos, runnable.getClass().getName(), queuePriority, sThreadPoolExecutor);
                }
            }
        } catch (Throwable th) {
            if (sThreadInfoListener != null) {
                long threadCpuTimeNanos2 = (Debug.threadCpuTimeNanos() - j2) / 1000000;
                long currentTimeMillis2 = System.currentTimeMillis() - j;
                if (sThreadInfoListener != null) {
                    int queuePriority2 = runnable2 instanceof PriorityQueue ? ((PriorityQueue) runnable2).getQueuePriority() : 30;
                    if (runnable2 instanceof StandaloneTask) {
                        StandaloneTask standaloneTask2 = (StandaloneTask) runnable2;
                        if (!(standaloneTask2.mRunnable instanceof TaggedRunnable)) {
                            str2 = standaloneTask2.mRunnable.getClass().getName();
                            if (str2 != null) {
                                try {
                                    if (str2.contains("AsyncTask$")) {
                                        if (sOuterField == null) {
                                            sOuterField = standaloneTask2.mRunnable.getClass().getDeclaredField("this$0");
                                            sOuterField.setAccessible(true);
                                        }
                                        str2 = sOuterField.get(standaloneTask2.mRunnable).getClass().getName();
                                    }
                                } catch (Exception unused3) {
                                }
                            }
                        } else {
                            str2 = ((TaggedRunnable) standaloneTask2.mRunnable).toString();
                        }
                        str = str2;
                    } else {
                        String name3 = runnable.getClass().getName();
                        if (name3 != null) {
                            try {
                                if (name3.contains("AsyncTask$")) {
                                    if (sOuterField == null) {
                                        sOuterField = runnable.getClass().getDeclaredField("this$0");
                                        sOuterField.setAccessible(true);
                                    }
                                    name3 = sOuterField.get(runnable2).getClass().getName();
                                }
                            } catch (Exception unused4) {
                            }
                        }
                        str = name3;
                    }
                    sThreadInfoListener.threadInfo(myTid, str, j, currentTimeMillis2, threadCpuTimeNanos2, runnable.getClass().getName(), queuePriority2, sThreadPoolExecutor);
                }
            }
            throw th;
        }
    }

    static Executor getDefaultAsyncTaskExecutor() {
        if (Build.VERSION.SDK_INT >= 11) {
            return AsyncTask.SERIAL_EXECUTOR;
        }
        try {
            Field declaredField = AsyncTask.class.getDeclaredField("sExecutor");
            declaredField.setAccessible(true);
            return (Executor) declaredField.get((Object) null);
        } catch (Exception unused) {
            return null;
        }
    }

    protected static void dumpTask() {
        Object[] array = mPoolWorkQueue.toArray();
        StringBuilder sb = new StringBuilder();
        sb.append(Operators.ARRAY_START);
        for (Object obj : array) {
            if (obj.getClass().isAnonymousClass()) {
                sb.append(getOuterClass(obj));
                sb.append(WVFileInfo.DIVISION);
                sb.append(' ');
            } else {
                sb.append(obj);
                sb.append(Typography.greater);
                sb.append(' ');
            }
        }
        sb.append(Operators.ARRAY_END);
        Log.w(TAG, "Task size:" + array.length + " --" + sb.toString());
    }

    public static class CoordinatorRejectHandler implements RejectedExecutionHandler {
        public void rejectedExecution(Runnable runnable, ThreadPoolExecutor threadPoolExecutor) {
            Object[] array = Coordinator.mPoolWorkQueue.toArray();
            StringBuilder sb = new StringBuilder();
            sb.append(Operators.ARRAY_START);
            for (Object obj : array) {
                if (obj.getClass().isAnonymousClass()) {
                    sb.append(Coordinator.getOuterClass(obj));
                    sb.append(WVFileInfo.DIVISION);
                    sb.append(' ');
                } else {
                    sb.append(obj);
                    sb.append(Typography.greater);
                    sb.append(' ');
                }
            }
            sb.append(Operators.ARRAY_END);
            throw new RejectedExecutionException("Task " + runnable.toString() + " rejected from " + threadPoolExecutor.toString() + " in " + sb.toString());
        }
    }

    protected static Object getOuterClass(Object obj) {
        try {
            Field declaredField = obj.getClass().getDeclaredField("this$0");
            declaredField.setAccessible(true);
            return declaredField.get(obj);
        } catch (NoSuchFieldException e) {
            e.printStackTrace();
            return obj;
        } catch (IllegalAccessException e2) {
            e2.printStackTrace();
            return obj;
        } catch (IllegalArgumentException e3) {
            e3.printStackTrace();
            return obj;
        }
    }

    public static ThreadPoolExecutor getDefaultThreadPoolExecutor() {
        return sThreadPoolExecutor;
    }

    public static class CoordThreadPoolExecutor extends ThreadPoolExecutor {
        public CoordThreadPoolExecutor(int i, int i2, long j, TimeUnit timeUnit, BlockingQueue<Runnable> blockingQueue, ThreadFactory threadFactory, RejectedExecutionHandler rejectedExecutionHandler) {
            super(i, i2, j, timeUnit, blockingQueue, threadFactory, rejectedExecutionHandler);
        }

        public void execute(Runnable runnable, int i) {
            if (runnable instanceof StandaloneTask) {
                super.execute(runnable);
                return;
            }
            StandaloneTask standaloneTask = new StandaloneTask(runnable);
            int i2 = 1;
            if (i >= 1) {
                i2 = i;
            }
            standaloneTask.mPriorityQueue = i2;
            super.execute(standaloneTask);
        }

        public void execute(Runnable runnable) {
            if (runnable instanceof StandaloneTask) {
                super.execute(runnable);
            } else {
                super.execute(new StandaloneTask(runnable));
            }
        }

        /* access modifiers changed from: protected */
        public void beforeExecute(Thread thread, Runnable runnable) {
            super.beforeExecute(thread, runnable);
            if (runnable instanceof StandaloneTask) {
                StandaloneTask standaloneTask = (StandaloneTask) runnable;
                if (standaloneTask.mRunnable instanceof TaggedRunnable) {
                    TaggedRunnable taggedRunnable = (TaggedRunnable) standaloneTask.mRunnable;
                    thread.setName(taggedRunnable.toString());
                    if (Build.VERSION.SDK_INT >= 14) {
                        TrafficStats.setThreadStatsTag(taggedRunnable.mTraffictag);
                        return;
                    }
                    return;
                }
                thread.setName(standaloneTask.mRunnable + "");
                return;
            }
            thread.setName(runnable + "");
        }

        /* access modifiers changed from: protected */
        @TargetApi(11)
        public void afterExecute(Runnable runnable, Throwable th) {
            super.afterExecute(runnable, th);
            if (runnable instanceof StandaloneTask) {
                StandaloneTask standaloneTask = (StandaloneTask) runnable;
                if (standaloneTask.mRunnable instanceof TaggedRunnable) {
                    TaggedRunnable taggedRunnable = (TaggedRunnable) standaloneTask.mRunnable;
                    if (Build.VERSION.SDK_INT >= 14) {
                        TrafficStats.clearThreadStatsTag();
                    }
                }
            }
        }
    }

    static class StandaloneTask implements Runnable, PriorityQueue {
        int mPriorityQueue = 30;
        final Runnable mRunnable;

        public StandaloneTask(Runnable runnable) {
            this.mRunnable = runnable;
        }

        public int getQueuePriority() {
            if (this.mRunnable instanceof PriorityQueue) {
                return ((PriorityQueue) this.mRunnable).getQueuePriority();
            }
            return this.mPriorityQueue;
        }

        public void run() {
            Coordinator.runWithTiming(this.mRunnable);
        }
    }
}
