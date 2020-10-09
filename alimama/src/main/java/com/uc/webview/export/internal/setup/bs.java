package com.uc.webview.export.internal.setup;

import android.webkit.ValueCallback;
import com.uc.webview.export.internal.setup.ae;
import com.uc.webview.export.internal.uc.startup.b;
import com.uc.webview.export.internal.utility.i;
import java.util.HashSet;
import java.util.Iterator;
import java.util.concurrent.Callable;

/* compiled from: U4Source */
public abstract class bs {
    static final Object g = new Object();
    protected br e;
    protected UCSubSetupTask f;
    HashSet<ae.b> h = new HashSet<>();

    /* access modifiers changed from: protected */
    public boolean a() {
        return false;
    }

    /* access modifiers changed from: protected */
    public void b() {
    }

    public bs(UCSubSetupTask uCSubSetupTask, br brVar) {
        this.e = brVar;
        this.f = uCSubSetupTask;
    }

    public final void c() {
        b.a(298);
        i.a();
        if (!a()) {
            b();
        }
    }

    protected static int d() {
        return ae.d.b;
    }

    public final void a(int i, ae.b bVar, Callable<?> callable, ValueCallback<Object> valueCallback) {
        if (i != ae.d.b) {
            synchronized (g) {
                this.h.add(bVar);
            }
        }
        ae.a().a(i, bVar, callable, valueCallback);
    }

    public final void e() {
        synchronized (g) {
            if (!this.h.isEmpty()) {
                Iterator<ae.b> it = this.h.iterator();
                while (it.hasNext()) {
                    ae.a().a(it.next());
                }
                this.h.clear();
            }
        }
    }
}
