package com.taobao.android.dinamicx.log;

import android.text.TextUtils;
import android.util.Log;
import com.taobao.android.dinamicx.DinamicXEngine;

public class DXLog {
    public static final String TAG = "DinamicX";
    public static boolean isOpen = false;

    public static void v(String str, String... strArr) {
        if (isOpen || DinamicXEngine.isDebug()) {
            Log.v(str, joinString(strArr));
        }
    }

    public static void d(String str, String... strArr) {
        if (isOpen || DinamicXEngine.isDebug()) {
            Log.d(str, joinString(strArr));
        }
    }

    public static void i(String str, String... strArr) {
        if (isOpen || DinamicXEngine.isDebug()) {
            Log.i(str, joinString(strArr));
        }
    }

    public static void w(String str, String... strArr) {
        if (isOpen || DinamicXEngine.isDebug()) {
            Log.w(str, joinString(strArr));
        }
    }

    public static void w(String str, Throwable th, String... strArr) {
        if (isOpen || DinamicXEngine.isDebug()) {
            Log.w(str, joinString(strArr), th);
        }
    }

    public static void e(String str, String... strArr) {
        if (isOpen || DinamicXEngine.isDebug()) {
            Log.e(str, joinString(strArr));
        }
    }

    public static void e(String str, Throwable th, String... strArr) {
        Log.e(str, joinString(strArr), th);
    }

    public static void e(String str, String str2, Throwable th) {
        if (isOpen || DinamicXEngine.isDebug()) {
            Log.e(str, str2, th);
        }
    }

    private static String joinString(String... strArr) {
        if (strArr == null || strArr.length == 0) {
            return "";
        }
        if (strArr.length == 1) {
            return strArr[0];
        }
        StringBuilder sb = new StringBuilder();
        for (String append : strArr) {
            sb.append(append);
        }
        return sb.toString();
    }

    public static void print(String str) {
        if (isOpen || DinamicXEngine.isDebug()) {
            d("DinamicX", str);
        }
    }

    public static void performLog(String str, String str2) {
        if (!DinamicXEngine.isDebug() && !isOpen) {
            return;
        }
        if (!TextUtils.isEmpty(str)) {
            Log.i("DinamicX_perform_" + str, str2);
            return;
        }
        Log.i("DinamicX_perform", str2);
    }
}
