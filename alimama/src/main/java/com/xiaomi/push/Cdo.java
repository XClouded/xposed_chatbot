package com.xiaomi.push;

import android.content.Context;
import android.content.Intent;

/* renamed from: com.xiaomi.push.do  reason: invalid class name */
class Cdo implements Runnable {
    final /* synthetic */ Context a;

    /* renamed from: a  reason: collision with other field name */
    final /* synthetic */ Intent f237a;

    /* renamed from: a  reason: collision with other field name */
    final /* synthetic */ dn f238a;

    Cdo(dn dnVar, Context context, Intent intent) {
        this.f238a = dnVar;
        this.a = context;
        this.f237a = intent;
    }

    public void run() {
        this.f238a.b(this.a, this.f237a);
    }
}
