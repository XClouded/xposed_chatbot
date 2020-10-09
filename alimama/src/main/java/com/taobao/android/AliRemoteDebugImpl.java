package com.taobao.android;

import android.content.Context;
import com.alibaba.ha.adapter.AliHaAdapter;
import com.alibaba.ha.adapter.service.tlog.TLogLevel;
import com.taobao.tlog.adapter.AdapterForTLog;
import com.taobao.tlog.adapter.TLogFileUploader;
import java.util.Map;

public class AliRemoteDebugImpl implements AliRemoteDebugInterface {
    public void OpenDebug(Boolean bool) {
        AliHaAdapter.getInstance().tLogService.OpenDebug(bool);
    }

    public void updateLogLevel(String str) {
        AliHaAdapter.getInstance().tLogService.updateLogLevel(TLogLevel.valueOf(str));
    }

    public void logv(String str, String str2, String str3) {
        AdapterForTLog.logv(str, str2, str3);
    }

    public void logd(String str, String str2, String str3) {
        AdapterForTLog.logd(str, str2, str3);
    }

    public void logi(String str, String str2, String str3) {
        AdapterForTLog.logi(str, str2, str3);
    }

    public void logw(String str, String str2, String str3) {
        AdapterForTLog.logw(str, str2, str3);
    }

    public void loge(String str, String str2, String str3) {
        AdapterForTLog.loge(str, str2, str3);
    }

    public void logw(String str, String str2, Throwable th) {
        AdapterForTLog.logw(str, str2, th);
    }

    public void loge(String str, String str2, Throwable th) {
        AdapterForTLog.loge(str, str2, th);
    }

    public void traceLog(String str, String str2) {
        AliHaAdapter.getInstance().tLogService.traceLog(str, str2);
    }

    public void changeRemoteDebugHost(String str) {
        AliHaAdapter.getInstance().tLogService.changeRemoteDebugHost(str);
    }

    public void changeRemoteDebugOssBucket(String str) {
        AliHaAdapter.getInstance().tLogService.changeRemoteDebugOssBucket(str);
    }

    public void changeAccsServiceId(String str) {
        AliHaAdapter.getInstance().tLogService.changeAccsServiceId(str);
    }

    public void changeRasPublishKey(String str) {
        AliHaAdapter.getInstance().tLogService.changeRasPublishKey(str);
    }

    public void uploadLogFile(Context context, Map<String, Object> map) {
        TLogFileUploader.uploadLogFile(context, map);
    }
}
