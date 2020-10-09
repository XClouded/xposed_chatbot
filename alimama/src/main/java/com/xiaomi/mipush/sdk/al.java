package com.xiaomi.mipush.sdk;

import com.xiaomi.mipush.sdk.MiTinyDataClient;
import com.xiaomi.push.hk;

class al implements Runnable {
    final /* synthetic */ MiTinyDataClient.a.C0031a a;

    /* renamed from: a  reason: collision with other field name */
    final /* synthetic */ hk f31a;

    al(MiTinyDataClient.a.C0031a aVar, hk hkVar) {
        this.a = aVar;
        this.f31a = hkVar;
    }

    public void run() {
        this.a.f24a.add(this.f31a);
        MiTinyDataClient.a.C0031a.a(this.a);
    }
}
