package com.huawei.hianalytics.log;

import android.os.Bundle;
import com.huawei.hianalytics.g.b;
import com.huawei.hianalytics.log.g.a;

public class AppLogApi {
    public static void checkUploadLog() {
        b.b("HiAnalytics/logServer", "AppLogApi.checkUploadLog() is execute.");
        a.c();
    }

    public static void collectErrorLog(String str, String str2, String str3, Bundle bundle) {
        b.b("HiAnalytics/logServer", "AppLogApi.collectErrorLog() is execute.");
        a.a(str, str2, str3, bundle);
    }

    public static void d(String str, String str2) {
        b.b("HiAnalytics/logServer", "AppLogApi.d(String tag, String msg) is execute.");
        a.a(3, "D", str, str2);
    }

    public static void e(String str, String str2) {
        b.b("HiAnalytics/logServer", "AppLogApi.e(String tag, String msg) is execute.");
        a.a(6, "E", str, str2);
    }

    public static void i(String str, String str2) {
        b.b("HiAnalytics/logServer", "AppLogApi.i(String tag, String msg) is execute.");
        a.a(4, "I", str, str2);
    }

    public static void setLogSecret(String str, String str2) {
        b.b("HiAnalytics/logServer", "AppLogApi.setLogSecret(String logServerID,String logSecret) is execute.");
        a.a(str, str2);
    }

    public static void w(String str, String str2) {
        b.b("HiAnalytics/logServer", "AppLogApi.w(String tag, String msg) is execute.");
        a.a(5, "W", str, str2);
    }
}
