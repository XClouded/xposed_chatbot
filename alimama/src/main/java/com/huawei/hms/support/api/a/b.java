package com.huawei.hms.support.api.a;

import com.huawei.hms.core.aidl.IMessageEntity;
import com.huawei.hms.support.api.ResolveResult;
import com.huawei.hms.support.api.c;
import com.huawei.hms.support.api.client.ApiClient;
import com.huawei.hms.support.api.client.Status;
import com.huawei.hms.support.api.entity.core.ConnectResp;
import com.huawei.hms.support.log.a;

/* compiled from: ConnectService */
final class b extends c<ResolveResult<ConnectResp>, ConnectResp> {
    /* access modifiers changed from: protected */
    public boolean checkApiClient(ApiClient apiClient) {
        return apiClient != null;
    }

    b(ApiClient apiClient, String str, IMessageEntity iMessageEntity) {
        super(apiClient, str, iMessageEntity);
    }

    /* renamed from: a */
    public ResolveResult<ConnectResp> onComplete(ConnectResp connectResp) {
        ResolveResult<ConnectResp> resolveResult = new ResolveResult<>(connectResp);
        resolveResult.setStatus(Status.SUCCESS);
        a.a("connectservice", "connect - onComplete: success");
        return resolveResult;
    }
}
