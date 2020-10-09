package com.xiaomi.push;

import com.xiaomi.channel.commonutils.logger.b;
import com.xiaomi.push.gw;
import com.xiaomi.push.service.XMPushService;
import com.xiaomi.push.service.al;
import java.util.Hashtable;

public class ha {
    private static final int a = fb.PING_RTT.a();

    static class a {
        static Hashtable<Integer, Long> a = new Hashtable<>();
    }

    public static void a() {
        a(0, a);
    }

    public static void a(int i) {
        fc a2 = gy.a().a();
        a2.a(fb.CHANNEL_STATS_COUNTER.a());
        a2.c(i);
        gy.a().a(a2);
    }

    public static synchronized void a(int i, int i2) {
        synchronized (ha.class) {
            if (i2 < 16777215) {
                try {
                    a.a.put(Integer.valueOf((i << 24) | i2), Long.valueOf(System.currentTimeMillis()));
                } catch (Throwable th) {
                    throw th;
                }
            } else {
                b.d("stats key should less than 16777215");
            }
        }
    }

    public static void a(int i, int i2, int i3, String str, int i4) {
        fc a2 = gy.a().a();
        a2.a((byte) i);
        a2.a(i2);
        a2.b(i3);
        a2.b(str);
        a2.c(i4);
        gy.a().a(a2);
    }

    public static synchronized void a(int i, int i2, String str, int i3) {
        synchronized (ha.class) {
            long currentTimeMillis = System.currentTimeMillis();
            int i4 = (i << 24) | i2;
            if (a.a.containsKey(Integer.valueOf(i4))) {
                fc a2 = gy.a().a();
                a2.a(i2);
                a2.b((int) (currentTimeMillis - a.a.get(Integer.valueOf(i4)).longValue()));
                a2.b(str);
                if (i3 > -1) {
                    a2.c(i3);
                }
                gy.a().a(a2);
                a.a.remove(Integer.valueOf(i2));
            } else {
                b.d("stats key not found");
            }
        }
    }

    public static void a(XMPushService xMPushService, al.b bVar) {
        new gt(xMPushService, bVar).a();
    }

    public static void a(String str, int i, Exception exc) {
        fc a2 = gy.a().a();
        if (i > 0) {
            a2.a(fb.GSLB_REQUEST_SUCCESS.a());
            a2.b(str);
            a2.b(i);
            gy.a().a(a2);
            return;
        }
        try {
            gw.a a3 = gw.a(exc);
            a2.a(a3.a.a());
            a2.c(a3.f431a);
            a2.b(str);
            gy.a().a(a2);
        } catch (NullPointerException unused) {
        }
    }

    public static void a(String str, Exception exc) {
        try {
            gw.a b = gw.b(exc);
            fc a2 = gy.a().a();
            a2.a(b.a.a());
            a2.c(b.f431a);
            a2.b(str);
            gy.a().a(a2);
        } catch (NullPointerException unused) {
        }
    }

    /* renamed from: a  reason: collision with other method in class */
    public static byte[] m360a() {
        fd a2 = gy.a().a();
        if (a2 != null) {
            return iq.a(a2);
        }
        return null;
    }

    public static void b() {
        a(0, a, (String) null, -1);
    }

    public static void b(String str, Exception exc) {
        try {
            gw.a d = gw.d(exc);
            fc a2 = gy.a().a();
            a2.a(d.a.a());
            a2.c(d.f431a);
            a2.b(str);
            gy.a().a(a2);
        } catch (NullPointerException unused) {
        }
    }
}
