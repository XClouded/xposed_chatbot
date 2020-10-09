package com.alipay.sdk.util;

import android.content.ComponentName;
import android.content.ServiceConnection;
import android.os.IBinder;
import com.alipay.android.app.IAlixPay;

class f implements ServiceConnection {
    final /* synthetic */ e a;

    f(e eVar) {
        this.a = eVar;
    }

    public void onServiceDisconnected(ComponentName componentName) {
        IAlixPay unused = this.a.d = null;
    }

    public void onServiceConnected(ComponentName componentName, IBinder iBinder) {
        synchronized (this.a.e) {
            IAlixPay unused = this.a.d = IAlixPay.Stub.asInterface(iBinder);
            this.a.e.notify();
        }
    }
}
