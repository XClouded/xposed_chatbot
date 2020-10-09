package com.taobao.alivfsadapter.appmonitor;

import android.util.Log;
import com.alibaba.mtl.appmonitor.AppMonitor;
import com.alibaba.mtl.appmonitor.AppMonitorStatTable;
import com.alibaba.mtl.appmonitor.model.DimensionSet;
import com.alibaba.mtl.appmonitor.model.DimensionValueSet;
import com.alibaba.mtl.appmonitor.model.MeasureSet;
import com.alibaba.mtl.appmonitor.model.MeasureValueSet;
import com.taobao.alivfsadapter.AVFSSDKAppMonitor;
import com.taobao.alivfsadapter.MonitorCacheEvent;

public class AVFSSDKAppMonitorImpl implements AVFSSDKAppMonitor {
    private static final String DIMENSION_CACHE = "Cache";
    private static final String DIMENSION_HIT_MEMORY = "HitMemory";
    private static final String DIMENSION_MEMORYCACHE = "MemoryCache";
    private static final String DIMENSION_MODULE = "Module";
    private static final String DIMENSION_OPERATION = "Operation";
    private static final String MEASURE_DISK_COST = "DiskCost";
    private static final String MODULE_NAME = "AliVfsSDK";
    private static final String MONITOR_POINT_MEMORY_HIT_RATE = "MemoryCacheHitRate";
    private static final String MONITOR_POINT_STAT_CACHE = "Cache";
    private static final String TAG = "AVFSSDKAppMonitorImpl";
    private final AppMonitorStatTable mStatTable = new AppMonitorStatTable(MODULE_NAME, "Cache");

    public AVFSSDKAppMonitorImpl() {
        MeasureSet create = MeasureSet.create();
        create.addMeasure(MEASURE_DISK_COST);
        DimensionSet create2 = DimensionSet.create();
        create2.addDimension("Cache");
        create2.addDimension(DIMENSION_MODULE);
        create2.addDimension(DIMENSION_OPERATION);
        create2.addDimension(DIMENSION_HIT_MEMORY);
        create2.addDimension(DIMENSION_MEMORYCACHE);
        this.mStatTable.registerRowAndColumn(create2, create, false);
    }

    public void hitMemoryCacheForModule(String str, boolean z) {
        if (z) {
            AppMonitor.Alarm.commitSuccess(MODULE_NAME, MONITOR_POINT_MEMORY_HIT_RATE, str);
        } else {
            AppMonitor.Alarm.commitFail(MODULE_NAME, MONITOR_POINT_MEMORY_HIT_RATE, str, (String) null, (String) null);
        }
    }

    public void writeEvent(MonitorCacheEvent monitorCacheEvent) {
        try {
            String monitorPoint = getMonitorPoint(monitorCacheEvent.cache, monitorCacheEvent.operation);
            if (monitorCacheEvent.errorCode == 0) {
                AppMonitor.Alarm.commitSuccess(MODULE_NAME, monitorPoint, monitorCacheEvent.moduleName);
                DimensionValueSet create = DimensionValueSet.create();
                create.setValue("Cache", monitorCacheEvent.cache);
                create.setValue(DIMENSION_MODULE, monitorCacheEvent.moduleName);
                create.setValue(DIMENSION_OPERATION, monitorCacheEvent.operation);
                create.setValue(DIMENSION_HIT_MEMORY, String.valueOf(monitorCacheEvent.hitMemory));
                create.setValue(DIMENSION_MEMORYCACHE, String.valueOf(monitorCacheEvent.memoryCache));
                MeasureValueSet create2 = MeasureValueSet.create();
                create2.setValue(MEASURE_DISK_COST, (double) monitorCacheEvent.diskTime);
                this.mStatTable.update(create, create2);
                AppMonitor.Stat.commit(MODULE_NAME, "Cache", create, create2);
                return;
            }
            AppMonitor.Alarm.commitFail(MODULE_NAME, monitorPoint, monitorCacheEvent.moduleName, String.valueOf(monitorCacheEvent.errorCode), monitorCacheEvent.errorMessage);
        } catch (Exception e) {
            Log.e(TAG, e.getMessage(), e);
        }
    }

    public static String getMonitorPoint(String str, String str2) {
        return getCacheString(str) + getOperationString(str2);
    }

