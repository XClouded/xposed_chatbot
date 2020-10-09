package com.huawei.updatesdk.service.otaupdate;

import android.app.Activity;
import android.content.ActivityNotFoundException;
import android.content.Context;
import android.content.Intent;
import android.content.pm.ApplicationInfo;
import android.content.pm.PackageInfo;
import android.os.AsyncTask;
import android.os.Build;
import android.os.Bundle;
import android.text.TextUtils;
import android.widget.Toast;
import com.huawei.updatesdk.sdk.service.c.a.d;
import com.huawei.updatesdk.service.appmgr.bean.ApkUpgradeInfo;
import com.huawei.updatesdk.service.b.a.b;
import com.huawei.updatesdk.support.c.a;
import java.util.ArrayList;
import java.util.List;

public class c extends AsyncTask<Void, Void, d> {
    private Context a;
    private CheckUpdateCallBack b;
    private boolean c = false;
    private boolean d = false;
    private String e = null;
    private String f = null;
    private Toast g;
    private boolean h = false;

    public c(Context context, CheckUpdateCallBack checkUpdateCallBack, boolean z) {
        this.b = checkUpdateCallBack;
        this.a = context;
        this.d = z;
    }

    private d a(Context context, String str) {
        PackageInfo a2 = a.a(str, context);
        if (a2 == null && this.e == null) {
            com.huawei.updatesdk.sdk.a.c.a.a.a.d("CheckOtaAndUpdataTask", "get app packageInfo failed,packageName:" + str);
            return null;
        }
        if (a2 == null) {
            a2 = new PackageInfo();
            a2.packageName = str;
            a2.versionName = "1.0";
            a2.versionCode = 1;
            ApplicationInfo applicationInfo = new ApplicationInfo();
            applicationInfo.targetSdkVersion = 19;
            a2.applicationInfo = applicationInfo;
        }
        ArrayList arrayList = new ArrayList();
        arrayList.add(a2);
        com.huawei.updatesdk.service.appmgr.bean.a a3 = com.huawei.updatesdk.service.appmgr.bean.a.a((List<PackageInfo>) arrayList);
        a3.a(0);
        return b.a(a3);
    }

    private ApkUpgradeInfo a(String str, List<ApkUpgradeInfo> list) {
        if (list == null || TextUtils.isEmpty(str)) {
            return null;
        }
        for (ApkUpgradeInfo next : list) {
            if (str.equals(next.getPackage_()) && next.getOldVersionCode_() < next.getVersionCode_()) {
                return next;
            }
        }
        return null;
    }

    private void a() {
        if (this.g != null) {
            this.g.cancel();
        }
    }

    private void a(Context context, ApkUpgradeInfo apkUpgradeInfo) {
        if (context != null) {
            Intent intent = new Intent(context, AppUpdateActivity.class);
            Bundle bundle = new Bundle();
            bundle.putSerializable("app_update_parm", apkUpgradeInfo);
            bundle.putSerializable("app_must_btn", Boolean.valueOf(this.d));
            intent.putExtras(bundle);
            if (!(context instanceof Activity)) {
                intent.setFlags(268435456);
            }
            try {
                context.startActivity(intent);
            } catch (ActivityNotFoundException e2) {
                com.huawei.updatesdk.sdk.a.c.a.a.a.d("CheckOtaAndUpdataTask", "go AppUpdateActivity error: " + e2.toString());
            }
        }
    }

    private void b() {
        StringBuilder sb = new StringBuilder();
        sb.append(com.huawei.updatesdk.sdk.a.d.b.a.c());
        sb.append(Build.VERSION.SDK_INT);
        sb.append(Build.MODEL);
        sb.append(com.huawei.updatesdk.sdk.a.d.b.a.b());
        sb.append(e.a().c());
        sb.append(com.huawei.updatesdk.support.e.c.a().b());
        boolean z = !TextUtils.equals(sb.toString(), com.huawei.updatesdk.service.a.a.a().c());
        if (z) {
            com.huawei.updatesdk.service.a.a.a().a(sb.toString());
        }
        long g2 = com.huawei.updatesdk.service.a.a.a().g();
        long b2 = com.huawei.updatesdk.service.a.a.a().b();
        if (z || TextUtils.isEmpty(e.a().c()) || Math.abs(g2 - b2) >= 7 || TextUtils.isEmpty(com.huawei.updatesdk.service.a.a.a().d())) {
            d a2 = b.a(com.huawei.updatesdk.a.a.a.a.f());
            if ((a2 instanceof com.huawei.updatesdk.a.a.a.b) && a2.d() == 0 && a2.c() == 0) {
                com.huawei.updatesdk.service.a.a.a().b(((com.huawei.updatesdk.a.a.a.b) a2).a());
            }
        }
    }

