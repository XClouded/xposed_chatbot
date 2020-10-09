package com.xiaomi.push.service;

import android.os.SystemClock;
import java.util.concurrent.RejectedExecutionException;

public class g {
    private static long a;
    private static long b = a;
    private static long c;

    /* renamed from: a  reason: collision with other field name */
    private final a f900a;

    /* renamed from: a  reason: collision with other field name */
    private final c f901a;

    private static final class a {
        private final c a;

        a(c cVar) {
            this.a = cVar;
        }

        /* access modifiers changed from: protected */
        public void finalize() {
            try {
                synchronized (this.a) {
                    boolean unused = this.a.c = true;
                    this.a.notify();
                }
                super.finalize();
            } catch (Throwable th) {
                super.finalize();
                throw th;
            }
        }
    }

    public static abstract class b implements Runnable {
        protected int a;

        public b(int i) {
            this.a = i;
        }
    }

    private static final class c extends Thread {
        private volatile long a = 0;

        /* renamed from: a  reason: collision with other field name */
        private a f902a = new a();

        /* renamed from: a  reason: collision with other field name */
        private volatile boolean f903a = false;
        private long b = 50;

        /* renamed from: b  reason: collision with other field name */
        private boolean f904b;
        /* access modifiers changed from: private */
        public boolean c;

        private static final class a {
            private int a;

            /* renamed from: a  reason: collision with other field name */
            private d[] f905a;
            private int b;
            private int c;

            private a() {
                this.a = 256;
                this.f905a = new d[this.a];
                this.b = 0;
                this.c = 0;
            }

            /* access modifiers changed from: private */
            public int a(d dVar) {
                for (int i = 0; i < this.f905a.length; i++) {
                    if (this.f905a[i] == dVar) {
                        return i;
                    }
                }
                return -1;
            }

            private void c() {
                int i = this.b - 1;
                int i2 = (i - 1) / 2;
                while (this.f905a[i].f906a < this.f905a[i2].f906a) {
                    d dVar = this.f905a[i];
                    this.f905a[i] = this.f905a[i2];
                    this.f905a[i2] = dVar;
                    int i3 = i2;
                    i2 = (i2 - 1) / 2;
                    i = i3;
                }
            }

            private void c(int i) {
                int i2 = (i * 2) + 1;
                while (i2 < this.b && this.b > 0) {
                    int i3 = i2 + 1;
                    if (i3 < this.b && this.f905a[i3].f906a < this.f905a[i2].f906a) {
                        i2 = i3;
                    }
                    if (this.f905a[i].f906a >= this.f905a[i2].f906a) {
                        d dVar = this.f905a[i];
                        this.f905a[i] = this.f905a[i2];
                        this.f905a[i2] = dVar;
                        int i4 = i2;
                        i2 = (i2 * 2) + 1;
                        i = i4;
                    } else {
                        return;
                    }
                }
            }

            public d a() {
                return this.f905a[0];
            }

            /* renamed from: a  reason: collision with other method in class */
            public void m600a() {
                this.f905a = new d[this.a];
                this.b = 0;
            }

            public void a(int i) {
                for (int i2 = 0; i2 < this.b; i2++) {
                    if (this.f905a[i2].a == i) {
                        this.f905a[i2].a();
                    }
                }
                b();
            }

            public void a(int i, b bVar) {
                for (int i2 = 0; i2 < this.b; i2++) {
                    if (this.f905a[i2].f907a == bVar) {
                        this.f905a[i2].a();
                    }
                }
                b();
            }

            /* renamed from: a  reason: collision with other method in class */
            public void m601a(d dVar) {
                if (this.f905a.length == this.b) {
                    d[] dVarArr = new d[(this.b * 2)];
                    System.arraycopy(this.f905a, 0, dVarArr, 0, this.b);
                    this.f905a = dVarArr;
                }
                d[] dVarArr2 = this.f905a;
                int i = this.b;
                this.b = i + 1;
                dVarArr2[i] = dVar;
                c();
            }

            /* renamed from: a  reason: collision with other method in class */
            public boolean m602a() {
                return this.b == 0;
            }

            /* renamed from: a  reason: collision with other method in class */
            public boolean m603a(int i) {
                for (int i2 = 0; i2 < this.b; i2++) {
                    if (this.f905a[i2].a == i) {
                        return true;
                    }
                }
                return false;
            }

