package com.ut.mini.internal;

import android.util.Log;
import com.alibaba.analytics.utils.ILogger;
import com.taobao.tlog.adapter.AdapterForTLog;
import java.util.HashMap;

public class LogAdapter implements ILogger {
    private boolean isNoClassDefFoundError = false;
    private HashMap<String, Integer> mTlogMap = new HashMap<>();

    public LogAdapter() {
        this.mTlogMap.put("V", 5);
        this.mTlogMap.put("D", 4);
        this.mTlogMap.put("I", 3);
        this.mTlogMap.put("W", 2);
        this.mTlogMap.put("E", 1);
        this.mTlogMap.put("L", 0);
    }

    public boolean isValid() {
        if (this.isNoClassDefFoundError) {
            return false;
        }
        try {
            return AdapterForTLog.isValid();
        } catch (Throwable unused) {
            Log.d("Analytics", "java.lang.NoClassDefFoundError: Failed resolution of: Lcom/taobao/tlog/adapter/AdapterForTLog");
            this.isNoClassDefFoundError = true;
            return false;
        }
    }

    public int getLogLevel() {
        String logLevel = AdapterForTLog.getLogLevel("Analytics");
        if (!this.mTlogMap.containsKey(logLevel)) {
            return 0;
        }
        try {
            return this.mTlogMap.get(logLevel).intValue();
        } catch (Exception unused) {
            return 0;
        }
    }

    public void logd(String str, String str2) {
        AdapterForTLog.logd(str, str2);
    }

    public void logw(String str, String str2) {
        AdapterForTLog.logw(str, str2);
    }

    public void logw(String str, String str2, Throwable th) {
        AdapterForTLog.logw(str, str2, th);
    }

    public void logi(String str, String str2) {
        AdapterForTLog.logi(str, str2);
    }

    public void loge(String str, String str2) {
        AdapterForTLog.loge(str, str2);
    }

    public void loge(String str, String str2, Throwable th) {
        AdapterForTLog.loge(str, str2, th);
    }
}
