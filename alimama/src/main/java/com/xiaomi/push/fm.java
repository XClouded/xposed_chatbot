package com.xiaomi.push;

import android.util.Pair;
import com.xiaomi.channel.commonutils.logger.b;
import com.xiaomi.push.service.XMPushService;
import com.xiaomi.push.service.al;
import com.xiaomi.push.service.ap;
import java.io.Reader;
import java.io.Writer;
import java.util.Collection;
import java.util.LinkedList;
import java.util.Map;
import java.util.concurrent.CancellationException;
import java.util.concurrent.ConcurrentHashMap;
import java.util.concurrent.CopyOnWriteArrayList;
import java.util.concurrent.atomic.AtomicInteger;

public abstract class fm {
    private static final AtomicInteger a = new AtomicInteger(0);

    /* renamed from: a  reason: collision with other field name */
    public static boolean f367a;

    /* renamed from: a  reason: collision with other field name */
    protected int f368a = 0;

    /* renamed from: a  reason: collision with other field name */
    protected long f369a = -1;

    /* renamed from: a  reason: collision with other field name */
    protected fn f370a;

    /* renamed from: a  reason: collision with other field name */
    protected fy f371a = null;

    /* renamed from: a  reason: collision with other field name */
    protected XMPushService f372a;

    /* renamed from: a  reason: collision with other field name */
    protected String f373a = "";

    /* renamed from: a  reason: collision with other field name */
    private final Collection<fp> f374a = new CopyOnWriteArrayList();

    /* renamed from: a  reason: collision with other field name */
    private LinkedList<Pair<Integer, Long>> f375a = new LinkedList<>();

    /* renamed from: a  reason: collision with other field name */
    protected final Map<fr, a> f376a = new ConcurrentHashMap();
    protected final int b = a.getAndIncrement();

    /* renamed from: b  reason: collision with other field name */
    protected volatile long f377b = 0;

    /* renamed from: b  reason: collision with other field name */
    protected String f378b = "";

    /* renamed from: b  reason: collision with other field name */
    protected final Map<fr, a> f379b = new ConcurrentHashMap();
    private int c = 2;

    /* renamed from: c  reason: collision with other field name */
    protected volatile long f380c = 0;
    protected long d = 0;
    private long e = 0;

    public static class a {
        private fr a;

        /* renamed from: a  reason: collision with other field name */
        private fz f381a;

        public a(fr frVar, fz fzVar) {
            this.a = frVar;
            this.f381a = fzVar;
        }

        public void a(ff ffVar) {
            this.a.a(ffVar);
        }

        public void a(gd gdVar) {
            if (this.f381a == null || this.f381a.a(gdVar)) {
                this.a.a(gdVar);
            }
        }
    }

    static {
        f367a = false;
        try {
            f367a = Boolean.getBoolean("smack.debugEnabled");
        } catch (Exception unused) {
        }
        fs.a();
    }

    protected fm(XMPushService xMPushService, fn fnVar) {
        this.f370a = fnVar;
        this.f372a = xMPushService;
        b();
    }

    private String a(int i) {
        return i == 1 ? "connected" : i == 0 ? "connecting" : i == 2 ? "disconnected" : "unknown";
    }

    /* renamed from: a  reason: collision with other method in class */
    private void m303a(int i) {
        synchronized (this.f375a) {
            if (i == 1) {
                try {
                    this.f375a.clear();
                } catch (Throwable th) {
                    throw th;
                }
            } else {
                this.f375a.add(new Pair(Integer.valueOf(i), Long.valueOf(System.currentTimeMillis())));
                if (this.f375a.size() > 6) {
                    this.f375a.remove(0);
                }
            }
        }
    }

    public int a() {
        return this.f368a;
    }

    /* renamed from: a  reason: collision with other method in class */
    public long m304a() {
        return this.f380c;
    }

    /* renamed from: a  reason: collision with other method in class */
    public fn m305a() {
        return this.f370a;
    }

    /* renamed from: a  reason: collision with other method in class */
    public String m306a() {
        return this.f370a.c();
    }

