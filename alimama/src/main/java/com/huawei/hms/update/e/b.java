package com.huawei.hms.update.e;

import android.app.Activity;
import android.app.AlertDialog;
import android.content.Context;
import android.os.Build;
import com.huawei.hms.support.log.a;

/* compiled from: AbstractDialog */
public abstract class b {
    private AlertDialog a;
    private a b;

    /* access modifiers changed from: protected */
    public abstract AlertDialog a();

    private static int a(Context context) {
        if (context == null) {
            return 0;
        }
        return context.getResources().getIdentifier("androidhwext:style/Theme.Emui", (String) null, (String) null);
    }

    public void a(a aVar) {
        this.b = aVar;
        if (f() == null || f().isFinishing()) {
            a.d("AbstractDialog", "In show, The activity is null or finishing.");
            return;
        }
        this.a = a();
        this.a.setCanceledOnTouchOutside(false);
        this.a.setOnCancelListener(new c(this));
        this.a.show();
    }

    public void b() {
        if (this.a != null) {
            this.a.cancel();
        }
    }

    public void c() {
        if (this.a != null) {
            this.a.dismiss();
        }
    }

    /* access modifiers changed from: protected */
    public void d() {
        if (this.b != null) {
            this.b.a(this);
        }
    }

    /* access modifiers changed from: protected */
    public void e() {
        if (this.b != null) {
            this.b.b(this);
        }
    }

    /* access modifiers changed from: protected */
    public Activity f() {
        if (this.b != null) {
            return this.b.a();
        }
        return null;
    }

    /* access modifiers changed from: protected */
    public int g() {
        return (a((Context) f()) == 0 || Build.VERSION.SDK_INT < 16) ? 3 : 0;
    }
}
