package com.taobao.weex.analyzer.utils;

import android.annotation.TargetApi;
import android.content.ClipData;
import android.content.ClipboardManager;
import android.content.Context;
import android.os.Build;
import android.os.Looper;
import android.os.PowerManager;
import android.provider.Settings;
import android.text.TextUtils;
import android.widget.Toast;
import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import com.taobao.weex.WXSDKEngine;

public class SDKUtils {
    private SDKUtils() {
    }

    public static boolean isWXInitialized() {
        return WXSDKEngine.isInitialized();
    }

    public static boolean isEmulator() {
        return Build.FINGERPRINT.startsWith("generic") || Build.FINGERPRINT.startsWith("unknown") || Build.MODEL.contains("google_sdk") || Build.MODEL.contains("Emulator") || Build.MODEL.contains("Android SDK built for x86") || Build.MANUFACTURER.contains("Genymotion") || (Build.BRAND.startsWith("generic") && Build.DEVICE.startsWith("generic")) || "google_sdk".equals(Build.PRODUCT);
    }

    public static void copyToClipboard(@NonNull Context context, @Nullable String str, boolean z) {
        if (!TextUtils.isEmpty(str)) {
            ((ClipboardManager) context.getSystemService("clipboard")).setPrimaryClip(ClipData.newPlainText("copied text", str));
            if (z) {
                Toast.makeText(context, "copied to clipboard success", 0).show();
            }
        }
    }

    public static boolean isDebugMode(@NonNull Context context) {
        try {
            if ((context.getApplicationInfo().flags & 2) != 0) {
                return true;
            }
            return false;
        } catch (Exception e) {
            e.printStackTrace();
            return false;
        }
    }

    public static boolean isInteractive(@NonNull Context context) {
        return ((PowerManager) context.getSystemService("power")).isScreenOn();
    }

    public static boolean isInUiThread() {
        return Looper.getMainLooper() == Looper.myLooper();
    }

    @TargetApi(23)
    public static boolean canDrawOverlays(@NonNull Context context) {
        return Settings.canDrawOverlays(context);
    }
}
