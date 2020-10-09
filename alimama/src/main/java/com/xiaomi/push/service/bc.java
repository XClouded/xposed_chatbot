package com.xiaomi.push.service;

import android.content.Context;
import android.content.SharedPreferences;
import com.xiaomi.push.af;
import com.xiaomi.push.ai;
import com.xiaomi.push.as;
import com.xiaomi.push.r;
import java.util.concurrent.ConcurrentHashMap;

public final class bc implements ae {
    private static volatile bc a;

    /* renamed from: a  reason: collision with other field name */
    private long f885a;

    /* renamed from: a  reason: collision with other field name */
    Context f886a;

    /* renamed from: a  reason: collision with other field name */
    private SharedPreferences f887a;

    /* renamed from: a  reason: collision with other field name */
    private ConcurrentHashMap<String, a> f888a = new ConcurrentHashMap<>();
    /* access modifiers changed from: private */

    /* renamed from: a  reason: collision with other field name */
    public volatile boolean f889a = false;

    public static abstract class a implements Runnable {
        long a;

        /* renamed from: a  reason: collision with other field name */
        String f890a;

        a(String str, long j) {
            this.f890a = str;
            this.a = j;
        }

        /* access modifiers changed from: package-private */
        public abstract void a(bc bcVar);

        public void run() {
            if (bc.a() != null) {
                Context context = bc.a().f886a;
                if (as.c(context)) {
                    long currentTimeMillis = System.currentTimeMillis();
                    SharedPreferences a2 = bc.a(bc.a());
                    if (currentTimeMillis - a2.getLong(":ts-" + this.f890a, 0) > this.a || af.a(context)) {
                        SharedPreferences.Editor edit = bc.a(bc.a()).edit();
                        r.a(edit.putLong(":ts-" + this.f890a, System.currentTimeMillis()));
                        a(bc.a());
                    }
                }
            }
        }
    }

    private bc(Context context) {
        this.f886a = context.getApplicationContext();
        this.f887a = context.getSharedPreferences("sync", 0);
    }

    public static bc a(Context context) {
        if (a == null) {
            synchronized (bc.class) {
                if (a == null) {
                    a = new bc(context);
                }
            }
        }
        return a;
    }

    public String a(String str, String str2) {
        SharedPreferences sharedPreferences = this.f887a;
        return sharedPreferences.getString(str + ":" + str2, "");
    }

    public void a() {
        if (!this.f889a) {
            long currentTimeMillis = System.currentTimeMillis();
            if (currentTimeMillis - this.f885a >= 3600000) {
                this.f885a = currentTimeMillis;
                this.f889a = true;
                ai.a(this.f886a).a((Runnable) new bd(this), (int) (Math.random() * 10.0d));
            }
        }
    }

    public void a(a aVar) {
        if (this.f888a.putIfAbsent(aVar.f890a, aVar) == null) {
            ai.a(this.f886a).a((Runnable) aVar, ((int) (Math.random() * 30.0d)) + 10);
        }
    }

    public void a(String str, String str2, String str3) {
        SharedPreferences.Editor edit = a.f887a.edit();
        r.a(edit.putString(str + ":" + str2, str3));
    }
}
