package com.vivo.push.b;

import com.vivo.push.a;
import com.vivo.push.y;
import com.xiaomi.mipush.sdk.Constants;

/* compiled from: StopServiceCommand */
public final class ac extends y {
    private String a;

    public final String toString() {
        return "StopServiceCommand";
    }

    public ac(String str) {
        super(2008);
        this.a = str;
    }

    public ac() {
        super(2008);
    }

    /* access modifiers changed from: protected */
    public final void c(a aVar) {
        aVar.a(Constants.PACKAGE_NAME, this.a);
    }

    /* access modifiers changed from: protected */
    public final void d(a aVar) {
        this.a = aVar.a(Constants.PACKAGE_NAME);
    }
}
