package com.huawei.updatesdk.sdk.a.d.b.a;

import com.huawei.updatesdk.sdk.a.c.a.a.a;
import java.lang.reflect.InvocationTargetException;

public class c implements a {
    private static c a;
    private static final Object b = new Object();

    public static c b() {
        c cVar;
        synchronized (b) {
            if (a == null) {
                a = new c();
            }
            cVar = a;
        }
        return cVar;
    }

    public static Object c() {
        StringBuilder sb;
        String str;
        String str2;
        try {
            Class<?> cls = Class.forName("android.telephony.MSimTelephonyManager");
            return cls.getDeclaredMethod("getDefault", new Class[0]).invoke(cls, new Object[0]);
        } catch (NoSuchMethodException e) {
            str = "MutiCardHwImpl";
            sb = new StringBuilder();
            sb.append(" getDefaultMSimTelephonyManager wrong ");
            str2 = e.toString();
            sb.append(str2);
            a.d(str, sb.toString());
            return null;
        } catch (IllegalAccessException e2) {
            str = "MutiCardHwImpl";
            sb = new StringBuilder();
            sb.append(" getDefaultMSimTelephonyManager wrong ");
            str2 = e2.toString();
            sb.append(str2);
            a.d(str, sb.toString());
            return null;
        } catch (IllegalArgumentException e3) {
            str = "MutiCardHwImpl";
            sb = new StringBuilder();
            sb.append(" getDefaultMSimTelephonyManager wrong ");
            str2 = e3.toString();
            sb.append(str2);
            a.d(str, sb.toString());
            return null;
        } catch (InvocationTargetException e4) {
            str = "MutiCardHwImpl";
            sb = new StringBuilder();
            sb.append(" getDefaultMSimTelephonyManager wrong ");
            str2 = e4.toString();
            sb.append(str2);
            a.d(str, sb.toString());
            return null;
        } catch (ClassNotFoundException e5) {
            str = "MutiCardHwImpl";
            sb = new StringBuilder();
            sb.append(" getDefaultMSimTelephonyManager wrong ");
            str2 = e5.toString();
            sb.append(str2);
            a.d(str, sb.toString());
            return null;
        } catch (Exception e6) {
            str = "MutiCardHwImpl";
            sb = new StringBuilder();
            sb.append(" getDefaultMSimTelephonyManager wrong ");
            str2 = e6.toString();
            sb.append(str2);
            a.d(str, sb.toString());
            return null;
        }
    }

    public int a() {
        StringBuilder sb;
        String str;
        String str2;
        try {
            Object c = c();
            if (c != null) {
                return ((Integer) c.getClass().getMethod("getPreferredDataSubscription", new Class[0]).invoke(c, new Object[0])).intValue();
            }
            return 0;
        } catch (NoSuchMethodException e) {
            str = "MutiCardHwImpl";
            sb = new StringBuilder();
            sb.append(" getPreferredDataSubscription wrong ");
            str2 = e.toString();
            sb.append(str2);
            a.d(str, sb.toString());
            return -1;
        } catch (IllegalAccessException e2) {
            str = "MutiCardHwImpl";
            sb = new StringBuilder();
            sb.append(" getPreferredDataSubscription wrong ");
            str2 = e2.toString();
            sb.append(str2);
            a.d(str, sb.toString());
            return -1;
        } catch (InvocationTargetException e3) {
            str = "MutiCardHwImpl";
            sb = new StringBuilder();
            sb.append(" getPreferredDataSubscription wrong ");
            str2 = e3.toString();
            sb.append(str2);
            a.d(str, sb.toString());
            return -1;
        }
    }
}
