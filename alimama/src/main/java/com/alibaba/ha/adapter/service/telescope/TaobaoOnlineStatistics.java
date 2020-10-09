package com.alibaba.ha.adapter.service.telescope;

import android.app.Activity;
import android.util.Log;
import com.alibaba.android.umbrella.utils.UmbrellaConstants;
import com.alibaba.motu.crashreporter.CrashReport;
import com.alibaba.motu.watch.mainRunLoop.MainLooperHandler;
import com.alibaba.mtl.appmonitor.AppMonitor;
import com.alibaba.mtl.appmonitor.model.DimensionSet;
import com.alibaba.mtl.appmonitor.model.DimensionValueSet;
import com.alibaba.mtl.appmonitor.model.MeasureSet;
import com.alibaba.mtl.appmonitor.model.MeasureValueSet;
import com.taobao.android.dinamic.expressionv2.DinamicTokenizer;
import com.taobao.android.dinamicx.template.utils.DXTemplateNamePathUtil;
import com.taobao.onlinemonitor.OnLineMonitor;
import com.taobao.onlinemonitor.OnLineMonitorApp;
import com.taobao.onlinemonitor.OnlineStatistics;
import com.taobao.onlinemonitor.OutputData;
import com.taobao.onlinemonitor.ThreadInfo;
import com.taobao.weex.el.parse.Operators;
import com.taobao.weex.utils.WXUtils;
import java.lang.reflect.Field;
import java.util.HashMap;
import java.util.HashSet;
import java.util.Iterator;
import java.util.List;
import java.util.Map;
import java.util.StringTokenizer;
import java.util.concurrent.ThreadPoolExecutor;

public class TaobaoOnlineStatistics implements OnlineStatistics {
    public static final int MAX_TIME = 30000;
    public static boolean sTestAppMonitorLog = false;
    public String TAG = "OnLineMonitor";
    boolean mAnrReg = false;
    boolean mBlockOrCloseGuard = false;
    boolean mCommitResourceReg;
    Field mFieldThread;
    Field mFieldWorkers;
    boolean mGotoSleepReg;
    boolean mIsHotBootCommit = false;
    boolean mMemoryLeackRegisted = false;
    boolean mOnMemoryProblemReg;
    boolean mSmoothRegisted = false;
    StringBuilder mStringBuilderForLog = new StringBuilder(200);
    boolean mThreadPoolRegisted = false;
    boolean mWhiteScreenRegisted = false;

    public void onCreatePerformanceReport(OnLineMonitor.OnLineStat onLineStat, OutputData outputData) {
    }

    public void onSmoothStop(OnLineMonitor.OnLineStat onLineStat, OnLineMonitor.ActivityRuntimeInfo activityRuntimeInfo, long j, int i, int[] iArr, int i2, String str, int i3, long j2, int i4, int i5, String str2, int i6) {
    }

    public void onBootFinished(OnLineMonitor.OnLineStat onLineStat, long j, long j2, boolean z, String str) {
        OnLineMonitor.OnLineStat onLineStat2 = onLineStat;
        long j3 = j;
        long j4 = j2;
        boolean z2 = z;
        String str2 = str;
        if (j3 > 0 && j3 <= 30000) {
            if (z2 || !this.mIsHotBootCommit) {
                DimensionSet addDimension = DimensionSet.create().addDimension("isFirstInstall").addDimension("CpuCore").addDimension("APILevel").addDimension("IsLowMemory").addDimension("MemoryLevel").addDimension("BootType");
                addDimension.addDimension("Info");
                AppMonitor.register("system", "LaunchAll", MeasureSet.create().addMeasure("BootTotalTime").addMeasure("loadTime").addMeasure("BlockingGCCount").addMeasure("CpuMaxFreq").addMeasure("DeviceMem").addMeasure("DeviceAvailMem").addMeasure("TotalUsedMem").addMeasure("RemainMem").addMeasure("NativeHeapSize").addMeasure("JavaHeapSize").addMeasure("SysCpuPercent").addMeasure("PidCpuPercent").addMeasure("IOWaitTime").addMeasure("SysLoadAvg").addMeasure("RuntimeThread").addMeasure("RunningThread").addMeasure("DeviceScore").addMeasure("SysScore").addMeasure("PidScore").addMeasure("PidPrepareTime").addMeasure("AdvTime"), addDimension);
                DimensionValueSet create = DimensionValueSet.create();
                MeasureValueSet create2 = MeasureValueSet.create();
                DimensionValuesAdapter dimensionValuesAdapter = new DimensionValuesAdapter();
                MeasureValuesAdapter measureValuesAdapter = new MeasureValuesAdapter();
                double d = (double) j4;
                create2.setValue("BootTotalTime", d);
                double d2 = (double) j3;
                create2.setValue("loadTime", d2);
                measureValuesAdapter.setValue("BootTotalTime", d);
                measureValuesAdapter.setValue("loadTime", d2);
                if (onLineStat2.activityRuntimeInfo != null) {
                    create2.setValue("BlockingGCCount", (double) onLineStat2.activityRuntimeInfo.blockGc);
                    create2.setValue("IOWaitTime", (double) onLineStat2.activityRuntimeInfo.pidIoWaitSumAvg);
                    create2.setValue("IOWaitCount", (double) onLineStat2.activityRuntimeInfo.pidIoWaitCount);
                    measureValuesAdapter.setValue("BlockingGCCount", (double) onLineStat2.activityRuntimeInfo.blockGc);
                    measureValuesAdapter.setValue("IOWaitTime", (double) onLineStat2.activityRuntimeInfo.pidIoWaitSumAvg);
                    measureValuesAdapter.setValue("IOWaitCount", (double) onLineStat2.activityRuntimeInfo.pidIoWaitCount);
                }
                create2.setValue("CpuMaxFreq", (double) onLineStat2.deviceInfo.cpuMaxFreq);
                create2.setValue("DeviceAvailMem", (double) onLineStat2.memroyStat.deviceAvailMemory);
                create2.setValue("TotalUsedMem", (double) onLineStat2.memroyStat.totalUsedMemory);
                create2.setValue("RemainMem", (double) onLineStat2.memroyStat.remainAvailMemory);
                create2.setValue("NativeHeapSize", (double) onLineStat2.memroyStat.nativePss);
                create2.setValue("JavaHeapSize", (double) onLineStat2.memroyStat.dalvikPss);
                create2.setValue("SysCpuPercent", (double) onLineStat2.cpuStat.sysAvgCPUPercent);
                create2.setValue("PidCpuPercent", (double) onLineStat2.cpuStat.myPidCPUPercent);
                create2.setValue("SysLoadAvg", (double) onLineStat2.cpuStat.systemLoadAvg);
                create2.setValue("RuntimeThread", (double) onLineStat2.performanceInfo.runTimeThreadCount);
                create2.setValue("RunningThread", (double) onLineStat2.performanceInfo.runningThreadCount);
                create2.setValue("DeviceScore", (double) onLineStat2.performanceInfo.deviceScore);
                create2.setValue("SysScore", (double) onLineStat2.performanceInfo.systemRunningScore);
                create2.setValue("PidScore", (double) onLineStat2.performanceInfo.myPidScore);
                create2.setValue("DeviceMem", (double) onLineStat2.deviceInfo.deviceTotalMemory);
                create2.setValue("PidPrepareTime", (double) onLineStat2.preparePidTime);
                create2.setValue("AdvTime", (double) OnLineMonitorApp.sAdvertisementTime);
                measureValuesAdapter.setValue("CpuMaxFreq", (double) onLineStat2.deviceInfo.cpuMaxFreq);
                measureValuesAdapter.setValue("DeviceAvailMem", (double) onLineStat2.memroyStat.deviceAvailMemory);
                measureValuesAdapter.setValue("TotalUsedMem", (double) onLineStat2.memroyStat.totalUsedMemory);
                measureValuesAdapter.setValue("RemainMem", (double) onLineStat2.memroyStat.remainAvailMemory);
                measureValuesAdapter.setValue("NativeHeapSize", (double) onLineStat2.memroyStat.nativePss);
                measureValuesAdapter.setValue("JavaHeapSize", (double) onLineStat2.memroyStat.dalvikPss);
                measureValuesAdapter.setValue("SysCpuPercent", (double) onLineStat2.cpuStat.sysAvgCPUPercent);
                measureValuesAdapter.setValue("PidCpuPercent", (double) onLineStat2.cpuStat.myPidCPUPercent);
                measureValuesAdapter.setValue("SysLoadAvg", (double) onLineStat2.cpuStat.systemLoadAvg);
                measureValuesAdapter.setValue("RuntimeThread", (double) onLineStat2.performanceInfo.runTimeThreadCount);
                measureValuesAdapter.setValue("RunningThread", (double) onLineStat2.performanceInfo.runningThreadCount);
                measureValuesAdapter.setValue("DeviceScore", (double) onLineStat2.performanceInfo.deviceScore);
                measureValuesAdapter.setValue("SysScore", (double) onLineStat2.performanceInfo.systemRunningScore);
                measureValuesAdapter.setValue("PidScore", (double) onLineStat2.performanceInfo.myPidScore);
                measureValuesAdapter.setValue("DeviceMem", (double) onLineStat2.deviceInfo.deviceTotalMemory);
                measureValuesAdapter.setValue("PidPrepareTime", (double) onLineStat2.preparePidTime);
                measureValuesAdapter.setValue("AdvTime", (double) OnLineMonitorApp.sAdvertisementTime);
                create.setValue("isFirstInstall", onLineStat2.isFirstInstall ? "true" : "false");
                create.setValue("CpuCore", String.valueOf(onLineStat2.deviceInfo.cpuProcessCount));
                create.setValue("APILevel", String.valueOf(onLineStat2.deviceInfo.apiLevel));
                create.setValue("IsLowMemory", onLineStat2.memroyStat.isLowMemroy ? "true" : "false");
                create.setValue("MemoryLevel", String.valueOf(onLineStat2.memroyStat.trimMemoryLevel));
                dimensionValuesAdapter.setValue("isFirstInstall", onLineStat2.isFirstInstall ? "true" : "false");
                dimensionValuesAdapter.setValue("CpuCore", String.valueOf(onLineStat2.deviceInfo.cpuProcessCount));
                dimensionValuesAdapter.setValue("APILevel", String.valueOf(onLineStat2.deviceInfo.apiLevel));
                dimensionValuesAdapter.setValue("IsLowMemory", onLineStat2.memroyStat.isLowMemroy ? "true" : "false");
                dimensionValuesAdapter.setValue("MemoryLevel", String.valueOf(onLineStat2.memroyStat.trimMemoryLevel));
                if (z2) {
                    create.setValue("BootType", str2);
                    dimensionValuesAdapter.setValue("BootType", str2);
                } else {
                    create.setValue("BootType", "HotBoot");
                    dimensionValuesAdapter.setValue("BootType", "HotBoot");
                }
                String appendDeviceInfo = appendDeviceInfo(onLineStat2, onLineStat2.activityRuntimeInfo);
                create.setValue("Info", appendDeviceInfo);
                dimensionValuesAdapter.setValue("Info", appendDeviceInfo);
                if (OnLineMonitor.sIsNormalDebug) {
                    String str3 = this.TAG;
                    Log.e(str3, "DeviceInfo=" + appendDeviceInfo);
                }
                this.mIsHotBootCommit = true;
                if (OnLineMonitor.sPerformanceLog) {
                    String str4 = this.TAG;
                    Log.e(str4, "BootFirstTime: " + j3 + ", BootTotalTime: " + j2 + ", FirstInstall : " + onLineStat2.isFirstInstall + ", BootType : " + str2 + ", CodeBoot : " + z2 + ", AdvTime : " + OnLineMonitorApp.sAdvertisementTime);
                }
                if ((!onLineStat2.deviceInfo.isEmulator && !OnLineMonitorApp.sIsDebug) || sTestAppMonitorLog) {
                    AppMonitor.Stat.commit("system", "LaunchAll", create, create2);
                    com.ali.telescope.internal.plugins.onlinemonitor.OnlineStatistics.getInstance().OnBootFinish(dimensionValuesAdapter.dimensionValues, measureValuesAdapter.measureValues);
                }
            }
        }
    }

