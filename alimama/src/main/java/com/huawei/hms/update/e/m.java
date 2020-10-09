package com.huawei.hms.update.e;

import android.app.Activity;
import android.content.ActivityNotFoundException;
import android.content.Intent;
import android.text.TextUtils;
import android.view.KeyEvent;
import com.huawei.hms.support.log.a;

/* compiled from: HiappWizard */
public class m extends a {
    public int c() {
        return 2005;
    }

    public void onBridgeActivityCreate(Activity activity) {
        super.onBridgeActivityCreate(activity);
        if (this.c != null) {
            this.f = 5;
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

    public boolean onBridgeActivityResult(int i, int i2, Intent intent) {
        if (this.e && this.b != null) {
            return this.b.onBridgeActivityResult(i, i2, intent);
        }
        if (this.f != 5 || i != c()) {
            return false;
        }
        if (a(this.g, this.i)) {
            b(0, this.f);
            return true;
        }
        b(8, this.f);
        return true;
    }

    public void onBridgeConfigurationChanged() {
        super.onBridgeConfigurationChanged();
    }

    public void a(b bVar) {
        a.b("HiappWizard", "Enter onCancel.");
        if (bVar instanceof n) {
            d();
        }
    }

    public void b(b bVar) {
        a.b("HiappWizard", "Enter onDoWork.");
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
            Intent intent = new Intent();
            intent.setAction("com.huawei.appmarket.intent.action.AppDetail");
            intent.putExtra("APP_PACKAGENAME", this.g);
            intent.setPackage("com.huawei.appmarket");
            a.startActivityForResult(intent, c());
            return true;
        } catch (ActivityNotFoundException unused) {
            a.d("HiappWizard", "can not open hiapp");
            return false;
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
            a.d("HiappWizard", "In showDialog, Failed to show the dialog." + e.getMessage());
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
            a.b("HiappWizard", "In onKeyUp, Call finish.");
            Activity a = a();
            if (a != null && !a.isFinishing()) {
                a.setResult(0, (Intent) null);
                a.finish();
            }
        }
    }
}
