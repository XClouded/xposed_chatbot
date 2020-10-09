package com.vivo.push.b;

import com.vivo.push.a;
import com.vivo.push.y;

/* compiled from: MsgArriveCommand */
public final class j extends y {
    private String a;

    public j() {
        super(2013);
    }

    public j(String str) {
        this();
        this.a = str;
    }

    /* access modifiers changed from: protected */
    public final void c(a aVar) {
        aVar.a("MsgArriveCommand.MSG_TAG", this.a);
    }

    /* access modifiers changed from: protected */
    public final void d(a aVar) {
        this.a = aVar.a("MsgArriveCommand.MSG_TAG");
    }
}