    /* access modifiers changed from: package-private */
    public String appendDeviceInfo(OnLineMonitor.OnLineStat onLineStat, OnLineMonitor.ActivityRuntimeInfo activityRuntimeInfo) {
        if (onLineStat == null || activityRuntimeInfo == null) {
            return "";
        }
        StringBuilder sb = new StringBuilder(200);
        try {
            sb.append("model=");
            sb.append(onLineStat.deviceInfo.mobileModel);
            sb.append(",brand=");
            sb.append(onLineStat.deviceInfo.mobileBrand);
            sb.append(",CpuModel=");
            sb.append(onLineStat.deviceInfo.cpuModel);
            sb.append(",CpuBrand=");
            sb.append(onLineStat.deviceInfo.cpuBrand);
            sb.append(",GpuModel=");
            sb.append(onLineStat.deviceInfo.gpuModel);
            sb.append(",GpuBrand=");
            sb.append(onLineStat.deviceInfo.gpuBrand);
            sb.append(",GpuFreq=");
            sb.append(onLineStat.deviceInfo.gpuMaxFreq);
            sb.append(",CpuArch=");
            sb.append(onLineStat.deviceInfo.cpuArch);
            sb.append(",IsRoot=");
            sb.append(onLineStat.deviceInfo.isRooted);
            sb.append(",ScreenWidth=");
            sb.append(onLineStat.deviceInfo.screenWidth);
            sb.append(",ScreenHeght=");
            sb.append(onLineStat.deviceInfo.screenHeght);
            sb.append(",Density=");
            sb.append(onLineStat.deviceInfo.density);
            sb.append(",DeviceTotalAvailMem=");
            sb.append(onLineStat.deviceInfo.deviceTotalAvailMemory);
            sb.append(",DeviceAvailMemPercent=");
            sb.append((onLineStat.deviceInfo.deviceTotalAvailMemory * 100) / onLineStat.deviceInfo.deviceTotalMemory);
            sb.append(",DeviceRemainMem=");
            sb.append(onLineStat.memroyStat.remainAvailMemory);
            sb.append(",DeviceRemainMemPercent=");
            sb.append(((long) (onLineStat.memroyStat.remainAvailMemory * 100)) / onLineStat.deviceInfo.deviceTotalAvailMemory);
            sb.append(",MemoryThreshold=");
            sb.append(onLineStat.deviceInfo.memoryThreshold);
            sb.append(",MaxJavaAvailMem=");
            sb.append(onLineStat.memroyStat.maxCanUseJavaMemory);
            sb.append(",RemainAvailMemory=");
            sb.append(onLineStat.memroyStat.remainAvailMemory);
            sb.append(",JavaUsedMemPercent=");
            sb.append(onLineStat.memroyStat.totalJavaPercent);
            sb.append(",TotalMemoryPercent=");
            sb.append(onLineStat.memroyStat.totalMemoryPercent);
            sb.append(",summaryGraphics=");
            sb.append(Math.round((float) (activityRuntimeInfo.summaryGraphics / 1024)));
            sb.append(",summaryStack=");
            sb.append(Math.round((float) (activityRuntimeInfo.summaryStack / 1024)));
            sb.append(",summaryCode=");
            sb.append(Math.round((float) (activityRuntimeInfo.summaryCode / 1024)));
            sb.append(",summarySystem=");
            sb.append(Math.round((float) (activityRuntimeInfo.summarySystem / 1024)));
            sb.append(",summaryJavaHeap=");
            sb.append(Math.round((float) (activityRuntimeInfo.summaryJavaHeap / 1024)));
            sb.append(",summaryNativeHeap=");
            sb.append(Math.round((float) (activityRuntimeInfo.summaryNativeHeap / 1024)));
            sb.append(",summaryPrivateOther=");
            sb.append(Math.round((float) (activityRuntimeInfo.summaryPrivateOther / 1024)));
            sb.append(",summaryTotalpss=");
            sb.append(Math.round((float) (activityRuntimeInfo.summaryTotalpss / 1024)));
            sb.append(",summaryTotalswap=");
            sb.append(Math.round((float) (activityRuntimeInfo.summaryTotalswap / 1024)));
            sb.append(",databaseMemory=");
            sb.append(((float) Math.round(((((float) activityRuntimeInfo.databaseMemory) * 100.0f) / 1024.0f) / 1024.0f)) / 100.0f);
            sb.append(",totalUss=");
            sb.append(activityRuntimeInfo.totalUss);
            sb.append(",MemoryAlert=");
            sb.append(onLineStat.memroyStat.memoryAlert);
            sb.append(",OtherSo=");
            sb.append(onLineStat.activityRuntimeInfo.memOtherSo);
            sb.append(",OtherApk=");
            sb.append(onLineStat.activityRuntimeInfo.memOtherApk);
            sb.append(",OtherJar=");
            sb.append(onLineStat.activityRuntimeInfo.memOtherJar);
            sb.append(",OtherTtf=");
            sb.append(onLineStat.activityRuntimeInfo.memOtherTtf);
            sb.append(",OtherDex=");
            sb.append(onLineStat.activityRuntimeInfo.memOtherDex);
            sb.append(",OtherOat=");
            sb.append(onLineStat.activityRuntimeInfo.memOtherOat);
            sb.append(",OtherArt=");
            sb.append(onLineStat.activityRuntimeInfo.memOtherArt);
            sb.append(",OtherMap=");
            sb.append(onLineStat.activityRuntimeInfo.memOtherMap);
            sb.append(",OtherAshmem=");
            sb.append(onLineStat.activityRuntimeInfo.memOtherAshmem);
            sb.append(",finalizerSize=");
            sb.append(onLineStat.memroyStat.finalizerSize);
            sb.append(",majorFault=");
            sb.append(onLineStat.memroyStat.majorFault);
            sb.append(",blockingGCCount=");
            sb.append(onLineStat.memroyStat.blockingGCCount);
            sb.append(",blockingGCTime=");
            sb.append(onLineStat.memroyStat.totalBlockingGCTime);
            sb.append(",pidWaitSum=");
            sb.append(onLineStat.cpuStat.pidWaitSum);
            sb.append(",pidWaitMax=");
            sb.append(onLineStat.cpuStat.pidWaitMax);
            sb.append(",pidWaitCount=");
            sb.append(onLineStat.cpuStat.pidWaitCount);
            sb.append(",InnerStoreSize=");
            sb.append(onLineStat.deviceInfo.storeTotalSize);
            sb.append(",InnerStoreFreeSize=");
            sb.append(onLineStat.deviceInfo.storeTotalSize);
            if (onLineStat.deviceInfo.cpuFreqArray != null && onLineStat.deviceInfo.cpuProcessCount > 0) {
                sb.append(",CpuFreqList=");
                for (int i = 0; i < onLineStat.deviceInfo.cpuProcessCount; i++) {
                    sb.append(onLineStat.deviceInfo.cpuFreqArray[i]);
                    if (i < onLineStat.deviceInfo.cpuProcessCount - 1) {
                        sb.append(DXTemplateNamePathUtil.DIR);
                    }
                }
            }
        } catch (Exception e) {
            e.printStackTrace();
        }
        return sb.toString();
    }

    public void onMemoryLeak(String str, long j, String str2) {
        if (!this.mMemoryLeackRegisted) {
            DimensionSet addDimension = DimensionSet.create().addDimension("activityName").addDimension("chain");
            MeasureSet.create().addMeasure("leakSize");
            AppMonitor.register("system", "activityLeak", (MeasureSet) null, addDimension);
            this.mMemoryLeackRegisted = true;
        }
        DimensionValueSet create = DimensionValueSet.create();
        MeasureValueSet create2 = MeasureValueSet.create();
        double d = (double) j;
        create2.setValue("leakSize", d);
        create.setValue("activityName", str);
        create.setValue("chain", str2);
        AppMonitor.Stat.commit("system", "activityLeak", create, create2);
        DimensionValuesAdapter dimensionValuesAdapter = new DimensionValuesAdapter();
        MeasureValuesAdapter measureValuesAdapter = new MeasureValuesAdapter();
        measureValuesAdapter.setValue("leakSize", d);
        dimensionValuesAdapter.setValue("activityName", str);
        dimensionValuesAdapter.setValue("chain", str2);
        com.ali.telescope.internal.plugins.onlinemonitor.OnlineStatistics.getInstance().OnMemoryLeak(dimensionValuesAdapter.dimensionValues, measureValuesAdapter.measureValues);
    }

    public void onBlockOrCloseGuard(OnLineMonitor.OnLineStat onLineStat, int i, String str, String str2, String str3, String str4, int i2) {
        if (!this.mBlockOrCloseGuard) {
            AppMonitor.register("system", "BlockOrCloseGuard", MeasureSet.create().addMeasure("type").addMeasure("size"), DimensionSet.create().addDimension("activityName").addDimension("threadName").addDimension("typeString").addDimension("stacks"));
            this.mBlockOrCloseGuard = true;
        }
        DimensionValueSet create = DimensionValueSet.create();
        MeasureValueSet create2 = MeasureValueSet.create();
        create.setValue("typeString", str);
        create.setValue("activityName", str2);
        create.setValue("threadName", str3);
        create.setValue("stacks", str4);
        double d = (double) i;
        create2.setValue("type", d);
        double d2 = (double) i2;
        create2.setValue("size", d2);
        AppMonitor.Stat.commit("system", "BlockOrCloseGuard", create, create2);
        DimensionValuesAdapter dimensionValuesAdapter = new DimensionValuesAdapter();
        MeasureValuesAdapter measureValuesAdapter = new MeasureValuesAdapter();
        dimensionValuesAdapter.setValue("typeString", str);
        dimensionValuesAdapter.setValue("activityName", str2);
        dimensionValuesAdapter.setValue("threadName", str3);
        dimensionValuesAdapter.setValue("stacks", str4);
        measureValuesAdapter.setValue("type", d);
        measureValuesAdapter.setValue("size", d2);
        com.ali.telescope.internal.plugins.onlinemonitor.OnlineStatistics.getInstance().OnBlockOrCloseGuard(dimensionValuesAdapter.dimensionValues, measureValuesAdapter.measureValues);
    }

