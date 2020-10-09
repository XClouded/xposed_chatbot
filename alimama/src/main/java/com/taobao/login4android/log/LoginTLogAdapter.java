package com.taobao.login4android.log;

import com.taobao.tao.log.TLog;

public class LoginTLogAdapter {
    public static void i(String str, String str2) {
        TLog.logi(str, str2);
    }

    public static void e(String str, String str2) {
        TLog.loge(str, str2);
    }

    public static void e(String str, Throwable th) {
        e(str, "", th);
    }

    public static void e(String str, String str2, Throwable th) {
        TLog.loge(str, str2, th);
    }

    public static void w(String str, String str2, Throwable th) {
        TLog.logw(str, str2, th);
    }

    public static void d(String str, String str2) {
        TLog.logd(str, str2);
    }

    public static void v(String str, String str2) {
        TLog.logd(str, str2);
    }

    public static void w(String str, String str2) {
        TLog.logw(str, str2);
    }

    public static void w(String str, Throwable th) {
        w(str, "", th);
    }
}
