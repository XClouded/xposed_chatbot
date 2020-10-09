package com.huawei.hms.c;

import android.app.Activity;
import android.app.ActivityManager;
import android.app.KeyguardManager;
import android.content.Context;
import android.content.ServiceConnection;
import android.content.pm.ApplicationInfo;
import android.content.pm.PackageManager;
import android.content.res.Resources;
import android.text.TextUtils;
import com.huawei.hms.a.a;
import com.huawei.hms.support.log.a;
import com.taobao.onlinemonitor.OnLineMonitor;
import com.xiaomi.mipush.sdk.Constants;
import java.lang.reflect.InvocationTargetException;
import java.util.List;
import java.util.Locale;
import java.util.regex.Pattern;

/* compiled from: Util */
public class j {
    public static boolean a() {
        String c = c();
        if (!TextUtils.isEmpty(c)) {
            return "cn".equalsIgnoreCase(c);
        }
        String d = d();
        if (!TextUtils.isEmpty(d)) {
            return d.toLowerCase(Locale.US).contains("cn");
        }
        String e = e();
        if (!TextUtils.isEmpty(e)) {
            return "cn".equalsIgnoreCase(e);
        }
        return false;
    }

    private static String c() {
        return a("ro.product.locale.region", "");
    }

    public static String a(String str, String str2) {
        try {
            Class<?> cls = Class.forName("android.os.SystemProperties");
            return (String) cls.getDeclaredMethod("get", new Class[]{String.class, String.class}).invoke(cls, new Object[]{str, str2});
        } catch (ClassCastException | ClassNotFoundException | IllegalAccessException | IllegalArgumentException | NoSuchMethodException | InvocationTargetException unused) {
            a.d("Util", "An exception occurred while reading: getSystemProperties:" + str);
            return str2;
        }
    }

    private static String d() {
        return a("ro.product.locale", "");
    }

    private static String e() {
        Locale locale = Locale.getDefault();
        if (locale != null) {
            return locale.getCountry();
        }
        return "";
    }

    public static String a(Context context) {
        Object obj;
        PackageManager packageManager = context.getPackageManager();
        if (packageManager == null) {
            a.d("Util", "In getMetaDataAppId, Failed to get 'PackageManager' instance.");
            return "";
        }
        try {
            ApplicationInfo applicationInfo = packageManager.getApplicationInfo(context.getPackageName(), 128);
            if (applicationInfo == null || applicationInfo.metaData == null || (obj = applicationInfo.metaData.get(Constants.HUAWEI_HMS_CLIENT_APPID)) == null) {
                a.d("Util", "In getMetaDataAppId, Failed to read meta data for the AppID.");
                return "";
            }
            String valueOf = String.valueOf(obj);
            return valueOf.startsWith("appid=") ? valueOf.substring("appid=".length()) : valueOf;
        } catch (PackageManager.NameNotFoundException unused) {
            a.d("Util", "In getMetaDataAppId, Failed to read meta data for the AppID.");
            return "";
        }
    }

    public static int b(Context context) {
        Object obj;
        PackageManager packageManager = context.getPackageManager();
        if (packageManager == null) {
            a.d("Util", "In getHmsVersion, Failed to get 'PackageManager' instance.");
            return 0;
        }
        try {
            ApplicationInfo applicationInfo = packageManager.getApplicationInfo(context.getPackageName(), 128);
            if (!(applicationInfo == null || applicationInfo.metaData == null || (obj = applicationInfo.metaData.get("com.huawei.hms.version")) == null)) {
                String valueOf = String.valueOf(obj);
                if (!TextUtils.isEmpty(valueOf)) {
                    return b(valueOf);
                }
            }
            a.b("Util", "In getHmsVersion, Failed to read meta data for the HMS VERSION.");
            return 0;
        } catch (PackageManager.NameNotFoundException unused) {
            a.d("Util", "In getHmsVersion, Failed to read meta data for the HMS VERSION.");
            return 0;
        }
    }

    private static boolean a(String str) {
        return Pattern.compile("(^([0-9]{1,2}.){2}[0-9]{1,2}$)|(^([0-9]{1,2}.){3}[0-9]{1,3}$)").matcher(str).find();
    }

    private static int b(String str) {
        if (!a(str)) {
            return 0;
        }
        String[] split = str.split("\\.");
        if (split.length < 3) {
            return 0;
        }
        int parseInt = (Integer.parseInt(split[0]) * 10000000) + (Integer.parseInt(split[1]) * OnLineMonitor.TASK_TYPE_FROM_BOOT) + (Integer.parseInt(split[2]) * 1000);
        return split.length == 4 ? parseInt + Integer.parseInt(split[3]) : parseInt;
    }

