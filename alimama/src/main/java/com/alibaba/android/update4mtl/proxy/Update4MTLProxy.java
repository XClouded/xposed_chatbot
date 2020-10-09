package com.alibaba.android.update4mtl.proxy;

import android.content.Context;
import android.text.TextUtils;
import com.alibaba.android.common.ServiceProxy;
import com.alibaba.android.common.ServiceProxyBase;
import com.alibaba.android.update4mtl.Constants;

public class Update4MTLProxy extends ServiceProxyBase {
    public Update4MTLProxy(Context context) {
        super((ServiceProxy) null);
        setApplicationContext(context);
    }

    /* access modifiers changed from: protected */
    public Object createServiceDelegate(String str) {
        if (TextUtils.equals(str, Constants.UTIL_SERVICE)) {
            return new UtilService();
        }
        return null;
    }
}
