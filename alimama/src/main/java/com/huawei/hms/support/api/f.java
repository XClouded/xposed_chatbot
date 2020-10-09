package com.huawei.hms.support.api;

import com.huawei.hms.core.aidl.IMessageEntity;
import com.huawei.hms.support.api.c;
import com.huawei.hms.support.api.client.ResultCallback;
import com.huawei.hms.support.api.transport.DatagramTransport;

/* compiled from: PendingResultImpl */
class f implements DatagramTransport.a {
    final /* synthetic */ c.a a;
    final /* synthetic */ ResultCallback b;
    final /* synthetic */ c c;

    f(c cVar, c.a aVar, ResultCallback resultCallback) {
        this.c = cVar;
        this.a = aVar;
        this.b = resultCallback;
    }

    public void a(int i, IMessageEntity iMessageEntity) {
        this.c.a(i, iMessageEntity);
        this.a.a(this.b, this.c.b);
    }
}
