package com.xiaomi.push;

import android.os.Handler;
import android.os.Looper;
import java.util.concurrent.LinkedBlockingQueue;
import java.util.concurrent.TimeUnit;

public class al {
    private int a;

    /* renamed from: a  reason: collision with other field name */
    private Handler f116a;

    /* renamed from: a  reason: collision with other field name */
    private a f117a;
    /* access modifiers changed from: private */

    /* renamed from: a  reason: collision with other field name */
    public volatile b f118a;

    /* renamed from: a  reason: collision with other field name */
    private volatile boolean f119a;
    private final boolean b;

    private class a extends Thread {

        /* renamed from: a  reason: collision with other field name */
        private final LinkedBlockingQueue<b> f120a = new LinkedBlockingQueue<>();

        public a() {
            super("PackageProcessor");
        }

        private void a(int i, b bVar) {
            al.a(al.this).sendMessage(al.a(al.this).obtainMessage(i, bVar));
        }

        public void a(b bVar) {
            try {
                this.f120a.add(bVar);
            } catch (Exception e) {
                e.printStackTrace();
            }
        }

        public void run() {
            long a2 = al.a(al.this) > 0 ? (long) al.a(al.this) : Long.MAX_VALUE;
            while (!al.a(al.this)) {
                try {
                    b poll = this.f120a.poll(a2, TimeUnit.SECONDS);
                    b unused = al.this.f118a = poll;
                    if (poll != null) {
                        a(0, poll);
                        poll.b();
                        a(1, poll);
                    } else if (al.a(al.this) > 0) {
                        al.a(al.this);
                    }
                } catch (InterruptedException e) {
                    com.xiaomi.channel.commonutils.logger.b.a((Throwable) e);
                }
            }
        }
    }

    public static abstract class b {
        public void a() {
        }

        public abstract void b();

        public void c() {
        }
    }

    public al() {
        this(false);
    }

    public al(boolean z) {
        this(z, 0);
    }

    public al(boolean z, int i) {
        this.f116a = null;
        this.f119a = false;
        this.a = 0;
        this.f116a = new am(this, Looper.getMainLooper());
        this.b = z;
        this.a = i;
    }

    private synchronized void a() {
        this.f117a = null;
        this.f119a = true;
    }

    public synchronized void a(b bVar) {
        if (this.f117a == null) {
            this.f117a = new a();
            this.f117a.setDaemon(this.b);
            this.f119a = false;
            this.f117a.start();
        }
        this.f117a.a(bVar);
    }

    public void a(b bVar, long j) {
        this.f116a.postDelayed(new an(this, bVar), j);
    }
}