    public void onActivityPaused(Activity activity, OnLineMonitor.OnLineStat onLineStat, OnLineMonitor.ActivityRuntimeInfo activityRuntimeInfo) {
        float f;
        MeasureValueSet measureValueSet;
        OnLineMonitor.OnLineStat onLineStat2 = onLineStat;
        OnLineMonitor.ActivityRuntimeInfo activityRuntimeInfo2 = activityRuntimeInfo;
        if (activityRuntimeInfo2 == null) {
            return;
        }
        if (activityRuntimeInfo2.loadTime != 0 || activityRuntimeInfo2.activityTotalSmCount != 0) {
            if (!this.mSmoothRegisted) {
                DimensionSet addDimension = DimensionSet.create().addDimension("activityName").addDimension("CpuCore").addDimension("APILevel").addDimension("IsLowMemroy").addDimension("MemoryLevel").addDimension(UmbrellaConstants.LIFECYCLE_CREATE).addDimension("firstCreate").addDimension("isHotLauncher").addDimension("Info");
                MeasureSet addMeasure = MeasureSet.create().addMeasure("StayTime").addMeasure("JankTime").addMeasure("IdleTime").addMeasure("FrameTime").addMeasure("JankCount").addMeasure("FrameCount").addMeasure("DeviceMem").addMeasure("BadCountOne").addMeasure("BadCountTwo").addMeasure("BadCountThree").addMeasure("BadCountFour").addMeasure("BadCountFive").addMeasure("BadCountSix").addMeasure("BadCountSeven").addMeasure("BadCountEight").addMeasure("BadCountNine").addMeasure("BadCountTen").addMeasure("BadCountEleven").addMeasure("BadCountTwelve").addMeasure("loadTime").addMeasure("EnterIdleTime").addMeasure("CpuMaxFreq").addMeasure("DeviceAvailMem").addMeasure("TotalUsedMem").addMeasure("RemainMem").addMeasure("NativeHeapSize").addMeasure("JavaHeapSize").addMeasure("SysCpuPercent").addMeasure("PidCpuPercent").addMeasure("SysLoadAvg").addMeasure("RuntimeThread").addMeasure("RunningThread").addMeasure("ActivityScore").addMeasure("DeviceScore").addMeasure("SysScore").addMeasure("PidScore").addMeasure("StartActivityTime").addMeasure("LoadSmUsedTime").addMeasure("LoadSmCount").addMeasure("LoadBadSmCount").addMeasure("LoadBadSmUsedTime").addMeasure("OpenFileCount").addMeasure("TotalTx").addMeasure("TotalRx");
                this.mSmoothRegisted = true;
                AppMonitor.register("system", "activityload", addMeasure, addDimension, true);
            }
            onCommitResource(onLineStat2, activityRuntimeInfo2);
            DimensionValueSet create = DimensionValueSet.create();
            MeasureValueSet create2 = MeasureValueSet.create();
            DimensionValuesAdapter dimensionValuesAdapter = new DimensionValuesAdapter();
            MeasureValuesAdapter measureValuesAdapter = new MeasureValuesAdapter();
            if (activityRuntimeInfo2.activityTotalSmUsedTime > activityRuntimeInfo2.activityTotalBadSmUsedTime) {
                int i = activityRuntimeInfo2.activityBadSmoothStepCount[10] + activityRuntimeInfo2.activityBadSmoothStepCount[11] + activityRuntimeInfo2.activityBadSmoothStepCount[12] + activityRuntimeInfo2.activityBadSmoothStepCount[13] + activityRuntimeInfo2.activityBadSmoothStepCount[14];
                int i2 = 0;
                for (int i3 = 15; i3 < activityRuntimeInfo2.activityBadSmoothStepCount.length; i3++) {
                    i2 += activityRuntimeInfo2.activityBadSmoothStepCount[i3];
                }
                int i4 = (activityRuntimeInfo2.activityTotalSmCount * 1000) / activityRuntimeInfo2.activityTotalSmUsedTime;
                if (i4 > 60) {
                    i4 = 60;
                }
                float f2 = 100.0f - (((float) (activityRuntimeInfo2.activityTotalSmCount * 100)) / (((float) activityRuntimeInfo2.activityTotalSmUsedTime) / 16.6f));
                try {
                    float f3 = (float) ((activityRuntimeInfo2.activityBadSmoothStepCount[5] * 5) + (activityRuntimeInfo2.activityBadSmoothStepCount[6] * 10) + (activityRuntimeInfo2.activityBadSmoothStepCount[7] * 20) + (activityRuntimeInfo2.activityBadSmoothStepCount[8] * 40) + (activityRuntimeInfo2.activityBadSmoothStepCount[9] * 80) + (i * 150) + (i2 * 200));
                    float f4 = ((((((((float) activityRuntimeInfo2.activityBadSmoothStepCount[0]) * 0.25f) + (((float) activityRuntimeInfo2.activityBadSmoothStepCount[1]) * 0.3f)) + (((float) activityRuntimeInfo2.activityBadSmoothStepCount[2]) * 0.4f)) + (((float) activityRuntimeInfo2.activityBadSmoothStepCount[3]) * 0.5f)) + ((float) (activityRuntimeInfo2.activityBadSmoothStepCount[4] * 1))) + f3) / (((float) activityRuntimeInfo2.activityTotalSmCount) + f3);
                    try {
                        f = (float) (100 - Math.round(f4 * 100.0f));
                    } catch (Exception unused) {
                        f = f4;
                    }
                } catch (Exception unused2) {
                    f = 0.0f;
                }
                if (OnLineMonitor.sPerformanceLog) {
                    if (this.mStringBuilderForLog.length() > 0) {
                        this.mStringBuilderForLog.setLength(0);
                    }
                    StringBuilder sb = this.mStringBuilderForLog;
                    sb.append(activityRuntimeInfo2.activityName);
                    sb.append(", StayTime：");
                    measureValueSet = create2;
                    sb.append(activityRuntimeInfo2.stayTime);
                    sb.append(",  FrameTime：");
                    sb.append(activityRuntimeInfo2.activityTotalSmUsedTime);
                    sb.append(",  FrameCount：");
                    sb.append(activityRuntimeInfo2.activityTotalSmCount);
                    sb.append(", >16.6msTime：");
                    sb.append(activityRuntimeInfo2.activityTotalBadSmUsedTime);
                    sb.append(",  >16.6msCount：");
                    sb.append(activityRuntimeInfo2.activityTotalBadSmCount);
                    sb.append(",  FPS：");
                    sb.append(i4);
                    sb.append(",  LostFrames：");
                    sb.append(Math.round(f2 * 100.0f) / 100);
                    sb.append("%,  SM：");
                    sb.append(Math.round(f * 100.0f) / 100);
                    sb.append(WXUtils.PERCENT);
                    Log.e(this.TAG, this.mStringBuilderForLog.substring(0));
                } else {
                    measureValueSet = create2;
                }
                if (OnLineMonitor.sIsNormalDebug) {
                    StringBuilder sb2 = new StringBuilder(100);
                    int i5 = 0;
                    while (i5 < 10) {
                        sb2.append("badCount");
                        int i6 = i5 + 1;
                        sb2.append(i6);
                        sb2.append(Operators.CONDITION_IF_MIDDLE);
                        sb2.append(activityRuntimeInfo2.activityBadSmoothStepCount[i5]);
                        sb2.append(',');
                        i5 = i6;
                    }
                    sb2.append("badCount");
                    sb2.append(11);
                    sb2.append(Operators.CONDITION_IF_MIDDLE);
                    sb2.append(i);
                    sb2.append(',');
                    sb2.append("badCount");
                    sb2.append(12);
                    sb2.append(Operators.CONDITION_IF_MIDDLE);
                    sb2.append(i2);
                    Log.e(this.TAG, sb2.toString());
                }
                create2 = measureValueSet;
                create2.setValue("StayTime", (double) activityRuntimeInfo2.stayTime);
                create2.setValue("JankTime", (double) activityRuntimeInfo2.activityTotalBadSmUsedTime);
                create2.setValue("IdleTime", (double) (activityRuntimeInfo2.stayTime - ((long) activityRuntimeInfo2.activityTotalSmUsedTime)));
                create2.setValue("JankCount", (double) activityRuntimeInfo2.activityTotalBadSmCount);
                create2.setValue("FrameCount", (double) activityRuntimeInfo2.activityTotalSmCount);
                create2.setValue("FrameTime", (double) activityRuntimeInfo2.activityTotalSmUsedTime);
                create2.setValue("BadCountOne", (double) activityRuntimeInfo2.activityBadSmoothStepCount[0]);
                create2.setValue("BadCountTwo", (double) activityRuntimeInfo2.activityBadSmoothStepCount[1]);
                create2.setValue("BadCountThree", (double) activityRuntimeInfo2.activityBadSmoothStepCount[2]);
                create2.setValue("BadCountFour", (double) activityRuntimeInfo2.activityBadSmoothStepCount[3]);
                create2.setValue("BadCountFive", (double) activityRuntimeInfo2.activityBadSmoothStepCount[4]);
                create2.setValue("BadCountSix", (double) activityRuntimeInfo2.activityBadSmoothStepCount[5]);
                create2.setValue("BadCountSeven", (double) activityRuntimeInfo2.activityBadSmoothStepCount[6]);
                create2.setValue("BadCountEight", (double) activityRuntimeInfo2.activityBadSmoothStepCount[7]);
                create2.setValue("BadCountNine", (double) activityRuntimeInfo2.activityBadSmoothStepCount[8]);
                create2.setValue("BadCountTen", (double) activityRuntimeInfo2.activityBadSmoothStepCount[9]);
                double d = (double) i;
                create2.setValue("BadCountEleven", d);
                double d2 = (double) i2;
                create2.setValue("BadCountTwelve", d2);
                create2.setValue("LoadSmUsedTime", (double) activityRuntimeInfo2.activityLoadSmUsedTime);
                create2.setValue("LoadSmCount", (double) activityRuntimeInfo2.activityLoadSmCount);
                create2.setValue("LoadBadSmCount", (double) activityRuntimeInfo2.activityLoadBadSmCount);
                create2.setValue("LoadBadSmUsedTime", (double) activityRuntimeInfo2.activityLoadBadSmUsedTime);
                create2.setValue("TotalTx", (double) activityRuntimeInfo2.totalTx);
                create2.setValue("TotalRx", (double) activityRuntimeInfo2.totalRx);
                measureValuesAdapter.setValue("StayTime", (double) activityRuntimeInfo2.stayTime);
                measureValuesAdapter.setValue("JankTime", (double) activityRuntimeInfo2.activityTotalBadSmUsedTime);
                measureValuesAdapter.setValue("IdleTime", (double) (activityRuntimeInfo2.stayTime - ((long) activityRuntimeInfo2.activityTotalSmUsedTime)));
                measureValuesAdapter.setValue("JankCount", (double) activityRuntimeInfo2.activityTotalBadSmCount);
                measureValuesAdapter.setValue("FrameCount", (double) activityRuntimeInfo2.activityTotalSmCount);
                measureValuesAdapter.setValue("FrameTime", (double) activityRuntimeInfo2.activityTotalSmUsedTime);
                measureValuesAdapter.setValue("BadCountOne", (double) activityRuntimeInfo2.activityBadSmoothStepCount[0]);
                measureValuesAdapter.setValue("BadCountTwo", (double) activityRuntimeInfo2.activityBadSmoothStepCount[1]);
                measureValuesAdapter.setValue("BadCountThree", (double) activityRuntimeInfo2.activityBadSmoothStepCount[2]);
                measureValuesAdapter.setValue("BadCountFour", (double) activityRuntimeInfo2.activityBadSmoothStepCount[3]);
                measureValuesAdapter.setValue("BadCountFive", (double) activityRuntimeInfo2.activityBadSmoothStepCount[4]);
                measureValuesAdapter.setValue("BadCountSix", (double) activityRuntimeInfo2.activityBadSmoothStepCount[5]);
                measureValuesAdapter.setValue("BadCountSeven", (double) activityRuntimeInfo2.activityBadSmoothStepCount[6]);
                measureValuesAdapter.setValue("BadCountEight", (double) activityRuntimeInfo2.activityBadSmoothStepCount[7]);
                measureValuesAdapter.setValue("BadCountNine", (double) activityRuntimeInfo2.activityBadSmoothStepCount[8]);
                measureValuesAdapter.setValue("BadCountTen", (double) activityRuntimeInfo2.activityBadSmoothStepCount[9]);
                measureValuesAdapter.setValue("BadCountEleven", d);
                measureValuesAdapter.setValue("BadCountTwelve", d2);
                measureValuesAdapter.setValue("LoadSmUsedTime", (double) activityRuntimeInfo2.activityLoadSmUsedTime);
                measureValuesAdapter.setValue("LoadSmCount", (double) activityRuntimeInfo2.activityLoadSmCount);
                measureValuesAdapter.setValue("LoadBadSmCount", (double) activityRuntimeInfo2.activityLoadBadSmCount);
                measureValuesAdapter.setValue("LoadBadSmUsedTime", (double) activityRuntimeInfo2.activityLoadBadSmUsedTime);
                measureValuesAdapter.setValue("TotalTx", (double) activityRuntimeInfo2.totalTx);
                measureValuesAdapter.setValue("TotalRx", (double) activityRuntimeInfo2.totalRx);
            }
            int i7 = activityRuntimeInfo2.loadTime;
            int i8 = activityRuntimeInfo2.idleTime;
            if (activityRuntimeInfo2.loadTime < 0 || activityRuntimeInfo2.loadTime >= 30000) {
                i7 = 0;
            }
            if (activityRuntimeInfo2.idleTime < 0 || activityRuntimeInfo2.idleTime >= 30000) {
                i8 = 0;
            }
            if (OnLineMonitor.sPerformanceLog) {
                this.mStringBuilderForLog.setLength(0);
                StringBuilder sb3 = this.mStringBuilderForLog;
                sb3.append(OnLineMonitor.getSimpleName(activityRuntimeInfo2.activityName));
                sb3.append("  LoadingTime : ");
                sb3.append(activityRuntimeInfo2.loadTime);
                sb3.append(" ms , onCreate : ");
                sb3.append(activityRuntimeInfo2.isColdOpen);
                sb3.append(", FirstOpen : ");
                sb3.append(activityRuntimeInfo2.isFistTimeOpen);
                Log.e(this.TAG, this.mStringBuilderForLog.substring(0));
            }
            double d3 = (double) i7;
            create2.setValue("loadTime", d3);
            double d4 = (double) i8;
            create2.setValue("EnterIdleTime", d4);
            create2.setValue("DeviceMem", (double) onLineStat2.deviceInfo.deviceTotalMemory);
            create2.setValue("BlockingGCCount", (double) activityRuntimeInfo2.blockGc);
            create2.setValue("GcCount", (double) activityRuntimeInfo2.gcCount);
            create2.setValue("CpuMaxFreq", (double) onLineStat2.deviceInfo.cpuMaxFreq);
            create2.setValue("DeviceAvailMem", (double) onLineStat2.memroyStat.deviceAvailMemory);
            create2.setValue("RemainMem", (double) onLineStat2.memroyStat.remainAvailMemory);
            create2.setValue("TotalUsedMem", (double) activityRuntimeInfo2.memMax);
            create2.setValue("NativeHeapSize", (double) activityRuntimeInfo2.nativeMax);
            create2.setValue("JavaHeapSize", (double) activityRuntimeInfo2.javaMax);
            create2.setValue("SysCpuPercent", (double) onLineStat2.cpuStat.sysAvgCPUPercent);
            create2.setValue("PidCpuPercent", (double) onLineStat2.cpuStat.myPidCPUPercent);
            create2.setValue("SysLoadAvg", (double) onLineStat2.cpuStat.pidPerCpuLoadAvg);
            create2.setValue("RuntimeThread", (double) onLineStat2.performanceInfo.runTimeThreadCount);
            create2.setValue("RunningThread", (double) onLineStat2.performanceInfo.runningThreadCount);
            create2.setValue("DeviceScore", (double) onLineStat2.performanceInfo.deviceScore);
            create2.setValue("SysScore", (double) onLineStat2.performanceInfo.systemRunningScore);
            create2.setValue("PidScore", (double) onLineStat2.performanceInfo.myPidScore);
            create2.setValue("ActivityScore", (double) activityRuntimeInfo2.activityScore);
            create2.setValue("StartActivityTime", (double) activityRuntimeInfo2.startActivityTime);
            create2.setValue("OpenFileCount", (double) activityRuntimeInfo2.openFile);
            measureValuesAdapter.setValue("loadTime", d3);
            measureValuesAdapter.setValue("EnterIdleTime", d4);
            measureValuesAdapter.setValue("DeviceMem", (double) onLineStat2.deviceInfo.deviceTotalMemory);
            measureValuesAdapter.setValue("BlockingGCCount", (double) activityRuntimeInfo2.blockGc);
            measureValuesAdapter.setValue("GcCount", (double) activityRuntimeInfo2.gcCount);
            measureValuesAdapter.setValue("CpuMaxFreq", (double) onLineStat2.deviceInfo.cpuMaxFreq);
            measureValuesAdapter.setValue("DeviceAvailMem", (double) onLineStat2.memroyStat.deviceAvailMemory);
            measureValuesAdapter.setValue("RemainMem", (double) onLineStat2.memroyStat.remainAvailMemory);
            measureValuesAdapter.setValue("TotalUsedMem", (double) activityRuntimeInfo2.memMax);
            measureValuesAdapter.setValue("NativeHeapSize", (double) activityRuntimeInfo2.nativeMax);
            measureValuesAdapter.setValue("JavaHeapSize", (double) activityRuntimeInfo2.javaMax);
            measureValuesAdapter.setValue("SysCpuPercent", (double) onLineStat2.cpuStat.sysAvgCPUPercent);
            measureValuesAdapter.setValue("PidCpuPercent", (double) onLineStat2.cpuStat.myPidCPUPercent);
            measureValuesAdapter.setValue("SysLoadAvg", (double) onLineStat2.cpuStat.pidPerCpuLoadAvg);
            measureValuesAdapter.setValue("RuntimeThread", (double) onLineStat2.performanceInfo.runTimeThreadCount);
            measureValuesAdapter.setValue("RunningThread", (double) onLineStat2.performanceInfo.runningThreadCount);
            measureValuesAdapter.setValue("DeviceScore", (double) onLineStat2.performanceInfo.deviceScore);
            measureValuesAdapter.setValue("SysScore", (double) onLineStat2.performanceInfo.systemRunningScore);
            measureValuesAdapter.setValue("PidScore", (double) onLineStat2.performanceInfo.myPidScore);
            measureValuesAdapter.setValue("ActivityScore", (double) activityRuntimeInfo2.activityScore);
            measureValuesAdapter.setValue("StartActivityTime", (double) activityRuntimeInfo2.startActivityTime);
            measureValuesAdapter.setValue("OpenFileCount", (double) activityRuntimeInfo2.openFile);
            create.setValue("activityName", activityRuntimeInfo2.activityName);
            create.setValue("CpuCore", String.valueOf(onLineStat2.deviceInfo.cpuProcessCount));
            create.setValue("APILevel", String.valueOf(onLineStat2.deviceInfo.apiLevel));
            create.setValue("IsLowMemroy", onLineStat2.memroyStat.isLowMemroy ? "true" : "false");
            create.setValue("MemoryLevel", String.valueOf(onLineStat2.memroyStat.trimMemoryLevel));
            create.setValue(UmbrellaConstants.LIFECYCLE_CREATE, activityRuntimeInfo2.isColdOpen ? "true" : "false");
            create.setValue("firstCreate", activityRuntimeInfo2.isFistTimeOpen ? "true" : "false");
            create.setValue("isHotLauncher", !OnLineMonitorApp.isCodeBoot() ? "true" : "false");
            dimensionValuesAdapter.setValue("activityName", activityRuntimeInfo2.activityName);
            dimensionValuesAdapter.setValue("CpuCore", String.valueOf(onLineStat2.deviceInfo.cpuProcessCount));
            dimensionValuesAdapter.setValue("APILevel", String.valueOf(onLineStat2.deviceInfo.apiLevel));
            dimensionValuesAdapter.setValue("IsLowMemroy", onLineStat2.memroyStat.isLowMemroy ? "true" : "false");
            dimensionValuesAdapter.setValue("MemoryLevel", String.valueOf(onLineStat2.memroyStat.trimMemoryLevel));
            dimensionValuesAdapter.setValue(UmbrellaConstants.LIFECYCLE_CREATE, activityRuntimeInfo2.isColdOpen ? "true" : "false");
            dimensionValuesAdapter.setValue("firstCreate", activityRuntimeInfo2.isFistTimeOpen ? "true" : "false");
            dimensionValuesAdapter.setValue("isHotLauncher", !OnLineMonitorApp.isCodeBoot() ? "true" : "false");
            try {
                String appendDeviceInfo = appendDeviceInfo(onLineStat2, activityRuntimeInfo2);
                create.setValue("Info", appendDeviceInfo);
                dimensionValuesAdapter.setValue("Info", appendDeviceInfo);
                if (OnLineMonitor.sIsNormalDebug) {
                    String str = this.TAG;
                    Log.e(str, "onActivityPaused Info =" + appendDeviceInfo);
                }
                if (activityRuntimeInfo2.statisticsDiscard) {
                    return;
                }
            } catch (Exception e) {
                e.printStackTrace();
            }
            if ((!onLineStat2.deviceInfo.isEmulator && !OnLineMonitorApp.sIsDebug) || sTestAppMonitorLog) {
                AppMonitor.Stat.commit("system", "activityload", create, create2);
                com.ali.telescope.internal.plugins.onlinemonitor.OnlineStatistics.getInstance().OnActivityLoad(dimensionValuesAdapter.dimensionValues, measureValuesAdapter.measureValues);
            }
        }
    }

