package com.huawei.hms.support.api;

import com.huawei.hms.core.aidl.IMessageEntity;
import com.huawei.hms.support.api.transport.DatagramTransport;

/* compiled from: PendingResultImpl */
class d implements DatagramTransport.a {
    final /* synthetic */ c a;

    d(c cVar) {
        this.a = cVar;
    }

    public void a(int i, IMessageEntity iMessageEntity) {
        this.a.a(i, iMessageEntity);
        this.a.a.countDown();
    }
}
