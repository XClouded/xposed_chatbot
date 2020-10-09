package com.alipay.sdk.authjs;

import com.alipay.sdk.authjs.a;
import org.json.JSONException;

class e implements Runnable {
    final /* synthetic */ a a;
    final /* synthetic */ d b;

    e(d dVar, a aVar) {
        this.b = dVar;
        this.a = aVar;
    }

    public void run() {
        a.C0000a a2 = this.b.b(this.a);
        if (a2 != a.C0000a.NONE_ERROR) {
            try {
                this.b.a(this.a.b(), a2, true);
            } catch (JSONException unused) {
            }
        }
    }
}