    public void onCommitResource(OnLineMonitor.OnLineStat onLineStat, OnLineMonitor.ActivityRuntimeInfo activityRuntimeInfo) {
        if (onLineStat != null && activityRuntimeInfo != null && !activityRuntimeInfo.statisticsDiscard && onLineStat.deviceInfo.apiLevel >= 24) {
            if (!this.mCommitResourceReg) {
                AppMonitor.register("system", "BitmapStatic", MeasureSet.create().addMeasure("DeviceMem").addMeasure("DeviceTotalAvailMem").addMeasure("DeviceAvailMem").addMeasure("TotalUsedMem").addMeasure("RemainMem").addMeasure("NativeHeapSize").addMeasure("JavaHeapSize").addMeasure("DeviceScore").addMeasure("SysScore").addMeasure("PidScore").addMeasure("BitmapCount").addMeasure("Bitmap565Count").addMeasure("Bitmap8888Count").addMeasure("BitmapByte").addMeasure("Bitmap1M").addMeasure("Bitmap2M").addMeasure("Bitmap4M").addMeasure("Bitmap6M").addMeasure("Bitmap8M").addMeasure("Bitmap10M").addMeasure("Bitmap12M").addMeasure("Bitmap15M").addMeasure("Bitmap20M").addMeasure("SizeScreen").addMeasure("Size2Screen").addMeasure("SizeHashScreen").addMeasure("Size14Screen"), DimensionSet.create().addDimension("APILevel").addDimension("activityName").addDimension("Info"));
                AppMonitor.register("system", "CleanerStatic", MeasureSet.create().addMeasure("DeviceMem").addMeasure("DeviceTotalAvailMem").addMeasure("DeviceAvailMem").addMeasure("TotalUsedMem").addMeasure("RemainMem").addMeasure("NativeHeapSize").addMeasure("JavaHeapSize").addMeasure("DeviceScore").addMeasure("SysScore").addMeasure("PidScore").addMeasure("ClassCount"), DimensionSet.create().addDimension("APILevel").addDimension("activityName").addDimension("Info").addDimension("ClassName"));
                this.mCommitResourceReg = true;
            }
            try {
                if (activityRuntimeInfo.bitmapCount > 0) {
                    DimensionValueSet create = DimensionValueSet.create();
                    MeasureValueSet create2 = MeasureValueSet.create();
                    DimensionValuesAdapter dimensionValuesAdapter = new DimensionValuesAdapter();
                    MeasureValuesAdapter measureValuesAdapter = new MeasureValuesAdapter();
                    create2.setValue("DeviceMem", (double) onLineStat.deviceInfo.deviceTotalMemory);
                    create2.setValue("DeviceTotalAvailMem", (double) onLineStat.deviceInfo.deviceTotalAvailMemory);
                    create2.setValue("DeviceAvailMem", (double) onLineStat.memroyStat.deviceAvailMemory);
                    create2.setValue("TotalUsedMem", (double) onLineStat.memroyStat.totalUsedMemory);
                    create2.setValue("RemainMem", (double) onLineStat.memroyStat.remainAvailMemory);
                    create2.setValue("NativeHeapSize", (double) onLineStat.memroyStat.nativePss);
                    create2.setValue("JavaHeapSize", (double) onLineStat.memroyStat.dalvikPss);
                    create2.setValue("DeviceScore", (double) onLineStat.performanceInfo.deviceScore);
                    create2.setValue("SysScore", (double) onLineStat.performanceInfo.systemRunningScore);
                    create2.setValue("PidScore", (double) onLineStat.performanceInfo.myPidScore);
                    create2.setValue("BitmapCount", (double) activityRuntimeInfo.bitmapCount);
                    create2.setValue("Bitmap565Count", (double) activityRuntimeInfo.bitmap565Count);
                    create2.setValue("Bitmap8888Count", (double) activityRuntimeInfo.bitmap8888Count);
                    create2.setValue("BitmapByte", (double) activityRuntimeInfo.bitmapByteCount);
                    create2.setValue("Bitmap1M", (double) activityRuntimeInfo.bitmap1M);
                    create2.setValue("Bitmap2M", (double) activityRuntimeInfo.bitmap2M);
                    create2.setValue("Bitmap4M", (double) activityRuntimeInfo.bitmap4M);
                    create2.setValue("Bitmap6M", (double) activityRuntimeInfo.bitmap6M);
                    create2.setValue("Bitmap8M", (double) activityRuntimeInfo.bitmap8M);
                    create2.setValue("Bitmap10M", (double) activityRuntimeInfo.bitmap10M);
                    create2.setValue("Bitmap12M", (double) activityRuntimeInfo.bitmap12M);
                    create2.setValue("Bitmap15M", (double) activityRuntimeInfo.bitmap15M);
                    create2.setValue("Bitmap20M", (double) activityRuntimeInfo.bitmap20M);
                    create2.setValue("SizeScreen", (double) activityRuntimeInfo.bitmapSizeScreen);
                    create2.setValue("Size2Screen", (double) activityRuntimeInfo.bitmapSize2Screen);
                    create2.setValue("SizeHashScreen", (double) activityRuntimeInfo.bitmapSizeHashScreen);
                    create2.setValue("Size14Screen", (double) activityRuntimeInfo.bitmapSize14Screen);
                    measureValuesAdapter.setValue("DeviceMem", (double) onLineStat.deviceInfo.deviceTotalMemory);
                    measureValuesAdapter.setValue("DeviceTotalAvailMem", (double) onLineStat.deviceInfo.deviceTotalAvailMemory);
                    measureValuesAdapter.setValue("DeviceAvailMem", (double) onLineStat.memroyStat.deviceAvailMemory);
                    measureValuesAdapter.setValue("TotalUsedMem", (double) onLineStat.memroyStat.totalUsedMemory);
                    measureValuesAdapter.setValue("RemainMem", (double) onLineStat.memroyStat.remainAvailMemory);
                    measureValuesAdapter.setValue("NativeHeapSize", (double) onLineStat.memroyStat.nativePss);
                    measureValuesAdapter.setValue("JavaHeapSize", (double) onLineStat.memroyStat.dalvikPss);
                    measureValuesAdapter.setValue("DeviceScore", (double) onLineStat.performanceInfo.deviceScore);
                    measureValuesAdapter.setValue("SysScore", (double) onLineStat.performanceInfo.systemRunningScore);
                    measureValuesAdapter.setValue("PidScore", (double) onLineStat.performanceInfo.myPidScore);
                    measureValuesAdapter.setValue("BitmapCount", (double) activityRuntimeInfo.bitmapCount);
                    measureValuesAdapter.setValue("Bitmap565Count", (double) activityRuntimeInfo.bitmap565Count);
                    measureValuesAdapter.setValue("Bitmap8888Count", (double) activityRuntimeInfo.bitmap8888Count);
                    measureValuesAdapter.setValue("BitmapByte", (double) activityRuntimeInfo.bitmapByteCount);
                    measureValuesAdapter.setValue("Bitmap1M", (double) activityRuntimeInfo.bitmap1M);
                    measureValuesAdapter.setValue("Bitmap2M", (double) activityRuntimeInfo.bitmap2M);
                    measureValuesAdapter.setValue("Bitmap4M", (double) activityRuntimeInfo.bitmap4M);
                    measureValuesAdapter.setValue("Bitmap6M", (double) activityRuntimeInfo.bitmap6M);
                    measureValuesAdapter.setValue("Bitmap8M", (double) activityRuntimeInfo.bitmap8M);
                    measureValuesAdapter.setValue("Bitmap10M", (double) activityRuntimeInfo.bitmap10M);
                    measureValuesAdapter.setValue("Bitmap12M", (double) activityRuntimeInfo.bitmap12M);
                    measureValuesAdapter.setValue("Bitmap15M", (double) activityRuntimeInfo.bitmap15M);
                    measureValuesAdapter.setValue("Bitmap20M", (double) activityRuntimeInfo.bitmap20M);
                    measureValuesAdapter.setValue("SizeScreen", (double) activityRuntimeInfo.bitmapSizeScreen);
                    measureValuesAdapter.setValue("Size2Screen", (double) activityRuntimeInfo.bitmapSize2Screen);
                    measureValuesAdapter.setValue("SizeHashScreen", (double) activityRuntimeInfo.bitmapSizeHashScreen);
                    measureValuesAdapter.setValue("Size14Screen", (double) activityRuntimeInfo.bitmapSize14Screen);
                    create.setValue("activityName", activityRuntimeInfo.activityName);
                    create.setValue("CpuCore", String.valueOf(onLineStat.deviceInfo.cpuProcessCount));
                    create.setValue("APILevel", String.valueOf(onLineStat.deviceInfo.apiLevel));
                    dimensionValuesAdapter.setValue("activityName", activityRuntimeInfo.activityName);
                    dimensionValuesAdapter.setValue("CpuCore", String.valueOf(onLineStat.deviceInfo.cpuProcessCount));
                    dimensionValuesAdapter.setValue("APILevel", String.valueOf(onLineStat.deviceInfo.apiLevel));
                    int i = 0;
                    if (onLineStat.memroyStat.maxCanUseJavaMemory > 0) {
                        i = ((activityRuntimeInfo.bitmapByteCount * 100) / onLineStat.memroyStat.maxCanUseJavaMemory) / 1024;
                    }
                    if (OnLineMonitorApp.sIsDebug && (activityRuntimeInfo.bitmap6M > 0 || activityRuntimeInfo.bitmapCount >= 200 || activityRuntimeInfo.bitmapSizeHashScreen > 0 || i >= 25)) {
                        String str = this.TAG;
                        Log.d(str, "Bitmap Check ，activityName=" + activityRuntimeInfo.activityName + ",bitmapCount=" + activityRuntimeInfo.bitmapCount + ",bitmapJavaPercent=" + i + ",Bitmap6M=" + activityRuntimeInfo.bitmap6M + ", Bitmap8M=" + activityRuntimeInfo.bitmap8M + ", Bitmap10M=" + activityRuntimeInfo.bitmap10M + ", Bitmap12M=" + activityRuntimeInfo.bitmap12M + ", Bitmap15M=" + activityRuntimeInfo.bitmap15M + ", Bitmap20M=" + activityRuntimeInfo.bitmap20M + ", bitmapSizeHashScreen=" + activityRuntimeInfo.bitmapSizeHashScreen + ", bitmapSizeScreen=" + activityRuntimeInfo.bitmapSizeScreen);
                    }
                    String appendDeviceInfo = appendDeviceInfo(onLineStat, onLineStat.activityRuntimeInfo);
                    create.setValue("Info", appendDeviceInfo);
                    dimensionValuesAdapter.setValue("Info", appendDeviceInfo);
                    AppMonitor.Stat.commit("system", "BitmapStatic", create, create2);
                    com.ali.telescope.internal.plugins.onlinemonitor.OnlineStatistics.getInstance().OnBitmapStatic(dimensionValuesAdapter.dimensionValues, measureValuesAdapter.measureValues);
                }
            } catch (Exception e) {
                e.printStackTrace();
            } catch (Throwable unused) {
                return;
            }
            if (activityRuntimeInfo.cleanerObjectMap != null && activityRuntimeInfo.cleanerObjectMap.size() > 0) {
                for (Map.Entry next : activityRuntimeInfo.cleanerObjectMap.entrySet()) {
                    Integer num = (Integer) next.getValue();
                    if (num != null) {
                        String str2 = (String) next.getKey();
                        int intValue = num.intValue();
                        DimensionValueSet create3 = DimensionValueSet.create();
                        MeasureValueSet create4 = MeasureValueSet.create();
                        DimensionValuesAdapter dimensionValuesAdapter2 = new DimensionValuesAdapter();
                        MeasureValuesAdapter measureValuesAdapter2 = new MeasureValuesAdapter();
                        create4.setValue("DeviceMem", (double) onLineStat.deviceInfo.deviceTotalMemory);
                        create4.setValue("DeviceTotalAvailMem", (double) onLineStat.deviceInfo.deviceTotalAvailMemory);
                        create4.setValue("DeviceAvailMem", (double) onLineStat.memroyStat.deviceAvailMemory);
                        create4.setValue("TotalUsedMem", (double) onLineStat.memroyStat.totalUsedMemory);
                        create4.setValue("RemainMem", (double) onLineStat.memroyStat.remainAvailMemory);
                        create4.setValue("NativeHeapSize", (double) onLineStat.memroyStat.nativePss);
                        create4.setValue("JavaHeapSize", (double) onLineStat.memroyStat.dalvikPss);
                        create4.setValue("DeviceScore", (double) onLineStat.performanceInfo.deviceScore);
                        create4.setValue("SysScore", (double) onLineStat.performanceInfo.systemRunningScore);
                        create4.setValue("PidScore", (double) onLineStat.performanceInfo.myPidScore);
                        double d = (double) intValue;
                        create4.setValue("ClassCount", d);
                        measureValuesAdapter2.setValue("DeviceMem", (double) onLineStat.deviceInfo.deviceTotalMemory);
                        measureValuesAdapter2.setValue("DeviceTotalAvailMem", (double) onLineStat.deviceInfo.deviceTotalAvailMemory);
                        measureValuesAdapter2.setValue("DeviceAvailMem", (double) onLineStat.memroyStat.deviceAvailMemory);
                        measureValuesAdapter2.setValue("TotalUsedMem", (double) onLineStat.memroyStat.totalUsedMemory);
                        measureValuesAdapter2.setValue("RemainMem", (double) onLineStat.memroyStat.remainAvailMemory);
                        measureValuesAdapter2.setValue("NativeHeapSize", (double) onLineStat.memroyStat.nativePss);
                        measureValuesAdapter2.setValue("JavaHeapSize", (double) onLineStat.memroyStat.dalvikPss);
                        measureValuesAdapter2.setValue("DeviceScore", (double) onLineStat.performanceInfo.deviceScore);
                        measureValuesAdapter2.setValue("SysScore", (double) onLineStat.performanceInfo.systemRunningScore);
                        measureValuesAdapter2.setValue("PidScore", (double) onLineStat.performanceInfo.myPidScore);
                        measureValuesAdapter2.setValue("ClassCount", d);
                        create3.setValue("ClassName", str2);
                        create3.setValue("activityName", activityRuntimeInfo.activityName);
                        create3.setValue("CpuCore", String.valueOf(onLineStat.deviceInfo.cpuProcessCount));
                        create3.setValue("APILevel", String.valueOf(onLineStat.deviceInfo.apiLevel));
                        dimensionValuesAdapter2.setValue("ClassName", str2);
                        dimensionValuesAdapter2.setValue("activityName", activityRuntimeInfo.activityName);
                        dimensionValuesAdapter2.setValue("CpuCore", String.valueOf(onLineStat.deviceInfo.cpuProcessCount));
                        dimensionValuesAdapter2.setValue("APILevel", String.valueOf(onLineStat.deviceInfo.apiLevel));
                        AppMonitor.Stat.commit("system", "CleanerStatic", create3, create4);
                        if (OnLineMonitor.sIsNormalDebug) {
                            String str3 = this.TAG;
                            Log.e(str3, "Clearner activityName=" + activityRuntimeInfo.activityName + ", ClassName=" + str2 + ",ClassCount=" + intValue);
                        }
                        com.ali.telescope.internal.plugins.onlinemonitor.OnlineStatistics.getInstance().OnCleanerStatic(dimensionValuesAdapter2.dimensionValues, measureValuesAdapter2.measureValues);
                    }
                }
            }
        }
    }

