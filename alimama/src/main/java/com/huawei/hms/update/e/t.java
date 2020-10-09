package com.huawei.hms.update.e;

import android.app.Activity;
import android.content.ActivityNotFoundException;
import android.content.BroadcastReceiver;
import android.content.Intent;
import android.content.IntentFilter;
import android.os.Bundle;
import android.os.Handler;
import android.text.TextUtils;
import android.view.KeyEvent;
import com.huawei.hms.c.h;
import com.vivo.push.PushClientConstants;
import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

/* compiled from: SilentUpdateWizard */
public class t extends a {
    private BroadcastReceiver k;
    private Handler l = new Handler();
    private int m = 0;
    private Handler n = new u(this);

    public int c() {
        return 2000;
    }

    public void onBridgeActivityCreate(Activity activity) {
        super.onBridgeActivityCreate(activity);
        if (this.c != null) {
            this.f = 0;
            if (a(activity)) {
                return;
            }
            if (!a(true)) {
                b(8, this.f);
            } else {
                a(8, this.f);
            }
        }
    }

    public void onBridgeActivityDestroy() {
        this.l.removeCallbacksAndMessages((Object) null);
        f();
        super.onBridgeActivityDestroy();
    }

    public boolean onBridgeActivityResult(int i, int i2, Intent intent) {
        if (this.e && this.b != null) {
            return this.b.onBridgeActivityResult(i, i2, intent);
        }
        com.huawei.hms.support.log.a.b("SilentUpdateWizard", "onBridgeActivityResult requestCode is " + i + "resultCode is " + i2);
        if (i != c()) {
            return false;
        }
        if (i2 == 0) {
            e();
            b(20000);
            return true;
        } else if (i2 == 4) {
            d();
            return true;
        } else {
            if (!a(true)) {
                b(i2, this.f);
            } else {
                a(i2, this.f);
            }
            return true;
        }
    }

    private boolean a(Activity activity) {
        if (TextUtils.isEmpty(this.g)) {
            return false;
        }
        Intent intent = new Intent("com.huawei.appmarket.intent.action.ThirdUpdateAction");
        intent.setPackage("com.huawei.appmarket");
        JSONArray jSONArray = new JSONArray();
        JSONObject jSONObject = new JSONObject();
        try {
            jSONObject.put(PushClientConstants.TAG_PKG_NAME, this.g);
            jSONObject.put("versioncode", this.i);
            jSONArray.put(jSONObject);
            intent.putExtra("params", jSONArray.toString());
            intent.putExtra("isHmsOrApkUpgrade", this.c.a());
            intent.putExtra("buttonDlgY", h.d("hms_install"));
            intent.putExtra("buttonDlgN", h.d("hms_cancel"));
            intent.putExtra("upgradeDlgContent", h.a("hms_update_message_new", "%P"));
            try {
                activity.startActivityForResult(intent, c());
                return true;
            } catch (ActivityNotFoundException unused) {
                com.huawei.hms.support.log.a.d("SilentUpdateWizard", "ActivityNotFoundException");
                return false;
            }
        } catch (JSONException e) {
            com.huawei.hms.support.log.a.d("SilentUpdateWizard", "create hmsJsonObject fail" + e.getMessage());
            return false;
        }
    }

    public void onBridgeConfigurationChanged() {
        super.onBridgeConfigurationChanged();
    }

    private void e() {
        IntentFilter intentFilter = new IntentFilter();
        intentFilter.addAction("com.huawei.appmarket.service.downloadservice.Receiver");
        intentFilter.addAction("com.huawei.appmarket.service.downloadservice.progress.Receiver");
        intentFilter.addAction("com.huawei.appmarket.service.installerservice.Receiver");
        this.k = new com.huawei.hms.update.d.a(this.n);
        Activity a2 = a();
        if (a2 != null) {
            a2.registerReceiver(this.k, intentFilter);
        }
    }

    private void f() {
        Activity a2 = a();
        if (a2 != null && this.k != null) {
            a2.unregisterReceiver(this.k);
            this.k = null;
        }
    }

    private void b(int i) {
        this.l.removeCallbacksAndMessages((Object) null);
        this.l.postDelayed(new a(this, (u) null), (long) i);
    }

    /* access modifiers changed from: private */
    public void c(int i) {
        this.l.removeCallbacksAndMessages((Object) null);
        f();
        b();
        if (!a(false)) {
            b(i, this.f);
        } else {
            a(i, this.f);
        }
    }

    public void onKeyUp(int i, KeyEvent keyEvent) {
        super.onKeyUp(i, keyEvent);
    }

    /* access modifiers changed from: private */
    public void a(Bundle bundle) {
        String string = bundle.containsKey("UpgradePkgName") ? bundle.getString("UpgradePkgName") : null;
        if (string != null && string.equals(this.g) && bundle.containsKey("downloadtask.status")) {
            int i = bundle.getInt("downloadtask.status");
            com.huawei.hms.support.log.a.b("SilentUpdateWizard", "handleDownloadStatus-status is " + i);
            if (i == 3 || i == 5 || i == 6 || i == 8) {
                c(i);
            } else if (i == 4) {
                b(60000);
            } else {
                b(20000);
            }
        }
    }

    /* access modifiers changed from: private */
    public void b(Bundle bundle) {
        String string = bundle.containsKey("UpgradePkgName") ? bundle.getString("UpgradePkgName") : null;
        if (string != null && string.equals(this.g) && bundle.containsKey("UpgradeDownloadProgress") && bundle.containsKey("UpgradeAppName")) {
            int i = bundle.getInt("UpgradeDownloadProgress");
            b(20000);
            if (i >= 99) {
                i = 99;
            }
            this.m = i;
            if (this.d == null) {
                a((Class<? extends b>) i.class);
            }
            if (this.d != null) {
                ((i) this.d).b(i);
            }
        }
    }

    /* access modifiers changed from: private */
    public void c(Bundle bundle) {
        if (bundle.containsKey("packagename") && bundle.containsKey("status")) {
            String string = bundle.getString("packagename");
            int i = bundle.getInt("status");
            com.huawei.hms.support.log.a.b("SilentUpdateWizard", "handlerInstallStatus-status is " + i);
            if (string != null && string.equals(this.g)) {
                if (i == 2) {
                    this.l.removeCallbacksAndMessages((Object) null);
                    if (this.d != null) {
                        ((i) this.d).b(100);
                    }
                    b(0, this.f);
                } else if (i == -1 || i == -2) {
                    c(i);
                } else {
                    b(60000);
                }
            }
        }
    }

    /* access modifiers changed from: package-private */
    public void a(Class<? extends b> cls) {
        try {
            b bVar = (b) cls.newInstance();
            if (this.m > 0 && (bVar instanceof i)) {
                ((i) bVar).a(this.m);
            }
            bVar.a((a) this);
            this.d = bVar;
        } catch (IllegalAccessException | IllegalStateException | InstantiationException e) {
            com.huawei.hms.support.log.a.d("SilentUpdateWizard", "In showDialog, Failed to show the dialog." + e.getMessage());
        }
    }

    /* access modifiers changed from: package-private */
    public void d() {
        b(13, this.f);
    }

    /* compiled from: SilentUpdateWizard */
    private class a implements Runnable {
        private a() {
        }

        /* synthetic */ a(t tVar, u uVar) {
            this();
        }

        public void run() {
            t.this.c(14);
        }
    }
}
