package com.vivo.push.b;

import com.alibaba.motu.tbrest.rest.RestUrlWrapper;
import com.vivo.push.a;

/* compiled from: AppCommand */
public final class b extends c {
    private String a;
    private String b;
    private String c;
    private String d;

    /* JADX INFO: super call moved to the top of the method (can break code semantics) */
    public b(boolean z, String str) {
        super(z ? 2006 : 2007, (String) null, str);
    }

    public final void d() {
        this.c = null;
    }

    public final void e() {
        this.b = null;
    }

    public final void c(a aVar) {
        super.c(aVar);
        aVar.a("sdk_clients", this.a);
        aVar.a(RestUrlWrapper.FIELD_SDK_VERSION, 293);
        aVar.a("BaseAppCommand.EXTRA_APPID", this.c);
        aVar.a("BaseAppCommand.EXTRA_APPKEY", this.b);
        aVar.a("PUSH_REGID", this.d);
    }

    public final void d(a aVar) {
        super.d(aVar);
        this.a = aVar.a("sdk_clients");
        this.c = aVar.a("BaseAppCommand.EXTRA_APPID");
        this.b = aVar.a("BaseAppCommand.EXTRA_APPKEY");
        this.d = aVar.a("PUSH_REGID");
    }

    public final String toString() {
        return "AppCommand:" + b();
    }
}
