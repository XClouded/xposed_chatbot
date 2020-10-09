package com.taobao.android;

public interface AliMonitorInterface {
    void alarm_commitFail(String str, String str2, String str3, String str4);

    void alarm_commitFail(String str, String str2, String str3, String str4, String str5);

    void alarm_commitSuccess(String str, String str2);

    void alarm_commitSuccess(String str, String str2, String str3);

    void counter_commit(String str, String str2, double d);

    void counter_commit(String str, String str2, String str3, double d);

    void register(String str, String str2, AliMonitorMeasureSet aliMonitorMeasureSet);

    void register(String str, String str2, AliMonitorMeasureSet aliMonitorMeasureSet, AliMonitorDimensionSet aliMonitorDimensionSet);

    void register(String str, String str2, AliMonitorMeasureSet aliMonitorMeasureSet, AliMonitorDimensionSet aliMonitorDimensionSet, boolean z);

    void register(String str, String str2, AliMonitorMeasureSet aliMonitorMeasureSet, boolean z);

    void stat_begin(String str, String str2, String str3);

    void stat_commit(String str, String str2, double d);

    void stat_commit(String str, String str2, AliMonitorDimensionValueSet aliMonitorDimensionValueSet, double d);

    void stat_commit(String str, String str2, AliMonitorDimensionValueSet aliMonitorDimensionValueSet, AliMonitorMeasureValueSet aliMonitorMeasureValueSet);

    void stat_end(String str, String str2, String str3);

    void transaction_begin(AliMonitorTransaction aliMonitorTransaction, String str);

    void transaction_end(AliMonitorTransaction aliMonitorTransaction, String str);

    void updateMeasure(String str, String str2, String str3, double d, double d2, double d3);
}
