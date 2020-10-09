package com.huawei.hms.api;

import android.os.Handler;
import android.os.Message;
import com.huawei.hms.support.log.a;

/* compiled from: HuaweiApiClientImpl */
class g implements Handler.Callback {
    final /* synthetic */ HuaweiApiClientImpl a;

    g(HuaweiApiClientImpl huaweiApiClientImpl) {
        this.a = huaweiApiClientImpl;
    }

    public boolean handleMessage(Message message) {
        if (message == null || message.what != 2) {
            return false;
        }
        a.d("HuaweiApiClientImpl", "In connect, bind core service time out");
        if (this.a.k.get() == 5) {
            this.a.a(1);
            this.a.a();
        }
        return true;
    }
}
