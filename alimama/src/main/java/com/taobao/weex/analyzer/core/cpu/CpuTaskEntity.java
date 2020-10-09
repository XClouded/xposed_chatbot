package com.taobao.weex.analyzer.core.cpu;

import android.text.TextUtils;
import androidx.annotation.NonNull;
import com.taobao.weex.analyzer.core.TaskEntity;
import com.taobao.weex.el.parse.Operators;

public class CpuTaskEntity implements TaskEntity<CpuInfo> {
    private CpuInfo mCachedCpuInfo;
    private long mPidKernelCpuTimeLast = 0;
    private long mPidTotalCpuTimeLast = 0;
    private long mPidUserCpuTimeLast = 0;
    private long mTotalCpuTimeLast = 0;

    public static class CpuInfo {
        public double pidCpuUsage;
        public double pidKernelCpuUsage;
        public double pidUserCpuUsage;
    }

    public void onTaskInit() {
        this.mTotalCpuTimeLast = 0;
        this.mPidTotalCpuTimeLast = 0;
        this.mPidUserCpuTimeLast = 0;
        this.mPidKernelCpuTimeLast = 0;
        this.mCachedCpuInfo = new CpuInfo();
    }

    @NonNull
    public CpuInfo onTaskRun() {
        double d;
        double d2;
        double d3;
        String samplePidCpuRate = CpuSampler.samplePidCpuRate();
        String sampleCpuRate = CpuSampler.sampleCpuRate();
        if (this.mCachedCpuInfo == null) {
            this.mCachedCpuInfo = new CpuInfo();
        }
        if (TextUtils.isEmpty(samplePidCpuRate) || TextUtils.isEmpty(sampleCpuRate)) {
            this.mCachedCpuInfo.pidCpuUsage = 0.0d;
            this.mCachedCpuInfo.pidKernelCpuUsage = 0.0d;
            this.mCachedCpuInfo.pidUserCpuUsage = 0.0d;
            return this.mCachedCpuInfo;
        }
        String[] split = sampleCpuRate.split(Operators.SPACE_STR);
        if (split.length < 9) {
            this.mCachedCpuInfo.pidCpuUsage = 0.0d;
            this.mCachedCpuInfo.pidKernelCpuUsage = 0.0d;
            this.mCachedCpuInfo.pidUserCpuUsage = 0.0d;
            return this.mCachedCpuInfo;
        }
        String[] split2 = samplePidCpuRate.split(Operators.SPACE_STR);
        if (split2.length < 17) {
            this.mCachedCpuInfo.pidCpuUsage = 0.0d;
            this.mCachedCpuInfo.pidKernelCpuUsage = 0.0d;
            this.mCachedCpuInfo.pidUserCpuUsage = 0.0d;
            return this.mCachedCpuInfo;
        }
        long parseLong = Long.parseLong(split[2]);
        long parseLong2 = Long.parseLong(split[3]);
        long parseLong3 = Long.parseLong(split[4]);
        long parseLong4 = Long.parseLong(split[5]);
        long parseLong5 = Long.parseLong(split[6]);
        long parseLong6 = Long.parseLong(split[7]);
        long parseLong7 = Long.parseLong(split[8]);
        long parseLong8 = Long.parseLong(split[9]);
        long parseLong9 = Long.parseLong(split2[13]);
        long parseLong10 = Long.parseLong(split2[14]);
        long parseLong11 = Long.parseLong(split2[15]);
        long j = parseLong + parseLong2 + parseLong3 + parseLong4 + parseLong5 + parseLong6 + parseLong7 + parseLong8;
        long parseLong12 = parseLong9 + parseLong10 + parseLong11 + Long.parseLong(split2[16]);
        if (this.mTotalCpuTimeLast != 0) {
            double d4 = (double) ((parseLong9 - this.mPidUserCpuTimeLast) * 100);
            double d5 = (double) (j - this.mTotalCpuTimeLast);
            Double.isNaN(d4);
            Double.isNaN(d5);
            d3 = d4 / d5;
            double d6 = (double) ((parseLong10 - this.mPidKernelCpuTimeLast) * 100);
            double d7 = (double) (j - this.mTotalCpuTimeLast);
            Double.isNaN(d6);
            Double.isNaN(d7);
            d2 = d6 / d7;
            d = d3 + d2;
        } else {
            d3 = 0.0d;
            d2 = 0.0d;
            d = 0.0d;
        }
        this.mCachedCpuInfo.pidCpuUsage = Math.max(0.0d, d);
        this.mCachedCpuInfo.pidUserCpuUsage = Math.max(0.0d, d3);
        this.mCachedCpuInfo.pidKernelCpuUsage = Math.max(0.0d, d2);
        this.mTotalCpuTimeLast = j;
        this.mPidTotalCpuTimeLast = parseLong12;
        this.mPidUserCpuTimeLast = parseLong9;
        this.mPidKernelCpuTimeLast = parseLong10;
        return this.mCachedCpuInfo;
    }

    public void onTaskStop() {
        this.mTotalCpuTimeLast = 0;
        this.mPidTotalCpuTimeLast = 0;
        this.mPidUserCpuTimeLast = 0;
        this.mPidKernelCpuTimeLast = 0;
        this.mCachedCpuInfo = null;
    }
}
