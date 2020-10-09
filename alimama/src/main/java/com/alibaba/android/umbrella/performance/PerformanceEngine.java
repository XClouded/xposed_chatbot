package com.alibaba.android.umbrella.performance;

import android.text.TextUtils;
import androidx.annotation.Keep;
import com.alibaba.android.umbrella.trace.UmbrellaSimple;
import com.alibaba.android.umbrella.utils.UmbrellaConstants;
import com.alibaba.mtl.appmonitor.AppMonitor;
import com.taobao.tao.log.TLog;

@Keep
public class PerformanceEngine {
    static void commitPerformancePage(PerformanceEntity performanceEntity) {
    }

    static void commitPerformancePage(ProcessEntity processEntity) {
        if (processEntity != null && !TextUtils.isEmpty(processEntity.bizName) && !UmbrellaSimple.isForceClosePerformancePage()) {
            TLog.loge("PerformanceEngine", processEntity.toJsonString());
            AppMonitor.Alarm.commitSuccess(UmbrellaConstants.PERFORMANCE_MODULE, UmbrellaConstants.PERFORMANCE_POINT, processEntity.toJsonString());
        }
    }

    static void commitPerformancePoint(PerformanceEntity performanceEntity) {
        if (!UmbrellaSimple.isForceClosePerformancePoint()) {
            AppMonitor.Alarm.commitSuccess(UmbrellaConstants.PERFORMANCE_MODULE, "Monitor_" + performanceEntity.bizName + "_Service", performanceEntity.toJsonString());
        }
    }
}
