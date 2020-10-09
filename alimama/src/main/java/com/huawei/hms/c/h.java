package com.huawei.hms.c;

import android.content.Context;

/* compiled from: ResourceLoaderUtil */
public abstract class h {
    private static Context a;
    private static String b;

    public static Context a() {
        return a;
    }

    public static void a(Context context) {
        a = context;
        b = context.getPackageName();
    }

    public static int a(String str) {
        return a.getResources().getIdentifier(str, "layout", b);
    }

    public static int b(String str) {
        return a.getResources().getIdentifier(str, "id", b);
    }

    public static int c(String str) {
        return a.getResources().getIdentifier(str, "string", b);
    }

    public static String d(String str) {
        String string = a.getResources().getString(c(str));
        return string == null ? "" : string;
    }

    public static String a(String str, Object... objArr) {
        String string = a.getResources().getString(c(str), objArr);
        return string == null ? "" : string;
    }
}
