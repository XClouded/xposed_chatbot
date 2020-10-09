package com.alibaba.ut.abtest.internal.util;

import android.app.ActivityManager;
import android.content.Context;
import android.content.Intent;
import android.content.pm.ResolveInfo;
import android.os.Process;
import android.text.TextUtils;
import java.io.BufferedReader;
import java.io.FileInputStream;
import java.io.InputStreamReader;
import java.util.List;

public class ProcessUtils {
    private static final String TAG = "ProcessUtils";

    public static boolean isMainProcess(Context context) {
        String curProcessName = getCurProcessName(context);
        if (TextUtils.isEmpty(curProcessName)) {
            return true;
        }
        String mainProcessName = getMainProcessName(context);
        String packageName = context.getPackageName();
        LogUtils.logD(TAG, "Current process name:" + curProcessName + ", main process name:" + mainProcessName);
        if (curProcessName.equals(mainProcessName) || curProcessName.equals(packageName)) {
            return true;
        }
        return false;
    }

    public static String getCurProcessName(Context context) {
        ActivityManager activityManager = (ActivityManager) context.getSystemService("activity");
        if (activityManager == null) {
            return getProcessNameFromProc();
        }
        List<ActivityManager.RunningAppProcessInfo> runningAppProcesses = activityManager.getRunningAppProcesses();
        if (runningAppProcesses == null || runningAppProcesses.isEmpty()) {
            return getProcessNameFromProc();
        }
        int myPid = Process.myPid();
        for (ActivityManager.RunningAppProcessInfo next : runningAppProcesses) {
            if (next.pid == myPid) {
                return next.processName;
            }
        }
        return "";
    }

    private static String getProcessNameFromProc() {
        BufferedReader bufferedReader = null;
        try {
            BufferedReader bufferedReader2 = new BufferedReader(new InputStreamReader(new FileInputStream("/proc/" + Process.myPid() + "/cmdline")));
            try {
                StringBuilder sb = new StringBuilder();
                while (true) {
                    int read = bufferedReader2.read();
                    if (read > 0) {
                        sb.append((char) read);
                    } else {
                        String sb2 = sb.toString();
                        IOUtils.closeIO(bufferedReader2);
                        return sb2;
                    }
                }
            } catch (Throwable th) {
                BufferedReader bufferedReader3 = bufferedReader2;
                th = th;
                bufferedReader = bufferedReader3;
                IOUtils.closeIO(bufferedReader);
                throw th;
            }
        } catch (Throwable th2) {
            th = th2;
            LogUtils.logD(TAG, th.getMessage());
            IOUtils.closeIO(bufferedReader);
            return "";
        }
    }

    private static String getMainProcessName(Context context) {
        String str = "";
        Intent intent = new Intent("android.intent.action.MAIN");
        String packageName = context.getPackageName();
        LogUtils.logD(TAG, "current context packageName:" + packageName);
        intent.setPackage(packageName);
        try {
            ResolveInfo resolveActivity = context.getPackageManager().resolveActivity(intent, 64);
            if (!(resolveActivity == null || resolveActivity.activityInfo == null)) {
                str = resolveActivity.activityInfo.processName;
            }
            return (TextUtils.isEmpty(str) || str.contains("system") || str.contains(":")) ? context.getPackageName() : str;
        } catch (Exception unused) {
            return "";
        }
    }
}
