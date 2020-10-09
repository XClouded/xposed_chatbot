package com.huawei.hms.b;

import android.app.Activity;
import android.app.AlertDialog;
import android.content.Context;
import android.os.Build;

/* compiled from: AbstractDialog */
public abstract class a {
    private Activity a;
    private AlertDialog b;
    private C0010a c;

    /* renamed from: com.huawei.hms.b.a$a  reason: collision with other inner class name */
    /* compiled from: AbstractDialog */
    public interface C0010a {
        void a(a aVar);

        void b(a aVar);
    }

    /* access modifiers changed from: protected */
    public abstract String a(Context context);

    /* access modifiers changed from: protected */
    public abstract String b(Context context);

    /* access modifiers changed from: protected */
    public abstract String c(Context context);

    /* access modifiers changed from: protected */
    public abstract String d(Context context);

    private static int e(Context context) {
        if (context == null) {
            return 0;
        }
        return context.getResources().getIdentifier("androidhwext:style/Theme.Emui", (String) null, (String) null);
    }

    /* access modifiers changed from: protected */
    public AlertDialog a(Activity activity) {
        AlertDialog.Builder builder = new AlertDialog.Builder(e(), f());
        String c2 = c(activity);
        if (c2 != null) {
            builder.setTitle(c2);
        }
        String a2 = a((Context) activity);
        if (a2 != null) {
            builder.setMessage(a2);
        }
        String b2 = b(activity);
        if (b2 != null) {
            builder.setPositiveButton(b2, new b(this));
        }
        String d = d(activity);
        if (d != null) {
            builder.setNegativeButton(d, new c(this));
        }
        return builder.create();
    }

    public void a(Activity activity, C0010a aVar) {
        this.a = activity;
        this.c = aVar;
        if (this.a == null || this.a.isFinishing()) {
            com.huawei.hms.support.log.a.d("AbstractDialog", "In show, The activity is null or finishing.");
            return;
        }
        this.b = a(this.a);
        this.b.setCanceledOnTouchOutside(false);
        this.b.setOnCancelListener(new d(this));
        this.b.setOnKeyListener(new e(this));
        this.b.show();
    }

    public void a() {
        if (this.b != null) {
            this.b.cancel();
        }
    }

    public void b() {
        if (this.b != null) {
            this.b.dismiss();
        }
    }

    /* access modifiers changed from: protected */
    public void c() {
        if (this.c != null) {
            this.c.b(this);
        }
    }

    /* access modifiers changed from: protected */
    public void d() {
        if (this.c != null) {
            this.c.a(this);
        }
    }

    /* access modifiers changed from: protected */
    public Activity e() {
        return this.a;
    }

    /* access modifiers changed from: protected */
    public int f() {
        return (e(this.a) == 0 || Build.VERSION.SDK_INT < 16) ? 3 : 0;
    }
}