    public void onGotoSleep(OnLineMonitor.OnLineStat onLineStat, Map<String, ThreadInfo> map, Map<String, Integer> map2, Map<String, OnLineMonitor.ThreadIoInfo> map3) {
        Iterator<Map.Entry<String, ThreadInfo>> it;
        int i;
        int i2;
        int i3;
        int i4;
        Integer num;
        Integer num2;
        Integer num3;
        Integer num4;
        OnLineMonitor.OnLineStat onLineStat2 = onLineStat;
        Map<String, Integer> map4 = map2;
        if (!this.mGotoSleepReg) {
            AppMonitor.register("system", "OnGotoSleep", MeasureSet.create().addMeasure("DeviceTotalAvailMem").addMeasure("DeviceMem").addMeasure("CpuMaxFreq").addMeasure("DeviceAvailMem").addMeasure("TotalUsedMem").addMeasure("RemainMem").addMeasure("NativeHeapSize").addMeasure("JavaHeapSize").addMeasure("DeviceScore").addMeasure("SysScore").addMeasure("PidScore").addMeasure("MaxCpuSys").addMeasure("MaxCpuDev").addMeasure("CpuSys").addMeasure("CpuDev").addMeasure("IsThread"), DimensionSet.create().addDimension("CpuCore").addDimension("APILevel").addDimension("CpuUser").addDimension("Info"));
            AppMonitor.register("system", "ThreadIoTimes", MeasureSet.create().addMeasure("DeviceScore").addMeasure("SysScore").addMeasure("PidScore").addMeasure("RWTimes").addMeasure("RTimes").addMeasure("WTimes").addMeasure("NetTimes").addMeasure("nice").addMeasure("ioWaitCount").addMeasure("ioWaitTime"), DimensionSet.create().addDimension("Thread").addDimension("Info"));
            this.mGotoSleepReg = true;
        }
        if (map3 != null) {
            try {
                for (Map.Entry<String, OnLineMonitor.ThreadIoInfo> value : map3.entrySet()) {
                    OnLineMonitor.ThreadIoInfo threadIoInfo = (OnLineMonitor.ThreadIoInfo) value.getValue();
                    if (threadIoInfo != null) {
                        DimensionValueSet create = DimensionValueSet.create();
                        MeasureValueSet create2 = MeasureValueSet.create();
                        DimensionValuesAdapter dimensionValuesAdapter = new DimensionValuesAdapter();
                        MeasureValuesAdapter measureValuesAdapter = new MeasureValuesAdapter();
                        create2.setValue("RWTimes", (double) threadIoInfo.readWriteTimes);
                        create2.setValue("RTimes", (double) threadIoInfo.readTimes);
                        create2.setValue("WTimes", (double) threadIoInfo.writeTimes);
                        create2.setValue("NetTimes", (double) threadIoInfo.netTimes);
                        create2.setValue("nice", (double) threadIoInfo.nice);
                        create2.setValue("ioWaitCount", (double) threadIoInfo.ioWaitCount);
                        create2.setValue("ioWaitTime", (double) threadIoInfo.ioWaitTime);
                        create2.setValue("DeviceScore", (double) onLineStat2.performanceInfo.deviceScore);
                        create2.setValue("SysScore", (double) onLineStat2.performanceInfo.systemRunningScore);
                        create2.setValue("PidScore", (double) onLineStat2.performanceInfo.myPidScore);
                        measureValuesAdapter.setValue("RWTimes", (double) threadIoInfo.readWriteTimes);
                        measureValuesAdapter.setValue("RTimes", (double) threadIoInfo.readTimes);
                        measureValuesAdapter.setValue("WTimes", (double) threadIoInfo.writeTimes);
                        measureValuesAdapter.setValue("NetTimes", (double) threadIoInfo.netTimes);
                        measureValuesAdapter.setValue("nice", (double) threadIoInfo.nice);
                        measureValuesAdapter.setValue("ioWaitCount", (double) threadIoInfo.ioWaitCount);
                        measureValuesAdapter.setValue("ioWaitTime", (double) threadIoInfo.ioWaitTime);
                        measureValuesAdapter.setValue("DeviceScore", (double) onLineStat2.performanceInfo.deviceScore);
                        measureValuesAdapter.setValue("SysScore", (double) onLineStat2.performanceInfo.systemRunningScore);
                        measureValuesAdapter.setValue("PidScore", (double) onLineStat2.performanceInfo.myPidScore);
                        create.setValue("Thread", threadIoInfo.name);
                        dimensionValuesAdapter.setValue("Thread", threadIoInfo.name);
                        AppMonitor.Stat.commit("system", "ThreadIoTimes", create, create2);
                        com.ali.telescope.internal.plugins.onlinemonitor.OnlineStatistics.getInstance().OnThreadIoTimes(dimensionValuesAdapter.dimensionValues, measureValuesAdapter.measureValues);
                    }
                }
            } catch (Throwable unused) {
            }
        }
        if (map4 != null) {
            try {
                if (map2.size() > 0) {
                    DimensionValueSet create3 = DimensionValueSet.create();
                    MeasureValueSet create4 = MeasureValueSet.create();
                    DimensionValuesAdapter dimensionValuesAdapter2 = new DimensionValuesAdapter();
                    MeasureValuesAdapter measureValuesAdapter2 = new MeasureValuesAdapter();
                    create4.setValue("CpuMaxFreq", (double) onLineStat2.deviceInfo.cpuMaxFreq);
                    create4.setValue("DeviceMem", (double) onLineStat2.deviceInfo.deviceTotalMemory);
                    create4.setValue("DeviceAvailMem", (double) onLineStat2.memroyStat.deviceAvailMemory);
                    create4.setValue("DeviceTotalAvailMem", (double) onLineStat2.deviceInfo.deviceTotalAvailMemory);
                    create4.setValue("TotalUsedMem", (double) onLineStat2.memroyStat.totalUsedMemory);
                    create4.setValue("RemainMem", (double) onLineStat2.memroyStat.remainAvailMemory);
                    create4.setValue("NativeHeapSize", (double) onLineStat2.memroyStat.nativePss);
                    create4.setValue("JavaHeapSize", (double) onLineStat2.memroyStat.dalvikPss);
                    create4.setValue("DeviceScore", (double) onLineStat2.performanceInfo.deviceScore);
                    create4.setValue("SysScore", (double) onLineStat2.performanceInfo.systemRunningScore);
                    create4.setValue("PidScore", (double) onLineStat2.performanceInfo.myPidScore);
                    create4.setValue("IsThread", 0.0d);
                    measureValuesAdapter2.setValue("CpuMaxFreq", (double) onLineStat2.deviceInfo.cpuMaxFreq);
                    measureValuesAdapter2.setValue("DeviceMem", (double) onLineStat2.deviceInfo.deviceTotalMemory);
                    measureValuesAdapter2.setValue("DeviceAvailMem", (double) onLineStat2.memroyStat.deviceAvailMemory);
                    measureValuesAdapter2.setValue("DeviceTotalAvailMem", (double) onLineStat2.deviceInfo.deviceTotalAvailMemory);
                    measureValuesAdapter2.setValue("TotalUsedMem", (double) onLineStat2.memroyStat.totalUsedMemory);
                    measureValuesAdapter2.setValue("RemainMem", (double) onLineStat2.memroyStat.remainAvailMemory);
                    measureValuesAdapter2.setValue("NativeHeapSize", (double) onLineStat2.memroyStat.nativePss);
                    measureValuesAdapter2.setValue("JavaHeapSize", (double) onLineStat2.memroyStat.dalvikPss);
                    measureValuesAdapter2.setValue("DeviceScore", (double) onLineStat2.performanceInfo.deviceScore);
                    measureValuesAdapter2.setValue("SysScore", (double) onLineStat2.performanceInfo.systemRunningScore);
                    measureValuesAdapter2.setValue("PidScore", (double) onLineStat2.performanceInfo.myPidScore);
                    measureValuesAdapter2.setValue("IsThread", 0.0d);
                    if (!map4.containsKey("MaxCpuSysRun") || (num4 = map4.get("MaxCpuSysRun")) == null) {
                        i = 0;
                    } else {
                        i = num4.intValue();
                        double d = (double) i;
                        create4.setValue("MaxCpuSys", d);
                        measureValuesAdapter2.setValue("MaxCpuSys", d);
                    }
                    if (!map4.containsKey("MaxCpuSysTotal") || (num3 = map4.get("MaxCpuSysTotal")) == null) {
                        i2 = 0;
                    } else {
                        i2 = num3.intValue();
                        double d2 = (double) i2;
                        create4.setValue("MaxCpuDev", d2);
                        measureValuesAdapter2.setValue("MaxCpuDev", d2);
                    }
                    if (!map4.containsKey("CpuSysRun") || (num2 = map4.get("CpuSysRun")) == null) {
                        i3 = 0;
                    } else {
                        i3 = num2.intValue();
                        double d3 = (double) i3;
                        create4.setValue("CpuSys", d3);
                        measureValuesAdapter2.setValue("CpuSys", d3);
                    }
                    if (!map4.containsKey("CpuSysTotal") || (num = map4.get("CpuSysTotal")) == null) {
                        i4 = 0;
                    } else {
                        i4 = num.intValue();
                        double d4 = (double) i4;
                        create4.setValue("CpuDev", d4);
                        measureValuesAdapter2.setValue("CpuDev", d4);
                    }
                    create3.setValue("CpuUser", "MyApp");
                    create3.setValue("CpuCore", String.valueOf(onLineStat2.deviceInfo.cpuProcessCount));
                    create3.setValue("APILevel", String.valueOf(onLineStat2.deviceInfo.apiLevel));
                    dimensionValuesAdapter2.setValue("CpuUser", "MyApp");
                    dimensionValuesAdapter2.setValue("CpuCore", String.valueOf(onLineStat2.deviceInfo.cpuProcessCount));
                    dimensionValuesAdapter2.setValue("APILevel", String.valueOf(onLineStat2.deviceInfo.apiLevel));
                    if (OnLineMonitor.sIsNormalDebug) {
                        String str = this.TAG;
                        Log.e(str, "Name=MyApp, MaxCpuSys=" + i + ",MaxCpuDev=" + i2 + ", CpuSys=" + i3 + ",CpuDev=" + i4);
                    }
                    AppMonitor.Stat.commit("system", "OnGotoSleep", create3, create4);
                    com.ali.telescope.internal.plugins.onlinemonitor.OnlineStatistics.getInstance().OnGotoSleep(dimensionValuesAdapter2.dimensionValues, measureValuesAdapter2.measureValues);
                }
            } catch (Throwable unused2) {
                return;
            }
        }
        if (map != null && map.size() > 0) {
            Iterator<Map.Entry<String, ThreadInfo>> it2 = map.entrySet().iterator();
            while (it2.hasNext()) {
                ThreadInfo threadInfo = (ThreadInfo) it2.next().getValue();
                if (threadInfo != null) {
                    long j = threadInfo.mUtime + threadInfo.mStime;
                    if (j > 0 && threadInfo.mPidLastTotalTime > 0 && threadInfo.mPidFirstTotalTime > 0) {
                        short s = threadInfo.mMaxPercentInPid;
                        short s2 = threadInfo.mMaxPercentInDevice;
                        int round = threadInfo.mPidLastTotalTime - threadInfo.mPidFirstTotalTime > 0 ? Math.round((float) ((j * 100) / (threadInfo.mPidLastTotalTime - threadInfo.mPidFirstTotalTime))) : 0;
                        int round2 = threadInfo.mDeviceLastTotalTime - threadInfo.mDeviceFirstTotalTime > 0 ? Math.round((float) ((j * 100) / (threadInfo.mDeviceLastTotalTime - threadInfo.mDeviceFirstTotalTime))) : 0;
                        if (s > 30 || s2 > 10 || round > 0 || round2 > 0) {
                            DimensionValueSet create5 = DimensionValueSet.create();
                            MeasureValueSet create6 = MeasureValueSet.create();
                            DimensionValuesAdapter dimensionValuesAdapter3 = new DimensionValuesAdapter();
                            MeasureValuesAdapter measureValuesAdapter3 = new MeasureValuesAdapter();
                            create6.setValue("DeviceMem", (double) onLineStat2.deviceInfo.deviceTotalMemory);
                            create6.setValue("DeviceTotalAvailMem", (double) onLineStat2.deviceInfo.deviceTotalAvailMemory);
                            create6.setValue("CpuMaxFreq", (double) onLineStat2.deviceInfo.cpuMaxFreq);
                            create6.setValue("DeviceAvailMem", (double) onLineStat2.memroyStat.deviceAvailMemory);
                            create6.setValue("TotalUsedMem", (double) onLineStat2.memroyStat.totalUsedMemory);
                            create6.setValue("RemainMem", (double) onLineStat2.memroyStat.remainAvailMemory);
                            create6.setValue("NativeHeapSize", (double) onLineStat2.memroyStat.nativePss);
                            create6.setValue("JavaHeapSize", (double) onLineStat2.memroyStat.dalvikPss);
                            create6.setValue("DeviceScore", (double) onLineStat2.performanceInfo.deviceScore);
                            create6.setValue("SysScore", (double) onLineStat2.performanceInfo.systemRunningScore);
                            create6.setValue("PidScore", (double) onLineStat2.performanceInfo.myPidScore);
                            create6.setValue("IsThread", 1.0d);
                            double d5 = (double) s;
                            create6.setValue("MaxCpuSys", d5);
                            double d6 = (double) s2;
                            create6.setValue("MaxCpuDev", d6);
                            short s3 = s;
                            short s4 = s2;
                            double d7 = (double) round;
                            create6.setValue("CpuSys", d7);
                            DimensionValueSet dimensionValueSet = create5;
                            int i5 = round;
                            double d8 = (double) round2;
                            create6.setValue("CpuDev", d8);
                            it = it2;
                            int i6 = round2;
                            measureValuesAdapter3.setValue("DeviceMem", (double) onLineStat2.deviceInfo.deviceTotalMemory);
                            measureValuesAdapter3.setValue("DeviceTotalAvailMem", (double) onLineStat2.deviceInfo.deviceTotalAvailMemory);
                            measureValuesAdapter3.setValue("CpuMaxFreq", (double) onLineStat2.deviceInfo.cpuMaxFreq);
                            measureValuesAdapter3.setValue("DeviceAvailMem", (double) onLineStat2.memroyStat.deviceAvailMemory);
                            measureValuesAdapter3.setValue("TotalUsedMem", (double) onLineStat2.memroyStat.totalUsedMemory);
                            measureValuesAdapter3.setValue("RemainMem", (double) onLineStat2.memroyStat.remainAvailMemory);
                            measureValuesAdapter3.setValue("NativeHeapSize", (double) onLineStat2.memroyStat.nativePss);
                            measureValuesAdapter3.setValue("JavaHeapSize", (double) onLineStat2.memroyStat.dalvikPss);
                            measureValuesAdapter3.setValue("DeviceScore", (double) onLineStat2.performanceInfo.deviceScore);
                            measureValuesAdapter3.setValue("SysScore", (double) onLineStat2.performanceInfo.systemRunningScore);
                            measureValuesAdapter3.setValue("PidScore", (double) onLineStat2.performanceInfo.myPidScore);
                            measureValuesAdapter3.setValue("IsThread", 1.0d);
                            measureValuesAdapter3.setValue("MaxCpuSys", d5);
                            measureValuesAdapter3.setValue("MaxCpuDev", d6);
                            measureValuesAdapter3.setValue("CpuSys", d7);
                            measureValuesAdapter3.setValue("CpuDev", d8);
                            ThreadInfo threadInfo2 = threadInfo;
                            DimensionValueSet dimensionValueSet2 = dimensionValueSet;
                            dimensionValueSet2.setValue("CpuUser", threadInfo2.mName);
                            dimensionValueSet2.setValue("CpuCore", String.valueOf(onLineStat2.deviceInfo.cpuProcessCount));
                            dimensionValueSet2.setValue("APILevel", String.valueOf(onLineStat2.deviceInfo.apiLevel));
                            dimensionValuesAdapter3.setValue("CpuUser", threadInfo2.mName);
                            dimensionValuesAdapter3.setValue("CpuCore", String.valueOf(onLineStat2.deviceInfo.cpuProcessCount));
                            dimensionValuesAdapter3.setValue("APILevel", String.valueOf(onLineStat2.deviceInfo.apiLevel));
                            AppMonitor.Stat.commit("system", "OnGotoSleep", dimensionValueSet2, create6);
                            if (OnLineMonitor.sIsNormalDebug) {
                                String str2 = this.TAG;
                                Log.e(str2, "Name=" + threadInfo2.mName + ", MaxCpuSys=" + s3 + ",MaxCpuDev=" + s4 + ", CpuSys=" + i5 + ",CpuDev=" + i6);
                            }
                            com.ali.telescope.internal.plugins.onlinemonitor.OnlineStatistics.getInstance().OnGotoSleep(dimensionValuesAdapter3.dimensionValues, measureValuesAdapter3.measureValues);
                            it2 = it;
                        }
                    }
                }
                it = it2;
                it2 = it;
            }
        }
    }

