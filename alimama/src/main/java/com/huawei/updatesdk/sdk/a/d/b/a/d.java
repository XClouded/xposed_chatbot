package com.huawei.updatesdk.sdk.a.d.b.a;

import com.huawei.updatesdk.sdk.a.c.a.a.a;
import java.lang.reflect.InvocationTargetException;

public final class d implements a {
    private static d a;
    private static final Object b = new Object();

    private d() {
    }

    public static d b() {
        d dVar;
        synchronized (b) {
            if (a == null) {
                a = new d();
            }
            dVar = a;
        }
        return dVar;
    }

    private static Object c() {
        StringBuilder sb;
        String str;
        String str2;
        try {
            Class<?> cls = Class.forName("com.mediatek.telephony.TelephonyManagerEx");
            return cls.getDeclaredMethod("getDefault", new Class[0]).invoke(cls, new Object[0]);
        } catch (ClassNotFoundException e) {
            str = "mutiCardMTKImpl";
            sb = new StringBuilder();
            sb.append(" getDefaultTelephonyManagerEx wrong ");
            str2 = e.toString();
            sb.append(str2);
            a.d(str, sb.toString());
            return null;
        } catch (SecurityException e2) {
            str = "mutiCardMTKImpl";
            sb = new StringBuilder();
            sb.append(" getDefaultTelephonyManagerEx wrong ");
            str2 = e2.toString();
            sb.append(str2);
            a.d(str, sb.toString());
            return null;
        } catch (NoSuchMethodException e3) {
            str = "mutiCardMTKImpl";
            sb = new StringBuilder();
            sb.append(" getDefaultTelephonyManagerEx wrong ");
            str2 = e3.toString();
            sb.append(str2);
            a.d(str, sb.toString());
            return null;
        } catch (IllegalAccessException e4) {
            str = "mutiCardMTKImpl";
            sb = new StringBuilder();
            sb.append(" getDefaultTelephonyManagerEx wrong ");
            str2 = e4.toString();
            sb.append(str2);
            a.d(str, sb.toString());
            return null;
        } catch (IllegalArgumentException e5) {
            str = "mutiCardMTKImpl";
            sb = new StringBuilder();
            sb.append(" getDefaultTelephonyManagerEx wrong ");
            str2 = e5.toString();
            sb.append(str2);
            a.d(str, sb.toString());
            return null;
        } catch (InvocationTargetException e6) {
            str = "mutiCardMTKImpl";
            sb = new StringBuilder();
            sb.append(" getDefaultTelephonyManagerEx wrong ");
            str2 = e6.toString();
            sb.append(str2);
            a.d(str, sb.toString());
            return null;
        } catch (Exception e7) {
            str = "mutiCardMTKImpl";
            sb = new StringBuilder();
            sb.append(" getDefaultTelephonyManagerEx wrong ");
            str2 = e7.toString();
            sb.append(str2);
            a.d(str, sb.toString());
            return null;
        }
    }

    public int a() {
        StringBuilder sb;
        String str;
        String str2;
        Object c = c();
        if (c == null) {
            return 0;
        }
        try {
            return ((Integer) c.getClass().getMethod("getPreferredDataSubscription", new Class[0]).invoke(c, new Object[0])).intValue();
        } catch (NoSuchMethodException e) {
            str = "mutiCardMTKImpl";
            sb = new StringBuilder();
            sb.append(" getPreferredDataSubscription wrong ");
            str2 = e.toString();
            sb.append(str2);
            a.d(str, sb.toString());
            return -1;
        } catch (InvocationTargetException e2) {
            str = "mutiCardMTKImpl";
            sb = new StringBuilder();
            sb.append(" getPreferredDataSubscription wrong ");
            str2 = e2.toString();
            sb.append(str2);
            a.d(str, sb.toString());
            return -1;
        } catch (IllegalAccessException e3) {
            str = "mutiCardMTKImpl";
            sb = new StringBuilder();
            sb.append(" getPreferredDataSubscription wrong ");
            str2 = e3.toString();
            sb.append(str2);
            a.d(str, sb.toString());
            return -1;
        }
    }
}
