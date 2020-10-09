package com.taobao.onlinemonitor;

import java.io.Serializable;
import java.util.ArrayList;

public final class ThreadInfo implements Serializable {
    int mBackGroundCpuTime;
    long mBootCpuTime;
    short mBootMaxPercentInDevice;
    short mBootMaxPercentInPid;
    String mClassName;
    int mCpuPercentInDevice;
    int mCpuPercentInPid;
    ArrayList<String> mCpuStacks;
    long mDeviceBootTotalTime;
    public long mDeviceFirstTotalTime;
    public long mDeviceLastTotalTime;
    long mEndTime;
    String mFirstActivity;
    long mFirstTime;
    int mId;
    ArrayList<String> mIncreaseThreadList;
    int mIoWaitCount;
    int mIoWaitTime;
    boolean mIsDaemon;
    int mLastIoWaitCount;
    int mLastIoWaitTime;
    int mLastSchedWaitCount;
    int mLastSchedWaitMax;
    int mLastSchedWaitSum;
    long mLastStime;
    long mLastUtime;
    float mMaxAvgPerCpu;
    public short mMaxPercentInDevice;
    public short mMaxPercentInPid;
    public String mName;
    public int mNice;
    long mPidBootTotalTime;
    int mPidFirstIoWaitCount;
    int mPidFirstIoWaitTime;
    int mPidFirstSchedWaitCount;
    int mPidFirstSchedWaitSum;
    public long mPidFirstTotalTime;
    int mPidLastIoWaitCount;
    int mPidLastIoWaitTime;
    int mPidLastSchedWaitCount;
    int mPidLastSchedWaitSum;
    public long mPidLastTotalTime;
    String mPoolName;
    int mQueuePriority;
    int mQueueSize;
    int mSchedWaitCount;
    int mSchedWaitMax;
    int mSchedWaitSum;
    String mStackTraceElements;
    int mStatus;
    public long mStime;
    int mThreadId;
    int mTooMuchLock;
    int mTotalThreadCount;
    public long mUtime;

    ThreadInfo(int i, String str, int i2, int i3, long j, long j2, boolean z, int i4, long j3, long j4, long j5, boolean z2, String str2, int i5, int i6, int i7, int i8, String str3) {
        long j6 = j;
        long j7 = j2;
        long j8 = j3;
        long j9 = j4;
        long j10 = j5;
        this.mId = i;
        this.mName = str;
        this.mStatus = i2;
        this.mThreadId = i3;
        this.mLastUtime = j6;
        this.mLastStime = j7;
        this.mUtime = j6;
        this.mStime = j7;
        this.mIsDaemon = z;
        this.mFirstTime = j8;
        this.mEndTime = j8;
        this.mNice = i4;
        this.mPidFirstTotalTime = j9;
        this.mDeviceFirstTotalTime = j10;
        this.mPidLastTotalTime = j9;
        this.mDeviceLastTotalTime = j10;
        if (z2) {
            this.mBackGroundCpuTime = (int) (((long) this.mBackGroundCpuTime) + j6 + j7);
        }
        this.mFirstActivity = str2;
        this.mPidFirstIoWaitCount = i5;
        this.mPidFirstIoWaitTime = i6;
        this.mPidFirstSchedWaitCount = i7;
        this.mPidFirstSchedWaitSum = i8;
        this.mStackTraceElements = str3;
    }

    public void updateThread(int i, long j, long j2, long j3, long j4, long j5, int i2, boolean z) {
        this.mStatus = i;
        this.mLastUtime = this.mUtime;
        this.mLastStime = this.mStime;
        this.mUtime = j;
        this.mStime = j2;
        this.mEndTime = j3;
        this.mNice = i2;
        if (z) {
            this.mBackGroundCpuTime = (int) (((long) this.mBackGroundCpuTime) + (((j + j2) - this.mLastUtime) - this.mLastStime));
        }
        if (j4 > 0) {
            long j6 = (((j + j2) - this.mLastUtime) - this.mLastStime) * 100;
            long j7 = j6 / j4;
            if (j7 > 100) {
                j7 = 100;
            }
            int i3 = (int) j7;
            this.mCpuPercentInPid = i3;
            if (((long) this.mMaxPercentInPid) < j7) {
                this.mMaxPercentInPid = (short) i3;
            }
            long j8 = j6 / j5;
            if (j8 > 100) {
                j8 = 100;
            }
            int i4 = (int) j8;
            this.mCpuPercentInDevice = i4;
            if (((long) this.mMaxPercentInDevice) < j8) {
                this.mMaxPercentInDevice = (short) i4;
            }
        }
    }

    public void onBootEnd() {
        this.mBootCpuTime = this.mStime + this.mUtime;
        this.mBootMaxPercentInPid = this.mMaxPercentInPid;
        this.mBootMaxPercentInDevice = this.mMaxPercentInDevice;
        this.mPidBootTotalTime = this.mPidLastTotalTime;
        this.mDeviceBootTotalTime = this.mDeviceLastTotalTime;
    }
}
