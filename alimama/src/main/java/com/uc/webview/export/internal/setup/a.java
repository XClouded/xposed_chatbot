package com.uc.webview.export.internal.setup;

import android.util.Pair;
import android.webkit.ValueCallback;
import com.uc.webview.export.internal.uc.startup.b;
import com.uc.webview.export.internal.utility.Log;

/* compiled from: U4Source */
public abstract class a implements Runnable {
    protected String a;
    protected Pair<Integer, Integer> b;
    private int c;
    private Throwable d;
    private ValueCallback<a> e;

    /* access modifiers changed from: protected */
    public abstract void a();

    a() {
        this.c = 0;
        this.d = null;
        this.e = null;
        this.a = a.class.getSimpleName();
        this.b = null;
        this.e = null;
    }

    public void run() {
        long d2 = b.d();
        StringBuilder sb = new StringBuilder();
        sb.append(this.a);
        sb.append(".run");
        a(1);
        if (this.b != null) {
            b.a(((Integer) this.b.first).intValue());
        }
        a();
        a(2);
        if (this.b != null) {
            b.a(((Integer) this.b.second).intValue());
        }
        StringBuilder sb2 = new StringBuilder();
        sb2.append(this.a);
        sb2.append(".run");
        String str = this.a;
        Log.i(str, "execute cost:" + (b.d() - d2));
    }

    private void a(int i) {
        String str = this.a;
        Log.i(str, "notifyStatusChange status:" + i);
        this.c = i;
        if (this.e != null) {
            this.e.onReceiveValue(this);
        }
    }
}
