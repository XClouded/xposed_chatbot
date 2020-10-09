package com.taobao.zcachecorewrapper.log;

import android.util.Log;

public class ZCLog {
    private static final String TAG = "ZCacheCW";
    private static IZCLog sLogImpl;

    public static void setLogImpl(IZCLog iZCLog) {
        sLogImpl = iZCLog;
    }

    public static void d(String str) {
        d("", str);
    }

    public static void d(String str, String str2) {
        if (sLogImpl == null || !sLogImpl.isLogLevelEnabled(2)) {
            Log.d(TAG, str2);
            return;
        }
        IZCLog iZCLog = sLogImpl;
        iZCLog.d(TAG, str + str2);
    }

    public static void i(String str) {
        i("", str);
    }

    public static void i(String str, String str2) {
        if (sLogImpl == null || !sLogImpl.isLogLevelEnabled(3)) {
            Log.i(TAG, str2);
            return;
        }
        IZCLog iZCLog = sLogImpl;
        iZCLog.i(TAG, str + str2);
    }

    public static void v(String str) {
        v("", str);
    }

    public static void v(String str, String str2) {
        if (sLogImpl == null || !sLogImpl.isLogLevelEnabled(1)) {
            Log.v(TAG, str2);
            return;
        }
        IZCLog iZCLog = sLogImpl;
        iZCLog.v(TAG, str + str2);
    }

    public static void w(String str) {
        w("", str);
    }

    public static void w(String str, String str2) {
        if (sLogImpl == null || !sLogImpl.isLogLevelEnabled(4)) {
            Log.w(TAG, str2);
            return;
        }
        IZCLog iZCLog = sLogImpl;
        iZCLog.w(TAG, str + str2);
    }

    public static void e(String str) {
        e("", str);
    }

    public static void e(String str, String str2) {
        if (sLogImpl == null || !sLogImpl.isLogLevelEnabled(5)) {
            Log.e(TAG, str2);
            return;
        }
        IZCLog iZCLog = sLogImpl;
        iZCLog.e(TAG, str + str2);
    }
}
