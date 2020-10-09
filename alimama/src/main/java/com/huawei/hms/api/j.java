package com.huawei.hms.api;

import com.huawei.hms.api.HuaweiApiClientImpl;
import com.huawei.hms.support.api.ResolveResult;
import com.huawei.hms.support.api.entity.core.DisconnectResp;

/* compiled from: HuaweiApiClientImpl */
class j implements Runnable {
    final /* synthetic */ ResolveResult a;
    final /* synthetic */ HuaweiApiClientImpl.b b;

    j(HuaweiApiClientImpl.b bVar, ResolveResult resolveResult) {
        this.b = bVar;
        this.a = resolveResult;
    }

    public void run() {
        HuaweiApiClientImpl.this.a((ResolveResult<DisconnectResp>) this.a);
    }
}
