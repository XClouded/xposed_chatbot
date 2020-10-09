package com.alipay.sdk.data;

import android.content.Context;
import java.util.HashMap;
import java.util.concurrent.Callable;

class d implements Callable<String> {
    final /* synthetic */ Context a;
    final /* synthetic */ HashMap b;
    final /* synthetic */ c c;

    d(c cVar, Context context, HashMap hashMap) {
        this.c = cVar;
        this.a = context;
        this.b = hashMap;
    }

    /* renamed from: a */
    public String call() throws Exception {
        return this.c.a(this.a, this.b);
    }
}
