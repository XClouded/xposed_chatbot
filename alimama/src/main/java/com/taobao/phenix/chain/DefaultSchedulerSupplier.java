package com.taobao.phenix.chain;

import com.taobao.phenix.common.UnitedLog;
import com.taobao.phenix.loader.network.NetworkQualityListener;
import com.taobao.rxm.schedule.BranchThrottlingScheduler;
import com.taobao.rxm.schedule.CentralWorkScheduler;
import com.taobao.rxm.schedule.MasterThrottlingScheduler;
import com.taobao.rxm.schedule.PairingThrottlingScheduler;
import com.taobao.rxm.schedule.Scheduler;
import com.taobao.rxm.schedule.SchedulerSupplier;
import com.taobao.rxm.schedule.ThrottlingScheduler;
import com.taobao.rxm.schedule.UiThreadScheduler;
import com.taobao.rxm.schedule.UiThreadSchedulerFront;

public class DefaultSchedulerSupplier implements SchedulerSupplier, NetworkQualityListener {
    private static boolean mUsePostFrontUIScheduler;
    private final Scheduler mCentralScheduler;
    private Scheduler mDecodeScheduler;
    private boolean mIsLastSpeedSlow;
    private final int mMaxNetworkRunningAtFast;
    private final int mMaxNetworkRunningAtSlow;
    private ThrottlingScheduler mNetworkScheduler;
    private Scheduler mUiThreadScheduler;

    public DefaultSchedulerSupplier() {
        this((Scheduler) null, 3, 6, 8, 5, 1500, 3, 5, 2, -1);
    }

    public DefaultSchedulerSupplier(Scheduler scheduler, int i, int i2, int i3, int i4, int i5, int i6, int i7, int i8) {
        this(scheduler, i, i2, i3, i4, i5, i6, i7, i8, -1);
    }

    public DefaultSchedulerSupplier(Scheduler scheduler, int i, int i2, int i3, int i4, int i5, int i6, int i7, int i8, int i9) {
        this(scheduler, i, i2, i3, i4, i5, i6, i7, i8, i9, false);
    }

    public DefaultSchedulerSupplier(Scheduler scheduler, int i, int i2, int i3, int i4, int i5, int i6, int i7, int i8, int i9, boolean z) {
        int i10 = i9;
        if (scheduler == null || z) {
            int i11 = i2;
            int i12 = i4;
            int i13 = i5;
            this.mCentralScheduler = new CentralWorkScheduler("Phenix-Scheduler", i, i2, i3, i4, i5);
        } else {
            int i14 = i2;
            this.mCentralScheduler = new MasterThrottlingScheduler(scheduler, i2, i4, i5);
        }
        this.mMaxNetworkRunningAtFast = i7;
        this.mMaxNetworkRunningAtSlow = i8;
        if (i10 > 0) {
            this.mNetworkScheduler = new PairingThrottlingScheduler(this.mCentralScheduler, this.mMaxNetworkRunningAtFast, i10);
        } else {
            this.mNetworkScheduler = new BranchThrottlingScheduler(this.mCentralScheduler, this.mMaxNetworkRunningAtFast);
        }
        this.mDecodeScheduler = new BranchThrottlingScheduler(this.mCentralScheduler, i6);
    }

    public static void setUsePostFrontUI(boolean z) {
        mUsePostFrontUIScheduler = z;
    }

    public synchronized void onNetworkQualityChanged(boolean z) {
        if (this.mIsLastSpeedSlow == z) {
            Object[] objArr = new Object[1];
            objArr[0] = z ? "SLOW" : "FAST";
            UnitedLog.i("Network", "network speed not changed, still %s", objArr);
            return;
        }
        if (z) {
            UnitedLog.i("Network", "network speed changed from FAST to SLOW", new Object[0]);
            this.mNetworkScheduler.setMaxRunningCount(this.mMaxNetworkRunningAtSlow);
        } else {
            UnitedLog.i("Network", "network speed changed from SLOW to FAST", new Object[0]);
            this.mNetworkScheduler.setMaxRunningCount(this.mMaxNetworkRunningAtFast);
        }
        this.mIsLastSpeedSlow = z;
    }

    public Scheduler forIoBound() {
        return this.mCentralScheduler;
    }

    public Scheduler forNetwork() {
        return this.mNetworkScheduler;
    }

    public Scheduler forDecode() {
        return this.mDecodeScheduler;
    }

    public Scheduler forCpuBound() {
        return this.mCentralScheduler;
    }

    public Scheduler forUiThread() {
        if (this.mUiThreadScheduler == null) {
            this.mUiThreadScheduler = mUsePostFrontUIScheduler ? new UiThreadSchedulerFront() : new UiThreadScheduler();
        }
        return this.mUiThreadScheduler;
    }
}
