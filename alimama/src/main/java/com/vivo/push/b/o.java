package com.vivo.push.b;

import com.vivo.push.a;
import java.util.ArrayList;

/* compiled from: OnListTagReceiveCommand */
public final class o extends u {
    private ArrayList<String> a;

    public final String toString() {
        return "OnListTagCommand";
    }

    public o() {
        super(8);
    }

    public final ArrayList<String> d() {
        return this.a;
    }

    /* access modifiers changed from: protected */
    public final void c(a aVar) {
        super.c(aVar);
        aVar.a("tags_list", this.a);
    }

    /* access modifiers changed from: protected */
    public final void d(a aVar) {
        super.d(aVar);
        this.a = aVar.b("tags_list");
    }
}
