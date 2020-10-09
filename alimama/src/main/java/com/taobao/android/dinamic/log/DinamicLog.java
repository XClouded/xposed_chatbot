package com.taobao.android.dinamic.log;

import android.util.Log;
import com.taobao.android.dinamic.DRegisterCenter;
import com.taobao.android.dinamic.Dinamic;
import com.taobao.android.dinamic.dinamic.DinamicPerformMonitor;

public class DinamicLog {
    public static IDinamicRemoteDebugLog iDinamicRemoteDebugLog = null;
    public static boolean isOpen = false;

    public static void setDinamicRemoteDebugLog(IDinamicRemoteDebugLog iDinamicRemoteDebugLog2) {
        iDinamicRemoteDebugLog = iDinamicRemoteDebugLog2;
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

    public static void v(String str, String... strArr) {
        Log.v(str, joinString(strArr));
    }

    public static void d(String str, String... strArr) {
        Log.d(str, joinString(strArr));
    }

    public static void i(String str, String... strArr) {
        Log.i(str, joinString(strArr));
    }

    public static void w(String str, String... strArr) {
        Log.w(str, joinString(strArr));
    }

    public static void w(String str, Throwable th, String... strArr) {
        Log.w(str, joinString(strArr), th);
    }

    public static void e(String str, String... strArr) {
        Log.e(str, joinString(strArr));
    }

    public static void e(String str, Throwable th, String... strArr) {
        Log.e(str, joinString(strArr), th);
    }

    public static void e(String str, String str2, Throwable th) {
        Log.e(str, str2, th);
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
        if (isOpen) {
            d("DinamicX", str);
        }
    }

    public static void logHandleEvent(final String str, final String str2, final long j) {
        if (DRegisterCenter.shareCenter().getPerformMonitor() != null && DinamicLogThread.checkInit()) {
            DinamicLogThread.threadHandler.postTask(new Runnable() {
                public void run() {
                    if (Dinamic.isDebugable()) {
                        StringBuilder sb = new StringBuilder();
                        sb.append("handleEvent module=");
                        sb.append(str);
                        sb.append("identifier=");
                        sb.append(str2);
                        sb.append("consuming=");
                        double d = (double) j;
                        Double.isNaN(d);
                        sb.append(d / 1000000.0d);
                        DinamicLog.d(Dinamic.TAG, sb.toString());
                    }
                    DinamicPerformMonitor performMonitor = DRegisterCenter.shareCenter().getPerformMonitor();
                    String str = str;
                    String str2 = str2;
                    double d2 = (double) j;
                    Double.isNaN(d2);
                    performMonitor.trackHandleEvent(str, str2, d2 / 1000000.0d);
                }
            });
        }
    }
}
