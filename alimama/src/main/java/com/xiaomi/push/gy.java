package com.xiaomi.push;

import android.content.Context;
import com.taobao.uikit.feature.features.FeatureFactory;
import com.xiaomi.channel.commonutils.logger.b;
import com.xiaomi.push.au;
import com.xiaomi.push.ji;
import com.xiaomi.push.service.XMPushService;
import com.xiaomi.push.service.ba;
import java.util.ArrayList;
import java.util.LinkedList;
import java.util.NoSuchElementException;

public class gy {
    private int a;

    /* renamed from: a  reason: collision with other field name */
    private long f437a;

    /* renamed from: a  reason: collision with other field name */
    private au f438a = au.a();

    /* renamed from: a  reason: collision with other field name */
    private gx f439a;

    /* renamed from: a  reason: collision with other field name */
    private String f440a;

    /* renamed from: a  reason: collision with other field name */
    private boolean f441a = false;

    static class a {
        static final gy a = new gy();
    }

    private fc a(au.a aVar) {
        if (aVar.f126a != 0) {
            fc a2 = a();
            a2.a(fb.CHANNEL_STATS_COUNTER.a());
            a2.c(aVar.f126a);
            a2.c(aVar.f128a);
            return a2;
        } else if (aVar.f127a instanceof fc) {
            return (fc) aVar.f127a;
        } else {
            return null;
        }
    }

    private fd a(int i) {
        ArrayList arrayList = new ArrayList();
        fd fdVar = new fd(this.f440a, arrayList);
        if (!as.d(this.f439a.f434a)) {
            fdVar.a(i.m(this.f439a.f434a));
        }
        jk jkVar = new jk(i);
        jc a2 = new ji.a().a(jkVar);
        try {
            fdVar.b(a2);
        } catch (iw unused) {
        }
        LinkedList a3 = this.f438a.a();
        while (true) {
            try {
                if (a3.size() <= 0) {
                    break;
                }
                fc a4 = a((au.a) a3.getLast());
                if (a4 != null) {
                    a4.b(a2);
                }
                if (jkVar.a_() > i) {
                    break;
                }
                if (a4 != null) {
                    arrayList.add(a4);
                }
                a3.removeLast();
            } catch (iw | NoSuchElementException unused2) {
            }
        }
        return fdVar;
    }

    public static gx a() {
        gx gxVar;
        synchronized (a.a) {
            gxVar = a.a.f439a;
        }
        return gxVar;
    }

    /* renamed from: a  reason: collision with other method in class */
    public static gy m354a() {
        return a.a;
    }

    /* renamed from: a  reason: collision with other method in class */
    private void m355a() {
        if (this.f441a && System.currentTimeMillis() - this.f437a > ((long) this.a)) {
            this.f441a = false;
            this.f437a = 0;
        }
    }

    /* access modifiers changed from: package-private */
    /* renamed from: a  reason: collision with other method in class */
    public synchronized fc m356a() {
        fc fcVar;
        fcVar = new fc();
        fcVar.a(as.a((Context) this.f439a.f434a));
        fcVar.f332a = 0;
        fcVar.f336b = 1;
        fcVar.d((int) (System.currentTimeMillis() / 1000));
        return fcVar;
    }

    /* access modifiers changed from: package-private */
    /* renamed from: a  reason: collision with other method in class */
    public synchronized fd m357a() {
        fd fdVar;
        fdVar = null;
        if (b()) {
            int i = FeatureFactory.PRIORITY_ABOVE_NORMAL;
            if (!as.d(this.f439a.f434a)) {
                i = 375;
            }
            fdVar = a(i);
        }
        return fdVar;
    }

    /* renamed from: a  reason: collision with other method in class */
    public void m358a(int i) {
        if (i > 0) {
            int i2 = i * 1000;
            if (i2 > 604800000) {
                i2 = 604800000;
            }
            if (this.a != i2 || !this.f441a) {
                this.f441a = true;
                this.f437a = System.currentTimeMillis();
                this.a = i2;
                b.c("enable dot duration = " + i2 + " start = " + this.f437a);
            }
        }
    }

    /* access modifiers changed from: package-private */
    public synchronized void a(fc fcVar) {
        this.f438a.a(fcVar);
    }

    public synchronized void a(XMPushService xMPushService) {
        this.f439a = new gx(xMPushService);
        this.f440a = "";
        ba.a().a((ba.a) new gz(this));
    }

    /* renamed from: a  reason: collision with other method in class */
    public boolean m359a() {
        return this.f441a;
    }

    /* access modifiers changed from: package-private */
    public boolean b() {
        a();
        return this.f441a && this.f438a.a() > 0;
    }
}