            public void b() {
                int i = 0;
                while (i < this.b) {
                    if (this.f905a[i].f909a) {
                        this.c++;
                        b(i);
                        i--;
                    }
                    i++;
                }
            }

            public void b(int i) {
                if (i >= 0 && i < this.b) {
                    d[] dVarArr = this.f905a;
                    d[] dVarArr2 = this.f905a;
                    int i2 = this.b - 1;
                    this.b = i2;
                    dVarArr[i] = dVarArr2[i2];
                    this.f905a[this.b] = null;
                    c(i);
                }
            }
        }

        c(String str, boolean z) {
            setName(str);
            setDaemon(z);
            start();
        }

        /* access modifiers changed from: private */
        public void a(d dVar) {
            this.f902a.a(dVar);
            notify();
        }

        public synchronized void a() {
            this.f904b = true;
            this.f902a.a();
            notify();
        }

        /* renamed from: a  reason: collision with other method in class */
        public boolean m599a() {
            return this.f903a && SystemClock.uptimeMillis() - this.a > 600000;
        }

        /* JADX WARNING: Can't wrap try/catch for region: R(3:6|(2:8|(3:83|10|11)(2:12|13))(2:17|26)|14) */
        /* JADX WARNING: Code restructure failed: missing block: B:57:?, code lost:
            r10.a = android.os.SystemClock.uptimeMillis();
            r10.f903a = true;
            r2.f907a.run();
            r10.f903a = false;
         */
        /* JADX WARNING: Code restructure failed: missing block: B:58:0x00a8, code lost:
            r1 = move-exception;
         */
        /* JADX WARNING: Code restructure failed: missing block: B:59:0x00a9, code lost:
            monitor-enter(r10);
         */
        /* JADX WARNING: Code restructure failed: missing block: B:61:?, code lost:
            r10.f904b = true;
         */
        /* JADX WARNING: Code restructure failed: missing block: B:63:0x00ad, code lost:
            throw r1;
         */
        /* JADX WARNING: Missing exception handler attribute for start block: B:14:0x0018 */
        /* Code decompiled incorrectly, please refer to instructions dump. */
        public void run() {
            /*
                r10 = this;
            L_0x0000:
                monitor-enter(r10)
                boolean r0 = r10.f904b     // Catch:{ all -> 0x00b7 }
                if (r0 == 0) goto L_0x0007
                monitor-exit(r10)     // Catch:{ all -> 0x00b7 }
                return
            L_0x0007:
                com.xiaomi.push.service.g$c$a r0 = r10.f902a     // Catch:{ all -> 0x00b7 }
                boolean r0 = r0.a()     // Catch:{ all -> 0x00b7 }
                if (r0 == 0) goto L_0x001a
                boolean r0 = r10.c     // Catch:{ all -> 0x00b7 }
                if (r0 == 0) goto L_0x0015
                monitor-exit(r10)     // Catch:{ all -> 0x00b7 }
                return
            L_0x0015:
                r10.wait()     // Catch:{ InterruptedException -> 0x0018 }
            L_0x0018:
                monitor-exit(r10)     // Catch:{ all -> 0x00b7 }
                goto L_0x0000
            L_0x001a:
                long r0 = com.xiaomi.push.service.g.a()     // Catch:{ all -> 0x00b7 }
                com.xiaomi.push.service.g$c$a r2 = r10.f902a     // Catch:{ all -> 0x00b7 }
                com.xiaomi.push.service.g$d r2 = r2.a()     // Catch:{ all -> 0x00b7 }
                java.lang.Object r3 = r2.f908a     // Catch:{ all -> 0x00b7 }
                monitor-enter(r3)     // Catch:{ all -> 0x00b7 }
                boolean r4 = r2.f909a     // Catch:{ all -> 0x00b4 }
                r5 = 0
                if (r4 == 0) goto L_0x0033
                com.xiaomi.push.service.g$c$a r0 = r10.f902a     // Catch:{ all -> 0x00b4 }
                r0.b(r5)     // Catch:{ all -> 0x00b4 }
                monitor-exit(r3)     // Catch:{ all -> 0x00b4 }
                goto L_0x0018
            L_0x0033:
                long r6 = r2.f906a     // Catch:{ all -> 0x00b4 }
                r4 = 0
                long r6 = r6 - r0
                monitor-exit(r3)     // Catch:{ all -> 0x00b4 }
                r0 = 50
                r3 = 0
                int r8 = (r6 > r3 ? 1 : (r6 == r3 ? 0 : -1))
                if (r8 <= 0) goto L_0x005c
                long r2 = r10.b     // Catch:{ all -> 0x00b7 }
                int r4 = (r6 > r2 ? 1 : (r6 == r2 ? 0 : -1))
                if (r4 <= 0) goto L_0x0048
                long r6 = r10.b     // Catch:{ all -> 0x00b7 }
            L_0x0048:
                long r2 = r10.b     // Catch:{ all -> 0x00b7 }
                r4 = 0
                long r2 = r2 + r0
                r10.b = r2     // Catch:{ all -> 0x00b7 }
                long r0 = r10.b     // Catch:{ all -> 0x00b7 }
                r2 = 500(0x1f4, double:2.47E-321)
                int r4 = (r0 > r2 ? 1 : (r0 == r2 ? 0 : -1))
                if (r4 <= 0) goto L_0x0058
                r10.b = r2     // Catch:{ all -> 0x00b7 }
            L_0x0058:
                r10.wait(r6)     // Catch:{ InterruptedException -> 0x0018 }
                goto L_0x0018
            L_0x005c:
                r10.b = r0     // Catch:{ all -> 0x00b7 }
                java.lang.Object r0 = r2.f908a     // Catch:{ all -> 0x00b7 }
                monitor-enter(r0)     // Catch:{ all -> 0x00b7 }
                com.xiaomi.push.service.g$c$a r1 = r10.f902a     // Catch:{ all -> 0x00b1 }
                com.xiaomi.push.service.g$d r1 = r1.a()     // Catch:{ all -> 0x00b1 }
                long r6 = r1.f906a     // Catch:{ all -> 0x00b1 }
                long r8 = r2.f906a     // Catch:{ all -> 0x00b1 }
                int r1 = (r6 > r8 ? 1 : (r6 == r8 ? 0 : -1))
                if (r1 == 0) goto L_0x0076
                com.xiaomi.push.service.g$c$a r1 = r10.f902a     // Catch:{ all -> 0x00b1 }
                int r1 = r1.a((com.xiaomi.push.service.g.d) r2)     // Catch:{ all -> 0x00b1 }
                goto L_0x0077
            L_0x0076:
                r1 = 0
            L_0x0077:
                boolean r6 = r2.f909a     // Catch:{ all -> 0x00b1 }
                if (r6 == 0) goto L_0x0088
                com.xiaomi.push.service.g$c$a r1 = r10.f902a     // Catch:{ all -> 0x00b1 }
                com.xiaomi.push.service.g$c$a r3 = r10.f902a     // Catch:{ all -> 0x00b1 }
                int r2 = r3.a((com.xiaomi.push.service.g.d) r2)     // Catch:{ all -> 0x00b1 }
                r1.b(r2)     // Catch:{ all -> 0x00b1 }
                monitor-exit(r0)     // Catch:{ all -> 0x00b1 }
                goto L_0x0018
            L_0x0088:
                long r6 = r2.f906a     // Catch:{ all -> 0x00b1 }
                r2.a(r6)     // Catch:{ all -> 0x00b1 }
                com.xiaomi.push.service.g$c$a r6 = r10.f902a     // Catch:{ all -> 0x00b1 }
                r6.b(r1)     // Catch:{ all -> 0x00b1 }
                r2.f906a = r3     // Catch:{ all -> 0x00b1 }
                monitor-exit(r0)     // Catch:{ all -> 0x00b1 }
                monitor-exit(r10)     // Catch:{ all -> 0x00b7 }
                r0 = 1
                long r3 = android.os.SystemClock.uptimeMillis()     // Catch:{ all -> 0x00a8 }
                r10.a = r3     // Catch:{ all -> 0x00a8 }
                r10.f903a = r0     // Catch:{ all -> 0x00a8 }
                com.xiaomi.push.service.g$b r1 = r2.f907a     // Catch:{ all -> 0x00a8 }
                r1.run()     // Catch:{ all -> 0x00a8 }
                r10.f903a = r5     // Catch:{ all -> 0x00a8 }
                goto L_0x0000
            L_0x00a8:
                r1 = move-exception
                monitor-enter(r10)
                r10.f904b = r0     // Catch:{ all -> 0x00ae }
                monitor-exit(r10)     // Catch:{ all -> 0x00ae }
                throw r1
            L_0x00ae:
                r0 = move-exception
                monitor-exit(r10)     // Catch:{ all -> 0x00ae }
                throw r0
            L_0x00b1:
                r1 = move-exception
                monitor-exit(r0)     // Catch:{ all -> 0x00b1 }
                throw r1     // Catch:{ all -> 0x00b7 }
            L_0x00b4:
                r0 = move-exception
                monitor-exit(r3)     // Catch:{ all -> 0x00b4 }
                throw r0     // Catch:{ all -> 0x00b7 }
            L_0x00b7:
                r0 = move-exception
                monitor-exit(r10)     // Catch:{ all -> 0x00b7 }
                throw r0
            */
            throw new UnsupportedOperationException("Method not decompiled: com.xiaomi.push.service.g.c.run():void");
        }
    }

