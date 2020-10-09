package com.taobao.rxm.schedule;

import java.util.LinkedList;
import java.util.Queue;

public class BranchThrottlingScheduler implements ThrottlingScheduler, ScheduledActionListener {
    private int mCurrentRunning;
    private final Scheduler mHostScheduler;
    private int mMaxRunningCount;
    private final Queue<ScheduledAction> mWaitQueue = new LinkedList();

    public BranchThrottlingScheduler(Scheduler scheduler, int i) {
        this.mHostScheduler = scheduler;
        this.mMaxRunningCount = i;
    }

    /* JADX WARNING: Removed duplicated region for block: B:11:0x0019  */
    /* Code decompiled incorrectly, please refer to instructions dump. */
    public void schedule(com.taobao.rxm.schedule.ScheduledAction r4) {
        /*
            r3 = this;
            r4.setBranchActionListener(r3)
            monitor-enter(r3)
            int r0 = r3.mCurrentRunning     // Catch:{ all -> 0x0027 }
            int r1 = r3.mMaxRunningCount     // Catch:{ all -> 0x0027 }
            r2 = 1
            if (r0 < r1) goto L_0x0016
            java.util.Queue<com.taobao.rxm.schedule.ScheduledAction> r0 = r3.mWaitQueue     // Catch:{ all -> 0x0027 }
            boolean r0 = r0.offer(r4)     // Catch:{ all -> 0x0027 }
            if (r0 != 0) goto L_0x0014
            goto L_0x0016
        L_0x0014:
            r0 = 0
            goto L_0x0017
        L_0x0016:
            r0 = 1
        L_0x0017:
            if (r0 == 0) goto L_0x001e
            int r1 = r3.mCurrentRunning     // Catch:{ all -> 0x0027 }
            int r1 = r1 + r2
            r3.mCurrentRunning = r1     // Catch:{ all -> 0x0027 }
        L_0x001e:
            monitor-exit(r3)     // Catch:{ all -> 0x0027 }
            if (r0 == 0) goto L_0x0026
            com.taobao.rxm.schedule.Scheduler r0 = r3.mHostScheduler
            r0.schedule(r4)
        L_0x0026:
            return
        L_0x0027:
            r4 = move-exception
            monitor-exit(r3)     // Catch:{ all -> 0x0027 }
            throw r4
        */
        throw new UnsupportedOperationException("Method not decompiled: com.taobao.rxm.schedule.BranchThrottlingScheduler.schedule(com.taobao.rxm.schedule.ScheduledAction):void");
    }

    public void setMaxRunningCount(int i) {
        synchronized (this) {
            this.mMaxRunningCount = i;
        }
        checkRunningCount();
    }

    private void checkRunningCount() {
        ScheduledAction scheduledAction = ScheduledAction.sActionCallerThreadLocal.get();
        while (true) {
            ScheduledAction scheduledAction2 = null;
            synchronized (this) {
                if (this.mCurrentRunning < this.mMaxRunningCount) {
                    scheduledAction2 = this.mWaitQueue.poll();
                }
                if (scheduledAction2 != null) {
                    this.mCurrentRunning++;
                }
            }
            if (scheduledAction2 != null) {
                this.mHostScheduler.schedule(scheduledAction2);
                ScheduledAction.sActionCallerThreadLocal.set(scheduledAction);
            } else {
                return;
            }
        }
        while (true) {
        }
    }

    public synchronized String getStatus() {
        return this.mHostScheduler.getStatus();
    }

    public synchronized boolean isScheduleMainThread() {
        return this.mHostScheduler.isScheduleMainThread();
    }

    public int getQueueSize() {
        return this.mWaitQueue.size();
    }

    public void onActionFinished(ScheduledAction scheduledAction) {
        synchronized (this) {
            this.mCurrentRunning--;
        }
        checkRunningCount();
    }
}
