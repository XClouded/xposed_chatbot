package com.alipay.sdk.tid;

import android.content.Context;
import android.os.Looper;
import android.text.TextUtils;
import com.alipay.sdk.sys.b;
import com.alipay.sdk.util.a;
import com.alipay.sdk.util.c;
import org.json.JSONObject;

public class TidHelper {
    public static Tid loadTID(Context context) {
        a(context);
        Tid a = a(context, b.a(context));
        if (a == null) {
            c.b("TidHelper.loadTID", "TidHelper:::loadTID > null");
        } else {
            c.b("TidHelper.loadTID", "TidHelper:::loadTID > " + a.toString());
        }
        return a;
    }

    /* JADX WARNING: Code restructure failed: missing block: B:19:0x0036, code lost:
        return r4;
     */
    /* Code decompiled incorrectly, please refer to instructions dump. */
    public static synchronized com.alipay.sdk.tid.Tid loadOrCreateTID(android.content.Context r4) {
        /*
            java.lang.Class<com.alipay.sdk.tid.TidHelper> r0 = com.alipay.sdk.tid.TidHelper.class
            monitor-enter(r0)
            java.lang.String r1 = "TidHelper"
            java.lang.String r2 = "TidHelper.loadOrCreateTID"
            com.alipay.sdk.util.c.b(r1, r2)     // Catch:{ all -> 0x0037 }
            if (r4 != 0) goto L_0x0015
            java.lang.String r1 = "tid"
            java.lang.String r2 = "tid_context_null"
            java.lang.String r3 = ""
            com.alipay.sdk.app.statistic.a.a((android.content.Context) r4, (java.lang.String) r1, (java.lang.String) r2, (java.lang.String) r3)     // Catch:{ all -> 0x0037 }
        L_0x0015:
            a(r4)     // Catch:{ all -> 0x0037 }
            com.alipay.sdk.tid.Tid r1 = loadTID(r4)     // Catch:{ all -> 0x0037 }
            boolean r2 = com.alipay.sdk.tid.Tid.isEmpty(r1)     // Catch:{ all -> 0x0037 }
            if (r2 == 0) goto L_0x0034
            android.os.Looper r2 = android.os.Looper.myLooper()     // Catch:{ all -> 0x0037 }
            android.os.Looper r3 = android.os.Looper.getMainLooper()     // Catch:{ all -> 0x0037 }
            if (r2 != r3) goto L_0x002f
            r4 = 0
            monitor-exit(r0)
            return r4
        L_0x002f:
            com.alipay.sdk.tid.Tid r4 = b(r4)     // Catch:{ Throwable -> 0x0034 }
            goto L_0x0035
        L_0x0034:
            r4 = r1
        L_0x0035:
            monitor-exit(r0)
            return r4
        L_0x0037:
            r4 = move-exception
            monitor-exit(r0)
            throw r4
        */
        throw new UnsupportedOperationException("Method not decompiled: com.alipay.sdk.tid.TidHelper.loadOrCreateTID(android.content.Context):com.alipay.sdk.tid.Tid");
    }

    public static synchronized String getTIDValue(Context context) {
        String tid;
        synchronized (TidHelper.class) {
            Tid loadOrCreateTID = loadOrCreateTID(context);
            tid = Tid.isEmpty(loadOrCreateTID) ? "" : loadOrCreateTID.getTid();
        }
        return tid;
    }

    public static boolean resetTID(Context context) throws Exception {
        Tid tid;
        c.b("TidHelper.resetTID", "resetTID");
        if (Looper.myLooper() != Looper.getMainLooper()) {
            a(context);
            clearTID(context);
            try {
                tid = b(context);
            } catch (Throwable unused) {
                tid = null;
            }
            return !Tid.isEmpty(tid);
        }
        throw new Exception("不能在主线程中调用此方法");
    }

    public static void clearTID(Context context) {
        b.a(context).g();
    }

    public static String getIMEI(Context context) {
        a(context);
        return a.a(context).b();
    }

    public static String getIMSI(Context context) {
        a(context);
        return a.a(context).a();
    }

    public static String getVirtualImei(Context context) {
        a(context);
        return com.alipay.sdk.data.c.b().c();
    }

    public static String getVirtualImsi(Context context) {
        a(context);
        return com.alipay.sdk.data.c.b().d();
    }

    private static void a(Context context) {
        if (context != null) {
            b.a().a(context, com.alipay.sdk.data.c.b());
        }
    }

    private static Tid b(Context context) throws Exception {
        try {
            com.alipay.sdk.packet.b a = new com.alipay.sdk.packet.impl.c().a(context);
            if (a == null) {
                return null;
            }
            JSONObject jSONObject = new JSONObject(a.b());
            b a2 = b.a(context);
            String optString = jSONObject.optString("tid");
            String string = jSONObject.getString(b.e);
            if (!TextUtils.isEmpty(optString) && !TextUtils.isEmpty(string)) {
                a2.a(optString, string);
            }
            return a(context, a2);
        } catch (Throwable unused) {
            return null;
        }
    }

    private static Tid a(Context context, b bVar) {
        if (bVar == null || bVar.e()) {
            return null;
        }
        return new Tid(bVar.a(), bVar.b(), bVar.i().longValue());
    }

    public static Tid loadLocalTid(Context context) {
        b a = b.a(context);
        if (a.h()) {
            return null;
        }
        return new Tid(a.a(), a.b(), a.i().longValue());
    }
}
