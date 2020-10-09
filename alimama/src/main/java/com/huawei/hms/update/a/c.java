package com.huawei.hms.update.a;

import com.huawei.hms.update.a.a.a;
import com.huawei.hms.update.a.a.b;
import java.util.concurrent.Executor;
import java.util.concurrent.Executors;

/* compiled from: ThreadWrapper */
public class c implements a {
    private static final Executor a = Executors.newSingleThreadExecutor();
    /* access modifiers changed from: private */
    public final a b;

    public c(a aVar) {
        com.huawei.hms.c.a.a(aVar, "update must not be null.");
        this.b = aVar;
    }

    /* access modifiers changed from: private */
    public static b b(b bVar) {
        return new d(bVar);
    }

    public void a() {
        this.b.a();
    }

    public void a(b bVar, com.huawei.hms.update.a.a.c cVar) {
        a.execute(new g(this, bVar, cVar));
    }
}
