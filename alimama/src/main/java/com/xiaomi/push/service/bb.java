package com.xiaomi.push.service;

import android.util.Base64;
import com.xiaomi.channel.commonutils.logger.b;
import com.xiaomi.push.al;
import com.xiaomi.push.ar;
import com.xiaomi.push.cy;
import com.xiaomi.push.ed;
import com.xiaomi.push.service.ba;
import com.xiaomi.push.t;
import java.util.List;

class bb extends al.b {
    final /* synthetic */ ba a;

    /* renamed from: a  reason: collision with other field name */
    boolean f884a = false;

    bb(ba baVar) {
        this.a = baVar;
    }

    public void b() {
        try {
            ed.a a2 = ed.a.a(Base64.decode(cy.a(t.a(), "http://resolver.msg.xiaomi.net/psc/?t=a", (List<ar>) null), 10));
            if (a2 != null) {
                ed.a unused = this.a.f882a = a2;
                this.f884a = true;
                ba.a(this.a);
            }
        } catch (Exception e) {
            b.a("fetch config failure: " + e.getMessage());
        }
    }

    public void c() {
        ba.a[] aVarArr;
        al.b unused = this.a.f881a = null;
        if (this.f884a) {
            synchronized (this.a) {
                aVarArr = (ba.a[]) ba.a(this.a).toArray(new ba.a[ba.a(this.a).size()]);
            }
            for (ba.a a2 : aVarArr) {
                a2.a(ba.a(this.a));
            }
        }
    }
}
