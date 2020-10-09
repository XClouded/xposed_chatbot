package com.taobao.rxm.schedule;

import android.os.Handler;
import android.os.Looper;
import com.taobao.rxm.common.RxModel4Phenix;
import java.util.concurrent.ConcurrentLinkedQueue;

public class UiThreadSchedulerFront implements Scheduler, Runnable {
    private static final long INIT_POST_TIME = System.currentTimeMillis();
    private static final int MAX_COST_TIME = 8;
    private static final long MAX_POST_TIME = 4000;
    private static final int MAX_RECURSIVE_DEPTH = 10;
    private long mCostTime;
    private int mCurrRecursiveDepth;
    private final Handler mHandler = new Handler(Looper.getMainLooper());
    private final ConcurrentLinkedQueue<ScheduledAction> mQueue = new ConcurrentLinkedQueue<>();

    public boolean isScheduleMainThread() {
        return true;
    }

    public void run() {
        int i = this.mCurrRecursiveDepth + 1;
        this.mCurrRecursiveDepth = i;
        if (i > 10 || this.mCostTime > 8) {
            this.mCurrRecursiveDepth = 0;
            this.mCostTime = 0;
            if (this.mQueue.isEmpty()) {
                return;
            }
            if (isUsePostAtFront()) {
                this.mHandler.postAtFrontOfQueue(this);
            } else {
                this.mHandler.post(this);
            }
        } else {
            ScheduledAction poll = this.mQueue.poll();
            if (poll != null) {
                long currentTimeMillis = System.currentTimeMillis();
                poll.run();
                this.mCostTime += System.currentTimeMillis() - currentTimeMillis;
                run();
            }
        }
    }

    public void schedule(final ScheduledAction scheduledAction) {
        if (RxModel4Phenix.isUseNewThread()) {
            this.mHandler.post(new Runnable() {
                public void run() {
                    scheduledAction.run();
                }
            });
            return;
        }
        boolean isEmpty = this.mQueue.isEmpty();
        this.mQueue.add(scheduledAction);
        if (isEmpty && !this.mQueue.isEmpty()) {
            if (isUsePostAtFront()) {
                this.mHandler.postAtFrontOfQueue(this);
            } else {
                this.mHandler.post(this);
            }
        }
    }

    private boolean isUsePostAtFront() {
        return RxModel4Phenix.isUsePostAtFront() && System.currentTimeMillis() - INIT_POST_TIME < MAX_POST_TIME;
    }

    public String getStatus() {
        return "ui thread scheduler status:\nqueue size:" + getQueueSize();
    }

    public int getQueueSize() {
        return this.mQueue.size();
    }
}
