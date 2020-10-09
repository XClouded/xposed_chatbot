package com.vivo.push.c;

import com.vivo.push.a.a;
import com.vivo.push.b.ac;
import com.vivo.push.b.d;
import com.vivo.push.b.f;
import com.vivo.push.util.s;
import com.vivo.push.v;
import com.vivo.push.y;

/* compiled from: ChangeNetPermissionTask */
final class b extends v {
    b(y yVar) {
        super(yVar);
    }

    /* access modifiers changed from: protected */
    public final void a(y yVar) {
        boolean z;
        com.vivo.push.model.b a = s.a(this.a);
        if (((d) yVar).d()) {
            try {
                z = f.a(this.a);
            } catch (Exception e) {
                e.printStackTrace();
                return;
            }
        } else {
            z = f.b(this.a);
        }
        if (z) {
            com.vivo.push.model.b a2 = s.a(this.a);
            if (a == null || a2 == null || a2.a() == null || !a2.a().equals(a.a())) {
                if (!(a == null || a.a() == null)) {
                    a.a(this.a, a.a(), (y) new ac(a.a()));
                }
                if (a2 != null && a2.a() != null) {
                    a.a(this.a, a2.a(), (y) new f());
                }
            }
        }
    }
}
