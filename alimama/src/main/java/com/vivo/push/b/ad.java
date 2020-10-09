package com.vivo.push.b;

import com.coloros.mcssdk.mode.CommandMessage;
import com.vivo.push.a;
import java.io.Serializable;
import java.util.ArrayList;

/* compiled from: TagCommand */
public final class ad extends c {
    private ArrayList<String> a;

    public final String toString() {
        return "TagCommand";
    }

    /* JADX INFO: super call moved to the top of the method (can break code semantics) */
    public ad(boolean z, String str, String str2, ArrayList<String> arrayList) {
        super(z ? 2004 : 2005, str, str2);
        this.a = arrayList;
    }

    /* access modifiers changed from: protected */
    public final void c(a aVar) {
        super.c(aVar);
        aVar.a(CommandMessage.TYPE_TAGS, (Serializable) this.a);
    }

    /* access modifiers changed from: protected */
    public final void d(a aVar) {
        super.d(aVar);
        this.a = aVar.b(CommandMessage.TYPE_TAGS);
    }
}
