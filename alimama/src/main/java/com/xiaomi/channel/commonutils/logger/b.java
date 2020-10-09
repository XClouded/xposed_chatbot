package com.xiaomi.channel.commonutils.logger;

import java.util.HashMap;
import java.util.concurrent.atomic.AtomicInteger;

public abstract class b {
    private static int a = 2;

    /* renamed from: a  reason: collision with other field name */
    private static LoggerInterface f0a = new a();

    /* renamed from: a  reason: collision with other field name */
    private static final Integer f1a = -1;

    /* renamed from: a  reason: collision with other field name */
    private static final HashMap<Integer, Long> f2a = new HashMap<>();

    /* renamed from: a  reason: collision with other field name */
    private static AtomicInteger f3a = new AtomicInteger(1);
    private static final HashMap<Integer, String> b = new HashMap<>();

    public static int a() {
        return a;
    }

    public static Integer a(String str) {
        if (a > 1) {
            return f1a;
        }
        Integer valueOf = Integer.valueOf(f3a.incrementAndGet());
        f2a.put(valueOf, Long.valueOf(System.currentTimeMillis()));
        b.put(valueOf, str);
        LoggerInterface loggerInterface = f0a;
        loggerInterface.log(str + " starts");
        return valueOf;
    }

    public static void a(int i) {
        if (i < 0 || i > 5) {
            a(2, "set log level as " + i);
        }
        a = i;
    }

    public static void a(int i, String str) {
        if (i >= a) {
            f0a.log(str);
        }
    }

    public static void a(int i, String str, Throwable th) {
        if (i >= a) {
            f0a.log(str, th);
        }
    }

    public static void a(int i, Throwable th) {
        if (i >= a) {
            f0a.log("", th);
        }
    }

    public static void a(LoggerInterface loggerInterface) {
        f0a = loggerInterface;
    }

    public static void a(Integer num) {
        if (a <= 1 && f2a.containsKey(num)) {
            long currentTimeMillis = System.currentTimeMillis() - f2a.remove(num).longValue();
            LoggerInterface loggerInterface = f0a;
            loggerInterface.log(b.remove(num) + " ends in " + currentTimeMillis + " ms");
        }
    }

    /* renamed from: a  reason: collision with other method in class */
    public static void m20a(String str) {
        a(2, "[Thread:" + Thread.currentThread().getId() + "] " + str);
    }

    public static void a(String str, Throwable th) {
        a(4, str, th);
    }

    public static void a(Throwable th) {
        a(4, th);
    }

    public static void b(String str) {
        a(0, str);
    }

    public static void c(String str) {
        a(1, "[Thread:" + Thread.currentThread().getId() + "] " + str);
    }

    public static void d(String str) {
        a(4, str);
    }
}
