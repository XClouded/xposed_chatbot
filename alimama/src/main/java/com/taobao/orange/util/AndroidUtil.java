package com.taobao.orange.util;

import android.app.ActivityManager;
import android.content.Context;
import android.net.ConnectivityManager;
import android.net.NetworkInfo;
import android.os.Process;
import android.text.TextUtils;

public class AndroidUtil {
    private static final String TAG = "AndroidUtil";
    private static final String TAOBAO_PACKAGE_NAME = "com.taobao.taobao";
    private static final String TMALL_PACKAGE_NAME = "com.tmall.wireless";
    private static String currentProcess = "";
    private static String mainProcess = "";

    public static boolean isMainProcess(Context context) {
        if (context == null) {
            return true;
        }
        try {
            if (TextUtils.isEmpty(mainProcess)) {
                mainProcess = context.getPackageManager().getPackageInfo(context.getPackageName(), 0).applicationInfo.processName;
                OLog.d(TAG, "isMainProcess", "mainProcessName", mainProcess);
            }
            if (TextUtils.isEmpty(currentProcess)) {
                currentProcess = getProcessName(context, Process.myPid());
                OLog.d(TAG, "isMainProcess", "currentProcessName", currentProcess);
            }
            if (TextUtils.isEmpty(mainProcess) || TextUtils.isEmpty(currentProcess)) {
                return true;
            }
            return mainProcess.equalsIgnoreCase(currentProcess);
        } catch (Throwable th) {
            OLog.e(TAG, "isMainProcess", th, new Object[0]);
            return true;
        }
    }

    private static String getProcessName(Context context, int i) {
        String str = "";
        for (ActivityManager.RunningAppProcessInfo next : ((ActivityManager) context.getSystemService("activity")).getRunningAppProcesses()) {
            try {
                if (next.pid == i) {
                    str = next.processName;
                }
            } catch (Exception unused) {
            }
        }
        return str;
    }

    public static void setThreadPriority() {
        try {
            Process.setThreadPriority(2);
        } catch (Throwable th) {
            OLog.e(TAG, "setThreadPriority", th, new Object[0]);
        }
    }

    public static boolean isTaobaoPackage(Context context) {
        if (context == null) {
            return false;
        }
        String packageName = context.getPackageName();
        if ("com.taobao.taobao".equals(packageName) || TMALL_PACKAGE_NAME.equals(packageName)) {
            return true;
        }
        return false;
    }

    public static boolean isNetworkConnected(Context context) {
        if (context == null) {
            return false;
        }
        try {
            NetworkInfo activeNetworkInfo = ((ConnectivityManager) context.getSystemService("connectivity")).getActiveNetworkInfo();
            if (activeNetworkInfo != null) {
                return activeNetworkInfo.isAvailable();
            }
            return false;
        } catch (Throwable unused) {
            return false;
        }
    }
}
