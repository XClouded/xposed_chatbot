package com.huawei.hms.api;

import android.os.Bundle;
import com.huawei.hms.core.aidl.IMessageEntity;
import com.huawei.hms.core.aidl.ResponseHeader;
import com.huawei.hms.core.aidl.a;
import com.huawei.hms.core.aidl.b;
import com.huawei.hms.core.aidl.d;
import com.huawei.hms.core.aidl.f;
import com.huawei.hms.support.api.client.BundleResult;
import com.huawei.hms.support.api.client.ResultCallback;

/* compiled from: HuaweiApiClientImpl */
class h extends d.a {
    final /* synthetic */ ResultCallback a;
    final /* synthetic */ HuaweiApiClientImpl b;

    h(HuaweiApiClientImpl huaweiApiClientImpl, ResultCallback resultCallback) {
        this.b = huaweiApiClientImpl;
        this.a = resultCallback;
    }

    public void call(b bVar) {
        if (bVar != null) {
            f a2 = a.a(bVar.c());
            ResponseHeader responseHeader = new ResponseHeader();
            a2.a(bVar.b, (IMessageEntity) responseHeader);
            BundleResult bundleResult = new BundleResult(responseHeader.getStatusCode(), bVar.a());
            com.huawei.hms.support.log.a.b("HuaweiApiClientImpl", "Exit asyncRequest onResult");
            this.a.onResult(bundleResult);
            return;
        }
        com.huawei.hms.support.log.a.b("HuaweiApiClientImpl", "Exit asyncRequest onResult -1");
        this.a.onResult(new BundleResult(-1, (Bundle) null));
    }
}