    /* JADX WARNING: Removed duplicated region for block: B:17:0x0036  */
    /* JADX WARNING: Removed duplicated region for block: B:19:0x004d A[RETURN] */
    /* JADX WARNING: Removed duplicated region for block: B:20:0x0050 A[RETURN] */
    /* JADX WARNING: Removed duplicated region for block: B:21:0x0053 A[RETURN] */
    /* Code decompiled incorrectly, please refer to instructions dump. */
    public static java.lang.String getCacheString(java.lang.String r3) {
        /*
            int r0 = r3.hashCode()
            r1 = 114126(0x1bdce, float:1.59925E-40)
            if (r0 == r1) goto L_0x0028
            r1 = 3143036(0x2ff57c, float:4.404332E-39)
            if (r0 == r1) goto L_0x001e
            r1 = 3355087(0x3331cf, float:4.701478E-39)
            if (r0 == r1) goto L_0x0014
            goto L_0x0032
        L_0x0014:
            java.lang.String r0 = "mmap"
            boolean r0 = r3.equals(r0)
            if (r0 == 0) goto L_0x0032
            r0 = 2
            goto L_0x0033
        L_0x001e:
            java.lang.String r0 = "file"
            boolean r0 = r3.equals(r0)
            if (r0 == 0) goto L_0x0032
            r0 = 0
            goto L_0x0033
        L_0x0028:
            java.lang.String r0 = "sql"
            boolean r0 = r3.equals(r0)
            if (r0 == 0) goto L_0x0032
            r0 = 1
            goto L_0x0033
        L_0x0032:
            r0 = -1
        L_0x0033:
            switch(r0) {
                case 0: goto L_0x0053;
                case 1: goto L_0x0050;
                case 2: goto L_0x004d;
                default: goto L_0x0036;
            }
        L_0x0036:
            java.lang.IllegalArgumentException r0 = new java.lang.IllegalArgumentException
            java.lang.StringBuilder r1 = new java.lang.StringBuilder
            r1.<init>()
            java.lang.String r2 = "Unknown URL: "
            r1.append(r2)
            r1.append(r3)
            java.lang.String r3 = r1.toString()
            r0.<init>(r3)
            throw r0
        L_0x004d:
            java.lang.String r3 = "MmapCache"
            return r3
        L_0x0050:
            java.lang.String r3 = "SQLiteCache"
            return r3
        L_0x0053:
            java.lang.String r3 = "FileCache"
            return r3
        */
        throw new UnsupportedOperationException("Method not decompiled: com.taobao.alivfsadapter.appmonitor.AVFSSDKAppMonitorImpl.getCacheString(java.lang.String):java.lang.String");
    }

    /* JADX WARNING: Removed duplicated region for block: B:12:0x0027  */
    /* JADX WARNING: Removed duplicated region for block: B:14:0x003e A[RETURN] */
    /* JADX WARNING: Removed duplicated region for block: B:15:0x0041 A[RETURN] */
    /* Code decompiled incorrectly, please refer to instructions dump. */
    public static java.lang.String getOperationString(java.lang.String r3) {
        /*
            int r0 = r3.hashCode()
            r1 = 3496342(0x355996, float:4.899419E-39)
            if (r0 == r1) goto L_0x0019
            r1 = 113399775(0x6c257df, float:7.3103804E-35)
            if (r0 == r1) goto L_0x000f
            goto L_0x0023
        L_0x000f:
            java.lang.String r0 = "write"
            boolean r0 = r3.equals(r0)
            if (r0 == 0) goto L_0x0023
            r0 = 1
            goto L_0x0024
        L_0x0019:
            java.lang.String r0 = "read"
            boolean r0 = r3.equals(r0)
            if (r0 == 0) goto L_0x0023
            r0 = 0
            goto L_0x0024
        L_0x0023:
            r0 = -1
        L_0x0024:
            switch(r0) {
                case 0: goto L_0x0041;
                case 1: goto L_0x003e;
                default: goto L_0x0027;
            }
        L_0x0027:
            java.lang.IllegalArgumentException r0 = new java.lang.IllegalArgumentException
            java.lang.StringBuilder r1 = new java.lang.StringBuilder
            r1.<init>()
            java.lang.String r2 = "Unknown URL: "
            r1.append(r2)
            r1.append(r3)
            java.lang.String r3 = r1.toString()
            r0.<init>(r3)
            throw r0
        L_0x003e:
            java.lang.String r3 = "Write"
            return r3
        L_0x0041:
            java.lang.String r3 = "Read"
            return r3
        */
        throw new UnsupportedOperationException("Method not decompiled: com.taobao.alivfsadapter.appmonitor.AVFSSDKAppMonitorImpl.getOperationString(java.lang.String):java.lang.String");
    }
}
