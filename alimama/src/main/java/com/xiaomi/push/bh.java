package com.xiaomi.push;

import android.content.Context;
import android.content.SharedPreferences;

public class bh {
    private static volatile bh a;

    /* renamed from: a  reason: collision with other field name */
    private Context f137a;

    private bh(Context context) {
        this.f137a = context;
    }

    public static bh a(Context context) {
        if (a == null) {
            synchronized (bh.class) {
                if (a == null) {
                    a = new bh(context);
                }
            }
        }
        return a;
    }

    /* JADX WARNING: Code restructure failed: missing block: B:10:0x0012, code lost:
        return r5;
     */
    /* JADX WARNING: Exception block dominator not found, dom blocks: [] */
    /* Code decompiled incorrectly, please refer to instructions dump. */
    public synchronized long a(java.lang.String r3, java.lang.String r4, long r5) {
        /*
            r2 = this;
            monitor-enter(r2)
            android.content.Context r0 = r2.f137a     // Catch:{ Throwable -> 0x0011, all -> 0x000e }
            r1 = 4
            android.content.SharedPreferences r3 = r0.getSharedPreferences(r3, r1)     // Catch:{ Throwable -> 0x0011, all -> 0x000e }
            long r3 = r3.getLong(r4, r5)     // Catch:{ Throwable -> 0x0011, all -> 0x000e }
            monitor-exit(r2)
            return r3
        L_0x000e:
            r3 = move-exception
            monitor-exit(r2)
            throw r3
        L_0x0011:
            monitor-exit(r2)
            return r5
        */
        throw new UnsupportedOperationException("Method not decompiled: com.xiaomi.push.bh.a(java.lang.String, java.lang.String, long):long");
    }

    /* JADX WARNING: Code restructure failed: missing block: B:10:0x0012, code lost:
        return r5;
     */
    /* JADX WARNING: Exception block dominator not found, dom blocks: [] */
    /* Code decompiled incorrectly, please refer to instructions dump. */
    public synchronized java.lang.String a(java.lang.String r3, java.lang.String r4, java.lang.String r5) {
        /*
            r2 = this;
            monitor-enter(r2)
            android.content.Context r0 = r2.f137a     // Catch:{ Throwable -> 0x0011, all -> 0x000e }
            r1 = 4
            android.content.SharedPreferences r3 = r0.getSharedPreferences(r3, r1)     // Catch:{ Throwable -> 0x0011, all -> 0x000e }
            java.lang.String r3 = r3.getString(r4, r5)     // Catch:{ Throwable -> 0x0011, all -> 0x000e }
            monitor-exit(r2)
            return r3
        L_0x000e:
            r3 = move-exception
            monitor-exit(r2)
            throw r3
        L_0x0011:
            monitor-exit(r2)
            return r5
        */
        throw new UnsupportedOperationException("Method not decompiled: com.xiaomi.push.bh.a(java.lang.String, java.lang.String, java.lang.String):java.lang.String");
    }

    /* renamed from: a  reason: collision with other method in class */
    public synchronized void m119a(String str, String str2, long j) {
        SharedPreferences.Editor edit = this.f137a.getSharedPreferences(str, 4).edit();
        edit.putLong(str2, j);
        edit.commit();
    }

    /* renamed from: a  reason: collision with other method in class */
    public synchronized void m120a(String str, String str2, String str3) {
        SharedPreferences.Editor edit = this.f137a.getSharedPreferences(str, 4).edit();
        edit.putString(str2, str3);
        edit.commit();
    }
}