    public void onMemoryProblem(OnLineMonitor.OnLineStat onLineStat, String str, String str2, String str3, String str4) {
        if (!this.mOnMemoryProblemReg) {
            AppMonitor.register("system", "OnMemoryProblem", MeasureSet.create().addMeasure("DeviceMem").addMeasure("TotalUsedMem").addMeasure("DeviceScore").addMeasure("SysScore").addMeasure("PidScore").addMeasure("RuntimeThread").addMeasure("RunningThread").addMeasure("OtherSo").addMeasure("OtherJar").addMeasure("OtherApk").addMeasure("OtherTtf").addMeasure("OtherDex").addMeasure("OtherOat").addMeasure("OtherArt").addMeasure("OtherMap").addMeasure("OtherAshmem"), DimensionSet.create().addDimension("APILevel").addDimension("ActivityName").addDimension("Info").addDimension("MemoryLevel").addDimension("Activitys").addDimension("Threads").addDimension("MemoryType"));
            this.mOnMemoryProblemReg = true;
        }
        DimensionValueSet create = DimensionValueSet.create();
        MeasureValueSet create2 = MeasureValueSet.create();
        DimensionValuesAdapter dimensionValuesAdapter = new DimensionValuesAdapter();
        MeasureValuesAdapter measureValuesAdapter = new MeasureValuesAdapter();
        create2.setValue("DeviceMem", (double) onLineStat.deviceInfo.deviceTotalMemory);
        create2.setValue("DeviceTotalAvailMem", (double) onLineStat.deviceInfo.deviceTotalAvailMemory);
        create2.setValue("TotalUsedMem", (double) onLineStat.memroyStat.totalUsedMemory);
        create2.setValue("DeviceScore", (double) onLineStat.performanceInfo.deviceScore);
        create2.setValue("SysScore", (double) onLineStat.performanceInfo.systemRunningScore);
        create2.setValue("PidScore", (double) onLineStat.performanceInfo.myPidScore);
        create2.setValue("RuntimeThread", (double) onLineStat.performanceInfo.runTimeThreadCount);
        create2.setValue("RunningThread", (double) onLineStat.performanceInfo.runningThreadCount);
        measureValuesAdapter.setValue("DeviceMem", (double) onLineStat.deviceInfo.deviceTotalMemory);
        measureValuesAdapter.setValue("DeviceTotalAvailMem", (double) onLineStat.deviceInfo.deviceTotalAvailMemory);
        measureValuesAdapter.setValue("TotalUsedMem", (double) onLineStat.memroyStat.totalUsedMemory);
        measureValuesAdapter.setValue("DeviceScore", (double) onLineStat.performanceInfo.deviceScore);
        measureValuesAdapter.setValue("SysScore", (double) onLineStat.performanceInfo.systemRunningScore);
        measureValuesAdapter.setValue("PidScore", (double) onLineStat.performanceInfo.myPidScore);
        measureValuesAdapter.setValue("RuntimeThread", (double) onLineStat.performanceInfo.runTimeThreadCount);
        measureValuesAdapter.setValue("RunningThread", (double) onLineStat.performanceInfo.runningThreadCount);
        if (onLineStat.activityRuntimeInfo != null) {
            create2.setValue("OtherSo", (double) onLineStat.activityRuntimeInfo.memOtherSo);
            create2.setValue("OtherJar", (double) onLineStat.activityRuntimeInfo.memOtherJar);
            create2.setValue("OtherApk", (double) onLineStat.activityRuntimeInfo.memOtherApk);
            create2.setValue("OtherTtf", (double) onLineStat.activityRuntimeInfo.memOtherTtf);
            create2.setValue("OtherDex", (double) onLineStat.activityRuntimeInfo.memOtherDex);
            create2.setValue("OtherOat", (double) onLineStat.activityRuntimeInfo.memOtherOat);
            create2.setValue("OtherArt", (double) onLineStat.activityRuntimeInfo.memOtherArt);
            create2.setValue("OtherMap", (double) onLineStat.activityRuntimeInfo.memOtherMap);
            create2.setValue("OtherAshmem", (double) onLineStat.activityRuntimeInfo.memOtherAshmem);
            measureValuesAdapter.setValue("OtherSo", (double) onLineStat.activityRuntimeInfo.memOtherSo);
            measureValuesAdapter.setValue("OtherJar", (double) onLineStat.activityRuntimeInfo.memOtherJar);
            measureValuesAdapter.setValue("OtherApk", (double) onLineStat.activityRuntimeInfo.memOtherApk);
            measureValuesAdapter.setValue("OtherTtf", (double) onLineStat.activityRuntimeInfo.memOtherTtf);
            measureValuesAdapter.setValue("OtherDex", (double) onLineStat.activityRuntimeInfo.memOtherDex);
            measureValuesAdapter.setValue("OtherOat", (double) onLineStat.activityRuntimeInfo.memOtherOat);
            measureValuesAdapter.setValue("OtherArt", (double) onLineStat.activityRuntimeInfo.memOtherArt);
            measureValuesAdapter.setValue("OtherMap", (double) onLineStat.activityRuntimeInfo.memOtherMap);
            measureValuesAdapter.setValue("OtherAshmem", (double) onLineStat.activityRuntimeInfo.memOtherAshmem);
        }
        create.setValue("ActivityName", onLineStat.activityRuntimeInfo.activityName);
        create.setValue("MemoryType", str);
        create.setValue("APILevel", String.valueOf(onLineStat.deviceInfo.apiLevel));
        create.setValue("MemoryLevel", String.valueOf(onLineStat.memroyStat.trimMemoryLevel));
        create.setValue("Activitys", str3);
        dimensionValuesAdapter.setValue("ActivityName", onLineStat.activityRuntimeInfo.activityName);
        dimensionValuesAdapter.setValue("MemoryType", str);
        dimensionValuesAdapter.setValue("APILevel", String.valueOf(onLineStat.deviceInfo.apiLevel));
        dimensionValuesAdapter.setValue("MemoryLevel", String.valueOf(onLineStat.memroyStat.trimMemoryLevel));
        dimensionValuesAdapter.setValue("Activitys", str3);
        if (str4 != null) {
            create.setValue("Threads", str4);
            dimensionValuesAdapter.setValue("Threads", str4);
        }
        try {
            String appendDeviceInfo = appendDeviceInfo(onLineStat, onLineStat.activityRuntimeInfo);
            create.setValue("Info", appendDeviceInfo);
            dimensionValuesAdapter.setValue("Info", appendDeviceInfo);
        } catch (Exception e) {
            e.printStackTrace();
        }
        AppMonitor.Stat.commit("system", "OnMemoryProblem", create, create2);
        com.ali.telescope.internal.plugins.onlinemonitor.OnlineStatistics.getInstance().OnMemoryProblem(dimensionValuesAdapter.dimensionValues, measureValuesAdapter.measureValues);
    }

    public void onAnr(OnLineMonitor.OnLineStat onLineStat, String str, Map<Thread, StackTraceElement[]> map) {
        new MainLooperHandler().start();
        if (!this.mAnrReg) {
            AppMonitor.register("system", CrashReport.TYPE_ANR, MeasureSet.create().addMeasure("DeviceMem").addMeasure("CpuMaxFreq").addMeasure("TotalUsedMem").addMeasure("SysCpuPercent").addMeasure("PidCpuPercent").addMeasure("SysLoadAvg").addMeasure("RuntimeThread").addMeasure("RunningThread").addMeasure("DeviceScore").addMeasure("SysScore").addMeasure("PidScore"), DimensionSet.create().addDimension("fileName").addDimension("stack").addDimension("activityname").addDimension("CpuCore").addDimension("APILevel").addDimension("IsLowMemroy").addDimension("MemoryLevel").addDimension("Info"));
            this.mAnrReg = true;
        }
        try {
            StringBuilder sb = new StringBuilder(500);
            Iterator<Map.Entry<Thread, StackTraceElement[]>> it = map.entrySet().iterator();
            while (true) {
                if (!it.hasNext()) {
                    break;
                }
                Map.Entry next = it.next();
                if (next != null) {
                    Thread thread = (Thread) next.getKey();
                    StackTraceElement[] stackTraceElementArr = (StackTraceElement[]) next.getValue();
                    if (thread == null) {
                        continue;
                    } else if (thread.getId() == 1) {
                        if (stackTraceElementArr != null) {
                            int i = 0;
                            while (i < stackTraceElementArr.length && i < 10) {
                                sb.append(stackTraceElementArr[i].toString());
                                sb.append(";");
                                i++;
                            }
                        }
                    }
                }
            }
            if (sb.length() != 0) {
                DimensionValueSet create = DimensionValueSet.create();
                MeasureValueSet create2 = MeasureValueSet.create();
                DimensionValuesAdapter dimensionValuesAdapter = new DimensionValuesAdapter();
                MeasureValuesAdapter measureValuesAdapter = new MeasureValuesAdapter();
                create2.setValue("CpuMaxFreq", (double) onLineStat.deviceInfo.cpuMaxFreq);
                create2.setValue("TotalUsedMem", (double) onLineStat.memroyStat.totalUsedMemory);
                create2.setValue("SysCpuPercent", (double) onLineStat.cpuStat.sysAvgCPUPercent);
                create2.setValue("PidCpuPercent", (double) onLineStat.cpuStat.myPidCPUPercent);
                create2.setValue("SysLoadAvg", (double) onLineStat.cpuStat.pidPerCpuLoadAvg);
                create2.setValue("RuntimeThread", (double) onLineStat.performanceInfo.runTimeThreadCount);
                create2.setValue("RunningThread", (double) onLineStat.performanceInfo.runningThreadCount);
                create2.setValue("DeviceScore", (double) onLineStat.performanceInfo.deviceScore);
                create2.setValue("SysScore", (double) onLineStat.performanceInfo.systemRunningScore);
                create2.setValue("PidScore", (double) onLineStat.performanceInfo.myPidScore);
                measureValuesAdapter.setValue("CpuMaxFreq", (double) onLineStat.deviceInfo.cpuMaxFreq);
                measureValuesAdapter.setValue("TotalUsedMem", (double) onLineStat.memroyStat.totalUsedMemory);
                measureValuesAdapter.setValue("SysCpuPercent", (double) onLineStat.cpuStat.sysAvgCPUPercent);
                measureValuesAdapter.setValue("PidCpuPercent", (double) onLineStat.cpuStat.myPidCPUPercent);
                measureValuesAdapter.setValue("SysLoadAvg", (double) onLineStat.cpuStat.pidPerCpuLoadAvg);
                measureValuesAdapter.setValue("RuntimeThread", (double) onLineStat.performanceInfo.runTimeThreadCount);
                measureValuesAdapter.setValue("RunningThread", (double) onLineStat.performanceInfo.runningThreadCount);
                measureValuesAdapter.setValue("DeviceScore", (double) onLineStat.performanceInfo.deviceScore);
                measureValuesAdapter.setValue("SysScore", (double) onLineStat.performanceInfo.systemRunningScore);
                measureValuesAdapter.setValue("PidScore", (double) onLineStat.performanceInfo.myPidScore);
                create.setValue("CpuCore", String.valueOf(onLineStat.deviceInfo.cpuProcessCount));
                create.setValue("APILevel", String.valueOf(onLineStat.deviceInfo.apiLevel));
                create.setValue("IsLowMemroy", onLineStat.memroyStat.isLowMemroy ? "true" : "false");
                create.setValue("MemoryLevel", String.valueOf(onLineStat.memroyStat.trimMemoryLevel));
                create.setValue("stack", sb.toString());
                create.setValue("fileName", "");
                create.setValue("activityname", str);
                dimensionValuesAdapter.setValue("CpuCore", String.valueOf(onLineStat.deviceInfo.cpuProcessCount));
                dimensionValuesAdapter.setValue("APILevel", String.valueOf(onLineStat.deviceInfo.apiLevel));
                dimensionValuesAdapter.setValue("IsLowMemroy", onLineStat.memroyStat.isLowMemroy ? "true" : "false");
                dimensionValuesAdapter.setValue("MemoryLevel", String.valueOf(onLineStat.memroyStat.trimMemoryLevel));
                dimensionValuesAdapter.setValue("stack", sb.toString());
                dimensionValuesAdapter.setValue("fileName", "");
                dimensionValuesAdapter.setValue("activityname", str);
                try {
                    String appendDeviceInfo = appendDeviceInfo(onLineStat, onLineStat.activityRuntimeInfo);
                    create.setValue("Info", appendDeviceInfo);
                    dimensionValuesAdapter.setValue("Info", appendDeviceInfo);
                    if (OnLineMonitor.sIsNormalDebug) {
                        String str2 = this.TAG;
                        Log.e(str2, "onAnr Info =" + appendDeviceInfo);
                    }
                } catch (Exception e) {
                    e.printStackTrace();
                }
                if (!onLineStat.deviceInfo.isEmulator) {
                    AppMonitor.Stat.commit("system", CrashReport.TYPE_ANR, create, create2);
                    com.ali.telescope.internal.plugins.onlinemonitor.OnlineStatistics.getInstance().OnAnr(dimensionValuesAdapter.dimensionValues, measureValuesAdapter.measureValues);
                }
            }
        } catch (Exception unused) {
        }
    }

