package com.xiaomi.push;

import android.text.TextUtils;
import com.taobao.weex.el.parse.Operators;
import com.xiaomi.channel.commonutils.logger.b;
import com.xiaomi.push.ee;
import com.xiaomi.push.fm;
import com.xiaomi.push.service.XMPushService;
import com.xiaomi.push.service.al;
import com.xiaomi.push.service.au;
import com.xiaomi.push.service.ba;

public class fk extends ft {
    /* access modifiers changed from: private */
    public fg a;

    /* renamed from: a  reason: collision with other field name */
    private fh f364a;

    /* renamed from: a  reason: collision with other field name */
    private Thread f365a;

    /* renamed from: a  reason: collision with other field name */
    private byte[] f366a;

    public fk(XMPushService xMPushService, fn fnVar) {
        super(xMPushService, fnVar);
    }

    private ff a(boolean z) {
        fj fjVar = new fj();
        if (z) {
            fjVar.a("1");
        }
        byte[] a2 = ha.a();
        if (a2 != null) {
            ee.j jVar = new ee.j();
            jVar.a(a.a(a2));
            fjVar.a(jVar.a(), (String) null);
        }
        return fjVar;
    }

    private void h() {
        try {
            this.a = new fg(this.f387a.getInputStream(), this);
            this.f364a = new fh(this.f387a.getOutputStream(), this);
            this.f365a = new fl(this, "Blob Reader (" + this.b + Operators.BRACKET_END_STR);
            this.f365a.start();
        } catch (Exception e) {
            throw new fx("Error to init reader and writer", e);
        }
    }

    /* access modifiers changed from: protected */
    public synchronized void a() {
        h();
        this.f364a.a();
    }

    /* access modifiers changed from: protected */
    public synchronized void a(int i, Exception exc) {
        if (this.a != null) {
            this.a.b();
            this.a = null;
        }
        if (this.f364a != null) {
            try {
                this.f364a.b();
            } catch (Exception e) {
                b.a((Throwable) e);
            }
            this.f364a = null;
        }
        this.f366a = null;
        super.a(i, exc);
    }

    /* access modifiers changed from: package-private */
    public void a(ff ffVar) {
        if (ffVar != null) {
            if (ffVar.a()) {
                b.a("[Slim] RCV blob chid=" + ffVar.a() + "; id=" + ffVar.e() + "; errCode=" + ffVar.b() + "; err=" + ffVar.c());
            }
            if (ffVar.a() == 0) {
                if ("PING".equals(ffVar.a())) {
                    b.a("[Slim] RCV ping id=" + ffVar.e());
                    g();
                } else if ("CLOSE".equals(ffVar.a())) {
                    c(13, (Exception) null);
                }
            }
            for (fm.a a2 : this.f376a.values()) {
                a2.a(ffVar);
            }
        }
    }

    @Deprecated
    public void a(gd gdVar) {
        b(ff.a(gdVar, (String) null));
    }

    public synchronized void a(al.b bVar) {
        fe.a(bVar, c(), (fm) this);
    }

    public synchronized void a(String str, String str2) {
        fe.a(str, str2, (fm) this);
    }

    /* access modifiers changed from: protected */
    /* renamed from: a  reason: collision with other method in class */
    public void m300a(boolean z) {
        if (this.f364a != null) {
            ff a2 = a(z);
            b.a("[Slim] SND ping id=" + a2.e());
            b(a2);
            f();
            return;
        }
        throw new fx("The BlobWriter is null.");
    }

    public void a(ff[] ffVarArr) {
        for (ff b : ffVarArr) {
            b(b);
        }
    }

    /* renamed from: a  reason: collision with other method in class */
    public boolean m301a() {
        return true;
    }

    /* access modifiers changed from: package-private */
    /* renamed from: a  reason: collision with other method in class */
    public synchronized byte[] m302a() {
        if (this.f366a == null && !TextUtils.isEmpty(this.f373a)) {
            String a2 = ba.a();
            this.f366a = au.a(this.f373a.getBytes(), (this.f373a.substring(this.f373a.length() / 2) + a2.substring(a2.length() / 2)).getBytes());
        }
        return this.f366a;
    }

    public void b(ff ffVar) {
        if (this.f364a != null) {
            try {
                int a2 = this.f364a.a(ffVar);
                this.d = System.currentTimeMillis();
                String f = ffVar.f();
                if (!TextUtils.isEmpty(f)) {
                    gr.a(this.f372a, f, (long) a2, false, true, System.currentTimeMillis());
                }
                for (fm.a a3 : this.f379b.values()) {
                    a3.a(ffVar);
                }
            } catch (Exception e) {
                throw new fx((Throwable) e);
            }
        } else {
            throw new fx("the writer is null.");
        }
    }

    /* access modifiers changed from: package-private */
    public void b(gd gdVar) {
        if (gdVar != null) {
            for (fm.a a2 : this.f376a.values()) {
                a2.a(gdVar);
            }
        }
    }
}
