package com.xiaomi.push;

import com.xiaomi.push.al;
import com.xiaomi.push.dd;

class df extends al.b {
    al.b a;

    /* renamed from: a  reason: collision with other field name */
    final /* synthetic */ dd f230a;

    df(dd ddVar) {
        this.f230a = ddVar;
    }

    public void b() {
        dd.b bVar = (dd.b) dd.a(this.f230a).peek();
        if (bVar != null && bVar.a()) {
            if (dd.a(this.f230a).remove(bVar)) {
                this.a = bVar;
            }
            if (this.a != null) {
                this.a.b();
            }
        }
    }

    public void c() {
        if (this.a != null) {
            this.a.c();
        }
    }
}
