package com.huawei.hms.support.api.a;

import com.huawei.hms.core.aidl.IMessageEntity;
import com.huawei.hms.support.api.ResolveResult;
import com.huawei.hms.support.api.client.ApiClient;
import com.huawei.hms.support.api.client.Status;
import com.huawei.hms.support.api.entity.core.JosGetNoticeResp;
import com.huawei.hms.support.log.a;

/* compiled from: ConnectService */
final class c extends com.huawei.hms.support.api.c<ResolveResult<JosGetNoticeResp>, JosGetNoticeResp> {
    c(ApiClient apiClient, String str, IMessageEntity iMessageEntity) {
        super(apiClient, str, iMessageEntity);
    }

    /* renamed from: a */
    public ResolveResult<JosGetNoticeResp> onComplete(JosGetNoticeResp josGetNoticeResp) {
        if (josGetNoticeResp == null) {
            a.d("connectservice", "JosNoticeResp is null");
            return null;
        }
        a.b("connectservice", "josNoticeResp status code :" + josGetNoticeResp.getStatusCode());
        ResolveResult<JosGetNoticeResp> resolveResult = new ResolveResult<>(josGetNoticeResp);
        resolveResult.setStatus(Status.SUCCESS);
        return resolveResult;
    }
}
