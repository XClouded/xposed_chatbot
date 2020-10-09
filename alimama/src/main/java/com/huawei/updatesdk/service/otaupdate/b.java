package com.huawei.updatesdk.service.otaupdate;

import android.content.Intent;

public class b {
    private static b a;
    private static final Object c = new Object();
    private CheckUpdateCallBack b;

    public static b a() {
        b bVar;
        synchronized (c) {
            if (a == null) {
                a = new b();
            }
            bVar = a;
        }
        return bVar;
    }

    public void a(int i) {
        if (this.b != null) {
            this.b.onMarketStoreError(i);
        }
    }

    public void a(Intent intent) {
        if (this.b != null) {
            this.b.onMarketInstallInfo(intent);
        }
    }

    public void a(CheckUpdateCallBack checkUpdateCallBack) {
        this.b = checkUpdateCallBack;
    }

    public void b(Intent intent) {
        if (this.b != null) {
            this.b.onUpdateInfo(intent);
        }
    }
}