    public void onThreadPoolProblem(OnLineMonitor.OnLineStat onLineStat, String str, ThreadPoolExecutor threadPoolExecutor, String str2) {
        DimensionValueSet create;
        MeasureValueSet create2;
        DimensionValuesAdapter dimensionValuesAdapter;
        MeasureValuesAdapter measureValuesAdapter;
        HashSet hashSet;
        int indexOf;
        if (onLineStat != null && threadPoolExecutor != null) {
            try {
                if (!this.mThreadPoolRegisted) {
                    DimensionSet addDimension = DimensionSet.create().addDimension("activityName").addDimension("CpuCore").addDimension("APILevel").addDimension("IsLowMemroy").addDimension("MemoryLevel").addDimension("Info").addDimension("QueueThread").addDimension("PoolThread").addDimension("PoolThreadDetail");
                    MeasureSet addMeasure = MeasureSet.create().addMeasure("QueueSize").addMeasure("CoreSize").addMeasure("MaxSize").addMeasure("ActiveCount").addMeasure("CompletedCount").addMeasure("ThreadSize").addMeasure("DeviceMem").addMeasure("CpuMaxFreq").addMeasure("DeviceAvailMem").addMeasure("DeviceTotalAvailMem").addMeasure("TotalUsedMem").addMeasure("RemainMem").addMeasure("NativeHeapSize").addMeasure("JavaHeapSize").addMeasure("SysCpuPercent").addMeasure("PidCpuPercent").addMeasure("SysLoadAvg").addMeasure("RuntimeThread").addMeasure("RunningThread").addMeasure("DeviceScore").addMeasure("SysScore").addMeasure("PidScore");
                    this.mThreadPoolRegisted = true;
                    AppMonitor.register("system", "ThreadPoolProblem", addMeasure, addDimension, true);
                }
                create = DimensionValueSet.create();
                create2 = MeasureValueSet.create();
                dimensionValuesAdapter = new DimensionValuesAdapter();
                measureValuesAdapter = new MeasureValuesAdapter();
                if (threadPoolExecutor.getQueue() != null) {
                    int size = threadPoolExecutor.getQueue().size();
                    double d = (double) size;
                    create2.setValue("QueueSize", d);
                    measureValuesAdapter.setValue("QueueSize", d);
                    if (size > 0) {
                        String obj = threadPoolExecutor.getQueue().toString();
                        StringBuilder sb = new StringBuilder(300);
                        HashMap hashMap = new HashMap();
                        if (obj != null && obj.length() > 2) {
                            StringTokenizer stringTokenizer = new StringTokenizer(obj.replace(Operators.ARRAY_START_STR, "").replace(Operators.ARRAY_END_STR, "").replace(Operators.SPACE_STR, ""), ",");
                            while (stringTokenizer.hasMoreElements()) {
                                String nextToken = stringTokenizer.nextToken();
                                if (nextToken != null && (indexOf = nextToken.indexOf(64)) > 0) {
                                    String substring = nextToken.substring(0, indexOf);
                                    Integer num = (Integer) hashMap.get(substring);
                                    if (num == null) {
                                        hashMap.put(substring, 1);
                                    } else {
                                        hashMap.put(substring, Integer.valueOf(num.intValue() + 1));
                                    }
                                }
                            }
                            if (hashMap.size() > 0) {
                                int i = 0;
                                for (Map.Entry entry : hashMap.entrySet()) {
                                    if (entry != null) {
                                        if (i > 0) {
                                            sb.append(',');
                                        }
                                        sb.append((String) entry.getKey());
                                        sb.append('=');
                                        sb.append(entry.getValue());
                                        i++;
                                    }
                                }
                            }
                            create.setValue("QueueThread", sb.toString());
                            dimensionValuesAdapter.setValue("QueueThread", sb.toString());
                        }
                    } else {
                        create.setValue("QueueThread", "");
                        dimensionValuesAdapter.setValue("QueueThread", "");
                    }
                }
                create2.setValue("CoreSize", (double) threadPoolExecutor.getCorePoolSize());
                create2.setValue("MaxSize", (double) threadPoolExecutor.getMaximumPoolSize());
                create2.setValue("ActiveCount", (double) threadPoolExecutor.getActiveCount());
                create2.setValue("CompletedCount", (double) threadPoolExecutor.getCompletedTaskCount());
                create2.setValue("ThreadSize", (double) threadPoolExecutor.getTaskCount());
                create2.setValue("CpuMaxFreq", (double) onLineStat.deviceInfo.cpuMaxFreq);
                create2.setValue("DeviceTotalAvailMem", (double) onLineStat.deviceInfo.deviceTotalAvailMemory);
                create2.setValue("DeviceAvailMem", (double) onLineStat.memroyStat.deviceAvailMemory);
                create2.setValue("TotalUsedMem", (double) onLineStat.memroyStat.totalUsedMemory);
                create2.setValue("RemainMem", (double) onLineStat.memroyStat.remainAvailMemory);
                create2.setValue("NativeHeapSize", (double) onLineStat.memroyStat.nativePss);
                create2.setValue("JavaHeapSize", (double) onLineStat.memroyStat.dalvikPss);
                create2.setValue("SysCpuPercent", (double) onLineStat.cpuStat.sysAvgCPUPercent);
                create2.setValue("PidCpuPercent", (double) onLineStat.cpuStat.myPidCPUPercent);
                create2.setValue("SysLoadAvg", (double) onLineStat.cpuStat.pidPerCpuLoadAvg);
                create2.setValue("RuntimeThread", (double) onLineStat.performanceInfo.runTimeThreadCount);
                create2.setValue("RunningThread", (double) onLineStat.performanceInfo.runningThreadCount);
                create2.setValue("DeviceScore", (double) onLineStat.performanceInfo.deviceScore);
                create2.setValue("SysScore", (double) onLineStat.performanceInfo.systemRunningScore);
                create2.setValue("PidScore", (double) onLineStat.performanceInfo.myPidScore);
                measureValuesAdapter.setValue("CoreSize", (double) threadPoolExecutor.getCorePoolSize());
                measureValuesAdapter.setValue("MaxSize", (double) threadPoolExecutor.getMaximumPoolSize());
                measureValuesAdapter.setValue("ActiveCount", (double) threadPoolExecutor.getActiveCount());
                measureValuesAdapter.setValue("CompletedCount", (double) threadPoolExecutor.getCompletedTaskCount());
                measureValuesAdapter.setValue("ThreadSize", (double) threadPoolExecutor.getTaskCount());
                measureValuesAdapter.setValue("CpuMaxFreq", (double) onLineStat.deviceInfo.cpuMaxFreq);
                measureValuesAdapter.setValue("DeviceTotalAvailMem", (double) onLineStat.deviceInfo.deviceTotalAvailMemory);
                measureValuesAdapter.setValue("DeviceAvailMem", (double) onLineStat.memroyStat.deviceAvailMemory);
                measureValuesAdapter.setValue("TotalUsedMem", (double) onLineStat.memroyStat.totalUsedMemory);
                measureValuesAdapter.setValue("RemainMem", (double) onLineStat.memroyStat.remainAvailMemory);
                measureValuesAdapter.setValue("NativeHeapSize", (double) onLineStat.memroyStat.nativePss);
                measureValuesAdapter.setValue("JavaHeapSize", (double) onLineStat.memroyStat.dalvikPss);
                measureValuesAdapter.setValue("SysCpuPercent", (double) onLineStat.cpuStat.sysAvgCPUPercent);
                measureValuesAdapter.setValue("PidCpuPercent", (double) onLineStat.cpuStat.myPidCPUPercent);
                measureValuesAdapter.setValue("SysLoadAvg", (double) onLineStat.cpuStat.pidPerCpuLoadAvg);
                measureValuesAdapter.setValue("RuntimeThread", (double) onLineStat.performanceInfo.runTimeThreadCount);
                measureValuesAdapter.setValue("RunningThread", (double) onLineStat.performanceInfo.runningThreadCount);
                measureValuesAdapter.setValue("DeviceScore", (double) onLineStat.performanceInfo.deviceScore);
                measureValuesAdapter.setValue("SysScore", (double) onLineStat.performanceInfo.systemRunningScore);
                measureValuesAdapter.setValue("PidScore", (double) onLineStat.performanceInfo.myPidScore);
                create.setValue("CpuCore", String.valueOf(onLineStat.deviceInfo.cpuProcessCount));
                create.setValue("activityName", str);
                create.setValue("APILevel", String.valueOf(onLineStat.deviceInfo.apiLevel));
                create.setValue("IsLowMemroy", onLineStat.memroyStat.isLowMemroy ? "true" : "false");
                create.setValue("MemoryLevel", String.valueOf(onLineStat.memroyStat.trimMemoryLevel));
                dimensionValuesAdapter.setValue("CpuCore", String.valueOf(onLineStat.deviceInfo.cpuProcessCount));
                dimensionValuesAdapter.setValue("activityName", str);
                dimensionValuesAdapter.setValue("APILevel", String.valueOf(onLineStat.deviceInfo.apiLevel));
                dimensionValuesAdapter.setValue("IsLowMemroy", onLineStat.memroyStat.isLowMemroy ? "true" : "false");
                dimensionValuesAdapter.setValue("MemoryLevel", String.valueOf(onLineStat.memroyStat.trimMemoryLevel));
                String appendDeviceInfo = appendDeviceInfo(onLineStat, onLineStat.activityRuntimeInfo);
                create.setValue("Info", appendDeviceInfo);
                dimensionValuesAdapter.setValue("Info", appendDeviceInfo);
                if (OnLineMonitor.sIsNormalDebug) {
                    String str3 = this.TAG;
                    Log.e(str3, "onThreadPoolProblem Info =" + appendDeviceInfo);
                }
            } catch (Exception e) {
                e.printStackTrace();
            } catch (Throwable unused) {
                return;
            }
            if (!onLineStat.deviceInfo.isEmulator) {
                if (this.mFieldWorkers == null) {
                    Class<?> cls = Class.forName("java.util.concurrent.ThreadPoolExecutor");
                    Class<?> cls2 = Class.forName("java.util.concurrent.ThreadPoolExecutor$Worker");
                    this.mFieldWorkers = cls.getDeclaredField("workers");
                    this.mFieldWorkers.setAccessible(true);
                    this.mFieldThread = cls2.getDeclaredField("thread");
                    this.mFieldThread.setAccessible(true);
                }
                StringBuilder sb2 = new StringBuilder(1024);
                StringBuilder sb3 = new StringBuilder();
                if (!(this.mFieldWorkers == null || (hashSet = (HashSet) this.mFieldWorkers.get(threadPoolExecutor)) == null)) {
                    Iterator it = hashSet.iterator();
                    int i2 = 1;
                    while (it.hasNext()) {
                        Thread thread = (Thread) this.mFieldThread.get(it.next());
                        if (thread != null) {
                            long id = thread.getId();
                            StackTraceElement[] stackTrace = thread.getStackTrace();
                            String name = thread.getName();
                            if (i2 > 1) {
                                sb3.append(",");
                            }
                            sb3.append(name);
                            sb2.append(i2);
                            sb2.append(12289);
                            sb2.append(name);
                            sb2.append('-');
                            sb2.append(id);
                            sb2.append(Operators.CONDITION_IF_MIDDLE);
                            int i3 = 0;
                            while (i3 < 10 && i3 < stackTrace.length) {
                                sb2.append(stackTrace[i3].toString());
                                sb2.append(' ');
                                i3++;
                            }
                            sb2.append(DinamicTokenizer.TokenSEM);
                            i2++;
                        }
                    }
                    create.setValue("PoolThreadDetail", sb2.toString());
                    create.setValue("PoolThread", sb3.toString());
                    dimensionValuesAdapter.setValue("PoolThreadDetail", sb2.toString());
                    dimensionValuesAdapter.setValue("PoolThread", sb3.toString());
                }
                if (OnLineMonitor.sIsNormalDebug) {
                    String str4 = this.TAG;
                    Log.e(str4, "线程池队列太长：" + sb2.toString());
                }
                AppMonitor.Stat.commit("system", "ThreadPoolProblem", create, create2);
                com.ali.telescope.internal.plugins.onlinemonitor.OnlineStatistics.getInstance().OnThreadPoolProblem(dimensionValuesAdapter.dimensionValues, measureValuesAdapter.measureValues);
            }
        }
    }

