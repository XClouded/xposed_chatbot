package com.ali.telescope.util;

import android.app.Application;
import android.content.Context;
import android.content.pm.PackageInfo;

public class AppUtils {
    public static boolean isLargeHeapOpen(Application application) {
        return (application.getApplicationInfo().flags & 1048576) != 0;
    }

    public static PackageInfo getPackageInfo(Context context) {
        try {
            return context.getPackageManager().getPackageInfo(context.getPackageName(), 16384);
        } catch (Exception e) {
            e.printStackTrace();
            return null;
        }
    }

    public static String getVersionName(Context context) {
        return getPackageInfo(context) != null ? getPackageInfo(context).versionName : "";
    }
}
