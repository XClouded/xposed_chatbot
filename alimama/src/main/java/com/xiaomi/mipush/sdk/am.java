package com.xiaomi.mipush.sdk;

import com.xiaomi.mipush.sdk.MiTinyDataClient;
import java.util.concurrent.ScheduledFuture;

class am implements Runnable {
    final /* synthetic */ MiTinyDataClient.a.C0031a a;

    am(MiTinyDataClient.a.C0031a aVar) {
        this.a = aVar;
    }

    public void run() {
        if (this.a.f24a.size() != 0) {
            this.a.b();
        } else if (MiTinyDataClient.a.C0031a.a(this.a) != null) {
            MiTinyDataClient.a.C0031a.a(this.a).cancel(false);
            ScheduledFuture unused = this.a.f25a = null;
        }
    }
}
