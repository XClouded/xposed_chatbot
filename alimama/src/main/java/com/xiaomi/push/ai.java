package com.xiaomi.push;

import android.content.Context;
import android.content.SharedPreferences;
import android.util.SparseArray;
import java.util.concurrent.ScheduledFuture;
import java.util.concurrent.ScheduledThreadPoolExecutor;
import java.util.concurrent.TimeUnit;

public class ai {
    private static volatile ai a;

    /* renamed from: a  reason: collision with other field name */
    private SharedPreferences f111a;

    /* renamed from: a  reason: collision with other field name */
    private SparseArray<ScheduledFuture> f112a = new SparseArray<>();

    /* renamed from: a  reason: collision with other field name */
    private Object f113a = new Object();

    /* renamed from: a  reason: collision with other field name */
    private ScheduledThreadPoolExecutor f114a = new ScheduledThreadPoolExecutor(1);

    public static abstract class a implements Runnable {
        public abstract int a();
    }

    private static class b implements Runnable {
        a a;

        public b(a aVar) {
            this.a = aVar;
        }

        /* access modifiers changed from: package-private */
        public void a() {
        }

        /* access modifiers changed from: package-private */
        public void b() {
        }

        public void run() {
            a();
            this.a.run();
            b();
        }
    }

    private ai(Context context) {
        this.f111a = context.getSharedPreferences("mipush_extra", 0);
    }

    public static ai a(Context context) {
        if (a == null) {
            synchronized (ai.class) {
                if (a == null) {
                    a = new ai(context);
                }
            }
        }
        return a;
    }

    private static String a(int i) {
        return "last_job_time" + i;
    }

    private ScheduledFuture a(a aVar) {
        ScheduledFuture scheduledFuture;
        synchronized (this.f113a) {
            scheduledFuture = this.f112a.get(aVar.a());
        }
        return scheduledFuture;
    }

    public void a(Runnable runnable) {
        a(runnable, 0);
    }

    public void a(Runnable runnable, int i) {
        this.f114a.schedule(runnable, (long) i, TimeUnit.SECONDS);
    }

    /* renamed from: a  reason: collision with other method in class */
    public boolean m87a(int i) {
        synchronized (this.f113a) {
            ScheduledFuture scheduledFuture = this.f112a.get(i);
            if (scheduledFuture == null) {
                return false;
            }
            this.f112a.remove(i);
            return scheduledFuture.cancel(false);
        }
    }

    public boolean a(a aVar, int i) {
        return a(aVar, i, 0);
    }

    public boolean a(a aVar, int i, int i2) {
        if (aVar == null || a(aVar) != null) {
            return false;
        }
        String a2 = a(aVar.a());
        aj ajVar = new aj(this, aVar, a2);
        long abs = Math.abs(System.currentTimeMillis() - this.f111a.getLong(a2, 0)) / 1000;
        if (abs < ((long) (i - i2))) {
            i2 = (int) (((long) i) - abs);
        }
        ScheduledFuture<?> scheduleAtFixedRate = this.f114a.scheduleAtFixedRate(ajVar, (long) i2, (long) i, TimeUnit.SECONDS);
        synchronized (this.f113a) {
            this.f112a.put(aVar.a(), scheduleAtFixedRate);
        }
        return true;
    }

    public boolean b(a aVar, int i) {
        if (aVar == null || a(aVar) != null) {
            return false;
        }
        ScheduledFuture<?> schedule = this.f114a.schedule(new ak(this, aVar), (long) i, TimeUnit.SECONDS);
        synchronized (this.f113a) {
            this.f112a.put(aVar.a(), schedule);
        }
        return true;
    }
}
