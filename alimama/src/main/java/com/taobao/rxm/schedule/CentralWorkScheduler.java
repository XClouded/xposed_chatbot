package com.taobao.rxm.schedule;

import com.taobao.rxm.common.Constant;
import com.taobao.tcommon.core.Preconditions;
import com.taobao.tcommon.log.FLog;
import java.util.concurrent.RejectedExecutionHandler;
import java.util.concurrent.ThreadFactory;
import java.util.concurrent.ThreadPoolExecutor;
import java.util.concurrent.TimeUnit;
import java.util.concurrent.atomic.AtomicInteger;

public class CentralWorkScheduler implements Scheduler, ExecutorStateInspector {
    private final CentralSchedulerQueue mCentralQueue;
    private final ThreadPoolExecutor mExecutor;
    /* access modifiers changed from: private */
    public final String mName;
    /* access modifiers changed from: private */
    public final AtomicInteger mThreadNumber;

    public boolean isScheduleMainThread() {
        return false;
    }

    public CentralWorkScheduler(String str, int i, int i2, int i3, int i4) {
        this(str, i, i2, i3, i4, 1500);
    }

    public CentralWorkScheduler(String str, int i, int i2, int i3, int i4, int i5) {
        this.mThreadNumber = new AtomicInteger(1);
        boolean z = false;
        Preconditions.checkArgument(i >= 0, "corePoolSize must be >=0");
        Preconditions.checkArgument(i2 >= i ? true : z, "maxPoolSize shouldn't be less than corePoolSize");
        this.mName = str;
        this.mCentralQueue = new CentralSchedulerQueue(this, i4, i5);
        this.mExecutor = new ThreadPoolExecutor(i, i2, (long) i3, TimeUnit.SECONDS, this.mCentralQueue, new ThreadFactory() {
            public Thread newThread(Runnable runnable) {
                Thread thread = new Thread(runnable, CentralWorkScheduler.this.mName + CentralWorkScheduler.this.mThreadNumber.getAndIncrement());
                if (thread.isDaemon()) {
                    thread.setDaemon(false);
                }
                if (thread.getPriority() != 5) {
                    thread.setPriority(5);
                }
                return thread;
            }
        }, new RejectedExecutionHandler() {
            public void rejectedExecution(Runnable runnable, ThreadPoolExecutor threadPoolExecutor) {
                FLog.d(Constant.RX_LOG, "queue is full and no more available thread, directly run in thread(%s)", Thread.currentThread().getName());
                if (!threadPoolExecutor.isShutdown()) {
                    runnable.run();
                }
            }
        });
    }

    public int getPoolSize() {
        return this.mExecutor.getPoolSize();
    }

    public String getName() {
        return this.mName;
    }

    public void schedule(ScheduledAction scheduledAction) {
        if (FLog.isLoggable(3)) {
            FLog.d(Constant.RX_LOG, getStatus(), new Object[0]);
        }
        this.mExecutor.execute(scheduledAction);
    }

    public String getStatus() {
        return this.mName + " status: queue=" + this.mCentralQueue.size() + " active=" + this.mExecutor.getActiveCount() + " pool=" + this.mExecutor.getPoolSize() + " largest=" + this.mExecutor.getLargestPoolSize();
    }

    public int getQueueSize() {
        return this.mCentralQueue.size();
    }

    public boolean isNotFull() {
        return this.mExecutor.getPoolSize() < this.mExecutor.getMaximumPoolSize();
    }
}
