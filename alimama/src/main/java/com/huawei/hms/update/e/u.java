package com.huawei.hms.update.e;

import android.os.Bundle;
import android.os.Handler;
import android.os.Message;

/* compiled from: SilentUpdateWizard */
class u extends Handler {
    final /* synthetic */ t a;

    u(t tVar) {
        this.a = tVar;
    }

    public void handleMessage(Message message) {
        Bundle bundle = (Bundle) message.obj;
        switch (message.what) {
            case 101:
                this.a.a(bundle);
                return;
            case 102:
                this.a.b(bundle);
                return;
            case 103:
                this.a.c(bundle);
                return;
            default:
                return;
        }
    }
}