    public void a(int i, int i2, Exception exc) {
        if (i != this.c) {
            b.a(String.format("update the connection status. %1$s -> %2$s : %3$s ", new Object[]{a(this.c), a(i), ap.a(i2)}));
        }
        if (as.b(this.f372a)) {
            a(i);
        }
        if (i == 1) {
            this.f372a.a(10);
            if (this.c != 0) {
                b.a("try set connected while not connecting.");
            }
            this.c = i;
            for (fp a2 : this.f374a) {
                a2.a(this);
            }
        } else if (i == 0) {
            if (this.c != 2) {
                b.a("try set connecting while not disconnected.");
            }
            this.c = i;
            for (fp b2 : this.f374a) {
                b2.b(this);
            }
        } else if (i == 2) {
            this.f372a.a(10);
            if (this.c == 0) {
                for (fp a3 : this.f374a) {
                    a3.a(this, exc == null ? new CancellationException("disconnect while connecting") : exc);
                }
            } else if (this.c == 1) {
                for (fp a4 : this.f374a) {
                    a4.a(this, i2, exc);
                }
            }
            this.c = i;
        }
    }

    public void a(fp fpVar) {
        if (fpVar != null && !this.f374a.contains(fpVar)) {
            this.f374a.add(fpVar);
        }
    }

    public void a(fr frVar, fz fzVar) {
        if (frVar != null) {
            this.f376a.put(frVar, new a(frVar, fzVar));
            return;
        }
        throw new NullPointerException("Packet listener is null.");
    }

    public abstract void a(gd gdVar);

    public abstract void a(al.b bVar);

    public synchronized void a(String str) {
        if (this.c == 0) {
            b.a("setChallenge hash = " + ax.a(str).substring(0, 8));
            this.f373a = str;
            a(1, 0, (Exception) null);
        } else {
            b.a("ignore setChallenge because connection was disconnected");
        }
    }

    public abstract void a(String str, String str2);

    public abstract void a(ff[] ffVarArr);

    /* renamed from: a  reason: collision with other method in class */
    public boolean m307a() {
        return false;
    }

    public synchronized boolean a(long j) {
        return this.e >= j;
    }

    public int b() {
        return this.c;
    }

    /* renamed from: b  reason: collision with other method in class */
    public String m308b() {
        return this.f370a.b();
    }

    /* access modifiers changed from: protected */
    /* renamed from: b  reason: collision with other method in class */
    public void m309b() {
        String str;
        if (this.f370a.a() && this.f371a == null) {
            Class<?> cls = null;
            try {
                str = System.getProperty("smack.debuggerClass");
            } catch (Throwable unused) {
                str = null;
            }
            if (str != null) {
                try {
                    cls = Class.forName(str);
                } catch (Exception e2) {
                    e2.printStackTrace();
                }
            }
            if (cls == null) {
                this.f371a = new bi(this);
                return;
            }
            try {
                this.f371a = (fy) cls.getConstructor(new Class[]{fm.class, Writer.class, Reader.class}).newInstance(new Object[]{this});
            } catch (Exception e3) {
                throw new IllegalArgumentException("Can't initialize the configured debugger!", e3);
            }
        }
    }

    public abstract void b(int i, Exception exc);

    public abstract void b(ff ffVar);

    public void b(fp fpVar) {
        this.f374a.remove(fpVar);
    }

    public void b(fr frVar, fz fzVar) {
        if (frVar != null) {
            this.f379b.put(frVar, new a(frVar, fzVar));
            return;
        }
        throw new NullPointerException("Packet listener is null.");
    }

    public abstract void b(boolean z);

    /* renamed from: b  reason: collision with other method in class */
    public boolean m310b() {
        return this.c == 0;
    }

    public synchronized void c() {
        this.e = System.currentTimeMillis();
    }

    /* renamed from: c  reason: collision with other method in class */
    public boolean m311c() {
        return this.c == 1;
    }

    public void d() {
        synchronized (this.f375a) {
            this.f375a.clear();
        }
    }

    /* renamed from: d  reason: collision with other method in class */
    public synchronized boolean m312d() {
        return System.currentTimeMillis() - this.e < ((long) fs.a());
    }

    public synchronized boolean e() {
        boolean z;
        z = true;
        if (System.currentTimeMillis() - this.d >= ((long) (fs.a() << 1))) {
            z = false;
        }
        return z;
    }
}