    public void onWhiteScreen(OnLineMonitor.OnLineStat onLineStat, String str, int i, int i2, int i3) {
        OnLineMonitor.OnLineStat onLineStat2 = onLineStat;
        String str2 = str;
        if (onLineStat2 != null && str2 != null) {
            try {
                if (!this.mWhiteScreenRegisted) {
                    DimensionSet addDimension = DimensionSet.create().addDimension("activityName").addDimension("CpuCore").addDimension("APILevel").addDimension("IsLowMemroy").addDimension("MemoryLevel");
                    MeasureSet addMeasure = MeasureSet.create().addMeasure("WidthPercent").addMeasure("HeightPercent").addMeasure("UseTime").addMeasure("DeviceMem").addMeasure("CpuMaxFreq").addMeasure("DeviceAvailMem").addMeasure("TotalUsedMem").addMeasure("RemainMem").addMeasure("NativeHeapSize").addMeasure("JavaHeapSize").addMeasure("SysCpuPercent").addMeasure("PidCpuPercent").addMeasure("SysLoadAvg").addMeasure("RuntimeThread").addMeasure("RunningThread").addMeasure("DeviceScore").addMeasure("SysScore").addMeasure("PidScore");
                    this.mWhiteScreenRegisted = true;
                    AppMonitor.register("system", "WhiteScreen", addMeasure, addDimension, true);
                }
                DimensionValueSet create = DimensionValueSet.create();
                MeasureValueSet create2 = MeasureValueSet.create();
                DimensionValuesAdapter dimensionValuesAdapter = new DimensionValuesAdapter();
                MeasureValuesAdapter measureValuesAdapter = new MeasureValuesAdapter();
                double d = (double) i;
                create2.setValue("WidthPercent", d);
                double d2 = (double) i2;
                create2.setValue("HeightPercent", d2);
                double d3 = (double) i3;
                create2.setValue("UseTime", d3);
                create2.setValue("CpuMaxFreq", (double) onLineStat2.deviceInfo.cpuMaxFreq);
                create2.setValue("DeviceAvailMem", (double) onLineStat2.memroyStat.deviceAvailMemory);
                create2.setValue("TotalUsedMem", (double) onLineStat2.memroyStat.totalUsedMemory);
                create2.setValue("RemainMem", (double) onLineStat2.memroyStat.remainAvailMemory);
                create2.setValue("NativeHeapSize", (double) onLineStat2.memroyStat.nativePss);
                create2.setValue("JavaHeapSize", (double) onLineStat2.memroyStat.dalvikPss);
                create2.setValue("SysCpuPercent", (double) onLineStat2.cpuStat.sysAvgCPUPercent);
                create2.setValue("PidCpuPercent", (double) onLineStat2.cpuStat.myPidCPUPercent);
                create2.setValue("SysLoadAvg", (double) onLineStat2.cpuStat.pidPerCpuLoadAvg);
                create2.setValue("RuntimeThread", (double) onLineStat2.performanceInfo.runTimeThreadCount);
                create2.setValue("RunningThread", (double) onLineStat2.performanceInfo.runningThreadCount);
                create2.setValue("DeviceScore", (double) onLineStat2.performanceInfo.deviceScore);
                create2.setValue("SysScore", (double) onLineStat2.performanceInfo.systemRunningScore);
                create2.setValue("PidScore", (double) onLineStat2.performanceInfo.myPidScore);
                measureValuesAdapter.setValue("WidthPercent", d);
                measureValuesAdapter.setValue("HeightPercent", d2);
                measureValuesAdapter.setValue("UseTime", d3);
                measureValuesAdapter.setValue("CpuMaxFreq", (double) onLineStat2.deviceInfo.cpuMaxFreq);
                measureValuesAdapter.setValue("DeviceAvailMem", (double) onLineStat2.memroyStat.deviceAvailMemory);
                measureValuesAdapter.setValue("TotalUsedMem", (double) onLineStat2.memroyStat.totalUsedMemory);
                measureValuesAdapter.setValue("RemainMem", (double) onLineStat2.memroyStat.remainAvailMemory);
                measureValuesAdapter.setValue("NativeHeapSize", (double) onLineStat2.memroyStat.nativePss);
                measureValuesAdapter.setValue("JavaHeapSize", (double) onLineStat2.memroyStat.dalvikPss);
                measureValuesAdapter.setValue("SysCpuPercent", (double) onLineStat2.cpuStat.sysAvgCPUPercent);
                measureValuesAdapter.setValue("PidCpuPercent", (double) onLineStat2.cpuStat.myPidCPUPercent);
                measureValuesAdapter.setValue("SysLoadAvg", (double) onLineStat2.cpuStat.pidPerCpuLoadAvg);
                measureValuesAdapter.setValue("RuntimeThread", (double) onLineStat2.performanceInfo.runTimeThreadCount);
                measureValuesAdapter.setValue("RunningThread", (double) onLineStat2.performanceInfo.runningThreadCount);
                measureValuesAdapter.setValue("DeviceScore", (double) onLineStat2.performanceInfo.deviceScore);
                measureValuesAdapter.setValue("SysScore", (double) onLineStat2.performanceInfo.systemRunningScore);
                measureValuesAdapter.setValue("PidScore", (double) onLineStat2.performanceInfo.myPidScore);
                create.setValue("CpuCore", String.valueOf(onLineStat2.deviceInfo.cpuProcessCount));
                create.setValue("activityName", str2);
                create.setValue("APILevel", String.valueOf(onLineStat2.deviceInfo.apiLevel));
                create.setValue("IsLowMemroy", onLineStat2.memroyStat.isLowMemroy ? "true" : "false");
                create.setValue("MemoryLevel", String.valueOf(onLineStat2.memroyStat.trimMemoryLevel));
                dimensionValuesAdapter.setValue("CpuCore", String.valueOf(onLineStat2.deviceInfo.cpuProcessCount));
                dimensionValuesAdapter.setValue("activityName", str2);
                dimensionValuesAdapter.setValue("APILevel", String.valueOf(onLineStat2.deviceInfo.apiLevel));
                dimensionValuesAdapter.setValue("IsLowMemroy", onLineStat2.memroyStat.isLowMemroy ? "true" : "false");
                dimensionValuesAdapter.setValue("MemoryLevel", String.valueOf(onLineStat2.memroyStat.trimMemoryLevel));
                if (!onLineStat2.deviceInfo.isEmulator) {
                    AppMonitor.Stat.commit("system", "WhiteScreen", create, create2);
                    com.ali.telescope.internal.plugins.onlinemonitor.OnlineStatistics.getInstance().WhiteScreen(dimensionValuesAdapter.dimensionValues, measureValuesAdapter.measureValues);
                }
            } catch (Throwable unused) {
            }
        }
    }

    public void onBootPerformance(OnLineMonitor.OnLineStat onLineStat, List<OnLineMonitor.ResourceUsedInfo> list, boolean z, String str, long j) {
        OnLineMonitor.OnLineStat onLineStat2 = onLineStat;
        List<OnLineMonitor.ResourceUsedInfo> list2 = list;
        String str2 = str;
        long j2 = j;
        if (onLineStat2 != null && list2 != null && z && !onLineStat2.isFirstInstall && !onLineStat2.deviceInfo.isEmulator) {
            try {
                AppMonitor.register("system", "BootPerformance", MeasureSet.create().addMeasure("DeviceMem").addMeasure("DeviceAvailMem").addMeasure("TotalUsedMem").addMeasure("RemainMem").addMeasure("NativeHeapSize").addMeasure("JavaHeapSize").addMeasure("SysCpuPercent").addMeasure("PidCpuPercent").addMeasure("SysLoadAvg").addMeasure("ThreadCount").addMeasure("DeviceScore").addMeasure("SysScore").addMeasure("PidScore").addMeasure("PercentInBoot").addMeasure("PercentInPid").addMeasure("PercentInSystem").addMeasure("PercentInDevice").addMeasure("TaskUsedTime").addMeasure("TaskCpuTime"), DimensionSet.create().addDimension("MemoryLevel").addDimension("BootType").addDimension("InBootStep").addDimension("TaskName"));
                for (int i = 0; i < list.size(); i++) {
                    OnLineMonitor.ResourceUsedInfo resourceUsedInfo = list2.get(i);
                    DimensionValueSet create = DimensionValueSet.create();
                    MeasureValueSet create2 = MeasureValueSet.create();
                    DimensionValuesAdapter dimensionValuesAdapter = new DimensionValuesAdapter();
                    MeasureValuesAdapter measureValuesAdapter = new MeasureValuesAdapter();
                    create2.setValue("DeviceScore", (double) onLineStat2.performanceInfo.deviceScore);
                    create2.setValue("SysScore", (double) onLineStat2.performanceInfo.systemRunningScore);
                    create2.setValue("PidScore", (double) onLineStat2.performanceInfo.myPidScore);
                    create2.setValue("ThreadCount", (double) resourceUsedInfo.threadMax);
                    create2.setValue("DeviceMem", (double) onLineStat2.deviceInfo.deviceTotalMemory);
                    create2.setValue("DeviceAvailMem", (double) onLineStat2.memroyStat.deviceAvailMemory);
                    create2.setValue("TotalUsedMem", (double) onLineStat2.memroyStat.totalUsedMemory);
                    create2.setValue("RemainMem", (double) onLineStat2.memroyStat.remainAvailMemory);
                    create2.setValue("NativeHeapSize", (double) onLineStat2.memroyStat.nativePss);
                    create2.setValue("JavaHeapSize", (double) onLineStat2.memroyStat.dalvikPss);
                    create2.setValue("SysLoadAvg", (double) onLineStat2.cpuStat.systemLoadAvg);
                    create2.setValue("TaskUsedTime", (double) resourceUsedInfo.debugUsedTime);
                    create2.setValue("TaskCpuTime", (double) resourceUsedInfo.debugUsedCpuTime);
                    measureValuesAdapter.setValue("DeviceScore", (double) onLineStat2.performanceInfo.deviceScore);
                    measureValuesAdapter.setValue("SysScore", (double) onLineStat2.performanceInfo.systemRunningScore);
                    measureValuesAdapter.setValue("PidScore", (double) onLineStat2.performanceInfo.myPidScore);
                    measureValuesAdapter.setValue("ThreadCount", (double) resourceUsedInfo.threadMax);
                    measureValuesAdapter.setValue("DeviceMem", (double) onLineStat2.deviceInfo.deviceTotalMemory);
                    measureValuesAdapter.setValue("DeviceAvailMem", (double) onLineStat2.memroyStat.deviceAvailMemory);
                    measureValuesAdapter.setValue("TotalUsedMem", (double) onLineStat2.memroyStat.totalUsedMemory);
                    measureValuesAdapter.setValue("RemainMem", (double) onLineStat2.memroyStat.remainAvailMemory);
                    measureValuesAdapter.setValue("NativeHeapSize", (double) onLineStat2.memroyStat.nativePss);
                    measureValuesAdapter.setValue("JavaHeapSize", (double) onLineStat2.memroyStat.dalvikPss);
                    measureValuesAdapter.setValue("SysLoadAvg", (double) onLineStat2.cpuStat.systemLoadAvg);
                    measureValuesAdapter.setValue("TaskUsedTime", (double) resourceUsedInfo.debugUsedTime);
                    measureValuesAdapter.setValue("TaskCpuTime", (double) resourceUsedInfo.debugUsedCpuTime);
                    if (j2 > 0) {
                        double d = (double) (((float) resourceUsedInfo.threadJiffyTime) / ((float) j2));
                        create2.setValue("PercentInBoot", d);
                        measureValuesAdapter.setValue("PercentInBoot", d);
                        if (resourceUsedInfo.systemJiffyTime > 0) {
                            double d2 = (double) (((float) resourceUsedInfo.threadJiffyTime) / ((float) resourceUsedInfo.systemJiffyTime));
                            create2.setValue("PercentInSystem", d2);
                            measureValuesAdapter.setValue("PercentInSystem", d2);
                        }
                        if (resourceUsedInfo.systemJiffyTime > 0) {
                            double d3 = (double) (((float) resourceUsedInfo.threadJiffyTime) / ((float) resourceUsedInfo.pidJiffyTime));
                            create2.setValue("PercentInPid", d3);
                            measureValuesAdapter.setValue("PercentInPid", d3);
                        }
                        if (resourceUsedInfo.totalJiffyTime > 0) {
                            double d4 = (double) (((float) resourceUsedInfo.threadJiffyTime) / ((float) resourceUsedInfo.totalJiffyTime));
                            create2.setValue("PercentInDevice", d4);
                            measureValuesAdapter.setValue("PercentInDevice", d4);
                            double d5 = (double) (((float) resourceUsedInfo.pidJiffyTime) / ((float) resourceUsedInfo.totalJiffyTime));
                            create2.setValue("PidCpuPercent", d5);
                            measureValuesAdapter.setValue("PidCpuPercent", d5);
                            double d6 = (double) (((float) resourceUsedInfo.systemJiffyTime) / ((float) resourceUsedInfo.totalJiffyTime));
                            create2.setValue("SysCpuPercent", d6);
                            measureValuesAdapter.setValue("SysCpuPercent", d6);
                        }
                    }
                    create.setValue("MemoryLevel", String.valueOf(onLineStat2.memroyStat.trimMemoryLevel));
                    create.setValue("TaskName", resourceUsedInfo.taskName);
                    create.setValue("InBootStep", resourceUsedInfo.isInBootStep ? "true" : "false");
                    dimensionValuesAdapter.setValue("MemoryLevel", String.valueOf(onLineStat2.memroyStat.trimMemoryLevel));
                    dimensionValuesAdapter.setValue("TaskName", resourceUsedInfo.taskName);
                    dimensionValuesAdapter.setValue("InBootStep", resourceUsedInfo.isInBootStep ? "true" : "false");
                    if (z) {
                        create.setValue("BootType", str2);
                        dimensionValuesAdapter.setValue("BootType", str2);
                    } else {
                        create.setValue("BootType", "HotBoot");
                        dimensionValuesAdapter.setValue("BootType", "HotBoot");
                    }
                    AppMonitor.Stat.commit("system", "BootPerformance", create, create2);
                    com.ali.telescope.internal.plugins.onlinemonitor.OnlineStatistics.getInstance().OnBootPerfmance(dimensionValuesAdapter.dimensionValues, measureValuesAdapter.measureValues);
                }
            } catch (Throwable unused) {
            }
        }
    }
}
