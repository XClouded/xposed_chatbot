package com.taobao.rxm.schedule;

import com.taobao.rxm.common.RxModel4Phenix;
import com.taobao.tcommon.core.Pool;
import java.util.Queue;
import java.util.concurrent.ConcurrentLinkedQueue;

public class ScheduledActionPool implements Pool<ScheduledAction> {
    private static final int DEFAULT_MAX_SIZE = 50;
    private final int mMaxSize;
    private final Queue<ScheduledAction> mRecycledQueue;

    public ScheduledActionPool() {
        this(50);
    }

    public ScheduledActionPool(int i) {
        this.mMaxSize = i;
        this.mRecycledQueue = new ConcurrentLinkedQueue();
    }

    public ScheduledAction offer() {
        if (RxModel4Phenix.isUseRecycle()) {
            return this.mRecycledQueue.poll();
        }
        return null;
    }

    public boolean recycle(ScheduledAction scheduledAction) {
        if (scheduledAction != null) {
            scheduledAction.reset();
        }
        if (!RxModel4Phenix.isUseRecycle() || this.mRecycledQueue.size() >= this.mMaxSize || !this.mRecycledQueue.offer(scheduledAction)) {
            return false;
        }
        return true;
    }
}
