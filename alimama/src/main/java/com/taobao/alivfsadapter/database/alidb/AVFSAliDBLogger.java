package com.taobao.alivfsadapter.database.alidb;

import com.alibaba.mtl.appmonitor.AppMonitor;
import com.alibaba.mtl.appmonitor.model.DimensionSet;
import com.alibaba.mtl.appmonitor.model.DimensionValueSet;
import com.alibaba.mtl.appmonitor.model.MeasureSet;
import com.alibaba.mtl.appmonitor.model.MeasureValueSet;
import com.taobao.android.alivfsdb.IDBLogger;
import java.util.Collection;
import java.util.List;
import java.util.Map;

public class AVFSAliDBLogger implements IDBLogger {
    private static final String TAG = "AVFSAliDBLogger";

    public void commitSuccess(String str, String str2, String str3) {
        AppMonitor.Alarm.commitSuccess(str, str2, str3);
    }

    public void commitFail(String str, String str2, String str3, String str4, String str5) {
        AppMonitor.Alarm.commitFail(str, str2, str3, str4, str5);
    }

    public void register(String str, String str2, List<String> list, List<String> list2) {
        AppMonitor.register(str, str2, MeasureSet.create((Collection<String>) list), DimensionSet.create((Collection<String>) list2));
    }

    public void commit(String str, String str2, Map<String, String> map, Map<String, Double> map2) {
        DimensionValueSet dimensionValueSet;
        if (map != null) {
            dimensionValueSet = DimensionValueSet.fromStringMap(map);
        } else {
            dimensionValueSet = null;
        }
        AppMonitor.Stat.commit(str, str2, dimensionValueSet, MeasureValueSet.create(map2));
    }
}
