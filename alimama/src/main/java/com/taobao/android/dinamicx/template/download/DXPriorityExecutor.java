package com.taobao.android.dinamicx.template.download;

import androidx.annotation.NonNull;
import java.util.Comparator;
import java.util.concurrent.BlockingQueue;
import java.util.concurrent.PriorityBlockingQueue;
import java.util.concurrent.ThreadFactory;
import java.util.concurrent.ThreadPoolExecutor;
import java.util.concurrent.TimeUnit;
import java.util.concurrent.atomic.AtomicInteger;
import java.util.concurrent.atomic.AtomicLong;

public class DXPriorityExecutor extends ThreadPoolExecutor {
    private static final int CORE_POOL_SIZE = 5;
    private static final Comparator FIFO = new Comparator() {
        public int compare(Object obj, Object obj2) {
            if (!(obj instanceof DXPriorityRunnable) || !(obj2 instanceof DXPriorityRunnable)) {
                return 0;
            }
            DXPriorityRunnable dXPriorityRunnable = (DXPriorityRunnable) obj;
            DXPriorityRunnable dXPriorityRunnable2 = (DXPriorityRunnable) obj2;
            int i = dXPriorityRunnable.priority - dXPriorityRunnable2.priority;
            return i == 0 ? (int) (dXPriorityRunnable.SEQ - dXPriorityRunnable2.SEQ) : i;
        }
    };
    private static final int KEEP_ALIVE = 1;
    private static final Comparator LIFO = new Comparator() {
        public int compare(Object obj, Object obj2) {
            if (!(obj instanceof DXPriorityRunnable) || !(obj2 instanceof DXPriorityRunnable)) {
                return 0;
            }
            DXPriorityRunnable dXPriorityRunnable = (DXPriorityRunnable) obj;
            DXPriorityRunnable dXPriorityRunnable2 = (DXPriorityRunnable) obj2;
            int i = dXPriorityRunnable.priority - dXPriorityRunnable2.priority;
            return i == 0 ? (int) (dXPriorityRunnable2.SEQ - dXPriorityRunnable.SEQ) : i;
        }
    };
    private static final int MAXIMUM_POOL_SIZE = 128;
    private static final AtomicLong SEQ_SEED = new AtomicLong(0);
    private static final ThreadFactory sThreadFactory = new ThreadFactory() {
        private final AtomicInteger mCount = new AtomicInteger(1);

        public Thread newThread(@NonNull Runnable runnable) {
            return new Thread(runnable, "download#" + this.mCount.getAndIncrement());
        }
    };

    public DXPriorityExecutor(boolean z) {
        this(5, z);
    }

    /* JADX INFO: this call moved to the top of the method (can break code semantics) */
    public DXPriorityExecutor(int i, boolean z) {
        this(i, 128, 1, TimeUnit.SECONDS, new PriorityBlockingQueue(128, z ? FIFO : LIFO), sThreadFactory);
    }

    public DXPriorityExecutor(int i, int i2, long j, TimeUnit timeUnit, BlockingQueue blockingQueue, ThreadFactory threadFactory) {
        super(i, i2, j, timeUnit, blockingQueue, threadFactory);
    }

    public boolean isBusy() {
        return getActiveCount() >= getCorePoolSize();
    }

    public void clear() {
        getQueue().clear();
    }

    public void execute(Runnable runnable) {
        if (runnable instanceof DXPriorityRunnable) {
            ((DXPriorityRunnable) runnable).SEQ = SEQ_SEED.getAndIncrement();
        }
        super.execute(runnable);
    }
}
