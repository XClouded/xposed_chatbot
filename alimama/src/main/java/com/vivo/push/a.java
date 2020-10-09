package com.vivo.push;

import android.os.Bundle;
import java.io.Serializable;
import java.util.ArrayList;

/* compiled from: BundleWapper */
public final class a {
    private Bundle a;
    private String b;
    private String c;

    public a(String str, String str2, Bundle bundle) {
        this.b = str;
        this.c = str2;
        this.a = bundle;
    }

    /* JADX WARNING: Code restructure failed: missing block: B:7:0x001b, code lost:
        if (android.text.TextUtils.isEmpty(r2) == false) goto L_0x001f;
     */
    /* Code decompiled incorrectly, please refer to instructions dump. */
    public static com.vivo.push.a a(android.content.Intent r5) {
        /*
            r0 = 0
            if (r5 != 0) goto L_0x000b
            java.lang.String r5 = "BundleWapper"
            java.lang.String r1 = "create error : intent is null"
            com.vivo.push.util.p.a((java.lang.String) r5, (java.lang.String) r1)
            return r0
        L_0x000b:
            android.os.Bundle r1 = r5.getExtras()
            if (r1 == 0) goto L_0x001e
            java.lang.String r2 = "client_pkgname"
            java.lang.String r2 = r1.getString(r2)
            boolean r3 = android.text.TextUtils.isEmpty(r2)
            if (r3 != 0) goto L_0x001e
            goto L_0x001f
        L_0x001e:
            r2 = r0
        L_0x001f:
            boolean r3 = android.text.TextUtils.isEmpty(r2)
            if (r3 == 0) goto L_0x002c
            java.lang.String r3 = "BundleWapper"
            java.lang.String r4 = "create warning: pkgName is null"
            com.vivo.push.util.p.b((java.lang.String) r3, (java.lang.String) r4)
        L_0x002c:
            java.lang.String r3 = r5.getPackage()
            boolean r4 = android.text.TextUtils.isEmpty(r3)
            if (r4 == 0) goto L_0x0054
            android.content.ComponentName r3 = r5.getComponent()
            if (r3 != 0) goto L_0x003e
        L_0x003c:
            r3 = r0
            goto L_0x0047
        L_0x003e:
            android.content.ComponentName r5 = r5.getComponent()
            java.lang.String r0 = r5.getPackageName()
            goto L_0x003c
        L_0x0047:
            boolean r5 = android.text.TextUtils.isEmpty(r3)
            if (r5 == 0) goto L_0x0054
            java.lang.String r5 = "BundleWapper"
            java.lang.String r0 = "create warning: targetPkgName is null"
            com.vivo.push.util.p.b((java.lang.String) r5, (java.lang.String) r0)
        L_0x0054:
            com.vivo.push.a r5 = new com.vivo.push.a
            r5.<init>(r2, r3, r1)
            return r5
        */
        throw new UnsupportedOperationException("Method not decompiled: com.vivo.push.a.a(android.content.Intent):com.vivo.push.a");
    }

    public final void a(String str, int i) {
        if (this.a == null) {
            this.a = new Bundle();
        }
        this.a.putInt(str, i);
    }

    public final void a(String str, long j) {
        if (this.a == null) {
            this.a = new Bundle();
        }
        this.a.putLong(str, j);
    }

    public final void a(String str, String str2) {
        if (this.a == null) {
            this.a = new Bundle();
        }
        this.a.putString(str, str2);
    }

    public final void a(String str, Serializable serializable) {
        if (this.a == null) {
            this.a = new Bundle();
        }
        this.a.putSerializable(str, serializable);
    }

    public final void a(String str, boolean z) {
        if (this.a == null) {
            this.a = new Bundle();
        }
        this.a.putBoolean(str, z);
    }

    public final void a(String str, ArrayList<String> arrayList) {
        if (this.a == null) {
            this.a = new Bundle();
        }
        this.a.putStringArrayList(str, arrayList);
    }

    public final String a(String str) {
        if (this.a == null) {
            return null;
        }
        return this.a.getString(str);
    }

    public final int b(String str, int i) {
        return this.a == null ? i : this.a.getInt(str, i);
    }

    public final ArrayList<String> b(String str) {
        if (this.a == null) {
            return null;
        }
        return this.a.getStringArrayList(str);
    }

    public final long b(String str, long j) {
        return this.a == null ? j : this.a.getLong(str, j);
    }

    public final Serializable c(String str) {
        if (this.a == null) {
            return null;
        }
        return this.a.getSerializable(str);
    }

    public final boolean d(String str) {
        if (this.a == null) {
            return false;
        }
        return this.a.getBoolean(str, false);
    }

    public final String a() {
        return this.b;
    }

    public final Bundle b() {
        return this.a;
    }
}
