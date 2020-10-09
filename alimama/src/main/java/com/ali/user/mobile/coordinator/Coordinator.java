package com.ali.user.mobile.coordinator;

import android.annotation.TargetApi;
import android.os.Looper;
import android.os.Process;
import android.taobao.windvane.cache.WVFileInfo;
import com.taobao.weex.el.parse.Operators;
import java.lang.reflect.Field;
import java.util.Comparator;
import java.util.concurrent.BlockingQueue;
import java.util.concurrent.PriorityBlockingQueue;
import java.util.concurrent.RejectedExecutionException;
import java.util.concurrent.RejectedExecutionHandler;
import java.util.concurrent.ThreadFactory;
import java.util.concurrent.ThreadPoolExecutor;
import java.util.concurrent.TimeUnit;
import kotlin.text.Typography;

public class Coordinator {
    private static final int CORE_POOL_SIZE = 8;
    private static final int KEEP_ALIVE = 10;
    private static final int MAXIMUM_POOL_SIZE = 128;
    public static final int QUEUE_PRIORITY_NORMAL = 30;
    protected static final String TAG = "Coordinator";
    protected static final BlockingQueue<Runnable> mPoolWorkQueue = new PriorityBlockingQueue(100, new PriorityComparator());
    private static final ThreadFactory sThreadFactory = new ThreadFactory() {
        public Thread newThread(Runnable runnable) {
            return new Thread(runnable, "login#" + runnable.getClass().getName());
        }
    };
    static CoordThreadPoolExecutor sThreadPoolExecutor = new CoordThreadPoolExecutor(8, 128, 10, TimeUnit.SECONDS, mPoolWorkQueue, sThreadFactory, new CoordinatorRejectHandler());

    public interface PriorityQueue {
        int getQueuePriority();
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

    public static void execute(Runnable runnable) {
        sThreadPoolExecutor.execute(runnable, 30);
    }

    public static void execute(Runnable runnable, int i) {
        sThreadPoolExecutor.execute(runnable, i);
    }

    protected static void runWithTiming(Runnable runnable) {
        if (Looper.getMainLooper() != Looper.myLooper()) {
            Process.setThreadPriority(10);
        }
        try {
            runnable.run();
        } catch (Throwable th) {
            th.printStackTrace();
        }
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
            allowCoreThreadTimeOut(true);
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

        /* access modifiers changed from: protected */
        public void beforeExecute(Thread thread, Runnable runnable) {
            super.beforeExecute(thread, runnable);
            if (runnable instanceof StandaloneTask) {
                thread.setName(((StandaloneTask) runnable).mRunnable + "");
                return;
            }
            thread.setName(runnable + "");
        }

        /* access modifiers changed from: protected */
        @TargetApi(11)
        public void afterExecute(Runnable runnable, Throwable th) {
            super.afterExecute(runnable, th);
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
