package com.taobao.rxm.schedule;

import android.os.Handler;
import android.os.Looper;
import java.util.PriorityQueue;

public class UiThreadScheduler implements Scheduler, Runnable {
    private static final int MAX_COST_TIME = 8;
    private static final int MAX_RECURSIVE_DEPTH = 10;
    private static final int PRIORITY_QUEUE_CAPACITY = 200;
    private long mCostTime;
    private int mCurrRecursiveDepth;
    private boolean mExecutionInProgress;
    private final Handler mHandler = new Handler(Looper.getMainLooper());
    private final PriorityQueue<ScheduledAction> mPriorityQueue = new PriorityQueue<>(200);

    public boolean isScheduleMainThread() {
        return true;
    }

    public void run() {
        ScheduledAction poll;
        int i = this.mCurrRecursiveDepth + 1;
        this.mCurrRecursiveDepth = i;
        if (i > 10 || this.mCostTime > 8) {
            this.mCurrRecursiveDepth = 0;
            this.mCostTime = 0;
            synchronized (this) {
                if (this.mPriorityQueue.size() > 0) {
                    this.mHandler.post(this);
                } else {
                    this.mExecutionInProgress = false;
                }
            }
            return;
        }
        synchronized (this) {
            poll = this.mPriorityQueue.poll();
        }
        if (poll != null) {
            long currentTimeMillis = System.currentTimeMillis();
            poll.run();
            this.mCostTime += System.currentTimeMillis() - currentTimeMillis;
            run();
            return;
        }
        synchronized (this) {
            this.mExecutionInProgress = false;
        }
    }

    /* JADX WARNING: Code restructure failed: missing block: B:12:0x002d, code lost:
        return;
     */
    /* JADX WARNING: Code restructure failed: missing block: B:14:0x002f, code lost:
        return;
     */
    /* Code decompiled incorrectly, please refer to instructions dump. */
    public synchronized void schedule(final com.taobao.rxm.schedule.ScheduledAction r3) {
        /*
            r2 = this;
            monitor-enter(r2)
            boolean r0 = com.taobao.rxm.common.RxModel4Phenix.isUseNewThread()     // Catch:{ all -> 0x0030 }
            if (r0 == 0) goto L_0x0012
            android.os.Handler r0 = r2.mHandler     // Catch:{ all -> 0x0030 }
            com.taobao.rxm.schedule.UiThreadScheduler$1 r1 = new com.taobao.rxm.schedule.UiThreadScheduler$1     // Catch:{ all -> 0x0030 }
            r1.<init>(r3)     // Catch:{ all -> 0x0030 }
            r0.post(r1)     // Catch:{ all -> 0x0030 }
            goto L_0x002c
        L_0x0012:
            java.util.PriorityQueue<com.taobao.rxm.schedule.ScheduledAction> r0 = r2.mPriorityQueue     // Catch:{ all -> 0x0030 }
            r0.add(r3)     // Catch:{ all -> 0x0030 }
            boolean r3 = r2.mExecutionInProgress     // Catch:{ all -> 0x0030 }
            if (r3 != 0) goto L_0x002e
            java.util.PriorityQueue<com.taobao.rxm.schedule.ScheduledAction> r3 = r2.mPriorityQueue     // Catch:{ all -> 0x0030 }
            boolean r3 = r3.isEmpty()     // Catch:{ all -> 0x0030 }
            if (r3 == 0) goto L_0x0024
            goto L_0x002e
        L_0x0024:
            r3 = 1
            r2.mExecutionInProgress = r3     // Catch:{ all -> 0x0030 }
            android.os.Handler r3 = r2.mHandler     // Catch:{ all -> 0x0030 }
            r3.post(r2)     // Catch:{ all -> 0x0030 }
        L_0x002c:
            monitor-exit(r2)
            return
        L_0x002e:
            monitor-exit(r2)
            return
        L_0x0030:
            r3 = move-exception
            monitor-exit(r2)
            throw r3
        */
        throw new UnsupportedOperationException("Method not decompiled: com.taobao.rxm.schedule.UiThreadScheduler.schedule(com.taobao.rxm.schedule.ScheduledAction):void");
    }

    public String getStatus() {
        return "ui thread scheduler status:\nqueue size:" + getQueueSize() + "\nexecuting:" + this.mExecutionInProgress;
    }

    public int getQueueSize() {
        return this.mPriorityQueue.size();
    }
}
