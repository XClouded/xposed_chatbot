package com.taobao.weex.analyzer.utils;

import android.content.Context;
import android.content.pm.ApplicationInfo;
import android.os.Build;
import android.provider.Settings;
import android.text.TextUtils;
import androidx.annotation.NonNull;
import com.taobao.ju.track.constants.Constants;
import com.taobao.weex.BuildConfig;
import com.taobao.weex.el.parse.Operators;
import com.taobao.weex.utils.WXLogUtils;
import com.taobao.weex.utils.tools.TimeCalculator;

public class DeviceUtils {
    private static final String TAG = "DeviceUtils";

    @Deprecated
    public static String getMyIp(@NonNull Context context) {
        return Constants.PARAM_OUTER_SPM_NONE;
    }

    @NonNull
    public static String getOSType() {
        return TimeCalculator.PLATFORM_ANDROID;
    }

    private DeviceUtils() {
    }

    @NonNull
    public static String getAppName(@NonNull Context context) {
        try {
            ApplicationInfo applicationInfo = context.getApplicationInfo();
            int i = applicationInfo.labelRes;
            return i == 0 ? applicationInfo.nonLocalizedLabel.toString() : context.getString(i);
        } catch (Exception e) {
            WXLogUtils.e(TAG, e.getMessage());
            return "UNKNOWN";
        }
    }

    @NonNull
    public static String getAppVersion(@NonNull Context context) {
        try {
            return context.getPackageManager().getPackageInfo(context.getPackageName(), 0).versionName;
        } catch (Exception unused) {
            return BuildConfig.buildJavascriptFrameworkVersion;
        }
    }

    @NonNull
    public static String getPackageName(@NonNull Context context) {
        try {
            return context.getPackageName();
        } catch (Exception unused) {
            return BuildConfig.buildJavascriptFrameworkVersion;
        }
    }

    @NonNull
    public static String getOSVersion() {
        try {
            return "Android " + String.valueOf(Build.VERSION.RELEASE);
        } catch (Exception e) {
            WXLogUtils.e(TAG, e.getMessage());
            return "unknown";
        }
    }

    @NonNull
    public static String getDeviceModel() {
        String str = Build.MANUFACTURER;
        String str2 = Build.MODEL;
        if (str2.startsWith(str)) {
            try {
                return capitalize(str2);
            } catch (Exception unused) {
                return "unknown";
            }
        } else {
            try {
                return capitalize(str) + Operators.SPACE_STR + str2;
            } catch (Exception unused2) {
                return "unknown";
            }
        }
    }

    @NonNull
    public static String getDeviceId(@NonNull Context context) {
        try {
            return Settings.Secure.getString(context.getApplicationContext().getContentResolver(), "android_id");
        } catch (Exception e) {
            WXLogUtils.e(TAG, e.getMessage());
            return BuildConfig.buildJavascriptFrameworkVersion;
        }
    }

    private static String capitalize(String str) {
        if (TextUtils.isEmpty(str)) {
            return str;
        }
        char[] charArray = str.toCharArray();
        StringBuilder sb = new StringBuilder();
        boolean z = true;
        for (char c : charArray) {
            if (!z || !Character.isLetter(c)) {
                if (Character.isWhitespace(c)) {
                    z = true;
                }
                sb.append(c);
            } else {
                sb.append(Character.toUpperCase(c));
                z = false;
            }
        }
        return sb.toString();
    }
}
