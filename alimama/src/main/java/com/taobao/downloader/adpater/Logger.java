package com.taobao.downloader.adpater;

public interface Logger {
    void debug(String str, String str2);

    void error(String str, String str2);

    void error(String str, String str2, Throwable th);

    void log(String str, String str2);
}
