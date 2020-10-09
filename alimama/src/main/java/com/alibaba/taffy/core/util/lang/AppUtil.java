package com.alibaba.taffy.core.util.lang;

import android.app.ActivityManager;
import android.content.Context;
import android.content.pm.PackageManager;
import android.os.Process;
import android.text.TextUtils;
import java.util.List;

public class AppUtil {
    private static Boolean sharedUserId;

    public static boolean isMainThread() {
        return ThreadUtil.isMainThread();
    }

    public static String getProcessName(Context context) {
        List<ActivityManager.RunningAppProcessInfo> runningAppProcesses;
        int myPid = Process.myPid();
        ActivityManager activityManager = (ActivityManager) context.getSystemService("activity");
        if (activityManager == null || (runningAppProcesses = activityManager.getRunningAppProcesses()) == null) {
            return null;
        }
        for (ActivityManager.RunningAppProcessInfo next : runningAppProcesses) {
            if (next.pid == myPid) {
                return next.processName;
            }
        }
        return null;
    }

    public static boolean isMultiPackageMode(Context context) {
        if (sharedUserId == null) {
            try {
                sharedUserId = Boolean.valueOf(!TextUtils.isEmpty(context.getPackageManager().getPackageInfo(context.getPackageName(), 0).sharedUserId));
            } catch (PackageManager.NameNotFoundException unused) {
                sharedUserId = false;
            }
        }
        return sharedUserId.booleanValue();
    }

    public static boolean isDebug(Context context) {
        try {
            if ((context.getPackageManager().getPackageInfo(context.getPackageName(), 0).applicationInfo.flags & 2) != 0) {
                return true;
            }
            return false;
        } catch (Exception unused) {
            return false;
        }
    }
}
