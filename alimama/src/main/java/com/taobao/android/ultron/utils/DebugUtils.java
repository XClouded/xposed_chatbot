package com.taobao.android.ultron.utils;

import android.content.Context;

public class DebugUtils {
    static Boolean sDebuggable;

    public static boolean isDebuggable(Context context) {
        if (sDebuggable == null) {
            boolean z = false;
            try {
                if ((context.getApplicationInfo().flags & 2) != 0) {
                    z = true;
                }
            } catch (Exception unused) {
            }
            sDebuggable = Boolean.valueOf(z);
        }
        return sDebuggable.booleanValue();
    }
}