    static class d {
        int a;

        /* renamed from: a  reason: collision with other field name */
        long f906a;

        /* renamed from: a  reason: collision with other field name */
        b f907a;

        /* renamed from: a  reason: collision with other field name */
        final Object f908a = new Object();

        /* renamed from: a  reason: collision with other field name */
        boolean f909a;
        private long b;

        d() {
        }

        /* access modifiers changed from: package-private */
        public void a(long j) {
            synchronized (this.f908a) {
                this.b = j;
            }
        }

        public boolean a() {
            boolean z;
            synchronized (this.f908a) {
                z = !this.f909a && this.f906a > 0;
                this.f909a = true;
            }
            return z;
        }
    }

    static {
        long j = 0;
        if (SystemClock.elapsedRealtime() > 0) {
            j = SystemClock.elapsedRealtime();
        }
        a = j;
    }

    public g() {
        this(false);
    }

    public g(String str) {
        this(str, false);
    }

    public g(String str, boolean z) {
        if (str != null) {
            this.f901a = new c(str, z);
            this.f900a = new a(this.f901a);
            return;
        }
        throw new NullPointerException("name == null");
    }

    public g(boolean z) {
        this("Timer-" + b(), z);
    }

    static synchronized long a() {
        long j;
        synchronized (g.class) {
            long elapsedRealtime = SystemClock.elapsedRealtime();
            if (elapsedRealtime > b) {
                a += elapsedRealtime - b;
            }
            b = elapsedRealtime;
            j = a;
        }
        return j;
    }

