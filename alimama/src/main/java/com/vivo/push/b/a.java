package com.vivo.push.b;

import com.coloros.mcssdk.mode.CommandMessage;
import java.util.ArrayList;

/* compiled from: AliasCommand */
public final class a extends c {
    private ArrayList<String> a;

    /* JADX INFO: super call moved to the top of the method (can break code semantics) */
    public a(boolean z, String str, String str2, ArrayList<String> arrayList) {
        super(z ? 2002 : 2003, str, str2);
        this.a = arrayList;
    }

    public final void c(com.vivo.push.a aVar) {
        super.c(aVar);
        aVar.a(CommandMessage.TYPE_TAGS, this.a);
    }

    public final void d(com.vivo.push.a aVar) {
        super.d(aVar);
        this.a = aVar.b(CommandMessage.TYPE_TAGS);
    }

    public final String toString() {
        return "AliasCommand:" + b();
    }
}
