package com.alipay.sdk.app;

import android.app.Activity;
import android.content.res.Configuration;
import android.os.Bundle;
import com.alipay.sdk.app.statistic.a;
import com.alipay.sdk.app.statistic.c;
import com.alipay.sdk.util.n;
import com.alipay.sdk.widget.g;
import com.alipay.sdk.widget.h;
import com.alipay.sdk.widget.j;

public class H5PayActivity extends Activity {
    private g a;
    private String b;
    private String c;
    private String d;
    private String e;
    private boolean f;
    private String g;

    /* access modifiers changed from: protected */
    public void onCreate(Bundle bundle) {
        b();
        super.onCreate(bundle);
        try {
            Bundle extras = getIntent().getExtras();
            this.b = extras.getString("url", (String) null);
            if (!n.f(this.b)) {
                finish();
                return;
            }
            this.d = extras.getString("cookie", (String) null);
            this.c = extras.getString("method", (String) null);
            this.e = extras.getString("title", (String) null);
            this.g = extras.getString("version", "v1");
            this.f = extras.getBoolean("backisexit", false);
            try {
                if ("v2".equals(this.g)) {
                    j jVar = new j(this);
                    setContentView(jVar);
                    jVar.a(this.e, this.c, this.f);
                    jVar.a(this.b);
                    this.a = jVar;
                    return;
                }
                this.a = new h(this);
                setContentView(this.a);
                this.a.a(this.b, this.d);
                this.a.a(this.b);
            } catch (Throwable th) {
                a.a(c.b, "GetInstalledAppEx", th);
                finish();
            }
        } catch (Exception unused) {
            finish();
        }
    }

    private void b() {
        try {
            super.requestWindowFeature(1);
        } catch (Throwable th) {
            com.alipay.sdk.util.c.a(th);
        }
    }

    public void onBackPressed() {
        if (this.a instanceof h) {
            this.a.b();
            return;
        }
        if (!this.a.b()) {
            super.onBackPressed();
        }
        j.a(j.c());
        finish();
    }

    public void finish() {
        a();
        super.finish();
    }

    /* JADX WARNING: Exception block dominator not found, dom blocks: [] */
    /* JADX WARNING: Missing exception handler attribute for start block: B:5:0x0009 */
    /* Code decompiled incorrectly, please refer to instructions dump. */
    public void a() {
        /*
            r2 = this;
            java.lang.Object r0 = com.alipay.sdk.app.PayTask.a
            monitor-enter(r0)
            r0.notify()     // Catch:{ Exception -> 0x0009 }
            goto L_0x0009
        L_0x0007:
            r1 = move-exception
            goto L_0x000b
        L_0x0009:
            monitor-exit(r0)     // Catch:{ all -> 0x0007 }
            return
        L_0x000b:
            monitor-exit(r0)     // Catch:{ all -> 0x0007 }
            throw r1
        */
        throw new UnsupportedOperationException("Method not decompiled: com.alipay.sdk.app.H5PayActivity.a():void");
    }

    public void onConfigurationChanged(Configuration configuration) {
        super.onConfigurationChanged(configuration);
    }

    /* access modifiers changed from: protected */
    public void onDestroy() {
        super.onDestroy();
        this.a.a();
    }
}
