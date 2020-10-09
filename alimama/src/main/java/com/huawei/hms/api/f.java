package com.huawei.hms.api;

import android.content.Intent;
import com.huawei.hms.support.log.a;
import com.huawei.updatesdk.service.appmgr.bean.ApkUpgradeInfo;
import com.huawei.updatesdk.service.otaupdate.CheckUpdateCallBack;
import com.huawei.updatesdk.service.otaupdate.UpdateKey;

/* compiled from: HuaweiApiClientImpl */
class f implements CheckUpdateCallBack {
    final /* synthetic */ HuaweiApiClientImpl a;

    f(HuaweiApiClientImpl huaweiApiClientImpl) {
        this.a = huaweiApiClientImpl;
    }

    public void onUpdateInfo(Intent intent) {
        if (intent != null && this.a.u != null) {
            try {
                int intExtra = intent.getIntExtra("status", -99);
                int intExtra2 = intent.getIntExtra(UpdateKey.FAIL_CODE, -99);
                boolean booleanExtra = intent.getBooleanExtra(UpdateKey.MUST_UPDATE, false);
                a.b("HuaweiApiClientImpl", "onUpdateInfo status: " + intExtra + ",failcause: " + intExtra2 + ",isExit: " + booleanExtra);
                if (intExtra == 7) {
                    ApkUpgradeInfo apkUpgradeInfo = (ApkUpgradeInfo) intent.getSerializableExtra(UpdateKey.INFO);
                    if (apkUpgradeInfo != null) {
                        a.b("HuaweiApiClientImpl", "onUpdateInfo: " + apkUpgradeInfo.toString());
                    }
                    this.a.u.onResult(1);
                } else if (intExtra == 3) {
                    this.a.u.onResult(0);
                } else {
                    this.a.u.onResult(-1);
                }
                CheckUpdatelistener unused = this.a.u = null;
            } catch (Exception e) {
                a.d("HuaweiApiClientImpl", "intent has some error" + e.getMessage());
                this.a.u.onResult(-1);
            }
        }
    }

    public void onMarketInstallInfo(Intent intent) {
        if (intent != null) {
            int intExtra = intent.getIntExtra(UpdateKey.MARKET_DLD_STATUS, -99);
            int intExtra2 = intent.getIntExtra(UpdateKey.MARKET_INSTALL_STATE, -99);
            int intExtra3 = intent.getIntExtra(UpdateKey.MARKET_INSTALL_TYPE, -99);
            a.b("HuaweiApiClientImpl", "onMarketInstallInfo installState: " + intExtra2 + ",installType: " + intExtra3 + ",downloadCode: " + intExtra);
        }
    }

    public void onMarketStoreError(int i) {
        a.d("HuaweiApiClientImpl", "onMarketStoreError responseCode: " + i);
    }

    public void onUpdateStoreError(int i) {
        a.d("HuaweiApiClientImpl", "onUpdateStoreError responseCode: " + i);
    }
}
