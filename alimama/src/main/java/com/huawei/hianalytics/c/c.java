package com.huawei.hianalytics.c;

import android.annotation.SuppressLint;
import android.annotation.TargetApi;
import android.content.Context;
import android.content.pm.ApplicationInfo;
import android.content.pm.PackageManager;
import android.os.Build;
import android.provider.Settings;
import android.telephony.TelephonyManager;
import android.text.TextUtils;
import android.util.AndroidRuntimeException;
import android.util.Pair;
import com.alibaba.motu.crashreporter.Constants;
import com.huawei.hianalytics.g.b;
import com.huawei.hianalytics.util.e;
import com.taobao.weex.BuildConfig;
import java.lang.reflect.InvocationTargetException;

public abstract class c {
    public static String a() {
        String str;
        String str2;
        String str3;
        try {
            str = (String) Class.forName("com.huawei.android.os.BuildEx").getMethod("getUDID", new Class[0]).invoke((Object) null, new Object[0]);
            try {
                b.b("HianalyticsSDK", "getUDID success");
            } catch (ClassNotFoundException unused) {
            } catch (AndroidRuntimeException unused2) {
                str3 = "HianalyticsSDK";
                str2 = "getUDID getudid failed, RuntimeException is AndroidRuntimeException";
                b.c(str3, str2);
                return str;
            } catch (NoSuchMethodException unused3) {
                str3 = "HianalyticsSDK";
                str2 = "getUDID method invoke failed : NoSuchMethodException";
                b.c(str3, str2);
                return str;
            } catch (IllegalAccessException unused4) {
                str3 = "HianalyticsSDK";
                str2 = "getUDID method invoke failed : Illegal AccessException";
                b.c(str3, str2);
                return str;
            } catch (IllegalArgumentException unused5) {
                str3 = "HianalyticsSDK";
                str2 = "getUDID method invoke failed : Illegal ArgumentException";
                b.c(str3, str2);
                return str;
            } catch (InvocationTargetException unused6) {
                str3 = "HianalyticsSDK";
                str2 = "getUDID method invoke failed : InvocationTargetException";
                b.c(str3, str2);
                return str;
            }
        } catch (ClassNotFoundException unused7) {
            str = "";
            str3 = "HianalyticsSDK";
            str2 = "getUDID method invoke failed";
            b.c(str3, str2);
            return str;
        } catch (AndroidRuntimeException unused8) {
            str = "";
            str3 = "HianalyticsSDK";
            str2 = "getUDID getudid failed, RuntimeException is AndroidRuntimeException";
            b.c(str3, str2);
            return str;
        } catch (NoSuchMethodException unused9) {
            str = "";
            str3 = "HianalyticsSDK";
            str2 = "getUDID method invoke failed : NoSuchMethodException";
            b.c(str3, str2);
            return str;
        } catch (IllegalAccessException unused10) {
            str = "";
            str3 = "HianalyticsSDK";
            str2 = "getUDID method invoke failed : Illegal AccessException";
            b.c(str3, str2);
            return str;
        } catch (IllegalArgumentException unused11) {
            str = "";
            str3 = "HianalyticsSDK";
            str2 = "getUDID method invoke failed : Illegal ArgumentException";
            b.c(str3, str2);
            return str;
        } catch (InvocationTargetException unused12) {
            str = "";
            str3 = "HianalyticsSDK";
            str2 = "getUDID method invoke failed : InvocationTargetException";
            b.c(str3, str2);
            return str;
        }
        return str;
    }

    public static String a(Context context) {
        return context == null ? "" : context.getPackageName();
    }

    public static String b() {
        return e.b("ro.build.version.emui", "");
    }

    public static String b(Context context) {
        if (context == null) {
            return "";
        }
        try {
            return context.getPackageManager().getPackageInfo(a(context), 0).versionName;
        } catch (PackageManager.NameNotFoundException unused) {
            b.c("HianalyticsSDK", "getVersion(): The package name is not correct!");
            return "";
        }
    }

    @SuppressLint({"HardwareIds"})
    public static String c(Context context) {
        if (e.a(context, "android.permission.READ_PHONE_STATE")) {
            return "";
        }
        try {
            TelephonyManager telephonyManager = (TelephonyManager) context.getSystemService("phone");
            return telephonyManager != null ? telephonyManager.getDeviceId() : "";
        } catch (SecurityException unused) {
            b.d("HianalyticsSDK", "getDeviceID Incorrect permissions!");
            return "";
        }
    }

    @SuppressLint({"HardwareIds"})
    public static String d(Context context) {
        return context == null ? "" : Settings.Secure.getString(context.getContentResolver(), "android_id");
    }

    public static String e(Context context) {
        String str = Build.SERIAL;
        return (TextUtils.isEmpty(str) || str.equalsIgnoreCase("unknown")) ? h(context) : str;
    }

    public static String f(Context context) {
        Object obj;
        try {
            ApplicationInfo applicationInfo = context.getPackageManager().getApplicationInfo(context.getPackageName(), 128);
            if (applicationInfo == null || applicationInfo.metaData == null || (obj = applicationInfo.metaData.get(Constants.CHANNEL)) == null) {
                return "Unknown";
            }
            String obj2 = obj.toString();
            if (obj2.length() > 256) {
                obj2 = "Unknown";
            }
            return obj2;
        } catch (PackageManager.NameNotFoundException unused) {
            b.c("HianalyticsSDK", "getChannel(): The packageName is not correct!");
            return "Unknown";
        }
    }

    public static Pair<String, String> g(Context context) {
        if (e.a(context, "android.permission.READ_PHONE_STATE")) {
            b.c("HianalyticsSDK", "getMccAndMnc() Pair value is empty");
            return new Pair<>("", "");
        }
        TelephonyManager telephonyManager = (TelephonyManager) context.getSystemService("phone");
        if (telephonyManager == null) {
            return new Pair<>("", "");
        }
        if (telephonyManager.getSimState() != 5) {
            return new Pair<>("", "");
        }
        String networkOperator = telephonyManager.getNetworkOperator();
        return (TextUtils.isEmpty(networkOperator) || TextUtils.equals(networkOperator, BuildConfig.buildJavascriptFrameworkVersion)) ? new Pair<>("", "") : networkOperator.length() > 3 ? new Pair<>(networkOperator.substring(0, 3), networkOperator.substring(3)) : new Pair<>("", "");
    }

    @TargetApi(26)
    private static String h(Context context) {
        b.a("HianalyticsSDK", "getSerial : is executed.");
        if (context == null || e.a(context, "android.permission.READ_PHONE_STATE")) {
            return "";
        }
        try {
            return Build.getSerial();
        } catch (SecurityException unused) {
            b.c("HianalyticsSDK", "getSerial() Incorrect permissions!");
            return "";
        }
    }
}
