package com.vivo.push.b;

import android.text.TextUtils;
import com.alibaba.motu.tbrest.rest.RestUrlWrapper;
import com.vivo.push.a;
import com.vivo.push.y;
import com.xiaomi.mipush.sdk.Constants;

/* compiled from: BaseAppCommand */
public class c extends y {
    private String a;
    private String b;
    private long c = -1;
    private int d = -1;
    private int e;
    private String f;

    public String toString() {
        return "BaseAppCommand";
    }

    public c(int i, String str, String str2) {
        super(i);
        this.a = str;
        this.b = str2;
    }

    public final int f() {
        return this.e;
    }

    public final void a(int i) {
        this.e = i;
    }

    public final void g() {
        this.f = null;
    }

    public final String h() {
        return this.a;
    }

    public final void b(String str) {
        this.a = str;
    }

    /* access modifiers changed from: protected */
    public void c(a aVar) {
        aVar.a("req_id", this.a);
        aVar.a(Constants.PACKAGE_NAME, this.b);
        aVar.a(RestUrlWrapper.FIELD_SDK_VERSION, 293);
        aVar.a("PUSH_APP_STATUS", this.d);
        if (!TextUtils.isEmpty(this.f)) {
            aVar.a("BaseAppCommand.EXTRA__HYBRIDVERSION", this.f);
        }
    }

    /* access modifiers changed from: protected */
    public void d(a aVar) {
        this.a = aVar.a("req_id");
        this.b = aVar.a(Constants.PACKAGE_NAME);
        this.c = aVar.b(RestUrlWrapper.FIELD_SDK_VERSION, 0);
        this.d = aVar.b("PUSH_APP_STATUS", 0);
        this.f = aVar.a("BaseAppCommand.EXTRA__HYBRIDVERSION");
    }
}
