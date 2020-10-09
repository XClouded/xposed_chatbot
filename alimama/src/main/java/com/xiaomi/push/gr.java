package com.xiaomi.push;

import android.content.ContentValues;
import android.content.Context;
import android.database.sqlite.SQLiteDatabase;
import android.database.sqlite.SQLiteException;
import android.text.TextUtils;
import com.taobao.weex.analyzer.Config;
import com.xiaomi.channel.commonutils.logger.b;
import com.xiaomi.mipush.sdk.Constants;
import com.xiaomi.push.al;
import java.io.UnsupportedEncodingException;
import java.util.ArrayList;
import java.util.Collections;
import java.util.List;

public class gr {
    private static volatile int a = -1;

    /* renamed from: a  reason: collision with other field name */
    private static long f416a = System.currentTimeMillis();

    /* renamed from: a  reason: collision with other field name */
    private static al f417a = new al(true);

    /* renamed from: a  reason: collision with other field name */
    private static com.xiaomi.push.providers.a f418a = null;

    /* renamed from: a  reason: collision with other field name */
    private static final Object f419a = new Object();

    /* renamed from: a  reason: collision with other field name */
    private static String f420a = "";

    /* renamed from: a  reason: collision with other field name */
    private static List<a> f421a = Collections.synchronizedList(new ArrayList());

    static class a {
        public int a = -1;

        /* renamed from: a  reason: collision with other field name */
        public long f422a = 0;

        /* renamed from: a  reason: collision with other field name */
        public String f423a = "";
        public int b = -1;

        /* renamed from: b  reason: collision with other field name */
        public long f424b = 0;

        /* renamed from: b  reason: collision with other field name */
        public String f425b = "";

        public a(String str, long j, int i, int i2, String str2, long j2) {
            this.f423a = str;
            this.f422a = j;
            this.a = i;
            this.b = i2;
            this.f425b = str2;
            this.f424b = j2;
        }

        public boolean a(a aVar) {
            return TextUtils.equals(aVar.f423a, this.f423a) && TextUtils.equals(aVar.f425b, this.f425b) && aVar.a == this.a && aVar.b == this.b && Math.abs(aVar.f422a - this.f422a) <= 5000;
        }
    }

    public static int a(Context context) {
        if (a == -1) {
            a = b(context);
        }
        return a;
    }

    public static int a(String str) {
        try {
            return str.getBytes("UTF-8").length;
        } catch (UnsupportedEncodingException unused) {
            return str.getBytes().length;
        }
    }

    private static long a(int i, long j, boolean z, long j2, boolean z2) {
        if (z && z2) {
            long j3 = f416a;
            f416a = j2;
            if (j2 - j3 > 30000 && j > 1024) {
                return j * 2;
            }
        }
        return (j * ((long) (i == 0 ? 13 : 11))) / 10;
    }

    /* renamed from: a  reason: collision with other method in class */
    private static com.xiaomi.push.providers.a m347a(Context context) {
        if (f418a != null) {
            return f418a;
        }
        f418a = new com.xiaomi.push.providers.a(context);
        return f418a;
    }

    /* renamed from: a  reason: collision with other method in class */
    private static synchronized String m348a(Context context) {
        synchronized (gr.class) {
            if (TextUtils.isEmpty(f420a)) {
                return "";
            }
            String str = f420a;
            return str;
        }
    }

    /* renamed from: a  reason: collision with other method in class */
    public static void m349a(Context context) {
        a = b(context);
    }

    private static void a(Context context, String str, long j, boolean z, long j2) {
        int a2;
        boolean isEmpty;
        if (context != null && !TextUtils.isEmpty(str) && "com.xiaomi.xmsf".equals(context.getPackageName())) {
            String str2 = str;
            if (!"com.xiaomi.xmsf".equals(str) && -1 != (a2 = a(context))) {
                synchronized (f419a) {
                    isEmpty = f421a.isEmpty();
                    a(new a(str, j2, a2, z ? 1 : 0, a2 == 0 ? a(context) : "", j));
                }
                if (isEmpty) {
                    f417a.a((al.b) new gs(context), 5000);
                }
            }
        }
    }

    public static void a(Context context, String str, long j, boolean z, boolean z2, long j2) {
        a(context, str, a(a(context), j, z, j2, z2), z, j2);
    }

    private static void a(a aVar) {
        for (a next : f421a) {
            if (next.a(aVar)) {
                next.f424b += aVar.f424b;
                return;
            }
        }
        f421a.add(aVar);
    }

    /* renamed from: a  reason: collision with other method in class */
    public static synchronized void m350a(String str) {
        synchronized (gr.class) {
            if (!l.d() && !TextUtils.isEmpty(str)) {
                f420a = str;
            }
        }
    }

    /* JADX WARNING: No exception handlers in catch block: Catch:{  } */
    /* Code decompiled incorrectly, please refer to instructions dump. */
    private static int b(android.content.Context r2) {
        /*
            r0 = -1
            java.lang.String r1 = "connectivity"
            java.lang.Object r2 = r2.getSystemService(r1)     // Catch:{ Exception -> 0x0018 }
            android.net.ConnectivityManager r2 = (android.net.ConnectivityManager) r2     // Catch:{ Exception -> 0x0018 }
            if (r2 != 0) goto L_0x000c
            return r0
        L_0x000c:
            android.net.NetworkInfo r2 = r2.getActiveNetworkInfo()     // Catch:{  }
            if (r2 != 0) goto L_0x0013
            return r0
        L_0x0013:
            int r2 = r2.getType()
            return r2
        L_0x0018:
            return r0
        */
        throw new UnsupportedOperationException("Method not decompiled: com.xiaomi.push.gr.b(android.content.Context):int");
    }

    /* access modifiers changed from: private */
    public static void b(Context context, List<a> list) {
        try {
            synchronized (com.xiaomi.push.providers.a.f800a) {
                SQLiteDatabase writableDatabase = a(context).getWritableDatabase();
                writableDatabase.beginTransaction();
                try {
                    for (a next : list) {
                        ContentValues contentValues = new ContentValues();
                        contentValues.put(Constants.PACKAGE_NAME, next.f423a);
                        contentValues.put("message_ts", Long.valueOf(next.f422a));
                        contentValues.put("network_type", Integer.valueOf(next.a));
                        contentValues.put("bytes", Long.valueOf(next.f424b));
                        contentValues.put("rcv", Integer.valueOf(next.b));
                        contentValues.put("imsi", next.f425b);
                        writableDatabase.insert(Config.TYPE_TRAFFIC, (String) null, contentValues);
                    }
                    writableDatabase.setTransactionSuccessful();
                } finally {
                    writableDatabase.endTransaction();
                }
            }
        } catch (SQLiteException e) {
            b.a((Throwable) e);
        }
    }
}
