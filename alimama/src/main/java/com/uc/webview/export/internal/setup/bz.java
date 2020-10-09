package com.uc.webview.export.internal.setup;

import android.util.Pair;
import android.webkit.ValueCallback;
import com.uc.webview.export.internal.utility.Log;
import com.uc.webview.export.internal.utility.e;
import com.uc.webview.export.internal.utility.k;

/* compiled from: U4Source */
final class bz implements Runnable {
    final /* synthetic */ bo a;
    final /* synthetic */ ValueCallback b;
    final /* synthetic */ Pair c;
    final /* synthetic */ bw d;

    bz(bw bwVar, bo boVar, ValueCallback valueCallback, Pair pair) {
        this.d = bwVar;
        this.a = boVar;
        this.b = valueCallback;
        this.c = pair;
    }

    public final void run() {
        try {
            if (this.a.a()) {
                String d2 = e.d(this.d.getContext().getApplicationContext());
                String a2 = bw.e;
                Log.d(a2, ".shareCoreWaitTimeout localDir:" + d2 + " isWaitting:" + this.a.a());
                if (!k.a(d2) && this.a.a()) {
                    String unused = this.d.c = d2;
                    this.b.onReceiveValue(this.d);
                    this.a.a(8, (Object) null);
                } else if (this.a.a()) {
                    String e = e.e(this.d.getContext().getApplicationContext());
                    String a3 = bw.e;
                    Log.d(a3, ".shareCoreWaitTimeout decFile:" + e + " isWaitting:" + this.a.a());
                    if (!k.a(e) && this.a.a()) {
                        String unused2 = this.d.d = e;
                        this.b.onReceiveValue(this.d);
                        this.a.a(8, (Object) null);
                    } else if (((Integer) this.c.first).intValue() != 1) {
                        this.a.a(((Integer) this.c.first).intValue(), this.c.second);
                    }
                }
            }
        } catch (Throwable th) {
            Log.d(bw.e, ".shareCoreWaitTimeout Thread ", th);
            if (((Integer) this.c.first).intValue() != 1) {
                this.a.a(((Integer) this.c.first).intValue(), this.c.second);
            }
        }
    }
}
