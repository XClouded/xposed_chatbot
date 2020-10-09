package com.uploader.implement.b.a;

import com.uploader.implement.b.b;
import com.uploader.implement.b.e;
import com.uploader.implement.c;
import java.lang.ref.WeakReference;

/* compiled from: BaseConnection */
public abstract class a implements e {
    final com.uploader.implement.b.a a;
    volatile WeakReference<b> b;
    final int c = hashCode();
    c d;

    a(c cVar, com.uploader.implement.b.a aVar) {
        this.d = cVar;
        this.a = aVar;
    }

    public com.uploader.implement.b.a a() {
        return this.a;
    }

    public void a(b bVar) {
        this.b = new WeakReference<>(bVar);
    }

    /* access modifiers changed from: package-private */
    public b e() {
        WeakReference<b> weakReference = this.b;
        if (weakReference == null) {
            return null;
        }
        return (b) weakReference.get();
    }
}
