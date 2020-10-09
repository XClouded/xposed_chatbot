package com.vivo.push.sdk;

import com.vivo.push.util.p;
import java.lang.reflect.Method;

/* compiled from: CommandWorker */
final class b implements Runnable {
    final /* synthetic */ Method a;
    final /* synthetic */ Object b;
    final /* synthetic */ Object[] c;
    final /* synthetic */ a d;

    b(a aVar, Method method, Object obj, Object[] objArr) {
        this.d = aVar;
        this.a = method;
        this.b = obj;
        this.c = objArr;
    }

    public final void run() {
        try {
            this.a.invoke(this.b, this.c);
        } catch (Exception e) {
            p.b("CommandWorker", "reflect e: ", e);
        }
    }
}
