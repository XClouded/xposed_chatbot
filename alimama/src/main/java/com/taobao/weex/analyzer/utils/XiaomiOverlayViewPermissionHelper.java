package com.taobao.weex.analyzer.utils;

import android.app.AppOpsManager;
import android.content.Context;
import android.content.Intent;
import android.content.pm.PackageInfo;
import android.content.pm.PackageManager;
import android.content.pm.ResolveInfo;
import android.os.Binder;
import android.os.Build;
import androidx.annotation.NonNull;
import com.taobao.weex.BuildConfig;
import com.taobao.weex.utils.WXLogUtils;
import java.lang.reflect.Method;
import java.util.List;

public class XiaomiOverlayViewPermissionHelper {
    private static final String TAG = "PermissionHelper";

    public static boolean isPermissionGranted(@NonNull Context context) {
        if (!"Xiaomi".equalsIgnoreCase(Build.MANUFACTURER) || Build.VERSION.SDK_INT < 19) {
            return true;
        }
        AppOpsManager appOpsManager = (AppOpsManager) context.getSystemService("appops");
        try {
            if (((Integer) appOpsManager.getClass().getDeclaredMethod("checkOp", new Class[]{Integer.TYPE, Integer.TYPE, String.class}).invoke(appOpsManager, new Object[]{24, Integer.valueOf(Binder.getCallingUid()), context.getApplicationContext().getPackageName()})).intValue() == 0) {
                return true;
            }
            return false;
        } catch (Throwable th) {
            WXLogUtils.e("PermissionHelper", th.getMessage());
            return true;
        }
    }

    public static void requestPermission(@NonNull Context context) {
        Intent intent = new Intent("miui.intent.action.APP_PERM_EDITOR");
        if ("V5".equalsIgnoreCase(getProperty())) {
            try {
                PackageInfo packageInfo = context.getPackageManager().getPackageInfo(context.getPackageName(), 0);
                intent.setClassName("com.miui.securitycenter", "com.miui.securitycenter.permission.AppPermissionsEditor");
                intent.putExtra("extra_package_uid", packageInfo.applicationInfo.uid);
            } catch (Exception e) {
                WXLogUtils.e("PermissionHelper", e.getMessage());
                return;
            }
        } else {
            intent.setClassName("com.miui.securitycenter", "com.miui.permcenter.permissions.AppPermissionsEditorActivity");
            intent.putExtra("extra_pkgname", context.getPackageName());
        }
        if (isActivityAvailable(context, intent)) {
            context.startActivity(intent);
        } else {
            WXLogUtils.e("PermissionHelper", "request permission for xiaomi failed");
        }
    }

    private static String getProperty() {
        if (!"Xiaomi".equalsIgnoreCase(Build.MANUFACTURER)) {
            return BuildConfig.buildJavascriptFrameworkVersion;
        }
        try {
            Class<?> cls = Class.forName("android.os.SystemProperties");
            Method declaredMethod = cls.getDeclaredMethod("get", new Class[]{String.class, String.class});
            declaredMethod.setAccessible(true);
            return (String) declaredMethod.invoke(cls, new Object[]{"ro.miui.ui.version.name", null});
        } catch (Exception e) {
            WXLogUtils.e("PermissionHelper", e.getMessage());
            return BuildConfig.buildJavascriptFrameworkVersion;
        }
    }

    private static boolean isActivityAvailable(@NonNull Context context, @NonNull Intent intent) {
        List<ResolveInfo> queryIntentActivities;
        PackageManager packageManager = context.getPackageManager();
        if (packageManager == null || (queryIntentActivities = packageManager.queryIntentActivities(intent, 65536)) == null || queryIntentActivities.size() <= 0) {
            return false;
        }
        return true;
    }
}
