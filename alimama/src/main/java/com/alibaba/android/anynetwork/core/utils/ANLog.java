package com.alibaba.android.anynetwork.core.utils;

import android.util.Log;
import com.alibaba.android.anynetwork.core.common.ANConstants;

public class ANLog {
    static ILogProxy sProxy = null;
    public static String sTAG_PRE = "AnyNetwork_";

    public static void setProxy(ILogProxy iLogProxy) {
        sProxy = iLogProxy;
    }

    public static ILogProxy getProxy() {
        return sProxy;
    }

    public static void v(String str, String str2) {
        if (sProxy != null) {
            ILogProxy iLogProxy = sProxy;
            iLogProxy.v(sTAG_PRE + str, str2);
        } else if (ANConstants.DEBUG) {
            Log.v(sTAG_PRE + str, str2);
        }
    }

    public static void d(String str, String str2) {
        if (sProxy != null) {
            ILogProxy iLogProxy = sProxy;
            iLogProxy.d(sTAG_PRE + str, str2);
        } else if (ANConstants.DEBUG) {
            Log.d(sTAG_PRE + str, str2);
        }
    }

    public static void i(String str, String str2) {
        if (sProxy != null) {
            ILogProxy iLogProxy = sProxy;
            iLogProxy.i(sTAG_PRE + str, str2);
        } else if (ANConstants.DEBUG) {
            Log.i(sTAG_PRE + str, str2);
        }
    }

    public static void w(String str, String str2) {
        if (sProxy != null) {
            ILogProxy iLogProxy = sProxy;
            iLogProxy.w(sTAG_PRE + str, str2);
        } else if (ANConstants.DEBUG) {
            Log.w(sTAG_PRE + str, str2);
        }
    }

    public static void e(String str, String str2) {
        if (sProxy != null) {
            ILogProxy iLogProxy = sProxy;
            iLogProxy.e(sTAG_PRE + str, str2);
        } else if (ANConstants.DEBUG) {
            Log.e(sTAG_PRE + str, str2);
        }
    }

    public static void printThrowable(Throwable th) {
        if (sProxy != null) {
            sProxy.printThrowable(th);
        } else if (ANConstants.DEBUG) {
            th.printStackTrace();
        }
    }
}
