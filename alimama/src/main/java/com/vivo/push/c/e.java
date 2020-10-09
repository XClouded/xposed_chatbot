package com.vivo.push.c;

import android.text.TextUtils;
import com.vivo.push.b.k;

/* compiled from: OnBindAppReceiveTask */
final class e implements Runnable {
    final /* synthetic */ String a;
    final /* synthetic */ k b;
    final /* synthetic */ d c;

    e(d dVar, String str, k kVar) {
        this.c = dVar;
        this.a = str;
        this.b = kVar;
    }

    public final void run() {
        if (!TextUtils.isEmpty(this.a)) {
            this.c.b.onReceiveRegId(this.c.a, this.a);
        }
        this.c.b.onBind(this.c.a, this.b.h(), this.b.d());
    }
}
