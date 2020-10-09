package com.huawei.hms.api;

import android.os.RemoteException;
import android.text.TextUtils;
import com.huawei.hms.core.aidl.IMessageEntity;
import com.huawei.hms.core.aidl.ResponseHeader;
import com.huawei.hms.core.aidl.b;
import com.huawei.hms.core.aidl.d;
import com.huawei.hms.core.aidl.f;
import com.huawei.hms.support.api.transport.DatagramTransport;
import com.huawei.hms.support.log.a;

public class IPCCallback extends d.a {
    private final Class<? extends IMessageEntity> a;
    private final DatagramTransport.a b;

    public IPCCallback(Class<? extends IMessageEntity> cls, DatagramTransport.a aVar) {
        this.a = cls;
        this.b = aVar;
    }

    public void call(b bVar) throws RemoteException {
        if (bVar == null || TextUtils.isEmpty(bVar.a)) {
            a.d("IPCCallback", "In call, URI cannot be empty.");
            throw new RemoteException();
        }
        f a2 = com.huawei.hms.core.aidl.a.a(bVar.c());
        ResponseHeader responseHeader = new ResponseHeader();
        a2.a(bVar.b, (IMessageEntity) responseHeader);
        IMessageEntity iMessageEntity = null;
        if (bVar.b() > 0 && (iMessageEntity = newResponseInstance()) != null) {
            a2.a(bVar.a(), iMessageEntity);
        }
        this.b.a(responseHeader.getStatusCode(), iMessageEntity);
    }

    /* access modifiers changed from: protected */
    public IMessageEntity newResponseInstance() {
        if (this.a == null) {
            return null;
        }
        try {
            return (IMessageEntity) this.a.newInstance();
        } catch (IllegalAccessException | InstantiationException e) {
            a.d("IPCCallback", "In newResponseInstance, instancing exception." + e.getMessage());
            return null;
        }
    }
}
