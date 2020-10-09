package com.taobao.orange.util;

import android.text.TextUtils;
import com.alibaba.mtl.appmonitor.AppMonitor;
import com.alibaba.mtl.appmonitor.model.DimensionSet;
import com.alibaba.mtl.appmonitor.model.DimensionValueSet;
import com.alibaba.mtl.appmonitor.model.Measure;
import com.alibaba.mtl.appmonitor.model.MeasureSet;
import com.alibaba.mtl.appmonitor.model.MeasureValueSet;
import com.taobao.orange.OConstant;

public class OrangeMonitor {
    public static final String TAG = "OrangeMonitor";
    public static boolean mAppMonitorValid = false;
    public static boolean mPerformanceInfoRecordDone = false;

    static {
        try {
            Class.forName(OConstant.REFLECT_APPMONITOR);
            mAppMonitorValid = true;
        } catch (ClassNotFoundException unused) {
            mAppMonitorValid = false;
        }
    }

    public static void commitSuccess(String str, String str2, String str3) {
        if (mAppMonitorValid) {
            AppMonitor.Alarm.commitSuccess(str, str2, str3);
        }
    }

    public static void commitFail(String str, String str2, String str3, String str4, String str5) {
        if (mAppMonitorValid) {
            AppMonitor.Alarm.commitFail(str, str2, str3, str4, str5);
        }
    }

    public static void commitCount(String str, String str2, String str3, double d) {
        if (mAppMonitorValid) {
            AppMonitor.Counter.commit(str, str2, str3, d);
        }
    }

    public static void register(String str, String str2, MeasureSet measureSet, DimensionSet dimensionSet, boolean z) {
        if (mAppMonitorValid) {
            AppMonitor.register(str, str2, measureSet, dimensionSet, z);
        }
    }

    public static void commitStat(String str, String str2, DimensionValueSet dimensionValueSet, MeasureValueSet measureValueSet) {
        if (mAppMonitorValid) {
            AppMonitor.Stat.commit(str, str2, dimensionValueSet, measureValueSet);
        }
    }

    public static void init() {
        if (mAppMonitorValid) {
            DimensionSet create = DimensionSet.create();
            create.addDimension("bootType");
            create.addDimension("downgradeType");
            create.addDimension("monitorType");
            MeasureSet create2 = MeasureSet.create();
            create2.addMeasure(createMeasureWithRange("requestCount", 10000.0d));
            create2.addMeasure(createMeasureWithRange("persistCount", 10000.0d));
            create2.addMeasure(createMeasureWithRange("restoreCount", 10000.0d));
            create2.addMeasure(createMeasureWithRange("persistTime", 1000000.0d));
            create2.addMeasure(createMeasureWithRange("restoreTime", 1000000.0d));
            create2.addMeasure(createMeasureWithRange("ioTime", 1000000.0d));
            register(OConstant.MONITOR_MODULE, OConstant.POINT_BOOT_PERF, create2, create, false);
            DimensionSet create3 = DimensionSet.create();
            create3.addDimension(OConstant.DIMEN_CONFIG_NAME);
            create3.addDimension("configVersion");
            MeasureSet create4 = MeasureSet.create();
            register(OConstant.MONITOR_MODULE, OConstant.POINT_CONFIG_PENDING_UPDATE, create4, create3, false);
            register(OConstant.MONITOR_MODULE, OConstant.POINT_CONFIG_UPDATE, create4, create3, false);
            register(OConstant.MONITOR_MODULE, OConstant.POINT_CONFIG_USE, create4, create3, false);
        }
    }

    public static void commitBootPerformanceInfo(OrangeMonitorData orangeMonitorData) {
        if (mAppMonitorValid) {
            DimensionValueSet create = DimensionValueSet.create();
            create.setValue("bootType", orangeMonitorData.performance.bootType ? "1" : "0");
            create.setValue("downgradeType", String.valueOf(orangeMonitorData.performance.downgradeType));
            create.setValue("monitorType", String.valueOf(orangeMonitorData.performance.monitorType));
            MeasureValueSet create2 = MeasureValueSet.create();
            create2.setValue("requestCount", (double) orangeMonitorData.performance.requestCount);
            create2.setValue("persistCount", (double) orangeMonitorData.performance.persistCount);
            create2.setValue("restoreCount", (double) orangeMonitorData.performance.restoreCount);
            create2.setValue("persistTime", (double) orangeMonitorData.performance.persistTime);
            create2.setValue("restoreTime", (double) orangeMonitorData.performance.restoreTime);
            create2.setValue("ioTime", (double) orangeMonitorData.performance.ioTime);
            commitStat(OConstant.MONITOR_MODULE, OConstant.POINT_BOOT_PERF, create, create2);
            OLog.d(TAG, "commit boot stat", orangeMonitorData.performance.toString());
        }
    }

    public static void commitConfigMonitor(String str, String str2, String str3) {
        if (mAppMonitorValid) {
            if (TextUtils.isEmpty(str) || TextUtils.isEmpty(str2) || TextUtils.isEmpty(str3)) {
                OLog.e(TAG, "commit error because data empty!", new Object[0]);
                return;
            }
            DimensionValueSet create = DimensionValueSet.create();
            create.setValue(OConstant.DIMEN_CONFIG_NAME, str2);
            create.setValue("configVersion", str3);
            commitStat(OConstant.MONITOR_MODULE, str, create, MeasureValueSet.create());
        }
    }

    private static Measure createMeasureWithRange(String str, double d) {
        Measure measure = new Measure(str);
        measure.setRange(Double.valueOf(0.0d), Double.valueOf(d));
        return measure;
    }
}
