package com.taobao.android.dinamicx;

import com.taobao.android.AliMonitorInterface;
import com.taobao.android.AliMonitorServiceFetcher;
import com.taobao.android.dinamic.dinamic.DinamicAppMonitor;

public class AppMonitorImpl implements DinamicAppMonitor {
    private AliMonitorInterface monitorInterface = AliMonitorServiceFetcher.getMonitorService();

    public void counter_commit(String str, String str2, double d) {
        if (this.monitorInterface != null) {
            this.monitorInterface.counter_commit(str, str2, d);
        }
    }

    public void counter_commit(String str, String str2, String str3, double d) {
        if (this.monitorInterface != null) {
            this.monitorInterface.counter_commit(str, str2, str3, d);
        }
    }

    public void alarm_commitSuccess(String str, String str2) {
        if (this.monitorInterface != null) {
            this.monitorInterface.alarm_commitSuccess(str, str2);
        }
    }

    public void alarm_commitSuccess(String str, String str2, String str3) {
        if (this.monitorInterface != null) {
            this.monitorInterface.alarm_commitSuccess(str, str2, str3);
        }
    }

    public void alarm_commitFail(String str, String str2, String str3, String str4) {
        if (this.monitorInterface != null) {
            this.monitorInterface.alarm_commitFail(str, str2, str3, str4);
        }
    }

    public void alarm_commitFail(String str, String str2, String str3, String str4, String str5) {
        if (this.monitorInterface != null) {
            this.monitorInterface.alarm_commitFail(str, str2, str3, str4, str5);
        }
    }

    public void stat_begin(String str, String str2, String str3) {
        if (this.monitorInterface != null) {
            this.monitorInterface.stat_begin(str, str2, str3);
        }
    }

    public void stat_end(String str, String str2, String str3) {
        if (this.monitorInterface != null) {
            this.monitorInterface.stat_end(str, str2, str3);
        }
    }

    public void stat_commit(String str, String str2, double d) {
        if (this.monitorInterface != null) {
            this.monitorInterface.stat_commit(str, str2, d);
        }
    }
}
