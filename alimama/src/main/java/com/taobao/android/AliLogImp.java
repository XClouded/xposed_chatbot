package com.taobao.android;

import com.taobao.tao.log.TLog;
import com.taobao.tlog.adapter.AdapterForTLog;

public class AliLogImp implements AliLogInterface {
    private static final AliLogImp sInstance = new AliLogImp();
    private TLog mTLog;

    private AliLogImp() {
    }

    public static AliLogImp getInstance() {
        return sInstance;
    }

    public boolean isValid() {
        return AdapterForTLog.isValid();
    }

    public void logv(String str, String... strArr) {
        AdapterForTLog.logv(str, strArr);
    }

    public void logd(String str, String... strArr) {
        AdapterForTLog.logd(str, strArr);
    }

    public void logi(String str, String... strArr) {
        AdapterForTLog.logi(str, strArr);
    }

    public void logw(String str, String... strArr) {
        AdapterForTLog.logw(str, strArr);
    }

    public void loge(String str, String... strArr) {
        AdapterForTLog.loge(str, strArr);
    }

    public void logv(String str, String str2) {
        AdapterForTLog.logv(str, str2);
    }

    public void logd(String str, String str2) {
        AdapterForTLog.logd(str, str2);
    }

    public void logi(String str, String str2) {
        AdapterForTLog.logi(str, str2);
    }

    public void logw(String str, String str2) {
        AdapterForTLog.logw(str, str2);
    }

    public void loge(String str, String str2) {
        AdapterForTLog.loge(str, str2);
    }

    public void logw(String str, String str2, Throwable th) {
        AdapterForTLog.logw(str, str2, th);
    }

    public void loge(String str, String str2, Throwable th) {
        AdapterForTLog.loge(str, str2, th);
    }

    public void traceLog(String str, String str2) {
        AdapterForTLog.traceLog(str, str2);
    }

    public String getLogLevel(String str) {
        return AdapterForTLog.getLogLevel(str);
    }

    public String getLogLevel() {
        return AdapterForTLog.getLogLevel();
    }

    public void logv(String str, String str2, String str3) {
        TLog.logv(str, str2, str3);
    }

    public void logd(String str, String str2, String str3) {
        TLog.logd(str, str2, str3);
    }

    public void logi(String str, String str2, String str3) {
        TLog.logi(str, str2, str3);
    }

    public void logw(String str, String str2, String str3) {
        TLog.logw(str, str2, str3);
    }

    public void loge(String str, String str2, String str3) {
        TLog.loge(str, str2, str3);
    }
}
