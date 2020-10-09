package com.taobao.android;

import android.os.RemoteException;
import com.alibaba.analytics.AnalyticsMgr;
import com.alibaba.mtl.appmonitor.AppMonitor;
import com.alibaba.mtl.appmonitor.Transaction;
import com.alibaba.mtl.appmonitor.model.Dimension;
import com.alibaba.mtl.appmonitor.model.DimensionSet;
import com.alibaba.mtl.appmonitor.model.DimensionValueSet;
import com.alibaba.mtl.appmonitor.model.Measure;
import com.alibaba.mtl.appmonitor.model.MeasureSet;
import com.alibaba.mtl.appmonitor.model.MeasureValue;
import com.alibaba.mtl.appmonitor.model.MeasureValueSet;
import java.util.LinkedHashMap;
import java.util.Map;

public class AliMonitorImp implements AliMonitorInterface {
    private static final AliMonitorImp sInstance = new AliMonitorImp();

    public void init() {
    }

    public void setStatisticsInterval(int i, int i2) {
    }

    private AliMonitorImp() {
    }

    public static AliMonitorImp getInstance() {
        return sInstance;
    }

    public void destroy() {
        AppMonitor.destroy();
    }

    public void triggerUpload() {
        AppMonitor.triggerUpload();
    }

    public void setSampling(int i) {
        AppMonitor.setSampling(i);
    }

    public void enableLog(boolean z) {
        AppMonitor.enableLog(z);
    }

    public void setRequestAuthInfo(boolean z, String str, String str2) {
        AppMonitor.setRequestAuthInfo(z, str, str2);
    }

    public void setChannel(String str) {
        AppMonitor.setChannel(str);
    }

    public void turnOnRealTimeDebug(Map map) {
        AppMonitor.turnOnRealTimeDebug(map);
    }

    public void turnOffRealTimeDebug() {
        AppMonitor.turnOffRealTimeDebug();
    }

    public void counter_setStatisticsInterval(int i) {
        AppMonitor.Counter.setStatisticsInterval(i);
    }

    public void counter_setSampling(int i) {
        AppMonitor.Counter.setSampling(i);
    }

    public boolean counter_checkSampled(String str, String str2) {
        return AppMonitor.Counter.checkSampled(str, str2);
    }

    public void counter_commit(String str, String str2, double d) {
        AppMonitor.Counter.commit(str, str2, d);
    }

    public void counter_commit(String str, String str2, String str3, double d) {
        AppMonitor.Counter.commit(str, str2, str3, d);
    }

    public void alarm_setStatisticsInterval(int i) {
        AppMonitor.Alarm.setStatisticsInterval(i);
    }

    public void alarm_setSampling(int i) {
        AppMonitor.Alarm.setSampling(i);
    }

    public boolean alarm_checkSampled(String str, String str2) {
        return AppMonitor.Alarm.checkSampled(str, str2);
    }

    public void alarm_commitSuccess(String str, String str2) {
        AppMonitor.Alarm.commitSuccess(str, str2);
    }

    public void alarm_commitSuccess(String str, String str2, String str3) {
        AppMonitor.Alarm.commitSuccess(str, str2, str3);
    }

    public void alarm_commitFail(String str, String str2, String str3, String str4) {
        AppMonitor.Alarm.commitFail(str, str2, str3, str4);
    }

    public void alarm_commitFail(String str, String str2, String str3, String str4, String str5) {
        AppMonitor.Alarm.commitFail(str, str2, str3, str4, str5);
    }

    public void offlinecounter_setStatisticsInterval(int i) {
        AppMonitor.OffLineCounter.setStatisticsInterval(i);
    }

    public void offlinecounter_setSampling(int i) {
        AppMonitor.OffLineCounter.setSampling(i);
    }

    public boolean offlinecounter_checkSampled(String str, String str2) {
        return AppMonitor.OffLineCounter.checkSampled(str, str2);
    }

    public void offlinecounter_commit(String str, String str2, double d) {
        AppMonitor.OffLineCounter.commit(str, str2, d);
    }

    public void setStatisticsInterval(int i) {
        AppMonitor.setStatisticsInterval(i);
    }

    public void register(String str, String str2, AliMonitorMeasureSet aliMonitorMeasureSet) {
        AppMonitor.register(str, str2, toMeasureSet(aliMonitorMeasureSet));
    }

    public void register(String str, String str2, AliMonitorMeasureSet aliMonitorMeasureSet, boolean z) {
        AppMonitor.register(str, str2, toMeasureSet(aliMonitorMeasureSet), z);
    }

    public void register(String str, String str2, AliMonitorMeasureSet aliMonitorMeasureSet, AliMonitorDimensionSet aliMonitorDimensionSet) {
        AppMonitor.register(str, str2, toMeasureSet(aliMonitorMeasureSet), toDimensionSet(aliMonitorDimensionSet));
    }

    public void register(String str, String str2, AliMonitorMeasureSet aliMonitorMeasureSet, AliMonitorDimensionSet aliMonitorDimensionSet, boolean z) {
        AppMonitor.register(str, str2, toMeasureSet(aliMonitorMeasureSet), toDimensionSet(aliMonitorDimensionSet), z);
    }

    public void stat_begin(String str, String str2, String str3) {
        AppMonitor.Stat.begin(str, str2, str3);
    }

    public void stat_end(String str, String str2, String str3) {
        AppMonitor.Stat.end(str, str2, str3);
    }

