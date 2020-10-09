package com.huawei.updatesdk;

import android.app.Activity;
import android.content.ActivityNotFoundException;
import android.content.Context;
import android.content.Intent;
import android.os.Bundle;
import android.text.TextUtils;
import android.util.Log;
import android.widget.Toast;
import com.huawei.updatesdk.sdk.a.d.c.b;
import com.huawei.updatesdk.service.a.a;
import com.huawei.updatesdk.service.appmgr.bean.ApkUpgradeInfo;
import com.huawei.updatesdk.service.otaupdate.AppUpdateActivity;
import com.huawei.updatesdk.service.otaupdate.CheckUpdateCallBack;
import com.huawei.updatesdk.service.otaupdate.c;
import com.huawei.updatesdk.service.otaupdate.e;
import com.huawei.updatesdk.support.e.d;
import java.util.concurrent.Executors;

public final class UpdateSdkAPI {
    public static final String TAG = "UpdateSdk";

    public static void checkAppUpdate(Context context, CheckUpdateCallBack checkUpdateCallBack, boolean z, boolean z2) {
        if (context != null) {
            if (b.a(context)) {
                init(context);
                c cVar = new c(context, checkUpdateCallBack, z2);
                cVar.a(z);
                cVar.executeOnExecutor(Executors.newSingleThreadExecutor(), new Void[0]);
                return;
            }
            if (checkUpdateCallBack != null) {
                Intent intent = new Intent();
                intent.putExtra("status", 2);
                checkUpdateCallBack.onUpdateInfo(intent);
            }
            Toast.makeText(context, d.b(context, "upsdk_no_available_network_prompt_toast"), 0).show();
        }
    }

    public static void checkClientOTAUpdate(Context context, CheckUpdateCallBack checkUpdateCallBack, boolean z, int i, boolean z2) {
        if (context != null && b.a(context)) {
            init(context);
            long g = a.a().g();
            long e = a.a().e();
            if (i == 0 || Math.abs(g - e) >= ((long) i)) {
                a.a().b(g);
                c cVar = new c(context, checkUpdateCallBack, z2);
                cVar.b(true);
                cVar.a(z);
                cVar.executeOnExecutor(Executors.newSingleThreadExecutor(), new Void[0]);
            }
        }
    }

    public static void checkTargetAppUpdate(Context context, String str, CheckUpdateCallBack checkUpdateCallBack) {
        if (context == null || TextUtils.isEmpty(str)) {
            if (checkUpdateCallBack != null) {
                Intent intent = new Intent();
                intent.putExtra("status", 1);
                checkUpdateCallBack.onUpdateInfo(intent);
            }
        } else if (b.a(context)) {
            init(context);
            c cVar = new c(context, checkUpdateCallBack, false);
            cVar.a(str);
            cVar.executeOnExecutor(Executors.newSingleThreadExecutor(), new Void[0]);
        } else if (checkUpdateCallBack != null) {
            Intent intent2 = new Intent();
            intent2.putExtra("status", 2);
            checkUpdateCallBack.onUpdateInfo(intent2);
        }
    }

    private static void init(Context context) {
        com.huawei.updatesdk.sdk.service.a.a.a(context);
        com.huawei.updatesdk.sdk.a.d.b.a.a(context);
        com.huawei.updatesdk.service.b.a.a.a();
        Log.i("updatesdk", "UpdateSDK version is: 2.0.5.300");
    }

    public static void releaseCallBack() {
        com.huawei.updatesdk.service.otaupdate.b.a().a((CheckUpdateCallBack) null);
    }

    public static void setServiceZone(String str) {
        e.a().a(str);
    }

    public static void showUpdateDialog(Context context, ApkUpgradeInfo apkUpgradeInfo, boolean z) {
        if (context != null && apkUpgradeInfo != null) {
            Intent intent = new Intent(context, AppUpdateActivity.class);
            Bundle bundle = new Bundle();
            bundle.putSerializable("app_update_parm", apkUpgradeInfo);
            bundle.putSerializable("app_must_btn", Boolean.valueOf(z));
            intent.putExtras(bundle);
            if (!(context instanceof Activity)) {
                intent.setFlags(268435456);
            }
            try {
                context.startActivity(intent);
            } catch (ActivityNotFoundException e) {
                Log.e(TAG, "go AppUpdateActivity error: " + e.toString());
            }
        }
    }
}
