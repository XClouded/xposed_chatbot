package com.taobao.rxm.schedule;

public class MasterThrottlingScheduler implements ThrottlingScheduler, ExecutorStateInspector, ScheduledActionListener {
    private int mCurrentRunning;
    private final Scheduler mHostScheduler;
    private int mMaxRunningCount;
    private final CentralSchedulerQueue mScheduleQueue;

    public MasterThrottlingScheduler(Scheduler scheduler, int i, int i2, int i3) {
        this.mHostScheduler = scheduler;
        this.mMaxRunningCount = i;
        this.mScheduleQueue = new CentralSchedulerQueue(this, i2, i3);
    }

    public synchronized boolean isNotFull() {
        return this.mCurrentRunning < this.mMaxRunningCount;
    }

    private void handleReject(ScheduledAction scheduledAction) {
        scheduledAction.run();
    }

    private void scheduleInner(ScheduledAction scheduledAction, boolean z) {
        int moveIn;
        synchronized (this) {
            moveIn = this.mScheduleQueue.moveIn(scheduledAction, z);
            if (moveIn != 3) {
                this.mCurrentRunning++;
            }
        }
        if (moveIn == 1) {
            this.mHostScheduler.schedule(scheduledAction);
        } else if (moveIn == 2) {
            handleReject(scheduledAction);
        }
    }

    public void schedule(ScheduledAction scheduledAction) {
        scheduledAction.setMasterActionListener(this);
        scheduleInner(scheduledAction, true);
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
                if (this.mCurrentRunning < this.mMaxRunningCount || this.mScheduleQueue.reachPatienceCapacity()) {
                    scheduledAction2 = (ScheduledAction) this.mScheduleQueue.poll();
                }
            }
            if (scheduledAction2 != null) {
                scheduleInner(scheduledAction2, false);
                ScheduledAction.sActionCallerThreadLocal.set(scheduledAction);
            } else {
                return;
            }
        }
        while (true) {
        }
    }

    public synchronized String getStatus() {
        return "MasterThrottling[running=" + this.mCurrentRunning + ", max=" + this.mMaxRunningCount + "]," + this.mHostScheduler.getStatus();
    }

    public synchronized boolean isScheduleMainThread() {
        return this.mHostScheduler.isScheduleMainThread();
    }

    public int getQueueSize() {
        return this.mScheduleQueue.size();
    }

    public void onActionFinished(ScheduledAction scheduledAction) {
        synchronized (this) {
            this.mCurrentRunning--;
        }
        checkRunningCount();
    }
}
