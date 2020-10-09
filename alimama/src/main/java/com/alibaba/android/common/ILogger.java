package com.alibaba.android.common;

public interface ILogger {
    void logd(String str, String str2);

    void loge(String str, String str2);

    void loge(String str, String str2, Exception exc);

    void logi(String str, String str2);

    void logw(String str, String str2);
}
