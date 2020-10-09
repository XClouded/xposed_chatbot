package com.huawei.hms.update.e;

import android.app.Activity;
import android.content.ActivityNotFoundException;
import android.content.Intent;
import android.net.Uri;
import android.text.TextUtils;
import android.view.KeyEvent;
import com.huawei.hms.support.log.a;

/* compiled from: GooglePlayWizard */
public class k extends a {
    public int c() {
        return 2002;
    }

    public void onBridgeActivityCreate(Activity activity) {
        super.onBridgeActivityCreate(activity);
        if (this.c != null) {
            this.f = 2;
            if (this.c.g() && !TextUtils.isEmpty(this.h)) {
                a((Class<? extends b>) n.class);
            } else if (e()) {
            } else {
                if (!a(false)) {
                    b(8, this.f);
                } else {
                    a(8, this.f);
                }
            }
        }
    }

    public void onBridgeActivityDestroy() {
        super.onBridgeActivityDestroy();
    }

    public void onBridgeConfigurationChanged() {
        super.onBridgeConfigurationChanged();
    }

    public boolean onBridgeActivityResult(int i, int i2, Intent intent) {
        if (this.e && this.b != null) {
            return this.b.onBridgeActivityResult(i, i2, intent);
        }
        if (this.f != 2 || i != c()) {
            return false;
        }
        if (a(this.g, this.i)) {
            b(0, this.f);
            return true;
        }
        b(8, this.f);
        return true;
    }

    public void a(b bVar) {
        a.b("GooglePlayWizard", "Enter onCancel.");
        if (bVar instanceof n) {
            d();
        }
    }

    public void b(b bVar) {
        a.b("GooglePlayWizard", "Enter onDoWork.");
        if (bVar instanceof n) {
            bVar.c();
            if (e()) {
                return;
            }
            if (!a(false)) {
                b(8, this.f);
            } else {
                a(8, this.f);
            }
        }
    }

    private boolean e() {
        Activity a = a();
        if (a == null || a.isFinishing() || TextUtils.isEmpty(this.g)) {
            return false;
        }
        try {
            Intent intent = new Intent("android.intent.action.VIEW", Uri.parse("market://details?id=" + this.g));
            intent.setPackage("com.android.vending");
            a.startActivityForResult(intent, c());
            return true;
        } catch (ActivityNotFoundException unused) {
            a.d("GooglePlayWizard", "can not open google play");
            return false;
        }
    }

    /* access modifiers changed from: package-private */
    public void d() {
        b(13, this.f);
    }

    public void onKeyUp(int i, KeyEvent keyEvent) {
        if (this.e && this.b != null) {
            this.b.onKeyUp(i, keyEvent);
        } else if (4 == i) {
            a.b("GooglePlayWizard", "In onKeyUp, Call finish.");
            Activity a = a();
            if (a != null && !a.isFinishing()) {
                a.setResult(0, (Intent) null);
                a.finish();
            }
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
            bVar.a((a) this);
            this.d = bVar;
        } catch (IllegalAccessException | IllegalStateException | InstantiationException e) {
            a.d("GooglePlayWizard", "In showDialog, Failed to show the dialog." + e.getMessage());
        }
    }
}
