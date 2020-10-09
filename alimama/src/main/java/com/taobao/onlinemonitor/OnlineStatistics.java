package com.taobao.onlinemonitor;

import android.app.Activity;
import com.taobao.onlinemonitor.OnLineMonitor;
import java.util.List;
import java.util.Map;
import java.util.concurrent.ThreadPoolExecutor;

public interface OnlineStatistics {
    void onActivityPaused(Activity activity, OnLineMonitor.OnLineStat onLineStat, OnLineMonitor.ActivityRuntimeInfo activityRuntimeInfo);

    void onAnr(OnLineMonitor.OnLineStat onLineStat, String str, Map<Thread, StackTraceElement[]> map);

    void onBlockOrCloseGuard(OnLineMonitor.OnLineStat onLineStat, int i, String str, String str2, String str3, String str4, int i2);

    void onBootFinished(OnLineMonitor.OnLineStat onLineStat, long j, long j2, boolean z, String str);

    void onBootPerformance(OnLineMonitor.OnLineStat onLineStat, List<OnLineMonitor.ResourceUsedInfo> list, boolean z, String str, long j);

    void onCreatePerformanceReport(OnLineMonitor.OnLineStat onLineStat, OutputData outputData);

    void onGotoSleep(OnLineMonitor.OnLineStat onLineStat, Map<String, ThreadInfo> map, Map<String, Integer> map2, Map<String, OnLineMonitor.ThreadIoInfo> map3);

    void onMemoryLeak(String str, long j, String str2);

    void onMemoryProblem(OnLineMonitor.OnLineStat onLineStat, String str, String str2, String str3, String str4);

    void onSmoothStop(OnLineMonitor.OnLineStat onLineStat, OnLineMonitor.ActivityRuntimeInfo activityRuntimeInfo, long j, int i, int[] iArr, int i2, String str, int i3, long j2, int i4, int i5, String str2, int i6);

    void onThreadPoolProblem(OnLineMonitor.OnLineStat onLineStat, String str, ThreadPoolExecutor threadPoolExecutor, String str2);

    void onWhiteScreen(OnLineMonitor.OnLineStat onLineStat, String str, int i, int i2, int i3);
}
