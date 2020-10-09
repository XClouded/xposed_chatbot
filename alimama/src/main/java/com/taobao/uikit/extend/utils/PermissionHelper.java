package com.taobao.uikit.extend.utils;

import android.annotation.TargetApi;
import android.app.Activity;
import android.app.AppOpsManager;
import android.content.Context;
import android.os.Binder;
import android.os.Build;
import android.util.Log;
import androidx.core.app.ActivityCompat;
import androidx.core.content.ContextCompat;

public class PermissionHelper {
    public static final String TAG = "PermissionHelper";

    public static boolean isPermissionGranted(Activity activity, String str) {
        if (Build.VERSION.SDK_INT < 23) {
            Log.v(TAG, "Permission is granted");
            return true;
        } else if (ContextCompat.checkSelfPermission(activity, str) == 0) {
            Log.v(TAG, "Permission is granted");
            return true;
        } else {
            Log.v(TAG, "Permission is revoked");
            ActivityCompat.requestPermissions(activity, new String[]{str}, 1);
            return false;
        }
    }

    @TargetApi(19)
    public static boolean isMiuiFloatWindowOpAllowed(Context context) {
        if (Build.VERSION.SDK_INT >= 19) {
            return checkOp(context, 24);
        }
        return (context.getApplicationInfo().flags & 134217728) == 134217728;
    }

    public static boolean isMezuFloatWindowOpAllowed(Context context) {
        if (Build.VERSION.SDK_INT >= 19) {
            return checkOp(context, 24);
        }
        return true;
    }

    @TargetApi(19)
    public static boolean checkOp(Context context, int i) {
        if (Build.VERSION.SDK_INT >= 19) {
            try {
                AppOpsManager appOpsManager = (AppOpsManager) context.getSystemService("appops");
                Class.forName(appOpsManager.getClass().getName());
                int intValue = ((Integer) appOpsManager.getClass().getDeclaredMethod("checkOp", new Class[]{Integer.TYPE, Integer.TYPE, String.class}).invoke(appOpsManager, new Object[]{Integer.valueOf(i), Integer.valueOf(Binder.getCallingUid()), context.getPackageName()})).intValue();
                Log.e(TAG, "0 invoke " + intValue);
                if (intValue == 0) {
                    return true;
                }
                return false;
            } catch (Exception unused) {
                Log.e(TAG, "CheckOp failed for some reason!");
            }
        } else {
            Log.e(TAG, "Below API 19 cannot invoke!");
            return false;
        }
    }
}
