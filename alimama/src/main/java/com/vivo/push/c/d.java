package com.vivo.push.c;

import android.text.TextUtils;
import com.vivo.push.b.k;
import com.vivo.push.p;
import com.vivo.push.w;
import com.vivo.push.y;

/* compiled from: OnBindAppReceiveTask */
final class d extends ab {
    d(y yVar) {
        super(yVar);
    }

    /* access modifiers changed from: protected */
    public final void a(y yVar) {
        k kVar = (k) yVar;
        String e = kVar.e();
        p.a().a(kVar.g(), kVar.h(), e);
        if (TextUtils.isEmpty(kVar.g()) && !TextUtils.isEmpty(e)) {
            p.a().a(e);
        }
        w.b(new e(this, e, kVar));
    }
}
