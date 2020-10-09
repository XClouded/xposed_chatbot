package com.uc.webview.export.internal.setup;

import android.webkit.ValueCallback;
import com.uc.webview.export.extension.UCCore;
import com.uc.webview.export.internal.SDKFactory;
import com.uc.webview.export.internal.uc.startup.b;
import com.uc.webview.export.internal.utility.Log;
import com.uc.webview.export.internal.utility.h;
import java.util.ArrayList;
import java.util.List;

/* compiled from: U4Source */
final class u implements ValueCallback<l> {
    final /* synthetic */ o a;

    u(o oVar) {
        this.a = oVar;
    }

    public final /* synthetic */ void onReceiveValue(Object obj) {
        l lVar = (l) obj;
        if (SDKFactory.o != null) {
            SDKFactory.o.onReceiveValue(lVar.getException());
        }
        if (lVar.getException() != null) {
            UCSetupException unused = this.a.i = lVar.getException();
            UCSetupTask unused2 = this.a.j = lVar;
        }
        if (h.b()) {
            h.c(this.a.getContext());
        }
        Integer num = (Integer) this.a.mOptions.get(UCCore.OPTION_DELETE_CORE_POLICY);
        if (num != null && num.intValue() != 0 && (lVar instanceof av) && ((lVar.getException().errCode() == 1008 && (num.intValue() & 1) != 0) || ((lVar.getException().errCode() == 1011 && (num.intValue() & 8) != 0) || ((lVar.getException().errCode() == 3007 && (num.intValue() & 2) != 0) || ((lVar.getException().errCode() == 3005 && (num.intValue() & 16) != 0) || ((lVar.getException().errCode() == 4005 && (num.intValue() & 4) != 0) || (num.intValue() & 32) != 0)))))) {
            if (this.a.m == null) {
                List unused3 = this.a.m = new ArrayList();
            }
            this.a.m.add((av) lVar);
        }
        Log.d("SdkSetupTask", "mExceptionCB mExceptionTasks: " + o.c);
        if (o.c.size() > 0) {
            if (((UCSetupTask) o.c.pop()) != null) {
                ((UCSetupTask) o.c.pop()).start();
            } else {
                this.a.b((l) new ay()).start();
            }
        } else if (this.a.d != null) {
            ((l) ((l) ((l) this.a.d.setParent(this.a)).onEvent("success", this.a.o)).onEvent(UCCore.EVENT_EXCEPTION, this.a.p)).start();
            l unused4 = this.a.d = null;
        } else {
            this.a.setException(lVar.getException());
        }
        b.a(196, this.a.i != null ? this.a.i.toString() : "");
    }
}
