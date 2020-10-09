package com.alipay.sdk.util;

import android.content.Intent;
import android.net.Uri;
import android.os.Bundle;
import android.os.RemoteException;
import com.alipay.android.app.IRemoteServiceCallback;

class h extends IRemoteServiceCallback.Stub {
    final /* synthetic */ e a;

    public boolean isHideLoadingScreen() throws RemoteException {
        return false;
    }

    public void payEnd(boolean z, String str) throws RemoteException {
    }

    h(e eVar) {
        this.a = eVar;
    }

    public void startActivity(String str, String str2, int i, Bundle bundle) throws RemoteException {
        Intent intent = new Intent("android.intent.action.MAIN", (Uri) null);
        if (bundle == null) {
            bundle = new Bundle();
        }
        try {
            bundle.putInt("CallingPid", i);
            intent.putExtras(bundle);
        } catch (Exception unused) {
        }
        intent.setClassName(str, str2);
        if (this.a.c != null) {
            this.a.c.startActivity(intent);
        }
        this.a.g.b();
    }
}
