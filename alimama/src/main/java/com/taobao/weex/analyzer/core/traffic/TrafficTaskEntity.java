package com.taobao.weex.analyzer.core.traffic;

import android.os.Process;
import androidx.annotation.NonNull;
import com.taobao.weex.analyzer.core.TaskEntity;

public class TrafficTaskEntity implements TaskEntity<TrafficInfo> {
    private TrafficInfo mCachedTrafficInfo;
    private int mDelayInMillis = 1000;
    private double mTotalRxKBytes = 0.0d;
    private double mTotalTxKBytes = 0.0d;

    public static class TrafficInfo {
        public double rxSpeed;
        public double txSpeed;
    }

    public TrafficTaskEntity(int i) {
        this.mDelayInMillis = i;
    }

    public void onTaskInit() {
        this.mCachedTrafficInfo = new TrafficInfo();
    }

    @NonNull
    public TrafficInfo onTaskRun() {
        double d;
        double uidTxBytes = TrafficSampler.getUidTxBytes(Process.myUid()) / 1024.0d;
        double uidRxBytes = TrafficSampler.getUidRxBytes(Process.myUid()) / 1024.0d;
        int i = this.mDelayInMillis / 1000;
        double d2 = 0.0d;
        if (this.mTotalTxKBytes == 0.0d && this.mTotalRxKBytes == 0.0d) {
            d = 0.0d;
        } else {
            double d3 = (double) i;
            Double.isNaN(d3);
            d = Math.max(0.0d, (uidTxBytes - this.mTotalTxKBytes) / d3);
            Double.isNaN(d3);
            d2 = Math.max(0.0d, (uidRxBytes - this.mTotalRxKBytes) / d3);
        }
        if (this.mCachedTrafficInfo == null) {
            this.mCachedTrafficInfo = new TrafficInfo();
        }
        TrafficInfo trafficInfo = this.mCachedTrafficInfo;
        double round = (double) Math.round(d2 * 100.0d);
        Double.isNaN(round);
        trafficInfo.rxSpeed = round / 100.0d;
        TrafficInfo trafficInfo2 = this.mCachedTrafficInfo;
        double round2 = (double) Math.round(d * 100.0d);
        Double.isNaN(round2);
        trafficInfo2.txSpeed = round2 / 100.0d;
        this.mTotalRxKBytes = uidRxBytes;
        this.mTotalTxKBytes = uidTxBytes;
        return this.mCachedTrafficInfo;
    }

    public void onTaskStop() {
        this.mTotalRxKBytes = 0.0d;
        this.mTotalTxKBytes = 0.0d;
        this.mCachedTrafficInfo = null;
    }
}
