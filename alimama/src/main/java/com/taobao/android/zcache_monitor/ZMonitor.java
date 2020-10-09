package com.taobao.android.zcache_monitor;

import com.alibaba.aliweex.adapter.module.location.ILocatable;
import com.alibaba.mtl.appmonitor.AppMonitor;
import com.alibaba.mtl.appmonitor.model.DimensionSet;
import com.alibaba.mtl.appmonitor.model.DimensionValueSet;
import com.alibaba.mtl.appmonitor.model.Measure;
import com.alibaba.mtl.appmonitor.model.MeasureSet;
import com.alibaba.mtl.appmonitor.model.MeasureValueSet;
import com.taobao.zcache.monitor.ZMonitorManager;
import java.util.Map;

public class ZMonitor {
    private static final String MONITOR_INVALID_RESOURE = "InvalidResource";
    private static final String MONITOR_MODULE = "ZCache";
    private static final String MONITOR_POINT_APPUPDATE = "AppUpdate";
    private static final String MONITOR_POINT_APPVISIT = "AppVisit";
    private static final String MONITOR_POINT_CLEAN_UP = "Cleanup";
    private static final String MONITOR_POINT_CONFIG_UPDATE = "ConfigUpdate";
    private static final String MONITOR_POINT_UPDATE_QUEUE = "UpdateQueue";
    private static ZMonitor instance;

    public static ZMonitor getInstance() {
        if (instance == null) {
            synchronized (ZMonitor.class) {
                if (instance == null) {
                    instance = new ZMonitor();
                }
            }
        }
        return instance;
    }

    public void init() {
        initMonitor();
        ZMonitorManager.getInstance().setMonitorProxy(new ZMonitorImpl());
    }

    private static Measure createMeasuerWithRange(String str, double d, double d2) {
        Measure measure = new Measure(str);
        measure.setRange(Double.valueOf(d), Double.valueOf(d2));
        return measure;
    }

    private void initMonitor() {
        DimensionSet create = DimensionSet.create();
        create.addDimension("originVersion");
        create.addDimension("targetVersion");
        create.addDimension("updatedVersion");
        create.addDimension("errorCode");
        create.addDimension(ILocatable.ERROR_MSG);
        create.addDimension("configURL");
        create.addDimension("trigger");
        create.addDimension("sessionID");
        MeasureSet create2 = MeasureSet.create();
        create2.addMeasure(createMeasuerWithRange("downloaded", 0.0d, 600000.0d));
        create2.addMeasure(createMeasuerWithRange("updated", 0.0d, 600000.0d));
        AppMonitor.register(MONITOR_MODULE, MONITOR_POINT_CONFIG_UPDATE, create2, create);
        DimensionSet create3 = DimensionSet.create();
        create3.addDimension("appName");
        create3.addDimension("oldSeq");
        create3.addDimension("newSeq");
        create3.addDimension("errorCode");
        create3.addDimension(ILocatable.ERROR_MSG);
        create3.addDimension("online");
        create3.addDimension("incr");
        create3.addDimension("fileURL");
        create3.addDimension("trigger");
        create3.addDimension("sessionID");
        MeasureSet create4 = MeasureSet.create();
        create4.addMeasure(createMeasuerWithRange("configLoaded", 0.0d, 600000.0d));
        create4.addMeasure(createMeasuerWithRange("fileLoaded", 0.0d, 600000.0d));
        create4.addMeasure(createMeasuerWithRange("decrypted", 0.0d, 600000.0d));
        create4.addMeasure(createMeasuerWithRange("unzipped", 0.0d, 600000.0d));
        create4.addMeasure(createMeasuerWithRange("verified", 0.0d, 600000.0d));
        create4.addMeasure(createMeasuerWithRange("updateFinished", 0.0d, 600000.0d));
        create4.addMeasure("notificationTime");
        create4.addMeasure("publishTime");
        create4.addMeasure("waitingTime");
        AppMonitor.register(MONITOR_MODULE, MONITOR_POINT_APPUPDATE, create4, create3);
        DimensionSet create5 = DimensionSet.create();
        create5.addDimension("appName");
        create5.addDimension("packSeq");
        create5.addDimension("errorCode");
        create5.addDimension(ILocatable.ERROR_MSG);
        create5.addDimension("comboCount");
        create5.addDimension("sessionID");
        create5.addDimension("trigger");
        MeasureSet create6 = MeasureSet.create();
        create6.addMeasure(createMeasuerWithRange("time", 0.0d, 600000.0d));
        create6.addMeasure(createMeasuerWithRange("matchTime", 0.0d, 600000.0d));
        create6.addMeasure(createMeasuerWithRange("readAppResTime", 0.0d, 600000.0d));
        create6.addMeasure(createMeasuerWithRange("comboLoaderTime", 0.0d, 600000.0d));
        create6.addMeasure(createMeasuerWithRange("verifyTime", 0.0d, 600000.0d));
        create6.addMeasure(createMeasuerWithRange("readFileTime", 0.0d, 600000.0d));
        AppMonitor.register(MONITOR_MODULE, MONITOR_POINT_APPVISIT, create6, create5);
        DimensionSet create7 = DimensionSet.create();
        create7.addDimension("trigger");
        create7.addDimension("sessionID");
        MeasureSet create8 = MeasureSet.create();
        create8.addMeasure("fastCount");
        create8.addMeasure("fastSuccessCount");
        create8.addMeasure("normalCount");
        create8.addMeasure("normalSuccessCount");
        create8.addMeasure("time");
        AppMonitor.register(MONITOR_MODULE, MONITOR_POINT_UPDATE_QUEUE, create8, create7);
        DimensionSet create9 = DimensionSet.create();
        create9.addDimension("installedAppCount");
        create9.addDimension("expectedAppCount");
        create9.addDimension("trigger");
        create9.addDimension("sessionID");
        MeasureSet create10 = MeasureSet.create();
        create10.addMeasure("accessedAppCount");
        create10.addMeasure("accessCount");
        create10.addMeasure("eliminatedAppCount");
        create10.addMeasure("eliminatedAccessCount");
        create10.addMeasure("finished");
        AppMonitor.register(MONITOR_MODULE, MONITOR_POINT_CLEAN_UP, create10, create9);
        DimensionSet create11 = DimensionSet.create();
        create11.addDimension("appName");
        create11.addDimension("packSeq");
        create11.addDimension("url");
        create11.addDimension("sessionID");
        AppMonitor.register(MONITOR_MODULE, MONITOR_INVALID_RESOURE, MeasureSet.create(), create11);
    }

    private MeasureValueSet getMeasureValueSet(Map<String, Double> map) {
        MeasureValueSet create = MeasureValueSet.create();
        for (String next : map.keySet()) {
            create.setValue(next, map.get(next).doubleValue());
        }
        return create;
    }

    private DimensionValueSet getDimensionValueSet(Map<String, String> map) {
        DimensionValueSet create = DimensionValueSet.create();
        for (String next : map.keySet()) {
            create.setValue(next, map.get(next));
        }
        return create;
    }

    public void commitStat(String str, String str2, Map<String, String> map, Map<String, Double> map2) {
        try {
            AppMonitor.Stat.commit(str, str2, getDimensionValueSet(map), getMeasureValueSet(map2));
        } catch (Throwable unused) {
        }
    }
}
