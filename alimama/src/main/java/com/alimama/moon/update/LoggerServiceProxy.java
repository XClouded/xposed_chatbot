package com.alimama.moon.update;

import android.content.Context;
import android.text.TextUtils;
import com.alibaba.android.common.ServiceProxy;
import com.alibaba.android.common.ServiceProxyBase;

public class LoggerServiceProxy extends ServiceProxyBase {
    public LoggerServiceProxy(Context context) {
        super((ServiceProxy) null);
        setApplicationContext(context);
    }

    /* access modifiers changed from: protected */
    public Object createServiceDelegate(String str) {
        if (TextUtils.equals(str, ServiceProxy.COMMON_SERVICE_LOGGER)) {
            return new UpdateLogger();
        }
        return null;
    }
}
