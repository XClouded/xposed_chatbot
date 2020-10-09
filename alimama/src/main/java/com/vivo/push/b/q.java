package com.vivo.push.b;

import android.text.TextUtils;
import com.vivo.push.a;
import com.vivo.push.model.UnvarnishedMessage;

/* compiled from: OnMessageReceiveCommand */
public final class q extends x {
    protected UnvarnishedMessage a;

    public final String toString() {
        return "OnMessageCommand";
    }

    public q() {
        super(3);
    }

    /* access modifiers changed from: protected */
    public final void c(a aVar) {
        super.c(aVar);
        aVar.a("msg_v1", this.a.unpackToJson());
    }

    /* access modifiers changed from: protected */
    public final void d(a aVar) {
        super.d(aVar);
        String a2 = aVar.a("msg_v1");
        if (!TextUtils.isEmpty(a2)) {
            this.a = new UnvarnishedMessage(a2);
            this.a.setMsgId(f());
        }
    }

    public final String d() {
        if (this.a == null) {
            return null;
        }
        return this.a.unpackToJson();
    }

    public final UnvarnishedMessage e() {
        return this.a;
    }
}
