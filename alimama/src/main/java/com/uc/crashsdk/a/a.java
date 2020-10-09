package com.uc.crashsdk.a;

import android.util.Log;
import com.uc.crashsdk.JNIBridge;
import com.uc.crashsdk.b;
import com.uc.crashsdk.h;

/* compiled from: ProGuard */
public final class a {
    public static void a(String str) {
        if (h.K()) {
            Log.d("crashsdk", str);
        }
    }

    public static void b(String str) {
        if (h.K()) {
            Log.i("crashsdk", str);
        }
    }

    public static void c(String str) {
        if (h.K()) {
            Log.w("crashsdk", str);
        }
    }

    public static void a(String str, String str2, Throwable th) {
        if (!h.K()) {
            return;
        }
        if (th == null) {
            Log.e(str, str2);
        } else {
            Log.e(str, str2, th);
        }
    }

    public static void a(String str, String str2) {
        if (b.d) {
            JNIBridge.nativeLog(4, str, str2);
        } else {
            Log.i(str, str2);
        }
    }

    public static void b(String str, String str2) {
        if (b.d) {
            JNIBridge.nativeLog(5, str, str2);
        } else {
            Log.w(str, str2);
        }
    }

    public static void c(String str, String str2) {
        if (b.d) {
            JNIBridge.nativeLog(6, str, str2);
        } else {
            Log.e(str, str2);
        }
    }
}