    public static String c(Context context) {
        PackageManager packageManager = context.getPackageManager();
        if (packageManager == null) {
            a.d("Util", "In getMetaDataCpId, Failed to get 'PackageManager' instance.");
            return "";
        }
        try {
            ApplicationInfo applicationInfo = packageManager.getApplicationInfo(context.getPackageName(), 128);
            if (!(applicationInfo == null || applicationInfo.metaData == null)) {
                String string = applicationInfo.metaData.getString("com.huawei.hms.client.cpid", "");
                if (string.startsWith("cpid=")) {
                    return string.substring("cpid=".length());
                }
            }
            a.b("Util", "In getMetaDataCpId, Failed to read meta data for the CpId.");
            return "";
        } catch (PackageManager.NameNotFoundException unused) {
            a.d("Util", "In getMetaDataCpId, Failed to read meta data for the CpId.");
            return "";
        }
    }

    public static boolean d(Context context) {
        if (context == null) {
            a.d("Util", "In getBiSetting, context is null.");
            return false;
        }
        PackageManager packageManager = context.getPackageManager();
        if (packageManager == null) {
            a.d("Util", "In getBiSetting, Failed to get 'PackageManager' instance.");
            return false;
        }
        try {
            ApplicationInfo applicationInfo = packageManager.getApplicationInfo(context.getPackageName(), 128);
            if (applicationInfo != null && applicationInfo.metaData != null) {
                return applicationInfo.metaData.getBoolean("com.huawei.hms.client.bi.setting");
            }
            a.b("Util", "In getBiSetting, Failed to read meta data bisetting.");
            return false;
        } catch (PackageManager.NameNotFoundException unused) {
            a.d("Util", "In getBiSetting, Failed to read meta data bisetting.");
            return false;
        }
    }

    public static String a(Context context, String str) {
        if (context == null) {
            a.d("Util", "In getAppName, context is null.");
            return "";
        }
        PackageManager packageManager = context.getPackageManager();
        if (packageManager == null) {
            a.d("Util", "In getAppName, Failed to get 'PackageManager' instance.");
            return "";
        }
        try {
            if (TextUtils.isEmpty(str)) {
                str = context.getPackageName();
            }
            CharSequence applicationLabel = packageManager.getApplicationLabel(packageManager.getApplicationInfo(str, 0));
            if (applicationLabel == null) {
                return "";
            }
            return applicationLabel.toString();
        } catch (PackageManager.NameNotFoundException | Resources.NotFoundException unused) {
            a.d("Util", "In getAppName, Failed to get app name.");
            return "";
        }
    }

    public static void a(Context context, ServiceConnection serviceConnection) {
        try {
            context.unbindService(serviceConnection);
        } catch (Exception e) {
            a.d("Util", "On unBindServiceException:" + e.getMessage());
        }
    }

    public static Activity a(Activity activity, Activity activity2) {
        if (activity != null && !activity.isFinishing()) {
            return activity;
        }
        if (activity2 == null || activity2.isFinishing()) {
            return null;
        }
        return activity2;
    }

    public static boolean a(Activity activity) {
        return (activity.getWindow().getAttributes().flags & 1024) == 1024;
    }

    public static boolean b() {
        a.b("Util", "is Emui :" + a.C0009a.a);
        return a.C0009a.a > 0;
    }

    public static boolean e(Context context) {
        List<ActivityManager.RunningAppProcessInfo> runningAppProcesses;
        if (context == null) {
            return true;
        }
        ActivityManager activityManager = (ActivityManager) context.getSystemService("activity");
        KeyguardManager keyguardManager = (KeyguardManager) context.getSystemService("keyguard");
        if (activityManager == null || keyguardManager == null || (runningAppProcesses = activityManager.getRunningAppProcesses()) == null) {
            return true;
        }
        for (ActivityManager.RunningAppProcessInfo next : runningAppProcesses) {
            if (TextUtils.equals(next.processName, context.getPackageName())) {
                com.huawei.hms.support.log.a.b("Util", "appProcess.importance is " + next.importance);
                boolean z = next.importance == 100;
                boolean inKeyguardRestrictedInputMode = keyguardManager.inKeyguardRestrictedInputMode();
                com.huawei.hms.support.log.a.b("Util", "isForground is " + z + "***  isLockedState is " + inKeyguardRestrictedInputMode);
                if (!z || inKeyguardRestrictedInputMode) {
                    return true;
                }
                return false;
            }
        }
        return true;
    }
}
