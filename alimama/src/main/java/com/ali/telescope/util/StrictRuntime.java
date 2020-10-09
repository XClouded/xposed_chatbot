package com.ali.telescope.util;

public class StrictRuntime {
    public static boolean sStrict = false;

    public static void onHandle(String str, String str2, Throwable th) {
        TelescopeLog.e(str, str2, th);
        if (sStrict) {
            throw new RuntimeException(str2, th);
        }
    }

    public static void onHandle(Throwable th) {
        if (sStrict) {
            throw new RuntimeException(th);
        }
    }
}
