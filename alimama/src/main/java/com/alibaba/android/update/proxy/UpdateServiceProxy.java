package com.alibaba.android.update.proxy;

import android.content.Context;
import android.text.TextUtils;
import com.alibaba.android.common.ServiceProxy;
import com.alibaba.android.common.ServiceProxyBase;
import com.alibaba.android.update.state.DownloadedState;
import com.alibaba.android.update.state.DownloadingState;
import com.alibaba.android.update.state.UndownloadState;

public class UpdateServiceProxy extends ServiceProxyBase {
    public UpdateServiceProxy(Context context) {
        super((ServiceProxy) null);
        setApplicationContext(context);
    }

    /* access modifiers changed from: protected */
    public Object createServiceDelegate(String str) {
        if (TextUtils.equals(str, Constants.UNDOWNLOAD_STATE_SERVICE)) {
            return new UndownloadState();
        }
        if (TextUtils.equals(str, Constants.DOWNLOADING_STATE_SERVICE)) {
            return new DownloadingState();
        }
        if (TextUtils.equals(str, Constants.DOWNLOADED_STATE_SERVICE)) {
            return new DownloadedState();
        }
        return null;
    }
}
