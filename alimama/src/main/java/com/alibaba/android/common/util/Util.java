package com.alibaba.android.common.util;

import android.app.ActivityManager;
import android.content.Context;
import android.os.Process;
import android.text.TextUtils;
import java.util.Iterator;
import java.util.List;

public class Util {
    private static String processName;

    public static String getProcessName(Context context) {
        if (TextUtils.isEmpty(processName)) {
            synchronized (Util.class) {
                if (TextUtils.isEmpty(processName)) {
                    int myPid = Process.myPid();
                    ActivityManager activityManager = (ActivityManager) context.getSystemService("activity");
                    List<ActivityManager.RunningAppProcessInfo> list = null;
                    if (activityManager != null) {
                        list = activityManager.getRunningAppProcesses();
                    }
                    if (list != null) {
                        Iterator<ActivityManager.RunningAppProcessInfo> it = list.iterator();
                        while (true) {
                            if (!it.hasNext()) {
                                break;
                            }
                            ActivityManager.RunningAppProcessInfo next = it.next();
                            if (next.pid == myPid) {
                                processName = next.processName;
                                break;
                            }
                        }
                    }
                }
            }
        }
        return processName;
    }
}
