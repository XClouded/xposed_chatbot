package com.huawei.hms.update.e;

import android.content.Intent;
import com.huawei.hms.support.log.a;
import com.huawei.hms.update.a.a.b;
import com.huawei.updatesdk.service.otaupdate.CheckUpdateCallBack;

/* compiled from: UpdateWizard */
class y implements CheckUpdateCallBack {
    final /* synthetic */ b a;
    final /* synthetic */ w b;

    public void onMarketInstallInfo(Intent intent) {
    }

    y(w wVar, b bVar) {
        this.b = wVar;
        this.a = bVar;
    }

    public void onUpdateInfo(Intent intent) {
        if (intent != null) {
            this.b.a(intent, this.a);
        }
    }

    public void onMarketStoreError(int i) {
        a.d("UpdateWizard", "onMarketStoreError responseCode: " + i);
    }

    public void onUpdateStoreError(int i) {
        a.d("UpdateWizard", "onUpdateStoreError responseCode: " + i);
    }
}
