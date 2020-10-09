package com.huawei.hms.api;

import android.os.Bundle;
import androidx.core.app.NotificationCompat;
import com.huawei.hms.core.aidl.IMessageEntity;
import com.huawei.hms.core.aidl.RequestHeader;
import com.huawei.hms.core.aidl.a;
import com.huawei.hms.core.aidl.b;
import com.huawei.hms.core.aidl.d;
import com.huawei.hms.core.aidl.f;
import com.huawei.hms.support.api.client.ApiClient;
import com.huawei.hms.support.api.entity.core.CommonCode;
import com.huawei.hms.support.api.transport.DatagramTransport;

public class IPCTransport implements DatagramTransport {
    private final String a;
    private final IMessageEntity b;
    private final Class<? extends IMessageEntity> c;

    public IPCTransport(String str, IMessageEntity iMessageEntity, Class<? extends IMessageEntity> cls) {
        this.a = str;
        this.b = iMessageEntity;
        this.c = cls;
    }

    public final void send(ApiClient apiClient, DatagramTransport.a aVar) {
        int a2 = a(apiClient, new IPCCallback(this.c, aVar));
        if (a2 != 0) {
            aVar.a(a2, (IMessageEntity) null);
        }
    }

    public final void post(ApiClient apiClient, DatagramTransport.a aVar) {
        send(apiClient, aVar);
    }

    private int a(ApiClient apiClient, d dVar) {
        b bVar = new b(this.a, ProtocolNegotiate.getInstance().getVersion());
        f a2 = a.a(bVar.c());
        bVar.a(a2.a(this.b, new Bundle()));
        RequestHeader requestHeader = new RequestHeader();
        requestHeader.setAppID(apiClient.getAppID());
        requestHeader.setPackageName(apiClient.getPackageName());
        requestHeader.setSdkVersion(HuaweiApiAvailability.HMS_SDK_VERSION_CODE);
        requestHeader.setApiNameList(((HuaweiApiClientImpl) apiClient).getApiNameList());
        if (apiClient instanceof HuaweiApiClientImpl) {
            requestHeader.setSessionId(apiClient.getSessionId());
        }
        bVar.b = a2.a((IMessageEntity) requestHeader, new Bundle());
        try {
            ((HuaweiApiClientImpl) apiClient).getService().a(bVar, dVar);
            return 0;
        } catch (Exception e) {
            com.huawei.hms.support.log.a.d(NotificationCompat.CATEGORY_TRANSPORT, "sync call ex:" + e.getMessage());
            return CommonCode.ErrorCode.INTERNAL_ERROR;
        }
    }
}
