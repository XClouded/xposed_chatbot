package com.taobao.phenix.builder;

import com.taobao.phenix.chain.DefaultSchedulerSupplier;
import com.taobao.rxm.schedule.Scheduler;
import com.taobao.rxm.schedule.SchedulerSupplier;
import com.taobao.tcommon.core.Preconditions;

public class SchedulerBuilder implements Builder<SchedulerSupplier> {
    public static final int DEFAULT_CORE_SIZE = 3;
    public static final int DEFAULT_KEEP_ALIVE = 8;
    public static final int DEFAULT_MAX_RUNNING = 6;
    public static final int DEFAULT_PATIENCE_CAPACITY = 1500;
    public static final int DEFAULT_QUEUE_CAPACITY = 5;
    public static final int INVALID_NETWORK_RUNNING_EXPIRED = -1;
    public static final int MAX_DECODE_RUNNING = 3;
    public static final int MAX_NETWORK_RUNNING_AT_FAST = 5;
    public static final int MAX_NETWORK_RUNNING_AT_SLOW = 2;
    public static final int MIN_PATIENCE_CAPACITY = 500;
    public static final int VALID_NETWORK_RUNNING_EXPIRED = 25000;
    private Scheduler mCentralScheduler;
    private int mCoreSize = 3;
    private boolean mHaveBuilt;
    private int mKeepAliveSeconds = 8;
    private int mMaxDecodeRunning = 3;
    private int mMaxNetworkRunningAtFast = 5;
    private int mMaxNetworkRunningAtSlow = 2;
    private int mMaxRunning = 6;
    private int mNetworkRunningExpired = -1;
    private int mPatienceSize = 1500;
    private int mQueueSize = 5;
    private SchedulerSupplier mSchedulerSupplier;
    private boolean mUseNewThreadModel = true;

    public SchedulerBuilder with(SchedulerSupplier schedulerSupplier) {
        Preconditions.checkState(!this.mHaveBuilt, "SchedulerSupplier has been built, not allow with() now");
        this.mSchedulerSupplier = schedulerSupplier;
        return this;
    }

    public SchedulerBuilder central(Scheduler scheduler) {
        Preconditions.checkState(!this.mHaveBuilt, "SchedulerSupplier has been built, not allow central() now");
        this.mCentralScheduler = scheduler;
        return this;
    }

    public SchedulerBuilder coreSize(int i) {
        boolean z = true;
        Preconditions.checkState(!this.mHaveBuilt, "SchedulerSupplier has been built, not allow coreSize() now");
        if (i <= 0) {
            z = false;
        }
        Preconditions.checkState(z, "core size must be greater than zero");
        this.mCoreSize = i;
        return this;
    }

    public SchedulerBuilder maxRunning(int i) {
        boolean z = true;
        Preconditions.checkState(!this.mHaveBuilt, "SchedulerSupplier has been built, not allow maxRunning() now");
        if (this.mCentralScheduler == null) {
            if (i < this.mCoreSize) {
                z = false;
            }
            Preconditions.checkState(z, "max running cannot be lower than core size");
        } else {
            if (i <= 0) {
                z = false;
            }
            Preconditions.checkState(z, "max running must be greater than zero");
        }
        this.mMaxRunning = i;
        return this;
    }

    public SchedulerBuilder keepAlive(int i) {
        boolean z = true;
        Preconditions.checkState(!this.mHaveBuilt, "SchedulerSupplier has been built, not allow keepAlive() now");
        if (i <= 0) {
            z = false;
        }
        Preconditions.checkState(z, "keep alive time must be greater than zero");
        this.mKeepAliveSeconds = i;
        return this;
    }

    public SchedulerBuilder queueSize(int i) {
        boolean z = true;
        Preconditions.checkState(!this.mHaveBuilt, "SchedulerSupplier has been built, not allow queueSize() now");
        if (i <= 0) {
            z = false;
        }
        Preconditions.checkState(z, "queue size must be greater than zero");
        this.mQueueSize = i;
        return this;
    }

    public SchedulerBuilder patienceSize(int i) {
        boolean z = true;
        Preconditions.checkState(!this.mHaveBuilt, "SchedulerSupplier has been built, not allow patienceSize() now");
        if (i < 500) {
            z = false;
        }
        Preconditions.checkState(z, "patience size cannot be lower than 500");
        this.mPatienceSize = i;
        return this;
    }

    public SchedulerBuilder maxDecodeRunning(int i) {
        boolean z = true;
        Preconditions.checkState(!this.mHaveBuilt, "SchedulerSupplier has been built, not allow maxDecodeRunning() now");
        if (i > this.mMaxRunning) {
            z = false;
        }
        Preconditions.checkState(z, "max decode running cannot be greater than max running");
        this.mMaxDecodeRunning = i;
        return this;
    }

    public SchedulerBuilder maxNetworkRunningAtFast(int i) {
        boolean z = true;
        Preconditions.checkState(!this.mHaveBuilt, "SchedulerSupplier has been built, not allow maxNetworkRunningAtFast() now");
        if (i > this.mMaxRunning) {
            z = false;
        }
        Preconditions.checkState(z, "max network running at fast cannot be greater than max running");
        this.mMaxNetworkRunningAtFast = i;
        return this;
    }

    public SchedulerBuilder maxNetworkRunningAtSlow(int i) {
        boolean z = true;
        Preconditions.checkState(!this.mHaveBuilt, "SchedulerSupplier has been built, not allow maxNetworkRunningAtSlow() now");
        if (i > this.mMaxRunning) {
            z = false;
        }
        Preconditions.checkState(z, "max network running at slow cannot be greater than max running");
        this.mMaxNetworkRunningAtSlow = i;
        return this;
    }

    public SchedulerBuilder networkRunningExpired(int i) {
        Preconditions.checkState(!this.mHaveBuilt, "SchedulerSupplier has been built, not allow networkRunningExpired() now");
        this.mNetworkRunningExpired = i;
        return this;
    }

    public SchedulerBuilder useNewThreadModel(boolean z) {
        this.mUseNewThreadModel = z;
        return this;
    }

    public synchronized SchedulerSupplier build() {
        if (!this.mHaveBuilt) {
            if (this.mSchedulerSupplier == null) {
                this.mSchedulerSupplier = new DefaultSchedulerSupplier(this.mCentralScheduler, this.mCoreSize, this.mMaxRunning, this.mKeepAliveSeconds, this.mQueueSize, this.mPatienceSize, this.mMaxDecodeRunning, this.mMaxNetworkRunningAtFast, this.mMaxNetworkRunningAtSlow, this.mNetworkRunningExpired, this.mUseNewThreadModel);
                this.mHaveBuilt = true;
                return this.mSchedulerSupplier;
            }
        }
        return this.mSchedulerSupplier;
    }

    public boolean hasBuild() {
        return this.mHaveBuilt;
    }
}