    private static synchronized long b() {
        long j;
        synchronized (g.class) {
            j = c;
            c = 1 + j;
        }
        return j;
    }

    private void b(b bVar, long j) {
        synchronized (this.f901a) {
            if (!c.a(this.f901a)) {
                long a2 = j + a();
                if (a2 >= 0) {
                    d dVar = new d();
                    dVar.a = bVar.a;
                    dVar.f907a = bVar;
                    dVar.f906a = a2;
                    this.f901a.a(dVar);
                } else {
                    throw new IllegalArgumentException("Illegal delay to start the TimerTask: " + a2);
                }
            } else {
                throw new IllegalStateException("Timer was canceled");
            }
        }
    }

    /* renamed from: a  reason: collision with other method in class */
    public void m595a() {
        this.f901a.a();
    }

    public void a(int i) {
        synchronized (this.f901a) {
            c.a(this.f901a).a(i);
        }
    }

    public void a(int i, b bVar) {
        synchronized (this.f901a) {
            c.a(this.f901a).a(i, bVar);
        }
    }

    public void a(b bVar) {
        if (com.xiaomi.channel.commonutils.logger.b.a() >= 1 || Thread.currentThread() == this.f901a) {
            bVar.run();
        } else {
            com.xiaomi.channel.commonutils.logger.b.d("run job outside job job thread");
            throw new RejectedExecutionException("Run job outside job thread");
        }
    }

    public void a(b bVar, long j) {
        if (j >= 0) {
            b(bVar, j);
            return;
        }
        throw new IllegalArgumentException("delay < 0: " + j);
    }

    /* renamed from: a  reason: collision with other method in class */
    public boolean m596a() {
        return this.f901a.a();
    }

    /* renamed from: a  reason: collision with other method in class */
    public boolean m597a(int i) {
        boolean a2;
        synchronized (this.f901a) {
            a2 = c.a(this.f901a).a(i);
        }
        return a2;
    }

    /* renamed from: b  reason: collision with other method in class */
    public void m598b() {
        synchronized (this.f901a) {
            c.a(this.f901a).a();
        }
    }
}