    private void b(d dVar) {
        if (this.b != null) {
            Intent intent = new Intent();
            intent.putExtra("status", 6);
            if (dVar.e() != null) {
                intent.putExtra(UpdateKey.FAIL_CODE, dVar.e().ordinal());
            }
            intent.putExtra(UpdateKey.FAIL_REASON, dVar.f());
            intent.putExtra(UpdateKey.REQUEST_SIGN, com.huawei.updatesdk.service.a.a.a().d());
            this.b.onUpdateInfo(intent);
            this.b.onUpdateStoreError(dVar.c());
        }
    }

    /* access modifiers changed from: protected */
    /* renamed from: a */
    public d doInBackground(Void... voidArr) {
        com.huawei.updatesdk.sdk.a.c.a.a.a.a("CheckOtaAndUpdataTask", "CheckOtaAndUpdataTask doInBackground");
        if (Build.VERSION.SDK_INT >= 24 || com.huawei.updatesdk.sdk.a.d.b.a.g() == 0 || a.a(com.huawei.updatesdk.sdk.service.a.a.a().b(), "com.huawei.appmarket") != a.C0019a.NOT_INSTALLED || a.c()) {
            if (TextUtils.isEmpty(e.a().b())) {
                e.a().a(this.a);
            }
            this.f = this.e;
            if (TextUtils.isEmpty(this.e)) {
                this.f = this.a.getPackageName();
            }
            com.huawei.updatesdk.service.a.a a2 = com.huawei.updatesdk.service.a.a.a();
            a2.c("updatesdk_" + this.f);
            b();
            return a(this.a, this.f);
        }
        com.huawei.updatesdk.sdk.a.c.a.a.a.a("CheckOtaAndUpdataTask", "api <24,HiApp not installed and no permission");
        return null;
    }

    /* access modifiers changed from: protected */
    /* renamed from: a */
    public void onPostExecute(d dVar) {
        a();
        if (dVar != null) {
            ApkUpgradeInfo apkUpgradeInfo = null;
            if (dVar.c() == 0 && dVar.d() == 0) {
                apkUpgradeInfo = a(this.f, ((com.huawei.updatesdk.service.appmgr.bean.b) dVar).list_);
                if (apkUpgradeInfo == null && this.b != null) {
                    Intent intent = new Intent();
                    intent.putExtra("status", 3);
                    intent.putExtra(UpdateKey.REQUEST_SIGN, com.huawei.updatesdk.service.a.a.a().d());
                    this.b.onUpdateInfo(intent);
                }
            } else {
                b(dVar);
                com.huawei.updatesdk.sdk.a.c.a.a.a.d("CheckOtaAndUpdataTask", "get app update msg failed,responseCode is " + dVar.c());
            }
            if (apkUpgradeInfo != null && !TextUtils.isEmpty(apkUpgradeInfo.getFullDownUrl_())) {
                apkUpgradeInfo.setDownurl_(apkUpgradeInfo.getFullDownUrl_());
            }
            if (apkUpgradeInfo != null) {
                com.huawei.updatesdk.sdk.a.c.a.a.a.a("CheckOtaAndUpdataTask", "check store client update success!" + apkUpgradeInfo.getVersionCode_() + ",version:" + apkUpgradeInfo.getVersion_());
                if (this.b != null) {
                    Intent intent2 = new Intent();
                    intent2.putExtra(UpdateKey.INFO, apkUpgradeInfo);
                    intent2.putExtra("status", 7);
                    intent2.putExtra(UpdateKey.REQUEST_SIGN, com.huawei.updatesdk.service.a.a.a().d());
                    this.b.onUpdateInfo(intent2);
                }
                if (this.c) {
                    a(this.a, apkUpgradeInfo);
                }
            } else if (!this.h && TextUtils.isEmpty(this.e)) {
                Toast.makeText(this.a, com.huawei.updatesdk.support.e.d.b(this.a, "upsdk_update_check_no_new_version"), 0).show();
            }
        }
    }

    public void a(String str) {
        this.e = str;
    }

    public void a(boolean z) {
        this.c = z;
    }

    public void b(boolean z) {
        this.h = z;
    }

    /* access modifiers changed from: protected */
    public void onPreExecute() {
        super.onPreExecute();
        b.a().a(this.b);
        if (!this.h && TextUtils.isEmpty(this.e)) {
            this.g = Toast.makeText(this.a, com.huawei.updatesdk.support.e.d.b(this.a, "upsdk_checking_update_prompt"), 1);
            this.g.show();
        }
    }
}
