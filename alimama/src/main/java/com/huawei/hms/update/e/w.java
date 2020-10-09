package com.huawei.hms.update.e;

import android.app.Activity;
import android.content.ActivityNotFoundException;
import android.content.Context;
import android.content.Intent;
import android.net.Uri;
import android.os.Build;
import android.os.Handler;
import android.os.Looper;
import android.text.TextUtils;
import android.view.KeyEvent;
import com.alibaba.wireless.security.SecExceptionCode;
import com.huawei.hms.c.g;
import com.huawei.hms.update.a.a.a;
import com.huawei.hms.update.a.a.b;
import com.huawei.hms.update.a.a.c;
import com.huawei.hms.update.a.a.d;
import com.huawei.hms.update.a.h;
import com.huawei.hms.update.e.e;
import com.huawei.hms.update.e.q;
import com.huawei.hms.update.provider.UpdateProvider;
import com.huawei.updatesdk.UpdateSdkAPI;
import com.huawei.updatesdk.service.appmgr.bean.ApkUpgradeInfo;
import com.huawei.updatesdk.service.otaupdate.UpdateKey;
import java.io.File;
import java.io.Serializable;

/* compiled from: UpdateWizard */
public class w extends a implements b {
    private a k;
    private c l;
    private int m = 0;

    public int c() {
        return 2006;
    }

    private static Uri a(Context context, File file) {
        g gVar = new g(context);
        String packageName = context.getPackageName();
        String str = packageName + UpdateProvider.AUTHORITIES_SUFFIX;
        boolean z = true;
        if (Build.VERSION.SDK_INT <= 23 || (context.getApplicationInfo().targetSdkVersion <= 23 && !gVar.a(packageName, str))) {
            z = false;
        }
        if (z) {
            return UpdateProvider.getUriForFile(context, str, file);
        }
        return Uri.fromFile(file);
    }

    private static void a(b bVar, int i, c cVar) {
        if (bVar != null) {
            new Handler(Looper.getMainLooper()).post(new x(bVar, i, cVar));
        }
    }

    public void onBridgeActivityCreate(Activity activity) {
        super.onBridgeActivityCreate(activity);
        if (this.c != null) {
            this.f = 6;
            if (!this.c.g() || TextUtils.isEmpty(this.h)) {
                a((Class<? extends b>) d.class);
                a((b) this);
                return;
            }
            a((Class<? extends b>) n.class);
        }
    }

    public void onBridgeActivityDestroy() {
        g();
        super.onBridgeActivityDestroy();
    }

    public boolean onBridgeActivityResult(int i, int i2, Intent intent) {
        if (this.e && this.b != null) {
            return this.b.onBridgeActivityResult(i, i2, intent);
        }
        if (this.f != 6 || i != c()) {
            return false;
        }
        if (a(this.g, this.i)) {
            b(0, this.f);
            return true;
        }
        e();
        return true;
    }

    public void a(int i, c cVar) {
        com.huawei.hms.support.log.a.b("UpdateWizard", "Enter onCheckUpdate, status: " + d.a(i));
        if (i != 1000) {
            switch (i) {
                case 1201:
                case 1202:
                case 1203:
                    a((Class<? extends b>) q.b.class);
                    return;
                default:
                    a((Class<? extends b>) q.b.class);
                    return;
            }
        } else {
            this.l = cVar;
            a((Class<? extends b>) i.class);
            f();
        }
    }

    public void a(int i, int i2, int i3, File file) {
        com.huawei.hms.support.log.a.b("UpdateWizard", "Enter onDownloadPackage, status: " + d.a(i) + ", reveived: " + i2 + ", total: " + i3);
        if (i != 2000) {
            switch (i) {
                case 2100:
                    if (this.d != null && (this.d instanceof i)) {
                        int i4 = 0;
                        if (i2 >= 0 && i3 > 0) {
                            i4 = (int) ((((long) i2) * 100) / ((long) i3));
                        }
                        this.m = i4;
                        ((i) this.d).b(i4);
                        return;
                    }
                    return;
                case 2101:
                    return;
                default:
                    switch (i) {
                        case SecExceptionCode.SEC_ERROR_LBSRISK_INVALID_PARAM:
                            a((Class<? extends b>) q.c.class);
                            return;
                        case SecExceptionCode.SEC_ERROR_LBSRISK_INVALID_LOCATION_SET:
                            a((Class<? extends b>) e.b.class);
                            return;
                        case SecExceptionCode.SEC_ERROR_LBSRISK_NO_MEMORY:
                        case SecExceptionCode.SEC_ERROR_LBSRISK_NOT_INIT:
                            a((Class<? extends b>) q.d.class);
                            return;
                        default:
                            return;
                    }
            }
        } else {
            b();
            if (file == null) {
                e();
            } else {
                a(file);
            }
        }
    }

    private void a(File file) {
        Activity a = a();
        if (a != null && !a.isFinishing()) {
            Uri a2 = a((Context) a, file);
            if (a2 == null) {
                com.huawei.hms.support.log.a.d("UpdateWizard", "In startInstaller, Failed to creates a Uri from a file.");
                e();
                return;
            }
            Intent intent = new Intent("android.intent.action.VIEW");
            intent.setDataAndType(a2, "application/vnd.android.package-archive");
            intent.setFlags(3);
            intent.putExtra("android.intent.extra.NOT_UNKNOWN_SOURCE", true);
            try {
                a.startActivityForResult(intent, c());
            } catch (ActivityNotFoundException e) {
                com.huawei.hms.support.log.a.d("UpdateWizard", "In startInstaller, Failed to start package installer." + e.getMessage());
                e();
            }
        }
    }

