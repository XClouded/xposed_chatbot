package com.huawei.hms.api;

import com.huawei.hms.api.HuaweiApiClientImpl;
import com.huawei.hms.support.api.ResolveResult;
import com.huawei.hms.support.api.entity.core.ConnectResp;

/* compiled from: HuaweiApiClientImpl */
class i implements Runnable {
    final /* synthetic */ ResolveResult a;
    final /* synthetic */ HuaweiApiClientImpl.a b;

    i(HuaweiApiClientImpl.a aVar, ResolveResult resolveResult) {
        this.b = aVar;
        this.a = resolveResult;
    }

    public void run() {
        HuaweiApiClientImpl.this.b((ResolveResult<ConnectResp>) this.a);
    }
}
