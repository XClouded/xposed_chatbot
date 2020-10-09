package com.uc.webview.export.internal.uc.startup;

import android.os.SystemClock;
import android.util.Pair;
import java.util.concurrent.ConcurrentHashMap;

/* compiled from: U4Source */
public final class b {
    private static ConcurrentHashMap<Integer, Pair<Long, Long>> a = new ConcurrentHashMap<>();
    private static ConcurrentHashMap<Integer, String> b = new ConcurrentHashMap<>();

    static {
        a(534);
    }

    public static void a() {
        a(1);
        a(2, SystemClock.elapsedRealtime());
    }

    public static void b() {
        a(272);
    }

    public static void a(int i) {
        if (!a.containsKey(Integer.valueOf(i))) {
            a.put(Integer.valueOf(i), new Pair(Long.valueOf(System.currentTimeMillis()), Long.valueOf(SystemClock.currentThreadTimeMillis())));
        }
    }

    public static void a(int i, long j) {
        if (!a.containsKey(Integer.valueOf(i))) {
            a(i, String.valueOf(j));
        }
    }

    public static void a(int i, String str) {
        if (!b.containsKey(Integer.valueOf(i))) {
            b.put(Integer.valueOf(i), str);
        }
    }

    public static Pair<Object, Object> c() {
        return new Pair<>(a, b);
    }

    public static long d() {
        return System.currentTimeMillis();
    }
}