    public void a(b bVar) {
        com.huawei.hms.support.log.a.b("UpdateWizard", "Enter onCancel.");
        if (bVar instanceof n) {
            d();
        } else if (bVar instanceof d) {
            g();
            d();
        } else if (bVar instanceof i) {
            g();
            a((Class<? extends b>) e.c.class);
        } else if (bVar instanceof e.c) {
            a((Class<? extends b>) i.class);
            f();
        } else if (bVar instanceof e.b) {
            d();
        } else {
            e();
        }
    }

    public void b(b bVar) {
        com.huawei.hms.support.log.a.b("UpdateWizard", "Enter onDoWork.");
        if (bVar instanceof n) {
            bVar.c();
            a((Class<? extends b>) d.class);
            a((b) this);
        } else if (bVar instanceof e.c) {
            bVar.c();
            d();
        } else if (bVar instanceof e.b) {
            a((Class<? extends b>) i.class);
            f();
        } else if (bVar instanceof q.b) {
            e();
        } else if (bVar instanceof q.c) {
            e();
        } else if (bVar instanceof q.d) {
            e();
        }
    }

    private void e() {
        if (!a(false)) {
            b(8, this.f);
        } else {
            a(8, this.f);
        }
    }

    /* access modifiers changed from: package-private */
    public void a(Class<? extends b> cls) {
        b();
        try {
            b bVar = (b) cls.newInstance();
            if (!TextUtils.isEmpty(this.h) && (bVar instanceof n)) {
                ((n) bVar).a(this.h);
            }
            if (this.m > 0 && (bVar instanceof i)) {
                ((i) bVar).a(this.m);
            }
            bVar.a((a) this);
            this.d = bVar;
        } catch (IllegalAccessException | IllegalStateException | InstantiationException e) {
            com.huawei.hms.support.log.a.d("UpdateWizard", "In showDialog, Failed to show the dialog." + e.getMessage());
        }
    }

    /* access modifiers changed from: package-private */
    public void d() {
        b(13, this.f);
    }

    private void a(b bVar) {
        if (bVar != null) {
            Activity a = a();
            if (a == null || a.isFinishing()) {
                a(bVar, 1201, (c) null);
            } else {
                UpdateSdkAPI.checkTargetAppUpdate(a, this.c.b(), new y(this, bVar));
            }
        }
    }

    /* access modifiers changed from: private */
    public void a(Intent intent, b bVar) {
        try {
            int intExtra = intent.getIntExtra("status", -99);
            com.huawei.hms.support.log.a.b("UpdateWizard", "CheckUpdateCallBack status is " + intExtra);
            String stringExtra = intent.getStringExtra(UpdateKey.FAIL_REASON);
            if (!TextUtils.isEmpty(stringExtra)) {
                com.huawei.hms.support.log.a.d("UpdateWizard", "checkTargetAppUpdate reason is " + stringExtra);
            }
            if (intExtra == 7) {
                Serializable serializableExtra = intent.getSerializableExtra(UpdateKey.INFO);
                if (serializableExtra != null && (serializableExtra instanceof ApkUpgradeInfo)) {
                    ApkUpgradeInfo apkUpgradeInfo = (ApkUpgradeInfo) serializableExtra;
                    String package_ = apkUpgradeInfo.getPackage_();
                    int versionCode_ = apkUpgradeInfo.getVersionCode_();
                    String downurl_ = apkUpgradeInfo.getDownurl_();
                    int size_ = apkUpgradeInfo.getSize_();
                    String sha256_ = apkUpgradeInfo.getSha256_();
                    if (TextUtils.isEmpty(package_) || !package_.equals(this.c.b())) {
                        a(bVar, 1201, (c) null);
                    } else if (versionCode_ < this.c.c()) {
                        com.huawei.hms.support.log.a.d("UpdateWizard", "CheckUpdateCallBack versionCode is " + versionCode_ + "bean.getClientVersionCode() is " + this.c.c());
                        a(bVar, 1203, (c) null);
                    } else if (TextUtils.isEmpty(downurl_) || TextUtils.isEmpty(sha256_)) {
                        a(bVar, 1201, (c) null);
                    } else {
                        a(bVar, 1000, new c(package_, versionCode_, downurl_, size_, sha256_));
                    }
                }
            } else if (intExtra == 3) {
                a(bVar, 1202, (c) null);
            } else {
                a(bVar, 1201, (c) null);
            }
        } catch (Exception e) {
            com.huawei.hms.support.log.a.d("UpdateWizard", "intent has some error" + e.getMessage());
            a(bVar, 1201, (c) null);
        }
    }

    private void f() {
        Activity a = a();
        if (a == null || a.isFinishing()) {
            a((Class<? extends b>) q.c.class);
            return;
        }
        g();
        this.k = new com.huawei.hms.update.a.c(new h(a));
        this.k.a(this, this.l);
    }

    private void g() {
        if (this.k != null) {
            this.k.a();
            this.k = null;
        }
    }

    public void onKeyUp(int i, KeyEvent keyEvent) {
        if (this.e && this.b != null) {
            this.b.onKeyUp(i, keyEvent);
        } else if (4 == i) {
            com.huawei.hms.support.log.a.b("UpdateWizard", "In onKeyUp, Call finish.");
            Activity a = a();
            if (a != null && !a.isFinishing()) {
                a.setResult(0, (Intent) null);
                a.finish();
            }
        }
    }
}
