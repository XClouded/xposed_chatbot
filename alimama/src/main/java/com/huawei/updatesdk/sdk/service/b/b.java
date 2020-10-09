package com.huawei.updatesdk.sdk.service.b;

import android.content.Intent;
import android.os.Bundle;
import android.util.Log;
import com.huawei.updatesdk.sdk.a.c.a.a.a;

public final class b {
    private Intent a;

    private b(Intent intent) {
        this.a = intent;
    }

    public static b a(Intent intent) {
        return new b(intent);
    }

    public int a(String str, int i) {
        if (d()) {
            try {
                return this.a.getIntExtra(str, i);
            } catch (Exception unused) {
                Log.e("SecureIntent", "getIntExtra exception!");
            }
        }
        return i;
    }

    public Bundle a() {
        if (d()) {
            return this.a.getExtras();
        }
        return null;
    }

    public Bundle a(String str) {
        if (!d()) {
            return null;
        }
        try {
            return this.a.getBundleExtra(str);
        } catch (Exception unused) {
            a.d("SecureIntent", "getBundleExtra exception!");
            return null;
        }
    }

    public boolean a(String str, boolean z) {
        if (d()) {
            try {
                return this.a.getBooleanExtra(str, z);
            } catch (Exception unused) {
                Log.e("SecureIntent", "getBooleanExtra exception!");
            }
        }
        return z;
    }

    /* JADX WARNING: Code restructure failed: missing block: B:2:0x0008, code lost:
        r0 = r2.a.getAction();
     */
    /* Code decompiled incorrectly, please refer to instructions dump. */
    public java.lang.String b() {
        /*
            r2 = this;
            java.lang.String r0 = ""
            boolean r1 = r2.d()
            if (r1 == 0) goto L_0x0012
            android.content.Intent r0 = r2.a
            java.lang.String r0 = r0.getAction()
            if (r0 != 0) goto L_0x0012
            java.lang.String r0 = ""
        L_0x0012:
            return r0
        */
        throw new UnsupportedOperationException("Method not decompiled: com.huawei.updatesdk.sdk.service.b.b.b():java.lang.String");
    }

    public String b(String str) {
        if (!d()) {
            return "";
        }
        try {
            return this.a.getStringExtra(str);
        } catch (Exception unused) {
            Log.e("SecureIntent", "getStringExtra exception!");
            return "";
        }
    }

    public Intent c() {
        return this.a;
    }

    public boolean d() {
        return this.a != null;
    }
}
