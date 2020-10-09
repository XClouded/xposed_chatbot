package com.huawei.hms.update.e;

import android.app.Activity;
import android.content.ActivityNotFoundException;
import android.content.Intent;
import android.net.Uri;
import android.text.TextUtils;
import android.view.KeyEvent;
import com.huawei.hms.support.log.a;

/* compiled from: HiappWebWizard */
public class l extends a {
    public int c() {
        return 2004;
    }

    public boolean onBridgeActivityResult(int i, int i2, Intent intent) {
        return false;
    }

    public void onBridgeActivityCreate(Activity activity) {
        super.onBridgeActivityCreate(activity);
        if (this.c != null) {
            this.f = 4;
            if (this.c.g() && !TextUtils.isEmpty(this.h)) {
                a((Class<? extends b>) n.class);
            } else if (!e()) {
                b(8, this.f);
            }
        }
    }

    public void onBridgeActivityDestroy() {
        super.onBridgeActivityDestroy();
    }

    public void onBridgeConfigurationChanged() {
        super.onBridgeConfigurationChanged();
    }

    public void a(b bVar) {
        a.a("HiappWebWizard", "Enter onCancel.");
        if (bVar instanceof n) {
            d();
        }
    }

    public void b(b bVar) {
        a.a("HiappWebWizard", "Enter onDoWork.");
        if (bVar instanceof n) {
            bVar.c();
            if (!e()) {
                b(8, this.f);
            }
        }
    }

    private boolean e() {
        Activity a = a();
        if (a == null || a.isFinishing() || this.c == null || TextUtils.isEmpty(this.j)) {
            return false;
        }
        try {
            Intent intent = new Intent("android.intent.action.VIEW", Uri.parse("https://a.vmall.com/app/" + this.j));
            intent.setFlags(268435456);
            a.startActivityForResult(intent, c());
            a(0, this.f);
            return true;
        } catch (ActivityNotFoundException unused) {
            a.d("HiappWebWizard", "can not find web to hold update hms apk");
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
            a.d("HiappWebWizard", "In showDialog, Failed to show the dialog." + e.getMessage());
        }
    }

    /* access modifiers changed from: package-private */
    public void d() {
        b(13, this.f);
    }

    public void onKeyUp(int i, KeyEvent keyEvent) {
        if (4 == i) {
            a.b("HiappWebWizard", "In onKeyUp, Call finish.");
            Activity a = a();
            if (a != null && !a.isFinishing()) {
                a.setResult(0, (Intent) null);
                a.finish();
            }
        }
    }
}
