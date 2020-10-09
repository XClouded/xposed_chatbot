package com.xiaomi.push;

import com.alibaba.android.umbrella.link.export.UMLLCons;

public class ab {
    private static int a;

    /* renamed from: a  reason: collision with other field name */
    public static final String f108a = (ae.f110a ? "ONEBOX" : "@SHIP.TO.2A2FE0D7@");

    /* renamed from: a  reason: collision with other field name */
    public static final boolean f109a = f108a.contains("2A2FE0D7");
    public static final boolean b = (f109a || UMLLCons.FEATURE_TYPE_DEBUG.equalsIgnoreCase(f108a));
    public static final boolean c = "LOGABLE".equalsIgnoreCase(f108a);
    public static final boolean d = f108a.contains("YY");
    public static boolean e = f108a.equalsIgnoreCase("TEST");
    public static final boolean f = "BETA".equalsIgnoreCase(f108a);
    public static final boolean g;

    static {
        int i;
        boolean z = false;
        if (f108a != null && f108a.startsWith("RC")) {
            z = true;
        }
        g = z;
        a = 1;
        if (f108a.equalsIgnoreCase("SANDBOX")) {
            i = 2;
        } else if (f108a.equalsIgnoreCase("ONEBOX")) {
            i = 3;
        } else {
            a = 1;
            return;
        }
        a = i;
    }

    public static int a() {
        return a;
    }

    public static void a(int i) {
        a = i;
    }

    /* renamed from: a  reason: collision with other method in class */
    public static boolean m86a() {
        return a == 2;
    }

    public static boolean b() {
        return a == 3;
    }
}
