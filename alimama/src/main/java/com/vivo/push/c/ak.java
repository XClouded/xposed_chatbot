package com.vivo.push.c;

import com.vivo.push.a.a;
import com.vivo.push.b.c;
import com.vivo.push.b.e;
import com.vivo.push.model.b;
import com.vivo.push.p;
import com.vivo.push.util.r;
import com.vivo.push.util.s;
import com.vivo.push.v;
import com.vivo.push.y;

/* compiled from: UnbindAppSendCommandTask */
final class ak extends v {
    ak(y yVar) {
        super(yVar);
    }

    /* access modifiers changed from: protected */
    public final void a(y yVar) {
        c cVar = (c) yVar;
        b a = s.a(this.a);
        if (a == null) {
            p.a().a(cVar.h(), 1005, new Object[0]);
            return;
        }
        String a2 = a.a();
        if (a.c()) {
            p.a().a(cVar.h(), 1004, new Object[0]);
            yVar = new e();
        } else {
            int a3 = r.a(cVar);
            if (a3 != 0) {
                p.a().a(cVar.h(), a3, new Object[0]);
                return;
            }
        }
        a.a(this.a, a2, yVar);
    }
}
