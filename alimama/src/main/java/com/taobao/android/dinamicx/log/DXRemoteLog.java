package com.taobao.android.dinamicx.log;

import android.util.Log;

public class DXRemoteLog {
    public static IDXRemoteDebugLog iDinamicRemoteDebugLog;

    public static void setDinamicRemoteDebugLog(IDXRemoteDebugLog iDXRemoteDebugLog) {
        iDinamicRemoteDebugLog = iDXRemoteDebugLog;
    }

    public static void remoteLogi(String str, String str2, String str3) {
        if (iDinamicRemoteDebugLog != null) {
            try {
                iDinamicRemoteDebugLog.logi(str, str2, str3);
            } catch (Throwable unused) {
                Log.i(str2, str3);
            }
        } else {
            Log.i(str2, str3);
        }
    }

    public static void remoteLogi(String str) {
        remoteLogi("DinamicX", "DinamicX", str);
    }

    public static void remoteLoge(String str, String str2, String str3) {
        if (iDinamicRemoteDebugLog != null) {
            try {
                iDinamicRemoteDebugLog.loge(str, str2, str3);
            } catch (Throwable unused) {
                Log.i(str2, str3);
            }
        } else {
            Log.i(str2, str3);
        }
    }

    public static void remoteLoge(String str) {
        remoteLoge("DinamicX", "DinamicX", str);
    }
}