    public void stat_setStatisticsInterval(int i) {
        AppMonitor.Stat.setStatisticsInterval(i);
    }

    public void stat_setSampling(int i) {
        AppMonitor.Stat.setSampling(i);
    }

    public boolean stat_checkSampled(String str, String str2) {
        return AppMonitor.Stat.checkSampled(str, str2);
    }

    public void stat_commit(String str, String str2, double d) {
        AppMonitor.Stat.commit(str, str2, d);
    }

    public void stat_commit(String str, String str2, AliMonitorDimensionValueSet aliMonitorDimensionValueSet, double d) {
        AppMonitor.Stat.commit(str, str2, toDimensionValueSet(aliMonitorDimensionValueSet), d);
    }

    public void stat_commit(String str, String str2, AliMonitorDimensionValueSet aliMonitorDimensionValueSet, AliMonitorMeasureValueSet aliMonitorMeasureValueSet) {
        AppMonitor.Stat.commit(str, str2, toDimensionValueSet(aliMonitorDimensionValueSet), toMeasureValueSet(aliMonitorMeasureValueSet));
    }

    public void transaction_begin(AliMonitorTransaction aliMonitorTransaction, String str) {
        try {
            AnalyticsMgr.iAnalytics.transaction_begin(toTransaction(aliMonitorTransaction), str);
        } catch (RemoteException e) {
            e.printStackTrace();
        }
    }

    public void transaction_end(AliMonitorTransaction aliMonitorTransaction, String str) {
        try {
            AnalyticsMgr.iAnalytics.transaction_end(toTransaction(aliMonitorTransaction), str);
        } catch (RemoteException e) {
            e.printStackTrace();
        }
    }

    public void updateMeasure(String str, String str2, String str3, double d, double d2, double d3) {
        AppMonitor.updateMeasure(str, str2, str3, d, d2, d3);
    }

    private static MeasureSet toMeasureSet(AliMonitorMeasureSet aliMonitorMeasureSet) {
        if (aliMonitorMeasureSet == null) {
            return null;
        }
        MeasureSet create = MeasureSet.create();
        for (AliMonitorMeasure measure : aliMonitorMeasureSet.getMeasures()) {
            create.addMeasure(toMeasure(measure));
        }
        return create;
    }

    private static Measure toMeasure(AliMonitorMeasure aliMonitorMeasure) {
        if (aliMonitorMeasure == null) {
            return null;
        }
        return new Measure(aliMonitorMeasure.getName(), aliMonitorMeasure.getConstantValue(), aliMonitorMeasure.getBounds());
    }

    private static DimensionSet toDimensionSet(AliMonitorDimensionSet aliMonitorDimensionSet) {
        if (aliMonitorDimensionSet == null) {
            return null;
        }
        DimensionSet create = DimensionSet.create();
        for (AliMonitorDimension dimension : aliMonitorDimensionSet.getDimensions()) {
            create.addDimension(toDimension(dimension));
        }
        return create;
    }

    private static Dimension toDimension(AliMonitorDimension aliMonitorDimension) {
        if (aliMonitorDimension == null) {
            return null;
        }
        return new Dimension(aliMonitorDimension.getName());
    }

    private static DimensionValueSet toDimensionValueSet(AliMonitorDimensionValueSet aliMonitorDimensionValueSet) {
        if (aliMonitorDimensionValueSet == null) {
            return null;
        }
        try {
            return DimensionValueSet.fromStringMap(aliMonitorDimensionValueSet.getMap());
        } finally {
            AliMonitorBalancedPool.getInstance().offer(aliMonitorDimensionValueSet);
        }
    }

    private static MeasureValueSet toMeasureValueSet(AliMonitorMeasureValueSet aliMonitorMeasureValueSet) {
        if (aliMonitorMeasureValueSet == null) {
            return null;
        }
        try {
            MeasureValueSet create = MeasureValueSet.create();
            Map<String, AliMonitorMeasureValue> map = aliMonitorMeasureValueSet.getMap();
            LinkedHashMap linkedHashMap = new LinkedHashMap();
            for (Map.Entry next : map.entrySet()) {
                linkedHashMap.put(next.getKey(), toMeasureValue((AliMonitorMeasureValue) next.getValue()));
            }
            create.setMap(linkedHashMap);
            return create;
        } finally {
            AliMonitorBalancedPool.getInstance().offer(aliMonitorMeasureValueSet);
        }
    }

    private static MeasureValue toMeasureValue(AliMonitorMeasureValue aliMonitorMeasureValue) {
        MeasureValue measureValue;
        if (aliMonitorMeasureValue == null) {
            return null;
        }
        try {
            Double offset = aliMonitorMeasureValue.getOffset();
            if (offset == null) {
                measureValue = MeasureValue.create(aliMonitorMeasureValue.getValue());
            } else {
                measureValue = MeasureValue.create(aliMonitorMeasureValue.getValue(), offset.doubleValue());
            }
            return measureValue;
        } finally {
            AliMonitorBalancedPool.getInstance().offer(aliMonitorMeasureValue);
        }
    }

    private static Transaction toTransaction(AliMonitorTransaction aliMonitorTransaction) {
        if (aliMonitorTransaction == null) {
            return null;
        }
        return new Transaction(aliMonitorTransaction.eventId, aliMonitorTransaction.module, aliMonitorTransaction.monitorPoint, toDimensionValueSet(aliMonitorTransaction.dimensionValues));
    }
}
