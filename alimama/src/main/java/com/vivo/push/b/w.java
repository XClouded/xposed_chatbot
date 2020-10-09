package com.vivo.push.b;

import com.vivo.push.a;

/* compiled from: OnUndoMsgReceiveCommand */
public final class w extends x {
    private long a = -1;
    private int b;

    public final String toString() {
        return "OnUndoMsgCommand";
    }

    public w() {
        super(20);
    }

    public final long d() {
        return this.a;
    }

    /* access modifiers changed from: protected */
    public final void c(a aVar) {
        super.c(aVar);
        aVar.a("undo_msg_v1", this.a);
        aVar.a("undo_msg_type_v1", this.b);
    }

    /* access modifiers changed from: protected */
    public final void d(a aVar) {
        super.d(aVar);
        this.a = aVar.b("undo_msg_v1", this.a);
        this.b = aVar.b("undo_msg_type_v1", 0);
    }

    public final String e() {
        if (this.a != -1) {
            return String.valueOf(this.a);
        }
        return null;
    }
}
