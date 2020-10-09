package com.vivo.push.b;

import com.taobao.weex.WXEnvironment;
import com.uc.webview.export.cyclone.ErrorCode;
import com.vivo.push.a;
import com.vivo.push.y;

/* compiled from: DispatcherCommand */
public final class g extends y {
    private int a = -1;

    public static boolean a(int i) {
        return i > 0 && i <= 4;
    }

    public g() {
        super(ErrorCode.UCSERVICE_IMPL_UNSEVENZIP_IMPL_NOT_FOUND);
    }

    /* access modifiers changed from: protected */
    public final void c(a aVar) {
        if (a(this.a)) {
            aVar.a(WXEnvironment.ENVIRONMENT, this.a);
        }
    }

    /* access modifiers changed from: protected */
    public final void d(a aVar) {
        this.a = aVar.b(WXEnvironment.ENVIRONMENT, 1);
    }

    public final void b(int i) {
        this.a = i;
    }
}
