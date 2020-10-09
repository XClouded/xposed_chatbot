package com.taobao.zcache.log;

import android.content.Context;
import android.util.Log;
import com.taobao.zcachecorewrapper.log.ZCLog;

public class ZLog {
    private static boolean DEBUG = false;
    private static final String TAG = "ZCache";
    private static IZLog sLogImpl;

    public static void setLogImpl(IZLog iZLog) {
        sLogImpl = iZLog;
        ZCLog.setLogImpl(iZLog);
    }

    public static void init(Context context) {
        DEBUG = (context.getApplicationInfo().flags & 2) != 0;
    }

    public static void v(String str) {
        v("", str);
    }

    public static void v(String str, String str2) {
        try {
            if (sLogImpl != null) {
                IZLog iZLog = sLogImpl;
                iZLog.v(TAG, str + str2);
                return;
            }
            Log.v(TAG, str2);
        } catch (Throwable unused) {
        }
    }

    public static void d(String str) {
        d("", str);
    }

    public static void d(String str, String str2) {
        try {
            if (sLogImpl != null) {
                IZLog iZLog = sLogImpl;
                iZLog.d(TAG, str + str2);
                return;
            }
            Log.d(TAG, str2);
        } catch (Throwable unused) {
        }
    }

    public static void i(String str) {
        i("", str);
    }

    public static void i(String str, String str2) {
        try {
            if (sLogImpl != null) {
                IZLog iZLog = sLogImpl;
                iZLog.i(TAG, str + str2);
                return;
            }
            Log.i(TAG, str2);
        } catch (Throwable unused) {
        }
    }

    public static void w(String str) {
        w("", str);
    }

    public static void w(String str, String str2) {
        try {
            if (sLogImpl != null) {
                IZLog iZLog = sLogImpl;
                iZLog.w(TAG, str + str2);
                return;
            }
            Log.w(TAG, str2);
        } catch (Throwable unused) {
        }
    }

    public static void e(String str) {
        e("", str);
    }

    public static void e(String str, String str2) {
        try {
            if (sLogImpl == null || !sLogImpl.isLogLevelEnabled(5)) {
                Log.e(TAG, str2);
                return;
            }
            IZLog iZLog = sLogImpl;
            iZLog.e(TAG, str + str2);
        } catch (Throwable unused) {
        }
    }
}
