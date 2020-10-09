package com.uc.sandboxExport.helper;

import java.lang.reflect.Method;

/* compiled from: U4Source */
public final class b {
    private static Class<?> a;
    private static Method b;
    private static Method c;
    private static Method d;
    private static Method e;
    private static Method f;
    private static Method g;

    static {
        try {
            Class<?> cls = Class.forName("android.os.SystemProperties");
            a = cls;
            b = cls.getMethod("get", new Class[]{String.class});
            c = a.getMethod("get", new Class[]{String.class, String.class});
            d = a.getMethod("getInt", new Class[]{String.class, Integer.TYPE});
            e = a.getMethod("getLong", new Class[]{String.class, Long.TYPE});
            f = a.getMethod("getBoolean", new Class[]{String.class, Boolean.TYPE});
            g = a.getMethod("set", new Class[]{String.class, String.class});
        } catch (Throwable th) {
            th.printStackTrace();
        }
    }

    public static boolean a(String str) {
        if (!(a == null || f == null)) {
            try {
                return ((Boolean) f.invoke((Object) null, new Object[]{str, false})).booleanValue();
            } catch (Throwable th) {
                th.printStackTrace();
            }
        }
        return false;
    }
}
