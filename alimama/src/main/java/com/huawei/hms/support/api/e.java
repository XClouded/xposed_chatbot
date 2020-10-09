package com.huawei.hms.support.api;

import com.huawei.hms.core.aidl.IMessageEntity;
import com.huawei.hms.support.api.transport.DatagramTransport;
import java.util.concurrent.atomic.AtomicBoolean;

/* compiled from: PendingResultImpl */
class e implements DatagramTransport.a {
    final /* synthetic */ AtomicBoolean a;
    final /* synthetic */ c b;

    e(c cVar, AtomicBoolean atomicBoolean) {
        this.b = cVar;
        this.a = atomicBoolean;
    }

    public void a(int i, IMessageEntity iMessageEntity) {
        if (!this.a.get()) {
            this.b.a(i, iMessageEntity);
        }
        this.b.a.countDown();
    }
}
