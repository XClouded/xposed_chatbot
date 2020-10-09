package com.huawei.updatesdk.support.e;

import android.content.Context;
import android.content.res.Resources;

public final class d {
    private static String a;
    private static Resources b;

    public static int a(Context context, String str) {
        return a(context, str, "id");
    }

    private static int a(Context context, String str, String str2) {
        if (b == null) {
            b = context.getResources();
        }
        return b.getIdentifier(str, str2, a(context));
    }

    private static String a(Context context) {
        if (a == null) {
            a = context.getPackageName();
        }
        return a;
    }

    public static int b(Context context, String str) {
        return a(context, str, "string");
    }

    public static int c(Context context, String str) {
        return a(context, str, "layout");
    }

    public static int d(Context context, String str) {
        return a(context, str, "drawable");
    }

    public static int e(Context context, String str) {
        return a(context, str, "color");
    }
}
