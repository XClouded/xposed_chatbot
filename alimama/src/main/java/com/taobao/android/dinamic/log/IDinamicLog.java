package com.taobao.android.dinamic.log;

public interface IDinamicLog {
    void logd(String str, String str2);

    void loge(String str, String str2);

    void loge(String str, String str2, Throwable th);

    void logi(String str, String str2);

    void logv(String str, String str2);

    void logw(String str, String str2);

    void logw(String str, String str2, Throwable th);
}
